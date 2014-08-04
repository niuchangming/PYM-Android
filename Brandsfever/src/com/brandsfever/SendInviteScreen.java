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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class SendInviteScreen extends FragmentActivity implements
		OnItemClickListener, OnClickListener {
	ListView _getemails;
	 Context _ctx = SendInviteScreen.this;
	ArrayList<String> _storeEmails = new ArrayList<String>();
	Typeface _font;
	private String _email;
	String _ResponseFromServer;
	Button cancel_invite;
	Button proceed_to_invite;
	private PopupWindow _invitewindow;
	String _retValue;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	ImageButton main_menu, back_btn, cart_btn;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, _mycart, mSupport, _invite, _logout;
	int color,colors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.phone_send_invite_screen);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_send_invite);
		} else if (a >= 9) {
			setContentView(R.layout.ten_inch_send_invite);
		}
		_getemails = (ListView) findViewById(R.id.get_email_list);
		_getemails.setOnItemClickListener(this);
		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16)+0xFF000000;
		colors = Integer.parseInt("ffffff", 16)+0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);
		try {
			new GetContactsFromDevice().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		TextView set_user_name = (TextView)findViewById(R.id.set_user_name);
		String _username = _mypref .getString("_UserName", null);
		if(!(_username==null)){
			set_user_name.setTypeface(_font);
			set_user_name.setText("Hi! "+_username.replace("Hi!",""));
		}else{
			set_user_name.setText("Hi! Guest");
		}

		cart_btn = (ImageButton) findViewById(R.id.cart_btn);
		cart_btn.setOnClickListener(this);

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
		_mycart.setTextColor(colors);
		mSupport.setTextColor(colors);
		_invite.setTextColor(color);
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": invitations/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	// **************************************************************************************************************************//
	class GetContactsFromDevice extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(SendInviteScreen.this, "Loading",
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
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				ContentResolver cr = getContentResolver();
				Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
						null, null, null, null);
				if (cur.getCount() > 0) {
					while (cur.moveToNext()) {
						String name = cur
								.getString(cur
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						if (Integer
								.parseInt(cur.getString(cur
										.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						}
						_storeEmails.add(name);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					SendInviteScreen.this, android.R.layout.simple_list_item_1,
					_storeEmails);
			_getemails.setAdapter(arrayAdapter);
			mProgressHUD.dismiss();
			super.onPostExecute(result);
		}
	}

	// *************************************************************************************************************************//

	@Override
	public void onItemClick(AdapterView<?> adapter, View _view, int position,
			long arg3) {
		_email = _storeEmails.get(position);
		System.out.println("my email is" + _email);
		_InitializePopup();
	}

	// ***********************************************************************************************************************//

	public void _InitializePopup() {

		try {
			LayoutInflater inflater = (LayoutInflater) SendInviteScreen.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.send_invite_popup,
					(ViewGroup) findViewById(R.id.invite_popup));
			_invitewindow = new PopupWindow(layout, 500, 220, true);
			_invitewindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

			_invitewindow.isOutsideTouchable();
			_invitewindow.setOutsideTouchable(true);

			TextView _text = (TextView) layout.findViewById(R.id.invite_email);
			_text.setText(_email);
			cancel_invite = (Button) layout.findViewById(R.id.cancel_invite);
			cancel_invite.setOnClickListener(this);
			proceed_to_invite = (Button) layout
					.findViewById(R.id.proceed_invite);
			proceed_to_invite.setOnClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// ***********************************************************************************************************************//
	class SendEmailInvite extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(SendInviteScreen.this, "Loading",
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
					"invitee_identifiers", _email);
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
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (_retValue.equals("0") || _retValue.equals("502")) {
				_invitewindow.dismiss();
				_ResponsePopup();
				Intent _sucess = new Intent(SendInviteScreen.this,
						InviteFragment.class);
				startActivity(_sucess);
				finish();
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

	// **********************************************************************************************************************//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_invite:
			_invitewindow.dismiss();
			break;
		case R.id.proceed_invite:
			new SendEmailInvite().execute();
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
			Intent _cart = new Intent(_ctx, MyCartFragment.class);
			startActivity(_cart);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_invite:
			Intent _invite = new Intent(_ctx, InviteFragment.class);
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
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cart_btn:
			if (!(_getToken == null) && !(_getuserId == null)) {
				Intent _gotocart = new Intent(_ctx, MyCartFragment.class);
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

	// *******************************************************************************************************************//
	public void _ResponsePopup() {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.error_popop,
				(ViewGroup) findViewById(R.id.pop));
		TextView _seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText("Invite Send Successfully!");
		Toast toast = new Toast(SendInviteScreen.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	// ***************************************************************************************************************//
	@Override
	public void onBackPressed() {
		Intent i = new Intent(SendInviteScreen.this, InviteFragment.class);
		startActivity(i);
	}

}
