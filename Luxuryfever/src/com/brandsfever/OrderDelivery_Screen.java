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
import org.json.JSONObject;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.brandsfever.luxury.R;
import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class OrderDelivery_Screen extends FragmentActivity implements
		OnClickListener {
	CheckBox _checkBillingaddress;
	LinearLayout _shipping_address_layout;
	Context _ctx = OrderDelivery_Screen.this;
	TextView title_tag, billing_addrress, sameas_billing_tag;
	Typeface _font;
	ImageButton main_menu, back_btn, cart_btn, send_to_thid_address;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women,  _login,
			_settings, _mycart, mSupport, _invite, _logout;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	EditText first_name, last_name, address, zipcode, country, phone_nummber,
			bfirst_name, blast_name, baddress, bzipcode, bcountry,
			bphone_nummber;
	String pnumber="", fname="", lname="", saddress="", scountry="", zcode="";
	String _fname, _lname, _address, _zipcode, _country, _pnmber;
	String _ResponseFromServer;
	TextView _seterrormsg;
	String _msg;
	int color,colors;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			setContentView(R.layout.activity_order_delivery__screen);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceInch() < 9) {
			setContentView(R.layout.seven_inch_order_delivery);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
			setContentView(R.layout.order_delivery_screen_tablet);
		}
		_checkBillingaddress = (CheckBox) findViewById(R.id.checkBox1);
		_shipping_address_layout = (LinearLayout) findViewById(R.id.four);
		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16)+0xFF000000;
		colors = Integer.parseInt("ffffff", 16)+0xFF000000;
		title_tag = (TextView) findViewById(R.id.heading);
		billing_addrress = (TextView) findViewById(R.id.billing_addrress);
		billing_addrress.setTypeface(_font, Typeface.BOLD);
		sameas_billing_tag = (TextView) findViewById(R.id.sameas_billing_tag);
		sameas_billing_tag.setTypeface(_font, Typeface.BOLD);
		title_tag.setTypeface(_font, Typeface.BOLD);
		_checkBillingaddress.setTypeface(_font, Typeface.BOLD);

		_checkBillingaddress
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							_shipping_address_layout.setVisibility(View.GONE);
						} else {
							_shipping_address_layout
									.setVisibility(View.VISIBLE);
						}

					}
				});

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		new GetShipingAddress().execute();

		first_name = (EditText) findViewById(R.id.first_name);
		last_name = (EditText) findViewById(R.id.last_name);
		address = (EditText) findViewById(R.id.address);
		zipcode = (EditText) findViewById(R.id.zipcode);
		country = (EditText) findViewById(R.id.country);
		phone_nummber = (EditText) findViewById(R.id.phone_nummber);

		first_name.setTypeface(_font, Typeface.NORMAL);
		last_name.setTypeface(_font, Typeface.NORMAL);
		address.setTypeface(_font, Typeface.NORMAL);
		zipcode.setTypeface(_font, Typeface.NORMAL);
		country.setTypeface(_font, Typeface.NORMAL);
		phone_nummber.setTypeface(_font, Typeface.NORMAL);

		bfirst_name = (EditText) findViewById(R.id.bfirst_name);
		blast_name = (EditText) findViewById(R.id.blast_name);
		baddress = (EditText) findViewById(R.id.baddress);
		bzipcode = (EditText) findViewById(R.id.bzipcode);
		bcountry = (EditText) findViewById(R.id.bcountry);
		bphone_nummber = (EditText) findViewById(R.id.bphone_nummber);

		bfirst_name.setTypeface(_font, Typeface.NORMAL);
		blast_name.setTypeface(_font, Typeface.NORMAL);
		baddress.setTypeface(_font, Typeface.NORMAL);
		bzipcode.setTypeface(_font, Typeface.NORMAL);
		bcountry.setTypeface(_font, Typeface.NORMAL);
		bphone_nummber.setTypeface(_font, Typeface.NORMAL);

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));
		
		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String username = _mypref.getString("_UserName", null);
        set_user_name.setTypeface(_font);
		if (username != null) {
			set_user_name.setText("Hi! "+username);
		} else {
			set_user_name.setText("Hi! Guest");
		}

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		back_btn = (ImageButton) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

		send_to_thid_address = (ImageButton) findViewById(R.id.send_to_thid_address);
		send_to_thid_address.setOnClickListener(this);

		_all = (Button) findViewById(R.id.btn_all_cat);
		_all.setTypeface(_font);
		_all.setOnClickListener(this);

		_men = (Button) findViewById(R.id.cat_shoes);
		_men.setTypeface(_font);
		_men.setOnClickListener(this);

		_women = (Button) findViewById(R.id.cat_handbags);
		_women.setTypeface(_font);
		_women.setOnClickListener(this);

		_login = (Button) findViewById(R.id.btn_login);
		_login.setVisibility(View.GONE);

		_settings = (Button) findViewById(R.id.btn_setting);
		_settings.setTypeface(_font);
		_settings.setOnClickListener(this);

		_mycart = (Button) findViewById(R.id.my_cart);
		_mycart.setTypeface(_font);
		_mycart.setOnClickListener(this);

		mSupport = (Button)findViewById(R.id.btn_support);
		mSupport.setTypeface(_font);
		mSupport.setOnClickListener(this);
		
		_invite = (Button) findViewById(R.id.btn_invite);
		_invite.setTypeface(_font);
		_invite.setOnClickListener(this);

		_logout = (Button) findViewById(R.id.btn_logout);
		_logout.setTypeface(_font);
		_logout.setOnClickListener(this);
		
		_all.setTextColor(colors);
        _men.setTextColor(colors);
        _women.setTextColor(colors);
		_settings.setTextColor(colors);
		_mycart.setTextColor(color);
		mSupport.setTextColor(colors);
		_invite.setTextColor(colors);

	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": shipping-address/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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

		case R.id.cat_shoes:
			slide_me.closeRightSide();
			Intent men = new Intent(_ctx, ProductDisplay.class);
			men.putExtra("tab", "men");
			startActivity(men);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_handbags:
			slide_me.closeRightSide();
			Intent women = new Intent(_ctx, ProductDisplay.class);
			women.putExtra("tab", "women");
			startActivity(women);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_setting:
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				Intent _phonesetting = new Intent(_ctx, SettingPhone.class);
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
			slide_me.toggleRightDrawer();
			break;

		case R.id.btn_support:
			if(slide_me.isClosed()){
				slide_me.setEnabled(false);
			}
			else {
				slide_me.setEnabled(true);
				Intent support = new Intent(_ctx,SupportActivity.class);
				startActivity(support);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
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

		case R.id.back_btn:
			finish();
			break;

		case R.id.send_to_thid_address:
			_fname = first_name.getText().toString();
			_lname = last_name.getText().toString();
			_address = address.getText().toString();
			_zipcode = zipcode.getText().toString();
			_country = country.getText().toString();
			_pnmber = phone_nummber.getText().toString();
			if (_fname.length() > 0 && !(_fname.equalsIgnoreCase(""))
					&& !(_fname.equalsIgnoreCase(null)) && _lname.length() > 0
					&& !(_lname.equalsIgnoreCase(""))
					&& !(_lname.equalsIgnoreCase(null))
					&& _address.length() > 0
					&& !(_address.equalsIgnoreCase(""))
					&& !(_address.equalsIgnoreCase(null))
					&& _zipcode.length() > 0
					&& !(_zipcode.equalsIgnoreCase(""))
					&& !(_zipcode.equalsIgnoreCase(null))
					&& _country.length() > 0
					&& !(_country.equalsIgnoreCase(""))
					&& !(_country.equalsIgnoreCase(null))
					&& _pnmber.length() > 0 && !(_pnmber.equalsIgnoreCase(""))
					&& !(_pnmber.equalsIgnoreCase(null))) {

				String _sendnext = _fname + "" + _lname + "\n" + _address
						+ "\n" + _zipcode + "," + _country + "\n"
						+ "Phone:(+65)" + _pnmber;
				DataHolderClass.getInstance().setBill_ship_address(_sendnext);

				new CreateOrderOfUser().execute();
			} else if (_fname.length() == 0) {
				_msg = "Please enter\nfirst name!";
				_ResponsePopup();

			} else if (_lname.length() == 0) {
				_msg = "Please enter\nlast name";
				_ResponsePopup();

			} else if (_address.length() == 0) {
				_msg = "Please enter address!";
				_ResponsePopup();

			} else if (_zipcode.length() == 0) {
				_msg = "Please enter zipcode!";
				_ResponsePopup();

			} else if (_country.length() == 0) {
				_msg = "Please enter\ncountry name!";
				_ResponsePopup();

			} else if (_pnmber.length() == 0) {
				_msg = "Please enter\ncontact number!";
				_ResponsePopup();

			}
			break;
		default:
			break;
		}

	}

	class GetShipingAddress extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(OrderDelivery_Screen.this,
					"Loading", true, true, this);
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
			String _url = "https://www.brandsfever.com/api/v5/shipping-address/?user_id="
					+ _getuserId + "&token=" + _getToken;
			GetAddress(_url);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (!(fname.equalsIgnoreCase("null")) && fname.length() > 0) {
				first_name.setText(fname);
				bfirst_name.setText(fname);
			} else {
				first_name.setText("");
				bfirst_name.setText("");
			}
			if (!(lname.equalsIgnoreCase("null")) && lname.length() > 0) {
				last_name.setText(lname);
				blast_name.setText(lname);
			} else {
				last_name.setText("");
				blast_name.setText("");
			}
			if (!(saddress.equalsIgnoreCase("null")) && saddress.length() > 0) {
				address.setText(saddress);
				baddress.setText(saddress);
			} else {
				address.setText("");
				baddress.setText("");
			}
			if (!(zcode.equalsIgnoreCase("null")) && zcode.length() > 0) {
				zipcode.setText(zcode);
				bzipcode.setText(zcode);
			} else {
				zipcode.setText("");
				bzipcode.setText("");
			}
			if (!(scountry.equalsIgnoreCase("null")) && scountry.length() > 0) {
				country.setText(scountry);
				bcountry.setText(scountry);
			} else {
				country.setText("");
				bcountry.setText("");
			}
			if (!(pnumber.equalsIgnoreCase("null")) && pnumber.length() > 0) {
				phone_nummber.setText(pnumber);
				bphone_nummber.setText(pnumber);
			} else {
				phone_nummber.setText("");
				bphone_nummber.setText("");
			}

			mProgressHUD.dismiss();

		}
	}

	private void GetAddress(String url) {
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
					JSONObject _jobj = new JSONObject(_content);
					String ret = _jobj.getString("ret");
					if (ret.equals("0")) {
						pnumber = _jobj.getString("phone_number");
						fname = _jobj.getString("first_name");
						lname = _jobj.getString("last_name");
						saddress = _jobj.getString("address");
						scountry = _jobj.getString("country");
						zcode = _jobj.getString("zipcode");

						String _sendnext = fname + "" + lname + "\n" + saddress
								+ "\n" + zcode + "," + scountry + "\n"
								+ "Phone:(+65)" + pnumber;
						DataHolderClass.getInstance().setBill_ship_address(
								_sendnext);
					} else {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CreateOrderOfUser extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(OrderDelivery_Screen.this,
					"Loading", true, true, this);
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
			String _createCartUrl = "https://www.brandsfever.com/api/v5/orders/luxury/app/";
			String _sendCartpk = DataHolderClass.getInstance()
					.getFinal_cart_pk();
			BasicNameValuePair _senduser_id = new BasicNameValuePair("user_id",
					_getuserId);
			BasicNameValuePair _sendtoken = new BasicNameValuePair("token",
					_getToken);
			BasicNameValuePair _sendcart_pk = new BasicNameValuePair("cart_pk",
					_sendCartpk);
			BasicNameValuePair _sendshipping_first_name = new BasicNameValuePair(
					"shipping-first_name", _fname);
			BasicNameValuePair _sendshipping_last_name = new BasicNameValuePair(
					"shipping-last_name", _lname);
			BasicNameValuePair _sendshipping_address = new BasicNameValuePair(
					"shipping-address", _address);
			BasicNameValuePair _sendshipping_zipcode = new BasicNameValuePair(
					"shipping-zipcode", _zipcode);
			BasicNameValuePair _sendshipping_phone_number = new BasicNameValuePair(
					"shipping-phone_number", _pnmber);
			BasicNameValuePair _sendshipping_district = new BasicNameValuePair(
					"shipping-district", _country);
			BasicNameValuePair _sendshipping_country = new BasicNameValuePair(
					"shipping-country", _country);
			BasicNameValuePair _sendbilling_first_name = new BasicNameValuePair(
					"billing-first_name", _fname);
			BasicNameValuePair _sendbilling_last_name = new BasicNameValuePair(
					"billing-last_name", _lname);
			BasicNameValuePair _sendbilling_address = new BasicNameValuePair(
					"billing-address", _address);
			BasicNameValuePair _sendbilling_zipcode = new BasicNameValuePair(
					"billing-zipcode", _zipcode);
			BasicNameValuePair _sendbilling_phone_number = new BasicNameValuePair(
					"billing-phone_number", _pnmber);
			BasicNameValuePair _sendbilling_district = new BasicNameValuePair(
					"billing-district", _country);
			BasicNameValuePair _sendbilling_country = new BasicNameValuePair(
					"billing-country", _country);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_senduser_id);
			_namevalueList.add(_sendtoken);
			_namevalueList.add(_sendcart_pk);
			_namevalueList.add(_sendshipping_first_name);
			_namevalueList.add(_sendshipping_last_name);
			_namevalueList.add(_sendshipping_address);
			_namevalueList.add(_sendshipping_zipcode);
			_namevalueList.add(_sendshipping_phone_number);
			_namevalueList.add(_sendshipping_district);
			_namevalueList.add(_sendshipping_country);
			_namevalueList.add(_sendbilling_first_name);
			_namevalueList.add(_sendbilling_last_name);
			_namevalueList.add(_sendbilling_address);
			_namevalueList.add(_sendbilling_zipcode);
			_namevalueList.add(_sendbilling_phone_number);
			_namevalueList.add(_sendbilling_district);
			_namevalueList.add(_sendbilling_country);
			_ResponseFromServer = SendData(_createCartUrl, _namevalueList);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject _obj = new JSONObject(_ResponseFromServer);
				String _ret = _obj.getString("ret");
				if (_ret.equalsIgnoreCase("0")) {
					JSONObject obj = _obj.getJSONObject("data");
					String _orderPK = obj.getString("order_pk");
					DataHolderClass.getInstance().set_orderpk(_orderPK);
					Intent _sendforpayment = new Intent(_ctx,
							PaymentScreen.class);
					startActivity(_sendforpayment);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
				}

			} catch (Exception e) {
				// TODO: handle exception
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

	public void _ResponsePopup() {
		View view = View.inflate(OrderDelivery_Screen.this,
				R.layout.error_popop, null);
		_seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText(_msg);
		Toast toast = new Toast(OrderDelivery_Screen.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}
	

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
