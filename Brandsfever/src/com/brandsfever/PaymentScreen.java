package com.brandsfever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.PaymentScreenDataAdapter;
import com.brandsfever.MyCartScreen.MyCartAdapter;
import com.dataholder.DataHolderClass;
import com.datamodel.PaymentScreenOrderModel;
import com.datamodel.StoreCreditDetails;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class PaymentScreen extends FragmentActivity implements OnClickListener {
	Context _ctx = PaymentScreen.this;
	Typeface _font;
	private PopupWindow pwindo;
	ImageButton main_menu, back_btn, cart_btn;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, _mycart, _invite, _logout;

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
	String _status, _identifier, _paid, _date, _city, _totalprice, _taxprice,
			_country, _state, _pk, _shippingfree, _method;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

		System.out.println("iii---" + _setCamount);

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

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));

		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String _username = _mypref.getString("_UserName", null);
		if (!(_username == null)) {
			set_user_name.setTypeface(_font);
			set_user_name.setText("Hi! " + _username.replace("Hi!", ""));
		} else {
			set_user_name.setText("Hi! Guest");
		}

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		back_btn = (ImageButton) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

		apply_promo_code = (Button) findViewById(R.id.apply_promo_code);
		apply_promo_code.setOnClickListener(this);

		remove_pcode = (Button) findViewById(R.id.remove_pcode);
		remove_pcode.setOnClickListener(this);

		pay_with_paypal = (ImageButton) findViewById(R.id.pay_with_paypal);
		pay_with_paypal.setOnClickListener(this);

		_all = (Button) findViewById(R.id.btn_all_cat);
		_all.setTypeface(_font);
		_all.setOnClickListener(this);

		_men = (Button) findViewById(R.id.cat_men);
		_men.setTypeface(_font);
		_men.setOnClickListener(this);

		_women = (Button) findViewById(R.id.cat_women);
		_women.setTypeface(_font);
		_women.setOnClickListener(this);

		_childrens = (Button) findViewById(R.id.cat_children);
		_childrens.setTypeface(_font);
		_childrens.setOnClickListener(this);

		_home = (Button) findViewById(R.id.cat_home);
		_home.setTypeface(_font);
		_home.setOnClickListener(this);

		_accessories = (Button) findViewById(R.id.cat_accesories);
		_accessories.setTypeface(_font);
		_accessories.setOnClickListener(this);

		_login = (Button) findViewById(R.id.btn_login);
		_login.setVisibility(View.GONE);

		_settings = (Button) findViewById(R.id.btn_setting);
		_settings.setTypeface(_font);
		_settings.setOnClickListener(this);

		_mycart = (Button) findViewById(R.id.my_cart);
		_mycart.setTextColor(Color.parseColor("#8e1345"));
		_mycart.setTypeface(_font);
		_mycart.setOnClickListener(this);

		_invite = (Button) findViewById(R.id.btn_invite);
		_invite.setTypeface(_font);
		_invite.setOnClickListener(this);

		_logout = (Button) findViewById(R.id.btn_logout);
		_logout.setTypeface(_font);
		_logout.setOnClickListener(this);

		/*
		 * _all.setTextColor(colors); _men.setTextColor(colors);
		 * _women.setTextColor(colors); _childrens.setTextColor(colors);
		 * _home.setTextColor(colors); _accessories.setTextColor(colors);
		 * _settings.setTextColor(colors); _mycart.setTextColor(color);
		 * _invite.setTextColor(colors);
		 */
		
		new GetPaymentState().execute();

		set_orders.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
	}

	// *************************************************************************************************************************//

	@Override
	public void onBackPressed() {
		Intent _orderdelivery = new Intent(_ctx, OrderDelivery_Screen.class);
		startActivity(_orderdelivery);
		finish();
	}

	// **********************************************************************************************************************//
	class GetPaymentState extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(PaymentScreen.this, "Loading",
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
			String _stateUrl = "https://www.brandsfever.com/api/v5/orders/"
					+ order_pk + "/states/?token=" + _getToken + "&user_id="
					+ _getuserId;
			System.out.println(_stateUrl);
			GetState(_stateUrl);
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

	// ***********************************************************************************************************************//
	public void GetState(String _url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(_url);
		try {
			HttpResponse _httpresponse = _httpclient.execute(_httpget);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			Log.i("--------------Https Responsecode----------", "."
					+ _httpresponse);
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
				System.out.println("state is" + _content);
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

	// *************************************************************************************************************************//

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
			new GetAllCarts().execute();
			new GetStoreCredit().execute();
			double sub = Double.parseDouble(_totalprice)
					- Double.parseDouble(_shippingfree);

			double tot = Math
					.round((Double.parseDouble(_totalprice) - _setCamount) * 100) / 100.00;

			double finalValue = Math.round(sub * 100.00) / 100.00;

			//order_subtotal_amount.setText("S$" + "" + finalValue + "0");
			/*order_shiping_amount.setText("S$" + _shippingfree);
			order_total_amount.setText("S$" + _totalprice);*/
			_adapter = new PaymentScreenDataAdapter(PaymentScreen.this,
					_Payorderinfo);
			set_orders.setAdapter(_adapter);

		}

	}

	// *************************************************************************************************************************//
	public void GetData(String _url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(_url);
		try {
			HttpResponse _httpresponse = _httpclient.execute(_httpget);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			Log.i("--------------Https Responsecode----------", "."
					+ _responsecode);
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

					System.out.println("iii---" + _content);
					JSONObject obj = new JSONObject(_content);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						JSONObject _data = obj.getJSONObject("data");
						_status = _data.getString("status");
						_identifier = _data.getString("identifier");
						_paid = _data.getString("paid");
						_date = _data.getString("date");
						_city = _data.getString("city");
						_totalprice = _data.getString("total_price");
						_taxprice = _data.getString("tax_price");
						_country = _data.getString("country");
						_state = _data.getString("state");
						_pk = _data.getString("pk");
						_shippingfree = _data.getString("shipping_fee");
						_method = _data.getString("method");

						JSONArray _itemarray = _data.getJSONArray("item_list");
						for (int i = 0; i < _itemarray.length(); i++) {
							JSONObject _jobj = _itemarray.getJSONObject(i);
							String name = _jobj.getString("name");
							String campaign = _jobj.getString("campaign");
							String image = _jobj.getString("image");
							String unit_price = _jobj.getString("unit_price");
							String pk = _jobj.getString("pk");
							String quantity = _jobj.getString("quantity");

							PaymentScreenOrderModel _listorder = new PaymentScreenOrderModel();
							_listorder.setName(name);
							_listorder.setCampaign(campaign);
							_listorder.setImage(image);
							_listorder.setUnit_price(unit_price);
							_listorder.setPk(pk);
							_listorder.setQuantity(quantity);
							_listorder.setTotal_price(_totalprice);
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

	// **************************************************************************************************************************//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_with_paypal:
			Intent _pay = new Intent(PaymentScreen.this,
					PayWithPaypal_Screen.class);
			startActivity(_pay);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;
		case R.id.back_btn:
			Intent _bck = new Intent(_ctx, OrderDelivery_Screen.class);
			startActivity(_bck);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.main_menu:
			slide_me.toggleLeftDrawer();
			break;

		case R.id.btn_all_cat:
			slide_me.closeRightSide();
			Intent all = new Intent(_ctx, ProductDisplay.class);
			all.putExtra("tab", "all");
			startActivity(all);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_men:
			slide_me.closeRightSide();
			Intent men = new Intent(_ctx, ProductDisplay.class);
			men.putExtra("tab", "men");
			startActivity(men);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_women:
			slide_me.closeRightSide();
			Intent women = new Intent(_ctx, ProductDisplay.class);
			women.putExtra("tab", "women");
			startActivity(women);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_children:
			slide_me.closeRightSide();
			Intent children = new Intent(_ctx, ProductDisplay.class);
			children.putExtra("tab", "children");
			startActivity(children);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_home:
			slide_me.closeRightSide();
			Intent home = new Intent(_ctx, ProductDisplay.class);
			home.putExtra("tab", "home");
			startActivity(home);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_accesories:
			slide_me.closeRightSide();
			Intent acc = new Intent(_ctx, ProductDisplay.class);
			acc.putExtra("tab", "accessories");
			startActivity(acc);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_setting:
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				Intent _phonesetting = new Intent(_ctx, SettingPhone.class);
				System.out.println("in phone");
				startActivity(_phonesetting);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
					&& DataHolderClass.getInstance().get_deviceInch() < 9) {
				Intent _tabsetting = new Intent(_ctx, SettingTab.class);
				startActivity(_tabsetting);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
				Intent _tabsetting = new Intent(_ctx, SettingTab.class);
				startActivity(_tabsetting);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.my_cart:
			Intent _cart = new Intent(PaymentScreen.this, MyCartScreen.class);
			startActivity(_cart);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_invite:
			Intent _invite = new Intent(_ctx, InviteSction_Screen.class);
			startActivity(_invite);
			slide_me.closeRightSide();
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_logout:
			Editor editor = _mypref.edit();
			editor.clear();
			editor.commit();
			Intent _intent = new Intent(getApplicationContext(),
					ProductDisplay.class);
			startActivity(_intent);
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
			System.out.println("text click");
			new StoreCreditWindow().show(getSupportFragmentManager(), "");
			break;

		case R.id.remove_SC:
			new RemoveStoreCredits().execute();
			break;

		default:
			break;
		}
	}

	// *************************************************************************************************************************//
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
			mProgressHUD = ProgressHUD.show(PaymentScreen.this, "Loading",
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
			Log.e("===RESPONSE====>", "===RESPONSE====>" + _ResponseFromServer);
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
					System.out.println(_msg);
					// popup like iphone
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			mProgressHUD.dismiss();
		}

	}

	// =======================================================================================================================//
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
			Log.i("--------------Responsecode----------", "." + _responsecode);
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

	// ==============================================================================================================================//
	class GetStoreCredit extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String _url = "https://www.brandsfever.com/api/v5/storecredits/?user_id="
					+ _getuserId + "&token=" + _getToken;
			System.out.println("url is" + _url);
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
				System.out.println("iii---" + _setCamount);
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

	// ==============================================================================================================================//
	public void Get_Store_Credits(String url_get_store_credits) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(url_get_store_credits);
		try {
			HttpResponse _httpresponse = _httpclient.execute(_httpget);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			Log.i("--------------Responsecode----------", "." + _responsecode);
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
				System.out.println(G_P);
				try {
					JSONObject obj = new JSONObject(G_P);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						JSONArray get_credit_details = obj
								.getJSONArray("store_credits");
						System.out.println("credit...." + get_credit_details);

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
						System.out.println("s_c is" + _storeCredits.size());
					} else {
						// toast.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("invalid user");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// *****************************************************************************************************************//

	class RemoveStoreCredits extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(PaymentScreen.this, "Loading",
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
			Log.e("===RESPONSE====>", "===RESPONSE====>"
					+ _ResponseFromServerCredits);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
			System.out.println("i m here");
			remove_SC.setVisibility(View.GONE);
			apply_SC.setVisibility(View.VISIBLE);
			new GetPaymentState().execute();
			mProgressHUD.dismiss();
		}

	}

	// ========================================= chary add the
	// code=================================================================================//

	private class GetAllCarts extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			/*mProgressHUD = ProgressHUD.show(_ctx, "Loading", true, true, this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int displayHeight = displaymetrics.heightPixels;
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams wmlp = mProgressHUD.getWindow()
					.getAttributes();
			wmlp.y = displayHeight / 4;
			mProgressHUD.getWindow().setAttributes(wmlp);
			mProgressHUD.setCancelable(false);*/
			super.onPreExecute();
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected String doInBackground(String... arg0) {
			String _url = "https://www.brandsfever.com/api/v5/carts/?user_id="
					+ _getuserId + "&token=" + _getToken;
			HttpClient _httpclient = HttpsClient.getNewHttpClient();
			HttpGet _httpget = new HttpGet(_url);
			HttpResponse _httpresponse;
			try {
				_httpresponse = _httpclient.execute(_httpget);

				int _responsecode = _httpresponse.getStatusLine()
						.getStatusCode();
				if (_responsecode == 200) {
					InputStream _inputstream = _httpresponse.getEntity()
							.getContent();
					BufferedReader r = new BufferedReader(
							new InputStreamReader(_inputstream));
					StringBuilder total = new StringBuilder();
					String line;
					while ((line = r.readLine()) != null) {
						total.append(line);
					}
					String _content = total.toString();
					System.out.println(_content);
					JSONObject obj = new JSONObject(_content);
					String ret = obj.getString("ret");
					String mesg = obj.getString("msg");
					if (ret.equals("0") && mesg.equalsIgnoreCase("ok")) {
						System.out.println("injson");
						JSONArray _getcart = obj.getJSONArray("carts");
						for (int i = 0; i < _getcart.length(); i++) {
							JSONObject _obj = _getcart.getJSONObject(i);
							ch_totalprice = _obj.getString("total_price");
						}
					}
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			//mProgressHUD.cancel();
			
			order_subtotal_amount.setText(ch_totalprice.replace("GD", "$"));
			order_shiping_amount.setText("S$" + _shippingfree);
			order_total_amount.setText("S$" + _totalprice);
		}

	}
}
