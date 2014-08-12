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

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.adapter.PaymentScreenDataAdapter;
import com.dataholder.DataHolderClass;
import com.datamodel.PaymentScreenOrderModel;
import com.datamodel.StoreCreditDetails;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class PaymentActivity extends SherlockFragmentActivity implements
		OnClickListener {
	private static final String TAG = "PaymentActivity";
	Context _ctx = PaymentActivity.this;
	Typeface _font;
	private PopupWindow pwindo;

	TextView set_billing_address, set_shipping_address, ship_tag, bill_tag,
			promotion_code;
	TextView order_confirmation_tag, your_order_tag, payment_option_tag,
			set_campaign_tag, set_product_tag;
	TextView set_quantity_tag, set_unitprice_tag, set_totalprice_tag,
			store_credit_tag;
	ImageButton pay_with_paypal;
	String _ResponseFromServer, _ResponseFromServerCredits;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	String mStatus, mIdentifier, mPaid, mDate, mCity, mTotalprice, mTaxprice,
			mCountry, mState, mPk, mShippingfree, mMethod;
	double mSubtotal;
	public static ArrayList<PaymentScreenOrderModel> _Payorderinfo = new ArrayList<PaymentScreenOrderModel>();
	public static ArrayList<StoreCreditDetails> _storeCredits = new ArrayList<StoreCreditDetails>();
	TextView order_subtotal_amount, order_shiping_amount, order_total_amount,
			order_subtotal_text, order_shiping_text, order_total_text,
			Set_store_credit;
	PaymentScreenDataAdapter _adapter;
	ListView set_orders, store_credit_list;
	private int _paymentstate;
	Button apply_promo_code, remove_pcode;
	EditText enter_promo_code;
	String _getPromoCode;
	Button cancel_dialouge;
	LinearLayout remove_SC, apply_SC;
	int color, colors;
	double _setCamount;

	String ch_totalprice;
	JSONObject mOrderDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			setContentView(R.layout.activity_payment_screen);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceInch() < 9) {
			setContentView(R.layout.payment_order_info_inflator_tablet);
			_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
			color = Integer.parseInt("8e1345", 16) + 0xFF000000;
			colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
			set_quantity_tag = (TextView) findViewById(R.id.set_quantity_tag);
			set_quantity_tag.setTypeface(_font, Typeface.NORMAL);
			set_unitprice_tag = (TextView) findViewById(R.id.set_unitprice_tag);
			set_unitprice_tag.setTypeface(_font, Typeface.NORMAL);
			set_totalprice_tag = (TextView) findViewById(R.id.set_totalprice_tag);
			set_totalprice_tag.setTypeface(_font, Typeface.NORMAL);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
			setContentView(R.layout.payment_order_info_inflator_tablet);
			_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
			set_quantity_tag = (TextView) findViewById(R.id.set_quantity_tag);
			set_quantity_tag.setTypeface(_font, Typeface.NORMAL);
			set_unitprice_tag = (TextView) findViewById(R.id.set_unitprice_tag);
			set_unitprice_tag.setTypeface(_font, Typeface.NORMAL);
			set_totalprice_tag = (TextView) findViewById(R.id.set_totalprice_tag);
			set_totalprice_tag.setTypeface(_font, Typeface.NORMAL);
		}

		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
				.inflate(R.layout.action_bar, null);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		final ImageButton actionBarMenu = (ImageButton) findViewById(R.id.action_bar_left);
		actionBarMenu.setImageDrawable(getResources().getDrawable(
				R.drawable.back_button));
		actionBarMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		final ImageButton actionBarCart = (ImageButton) findViewById(R.id.action_bar_right);
		actionBarCart.setVisibility(View.INVISIBLE);

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		set_shipping_address = (TextView) findViewById(R.id.set_shipping_address);
		set_shipping_address.setTypeface(_font, Typeface.NORMAL);

		remove_SC = (LinearLayout) findViewById(R.id.remove_SC);
		remove_SC.setOnClickListener(this);
		apply_SC = (LinearLayout) findViewById(R.id.apply_SC);

		set_billing_address = (TextView) findViewById(R.id.set_billing_address);
		set_billing_address.setTypeface(_font, Typeface.NORMAL);

		your_order_tag = (TextView) findViewById(R.id.your_order_tag);
		your_order_tag.setTypeface(_font, Typeface.NORMAL);

		store_credit_tag = (TextView) findViewById(R.id.store_credit_tag);
		store_credit_tag.setTypeface(_font, Typeface.NORMAL);
		store_credit_tag.setPaintFlags(store_credit_tag.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
		store_credit_tag.setOnClickListener(this);

		Set_store_credit = (TextView) findViewById(R.id.Set_store_credit);
		Set_store_credit.setTypeface(_font);

		promotion_code = (TextView) findViewById(R.id.promotion_code);
		promotion_code.setTypeface(_font);

		order_confirmation_tag = (TextView) findViewById(R.id.order_confirmation_tag);
		order_confirmation_tag.setTypeface(_font, Typeface.NORMAL);

		payment_option_tag = (TextView) findViewById(R.id.payment_option_tag);
		payment_option_tag.setTypeface(_font, Typeface.NORMAL);

		set_campaign_tag = (TextView) findViewById(R.id.set_campaign_tag);
		set_campaign_tag.setTypeface(_font, Typeface.NORMAL);

		set_product_tag = (TextView) findViewById(R.id.set_product_tag);
		set_product_tag.setTypeface(_font, Typeface.NORMAL);

		ship_tag = (TextView) findViewById(R.id.ship_tag);
		ship_tag.setTypeface(_font, Typeface.BOLD);

		bill_tag = (TextView) findViewById(R.id.bill_tag);
		bill_tag.setTypeface(_font, Typeface.BOLD);

		order_subtotal_amount = (TextView) findViewById(R.id.order_subtotal_amount);
		order_subtotal_amount.setTypeface(_font);

		order_shiping_amount = (TextView) findViewById(R.id.order_shiping_amount);
		order_shiping_amount.setTypeface(_font);

		order_total_amount = (TextView) findViewById(R.id.order_total_amount);
		order_total_amount.setTypeface(_font);

		order_subtotal_text = (TextView) findViewById(R.id.order_subtotal_text);
		order_subtotal_text.setTypeface(_font);

		order_shiping_text = (TextView) findViewById(R.id.order_shiping_text);
		order_shiping_text.setTypeface(_font);

		order_total_text = (TextView) findViewById(R.id.order_total_text);
		order_total_text.setTypeface(_font);

		set_orders = (ListView) findViewById(R.id.set_orders);

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		set_shipping_address.setText(DataHolderClass.getInstance()
				.getBill_ship_address());
		set_billing_address.setText(DataHolderClass.getInstance()
				.getBill_ship_address());

		enter_promo_code = (EditText) findViewById(R.id.enter_promo_code);

		apply_promo_code = (Button) findViewById(R.id.apply_promo_code);
		apply_promo_code.setOnClickListener(this);

		remove_pcode = (Button) findViewById(R.id.remove_pcode);
		remove_pcode.setOnClickListener(this);

		pay_with_paypal = (ImageButton) findViewById(R.id.pay_with_paypal);
		pay_with_paypal.setOnClickListener(this);

		new GetPaymentState().execute();

		set_orders.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": orders/" + _getuserId + "/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	class GetPaymentState extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(PaymentActivity.this, "Loading",
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
		protected String doInBackground(String... params) {
			_Payorderinfo.clear();
			_storeCredits.clear();
			String order_pk = DataHolderClass.getInstance().get_orderpk();
			String stateUrl = "https://www.brandsfever.com/api/v5/orders/"
					+ order_pk + "/states/?token=" + _getToken + "&user_id="
					+ _getuserId;
			GetState(stateUrl);
			return null;
		}

		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		protected void onPostExecute(String result) {
			if (_paymentstate == 0) {
				new GetOrderList().execute();

			} else if (_paymentstate == 1) {
				// go to order summery screen
			}
			mProgressHUD.dismiss();
		}
	}

	public void GetState(String _url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(_url);
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
					JSONObject _jobj = new JSONObject(_content);
					String ret = _jobj.getString("ret");
					if (ret.equals("0")) {
						String statenumber = _jobj.getString("state");
						_paymentstate = Integer.parseInt(statenumber);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception _e) {
			_e.printStackTrace();
		}
	}

	class GetOrderList extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			String _setorderpk = DataHolderClass.getInstance().get_orderpk();
			String url = "https://www.brandsfever.com/api/v5/orders/"
					+ _setorderpk + "/" + "?user_id=" + _getuserId + "&token="
					+ _getToken;
			GetData(url);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			new GetStoreCredit().execute();

			double subtotal = Math.round(mSubtotal * 100.00) / 100.00;
			order_subtotal_amount.setText("S$" + "" + subtotal + "0");
			order_shiping_amount.setText("S$" + mShippingfree);
			order_total_amount.setText("S$" + mTotalprice);
			_adapter = new PaymentScreenDataAdapter(PaymentActivity.this,
					_Payorderinfo);
			set_orders.setAdapter(_adapter);

		}

	}

	public void GetData(String _url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(_url);
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
						mOrderDetail = obj.getJSONObject("data");
						mStatus = mOrderDetail.getString("status");
						mIdentifier = mOrderDetail.getString("identifier");
						mPaid = mOrderDetail.getString("paid");
						mDate = mOrderDetail.getString("date");
						mCity = mOrderDetail.getString("city");
						mTotalprice = mOrderDetail.getString("total_price");
						mTaxprice = mOrderDetail.getString("tax_price");
						mCountry = mOrderDetail.getString("country");
						mState = mOrderDetail.getString("state");
						mPk = mOrderDetail.getString("pk");
						mShippingfree = mOrderDetail.getString("shipping_fee");
						mMethod = mOrderDetail.getString("method");

						mSubtotal = 0;
						JSONArray _itemarray = mOrderDetail
								.getJSONArray("item_list");
						for (int i = 0; i < _itemarray.length(); i++) {
							JSONObject _jobj = _itemarray.getJSONObject(i);
							String name = _jobj.getString("name");
							String campaign = _jobj.getString("campaign");
							String image = _jobj.getString("image");
							String unit_price = _jobj.getString("unit_price");
							String pk = _jobj.getString("pk");
							String quantity = _jobj.getString("quantity");

							mSubtotal = mSubtotal
									+ Double.parseDouble(quantity)
									* Double.parseDouble(unit_price);

							PaymentScreenOrderModel _listorder = new PaymentScreenOrderModel();
							_listorder.setName(name);
							_listorder.setCampaign(campaign);
							_listorder.setImage(image);
							_listorder.setUnit_price(unit_price);
							_listorder.setPk(pk);
							_listorder.setQuantity(quantity);
							_listorder.setTotal_price(mTotalprice);
							_Payorderinfo.add(_listorder);
						}
					} else {
						// parsing error
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_with_paypal:
			Intent payIntent = new Intent(PaymentActivity.this,
					PayWithPaypalActivity.class);
			payIntent.putExtra("OrderDetailKey", mOrderDetail.toString());
			startActivity(payIntent);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.apply_promo_code:
			String _action = "promo_code_add";
			_getPromoCode = enter_promo_code.getText().toString();
			if (_getPromoCode.length() > 0) {
				new ApplyPromoCode(_action, _getPromoCode).execute();
			} else {
				String msg = "Please fill \n promocode";
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);
				_seterrormsg.setText(msg);
				Toast toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
			break;
		case R.id.cancel_dialouge:
			pwindo.dismiss();
			break;

		case R.id.remove_pcode:
			String _actionremove = "promo_code_remove";
			new ApplyPromoCode(_actionremove, "").execute();
			break;

		case R.id.store_credit_tag:
			new StoreCreditWindow().show(getSupportFragmentManager(), "");
			break;

		case R.id.remove_SC:
			new RemoveStoreCredits().execute();
			break;

		default:
			break;
		}
	}

	class ApplyPromoCode extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;
		String _sendaction, _sendcode;

		public ApplyPromoCode(String _getaction, String _getcode) {
			this._sendaction = _getaction;
			this._sendcode = _getcode;
		}

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(PaymentActivity.this, "Loading",
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
		protected String doInBackground(String... params) {
			String _getpk = DataHolderClass.getInstance().get_orderpk();
			String _urlpromo = "https://www.brandsfever.com/api/v5/orders/"
					+ _getpk + "/discount/";
			BasicNameValuePair _puserid = new BasicNameValuePair("user_id",
					_getuserId);
			BasicNameValuePair _ptoken = new BasicNameValuePair("token",
					_getToken);
			BasicNameValuePair _paction = new BasicNameValuePair(
					"apply_action", _sendaction);
			BasicNameValuePair _psCredits = new BasicNameValuePair(
					"identifier", _sendcode);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_puserid);
			_namevalueList.add(_ptoken);
			_namevalueList.add(_paction);
			_namevalueList.add(_psCredits);
			_ResponseFromServer = SendData(_urlpromo, _namevalueList);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				enter_promo_code.setText(null);
				JSONObject obj = new JSONObject(_ResponseFromServer);
				String ret = obj.getString("ret");
				if (ret.equals("0")) {
					new GetOrderList().execute();
					enter_promo_code.setVisibility(View.GONE);
					apply_promo_code.setVisibility(View.GONE);
					remove_pcode.setVisibility(View.VISIBLE);
					promotion_code.setText("Discount");
				} else {
					String _msg = obj.getString("msg");

					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.relativeLayout1));
					TextView _seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText(_msg);
					Toast toast = new Toast(_ctx);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();
					// popup like iphone
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			mProgressHUD.dismiss();
		}

	}

	public String SendData(String url, List<NameValuePair> _namevalueList) {
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

	class GetStoreCredit extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String _url = "https://www.brandsfever.com/api/v5/storecredits/?user_id="
					+ _getuserId + "&token=" + _getToken;
			Get_Store_Credits(_url);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (_storeCredits.size() > 0) {
				apply_SC.setVisibility(View.VISIBLE);
				TextView Remove_credit_tag = (TextView) findViewById(R.id.Remove_credit_tag);
				Remove_credit_tag.setTypeface(_font);
				TextView Remove_store_credit = (TextView) findViewById(R.id.Remove_store_credit);
				Remove_store_credit.setTypeface(_font);

				_setCamount = DataHolderClass.getInstance().get_creditAmount();
				if (_setCamount > 0) {
					String sw = "+S$" + _setCamount;
					Remove_store_credit.setText(sw);
					remove_SC.setVisibility(View.VISIBLE);
					apply_SC.setVisibility(View.GONE);
					DataHolderClass.getInstance().set_creditAmount(0.0);
				} else {

				}
			} else {
				apply_SC.setVisibility(View.GONE);
				remove_SC.setVisibility(View.GONE);
			}
		}
	}

	public void Get_Store_Credits(String url_get_store_credits) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(url_get_store_credits);
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
				String G_P = total.toString();
				try {
					JSONObject obj = new JSONObject(G_P);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						JSONArray get_credit_details = obj
								.getJSONArray("store_credits");

						for (int i = 0; i < get_credit_details.length(); i++) {
							JSONObject jsonobj = get_credit_details
									.getJSONObject(i);
							String one = jsonobj.getString("granted_by");
							String two = jsonobj.getString("expired_at");
							String three = jsonobj.getString("amount");
							String four = jsonobj.getString("redeemed_order");
							String five = jsonobj.getString("is_redeemable");
							String six = jsonobj.getString("pk");
							String seven = jsonobj.getString("redeemed_at");
							String eight = jsonobj.getString("state");
							String nine = jsonobj.getString("channel");

							StoreCreditDetails _credits = new StoreCreditDetails();
							_credits.setGranted_by(one);
							_credits.setAmount(three);
							_credits.setExpired_at(two);
							_credits.setChannel(nine);
							_credits.setIs_redeemable(five);
							_credits.setPk(six);
							_credits.setState(eight);
							_credits.setRedeemed_at(seven);
							_credits.setRedeemed_order(four);

							_storeCredits.add(_credits);
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

	class RemoveStoreCredits extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(PaymentActivity.this, "Loading",
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
		protected String doInBackground(String... params) {
			String _opk = DataHolderClass.getInstance().get_orderpk();
			String _url = "https://www.brandsfever.com/api/v5/orders/" + _opk
					+ "/discount/";
			String apply_action = "store_credit_apply";
			BasicNameValuePair _uid = new BasicNameValuePair("user_id",
					_getuserId);
			BasicNameValuePair _ut = new BasicNameValuePair("token", _getToken);
			BasicNameValuePair _apply_action = new BasicNameValuePair(
					"apply_action", apply_action);
			BasicNameValuePair _store_credits = new BasicNameValuePair(
					"store_credits", "");
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_uid);
			_namevalueList.add(_ut);
			_namevalueList.add(_apply_action);
			_namevalueList.add(_store_credits);
			_ResponseFromServerCredits = SendData(_url, _namevalueList);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
			remove_SC.setVisibility(View.GONE);
			apply_SC.setVisibility(View.VISIBLE);
			new GetPaymentState().execute();
			mProgressHUD.dismiss();
		}

	}

}