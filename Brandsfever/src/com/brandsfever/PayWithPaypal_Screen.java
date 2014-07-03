package com.brandsfever;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;

public class PayWithPaypal_Screen extends FragmentActivity implements
		OnClickListener {
	Context _ctx = PayWithPaypal_Screen.this;
	WebView mPaypalWebView;
	String mPaypalUrl;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	Typeface _font;
	ImageButton main_menu, back_btn, cart_btn;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, _mycart, mSupport, _invite, _logout;
	int color, colors;
	boolean isPaid;
	JSONObject mOrderDetail;
	
	private int mLoopCount;
	private static final int HANDLER_DELAY = 1000*4;
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			setContentView(R.layout.activity_pay_with_paypal__screen);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceInch() < 9) {
			setContentView(R.layout.seven_inch_paypal_screen);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
			setContentView(R.layout.pay_with_paypal_tablet);
		}
		mPaypalWebView = (WebView) findViewById(R.id.webView1);
		mPaypalWebView.getSettings().setJavaScriptEnabled(true);

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);
		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));

		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String _username = _mypref.getString("_UserName", null);
		if (!(_username == null)) {
			set_user_name.setTypeface(_font);
			set_user_name.setText("Hi! " + _username.replace("Hi!", ""));
		} else {
			set_user_name.setText("Hi! Guest");
		}

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		back_btn = (ImageButton) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

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

		mPaypalUrl = "https://www.brandsfever.com/api/v5/payments/paypal/?token="
				+ _getToken
				+ "&user_id="
				+ _getuserId
				+ "&order_id="
				+ DataHolderClass.getInstance().get_orderpk();

		_all.setTextColor(colors);
		_men.setTextColor(colors);
		_women.setTextColor(colors);
		_childrens.setTextColor(colors);
		_home.setTextColor(colors);
		_accessories.setTextColor(colors);
		_settings.setTextColor(colors);
		_mycart.setTextColor(color);
		mSupport.setTextColor(colors);
		_invite.setTextColor(colors);
		new SendForPayment().execute();
		
		String detail = getIntent().getExtras().getString("OrderDetailKey");
		try {
			mOrderDetail = new JSONObject(detail);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mLoopCount = 0;	
		
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": payments/paypal/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_btn:
			Intent _bck = new Intent(_ctx, PaymentScreen.class);
			startActivity(_bck);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			//finish();
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

		case R.id.btn_support:
			
			slide_me.setEnabled(true);
			Intent support = new Intent(_ctx,SupportActivity.class);
			startActivity(support);
			slide_me.closeRightSide();
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			
			break;
			
		case R.id.btn_invite:
			Intent _invite = new Intent(_ctx, InviteSction_Screen.class);
			startActivity(_invite);
			slide_me.closeRightSide();
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.my_cart:
			Intent _cart = new Intent(_ctx, MyCartScreen.class);
			startActivity(_cart);
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

		default:
			break;
		}

	}
	
	private void onPurchaseCompleted(){
		
		if(mOrderDetail != null){
			
			EasyTracker tracker = EasyTracker.getInstance(this);
			try {

				tracker.send(MapBuilder.createTransaction(mOrderDetail.getString("identifier"), "Brandsfever",
						mOrderDetail.getDouble("total_price"), mOrderDetail.getDouble("tax_price"), 
						mOrderDetail.getDouble("shipping_fee"), "SGD").build());
			
				JSONArray itemArray = mOrderDetail.getJSONArray("item_list");
				for(int i = 0; i < itemArray.length(); i++){
					JSONObject item = itemArray.getJSONObject(i);
					tracker.send(MapBuilder.createItem(mOrderDetail.getString("identifier"), 
							item.getString("name"), item.getString("pk"), 
							item.getString("campaign"), item.getDouble("unit_price"), 
							item.getLong("quantity"), "SGD").build());
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	// =======================================================================================================================//
	class SendForPayment extends AsyncTask<Void, Void, Boolean> implements OnCancelListener{
		String sessionCookie;
		CookieManager cookieManager;
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {

			mProgressHUD = ProgressHUD.show(PayWithPaypal_Screen.this,
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

			CookieSyncManager.createInstance(PayWithPaypal_Screen.this);
			cookieManager = CookieManager.getInstance();

			if (sessionCookie != null) {
				cookieManager.removeSessionCookie();
			}
			super.onPreExecute();
		}

		

		@Override
		protected Boolean doInBackground(Void... param) {
			SystemClock.sleep(1000);
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mProgressHUD.dismiss();
			if (sessionCookie != null) {
				cookieManager.setCookie("yourdomain.com", sessionCookie);
				CookieSyncManager.getInstance().sync();
			}
			WebSettings webSettings = mPaypalWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setTextZoom((int)(webSettings.getTextZoom() * 0.5));
			webSettings.setBuiltInZoomControls(true);
			
			mPaypalWebView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					return super.shouldOverrideUrlLoading(view, url);
				}
				
				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					
					// start to check order state.
					final Handler printHandler = new Handler();
					Runnable printTest = new Runnable(){

						@Override
						public void run() {
							
							System.out.println("Runnable run TEST.");
							mLoopCount++;
							if(mLoopCount < 5){
								System.out.println("Loop: "+mLoopCount);
								printHandler.postDelayed(this, HANDLER_DELAY);
							}
							else {
								System.out.println("Max Loop: " + mLoopCount);
								printHandler.removeCallbacks(this);
							}
						}
						
					};
					
					printHandler.postDelayed(printTest, HANDLER_DELAY);
				}

			});
			mPaypalWebView.loadUrl(mPaypalUrl);
		}



		@Override
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent _bck = new Intent(_ctx, PaymentScreen.class);
		startActivity(_bck);
		overridePendingTransition(R.anim.push_out_to_right,
				R.anim.push_out_to_left);
	}

}