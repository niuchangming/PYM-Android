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
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import com.actionbarsherlock.view.MenuItem;
import com.adapter.VPagerAdapter;
import com.crashlytics.android.Crashlytics;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductsDataModel;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class ProductDisplay extends SlidingFragmentActivity {

	private static final String TAG = "ProductDisplay";

	private Fragment mContent;

	Context _ctx = ProductDisplay.this;
	ImageButton _Menu, cart_btn;
	Typeface _font;
	String catagory_name;
	String _TabName;
	Bundle bundle;
	TextView set_user_name;
	public static ArrayList<ProductsDataModel> all_prdt = new ArrayList<ProductsDataModel>();
	public static ArrayList<ProductsDataModel> men_prdt = new ArrayList<ProductsDataModel>();
	public static ArrayList<ProductsDataModel> women_prdt = new ArrayList<ProductsDataModel>();
	public static ArrayList<ProductsDataModel> children_prdt = new ArrayList<ProductsDataModel>();
	public static ArrayList<ProductsDataModel> home_prdt = new ArrayList<ProductsDataModel>();
	public static ArrayList<ProductsDataModel> accesories_prdt = new ArrayList<ProductsDataModel>();

	TabHost mTabHost;
	ViewPager mViewPager;
	HashMap<String, TabInfo> mapTabInfo = new HashMap<String, ProductDisplay.TabInfo>();
	VPagerAdapter mPagerAdapter;
	HorizontalScrollView mHorizontalScroll;

	int color, colors;

	public static String _list_type = "all";

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		Log.d(TAG, "onCreate");

		setContentView(R.layout.content_frame);

		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			// show home as up so we can toggle
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null) {
			mContent = new CampaignFragment();
		}

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuFragment()).commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

		 new LoadProduct().execute();
	}

	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case android.R.id.home:
			toggle();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void updateSlidingMenu() {

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		String username = _mypref.getString("_UserName", null);
		String greeting = set_user_name.getText().toString();
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		if (username != null) {

			if (greeting.contains(username)) {
				return;
			}

			set_user_name.setText("Hi! " + username);
		} else {

			if (greeting.contains("Guest")) {
				return;
			}

			set_user_name.setText("Hi! Guest");
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": campaigns/?device=2");
		tracker.send(MapBuilder.createAppView().build());

//		updateSlidingMenu();
	}

	// Creating tabs
	protected void onSaveInstanceState(Bundle outState) {
//		outState.putString("tab", mTabHost.getCurrentTabTag());
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager() {
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this,
				AllProductDisplay.class.getName()));
		fragments.add(Fragment.instantiate(this,
				WomenProductDisplay.class.getName()));
		fragments.add(Fragment.instantiate(this,
				MenProductDisplay.class.getName()));
		fragments.add(Fragment.instantiate(this,
				ChildrenProductDisplay.class.getName()));
		fragments.add(Fragment.instantiate(this,
				HomeProductDisplay.class.getName()));
		fragments.add(Fragment.instantiate(this,
				AccesroiesProductDisplay.class.getName()));
		this.mPagerAdapter = new VPagerAdapter(
				super.getSupportFragmentManager(), fragments);
		this.mViewPager = (ViewPager) super.findViewById(R.id.viewPagers);
		this.mViewPager.setAdapter(this.mPagerAdapter);
