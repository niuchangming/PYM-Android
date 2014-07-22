package com.brandsfever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.brandsfever.luxury.R;
import com.dataholder.DataHolderClass;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Facebook.DialogListener;

public class PhoneSignupPage extends Fragment implements OnClickListener {
	
	private static final String TAG = "PhoneSignupPage";
	
	EditText first_name, last_name, email_address, choose_password;
	Typeface _font;
	ImageButton signup_btn, signup_with_fb_btn;
	PhoneSignupPage _sctx;
	String _Fname, _Lname, _Password, _Email;
	String _ResponseRegister, _SocailResponseFromServer;
	SharedPreferences _mypref;
	String _userFBemail;
	Facebook facebook;
	ViewGroup root;
	TextView _seterrormsg;
	String _msg;
	CheckBox checkBox1;
	private String _content;
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

	public static Fragment newInstance(Context context) {
		PhoneSignupPage f = new PhoneSignupPage();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (DataHolderClass.getInstance().get_deviceInch()<=6) {
			root = (ViewGroup) inflater.inflate(R.layout.phone_signup,null);
		}else if(DataHolderClass.getInstance().get_deviceInch()>=7 &&
				DataHolderClass.getInstance().get_deviceInch()<9){
			root = (ViewGroup) inflater.inflate(R.layout.seven_inch_signup_page,null);
		}else if(DataHolderClass.getInstance().get_deviceInch()>=9){
			root = (ViewGroup) inflater.inflate(R.layout.activity_signup_page,null);
		}
		 
		_sctx = this;
		_font = Typeface.createFromAsset(getActivity().getAssets(),	"fonts/georgia.ttf");

		first_name = (EditText) root.findViewById(R.id.first_name);
		first_name.setTypeface(_font, Typeface.NORMAL);

		last_name = (EditText) root.findViewById(R.id.last_name);
		last_name.setTypeface(_font, Typeface.NORMAL);

		email_address = (EditText) root.findViewById(R.id.email_address);
		email_address.setTypeface(_font, Typeface.NORMAL);

		choose_password = (EditText) root.findViewById(R.id.choose_password);
		choose_password.setTypeface(_font, Typeface.NORMAL);
		
		checkBox1 = (CheckBox) root.findViewById(R.id.checkBox1);
		checkBox1.setTypeface(_font, Typeface.NORMAL);

		signup_btn = (ImageButton) root.findViewById(R.id.signup_btn);
		signup_btn.setOnClickListener(this);

		signup_with_fb_btn = (ImageButton) root
				.findViewById(R.id.signup_with_fb_btn);
		signup_with_fb_btn.setOnClickListener(this);

		try {
			facebook = new Facebook(getResources().getString(R.string.fbid));
			SessionStore.restore(facebook, getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return root;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signup_btn:
			try {
				_Fname = first_name.getText().toString();
				_Lname = last_name.getText().toString();
				_Password = choose_password.getText().toString();
				_Email = email_address.getText().toString();
				if(_Fname.length()==0){
					_msg="Please fill first name!";
					_ResponsePopup();
				}else if(_Lname.length()==0){
					_msg="Please fill last name!";
					_ResponsePopup();
				}else if(_Email.length()==0){
					_msg="Please enter email-id!";
					_ResponsePopup();
				}
				else if(_Password.length()==0){
					_msg="Please fill password";
					_ResponsePopup();
				}else if(!checkBox1.isChecked()){
					_msg="Please agree to the terms ";
					_ResponsePopup();
				}
				else if(_Fname.length()>0 && _Lname.length()>0 && _Password.length()>0 && _Email.length()>0){
					if(_Email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
						new RegisterCustomer().execute();
					}else{
						_msg="Invalid email\nPlease enter a valid email!";
						_ResponsePopup();
					}					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.signup_with_fb_btn:
			if (!facebook.isSessionValid()) {
				Toast.makeText(getActivity(), "Redirecting you on facebook", 2)
						.show();
				facebook.authorize(getActivity(), new String[] {
						"publish_stream", "email", "user_groups",
						"read_stream", "user_about_me", "offline_access" }, 1,
						new LoginDialogListener());
			} else {
				facebook();
			}
			break;

		default:
			break;
		}

	}

	private class RegisterCustomer extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(getActivity(),
					"Loading", true, true, this);
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
			// TODO Auto-generated method stub

		}

		@Override
		protected String doInBackground(String... params) {
			String url_register = "https://www.brandsfever.com/api/v5/users/";
			String user_fname = _Fname;
			String user_lname = _Lname;
			String user_Rpassword = _Password;
			String user_emailid = _Email;
			BasicNameValuePair _Ufname = new BasicNameValuePair("first_name",
					user_fname);
			BasicNameValuePair _Ulname = new BasicNameValuePair("last_name",
					user_lname);
			BasicNameValuePair _Upassword = new BasicNameValuePair("password",
					user_Rpassword);
			BasicNameValuePair _Uemail = new BasicNameValuePair("email",
					user_emailid);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_Ufname);
			_namevalueList.add(_Ulname);
			_namevalueList.add(_Upassword);
			_namevalueList.add(_Uemail);
			_ResponseRegister = RegisterUser(url_register, _namevalueList);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobj = new JSONObject(_ResponseRegister);
				String _Ret = jobj.getString("ret");
				if (_Ret.equals("0")) {
					String _token = jobj.getString("token");
					String _UserId = jobj.getString("user");
					_mypref = getActivity().getApplicationContext()
							.getSharedPreferences("mypref", 0);
					Editor prefsEditor = _mypref.edit();
					prefsEditor.clear();
					prefsEditor.putString("ID", _UserId);
					prefsEditor.putString("TOKEN", _token);
					prefsEditor.commit();
					if (!(_UserId.length() == 0) && !(_token.length() == 0)) {
						mProgressHUD.dismiss();
						try {
							new GetUserName(_token,_UserId).execute();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						mProgressHUD.dismiss();
						// server response print
					}
				}else{
					mProgressHUD.dismiss();
					_msg="Username is alreay existed";
					_ResponsePopup();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
		private class GetUserName extends AsyncTask<String, String, String>{
			String _id;
			String mtoken;
			public GetUserName(String token,String id){
				this._id=id;
				this.mtoken=token;
			}

			@Override
			protected String doInBackground(String... params) {
				String _profileurl = "https://www.brandsfever.com/api/v5/profiles/?user_id="
						+ _id + "&token=" + mtoken;
				GetProfile(_profileurl);
				return null;
			}
			@Override
			protected void onPostExecute(String result) {
				try {
					JSONObject obj = new JSONObject(_content);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						JSONObject obj1 = obj.getJSONObject("profile");
								
						String _ss = obj1.getString("first_name");
						_mypref = getActivity().getApplicationContext()
								.getSharedPreferences("mypref", 0);
						Editor prefsEditor = _mypref.edit();
						prefsEditor.putString("_UserName", _ss);
						prefsEditor.commit();
						try {
							((PhoneLoginScreen) getActivity()).refreshPage();
							getActivity().overridePendingTransition(0,R.anim.puch_login_down);
							_msg = "login successful!";
							_ResponsePopup();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
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
					 _content = total.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	public String RegisterUser(String url, List<NameValuePair> _namevalueList) {
		String _RResponse = null;
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
				_RResponse = total.toString();
			} else {
				_RResponse = "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _RResponse;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public final class LoginDialogListener implements DialogListener {
		
		public void onComplete(Bundle values) {
			try {
				SessionStore.save(facebook, getActivity());
				facebook();
			} catch (Exception error) {
				error.printStackTrace();
			}
		}

		public void onCancel() {
		}

		@Override
		public void onFacebookError(FacebookError e) {
		}

		@Override
		public void onError(DialogError e) {
		}
	}

	public void facebook() {

		AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(getActivity());

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setTitle("Getting Facebook Information");
				dialog.setCancelable(false);
				dialog.setIndeterminate(true);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					JSONObject me = new JSONObject(facebook.request("me"));
					String a = me.getString("name");
					String b = me.getString("email");
					_userFBemail = b;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e){
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				new SocailLogin().execute();
				dialog.dismiss();
			}

		};
		updateTask.execute((Void[]) null);
	}

	class SocailLogin extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

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
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}

		@Override
		protected String doInBackground(String... params) {
			String url_socaillogin = "https://www.brandsfever.com/api/v5/social-login/";
			BasicNameValuePair _socailemail = new BasicNameValuePair("email",
					_userFBemail);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_socailemail);
			_SocailResponseFromServer = RegisterUser(url_socaillogin,
					_namevalueList);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobj = new JSONObject(_SocailResponseFromServer);
				String _Ret = jobj.getString("ret");
				if (_Ret.equals("0")) {
					String _token = jobj.getString("token");
					String _UserId = jobj.getString("user_id");
					_mypref = getActivity().getApplicationContext().getSharedPreferences("mypref", 0);
					Editor prefsEditor = _mypref.edit();
					prefsEditor.clear();
					prefsEditor.putString("ID", _UserId);
					prefsEditor.putString("TOKEN", _token);
					prefsEditor.commit();
					if (!(_UserId.length() == 0) && !(_token.length() == 0)) {
						mProgressHUD.dismiss();
						((PhoneLoginScreen) getActivity()).refreshPage();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressHUD.dismiss();
		}
	}
		public void _ResponsePopup() {
			View view = View.inflate(getActivity().getBaseContext(),
					R.layout.error_popop, null);
			_seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
			_seterrormsg.setText(_msg);
			Toast toast = new Toast(getActivity().getBaseContext());
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.show();
		}
		public boolean checkEmail(String checkemailvalidate) {
			return EMAIL_ADDRESS_PATTERN.matcher(checkemailvalidate).matches();
		}
}
