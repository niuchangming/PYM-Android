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
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductListDetailModel;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class ProductListing extends SherlockFragmentActivity {

	private static final String TAG = "ProductListing";

	Context _ctx = ProductListing.this;

	static Typeface _font;
	private int pk;
	private String _tabname;
	TabHost tabs;
	ViewPager _mViewPager;
	private TabsAdapter mTabsAdapter;
	static HorizontalScrollView mHorizontalScroll;
	public static ArrayList<ProductListDetailModel> mProductList = new ArrayList<ProductListDetailModel>();
	private Set<String> ss = new HashSet<String>();

	SharedPreferences _mypref;
	int color, colors;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_product_listing);

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
				directToCart();
			}
		});

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);

		tabs = (TabHost) findViewById(R.id.tabhost);
		tabs.setup();
		mHorizontalScroll = (HorizontalScrollView) findViewById(R.id.hsv);
		_mViewPager = (ViewPager) findViewById(R.id.dynamic_pager);
		mTabsAdapter = new TabsAdapter(ProductListing.this, tabs, _mViewPager);

		pk = DataHolderClass.getInstance().get_mainProductsPk();
		new GetAllProductsList().execute();
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": campaigns/" + pk + "/?device=2");
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
			for (int i = _pos; i < widget.getChildCount(); i++) {
				View v = widget.getChildAt(i);
				TextView tv = (TextView) v.findViewById(android.R.id.title);
				if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
					tv.setTextSize(14);
				} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
					tv.setTextSize(17);
				}

				// tv.setTypeface(_font);
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
			mProgressHUD = ProgressHUD.show(ProductListing.this, "Loading",
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

				if (!ss.contains("All Products")) {

					Bundle bundles = new Bundle();
					bundles.putString("name", "All Products");

					TabHost.TabSpec specs = tabs.newTabSpec("All Products");
					specs.setIndicator(_tabname);
					mTabsAdapter.addTab(tabs.newTabSpec("All Products")
							.setIndicator("All Products"),
							DynamicDisplayFragment.class, bundles);
				}

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
				InputStream inputStream = httpResponse.getEntity().getContent();
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
						Log.e(TAG, "error");
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

	private void directToCart() {

		SharedPreferences mypref = getApplicationContext()
				.getSharedPreferences("mypref", 0);
		String username = mypref.getString("UserName", null);
		
		if (username != null) { // check login status
			Intent gotocart = new Intent(ProductListing.this, MyCartActivity.class);
			startActivity(gotocart);
		} else {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.error_popop,
					(ViewGroup) findViewById(R.id.relativeLayout1));
			final TextView msgTextView = (TextView) view
					.findViewById(R.id._seterrormsg);
			msgTextView.setText("Please login!");
			Toast toast = new Toast(ProductListing.this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.show();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}