//		this.mViewPager.setOnPageChangeListener(this);
		if (bundle != null) {
			if (_TabName.equalsIgnoreCase("all")) {
				mViewPager.setCurrentItem(0);
			} else if (_TabName.equalsIgnoreCase("women")) {
				mViewPager.setCurrentItem(1);
			} else if (_TabName.equalsIgnoreCase("men")) {
				mViewPager.setCurrentItem(2);
			} else if (_TabName.equalsIgnoreCase("children")) {
				mViewPager.setCurrentItem(3);
			} else if (_TabName.equalsIgnoreCase("home")) {
				mViewPager.setCurrentItem(4);
			} else if (_TabName.equalsIgnoreCase("accessories")) {
				mViewPager.setCurrentItem(5);
			} else {
				mViewPager.setCurrentItem(0);
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
		ProductDisplay
				.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2")
						.setIndicator("WOMEN"), (tabInfo = new TabInfo("Tab2",
						WomenProductDisplay.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		ProductDisplay.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab3").setIndicator("MEN"),
				(tabInfo = new TabInfo("Tab3", MenProductDisplay.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		ProductDisplay.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab4").setIndicator("CHILDREN"),
				(tabInfo = new TabInfo("Tab4", ChildrenProductDisplay.class,
						args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		ProductDisplay
				.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab5")
						.setIndicator("HOME"), (tabInfo = new TabInfo("Tab5",
						HomeProductDisplay.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		ProductDisplay.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Tab6").setIndicator("ACCESSORIES"),
				(tabInfo = new TabInfo("Tab6", AccesroiesProductDisplay.class,
						args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		TabWidget widget = mTabHost.getTabWidget();
		for (int i = 0; i < widget.getChildCount(); i++) {
			View v = widget.getChildAt(i);
			TextView tv = (TextView) v.findViewById(android.R.id.title);
			int color = Integer.parseInt("000000", 16) + 0xFF000000;
			tv.setTextColor(color);
			tv.setTypeface(_font, Typeface.BOLD);
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				tv.setTextSize(14);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
				tv.setTextSize(20);
			}

			v.setBackgroundResource(R.drawable.selector);
		}
//		mTabHost.setOnTabChangedListener(this);
	}

	private static void AddTab(ProductDisplay activity, TabHost tabHost,
			TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {

		// case R.id.cat_men:
		// _list_type="men";
		// mViewPager.setCurrentItem(2);
		// slide_me.closeRightSide();
		// break;
		// case R.id.cat_children:
		// _list_type="children";
		// mViewPager.setCurrentItem(3);
		// slide_me.closeRightSide();
		// break;
		// case R.id.cat_home:
		// _list_type="home";
		// mViewPager.setCurrentItem(4);
		// slide_me.closeRightSide();
		// break;
		// case R.id.cat_accesories:
		// _list_type="accesories";
		// mViewPager.setCurrentItem(5);
		// slide_me.closeRightSide();
		// break;
		// case R.id.btn_login:
		// if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
		// Intent login = new Intent(_ctx, PhoneLoginScreen.class);
		// startActivityForResult(login, 1);
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.puch_out_to_top,R.anim.push_out_to_bottom);
		//
		// } else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
		// Intent _login = new Intent(_ctx, PhoneLoginScreen.class);
		// startActivityForResult(_login, 1);
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.puch_out_to_top,R.anim.login_screen_back);
		// }
		// break;
		// case R.id.my_cart:
		// Intent _cart = new Intent(ProductDisplay.this, MyCartScreen.class);
		// startActivity(_cart);
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
		// break;
		//
		// case R.id.btn_support:
		// Log.d(TAG, "support is clicked.");
		// Intent support = new
		// Intent(ProductDisplay.this,SupportActivity.class);
		// startActivity(support);
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
		// break;
		// case R.id.btn_invite:
		// Intent _invit = new Intent(_ctx, InviteSction_Screen.class);
		// startActivity(_invit);
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
		// break;
		// case R.id.btn_logout:
		// Editor editor = _mypref.edit();
		// editor.clear();
		// editor.commit();
		// Intent _intent = new
		// Intent(getApplicationContext(),ProductDisplay.class);
		// startActivity(_intent);
		// DataHolderClass.getInstance().setUsername("Hi! Guest");
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
		// break;
		// case R.id.btn_setting:
		// if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
		// Intent _phonesetting = new Intent(_ctx, SettingPhone.class);
		// startActivity(_phonesetting);
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
		// } else if (DataHolderClass.getInstance().get_deviceInch() >= 7
		// && DataHolderClass.getInstance().get_deviceInch() < 9) {
		// Intent _tabsetting = new Intent(_ctx, SettingTab.class);
		// startActivity(_tabsetting);
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
		// } else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
		// Intent _tabsetting = new Intent(_ctx, SettingTab.class);
		// startActivity(_tabsetting);
		// slide_me.closeRightSide();
		// overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
		// }
		// break;
		//
		// case R.id.cart_btn:
		// if((_getToken!=null)&& (_getuserId!=null)){
		// Intent _gotocart = new Intent(_ctx,MyCartScreen.class);
		// startActivity(_gotocart);
		// overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
		// }else{
		// LayoutInflater inflater = getLayoutInflater();
		// View view = inflater.inflate(R.layout.error_popop,(ViewGroup)
		// findViewById(R.id.relativeLayout1));
		// final TextView _seterrormsg =
		// (TextView)view.findViewById(R.id._seterrormsg);
		// _seterrormsg.setText("Please login!");
		// Toast toast = new Toast(_ctx);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.setView(view);
		// toast.show();
		// }
		// break;
//		}
//	}

//	@Override
//	public void onPageScrollStateChanged(int arg0) {
//	}
//
//	@Override
//	public void onPageScrolled(int position, float positionOffset,
//			int positionOffsetPixels) {
//		View tabView = mTabHost.getTabWidget().getChildAt(position);
//		if (tabView != null) {
//			final int width = mHorizontalScroll.getWidth();
//			final int scrollPos = tabView.getLeft()
//					- (width - tabView.getWidth()) / 2;
//			mHorizontalScroll.scrollTo(scrollPos, 0);
//		} else {
//			mHorizontalScroll.scrollBy(positionOffsetPixels, 0);
//		}
//	}
//
//	@Override
//	public void onPageSelected(int position) {
//		this.mTabHost.setCurrentTab(position);
//	}
//
//	@Override
//	public void onTabChanged(String tabId) {
//
//		int pos = this.mTabHost.getCurrentTab();
//
//		if (this.mViewPager != null)
//			this.mViewPager.setCurrentItem(pos);
//
//	}

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
			accesories_prdt.clear();
			men_prdt.clear();
			women_prdt.clear();
			home_prdt.clear();
			children_prdt.clear();

			String url_campaigns = "https://api-1.brandsfever.com/campaigns/channel/"
					+ getApplicationContext().getResources().getString(
							R.string.channel_code);
			GetProducts(url_campaigns);
			return null;
		}

		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			mProgressHUD.dismiss();
//			ProductDisplay.this.intialiseViewPager();
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
						ProductsDataModel men_data_model = new ProductsDataModel();
						ProductsDataModel women_data_model = new ProductsDataModel();
						ProductsDataModel children_data_model = new ProductsDataModel();
						ProductsDataModel home_data_model = new ProductsDataModel();
						ProductsDataModel accessories_data_model = new ProductsDataModel();

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

						JSONArray jobj = jsonobj.getJSONArray("category");
						for (int j = 0; j < jobj.length(); j++) {
							catagory_name = jobj.getString(j);
							if (catagory_name.equalsIgnoreCase("men")) {
								men_data_model.setEnds_at(ends_at);
								men_data_model.setTeaser_url(teaser_url);
								men_data_model.setName(name);
								men_data_model.setStarts_at(starts_at);
								men_data_model.setPk(pk);
								men_data_model.setExpress(express);
								men_data_model.setDiscount_text(discount_text);
								men_data_model.setFree_shipping(free_shipping);
								men_data_model.setShipping_fee(shipping_fee);
								men_data_model
										.setShipping_period(shipping_period);
								men_prdt.add(men_data_model);
							} else if (catagory_name.equalsIgnoreCase("women")) {
								women_data_model.setEnds_at(ends_at);
								women_data_model.setTeaser_url(teaser_url);
								women_data_model.setName(name);
								women_data_model.setStarts_at(starts_at);
								women_data_model.setPk(pk);
								women_data_model.setExpress(express);
								women_data_model
										.setDiscount_text(discount_text);
								women_data_model
										.setFree_shipping(free_shipping);
								women_data_model.setShipping_fee(shipping_fee);
								women_data_model
										.setShipping_period(shipping_period);
								women_prdt.add(women_data_model);
							} else if (catagory_name
									.equalsIgnoreCase("children")) {
								children_data_model.setEnds_at(ends_at);
								children_data_model.setTeaser_url(teaser_url);
								children_data_model.setName(name);
								children_data_model.setStarts_at(starts_at);
								children_data_model.setPk(pk);
								children_data_model.setExpress(express);
								children_data_model
										.setDiscount_text(discount_text);
								children_data_model
										.setFree_shipping(free_shipping);
								children_data_model
										.setShipping_fee(shipping_fee);
								children_data_model
										.setShipping_period(shipping_period);
								children_prdt.add(children_data_model);
							} else if (catagory_name.equalsIgnoreCase("home")) {
								home_data_model.setEnds_at(ends_at);
								home_data_model.setTeaser_url(teaser_url);
								home_data_model.setName(name);
								home_data_model.setStarts_at(starts_at);
								home_data_model.setPk(pk);
								home_data_model.setExpress(express);
								home_data_model.setDiscount_text(discount_text);
								home_data_model.setFree_shipping(free_shipping);
								home_data_model.setShipping_fee(shipping_fee);
								home_data_model
										.setShipping_period(shipping_period);
								home_prdt.add(home_data_model);
							} else if (catagory_name
									.equalsIgnoreCase("accessories")) {
								accessories_data_model.setEnds_at(ends_at);
								accessories_data_model
										.setTeaser_url(teaser_url);
								accessories_data_model.setName(name);
								accessories_data_model.setStarts_at(starts_at);
								accessories_data_model.setPk(pk);
								accessories_data_model.setExpress(express);
								accessories_data_model
										.setDiscount_text(discount_text);
								accessories_data_model
										.setFree_shipping(free_shipping);
								accessories_data_model
										.setShipping_fee(shipping_fee);
								accessories_data_model
										.setShipping_period(shipping_period);
								accesories_prdt.add(accessories_data_model);
							}
						}
						all_prdt.add(all_data_model);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Log.e(TAG, "error");
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

		if (mBackButtonCount >= 1) {
			Log.e(TAG, "EXIT");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			Toast.makeText(this, "Press the back button once again to exit.",
					Toast.LENGTH_SHORT).show();
			mBackButtonCount++;
		}
	}
}
