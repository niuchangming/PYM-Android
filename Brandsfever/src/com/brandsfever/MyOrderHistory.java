package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dataholder.DataHolderClass;
import com.datamodel.OrderHistoryDataModel;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class MyOrderHistory extends SherlockFragmentActivity {
	Context _ctx = MyOrderHistory.this;
	Typeface _font;
	ListView set_store_credits;
	TextView Your_order_tag;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	StoreAdapter _adapter;
	String _ResponseFromServer;
	ListView order_history_list;
	RelativeLayout _child_list;
	public static ArrayList<OrderHistoryDataModel> _orderData = new ArrayList<OrderHistoryDataModel>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			setContentView(R.layout.activity_my_order_history);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceInch() < 9) {
			setContentView(R.layout.activity_my_order_history_screen);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
			setContentView(R.layout.activity_my_order_history_screen);
		}


		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
				.inflate(R.layout.action_bar, null);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		final ImageButton actionBarLeft = (ImageButton) findViewById(R.id.action_bar_left);
		actionBarLeft.setImageDrawable(getResources().getDrawable(
				R.drawable.back_button));
		actionBarLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		final ImageButton actionBarCart = (ImageButton) findViewById(R.id.action_bar_right);
		actionBarCart.setImageDrawable(getResources().getDrawable(
				R.drawable.cart_btn_bg));
		actionBarCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				directToCart();
			}
		});

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		order_history_list = (ListView) findViewById(R.id.order_history_list);
		Your_order_tag = (TextView) findViewById(R.id.Your_order_tag);
		Your_order_tag.setTypeface(_font, Typeface.NORMAL);
		_child_list=(RelativeLayout)findViewById(R.id.three);
		
		try {
			_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
			_getuserId = _mypref.getString("ID", "115383");
			_getToken = _mypref.getString("TOKEN",
					"564ee031dcaca1c28056773189fd4cc2");

			new GetYourOrders().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": users/"+_getuserId+"/orders/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	class GetYourOrders extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(MyOrderHistory.this, "Loading",
					true, true, this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
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
			// TODO Auto-generated method stub

		}

		@Override
		protected String doInBackground(String... params) {
			_orderData.clear();
			String _url = "https://www.brandsfever.com/api/v5/users/"
					+ _getuserId + "/orders/?user_id=" + _getuserId + "&token="
					+ _getToken;
			GetOrderHistory(_url);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			_adapter = new StoreAdapter(MyOrderHistory.this, _orderData);
			if(_orderData.size()<1){
				_child_list.setBackgroundColor(Color.parseColor("#FFFFFF"));
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				TextView _seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
				_seterrormsg.setText("No order history found");
				Toast toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
			order_history_list.setAdapter(_adapter);
			mProgressHUD.dismiss();
		}
	}

	public void GetOrderHistory(String url) {
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
					if (ret.equals("0")) {
						JSONArray order_history_data = obj.getJSONArray("data");
						for (int i = 0; i < order_history_data.length(); i++) {
							JSONObject jsonobj = order_history_data
									.getJSONObject(i);
							String status = jsonobj.getString("status");
							String total_price = jsonobj
									.getString("total_price");
							String campaigns = jsonobj.getString("campaigns");
							String date = jsonobj.getString("date");
							String pk = jsonobj.getString("pk");
							String identifier = jsonobj.getString("identifier");

							OrderHistoryDataModel _ordermodel = new OrderHistoryDataModel();
							_ordermodel.setStatus(status);
							_ordermodel.setTotal_price(total_price);
							_ordermodel.setCampaigns(campaigns);
							_ordermodel.setDate(date);
							_ordermodel.setPk(pk);
							_ordermodel.setIdentifier(identifier);

							_orderData.add(_ordermodel);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class StoreAdapter extends BaseAdapter {
		Context _mcontext = null;
		LayoutInflater inflater;
		ArrayList<OrderHistoryDataModel> data;

		public StoreAdapter(Context context,
				ArrayList<OrderHistoryDataModel> arraylist) {
			this._mcontext = context;
			data = arraylist;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return pos;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView campaign_tag, set_product_name, orderId_tag, set_orderId, date_tag, set_date, status_tag, set_status, total_price_tag, set_total_price;

			inflater = (LayoutInflater) _mcontext.getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.order_history_inflator,
					parent, false);

			campaign_tag = (TextView) itemView.findViewById(R.id.campaign_tag);
			set_product_name = (TextView) itemView
					.findViewById(R.id.set_product_name);
			orderId_tag = (TextView) itemView.findViewById(R.id.orderId_tag);
			set_orderId = (TextView) itemView.findViewById(R.id.set_orderId);
			date_tag = (TextView) itemView.findViewById(R.id.date_tag);
			set_date = (TextView) itemView.findViewById(R.id.set_date);
			status_tag = (TextView) itemView.findViewById(R.id.status_tag);
			set_status = (TextView) itemView.findViewById(R.id.set_status);
			total_price_tag = (TextView) itemView
					.findViewById(R.id.total_price_tag);
			set_total_price = (TextView) itemView
					.findViewById(R.id.set_total_price);

			campaign_tag.setTypeface(_font);
			set_product_name.setTypeface(_font);
			orderId_tag.setTypeface(_font);
			set_orderId.setTypeface(_font);
			date_tag.setTypeface(_font);
			set_date.setTypeface(_font);
			status_tag.setTypeface(_font);
			set_status.setTypeface(_font);
			total_price_tag.setTypeface(_font);
			set_total_price.setTypeface(_font);

			OrderHistoryDataModel obj = data.get(position);

			set_date.setText(obj.getDate());
			set_product_name.setText(obj.getCampaigns());
			set_orderId.setText(obj.getIdentifier());
			set_status.setText(obj.getStatus());
			set_total_price.setText(obj.getTotal_price());

			return itemView;
		}

	}


	private void directToCart() {

		SharedPreferences mypref = getApplicationContext()
				.getSharedPreferences("mypref", 0);
		String username = mypref.getString("UserName", null);
		
		if (username != null) { // check login status
			Intent gotocart = new Intent(MyOrderHistory.this, MyCartActivity.class);
			startActivity(gotocart);
		} else {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.error_popop,
					(ViewGroup) findViewById(R.id.relativeLayout1));
			final TextView msgTextView = (TextView) view
					.findViewById(R.id._seterrormsg);
			msgTextView.setText("Please login!");
			Toast toast = new Toast(MyOrderHistory.this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.show();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
