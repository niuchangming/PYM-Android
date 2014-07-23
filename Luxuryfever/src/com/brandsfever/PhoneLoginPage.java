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
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.brandsfever.luxury.R;
import com.dataholder.DataHolderClass;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;

public class PhoneLoginPage extends Fragment implements OnClickListener {
	
	private static final String TAG = "PhoneLoginPage";
	
	private EditText email, password;
	private ImageButton submit, signup, phone_fb_login;
	private TextView forgot_password;
	private String _emailId, _password;
    SharedPreferences _mypref;
	private String _ResponseFromServer, _SocailResponseFromServer,_ResetResponseFromServer;
	Facebook facebook;
	private Typeface _font;
	private	String mFBEmail;
	private String mFBFirstName;
	private String mFBLastName;
	private String mFBGender;
	private TextView _seterrormsg;
	private String _msg;
	private String _content;
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");
	ViewGroup root;
	ProgressHUD mProgressHUD;
	Button cancel_password,reset_password;
	EditText resetEmail;
	private PopupWindow pwindo;
	String _getEmail;
	InputMethodManager imm;

	public static Fragment newInstance(Context context) {
		PhoneLoginPage f = new PhoneLoginPage();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(DataHolderClass.getInstance().get_deviceInch()<=6){
			root = (ViewGroup) inflater.inflate(R.layout.phone_login,null);
		}else if(DataHolderClass.getInstance().get_deviceInch()>=7 &&
				DataHolderClass.getInstance().get_deviceInch()<9){
			root = (ViewGroup) inflater.inflate(R.layout.seven_inch_login_page,null);
		}else if(DataHolderClass.getInstance().get_deviceInch()>=9){
			root = (ViewGroup) inflater.inflate(R.layout.activity_login_page,null);
		} 
		_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/georgia.ttf");
		
		
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		
		email = (EditText) root.findViewById(R.id.user_id);
		email.setTypeface(_font, Typeface.NORMAL);
		password = (EditText) root.findViewById(R.id.user_password);
		password.setTypeface(_font, Typeface.NORMAL);
		submit = (ImageButton) root.findViewById(R.id.hit_phone_login);
		submit.setOnClickListener(this);

		phone_fb_login = (ImageButton) root.findViewById(R.id.phone_fb_login);
		phone_fb_login.setOnClickListener(this);
		
		forgot_password = (TextView) root.findViewById(R.id.forgot_password);
		forgot_password.setOnClickListener(this);

		signup = (ImageButton) root.findViewById(R.id.go_to_register);
		signup.setOnClickListener(this);

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
		case R.id.forgot_password:
			try {
				forgotPassword();
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
			
		case R.id.cancel_password:
			
			closeKeyboard(getActivity(), resetEmail.getWindowToken());
			pwindo.dismiss();
			break;
			
		case R.id.reset_password:
			_getEmail = resetEmail.getText().toString();
			if(_getEmail.length()>0 && checkEmail(_getEmail)){
				new ResetUserPassword().execute();
			}else{
				_msg="Please enter a valid email!!";
				responsePopup();
			}
			break;

		case R.id.hit_phone_login:
			_emailId = email.getText().toString();
			_password = password.getText().toString();
			if (_emailId.length() > 0 && _password.length() > 0) {
				if(checkEmail(_emailId)){
					new PerformLogin().execute();
				}else{
					_msg = "Please enter a\nvalid email-id!";
                    responsePopup();
				}
				
			}else if (_emailId.length() == 0) {
				try {
                     _msg = "Please enter email";
                     responsePopup();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (_password.length() == 0) {
				try {
					 _msg = "Please enter password";
					 responsePopup();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Log.i(TAG, "popup here");
			}
			break;

		case R.id.go_to_register:
			((PhoneLoginScreen) getActivity())._ChangeTabs();
			break;
		case R.id.phone_fb_login:
			Toast.makeText(getActivity(), "Redirecting you on facebook", 2)
					.show();
			facebook.authorize(getActivity(), new String[] {
					"publish_stream", "email", "user_groups",
					"read_stream", "user_about_me", "offline_access" }, 1,
					new LoginDialogListener());
			break;

		default:
			break;
		}
	}
	

	public static void closeKeyboard(Context c, IBinder windowToken) {
	    InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
	    mgr.hideSoftInputFromWindow(windowToken, 0);
	}

	private class PerformLogin extends AsyncTask<String, String, String>
			implements OnCancelListener {
		

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
		protected String doInBackground(String... params) {
			String url_login = "https://www.brandsfever.com/api/v5/auth/";
			String user_email = _emailId;
			String user_password = _password;
			try{
				BasicNameValuePair emailpair = new BasicNameValuePair("email",	user_email);
				BasicNameValuePair passwordpair = new BasicNameValuePair("password", user_password);
				List<NameValuePair> namevalueList = new ArrayList<NameValuePair>();
				namevalueList.add(emailpair);
				namevalueList.add(passwordpair);
				_ResponseFromServer = SendData(url_login, namevalueList);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return _ResponseFromServer;
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobj = new JSONObject(_ResponseFromServer);
				String _Ret = jobj.getString("ret");
				if (_Ret.equals("0")) {
					String _token = jobj.getString("token");
					String _UserId = jobj.getString("user_id");
					_mypref = getActivity().getApplicationContext()
							.getSharedPreferences("mypref", 0);
					Editor prefsEditor = _mypref.edit();
					prefsEditor.clear();
					prefsEditor.putString("ID", _UserId);
					prefsEditor.putString("TOKEN", _token);
					prefsEditor.commit();
					if (!(_UserId.length() == 0) && !(_token.length() == 0)) {
						new GetUserName(_token,_UserId).execute();
					} else {
						_msg = jobj.getString("msg");
						responsePopup();
					}
				} else {
					mProgressHUD.dismiss();
					try {
						_msg = jobj.getString("msg");
						responsePopup();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String SendData(String url, List<NameValuePair> _namevalueList) {
		String _Response = null;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpPost _httppost = new HttpPost(url);
		try {
			_httppost.setEntity(new UrlEncodedFormEntity(_namevalueList,HTTP.UTF_8));
			HttpResponse _httpresponse = _httpclient.execute(_httppost);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			if (_responsecode == 200) {
				InputStream _inputstream = _httpresponse.getEntity()
						.getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(_inputstream));
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
			// TODO Auto-generated method stub

		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(DialogError e) {
			// TODO Auto-generated method stub

		}

	}

	public void facebook() {

		AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(getActivity());

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setTitle("BrandsFever"+ "\n" + "Plaese wait");
				dialog.setCancelable(false);
				dialog.setIndeterminate(true);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					JSONObject me = new JSONObject(facebook.request("me"));
					mFBFirstName = me.getString("first_name");
					mFBLastName = me.getString("last_name");
					mFBEmail = me.getString("email");
					mFBGender = me.getString("gender");
					if (mFBGender != null) { // convert "male" to "M" and "female" to "F"
						mFBGender = ""+mFBGender.toUpperCase().charAt(0);
					}
				} catch (JSONException e) {
						e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
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
		protected String doInBackground(String... params) {
			String url_socaillogin = "https://www.brandsfever.com/api/v5/social-login/";
			BasicNameValuePair socailemail = new BasicNameValuePair("email",mFBEmail);
			BasicNameValuePair firstName = new BasicNameValuePair("first_name",mFBFirstName);
			BasicNameValuePair lastName = new BasicNameValuePair("last_name",mFBLastName);
			BasicNameValuePair gender = new BasicNameValuePair("gender",mFBGender);
			
			List<NameValuePair> namevalueList = new ArrayList<NameValuePair>();
			namevalueList.add(socailemail);
			namevalueList.add(firstName);
			namevalueList.add(lastName);
			namevalueList.add(gender);
			_SocailResponseFromServer = SendData(url_socaillogin,namevalueList);
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
					_mypref = getActivity().getApplicationContext()
							.getSharedPreferences("mypref", 0);
					Editor prefsEditor = _mypref.edit();
					prefsEditor.clear();
					prefsEditor.putString("ID", _UserId);
					prefsEditor.putString("TOKEN", _token);
					prefsEditor.commit();
					if (!(_UserId.length() == 0) && !(_token.length() == 0)) {
						new GetUserName(_token,_UserId).execute();
					} else {
						_msg = jobj.getString("msg");
						responsePopup();
					}
				} else {
					_msg = jobj.getString("msg");
					responsePopup();
					mProgressHUD.dismiss();
					try {
						_msg = jobj.getString("msg");
						responsePopup();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressHUD.dismiss();
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}
	}

	public void responsePopup() {
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
	
	private class GetUserName extends AsyncTask<String, String, String>{
		String _id;
		String mtoken;
		public GetUserName(String token,String id){
			this._id=id;
			this.mtoken=token;
		}

		@Override
		protected String doInBackground(String... params) {
			String _profileurl = "https://www.brandsfever.com/api/v5/profiles/?user_id="+ _id + "&token=" + mtoken;
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
							
					String name = obj1.getString("first_name");
					_mypref = getActivity().getApplicationContext()
							.getSharedPreferences("mypref", 0);
					Editor prefsEditor = _mypref.edit();
					prefsEditor.putString("_UserName", name);
					prefsEditor.commit();
					try {
						((PhoneLoginScreen) getActivity()).refreshPage();
						getActivity().overridePendingTransition(R.anim.puch_out_to_top,R.anim.push_out_to_bottom);
						_msg = "login successful!";
						responsePopup();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				mProgressHUD.dismiss();
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
				BufferedReader r = new BufferedReader(new InputStreamReader(_inputstream));
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

	
	private void forgotPassword(){
		try {
			View view = View.inflate(getActivity().getBaseContext(),
					R.layout.forgot_password_file,null);			
					pwindo = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
					pwindo.showAtLocation(view, Gravity.CENTER, 0, 0);
					
					resetEmail = (EditText) view.findViewById(R.id.resetEmail);
					resetEmail.setTypeface(_font, Typeface.NORMAL);
					
					cancel_password = (Button) view.findViewById(R.id.cancel_password);
					cancel_password.setTypeface(_font, Typeface.NORMAL);
					cancel_password.setOnClickListener(this);
					
					reset_password = (Button) view.findViewById(R.id.reset_password);
					reset_password.setTypeface(_font, Typeface.BOLD);
					reset_password.setOnClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ResetUserPassword extends AsyncTask<String, String, String> implements OnCancelListener{
		
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
		protected String doInBackground(String... params) {
		 String _resetP = "https://www.brandsfever.com/api/v5/reset-password/";
		 BasicNameValuePair _socailemail = new BasicNameValuePair("email",_getEmail);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_socailemail);
			_ResetResponseFromServer = SendData(_resetP,_namevalueList);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
		}
		
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject _kobj = new JSONObject(_ResetResponseFromServer);
				int _ret = Integer.parseInt(_kobj.getString("ret"));
				if(_ret==0){
					mProgressHUD.dismiss();
					pwindo.dismiss();
					_msg="Password Reset link is sent\nto your email-id";
					responsePopup();
				}else{
					_msg=_kobj.getString("msg");
					responsePopup();
					mProgressHUD.dismiss();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}