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

	int color, colors;

	public static String _list_type = "all";

	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	private int mBackButtonCount;

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
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
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
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": campaigns/?device=2");
		tracker.send(MapBuilder.createAppView().build());

	}

	// Creating tabs
	protected void onSaveInstanceState(Bundle outState) {
		// outState.putString("tab", mTabHost.getCurrentTabTag());
		super.onSaveInstanceState(outState);
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
			// ProductDisplay.this.intialiseViewPager();
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
