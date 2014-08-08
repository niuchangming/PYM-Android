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
	private ImageButton continue_shoping, checkout_cart;
	private TextView shiping_fee_tag, shiping_fee_amount, payable_amount_tag,
			payable_amount, cart_summery_tag, item_count_tag;
	SharedPreferences _mypref;
	private String _getToken = "";
	private String _getuserId = "";
	Typeface _font;
	public static ArrayList<OrderInfoModel> Orderinfo = new ArrayList<OrderInfoModel>();
	private ListView mOrderListView;
	MyCartAdapter mOrderAdapter;
	private String shipping_fee, total_price, _pk;
	private String _updateResponse;
	private String _msg;
	private TextView _seterrormsg;
	int color, colors;

	int _display_items = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.activity_my_cart_screen, container, false);

		_font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getActivity().getApplicationContext().getSharedPreferences(
				"mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);
		shiping_fee_tag = (TextView) view.findViewById(R.id.shiping_fee_tag);
		shiping_fee_tag.setTypeface(_font, Typeface.NORMAL);
		shiping_fee_amount = (TextView) view
				.findViewById(R.id.shiping_fee_amount);
		shiping_fee_amount.setTypeface(_font, Typeface.NORMAL);
		payable_amount_tag = (TextView) view
				.findViewById(R.id.payable_amount_tag);
		payable_amount_tag.setTypeface(_font, Typeface.NORMAL);
		payable_amount = (TextView) view.findViewById(R.id.payable_amount);
		payable_amount.setTypeface(_font, Typeface.NORMAL);

		cart_summery_tag = (TextView) view.findViewById(R.id.cart_summery_tag);
		cart_summery_tag.setTypeface(_font, Typeface.NORMAL);

		item_count_tag = (TextView) view.findViewById(R.id.item_count_tag);

		mOrderListView = (ListView) view.findViewById(R.id.mycartlist);
		mOrderAdapter = new MyCartAdapter(getActivity(), Orderinfo);
		mOrderListView.setAdapter(mOrderAdapter);
		checkout_cart = (ImageButton) view.findViewById(R.id.checkout_cart);
		checkout_cart.setOnClickListener(this);

		continue_shoping = (ImageButton) view
				.findViewById(R.id.continue_shoping);
		continue_shoping.setOnClickListener(this);

		new GetAllCarts().execute();

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(getActivity());
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": carts/" + _getuserId + "/?device=2");
		tracker.send(MapBuilder.createAppView().build());
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
			String _url = "https://www.brandsfever.com/api/v5/carts/?user_id="
					+ _getuserId + "&token=" + _getToken;
			GetCarts(_url);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (Orderinfo.size() > 0) {

				item_count_tag.setText("" + _display_items + " " + "items(s)");
				item_count_tag.setTypeface(_font, Typeface.NORMAL);
				shiping_fee_amount.setText(shipping_fee.replace("GD", "$"));

				payable_amount.setText(total_price.replace("GD", "$"));
				DataHolderClass.getInstance().setFinal_cart_pk(_pk);
				mOrderAdapter.notifyDataSetChanged();

			} else {
				item_count_tag.setText("0 item");
				item_count_tag.setTypeface(_font, Typeface.NORMAL);
				shiping_fee_amount.setText("S$0");
				payable_amount.setText("S$0");

				_msg = "Your cart \n is empty";
				responsePopup();
			}

			mProgressHUD.dismiss();
		}
	}

	public void GetCarts(String url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(url);
		try {
			HttpResponse _httpresponse = _httpclient.execute(_httpget);
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
				String _content = total.toString();
				try {
					JSONObject obj = new JSONObject(_content);
					String ret = obj.getString("ret");
					String mesg = obj.getString("msg");
					if (ret.equals("0") && mesg.equalsIgnoreCase("ok")) {
						JSONArray _getcart = obj.getJSONArray("carts");
						for (int i = 0; i < _getcart.length(); i++) {
							JSONObject _obj = _getcart.getJSONObject(i);
							_pk = _obj.getString("pk");
							shipping_fee = _obj.getString("shipping_fee");
							total_price = _obj.getString("total_price");

							JSONArray _getcartitems = _obj
									.getJSONArray("cart_items");
							for (int j = 0; j < _getcartitems.length(); j++) {
								JSONObject _objs = _getcartitems
										.getJSONObject(j);
								String product_image = _objs
										.getString("product_image");
								String _totalprice = _objs
										.getString("total_price");
								String product_item_pk = _objs
										.getString("product_item_pk");
								String sales_price = _objs
										.getString("sales_price");
								String pk = _objs.getString("pk");
								String quantity = _objs.getString("quantity");
								_display_items = (_display_items + Integer
										.valueOf(quantity));
								String product_name = _objs
										.getString("product_name");
								String campaign_pk = _objs
										.getString("campaign_pk");

								OrderInfoModel _model = new OrderInfoModel();
								_model.setProduct_image(product_image);
								_model.setTotal_price(_totalprice);
								_model.setProduct_item_pk(product_item_pk);
								_model.setSales_price(sales_price);
								_model.setPk(pk);
								_model.setQuantity(quantity);
								_model.setProduct_name(product_name);
								_model.setCampaign_pk(campaign_pk);
								Orderinfo.add(_model);
							}
						}

					} else if (ret.equals("003")
							&& mesg.equals("login required")) {
						_msg = "You are already logged in \n from another device";
					} else {
						_msg = mesg;
					}
				} catch (Exception e) {
					e.printStackTrace();
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
		View _view;

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
			if (DataHolderClass.getInstance().get_deviceInch() <= 7) {
				inflater = (LayoutInflater) _mcontext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				_view = inflater.inflate(R.layout.cart_inflator_phone, parent,
						false);

			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
					&& DataHolderClass.getInstance().get_deviceInch() < 9) {
				inflater = (LayoutInflater) _mcontext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				_view = inflater.inflate(R.layout.seven_inch_cart_inflator,
						parent, false);
			}

			else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
				inflater = (LayoutInflater) _mcontext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				_view = inflater.inflate(R.layout.my_cart_inflator, parent,
						false);
			}

			Quantity_tag = (TextView) _view.findViewById(R.id.Quantity_tag);
			Quantity_tag.setTypeface(_font);

			setQuantity_text = (TextView) _view
					.findViewById(R.id.setQuantity_text);
			setQuantity_text.setTypeface(_font);

			total_tag = (TextView) _view.findViewById(R.id.total_tag);
			total_tag.setTypeface(_font);

			_total_amount = (TextView) _view.findViewById(R.id._total_amount);
			_total_amount.setTypeface(_font);

			product_name = (TextView) _view.findViewById(R.id.set_product_name);
			product_name.setTypeface(_font);

			unitprice_tag = (TextView) _view.findViewById(R.id.unitprice_tag);
			unitprice_tag.setTypeface(_font);

			unit_price = (TextView) _view.findViewById(R.id.set_unit_price);
			unit_price.setTypeface(_font);

			remove_text_click = (Button) _view
					.findViewById(R.id.remove_text_click);
			remove_text_click.setTypeface(_font);

			add_quantity = (Button) _view.findViewById(R.id.add_quantity);
			add_quantity.setTypeface(_font);

			subtract_quantity = (Button) _view
					.findViewById(R.id.subtract_quantity);
			subtract_quantity.setTypeface(_font);

			OrderInfoModel obj = data.get(position);
			remove_text_click.setTag(obj);
			add_quantity.setTag(obj);
			subtract_quantity.setTag(obj);
			setQuantity_text.setText(obj.getQuantity());
			product_name.setText(obj.getProduct_name());
			String _pic = "https:" + obj.getProduct_image();
			unit_price.setText(obj.getSales_price().replace("GD", "$"));
			_total_amount.setText(obj.getTotal_price().replace("GD", "$"));
			ImageView imageView = (ImageView) _view
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
					cancel_password.setTypeface(_font, Typeface.NORMAL);
					cancel_password.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pwindo.dismiss();

						}
					});

					Button reset_password = (Button) view
							.findViewById(R.id.reset_password);
					reset_password.setText("OK");
					reset_password.setTypeface(_font, Typeface.BOLD);
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

			return _view;
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
				getActivity().overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			} else {
				_msg = "your cart is empty";
				responsePopup();
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
			String _url = "https://www.brandsfever.com/api/v5/carts/";
			String _userid = _getuserId;
			String _token = _getToken;
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			BasicNameValuePair userid = new BasicNameValuePair("user_id",
					_userid);
			BasicNameValuePair token = new BasicNameValuePair("token", _token);
			BasicNameValuePair action = new BasicNameValuePair("action",
					_urACTION);
			BasicNameValuePair quantity = new BasicNameValuePair("quantity",
					_urQUAN);
			BasicNameValuePair itempk = new BasicNameValuePair(
					"product_item_pk", _urPK);
			_namevalueList.add(userid);
			_namevalueList.add(token);
			_namevalueList.add(action);
			_namevalueList.add(quantity);
			_namevalueList.add(itempk);
			_updateResponse = _UPDATEProduct(_url, _namevalueList);
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
					_msg = "Removed from cart!";
					responsePopup();
				} else {
					_msg = jobj.getString("msg");
					responsePopup();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			mProgressHUD.dismiss();
		}
	}

	public String _UPDATEProduct(String url, List<NameValuePair> _namevalueList) {
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

	public void responsePopup() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.error_popop,
				(ViewGroup) getView().findViewById(R.id.relativeLayout1));
		_seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText(_msg);
		Toast toast = new Toast(getActivity());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}
}