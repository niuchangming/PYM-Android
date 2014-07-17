package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
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

public class Edit_Profile extends FragmentActivity implements OnClickListener {
	Context _ctx = Edit_Profile.this;
	String _ret;
	ImageButton main_menu, back_btn, cart_btn;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women,_login,
			_settings, _mycart, _invite, mSupport, _logout;
	EditText _firstname, _lastname, _zender, _birthdate, _mobileno;
	private String Ufname, Ulname, Urupdates, Upenglish, Umno, Ubdate, Ugender,
			Uemail;
	ImageButton save_edited_profile;
	SharedPreferences _mypref;
	TextView setusername, setuseremail, edit_my_profile, mob_text;
	CheckBox _checkbox1, _checkbox2;
	String _getToken = "";
	String _getuserId = "";
	String _EditResponse;
	Calendar DateandTime;
	Button cancel_zender;
	String _checkone, _checktwo;
	Button done_zender;
	private PopupWindow _zender_pop;
	TextView female_text, male_text;
	String _setZender = "";
	Typeface mFont;
	TextView _seterrormsg;
	int color, colors;
	
	private String _msg="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.activity_edit__profile);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_edit_my_profile);
		} else if (a >= 9) {
			setContentView(R.layout.activity_edit_your_profile_screen);
		}

		mFont = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		System.out.println(_getToken + _getuserId);

		edit_my_profile = (TextView) findViewById(R.id.edit_my_profile);
		mob_text = (TextView) findViewById(R.id.mob_text);
		edit_my_profile.setTypeface(mFont, Typeface.NORMAL);
		_firstname = (EditText) findViewById(R.id.Enter_firstname);
		_lastname = (EditText) findViewById(R.id.Enter_lastname);
		_zender = (EditText) findViewById(R.id.Enter_gender);
		_zender.setFocusable(false);
		_zender.setFocusableInTouchMode(false);
		_zender.setOnClickListener(this);
		_birthdate = (EditText) findViewById(R.id.Enter_birthdate);
		_birthdate.setFocusable(false);
		_birthdate.setFocusableInTouchMode(false);
		_birthdate.setOnClickListener(this);
		_mobileno = (EditText) findViewById(R.id.Enter_mobile_number);
		_zender.setTypeface(mFont);
		_birthdate.setTypeface(mFont);
		_mobileno.setTypeface(mFont);
		_firstname.setTypeface(mFont);
		_lastname.setTypeface(mFont);
		mob_text.setTypeface(mFont);
		_checkbox1 = (CheckBox) findViewById(R.id.checkBox1);
		_checkbox2 = (CheckBox) findViewById(R.id.checkBox2);

		save_edited_profile = (ImageButton) findViewById(R.id.save_edited_profile);
		save_edited_profile.setOnClickListener(this);

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));
		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String _username = _mypref.getString("_UserName", null);
		if (!(_username == null)) {
			set_user_name.setTypeface(mFont);
			set_user_name.setText("Hi! "+_username.replace("Hi!",""));
		} else {
			set_user_name.setText("Hi! Guest");
		}

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		back_btn = (ImageButton) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

		cart_btn = (ImageButton) findViewById(R.id.cart_btn);
		cart_btn.setOnClickListener(this);

		_all = (Button) findViewById(R.id.btn_all_cat);
		_all.setTypeface(mFont);
		_all.setOnClickListener(this);

		_men = (Button) findViewById(R.id.cat_shoes);
		_men.setTypeface(mFont);
		_men.setOnClickListener(this);

		_women = (Button) findViewById(R.id.cat_handbags);
		_women.setTypeface(mFont);
		_women.setOnClickListener(this);

		_login = (Button) findViewById(R.id.btn_login);
		_login.setVisibility(View.GONE);

		_settings = (Button) findViewById(R.id.btn_setting);
		_settings.setTypeface(mFont);
		_settings.setOnClickListener(this);

		_mycart = (Button) findViewById(R.id.my_cart);
		_mycart.setTypeface(mFont);
		_mycart.setOnClickListener(this);

		mSupport = (Button) findViewById(R.id.btn_support);
		mSupport.setTypeface(mFont);
		mSupport.setOnClickListener(this);
		
		_invite = (Button) findViewById(R.id.btn_invite);
		_invite.setTypeface(mFont);
		_invite.setOnClickListener(this);

		_logout = (Button) findViewById(R.id.btn_logout);
		_logout.setTypeface(mFont);
		_logout.setOnClickListener(this);

		_all.setTextColor(colors);
		_men.setTextColor(colors);
		_women.setTextColor(colors);
		_settings.setTextColor(color);
		_mycart.setTextColor(colors);
		mSupport.setTextColor(colors);
		_invite.setTextColor(colors);

		new GetUserProfile().execute();
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": profiles/"+_getuserId+"/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Enter_birthdate:
			DateandTime = Calendar.getInstance(Locale.US);
			DatePickerDailog dp = new DatePickerDailog(_ctx, DateandTime,
					new DatePickerDailog.DatePickerListner() {
						@SuppressLint("SimpleDateFormat")
						@Override
						public void OnDoneButton(Dialog datedialog, Calendar c) {
							datedialog.dismiss();
							DateandTime.set(Calendar.YEAR, c.get(Calendar.YEAR));
							DateandTime.set(Calendar.MONTH,c.get(Calendar.MONTH));
							DateandTime.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH));
							_birthdate.setText(new SimpleDateFormat(
									"yyyy-MM-dd").format(c.getTime()));
						}

						@Override
						public void OnCancelButton(Dialog datedialog) {
							datedialog.dismiss();
						}
					});
			dp.show();
			break;

		case R.id.Enter_gender:
			FillZenderPopup();
			break;

		case R.id.cancel_zender:
			_zender_pop.dismiss();
			break;

		case R.id.Done_zender:
			if (!(_setZender == null) && !(_setZender.equals(""))) {
				_zender.setText(_setZender);
				_zender_pop.dismiss();
			} else {
				// please select zender image toast
				System.out.println("zender is null or blank;");
			}

			break;

		case R.id.save_edited_profile:
			if(_firstname.getText().toString().length()==0){
				_msg="Please fill first name!";
				_ResponsePopup();
			}else if(_lastname.getText().toString().length()==0){
				_msg="Please fill last name!";
				_ResponsePopup();
			}else if(_zender.getText().toString().length()==0){
				_msg="Please fill the gender";
				_ResponsePopup();
			}
			else if(_birthdate.getText().toString().length()==0){
				_msg="Please fill the birthday \n date";
				_ResponsePopup();
			}else if(_mobileno.getText().toString().length()<6){
				_msg="Please fill valid mobile \n number";
				_ResponsePopup();
			}
			else {
			new SaveEditedinfo().execute();
				}
			break;

		case R.id.female:
			female_text.setTextColor(Color.GRAY);
			male_text.setTextColor(Color.BLACK);
			_setZender = "F";
			break;

		case R.id.male:
			male_text.setTextColor(Color.GRAY);
			female_text.setTextColor(Color.BLACK);
			_setZender = "M";
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

				} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
						&& DataHolderClass.getInstance().get_deviceInch() < 9) {
					Intent _tabsetting = new Intent(_ctx, SettingTab.class);
					System.out.println("in 7 inch tab");
					startActivity(_tabsetting);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
				} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
					Intent _tabsetting = new Intent(_ctx, SettingTab.class);
					System.out.println("in 10 inch tab");
					startActivity(_tabsetting);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
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
			finish();
			break;

		case R.id.cart_btn:
			if (!(_getToken == null) && !(_getuserId == null)) {
				Intent _gotocart = new Intent(_ctx, MyCartScreen.class);
				startActivity(_gotocart);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			} else {

			}
			break;

		default:
			break;
		}

	}

	/**********************************************************************************************************************/

	public void FillZenderPopup() {
		try {
			LayoutInflater inflater = (LayoutInflater) Edit_Profile.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.zender_popup,
					(ViewGroup) findViewById(R.id.zender_custom));

			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				_zender_pop = new PopupWindow(layout,
						android.view.ViewGroup.LayoutParams.FILL_PARENT, 300,
						true);
				_zender_pop.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
			} else {
				_zender_pop = new PopupWindow(layout,
						android.view.ViewGroup.LayoutParams.FILL_PARENT, 250,
						true);
				_zender_pop.showAtLocation(layout, Gravity.BOTTOM, 10, 10);
			}

			cancel_zender = (Button) layout.findViewById(R.id.cancel_zender);
			cancel_zender.setOnClickListener(this);

			done_zender = (Button) layout.findViewById(R.id.Done_zender);
			done_zender.setOnClickListener(this);

			male_text = (TextView) layout.findViewById(R.id.male);
			male_text.setOnClickListener(this);

			female_text = (TextView) layout.findViewById(R.id.female);
			female_text.setOnClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ============================================================================================================================//
	class GetUserProfile extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(_ctx, "Loading", true, true, this);
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
			String _profileurl = "https://www.brandsfever.com/api/v5/profiles/?user_id="
					+ _getuserId + "&token=" + _getToken;
			System.out.println("url is" + _profileurl);
			GetProfile(_profileurl);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				if (!(Ufname.equalsIgnoreCase("null"))) {
					_firstname.setText(Ufname);
				} else {
					_firstname.setText("");
				}
				if (!(Ulname.equalsIgnoreCase("null"))) {
					_lastname.setText(Ulname);
				} else {
					_lastname.setText("");
				}
				if (!(Ugender.equalsIgnoreCase("null"))) {
					_zender.setText(Ugender);
				} else {
					_zender.setText("");
					System.out.println("i am in else");
				}
				if (!(Umno.equalsIgnoreCase("null"))) {
					_mobileno.setText(Umno);
				} else {
					_mobileno.setText("");
				}
				if (!(Ubdate.equalsIgnoreCase("null"))) {
					_birthdate.setText(Ubdate);
				} else {
					_birthdate.setText("");
				}

				if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
					setuseremail = (TextView) findViewById(R.id.user_email);
					setusername = (TextView) findViewById(R.id.user_id);
					if (!(Uemail.equalsIgnoreCase("null"))) {
						setuseremail.setText(Uemail);
					} else {
						setuseremail.setText("");
					}

					String _sName = "Hi!" + "" + Ufname + " " + Ulname;
					if (!(Ufname.equalsIgnoreCase("null"))
							&& !(Ulname.equalsIgnoreCase("null"))) {
						setusername.setText(_sName);
					} else {
						setusername.setText("");
					}

					setuseremail.setTypeface(mFont, Typeface.NORMAL);
					setusername.setTypeface(mFont, Typeface.BOLD);
					TextView edit_my_profile_line = (TextView) findViewById(R.id.edit_my_profile_line);
					edit_my_profile_line.setTypeface(mFont, Typeface.NORMAL);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressHUD.dismiss();
		}
	}

	private void GetProfile(String url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(url);
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
				System.out.println("content is" + _content);

				try {
					JSONObject obj = new JSONObject(_content);
					String ret = obj.getString("ret");
					System.out.println(ret);
					if (ret.equals("0")) {
						JSONObject obj1 = obj.getJSONObject("profile");
						Ufname = obj1.getString("first_name");
						Ulname = obj1.getString("last_name");
						Urupdates = obj1.getString("receive_updates");
						Upenglish = obj1.getString("prefer_english");
						Umno = obj1.getString("mobile_number");
						Ubdate = obj1.getString("birth_date");
						Ugender = obj1.getString("gender");
						Uemail = obj1.getString("email");
						if (Urupdates.equalsIgnoreCase("true")) {
							System.out.println("pankaj");
							_checkbox1.setChecked(true);
						} else {

						}
						if (Upenglish.equalsIgnoreCase("true")) {
							_checkbox2.setChecked(true);
						}
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

	/********************************************************************************************************************/
	class SaveEditedinfo extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(_ctx, "Loading", true, true, this);
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
			String _url = "https://www.brandsfever.com/api/v5/profiles/"
					+ _getuserId + "/";
			System.out.println(_url);
			String firstname = _firstname.getText().toString();
			String lastname = _lastname.getText().toString();
			String gender = _zender.getText().toString();
			String birthdate = _birthdate.getText().toString();

			if (_checkbox1.isChecked()) {
				_checkone = "true";
			} else {
				_checkone = "false";
			}

			if (_checkbox2.isChecked()) {
				_checktwo = "true";
			} else {
				_checktwo = "false";
			}

			System.out.println("parameters are" + firstname + "" + lastname
					+ "" + _zender + "" + birthdate + "" + _checkone + ""
					+ _checktwo + _getToken + "" + _getuserId);

			BasicNameValuePair _fname = new BasicNameValuePair("first_name",
					firstname);
			BasicNameValuePair _lname = new BasicNameValuePair("last_name",
					lastname);
			BasicNameValuePair _gender = new BasicNameValuePair("gender",
					gender);
			BasicNameValuePair _birthdate = new BasicNameValuePair(
					"birth_date", birthdate);
			BasicNameValuePair _muserid = new BasicNameValuePair("user_id",
					_getuserId);
			BasicNameValuePair _mtoken = new BasicNameValuePair("token",
					_getToken);
			BasicNameValuePair _checksOne = new BasicNameValuePair(
					"receive_updates", _checkone);
			BasicNameValuePair _checksTwo = new BasicNameValuePair(
					"prefer_english", _checktwo);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();

			_namevalueList.add(_fname);
			_namevalueList.add(_lname);
			_namevalueList.add(_gender);
			_namevalueList.add(_birthdate);
			_namevalueList.add(_muserid);
			_namevalueList.add(_mtoken);
			_namevalueList.add(_checksOne);
			_namevalueList.add(_checksTwo);

			if (firstname.length() > 0 && lastname.length() > 0
					&& !(_getuserId == null)) {
				_EditResponse = _SendEditedInfo(_url, _namevalueList);
				Log.e("===RESPONSE====>", "===RESPONSE====>" + _EditResponse);
			} else {
				System.out.println("pankaj");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (_ret.equals("0")) {
				mProgressHUD.dismiss();
				try {
					String _errormsg = "Changes Saved";
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.relativeLayout1));
					_seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText(_errormsg);
					Toast toast = new Toast(_ctx);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				mProgressHUD.dismiss();
			}
		}
	}

	// =================================================================================================================//
	public String _SendEditedInfo(String url, List<NameValuePair> _namevalueList) {
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
				InputStream _inputstream = _httpresponse.getEntity().getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(_inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				_Response = total.toString();
				try {
					JSONObject obj = new JSONObject(_Response);
					_ret = obj.getString("ret");
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

	// ==================================================================================================================//
	
	public void _ResponsePopup() {
		View view = View.inflate(getBaseContext(),R.layout.error_popop, null);
		_seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText(_msg);
		Toast toast = new Toast(getBaseContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}
	
	
	
	
	// ==================================================================================================================//
	@Override
	public void onBackPressed() {
		finish();
	}
}
