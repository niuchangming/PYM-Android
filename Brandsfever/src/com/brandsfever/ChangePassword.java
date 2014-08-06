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
import android.content.SharedPreferences;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class ChangePassword extends SherlockFragmentActivity implements OnClickListener {
	Context _ctx = ChangePassword.this;
	String _userfname, _userlname, _useremail;
	String _ret;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	Typeface mFont;
	TextView Uname, Uemail, change_password_tag;
	EditText newpassword, confirmnewpassword;
	String _password, _Cpassword;
	Button submit_password;
	String _ResponseFromServer;
	int color, colors;
	LinearLayout parent_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.activity_change_password);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_change_password);
		} else if (a >= 9) {
			setContentView(R.layout.activity_change_password_screen);
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
			}
		});
		mFont = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		newpassword = (EditText) findViewById(R.id.Enter_new_password);
		confirmnewpassword = (EditText) findViewById(R.id.Confirm_new_password);
		parent_layout = (LinearLayout) findViewById(R.id.parent_layout);
		new GetUserProfile().execute();

		parent_layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(newpassword.getWindowToken(), 0);
				return true;
			}
		});

		Uname = (TextView) findViewById(R.id.user_name);
		Uemail = (TextView) findViewById(R.id.user_email);
		change_password_tag = (TextView) findViewById(R.id.change_password_tag);
		Uname.setTypeface(mFont);
		Uemail.setTypeface(mFont);
		change_password_tag.setTypeface(mFont, Typeface.NORMAL);
		newpassword.setTypeface(mFont);
		confirmnewpassword.setTypeface(mFont);

		submit_password = (Button) findViewById(R.id.change_paswrd_submit);
		submit_password.setTypeface(mFont, Typeface.NORMAL);
		submit_password.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": users" + _getuserId + "/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.change_paswrd_submit:
			String _pOne = newpassword.getText().toString();
			String _ptwo = confirmnewpassword.getText().toString();

			if (_pOne.equals(_ptwo) && _pOne.length() > 0) {
				new RPassword().execute();
			} else if (_pOne.length() == 0) {
				String _errormsg = "Please enter \n new password";
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);

				_seterrormsg.setText(_errormsg);
				Toast toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			} else if (_ptwo.length() == 0) {
				String _errormsg = "Please confirm \n new password";
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);
				_seterrormsg.setText(_errormsg);
				Toast toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			} else {
				String _errormsg = "Password doesnot \n match";
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);
				_seterrormsg.setText(_errormsg);
				Toast toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
			break;

		default:
			break;
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
					+ _getuserId + "&token=" + _getToken;
			GetProfile(_profileurl);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				String s = _userfname + " " + _userlname;
				if (!(_userfname.equalsIgnoreCase("null"))
						&& !(_userlname.equalsIgnoreCase("null"))) {
					Uname.setText(s);
				} else {
					Uname.setText("");
				}
				if (!(_useremail.equalsIgnoreCase("null"))) {
					Uemail.setText(_useremail);
				} else {
					Uemail.setText("");
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
						_userfname = obj1.getString("first_name");
						_userlname = obj1.getString("last_name");
						_useremail = obj1.getString("email");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class RPassword extends AsyncTask<String, String, String> implements
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
			String _url = "https://www.brandsfever.com/api/v5/users/"
					+ _getuserId + "/";
			String passwrd = confirmnewpassword.getText().toString();

			BasicNameValuePair _userid = new BasicNameValuePair("user_id",
					_getuserId);
			BasicNameValuePair _token = new BasicNameValuePair("token",
					_getToken);
			BasicNameValuePair _passwrd = new BasicNameValuePair("password",
					passwrd);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();

			_namevalueList.add(_userid);
			_namevalueList.add(_token);
			_namevalueList.add(_passwrd);
			_ResponseFromServer = SendData(_url, _namevalueList);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobj = new JSONObject(_ResponseFromServer);
				String _Ret = jobj.getString("ret");
				if (_Ret.equals("0")) {
					confirmnewpassword.setText("");
					newpassword.setText("");
					try {
						String _errormsg = "Changes Saved";
						LayoutInflater inflater = getLayoutInflater();
						View view = inflater.inflate(R.layout.error_popop,
								(ViewGroup) findViewById(R.id.relativeLayout1));
						TextView _seterrormsg = (TextView) view
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
			// TODO: handle exception
			e.printStackTrace();
		}
		return _Response;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

}
