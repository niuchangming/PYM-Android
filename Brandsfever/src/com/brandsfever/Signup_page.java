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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;

import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class Signup_page extends Fragment implements OnClickListener {
	EditText F_Name,L_Name,Password,EmailId;
	ImageButton hit_register,signup_with_fb;
    String _Fname,_Lname,_Password,_Email;
	String _ResponseRegister;

	public static Fragment newInstance(String message,int item,DialogFragment _df) {
		Signup_page f = new Signup_page();			
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup _view = (ViewGroup) inflater.inflate(R.layout.activity_signup_page, null);		
//	    F_Name = (EditText)_view.findViewById(R.id.Fname_new);
//	    L_Name = (EditText)_view.findViewById(R.id.Lname_new);
//	    Password = (EditText)_view.findViewById(R.id.password_new);
//	    EmailId = (EditText)_view.findViewById(R.id.Email_new);
//	    
//		hit_register = (ImageButton)_view.findViewById(R.id.Sign_up_click);
//		hit_register.setOnClickListener(this);
//		
//		signup_with_fb = (ImageButton)_view.findViewById(R.id.signup_with_fb);
//		signup_with_fb.setOnClickListener(this);
		return _view;
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.Sign_up_click:
//			try {
//				_Fname = F_Name.getText().toString();
//				_Lname = L_Name.getText().toString();
//				_Password = Password.getText().toString();
//				_Email = EmailId.getText().toString();
//				new RegisterCustomer().execute();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			break;
//		case R.id.signup_with_fb:
//			break;
//
//		default:
//			break;
		}
		
	
	/***********************************************************************************************************************/
//	private class RegisterCustomer extends AsyncTask<String,String,String> implements OnCancelListener{
//		 ProgressHUD mProgressHUD;
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
//		public void onCancel(DialogInterface dialog) {
//			// TODO Auto-generated method stub
//			
//		}
//		@Override
//		protected String doInBackground(String... params) {
//			String url_register="https://www.brandsfever.com/api/v5/users/";
//			String user_fname = _Fname;
//			String user_lname = _Lname;
//			String user_Rpassword = _Password;
//			String user_emailid = _Email;
//			BasicNameValuePair _Ufname = new BasicNameValuePair("first_name",user_fname);
//            BasicNameValuePair _Ulname = new BasicNameValuePair("last_name",user_lname);
//            BasicNameValuePair _Upassword = new BasicNameValuePair("password",user_Rpassword);
//            BasicNameValuePair _Uemail = new BasicNameValuePair("email",user_emailid);
//            List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
//            _namevalueList.add(_Ufname);
//            _namevalueList.add(_Ulname);
//            _namevalueList.add(_Upassword);
//            _namevalueList.add(_Uemail);
//            _ResponseRegister=RegisterUser(url_register,_namevalueList);
//            Log.e("===RESPONSE====>","===RESPONSE====>"+_ResponseRegister);
//			return null;
//		}
//		@Override
//		protected void onPostExecute(String result) {
//			try {
//				JSONObject jobj=new JSONObject(_ResponseRegister);
//				String _Ret = jobj.getString("ret");
//				if(_Ret.equals("0")){					
//					String _token = jobj.getString("token");
//					String _UserId = jobj.getString("user");
//					System.out.println(_token);
//					System.out.println(_UserId);
//					Intent sign_up = new Intent(getActivity(),ProductDisplay.class);
//					startActivity(sign_up);
//										
//				}else{
//					new AlertDialog.Builder(getActivity()).setTitle("BrandFever").setMessage("Invalid Credentials.")
//					.setNeutralButton("Close", null).show();
//				}
//			} catch (Exception e) {
//			
//			}
//			mProgressHUD.dismiss();
//		}
//	}
//	/*
//	 * Register new user in background method
//	 * 
//	 */
//	public String RegisterUser(String url, List<NameValuePair> _namevalueList) {
//		String _RResponse=null;
//		TrustAllCertificates cert = new TrustAllCertificates();
//		cert.trustAllHosts() ;
//		HttpClient _httpclient = HttpsClient.getNewHttpClient();
//		HttpPost _httppost = new HttpPost(url);
//		try {
//    		_httppost.setEntity(new UrlEncodedFormEntity(_namevalueList, HTTP.UTF_8));
//		    HttpResponse _httpresponse = _httpclient.execute(_httppost);
//		    int _responsecode = _httpresponse.getStatusLine().getStatusCode();
//		    Log.i("--------------Responsecode----------", "." + _responsecode);
//		    if (_responsecode == 200) {
//                InputStream _inputstream = _httpresponse.getEntity().getContent();
//                BufferedReader r = new BufferedReader(new InputStreamReader(_inputstream));
//                StringBuilder total = new StringBuilder();
//                String line;
//                while ((line = r.readLine()) != null) {
//                        total.append(line);
//                }
//                _RResponse = total.toString();
//                } else {
//                _RResponse = "Error";
//                }
//         } catch (Exception e) {
//	         e.printStackTrace();
//         }
//		return _RResponse;
//		
//	}
}