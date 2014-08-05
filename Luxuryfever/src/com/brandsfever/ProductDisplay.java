package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
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
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.adapter.VPagerAdapter;
import com.brandsfever.luxury.R;
import com.crashlytics.android.Crashlytics;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductsDataModel;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class ProductDisplay extends FragmentActivity implements
		OnClickListener, TabHost.OnTabChangeListener,
		ViewPager.OnPageChangeListener {
	
	private static final String TAG = "ProductDisplay";
	
	Context _ctx = ProductDisplay.this;
	SimpleSideDrawer slide_me;
	ImageButton _Menu,cart_btn;
	Button _all, _men, _women,  _login,
			_settings, _mycart, mSupport, _invite, _logout;
	Typeface _font;
	String catagory_name;
	String mTabName;
	Bundle bundle;
	TextView set_user_name;
	public static ArrayList<ProductsDataModel> all_prdt = new ArrayList<ProductsDataModel>();
	public static ArrayList<ProductsDataModel> shoes_prdt = new ArrayList<ProductsDataModel>();
	public static ArrayList<ProductsDataModel> handbags_prdt = new ArrayList<ProductsDataModel>();

	TabHost mTabHost;
	ViewPager mViewPager;
	HashMap<String, TabInfo> mapTabInfo = new HashMap<String, ProductDisplay.TabInfo>();
	VPagerAdapter mPagerAdapter;
	HorizontalScrollView mHorizontalScroll;
	
	
	View _settings_view,_login_view,_mycart_view,mSupportView,_invite_view,_logout_view;
	
	int color,colors;
	
	public static String _list_type="all";

	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	private int mBackButtonCount;

	private class TabInfo {
		private String tag;
		@SuppressWarnings("unused")
		private Class<?> clss;
		@SuppressWarnings("unused")
		private Bundle args;
		@SuppressWarnings("unused")
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}

	class TabFactory implements TabContentFactory {
		private final Context mContext;

		public TabFactory(Context context) {
			mContext = context;
		}

		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);

			return v;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		Log.d(TAG, "onCreate");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if(a<=6){
			setContentView(R.layout.activity_product_display);
		}else if(a>=7 && a<9){
			setContentView(R.layout.seven_inch_product_display);
		}else if(a>=9){
			setContentView(R.layout.product_display_ten_inch_tab);
		}
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16)+0xFF000000;
		colors = Integer.parseInt("ffffff", 16)+0xFF000000;

		bundle = getIntent().getExtras();
		mTabName = null;
		if (bundle != null) {
			mTabName = bundle.getString("tab");
		} else {
			mTabName = "all";
		}

		mHorizontalScroll = (HorizontalScrollView) findViewById(R.id.hsv);
		this.initialiseTabHost(savedInstanceState);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));
		
		set_user_name = (TextView)findViewById(R.id.set_user_name);
		set_user_name.setTypeface(_font);


		_Menu = (ImageButton) findViewById(R.id.main_menu);
		_Menu.setOnClickListener(this);
		
		cart_btn = (ImageButton) findViewById(R.id.cart_btn);
		cart_btn.setOnClickListener(this);

		new LoadProduct().execute();

		_all = (Button) findViewById(R.id.btn_all_cat);
		_all.setTypeface(_font);
		_all.setTextColor(color);
		_all.setOnClickListener(this);

		_men = (Button) findViewById(R.id.cat_shoes);
		_men.setTypeface(_font);
		_men.setOnClickListener(this);

		_women = (Button) findViewById(R.id.cat_handbags);
		_women.setTypeface(_font);
		_women.setOnClickListener(this);

		_login = (Button) findViewById(R.id.btn_login);
		_login_view = (View) findViewById(R.id.btn_login_view);
		_login.setTypeface(_font);
		_login.setOnClickListener(this);

		_settings = (Button) findViewById(R.id.btn_setting);
		_settings_view = (View) findViewById(R.id.btn_setting_view);
		_settings.setTypeface(_font);
		_settings.setOnClickListener(this);

		_mycart = (Button) findViewById(R.id.my_cart);
		_mycart_view = (View) findViewById(R.id.my_cart_view);
		_mycart.setTypeface(_font);
		_mycart.setOnClickListener(this);

		mSupport = (Button)findViewById(R.id.btn_support);
		mSupportView = (View)findViewById(R.id.btn_support_view);
		mSupport.setTypeface(_font);
		mSupport.setOnClickListener(this);
		
		_invite = (Button) findViewById(R.id.btn_invite);
		_invite_view = (View) findViewById(R.id.btn_invite_view);
		_invite.setTypeface(_font);
		_invite.setOnClickListener(this);

		_logout = (Button) findViewById(R.id.btn_logout);
		_logout_view=(View) findViewById(R.id.btn_logout_view);
		_logout.setTypeface(_font);
		_logout.setOnClickListener(this);
		
	}
	
	private void updateSlidingMenu(){
		
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		String username = _mypref.getString("_UserName", null);
		String greeting = set_user_name.getText().toString();
		_getuserId = _mypref.getString("ID", null); 
        _getToken = _mypref.getString("TOKEN", null);
		
		if(username != null){
			
			if(greeting.contains(username)){
				return;
			} 
		
			set_user_name.setText("Hi! "+username);
		}
		else{
			
			if(greeting.contains("Guest")){
				return;
			}
			
			set_user_name.setText("Hi! Guest");
		}
		
		if(_getToken==null && _getuserId==null){
			_login.setVisibility(View.VISIBLE);	
			_mycart.setVisibility(View.GONE);
			_settings.setVisibility(View.GONE);
			mSupport.setVisibility(View.GONE);
			_invite.setVisibility(View.GONE);
			_logout.setVisibility(View.GONE);
			
			if(_logout_view != null)
				_logout_view.setVisibility(View.GONE);
			if(_mycart_view != null)
				_mycart_view.setVisibility(View.GONE);
			if(_settings_view != null)
				_settings_view.setVisibility(View.GONE);
			if(_invite_view != null)
				_invite_view.setVisibility(View.GONE);
			if( mSupportView != null)
				mSupportView.setVisibility(View.VISIBLE);
		}
		else{
			_login.setVisibility(View.GONE);	
			_mycart.setVisibility(View.VISIBLE);
			_settings.setVisibility(View.VISIBLE);
			mSupport.setVisibility(View.VISIBLE);
			_invite.setVisibility(View.VISIBLE);
			_logout.setVisibility(View.VISIBLE);
			
			if(_logout_view != null)
				_logout_view.setVisibility(View.VISIBLE);
			if(_login_view != null)
				_login_view.setVisibility(View.VISIBLE);
			if(_mycart_view != null)
				_mycart_view.setVisibility(View.VISIBLE);
			if(_settings_view != null)
				_settings_view.setVisibility(View.VISIBLE);
			if(_invite_view != null)
				_invite_view.setVisibility(View.VISIBLE);
			if( mSupportView != null)
				mSupportView.setVisibility(View.VISIBLE);
			
	     }
	}
	
	@Override
	public void onStart(){
		super.onStart();
		mBackButtonCount = 0;
		Log.d(TAG, "onStart");
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": campaigns/?device=2");
		tracker.send(MapBuilder.createAppView().build());
		
		updateSlidingMenu();
	}
	
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag());
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager() {
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, AllProductDisplay.class.getName()));
		fragments.add(Fragment.instantiate(this, HandbagsProductDisplay.class.getName()));
		fragments.add(Fragment.instantiate(this, ShoesProductDisplay.class.getName()));
		this.mPagerAdapter = new VPagerAdapter(
				super.getSupportFragmentManager(), fragments);
		this.mViewPager = (ViewPager) super.findViewById(R.id.viewPagers);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
		if (bundle != null) {
			if (mTabName.equalsIgnoreCase("all")) {
				mViewPager.setCurrentItem(0);
				int color = Integer.parseInt("8e1345", 16)+0xFF000000;
				_all.setTextColor(color);
				_men.setTextColor(colors);
				_women.setTextColor(colors);
				_settings.setTextColor(colors);
				_mycart.setTextColor(colors);
				mSupport.setTextColor(colors);
				_invite.setTextColor(colors);
			} else if (mTabName.equalsIgnoreCase("women")) {
				mViewPager.setCurrentItem(1);
				int color = Integer.parseInt("8e1345", 16)+0xFF000000;
				_women.setTextColor(color);
				_all.setTextColor(colors);
				_men.setTextColor(colors);
				_settings.setTextColor(colors);
				_mycart.setTextColor(colors);
				mSupport.setTextColor(colors);
				_invite.setTextColor(colors);
			} else if (mTabName.equalsIgnoreCase("men")) {
				int color = Integer.parseInt("8e1345", 16)+0xFF000000;
				_men.setTextColor(color);
				mViewPager.setCurrentItem(2);
			} 
		}
	}

	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		ProductDisplay.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab1").setIndicator("ALL"),
				(tabInfo = new TabInfo("Tab1", AllProductDisplay.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		ProductDisplay.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab2").setIndicator("HANDBAGS"),
				(tabInfo = new TabInfo("Tab2", HandbagsProductDisplay.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		ProductDisplay.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab3").setIndicator("SHOES"),
				(tabInfo = new TabInfo("Tab3", ShoesProductDisplay.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		TabWidget widget = mTabHost.getTabWidget();
		for (int i = 0; i < widget.getChildCount(); i++) {
			View v = widget.getChildAt(i);
			TextView tv = (TextView) v.findViewById(android.R.id.title);
			int color = Integer.parseInt("000000", 16)+0xFF000000;
			tv.setTextColor(color);
			tv.setTypeface(_font,Typeface.BOLD);
			if(DataHolderClass.getInstance().get_deviceInch()<=6){
				tv.setTextSize(14);
			}else if(DataHolderClass.getInstance().get_deviceInch()>=7){
				tv.setTextSize(20);
			}
			
			v.setBackgroundResource(R.drawable.selector);
		}
		mTabHost.setOnTabChangedListener(this);
	}

	private static void AddTab(ProductDisplay activity, TabHost tabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_menu:
			slide_me.toggleLeftDrawer();
			break;
		case R.id.btn_all_cat:
			_list_type="all";
			mViewPager.setCurrentItem(0);
			_all.setTextColor(color);
            _men.setTextColor(colors);
            _women.setTextColor(colors);
			_settings.setTextColor(colors);
			_mycart.setTextColor(colors);
			mSupport.setTextColor(colors);
			_invite.setTextColor(colors);
			slide_me.closeRightSide();
			break;

		case R.id.cat_handbags:
			_list_type="women";
			mViewPager.setCurrentItem(1);
			_all.setTextColor(colors);
            _men.setTextColor(colors);
            _women.setTextColor(color);
			_settings.setTextColor(colors);
			_mycart.setTextColor(colors);
			mSupport.setTextColor(colors);
			_invite.setTextColor(colors);
			slide_me.closeRightSide();
			break;

		case R.id.cat_shoes:
			_list_type="men";
			mViewPager.setCurrentItem(2);
			_all.setTextColor(colors);
            _men.setTextColor(color);
            _women.setTextColor(colors);
			_settings.setTextColor(colors);
			_mycart.setTextColor(colors);
			mSupport.setTextColor(colors);
			_invite.setTextColor(colors);
			slide_me.closeRightSide();
			break;

		case R.id.btn_login:
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				Intent login = new Intent(_ctx, PhoneLoginScreen.class);
				startActivityForResult(login, 1);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.puch_out_to_top,R.anim.push_out_to_bottom);
				
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
				Intent _login = new Intent(_ctx, PhoneLoginScreen.class);
				startActivityForResult(_login, 1);
				slide_me.closeRightSide();				
				overridePendingTransition(R.anim.puch_out_to_top,R.anim.login_screen_back);
			}
			break;

		case R.id.my_cart:
			Intent _cart = new Intent(ProductDisplay.this, MyCartScreen.class);
			startActivity(_cart);
			_all.setTextColor(colors);
            _men.setTextColor(colors);
            _women.setTextColor(colors);
			_settings.setTextColor(colors);
			_mycart.setTextColor(color);
			mSupport.setTextColor(colors);
			_invite.setTextColor(colors);
			slide_me.closeRightSide();
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			break;
		
		case R.id.btn_support:
			Intent support = new Intent(ProductDisplay.this,SupportActivity.class);
			startActivity(support);
			_all.setTextColor(colors);
            _men.setTextColor(colors);
            _women.setTextColor(colors);
			_settings.setTextColor(colors);
			_mycart.setTextColor(colors);
			mSupport.setTextColor(color);
			_invite.setTextColor(colors);
			slide_me.closeRightSide();
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);

			break;

		case R.id.btn_invite:
			Intent _invit = new Intent(_ctx, InviteSction_Screen.class);
			startActivity(_invit);
			_all.setTextColor(colors);
            _men.setTextColor(colors);
            _women.setTextColor(colors);
			_settings.setTextColor(colors);
			_mycart.setTextColor(colors);
			mSupport.setTextColor(colors);
			_invite.setTextColor(color);
			slide_me.closeRightSide();
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			break;

		case R.id.btn_logout:
			Editor editor = _mypref.edit();
			editor.clear();
			editor.commit();
			Intent _intent = new Intent(getApplicationContext(),ProductDisplay.class);
			startActivity(_intent);
			DataHolderClass.getInstance().setUsername("Hi! Guest");
			slide_me.closeRightSide();
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			break;

		case R.id.btn_setting:
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				Intent _phonesetting = new Intent(_ctx, SettingPhone.class);
				startActivity(_phonesetting);
				_all.setTextColor(colors);
	            _men.setTextColor(colors);
	            _women.setTextColor(colors);
				_settings.setTextColor(color);
				_mycart.setTextColor(colors);
				mSupport.setTextColor(colors);
				_invite.setTextColor(colors);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
					&& DataHolderClass.getInstance().get_deviceInch() < 9) {
				Intent _tabsetting = new Intent(_ctx, SettingTab.class);
				startActivity(_tabsetting);
				_all.setTextColor(colors);
	            _men.setTextColor(colors);
	            _women.setTextColor(colors);
				_settings.setTextColor(color);
				_mycart.setTextColor(colors);
				mSupport.setTextColor(colors);
				_invite.setTextColor(colors);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
				Intent _tabsetting = new Intent(_ctx, SettingTab.class);
				startActivity(_tabsetting);
				_all.setTextColor(colors);
	            _men.setTextColor(colors);
	            _women.setTextColor(colors);
				_settings.setTextColor(color);
				_mycart.setTextColor(colors);
				mSupport.setTextColor(colors);
				_invite.setTextColor(colors);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			}
			break;
			
		 case R.id.cart_btn:
			if((_getToken!=null)&& (_getuserId!=null)){
				Intent _gotocart = new Intent(_ctx,MyCartScreen.class);
				startActivity(_gotocart);
				overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			}else{
				LayoutInflater inflater = getLayoutInflater();
			    View view = inflater.inflate(R.layout.error_popop,(ViewGroup) findViewById(R.id.relativeLayout1));				    
			   final  TextView _seterrormsg = (TextView)view.findViewById(R.id._seterrormsg);
			    _seterrormsg.setText("Please login!");
			    Toast toast = new Toast(_ctx);
			    toast.setGravity(Gravity.CENTER, 0, 0);
			    toast.setView(view);
			    toast.show();
			}
			break;
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		View tabView = mTabHost.getTabWidget().getChildAt(position);
		if (tabView != null) {
			final int width = mHorizontalScroll.getWidth();
			final int scrollPos = tabView.getLeft()
					- (width - tabView.getWidth()) / 2;
			mHorizontalScroll.scrollTo(scrollPos, 0);
		} else {
			mHorizontalScroll.scrollBy(positionOffsetPixels, 0);
		}
	}

	@Override
	public void onPageSelected(int position) {
		this.mTabHost.setCurrentTab(position);
	}

	@Override
	public void onTabChanged(String tabId) {
		
		int pos = this.mTabHost.getCurrentTab();
		
		if(this.mViewPager!=null)
			this.mViewPager.setCurrentItem(pos);
		
		if(_all==null || _women==null || _men==null  )
			return;
		
		if(pos==0){
			_all.setTextColor(color);
			
            _men.setTextColor(colors);
            _women.setTextColor(colors);
			_settings.setTextColor(colors);
			_mycart.setTextColor(colors);
			mSupport.setTextColor(colors);
			_invite.setTextColor(colors);
			_list_type="all";
		}else if(pos==1){
			_women.setTextColor(color);
			
			_all.setTextColor(colors);
            _men.setTextColor(colors);
			_settings.setTextColor(colors);
			_mycart.setTextColor(colors);
			mSupport.setTextColor(colors);
			_invite.setTextColor(colors);
			_list_type="women";
		}else if(pos==2){
			_men.setTextColor(color);
			
			_all.setTextColor(colors);
            _women.setTextColor(colors);
			_settings.setTextColor(colors);
			_mycart.setTextColor(colors);
			mSupport.setTextColor(colors);
			_invite.setTextColor(colors);
			_list_type="men";
		}
		
	}
	
	class LoadProduct extends AsyncTask<Void, Void, Void> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(ProductDisplay.this, "Loading",
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
		public void onCancel(DialogInterface arg0) {
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			all_prdt.clear();
			shoes_prdt.clear();
			handbags_prdt.clear();

			String url_campaigns = "https://api-1.brandsfever.com/campaigns/channel/"
					+ getApplicationContext().getResources().getString(R.string.channel_code_go);
			GetProducts(url_campaigns);
			return null;
		}

		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgressHUD.dismiss();
			ProductDisplay.this.intialiseViewPager();
		}
	}

	
	public void GetProducts(String _url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(_url);
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
				String data = total.toString();
				try {
					JSONObject obj = new JSONObject(data);
					JSONArray get_campaigns = obj.getJSONArray("campaigns");
					for (int i = 0; i < get_campaigns.length(); i++) {
						JSONObject jsonobj = get_campaigns.getJSONObject(i);
						long ends_at = jsonobj.getLong("ends_at");
						String teaser_url = jsonobj.getString("teaser_url");
						String name = jsonobj.getString("name");
						long starts_at = jsonobj.getLong("starts_at");
						String pk = jsonobj.getString("pk");
						String express = jsonobj.getString("express");
						String discount_text = jsonobj
								.getString("discount_text");
						String shipping_fee = jsonobj.getString("shipping_fee");
						String shipping_period = jsonobj
								.getString("shipping_period");
						String free_shipping = jsonobj
								.getString("free_shipping");
						
						ProductsDataModel all_data_model = new ProductsDataModel();

						all_data_model.setEnds_at(ends_at);
						all_data_model.setTeaser_url(teaser_url);
						all_data_model.setName(name);
						all_data_model.setStarts_at(starts_at);
						all_data_model.setPk(pk);
						all_data_model.setExpress(express);
						all_data_model.setDiscount_text(discount_text);
						all_data_model.setFree_shipping(free_shipping);
						all_data_model.setShipping_fee(shipping_fee);
						all_data_model.setShipping_period(shipping_period);

						// all campaigns in luxury are in women category.
						// and there are only handbags and shoes.
						// we choose the product to shoes if there is shoe/heel in the name,
						// others are handbags
						if (name.toLowerCase().contains("shoe") || name.toLowerCase().contains("heel")){
                                ProductsDataModel shoes_data_model = new ProductsDataModel();
								shoes_data_model.setEnds_at(ends_at);
								shoes_data_model.setTeaser_url(teaser_url);
								shoes_data_model.setName(name);
								shoes_data_model.setStarts_at(starts_at);
								shoes_data_model.setPk(pk);
								shoes_data_model.setExpress(express);
								shoes_data_model.setDiscount_text(discount_text);
								shoes_data_model.setFree_shipping(free_shipping);
								shoes_data_model.setShipping_fee(shipping_fee);
								shoes_data_model
										.setShipping_period(shipping_period);
								shoes_prdt.add(shoes_data_model);
						} else {
								// otherwise this is handbag category
                                ProductsDataModel handbags_data_model = new ProductsDataModel();
								handbags_data_model.setEnds_at(ends_at);
								handbags_data_model.setTeaser_url(teaser_url);
								handbags_data_model.setName(name);
								handbags_data_model.setStarts_at(starts_at);
								handbags_data_model.setPk(pk);
								handbags_data_model.setExpress(express);
								handbags_data_model
										.setDiscount_text(discount_text);
								handbags_data_model
										.setFree_shipping(free_shipping);
								handbags_data_model.setShipping_fee(shipping_fee);
								handbags_data_model
										.setShipping_period(shipping_period);
								handbags_prdt.add(handbags_data_model);
						}
						all_prdt.add(all_data_model);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Log.e(TAG,"error");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				startActivity(getIntent());
				if (resultCode == RESULT_CANCELED) {
					// Do nothing?
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {

		if(mBackButtonCount >=1){
			Log.e(TAG, "EXIT");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		else {
			Toast.makeText(this, "Press the back button once again to exit.", Toast.LENGTH_SHORT).show();
			mBackButtonCount++;
		}
	}
}
