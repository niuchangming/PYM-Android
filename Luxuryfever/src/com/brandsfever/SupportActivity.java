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
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.brandsfever.luxury.R;
import com.dataholder.DataHolderClass;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;


public class SupportActivity extends FragmentActivity {

	private static final String TAG = "SupportActivity";
	
	private SimpleSideDrawer mSideDrawer;
	private ImageButton mMain, mBack, mCart;
	private Button mAll, mMen, mWomen, mLogin, mSettings, mSupport, mMyCart, mInvite, mLogout;
	private SharedPreferences mPref;
	
	private int mColor, mColors;
	
	String mUserId;
	String mToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_support);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SupportFragment()).commit();
		}		
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public class SupportFragment extends Fragment implements OnClickListener {

		Button mSubmit;
		Typeface mFont;
		
		EditText mOrderId;
		EditText mIssue;
		EditText mDetail;
		EditText mEmail;
		
		public SupportFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_support,
					container, false);
		
			mMain = (ImageButton) rootView.findViewById(R.id.main_menu);
			mMain.setOnClickListener(this);
	
			mBack = (ImageButton) rootView.findViewById(R.id.back_btn);
			mBack.setOnClickListener(this);
	
			mCart = (ImageButton) rootView.findViewById(R.id.cart_btn);
			mCart.setOnClickListener(this);		
			
			mFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/georgia.ttf");
			TextView title = (TextView) rootView.findViewById(R.id.support_title);
			title.setTypeface(mFont,Typeface.ITALIC);
			
			mOrderId = (EditText) rootView.findViewById(R.id.order_id);
			mIssue = (EditText) rootView.findViewById(R.id.issue);
			mDetail = (EditText) rootView.findViewById(R.id.detail);
			mEmail = (EditText) rootView.findViewById(R.id.email);
			
			mSubmit = (Button)rootView.findViewById(R.id.support_submit);
			mSubmit.setTypeface(mFont);
			mSubmit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG, "Submit is clicked.");
					
					if (mOrderId.getText().toString().length() < 1){
						showMessage("Please input your Order Identifier.");
					}
					else if (mIssue.getText().toString().length() < 1){
						showMessage("Please input your issue.");
					}
					else if (!isValidEmail(mEmail.getText().toString())) {
						showMessage("Please input a valid Email address.");
					}
					else {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
						nameValuePairs.add(new BasicNameValuePair("order_number", mOrderId.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("issue", mIssue.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("detail",mDetail.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("email", mEmail.getText().toString()));
						
						SubmitSupport submit = new SubmitSupport();
						submit.execute(nameValuePairs);
					}
				}
			});
			
			
			mColor = Integer.parseInt("8e1345", 16) + 0xFF000000;
			mColors = Integer.parseInt("ffffff",16) + 0xFF000000;
			
			mSideDrawer = new SimpleSideDrawer(SupportActivity.this);
			mSideDrawer.setLeftBehindContentView(R.layout.menu_bar);
			mSideDrawer.setBackgroundColor(Color.parseColor("#000000"));
			
			TextView profileView = (TextView)findViewById(R.id.set_user_name);
			profileView.setTypeface(mFont);
			mPref = getApplicationContext().getSharedPreferences("mypref", 0);
			mUserId = mPref.getString("ID", null);
			mToken = mPref.getString("TOKEN", null);
			String username = mPref.getString("_UserName", null);
			if(username != null){
				profileView.setText("Hi! " + username);
			}
			else {
				profileView.setText("Hi! Guest");
			}

			mAll = (Button) findViewById(R.id.btn_all_cat);
			mAll.setTypeface(mFont);
			mAll.setOnClickListener(this);

			mMen = (Button) findViewById(R.id.cat_shoes);
			mMen.setTypeface(mFont);
			mMen.setOnClickListener(this);

			mWomen = (Button) findViewById(R.id.cat_handbags);
			mWomen.setTypeface(mFont);
			mWomen.setOnClickListener(this);

			mLogin = (Button) findViewById(R.id.btn_login);
			mLogin.setVisibility(View.GONE);

			mSettings = (Button) findViewById(R.id.btn_setting);
			mSettings.setTypeface(mFont);
			mSettings.setOnClickListener(this);

			mMyCart = (Button) findViewById(R.id.my_cart);
			mMyCart.setTypeface(mFont);
			mMyCart.setOnClickListener(this);

			mSupport = (Button)findViewById(R.id.btn_support);
			mSupport.setTypeface(mFont);
			mSupport.setOnClickListener(this);
			
			mInvite = (Button) findViewById(R.id.btn_invite);
			mInvite.setTypeface(mFont);
			mInvite.setOnClickListener(this);

			mLogout = (Button) findViewById(R.id.btn_logout);
			mLogout.setTypeface(mFont);
			mLogout.setOnClickListener(this);

			mAll.setTextColor(mColors);
			mMen.setTextColor(mColors);
			mWomen.setTextColor(mColors);
			mSettings.setTextColor(mColors);
			mMyCart.setTextColor(mColors);
			mSupport.setTextColor(mColor);
			mInvite.setTextColor(mColors);
			
			return rootView;
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mOrderId.getWindowToken(), 0);
		}
		
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.main_menu:
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mOrderId.getWindowToken(), 0);
				mSideDrawer.toggleLeftDrawer();
				break;
			case R.id.btn_all_cat:
				mSideDrawer.closeRightSide();
				Intent all = new Intent(SupportActivity.this, ProductDisplay.class);
				all.putExtra("tab", "all");
				startActivity(all);
				mSideDrawer.closeRightSide();
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
				break;

			case R.id.cat_shoes:
				if (mSideDrawer.isClosed()) {
					mSideDrawer.setEnabled(false);
				} else {
					mSideDrawer.setEnabled(true);
					mSideDrawer.closeRightSide();
					Intent men = new Intent(SupportActivity.this, ProductDisplay.class);
					men.putExtra("tab", "men");
					startActivity(men);
					mSideDrawer.closeRightSide();
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
					finish();
				}
				break;

			case R.id.cat_handbags:
				if (mSideDrawer.isClosed()) {
					mSideDrawer.setEnabled(false);
				} else {
					mSideDrawer.setEnabled(true);
					mSideDrawer.closeRightSide();
					Intent women = new Intent(SupportActivity.this, ProductDisplay.class);
					women.putExtra("tab", "women");
					startActivity(women);
					mSideDrawer.closeRightSide();
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
					finish();
				}
				break;

			case R.id.btn_setting:
				if (mSideDrawer.isClosed()) {

					mSideDrawer.setEnabled(false);
				} else {
					mSideDrawer.setEnabled(true);
					if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
						Intent _phonesetting = new Intent(SupportActivity.this, SettingPhone.class);
						startActivity(_phonesetting);
						overridePendingTransition(R.anim.push_out_to_right,
								R.anim.push_out_to_left);

					} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
							&& DataHolderClass.getInstance().get_deviceInch() < 9) {
						Intent _tabsetting = new Intent(SupportActivity.this, SettingTab.class);
						startActivity(_tabsetting);
						overridePendingTransition(R.anim.push_out_to_right,
								R.anim.push_out_to_left);
					} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
						Intent _tabsetting = new Intent(SupportActivity.this, SettingTab.class);
						startActivity(_tabsetting);
						overridePendingTransition(R.anim.push_out_to_right,
								R.anim.push_out_to_left);
					}
				}
				break;

			case R.id.my_cart:
				if (mSideDrawer.isClosed()) {
					mSideDrawer.setEnabled(false);
				} else {
					mSideDrawer.setEnabled(true);
					Intent _cart = new Intent(SupportActivity.this, MyCartScreen.class);
					startActivity(_cart);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
					finish();
				}
				break;

			case R.id.btn_support:
				mSideDrawer.closeLeftSide();
				break;
				
			case R.id.btn_invite:
				if (mSideDrawer.isClosed()) {
					mSideDrawer.setEnabled(false);
				} else {
					mSideDrawer.setEnabled(true);
					Intent _invite = new Intent(SupportActivity.this, InviteSction_Screen.class);
					startActivity(_invite);
					mSideDrawer.closeRightSide();
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
					finish();
				}
				break;

			case R.id.btn_logout:
				if (mSideDrawer.isClosed()) {
					mSideDrawer.setEnabled(false);
				} else {
					mSideDrawer.setEnabled(true);
					Editor editor = mPref.edit();
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
				if ((mToken != null) && (mUserId != null)) {
					Intent _gotocart = new Intent(SupportActivity.this, MyCartScreen.class);
					startActivity(_gotocart);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
				}
				break;

			default:
				break;
			}

		}
	}
	
	class SubmitSupport extends AsyncTask<List<NameValuePair>, String, String> implements OnCancelListener {

		private ProgressHUD mProgressHUD;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(SupportActivity.this, "Loading", true, true, this);
			
		}
		
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			String supportUrl = "https://www.brandsfever.com/api/v5/customer-support/";
			
			String result = postData(supportUrl,params[0]);
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			mProgressHUD.dismiss();
			showMessage(result);
		}
		
	}
	
	private String postData(String url, List<NameValuePair> nameValuePairs){
		String response = null;
		
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpClient = HttpsClient.getNewHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			if(responseCode == 200){
				InputStream inputStream = httpResponse.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder total = new StringBuilder();
				String line;
				while((line = reader.readLine()) != null){
					total.append(line);
				}
				
				JSONObject responseObject = new JSONObject(total.toString());
				String responseMsg = responseObject.getString("msg");
				if(responseMsg.equalsIgnoreCase("OK")){
					response = "Thanks for your feedback. Happy shopping with Brandsfever:)";
				}
				else {
					response = "Your Order Identifier is invalid.Please check and try again.";
				}
			} 
			else {
				response = "Please try again later.";
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		
		return response;
	}

	
	private void showMessage(String message) {
		View view = View.inflate(SupportActivity.this.getBaseContext(),
				R.layout.error_popop, null);
		TextView msg = (TextView) view.findViewById(R.id._seterrormsg);
		msg.setText(message);
		Toast toast = new Toast(SupportActivity.this.getBaseContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}
	
	public final static boolean isValidEmail(CharSequence target){		
		return !TextUtils.isEmpty(target)&& android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
}
