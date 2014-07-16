 package com.brandsfever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;
import com.brandsfever.luxury.R;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;

public class Login_page extends Fragment  {
	EditText username,password;
	Button signin,signup;
	ImageButton login_with_fb;
	TextView forgotpassword;
	String _getusername,_getpassword;
    SharedPreferences _mypref;
    String _ResponseFromServer,_SocailResponseFromServer;
    Login_page context;
    static DialogFragment _df;
    Toast toast;
    ViewGroup _view;
    Facebook facebook;
    Typeface _font;
	String _userFBemail;
	public static final Login_page newInstance(String message,int item,DialogFragment _df){
		Login_page f = new Login_page();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 _view = (ViewGroup) inflater.inflate(R.layout.activity_login_page, null);			
	    context = this;		
	    

		return _view;
	}
}


//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.Lsubmit:
//			_getusername = username.getText().toString();
//			_getpassword = password.getText().toString();
//			if(_getusername.length()>0 && _getpassword.length()>0){
//				new PerformLogin().execute();
//			}else if(_getusername.length()==0){		
//				try {
//				} catch (Exception e) {
//					e.printStackTrace();
//				}				
//			}
//			else if(_getpassword.length()==0){
//                  try {
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}else{
//				System.out.println("popup here");
//			}
//			break;
//			
//		case R.id.login_with_fb:
//			if (!facebook.isSessionValid()) {
//				Toast.makeText(getActivity(), "Redirecting you on facebook", 2)
//						.show();
//				facebook.authorize(getActivity(), new String[] {
//						"publish_stream", "email", "user_groups",
//						"read_stream", "user_about_me", "offline_access" }, 1,
//						new LoginDialogListener());
//			    } else {
//				    facebook();
//			    }
//			break;
//
//		default:
//			break;
//		}
//		
//	}
	
	/************************************************************************************************************************/
//	private class PerformLogin extends AsyncTask<String,String,String> implements OnCancelListener{
//		 ProgressHUD mProgressHUD;;
//		@Override
//		protected void onPreExecute() {			
//			mProgressHUD = ProgressHUD.show(getActivity(),"Loading ,Please Wait", true,true,this);
//			DisplayMetrics displaymetrics = new DisplayMetrics();
//			getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//			int displayHeight = displaymetrics.heightPixels;
//			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
//			WindowManager.LayoutParams wmlp = mProgressHUD.getWindow().getAttributes();
//			wmlp.y =displayHeight / 4;
//			mProgressHUD.getWindow().setAttributes(wmlp);
//			mProgressHUD.setCancelable(false);
//			super.onPreExecute();
//			
//		}
//		@Override
//		protected String doInBackground(String... params) {
//			String url_login="https://www.brandsfever.com/api/v5/auth/";
//			String user_email = _getusername;
//			String user_password = _getpassword;
//			BasicNameValuePair _emailpair = new BasicNameValuePair("email",user_email);
//            BasicNameValuePair _passwordpair = new BasicNameValuePair("password",user_password);
//            List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
//            _namevalueList.add(_emailpair);
//            _namevalueList.add(_passwordpair);
//            _ResponseFromServer=SendData(url_login,_namevalueList);
//            Log.e("===RESPONSE====>","===RESPONSE====>"+_ResponseFromServer);
//			return null;
//		}
//		@Override
//		protected void onPostExecute(String result) {
//			try {
//				JSONObject jobj=new JSONObject(_ResponseFromServer);
//				String _Ret = jobj.getString("ret");
//				if(_Ret.equals("0")){					
//					String _token = jobj.getString("token");
//					String _UserId = jobj.getString("user_id");
//					System.out.println(_token);
//					System.out.println(_UserId);
//					_mypref = getActivity().getApplicationContext().getSharedPreferences("mypref", 0);
//					Editor prefsEditor = _mypref .edit();
//					prefsEditor.clear();
//					prefsEditor.putString("ID",_UserId);
//					prefsEditor.putString("TOKEN",_token);
//					prefsEditor.commit();					
//					if(!(_UserId.length()==0) &&  !(_token.length()==0)){
//						 mProgressHUD.dismiss();
//						 try {
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					((DialogFragment)context.getParentFragment()).dismiss();
//						 Intent intent = new Intent(getActivity(), getActivity().getClass());
//			               getActivity().startActivity(intent);
//						
//					}else{
//						System.out.println("some error");
//					}
//				
//				}else{
//					 mProgressHUD.dismiss();
//				}
//			} catch (Exception e) {
//			   e.printStackTrace();
//			}
//		  }
//		@Override
//		public void onCancel(DialogInterface dialog) {
//			
//		}
//	}
//	/***********************************************************************************************************************/
//	public String SendData(String url, List<NameValuePair> _namevalueList) {
//		String _Response = null;		
//		TrustAllCertificates cert = new TrustAllCertificates();
//		cert.trustAllHosts() ;
//		HttpClient _httpclient = HttpsClient.getNewHttpClient();
//		HttpPost _httppost = new HttpPost(url);		
//        try {
//        		_httppost.setEntity(new UrlEncodedFormEntity(_namevalueList, HTTP.UTF_8));
//			    HttpResponse _httpresponse = _httpclient.execute(_httppost);
//			    int _responsecode = _httpresponse.getStatusLine().getStatusCode();
//			    Log.i("--------------Responsecode----------", "." + _responsecode);
//			    if (_responsecode == 200) {
//                    InputStream _inputstream = _httpresponse.getEntity().getContent();
//                    BufferedReader r = new BufferedReader(new InputStreamReader(_inputstream));
//                    StringBuilder total = new StringBuilder();
//                    String line;
//                    while ((line = r.readLine()) != null) {
//                            total.append(line);
//                    }
//                    _Response = total.toString();
//                    } else {
//                    _Response = "Error";
//                    }
//	         } catch (Exception e) {
//		         e.printStackTrace();
//	         }
//		    return _Response;
//	    }
//	// ===================================================================================================================//
//		public void onActivityResult(int requestCode, int resultCode,
//				final Intent data) {
//            System.out.println("mahabir");
//			super.onActivityResult(requestCode, resultCode, data);
//			facebook.authorizeCallback(requestCode, resultCode, data);
//
//		}
//
//		public final class LoginDialogListener implements DialogListener {
//			public void onComplete(Bundle values) {
//				try {
//
//					SessionStore.save(facebook, getActivity());
//					facebook();
//				} catch (Exception error) {
//					error.printStackTrace();
//
//				}
//			}
//			public void onCancel() {
//			}
//			@Override
//			public void onFacebookError(FacebookError e) {
//
//			}
//			@Override
//			public void onError(DialogError e) {
//
//			}
//		}
//		public void facebook() {
//			AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {
//				ProgressDialog dialog = new ProgressDialog(getActivity());
//				private String s;
//
//				@Override
//				protected void onPreExecute() {
//					dialog.setTitle("Getting Facebook Information");
//					dialog.setCancelable(false);
//					dialog.setIndeterminate(true);
//					dialog.show();
//				}
//
//				@Override
//				protected Void doInBackground(Void... params) {
//					JSONObject jsonObj = null;
//					try {
//						try {
//							JSONObject me = new JSONObject(facebook.request("me"));
//							System.out.println("value show" + me);
//							String a = me.getString("name");
//							String b = me.getString("email");
//							_userFBemail = b;
//							Log.d("iii", "iii-name-" + a);
//							Log.d("iii", "iii-name-" + b);
//
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					} catch (MalformedURLException e) {
//						e.printStackTrace();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//					return null;
//				}
//
//				@Override
//				protected void onPostExecute(Void result) {
//					new SocailLogin().execute();
//					dialog.dismiss();
//				}
//
//			};
//			updateTask.execute((Void[]) null);
//		}
//
//		// ========================================================================================================================//
//		class SocailLogin extends AsyncTask<String, String, String> implements
//				OnCancelListener {
//			ProgressHUD mProgressHUD;
//
//			@Override
//			protected void onPreExecute() {
//				mProgressHUD = ProgressHUD.show(getActivity(), "Loading", true,
//						true, this);
//				DisplayMetrics displaymetrics = new DisplayMetrics();
//				getActivity().getWindowManager().getDefaultDisplay()
//						.getMetrics(displaymetrics);
//				int displayHeight = displaymetrics.heightPixels;
//				mProgressHUD.getWindow().setGravity(Gravity.CENTER);
//				WindowManager.LayoutParams wmlp = mProgressHUD.getWindow()
//						.getAttributes();
//				wmlp.y = displayHeight / 4;
//				mProgressHUD.getWindow().setAttributes(wmlp);
//				mProgressHUD.setCancelable(false);
//				super.onPreExecute();
//
//			}
//			@Override
//			public void onCancel(DialogInterface dialog) {
//
//			}
//			@Override
//			protected String doInBackground(String... params) {
//				String url_socaillogin = "https://www.brandsfever.com/api/v5/social-login/";
//				BasicNameValuePair _socailemail = new BasicNameValuePair("email",
//						_userFBemail);
//				List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
//				_namevalueList.add(_socailemail);
//				_SocailResponseFromServer = SendData(url_socaillogin,
//						_namevalueList);
//				Log.e("===RESPONSE====>", "===RESPONSE Socail====>"
//						+ _SocailResponseFromServer);
//				return null;
//			}
//
//			@Override
//			protected void onPostExecute(String result) {
//				try {
//					JSONObject jobj=new JSONObject(_SocailResponseFromServer);
//					String _Ret = jobj.getString("ret");
//					if(_Ret.equals("0")){					
//						String _token = jobj.getString("token");
//						String _UserId = jobj.getString("user_id");
//						System.out.println(_token);
//						System.out.println(_UserId);
//						_mypref = getActivity().getApplicationContext().getSharedPreferences("mypref", 0);
//						Editor prefsEditor = _mypref .edit();
//						prefsEditor.clear();
//						prefsEditor.putString("ID",_UserId);
//						prefsEditor.putString("TOKEN",_token);
//						prefsEditor.commit();					
//						if(!(_UserId.length()==0) &&  !(_token.length()==0)){
//							 mProgressHUD.dismiss();
//							 try {
//								
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						((DialogFragment)context.getParentFragment()).dismiss();
//							 Intent intent = new Intent(getActivity(), getActivity().getClass());
//				               getActivity().startActivity(intent);
//							
//						}else{
//							System.out.println("some error");
//						}
//     					}else{
//						 mProgressHUD.dismiss();
//						 try {
//							 
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				} catch (Exception e) {
//				   e.printStackTrace();
//				}
//					
//		}
//     }
//}