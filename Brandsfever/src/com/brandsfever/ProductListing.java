package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.dataholder.DataHolderClass;
import com.datamodel.ProductListDetailModel;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class ProductListing extends FragmentActivity implements OnClickListener {
	
	private static final String TAG = "ProductListing";
	
	Context _ctx = ProductListing.this;
	SimpleSideDrawer slide_me;
	private Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, _mycart, mSupport, _invite, _logout;
	static Typeface _font;
	private int pk;
	private ImageButton main_menu, back_btn, cart_btn;
	private String _tabname;
	TabHost tabs;
    ViewPager _mViewPager;
	private TabsAdapter mTabsAdapter;
	static HorizontalScrollView mHorizontalScroll;
	public static ArrayList<ProductListDetailModel> mProductList = new ArrayList<ProductListDetailModel>();
	private Set<String> ss = new HashSet<String>();
	
	SharedPreferences _mypref ;
    String _getToken = "";
    String _getuserId="";
    int color,colors;
    
    View _settings_view,_login_view,_mycart_view,_invite_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if(a<=6){
			setContentView(R.layout.activity_product_listing);
		}else if(a>=7 && a<9){
			setContentView(R.layout.seven_inch_product_listing);
		}else if(a>=9){
			setContentView(R.layout.product_listing_ten_inch);
		}

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16)+0xFF000000;
		colors = Integer.parseInt("ffffff", 16)+0xFF000000;
		
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId= _mypref .getString("ID", null); 
        _getToken = _mypref .getString("TOKEN", null);
		
		tabs = (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        mHorizontalScroll = (HorizontalScrollView) findViewById(R.id.hsv);
        _mViewPager = (ViewPager) findViewById(R.id.dynamic_pager);
        mTabsAdapter = new TabsAdapter(ProductListing.this, tabs, _mViewPager);

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));
		
		TextView set_user_name = (TextView)findViewById(R.id.set_user_name);
		String _username = _mypref .getString("_UserName", null);
		set_user_name.setTypeface(_font);

		if(_username != null){
			set_user_name.setText("Hi! "+_username);
		}else{
			set_user_name.setText("Hi! Guest");
		}

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);
		
		back_btn = (ImageButton) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

		cart_btn = (ImageButton) findViewById(R.id.cart_btn);
		cart_btn.setOnClickListener(this);
		
		pk = DataHolderClass.getInstance().get_mainProductsPk();
		new GetAllProductsList().execute();

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
		_childrens.setOnClickListener(this);

		_home = (Button) findViewById(R.id.cat_home);
		_home.setTypeface(_font);
		_home.setOnClickListener(this);

		_accessories = (Button) findViewById(R.id.cat_accesories);
		_accessories.setTypeface(_font);
		_accessories.setOnClickListener(this);

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

		mSupport = (Button) findViewById(R.id.btn_support);
		mSupport.setTypeface(_font);
		mSupport.setOnClickListener(this);
		
		_invite = (Button) findViewById(R.id.btn_invite);
		_invite_view = (View) findViewById(R.id.btn_invite_view);
		_invite.setTypeface(_font);
		_invite.setOnClickListener(this);

		_logout = (Button) findViewById(R.id.btn_logout);
		_logout.setTypeface(_font);
		_logout.setOnClickListener(this);
		
		
		
		
		
		try{
		    if(_getToken==null && _getuserId==null){
				_login.setVisibility(View.VISIBLE);	
				_mycart.setVisibility(View.GONE);
				_settings.setVisibility(View.GONE);
				_invite.setVisibility(View.GONE);
				_logout.setVisibility(View.GONE);
				
				_login_view.setVisibility(View.GONE);
				_mycart_view.setVisibility(View.GONE);
				_settings_view.setVisibility(View.GONE);
				_invite_view.setVisibility(View.GONE);
			}else{
				_login.setVisibility(View.GONE);	
				_mycart.setVisibility(View.VISIBLE);
				_settings.setVisibility(View.VISIBLE);
				_invite.setVisibility(View.VISIBLE);
				_logout.setVisibility(View.VISIBLE);
				
				_login_view.setVisibility(View.VISIBLE);
				_mycart_view.setVisibility(View.VISIBLE);
				_settings_view.setVisibility(View.VISIBLE);
				_invite_view.setVisibility(View.VISIBLE);
				
				_all.setTextColor(colors);
		        _men.setTextColor(colors);
		        _women.setTextColor(colors);
		        _childrens.setTextColor(colors);
		        _home.setTextColor(colors);
		        _accessories.setTextColor(colors);
				_settings.setTextColor(colors);
				_mycart.setTextColor(colors);
				_invite.setTextColor(colors);
				
		     }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		
		
		
		if(ProductDisplay._list_type.equalsIgnoreCase("all")){
			_all.setTextColor(color);
		}else if(ProductDisplay._list_type.equalsIgnoreCase("men")){
			  _men.setTextColor(color);
		}else if(ProductDisplay._list_type.equalsIgnoreCase("women")){
			_women.setTextColor(color);
		}else if(ProductDisplay._list_type.equalsIgnoreCase("children")){
			_childrens.setTextColor(color);
		}else if(ProductDisplay._list_type.equalsIgnoreCase("home")){
			 _home.setTextColor(color);
		}else if(ProductDisplay._list_type.equalsIgnoreCase("accessories")){
			 _accessories.setTextColor(color);
		}
		
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": campaigns/"+pk+"/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		String tag_name;

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new TabHost.TabContentFactory() {
				@Override
				public View createTabContent(String tag) {
					return new TextView(mContext);
				}
			});
			String tag = tabSpec.getTag();
			tag_name = tag;
			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);

			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);

		}

		public void onPageSelected(int position) {
			TabWidget widget = mTabHost.getTabWidget();
			this.mTabHost.setCurrentTab(position);
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			// TODO Auto-generated method stub
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
		public void onTabChanged(String tabId) {
			int _pos = this.mTabHost.getCurrentTab();
			notifyDataSetChanged();
			this.mViewPager.setCurrentItem(_pos);
			TabWidget widget = mTabHost.getTabWidget();
			for(int i = _pos; i < widget.getChildCount(); i++) {
	       	 View v = widget.getChildAt(i);
	       	 TextView tv = (TextView)v.findViewById(android.R.id.title);
	       	if(DataHolderClass.getInstance().get_deviceInch()<=6){
				tv.setTextSize(14);
			}else if(DataHolderClass.getInstance().get_deviceInch()>=7){
				tv.setTextSize(17);
			}
	    	
//	       	 tv.setTypeface(_font);
	       	 tv.setTextColor(Color.parseColor("#000000"));       	 
	       	 v.setBackgroundResource(R.drawable.selector);
	       }
		}
	}
 
	class GetAllProductsList extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(ProductListing.this,
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
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			mProductList.clear();
			String _url = "https://api-1.brandsfever.com/products/list/" + pk;
			GetAll_Products_List(_url);
			return null;
		}

		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected void onPostExecute(String result) {
			try {
				_mViewPager.setOffscreenPageLimit(ss.size());

				Bundle bundles = new Bundle();
				bundles.putString("name", "All Products");

				TabHost.TabSpec specs = tabs.newTabSpec("All Products");
				specs.setIndicator(_tabname);
				mTabsAdapter.addTab(
						tabs.newTabSpec("All Products").setIndicator("All Products"),
						DynamicDisplayFragment.class, bundles);
				
				for (Iterator<String> it = ss.iterator(); it.hasNext();) {
					_tabname = it.next();

					Bundle bundle = new Bundle();
					bundle.putString("name", _tabname);

					TabHost.TabSpec spec = tabs.newTabSpec(_tabname);
					spec.setIndicator(_tabname);
					mTabsAdapter.addTab(
							tabs.newTabSpec(_tabname).setIndicator(_tabname),
							DynamicDisplayFragment.class, bundle);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			mProgressHUD.dismiss();
		}
	}

	public void GetAll_Products_List(String url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpClient = HttpsClient.getNewHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			if (responseCode == 200) {
				InputStream inputStream = httpResponse.getEntity()
						.getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						inputStream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				String data = total.toString();
				try {
					JSONObject obj = new JSONObject(data);
					String msgs = obj.getString("msg");
					if (msgs.equalsIgnoreCase("ok")) {
						JSONArray listProducts = new JSONArray(
								obj.getString("products"));

						for (int i = 0; i < listProducts.length(); i++) {
							JSONObject js = listProducts.getJSONObject(i);
							ProductListDetailModel listmodel = new ProductListDetailModel();

							String pk = js.getString("pk");
							String marketPrice = js.getString("market_price");
							String name = js.getString("name");
							String img = js.getString("img");
							String salesPrice = js.getString("sales_price");
							int stockLeft = js.getInt("available_stock");

							ArrayList<String> catge = new ArrayList<String>();
							JSONArray jobj = js.getJSONArray("categories");
							String category;
							for (int j = 0; j < jobj.length(); j++) {
								category = jobj.getString(j);
								catge.add(category);
								ss.add(category);
							}
							listmodel.setPk(pk);
							listmodel.setMarket_price(marketPrice);
							listmodel.setName(name);
							listmodel.setImg(img);
							listmodel.setSales_price(salesPrice);
							listmodel.set_availstock(stockLeft);
							listmodel.set_catagory(jobj.get(0).toString());
							mProductList.add(listmodel);
						}
					} else {
						Log.e(TAG,"error");
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_menu:
			slide_me.toggleLeftDrawer();
			break;
		case R.id.btn_all_cat:
			ProductDisplay._list_type="all";
			slide_me.closeRightSide();
			Intent all = new Intent(_ctx, ProductDisplay.class);
			all.putExtra("tab", "all");
			startActivity(all);
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			finish();
			break;
			
		case R.id.cart_btn:
			if(!(_getToken==null)&& !(_getuserId==null)){
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

		case R.id.cat_men:
			ProductDisplay._list_type="men";
			slide_me.closeRightSide();
			Intent men = new Intent(_ctx, ProductDisplay.class);
			men.putExtra("tab", "men");
			startActivity(men);
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_women:
			ProductDisplay._list_type="women";
			slide_me.closeRightSide();
			Intent women = new Intent(_ctx, ProductDisplay.class);
			women.putExtra("tab", "women");
			startActivity(women);
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_children:
			ProductDisplay._list_type="children";
			slide_me.closeRightSide();
			Intent children = new Intent(_ctx, ProductDisplay.class);
			children.putExtra("tab", "children");
			startActivity(children);
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_home:
			ProductDisplay._list_type="home";
			slide_me.closeRightSide();
			Intent home = new Intent(_ctx, ProductDisplay.class);
			home.putExtra("tab", "home");
			startActivity(home);
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_accesories:
			ProductDisplay._list_type="accessories";
			slide_me.closeRightSide();
			Intent acc = new Intent(_ctx, ProductDisplay.class);
			acc.putExtra("tab", "accessories");
			startActivity(acc);
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_login:
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				Intent _login = new Intent(_ctx, PhoneLoginScreen.class);
				startActivityForResult(_login, 1);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.puch_out_to_top,R.anim.push_out_to_bottom);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
				Intent _login = new Intent(_ctx, PhoneLoginScreen.class);
				startActivityForResult(_login, 1);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.puch_out_to_top,R.anim.login_screen_back);
			}
			break;


		case R.id.btn_setting:
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				Intent _phonesetting = new Intent(_ctx, SettingPhone.class);
				startActivity(_phonesetting);
				overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
				finish();
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
					&& DataHolderClass.getInstance().get_deviceInch() < 9) {
				Intent _tabsetting = new Intent(_ctx, SettingTab.class);
				startActivity(_tabsetting);
				overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
				finish();
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
				Intent _tabsetting = new Intent(_ctx, SettingTab.class);
				startActivity(_tabsetting);
				overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.my_cart:
			Intent _cart = new Intent(ProductListing.this,MyCartScreen.class);
			startActivity(_cart);
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_support:
			
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
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_logout:
			Editor editor = _mypref.edit();
			editor.clear();
			editor.commit();
			Intent _intent = new Intent(getApplicationContext(),ProductDisplay.class);
			startActivity(_intent);
			overridePendingTransition(R.anim.push_out_to_right,R.anim.push_out_to_left);
			break;
			
		case R.id.back_btn:
			finish();
			break;

		default:
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		  if (requestCode == 1) {

		     if(resultCode == RESULT_OK){      
		         startActivity(getIntent());
		     if (resultCode == RESULT_CANCELED) {    
		         //Do nothing?
		     }
		  }
		}
    }
	
	@Override
	public void onBackPressed() {
		finish();
	}
}