package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.dataholder.DataHolderClass;
import com.datamodel.OrderInfoModel;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class MyCartFragment extends Fragment implements OnClickListener {

	private static final String TAG = "MyCartFragment";
	private RelativeLayout mView;
	private ImageButton continue_shoping, checkout_cart;
	private TextView shiping_fee_tag, shiping_fee_amount, payable_amount_tag,
			payable_amount, cart_summery_tag, item_count_tag;
	SharedPreferences mPref;
	private String mToken;
	private String mUserId;
	Typeface mTypeface;
	public static ArrayList<OrderInfoModel> Orderinfo = new ArrayList<OrderInfoModel>();
	private ListView mOrderListView;
	MyCartAdapter mOrderAdapter;
	private String shipping_fee, total_price, _pk;
	private String _updateResponse;

	int _display_items = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mView == null) {
			mView = (RelativeLayout) inflater.inflate(
					R.layout.activity_my_cart_screen, container, false);
			shiping_fee_tag = (TextView) mView
					.findViewById(R.id.shiping_fee_tag);
			shiping_fee_amount = (TextView) mView
					.findViewById(R.id.shiping_fee_amount);
			payable_amount_tag = (TextView) mView
					.findViewById(R.id.payable_amount_tag);
			payable_amount = (TextView) mView.findViewById(R.id.payable_amount);
			cart_summery_tag = (TextView) mView
					.findViewById(R.id.cart_summery_tag);
			item_count_tag = (TextView) mView.findViewById(R.id.item_count_tag);
			mOrderListView = (ListView) mView.findViewById(R.id.mycartlist);
			checkout_cart = (ImageButton) mView
					.findViewById(R.id.checkout_cart);
			checkout_cart.setOnClickListener(this);
			continue_shoping = (ImageButton) mView
					.findViewById(R.id.continue_shoping);
			continue_shoping.setOnClickListener(this);
		}

		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (mTypeface == null) {
			mTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/georgia.ttf");
			shiping_fee_tag.setTypeface(mTypeface, Typeface.NORMAL);
			shiping_fee_amount.setTypeface(mTypeface, Typeface.NORMAL);
			payable_amount_tag.setTypeface(mTypeface, Typeface.NORMAL);
			payable_amount.setTypeface(mTypeface, Typeface.NORMAL);
			cart_summery_tag.setTypeface(mTypeface, Typeface.NORMAL);
		}
		if (mPref == null) {
			mPref = getActivity().getApplicationContext().getSharedPreferences(
					"mypref", 0);
			mUserId = mPref.getString("ID", null);
			mToken = mPref.getString("TOKEN", null);
		}
		if (mOrderAdapter == null) {
			mOrderAdapter = new MyCartAdapter(getActivity(), Orderinfo);
			mOrderListView.setAdapter(mOrderAdapter);
		}
		new GetAllCarts().execute();
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(getActivity());
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": carts/" + mUserId + "/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((ViewGroup) mView.getParent()).removeView(mView);
	}

	private class GetAllCarts extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			_display_items = 0;
			mProgressHUD = ProgressHUD.show(getActivity(), "Loading", true,
					true, this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			int displayHeight = displaymetrics.heightPixels;
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams wmlp = mProgressHUD.getWindow()
					.getAttributes();
			wmlp.y = displayHeight / 4;
			mProgressHUD.getWindow().setAttributes(wmlp);
			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected String doInBackground(String... arg0) {
			Orderinfo.clear();
			String url = "https://www.brandsfever.com/api/v5/carts/?user_id="
					+ mUserId + "&token=" + mToken;
			getCarts(url);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (Orderinfo.size() > 0) {

				item_count_tag.setText("" + _display_items + " " + "items(s)");
				item_count_tag.setTypeface(mTypeface, Typeface.NORMAL);
				shiping_fee_amount.setText(shipping_fee.replace("GD", "$"));

				payable_amount.setText(total_price.replace("GD", "$"));
				DataHolderClass.getInstance().setFinal_cart_pk(_pk);
				mOrderAdapter.notifyDataSetChanged();

			} else {
				item_count_tag.setText("0 item");
				item_count_tag.setTypeface(mTypeface, Typeface.NORMAL);
				shiping_fee_amount.setText("S$0");
				payable_amount.setText("S$0");

				responsePopup("Your cart \n is empty");
			}

			mProgressHUD.dismiss();
		}
	}

	public void getCarts(String url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpclient = HttpsClient.getNewHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			int responsecode = httpresponse.getStatusLine().getStatusCode();

			if (responsecode == 200) {
				InputStream inputstream = httpresponse.getEntity().getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				String content = total.toString();
				JSONObject obj = new JSONObject(content);
				String ret = obj.getString("ret");
				String msg = obj.getString("msg");
				if (ret.equals("0") && msg.equalsIgnoreCase("ok")) {
					JSONArray carts = obj.getJSONArray("carts");
					for (int i = 0; i < carts.length(); i++) {
						JSONObject _obj = carts.getJSONObject(i);
						_pk = _obj.getString("pk");
						shipping_fee = _obj.getString("shipping_fee");
						total_price = _obj.getString("total_price");

						JSONArray _getcartitems = _obj
								.getJSONArray("cart_items");
						for (int j = 0; j < _getcartitems.length(); j++) {
							JSONObject _objs = _getcartitems.getJSONObject(j);
							String product_image = _objs
									.getString("product_image");
							String _totalprice = _objs.getString("total_price");
							String product_item_pk = _objs
									.getString("product_item_pk");
							String sales_price = _objs.getString("sales_price");
							String pk = _objs.getString("pk");
							String quantity = _objs.getString("quantity");
							_display_items = (_display_items + Integer
									.valueOf(quantity));
							String product_name = _objs
									.getString("product_name");
							String campaign_pk = _objs.getString("campaign_pk");

							OrderInfoModel model = new OrderInfoModel();
							model.setProduct_image(product_image);
							model.setTotal_price(_totalprice);
							model.setProduct_item_pk(product_item_pk);
							model.setSales_price(sales_price);
							model.setPk(pk);
							model.setQuantity(quantity);
							model.setProduct_name(product_name);
							model.setCampaign_pk(campaign_pk);
							Orderinfo.add(model);
						}
					}

				} else if (ret.equals("003") && msg.equals("login required")) {
					responsePopup("Please login");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class MyCartAdapter extends BaseAdapter {
		Context _mcontext = null;
		LayoutInflater inflater;
		ArrayList<OrderInfoModel> data;

		public MyCartAdapter(Context context,
				ArrayList<OrderInfoModel> arraylist) {
			this._mcontext = context;
			this.data = arraylist;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			TextView Quantity_tag, setQuantity_text, total_tag, _total_amount, product_name, unitprice_tag, unit_price;
			Button remove_text_click, add_quantity, subtract_quantity;
			if (convertView == null) {
				if (DataHolderClass.getInstance().get_deviceInch() <= 7) {
					inflater = (LayoutInflater) _mcontext
							.getApplicationContext().getSystemService(
									Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(
							R.layout.cart_inflator_phone, parent, false);

				} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
						&& DataHolderClass.getInstance().get_deviceInch() < 9) {
					inflater = (LayoutInflater) _mcontext
							.getApplicationContext().getSystemService(
									Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(
							R.layout.seven_inch_cart_inflator, parent, false);
				}

				else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
					inflater = (LayoutInflater) _mcontext
							.getApplicationContext().getSystemService(
									Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.my_cart_inflator,
							parent, false);
				}
			}
			Quantity_tag = (TextView) convertView.findViewById(R.id.Quantity_tag);
			Quantity_tag.setTypeface(mTypeface);

			setQuantity_text = (TextView) convertView
					.findViewById(R.id.setQuantity_text);
			setQuantity_text.setTypeface(mTypeface);

			total_tag = (TextView) convertView.findViewById(R.id.total_tag);
			total_tag.setTypeface(mTypeface);

			_total_amount = (TextView) convertView.findViewById(R.id._total_amount);
			_total_amount.setTypeface(mTypeface);

			product_name = (TextView) convertView.findViewById(R.id.set_product_name);
			product_name.setTypeface(mTypeface);

			unitprice_tag = (TextView) convertView.findViewById(R.id.unitprice_tag);
			unitprice_tag.setTypeface(mTypeface);

			unit_price = (TextView) convertView.findViewById(R.id.set_unit_price);
			unit_price.setTypeface(mTypeface);

			remove_text_click = (Button) convertView
					.findViewById(R.id.remove_text_click);
			remove_text_click.setTypeface(mTypeface);

			add_quantity = (Button) convertView.findViewById(R.id.add_quantity);
			add_quantity.setTypeface(mTypeface);

			subtract_quantity = (Button) convertView
					.findViewById(R.id.subtract_quantity);
			subtract_quantity.setTypeface(mTypeface);

			OrderInfoModel obj = data.get(position);
			remove_text_click.setTag(obj);
			add_quantity.setTag(obj);
			subtract_quantity.setTag(obj);
			setQuantity_text.setText(obj.getQuantity());
			product_name.setText(obj.getProduct_name());
			String _pic = "https:" + obj.getProduct_image();
			unit_price.setText(obj.getSales_price().replace("GD", "$"));
			_total_amount.setText(obj.getTotal_price().replace("GD", "$"));
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.my_oder_img);
			AQuery aq = new AQuery(_mcontext);
			aq.id(imageView).image(_pic);

			remove_text_click.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					OrderInfoModel obj = (OrderInfoModel) v.getTag();
					final String _sendAction = "remove_product";
					final String _sendquantity = "";
					final String _senditempk = obj.getProduct_item_pk();
					View view = View.inflate(_mcontext, R.layout.call_settings,
							null);
					final PopupWindow pwindo = new PopupWindow(view,
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT, true);
					pwindo.showAtLocation(view, Gravity.CENTER, 0, 0);

					TextView _message = (TextView) view
							.findViewById(R.id.textview_popup);
					_message.setText("Are you sure you want to delete this \n product?");

					Button cancel_password = (Button) view
							.findViewById(R.id.cancel_password);
					cancel_password.setTypeface(mTypeface, Typeface.NORMAL);
					cancel_password.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pwindo.dismiss();

						}
					});

					Button reset_password = (Button) view
							.findViewById(R.id.reset_password);
					reset_password.setText("OK");
					reset_password.setTypeface(mTypeface, Typeface.BOLD);
					reset_password.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pwindo.dismiss();
							new RemoveOrUpdateProduct(_sendAction,
									_sendquantity, _senditempk).execute();
						}
					});

				}
			});

			add_quantity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String _sendAction = "update_product";
					OrderInfoModel obj = (OrderInfoModel) v.getTag();
					int temp = Integer.parseInt(obj.getQuantity());
					int temap1 = temp + 1;
					String _sendquantity = String.valueOf(temap1);
					String _senditempk = obj.getProduct_item_pk();

					new RemoveOrUpdateProduct(_sendAction, _sendquantity,
							_senditempk).execute();

				}
			});

			subtract_quantity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String _sendAction = "update_product";
					OrderInfoModel obj = (OrderInfoModel) v.getTag();
					int temp = Integer.parseInt(obj.getQuantity());
					int temap1 = temp - 1;
					String _sendquantity = String.valueOf(temap1);
					String _senditempk = obj.getProduct_item_pk();
					if (_sendquantity.equals("0")) {
					} else {
						new RemoveOrUpdateProduct(_sendAction, _sendquantity,
								_senditempk).execute();

					}
				}
			});

			return convertView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.checkout_cart:
			if (Orderinfo.size() > 0) {
				Intent checkout = new Intent(getActivity(),
						OrderDeliveryActiviy.class);
				startActivity(checkout);
				getActivity().overridePendingTransition(
						R.anim.push_out_to_right, R.anim.push_out_to_left);
			} else {
				responsePopup("your cart is empty");
			}

			break;
		case R.id.continue_shoping:
			Intent continueshop = new Intent(getActivity(),
					ProductDisplay.class);
			startActivity(continueshop);
			break;

		default:
			break;
		}

	}

	public class RemoveOrUpdateProduct extends
			AsyncTask<String, String, String> implements OnCancelListener {
		ProgressHUD mProgressHUD;
		String _urPK;
		String _urACTION;
		String _urQUAN;

		public RemoveOrUpdateProduct(String _action, String _quan,
				String _sendpk) {
			_urACTION = _action;
			_urPK = _sendpk;
			_urQUAN = _quan;
		}

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(getActivity(), "Loading", true,
					true, this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
			int displayHeight = displaymetrics.heightPixels;
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams wmlp = mProgressHUD.getWindow()
					.getAttributes();
			wmlp.y = displayHeight / 4;
			mProgressHUD.getWindow().setAttributes(wmlp);
			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = "https://www.brandsfever.com/api/v5/carts/";
			String userid = mUserId;
			String token = mToken;
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			BasicNameValuePair useridPair = new BasicNameValuePair("user_id",
					userid);
			BasicNameValuePair tokenPair = new BasicNameValuePair("token",
					token);
			BasicNameValuePair actionPair = new BasicNameValuePair("action",
					_urACTION);
			BasicNameValuePair quantityPair = new BasicNameValuePair(
					"quantity", _urQUAN);
			BasicNameValuePair itempkPair = new BasicNameValuePair(
					"product_item_pk", _urPK);
			_namevalueList.add(useridPair);
			_namevalueList.add(tokenPair);
			_namevalueList.add(actionPair);
			_namevalueList.add(quantityPair);
			_namevalueList.add(itempkPair);
			_updateResponse = updateProduct(url, _namevalueList);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobj = new JSONObject(_updateResponse);
				String _Ret = jobj.getString("ret");
				if (_Ret.equals("0")) {
					Orderinfo.clear();
					mOrderAdapter.notifyDataSetChanged();
					new GetAllCarts().execute();
				} else {
					responsePopup(jobj.getString("msg"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			mProgressHUD.dismiss();
		}
	}

	public String updateProduct(String url, List<NameValuePair> _namevalueList) {
		String _Response = null;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpPost _httppost = new HttpPost(url);
		try {
			_httppost.setEntity(new UrlEncodedFormEntity(_namevalueList,
					HTTP.UTF_8));
			HttpResponse _httpresponse = _httpclient.execute(_httppost);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			if (_responsecode == 200) {
				InputStream _inputstream = _httpresponse.getEntity()
						.getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						_inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				_Response = total.toString();
			} else {
				_Response = "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _Response;
	}

	public void responsePopup(String msg) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.error_popop,
				(ViewGroup) getView().findViewById(R.id.relativeLayout1));
		TextView msgView = (TextView) view.findViewById(R.id._seterrormsg);
		msgView.setText(msg);
		Toast toast = new Toast(getActivity());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}
}