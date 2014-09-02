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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class EditProfile extends SherlockFragmentActivity implements
		OnClickListener {
	Context _ctx = EditProfile.this;
	EditText _firstname, _lastname, _zender, _birthdate, _mobileno;
	private String Ufname, Ulname, Urupdates, Upenglish, Umno, Ubdate, Ugender,
			Uemail;
	ImageButton save_edited_profile;
	SharedPreferences mPref;
	TextView setusername, setuseremail, edit_my_profile, mob_text;
	CheckBox _checkbox1, _checkbox2;
	String mToken = "";
	String mUserId = "";
	Calendar mDateAndTime;
	Button cancel_zender;
	String _checkone, _checktwo;
	Button done_zender;
	private PopupWindow _zender_pop;
	TextView female_text, male_text;
	String _setZender = "";
	Typeface mFont;
	int color, colors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.activity_edit__profile);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_edit_my_profile);
		} else if (a >= 9) {
			setContentView(R.layout.activity_edit_your_profile_screen);
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
		mFont = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		mPref= getApplicationContext().getSharedPreferences("mypref", 0);
		mUserId = mPref.getString("ID", null);
		mToken = mPref.getString("TOKEN", null);

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

		new GetUserProfile().execute();
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": profiles/" + mUserId + "/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.Enter_birthdate:
			mDateAndTime = Calendar.getInstance(Locale.US);
			DatePickerDailog dp = new DatePickerDailog(_ctx, mDateAndTime,
					new DatePickerDailog.DatePickerListner() {
						@SuppressLint("SimpleDateFormat")
						@Override
						public void OnDoneButton(Dialog datedialog, Calendar c) {
							datedialog.dismiss();
							mDateAndTime
									.set(Calendar.YEAR, c.get(Calendar.YEAR));
							mDateAndTime.set(Calendar.MONTH,
									c.get(Calendar.MONTH));
							mDateAndTime.set(Calendar.DAY_OF_MONTH,
									c.get(Calendar.DAY_OF_MONTH));
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
			}
			break;

		case R.id.save_edited_profile:
			if (_firstname.getText().toString().length() == 0) {
				responsePopup("Please fill first name!");
			} else if (_lastname.getText().toString().length() == 0) {
				responsePopup("Please fill last name!");
			} else if (_zender.getText().toString().length() == 0) {
				responsePopup("Please fill the gender");
			} else if (_birthdate.getText().toString().length() == 0) {
				responsePopup("Please fill the birthday \n date");
			} else if (_mobileno.getText().toString().length() < 6) {
				responsePopup("Please fill valid mobile \n number");
			} else {
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

		default:
			break;
		}

	}

	public void FillZenderPopup() {
		try {
			LayoutInflater inflater = (LayoutInflater) EditProfile.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.zender_popup,
					(ViewGroup) findViewById(R.id.zender_custom));

			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				_zender_pop = new PopupWindow(layout,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT, 300,
						true);
				_zender_pop.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
			} else {
				_zender_pop = new PopupWindow(layout,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT, 250,
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
					+ mUserId + "&token=" + mToken;
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
			String url = "https://www.brandsfever.com/api/v5/profiles/"
					+ mUserId + "/";
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

			BasicNameValuePair fname = new BasicNameValuePair("first_name",
					firstname);
			BasicNameValuePair lname = new BasicNameValuePair("last_name",
					lastname);
			BasicNameValuePair genderPair = new BasicNameValuePair("gender",
					gender);
			BasicNameValuePair birthdatePair = new BasicNameValuePair(
					"birth_date", birthdate);
			BasicNameValuePair muserid = new BasicNameValuePair("user_id",
					mUserId);
			BasicNameValuePair mtoken = new BasicNameValuePair("token",
					mToken);
			BasicNameValuePair checksOne = new BasicNameValuePair(
					"receive_updates", _checkone);
			BasicNameValuePair checksTwo = new BasicNameValuePair(
					"prefer_english", _checktwo);
			List<NameValuePair> namevalueList = new ArrayList<NameValuePair>();

			namevalueList.add(fname);
			namevalueList.add(lname);
			namevalueList.add(genderPair);
			namevalueList.add(birthdatePair);
			namevalueList.add(muserid);
			namevalueList.add(mtoken);
			namevalueList.add(checksOne);
			namevalueList.add(checksTwo);
			
			String result = null;
			if (firstname.length() > 0 && lastname.length() > 0
					&& !(mUserId == null)) {
				result = sendEditedInfo(url, namevalueList);
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null && result.equals("0")) {
				mProgressHUD.dismiss();
				responsePopup("Changes Saved");
				finish();
			} else {
				mProgressHUD.dismiss();
			}
		}
	}

	public String sendEditedInfo(String url, List<NameValuePair> namevalueList) {
		String response = null;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpclient = HttpsClient.getNewHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(namevalueList,
					HTTP.UTF_8));
			HttpResponse httpresponse = httpclient.execute(httppost);
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
				JSONObject obj = new JSONObject(total.toString());
				response = obj.getString("ret");
			} else {
				response = "Please try again later.";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public void responsePopup(String msg) {
		View view = View.inflate(getBaseContext(), R.layout.error_popop, null);
		TextView errormsg = (TextView) view.findViewById(R.id._seterrormsg);
		errormsg.setText(msg);
		Toast toast = new Toast(getBaseContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	private void directToCart() {

		SharedPreferences mypref = getApplicationContext()
				.getSharedPreferences("mypref", 0);
		String username = mypref.getString("UserName", null);

		if (username != null) { // check login status
			Intent gotocart = new Intent(EditProfile.this,
					MyCartActivity.class);
			startActivity(gotocart);
		} else {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.error_popop,
					(ViewGroup) findViewById(R.id.relativeLayout1));
			final TextView msgTextView = (TextView) view
					.findViewById(R.id._seterrormsg);
			msgTextView.setText("Please login!");
			Toast toast = new Toast(EditProfile.this);
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
