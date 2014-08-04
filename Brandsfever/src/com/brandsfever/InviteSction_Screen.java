package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dataholder.DataHolderClass;
import com.datamodel.StoreCreditDetails;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class InviteSction_Screen extends FragmentActivity implements
		OnClickListener {
	Context _ctx = InviteSction_Screen.this;
	Typeface _font;
	ImageButton main_menu, back_btn, cart_btn, fb_friends;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, _mycart, mSupport, _invite, _logout;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	int color, colors;
	ImageButton gmail_contacts, yahoo_contacts, hotmail_contacts,
			msngr_contacts, aol_contacts, send_email_invite;
	EditText enter_email_address;
	String _getEmailAddress, _ResponseFromServer, _retValue;
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
	private String _msg;
	TextView invitation_send, invitation_accepted, store_credit;
	public static ArrayList<StoreCreditDetails> _storeCredits = new ArrayList<StoreCreditDetails>();
	double _creditCount = 0.0;
	int emailInviteSent, facebookInviteSent, emailInviteAccepted,
			facebookInviteAccepted;
	LinearLayout a_three;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.activity_invite_sction__screen);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_invite_module);
		} else if (a >= 9) {
			setContentView(R.layout.activity_invite_section_screen);
		}

		fb_friends = (ImageButton) findViewById(R.id.invite_your_friendz);
		fb_friends.setOnClickListener(this);

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String _username = _mypref.getString("_UserName", null);
		if (!(_username == null)) {
			set_user_name.setTypeface(_font);
			set_user_name.setText("Hi! "+_username.replace("Hi!",""));
		} else {
			set_user_name.setText("Hi! Guest");
		}

		cart_btn = (ImageButton) findViewById(R.id.cart_btn);
		cart_btn.setOnClickListener(this);

		gmail_contacts = (ImageButton) findViewById(R.id.gmail_contacts);
		gmail_contacts.setOnClickListener(this);

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
		_mycart.setTypeface(_font);
		_mycart.setOnClickListener(this);

		mSupport = (Button) findViewById(R.id.btn_support);
		mSupport.setTypeface(_font);
		mSupport.setOnClickListener(this);
		
		_invite = (Button) findViewById(R.id.btn_invite);
		_invite.setTextColor(color);
		_invite.setTypeface(_font);
		_invite.setOnClickListener(this);

		_logout = (Button) findViewById(R.id.btn_logout);
		_logout.setTypeface(_font);
		_logout.setOnClickListener(this);

		_all.setTextColor(colors);
		_men.setTextColor(colors);
		_women.setTextColor(colors);
		_childrens.setTextColor(colors);
		_home.setTextColor(colors);
		_accessories.setTextColor(colors);
		_settings.setTextColor(colors);
		mSupport.setTextColor(colors);
		_mycart.setTextColor(colors);

		gmail_contacts = (ImageButton) findViewById(R.id.gmail_contacts);
		gmail_contacts.setOnClickListener(this);
		yahoo_contacts = (ImageButton) findViewById(R.id.yahoo_contacts);
		yahoo_contacts.setOnClickListener(this);
		hotmail_contacts = (ImageButton) findViewById(R.id.hotmail_contacts);
		hotmail_contacts.setOnClickListener(this);
		msngr_contacts = (ImageButton) findViewById(R.id.msngr_contacts);
		msngr_contacts.setOnClickListener(this);
		aol_contacts = (ImageButton) findViewById(R.id.aol_contacts);
		aol_contacts.setOnClickListener(this);
		send_email_invite = (ImageButton) findViewById(R.id.send_email_invite);
		send_email_invite.setOnClickListener(this);
		enter_email_address = (EditText) findViewById(R.id.enter_email_address);
		invitation_send = (TextView) findViewById(R.id.invitation_send);
		invitation_accepted = (TextView) findViewById(R.id.invitation_accepted);
		store_credit = (TextView) findViewById(R.id.store_credit);

		a_three = (LinearLayout) findViewById(R.id.a_three);
		a_three.setOnClickListener(this);

		new GetRequestCount().execute();
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": invitations/"+_getuserId+"/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_menu:
			slide_me.toggleLeftDrawer();
			break;

		case R.id.invite_your_friendz:
			Intent _fbinvite = new Intent(InviteSction_Screen.this,
					GetFacebookData.class);
			startActivity(_fbinvite);

			break;

		case R.id.gmail_contacts:
			Intent _gmail = new Intent(InviteSction_Screen.this,
					SendInviteScreen.class);
			startActivity(_gmail);
			finish();
			break;

		case R.id.yahoo_contacts:
			Intent _yahoo = new Intent(InviteSction_Screen.this,
					SendInviteScreen.class);
			startActivity(_yahoo);
			finish();
			break;

		case R.id.hotmail_contacts:
			Intent _hotmail = new Intent(InviteSction_Screen.this,
					SendInviteScreen.class);
			startActivity(_hotmail);
			finish();
			break;

		case R.id.msngr_contacts:
			Intent _msn = new Intent(InviteSction_Screen.this,
					SendInviteScreen.class);
			startActivity(_msn);
			finish();
			break;

		case R.id.aol_contacts:
			Intent _aol = new Intent(InviteSction_Screen.this,
					SendInviteScreen.class);
			startActivity(_aol);
			finish();
			break;

		case R.id.send_email_invite:
			_getEmailAddress = enter_email_address.getText().toString();
			if (_getEmailAddress.length() > 0) {
				if (checkEmail(_getEmailAddress)) {
					new SendEmailInvite().execute();
				} else {
					_msg = "Please enter a \n valid email!";
					_ResponsePopup();
				}
			} else {
				_msg = "Please enter a \n email!";
				_ResponsePopup();
			}

			break;
		case R.id.btn_all_cat:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent all = new Intent(_ctx, ProductDisplay.class);
				all.putExtra("tab", "all");
				startActivity(all);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_men:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent men = new Intent(_ctx, ProductDisplay.class);
				men.putExtra("tab", "men");
				startActivity(men);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_women:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent women = new Intent(_ctx, ProductDisplay.class);
				women.putExtra("tab", "women");
				startActivity(women);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_children:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent children = new Intent(_ctx, ProductDisplay.class);
				children.putExtra("tab", "children");
				startActivity(children);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_home:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent home = new Intent(_ctx, ProductDisplay.class);
				home.putExtra("tab", "home");
				startActivity(home);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_accesories:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent acc = new Intent(_ctx, ProductDisplay.class);
				acc.putExtra("tab", "accessories");
				startActivity(acc);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.btn_setting:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
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
			}
			break;

		case R.id.my_cart:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				Intent _cart = new Intent(_ctx, MyCartScreen.class);
				startActivity(_cart);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.btn_invite:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				Intent _invite = new Intent(_ctx, InviteSction_Screen.class);
				startActivity(_invite);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.btn_logout:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				Editor editor = _mypref.edit();
				editor.clear();
				editor.commit();
				Intent _intent = new Intent(getApplicationContext(),
						ProductDisplay.class);
				startActivity(_intent);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			}
			break;

		case R.id.back_btn:
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cart_btn:
			if (!(_getToken == null) && !(_getuserId == null)) {
				Intent _gotocart = new Intent(_ctx, MyCartScreen.class);
				startActivity(_gotocart);
				finish();
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			} else {
				Toast.makeText(_ctx, "please login", Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}

	}

	

	// *************************************************************************************************************************//
	// *************************************************************************************************************************//
	class SendEmailInvite extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(InviteSction_Screen.this,
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

		}

		@Override
		protected String doInBackground(String... params) {
			String _url = "https://www.brandsfever.com/api/v5/invitations/";
			BasicNameValuePair _uid = new BasicNameValuePair("user_id",
					_getuserId);
			BasicNameValuePair _ut = new BasicNameValuePair("token", _getToken);
			BasicNameValuePair _apply_action = new BasicNameValuePair(
					"channel", "email");
			BasicNameValuePair _store_credits = new BasicNameValuePair(
					"invitee_identifiers", _getEmailAddress);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_uid);
			_namevalueList.add(_ut);
			_namevalueList.add(_apply_action);
			_namevalueList.add(_store_credits);
			_ResponseFromServer = SendData(_url, _namevalueList);
			Log.e("===RESPONSE====>", "===RESPONSE====>" + _ResponseFromServer);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject obj = new JSONObject(_ResponseFromServer);
				_retValue = obj.getString("ret");
				if(_retValue.equals("502")){
					enter_email_address.setText("");
					_msg = "Invitee is a already \n member";
					_ResponsePopup();
				}
				else if(_retValue.equals("0")){
					enter_email_address.setText("");
					_msg = "Successfully Invited";
					_ResponsePopup();
				}else{
					enter_email_address.setText("");
					_msg = "Invitation failed";
					_ResponsePopup();
				}
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressHUD.dismiss();
		}

	}

	// *********************************************************************************************************************//
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
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				_Response = "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _Response;
	}

	// *****************************************************************************************************************************//
	public void _ResponsePopup() {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.error_popop,
				(ViewGroup) findViewById(R.id.pop));
		TextView _seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText(_msg);
		Toast toast = new Toast(InviteSction_Screen.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	// ***********************************************************************************************************************//
	private boolean checkEmail(String checkemailvalidate) {
		return EMAIL_ADDRESS_PATTERN.matcher(checkemailvalidate).matches();
	}

	// **************************************************************************************************************************//
	private class GetRequestCount extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(InviteSction_Screen.this,
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
			String _inviteUrl = "https://www.brandsfever.com/api/v5/invitations/?user_id="
					+ _getuserId + "&token=" + _getToken;
			GetInvitations(_inviteUrl);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			String setemailinvite = emailInviteSent + "/" + facebookInviteSent;
			String setfacebookinvite = facebookInviteSent + "/"
					+ facebookInviteAccepted;
			invitation_send.setText(setemailinvite);
			invitation_accepted.setText(setfacebookinvite);
			new GetStoreCredit().execute();
			mProgressHUD.dismiss();
		}
	}

	// *************************************************************************************************************************************//
	public void GetInvitations(String _urls) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(_urls);
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
				System.out.println(_content);
				try {
					JSONObject obj = new JSONObject(_content);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						JSONObject obj1 = obj.getJSONObject("invitations");
						JSONObject _facebook = obj1
								.getJSONObject("by_facebook");
						JSONObject _facebookpending = _facebook
								.getJSONObject("pending");
						_facebookpending.length();
						JSONArray facebookaccepted = _facebook
								.getJSONArray("accepted");
						facebookaccepted.length();
						for (int i = 0; i < facebookaccepted.length(); i++) {

						}
						JSONObject _byemail = obj1.getJSONObject("by_email");
						JSONObject _pendingemail = _byemail
								.getJSONObject("pending");
						_pendingemail.length();
						JSONArray _acceptedemail = _byemail
								.getJSONArray("accepted");
						_acceptedemail.length();

						emailInviteSent = _pendingemail.length()
								+ _acceptedemail.length();
						facebookInviteSent = _facebookpending.length()
								+ facebookaccepted.length();
						emailInviteAccepted = _acceptedemail.length();
						facebookInviteAccepted = facebookaccepted.length();
						System.out.println("length is " + emailInviteSent
								+ facebookInviteSent + emailInviteAccepted
								+ facebookInviteAccepted);

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

	// *************************************************************************************************************************//
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

			String setcredits = "" + _creditCount;
			double sub = Double.parseDouble(setcredits);
			double finalValue = Math.round(sub * 100.0) / 100.0;
			String f = "S$" + finalValue;
			store_credit.setText(f);

		}

	}

	// ****************************************************************************************************************************//
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
						for (int i = 0; i < get_credit_details.length(); i++) {
							JSONObject jsonobj = get_credit_details
									.getJSONObject(i);
							String one = jsonobj.getString("granted_by");
							String two = jsonobj.getString("expired_at");
							String three = jsonobj.getString("amount");
							String four = jsonobj.getString("redeemed_order");
							String five = jsonobj.getString("is_redeemable");
							if (five.equalsIgnoreCase("true")) {
								_creditCount = _creditCount
										+ Double.parseDouble(three);
							}
							String six = jsonobj.getString("pk");
							String seven = jsonobj.getString("redeemed_at");
							String eight = jsonobj.getString("state");
							String nine = jsonobj.getString("channel");

							StoreCreditDetails _credits = new StoreCreditDetails();
							_credits.setGranted_by(one);
							_credits.setAmount(two);
							_credits.setExpired_at(three);
							_credits.setChannel(nine);
							_credits.setIs_redeemable(five);
							_credits.setPk(six);
							_credits.setState(eight);
							_credits.setRedeemed_at(seven);
							_credits.setRedeemed_order(four);

							_storeCredits.add(_credits);

						}
						System.out.println("store credit id" + _creditCount);
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
	// *********************************************************************************************************************
	
	/*@Override
	public void onBackPressed() {
		Intent i = new Intent(_ctx, ProductDisplay.class);
		startActivity(i);
		finish();
	}*/
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
