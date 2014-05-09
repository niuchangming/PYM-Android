package com.brandsfever;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import com.dataholder.DataHolderClass;
import com.datamodel.SingleItemDataModel;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

import com.navdrawer.SimpleSideDrawer;

import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;
import com.viewpagerindicator.CirclePageIndicator;

public class SingleProductDisplay extends FragmentActivity implements
		OnClickListener, OnItemSelectedListener {
	Context _ctx = SingleProductDisplay.this;
	ImageButton main_menu, back_btn, cart_btn;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, _mycart, _invite, _logout;
	Typeface _font;
	int color, colors;
	ImageButton productinfo, shipinginfo, open_size_chart, add_to_cart,
			buy_now, click_to_zoom;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	String ends_at, _pk, name, offer_price, market_price, description;
	private int btn_press_count = 1;
	Spinner set_size, set_quantity;
	Spinner _sizes;
	Integer _sendItemPK = 0;
	private CustomSpinnerSizeAdapter _adapter;
	private String _acharturl;
	private int pk;
	public ArrayList<SingleItemDataModel> _product_items_arraylist = new ArrayList<SingleItemDataModel>();
	public ArrayList<SingleItemDataModel> _sizechartvalue = new ArrayList<SingleItemDataModel>();
	public ArrayList<SingleItemDataModel> _imglist = new ArrayList<SingleItemDataModel>();
	@SuppressLint("UseSparseArrays")
	HashMap<Integer, ArrayList<String>> img_map = new HashMap<Integer, ArrayList<String>>();
	AQuery aq = new AQuery(this);
	Button scrollbutton;
	ScrollView _scroll;
	RelativeLayout _layout;
	View _v;
	RelativeLayout _foot;
	Toast toast;
	String _cartResponse;
	TextView _pname, _settimeframe, set_product_description, set_shiping_info,
			set_market_price, set_sales_price;
	ProgressHUD _popmsg;
	TextView _seterrormsg;
	static String _zoomimage;

	ImageButton shares;
	int click_button = 0;
	int width, height;
	PopupWindow popupWindow;
	int popupWindow_status = 0;
	Display display;

	int type = 0;
	byte[] bitmapdata;
	private Facebook mFacebook;
	public String oauthVerifier;
	private String twitedata;
	protected Twitter mTwitter;
	ProgressDialog pDialog;
	protected RequestToken mRequestToken;
	public Bitmap facebook_bit_map;

	RelativeLayout parent_layout;

	View _settings_view, _login_view, _mycart_view, _invite_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.activity_single_product_display);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.single_product_display_seven_inch);
		} else if (a >= 9) {
			setContentView(R.layout.single_product_display_tab);
		}

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;

		_foot = (RelativeLayout) findViewById(R.id.foot);
		_layout = (RelativeLayout) findViewById(R.id.footer_set);
		_v = (View) findViewById(R.id.v);
		_pname = (TextView) findViewById(R.id.setproductname);
		_settimeframe = (TextView) findViewById(R.id.settimeframe);
		set_market_price = (TextView) findViewById(R.id.set_market_price);
		set_sales_price = (TextView) findViewById(R.id.set_sales_price);

		parent_layout = (RelativeLayout) findViewById(R.id.parentlayout);

		shares = (ImageButton) findViewById(R.id.share);
		shares.setOnClickListener(this);

		// ********************* social site ********************//
		display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();

		set_product_description = (TextView) findViewById(R.id.set_product_description);
		set_product_description
				.setMovementMethod(new ScrollingMovementMethod());
		set_product_description.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		set_shiping_info = (TextView) findViewById(R.id.set_shiping_info);
		set_shiping_info.setMovementMethod(new ScrollingMovementMethod());
		set_shiping_info.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

		productinfo = (ImageButton) findViewById(R.id.product_info);
		productinfo.setOnClickListener(this);

		shipinginfo = (ImageButton) findViewById(R.id.shiping_info);
		shipinginfo.setOnClickListener(this);

		buy_now = (ImageButton) findViewById(R.id.buy_now);
		buy_now.setOnClickListener(this);

		back_btn = (ImageButton) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

		add_to_cart = (ImageButton) findViewById(R.id.add_to_cart);
		add_to_cart.setOnClickListener(this);

		click_to_zoom = (ImageButton) findViewById(R.id.click_to_zoom);
		click_to_zoom.setOnClickListener(this);

		open_size_chart = (ImageButton) findViewById(R.id.open_size_chart);
		open_size_chart.setOnClickListener(this);
		open_size_chart.setEnabled(false);

		_sizes = (Spinner) findViewById(R.id.display_size);
		_sizes.setOnItemSelectedListener(this);

		set_quantity = (Spinner) findViewById(R.id.display_quantity);

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String _username = _mypref.getString("_UserName", null);
		if (!(_username == null)) {
			set_user_name.setText(_username);
		} else {
			set_user_name.setText("Hi! Guest");
		}

		cart_btn = (ImageButton) findViewById(R.id.cart_btn);
		cart_btn.setOnClickListener(this);

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

		_invite = (Button) findViewById(R.id.btn_invite);
		_invite_view = (View) findViewById(R.id.btn_invite_view);
		_invite.setTypeface(_font);
		_invite.setOnClickListener(this);

		_logout = (Button) findViewById(R.id.btn_logout);
		_logout.setTypeface(_font);
		_logout.setOnClickListener(this);

		scrollbutton = (Button) findViewById(R.id.scrollbutton);
		scrollbutton.setOnClickListener(this);

		_scroll = (ScrollView) findViewById(R.id.scroll);
		_scroll.setSmoothScrollingEnabled(true);

		try {
			if (_getToken == null && _getuserId == null) {
				_login.setVisibility(View.VISIBLE);
				_mycart.setVisibility(View.GONE);
				_settings.setVisibility(View.GONE);
				_invite.setVisibility(View.GONE);
				_logout.setVisibility(View.GONE);

				_login_view.setVisibility(View.GONE);
				_mycart_view.setVisibility(View.GONE);
				_settings_view.setVisibility(View.GONE);
				_invite_view.setVisibility(View.GONE);
			} else {
				_login.setVisibility(View.GONE);
				_mycart.setVisibility(View.VISIBLE);
				_settings.setVisibility(View.VISIBLE);
				_invite.setVisibility(View.VISIBLE);
				_logout.setVisibility(View.VISIBLE);

				_login_view.setVisibility(View.VISIBLE);
				_mycart_view.setVisibility(View.VISIBLE);
				_settings_view.setVisibility(View.VISIBLE);
				_invite_view.setVisibility(View.VISIBLE);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		_all.setTextColor(colors);
		_men.setTextColor(colors);
		_women.setTextColor(colors);
		_childrens.setTextColor(colors);
		_home.setTextColor(colors);
		_accessories.setTextColor(colors);
		_settings.setTextColor(colors);
		_mycart.setTextColor(colors);
		_invite.setTextColor(colors);

		if (ProductDisplay._list_type.equalsIgnoreCase("all")) {
			_all.setTextColor(color);
		} else if (ProductDisplay._list_type.equalsIgnoreCase("men")) {
			_men.setTextColor(color);
		} else if (ProductDisplay._list_type.equalsIgnoreCase("women")) {
			_women.setTextColor(color);
		} else if (ProductDisplay._list_type.equalsIgnoreCase("children")) {
			_childrens.setTextColor(color);
		} else if (ProductDisplay._list_type.equalsIgnoreCase("home")) {
			_home.setTextColor(color);
		} else if (ProductDisplay._list_type.equalsIgnoreCase("accessories")) {
			_accessories.setTextColor(color);
		}

		pk = DataHolderClass.getInstance().get_subProductsPk();
		new SingleItemDetails().execute();

		

	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": products/"+pk+"/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	// =====================================================================================================================//
	class SingleItemDetails extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(SingleProductDisplay.this,
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
			String _url = "https://www.brandsfever.com/api/v5/products/" + pk;
			GetItemDetails(_url);
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

			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				try {
					_pname.setText(name);
					_pname.setTypeface(_font, Typeface.BOLD);
					set_sales_price.setText(offer_price.replace("GD", "$"));
					set_sales_price.setTypeface(_font, Typeface.BOLD);
					set_market_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
							| Paint.ANTI_ALIAS_FLAG);
					set_market_price.setText(market_price.replace("GD", "$"));
					set_market_price.setTypeface(_font, Typeface.BOLD);

					long timeInMilliseconds = Long.valueOf(ends_at);
					long end = timeInMilliseconds * 1000;
					long current = System.currentTimeMillis();
					long diff = end - current;
					int dayCount = (int) diff / (24 * 60 * 60 * 1000);
					int hours_left = (int) ((diff / (1000 * 60 * 60)) % 24);
					int minutes_left = (int) ((diff / (1000 * 60)) % 60);
					int seconds_left = (int) ((diff / 1000) % 60);
					String _s = "Sale ends in" + " "
							+ Integer.toString(dayCount) + " Days" + " "
							+ Integer.toString(hours_left) + ":"
							+ Integer.toString(minutes_left) + ":"
							+ Integer.toString(seconds_left);
					_settimeframe.setText(_s);
					_settimeframe.setTypeface(_font, Typeface.NORMAL);

					final TextView set_quantity = (TextView) findViewById(R.id.set_quantity);
					set_quantity.setTypeface(_font, Typeface.NORMAL);

					final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
					final ImagePagerAdapter adapter = new ImagePagerAdapter();
					viewPager.setAdapter(adapter);
					final CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
					circleIndicator.setViewPager(viewPager);

					set_product_description.setText(description);
					set_product_description.setTypeface(_font, Typeface.BOLD);
					String _s1 = DataHolderClass.getInstance()
							.getShipping_fee();
					String _s2 = DataHolderClass.getInstance()
							.getShipping_period();
					String _s3 = DataHolderClass.getInstance()
							.getFree_shipping();
					String _sI = "How much does shipping cost?\nShipping on Brandsfever costs "
							+ "S$"
							+ _s1
							+ " For every order above "
							+ "S$"
							+ _s3
							+ " shipping is free.\n\nWhen will my order arrive?\nYour order is eligible for Express Shipping and will be ready for shipping within "
							+ _s2
							+ " after the campaign is over. We will notify you via email when your order is shipped.";
					set_shiping_info.setText(_sI);
					set_shiping_info.setTypeface(_font, Typeface.BOLD);
					// play with visibility of size chart
					try {
						for (int _k = 0; _k < _sizechartvalue.size(); _k++) {
							SingleItemDataModel _obj = _sizechartvalue.get(_k);
							if (_obj.get_propertylistName().equalsIgnoreCase(
									"Size Chart")) {
								_acharturl = _obj.get_propertylistValue();
								open_size_chart.setEnabled(true);
								open_size_chart.setVisibility(View.VISIBLE);
							} else {

							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					// playing with size spinner here
					for (int _k = 0; _k < _product_items_arraylist.size(); _k++) {
						SingleItemDataModel _check = _product_items_arraylist
								.get(_k);
						if (_check.getProduct_item_property().equalsIgnoreCase(
								"")) {

						} else {
							_sizes.setVisibility(View.VISIBLE);
							_adapter = new CustomSpinnerSizeAdapter(
									SingleProductDisplay.this,
									_product_items_arraylist);
							_sizes.setAdapter(_adapter);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
				try {
					_pname.setText(name);
					_pname.setTypeface(_font, Typeface.BOLD);
					set_sales_price.setText(offer_price.replace("GD", "$"));
					set_sales_price.setTypeface(_font, Typeface.NORMAL);
					set_market_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
							| Paint.ANTI_ALIAS_FLAG);
					set_market_price.setText(market_price.replace("GD", "$"));
					set_market_price.setTypeface(_font, Typeface.NORMAL);
					TextView retail_price_tag = (TextView) findViewById(R.id.retail_price_tag);
					retail_price_tag.setTypeface(_font, Typeface.NORMAL);
					TextView offer_price_tag = (TextView) findViewById(R.id.offer_price_tag);
					offer_price_tag.setTypeface(_font, Typeface.NORMAL);
					long timeInMilliseconds = Long.valueOf(ends_at);
					long end = timeInMilliseconds * 1000;
					long current = System.currentTimeMillis();
					long diff = end - current;
					int dayCount = (int) diff / (24 * 60 * 60 * 1000);
					int hours_left = (int) ((diff / (1000 * 60 * 60)) % 24);
					int minutes_left = (int) ((diff / (1000 * 60)) % 60);
					int seconds_left = (int) ((diff / 1000) % 60);
					String _s = "Sale ends in" + " "
							+ Integer.toString(dayCount) + " Days" + " "
							+ Integer.toString(hours_left) + ":"
							+ Integer.toString(minutes_left) + ":"
							+ Integer.toString(seconds_left);
					_settimeframe.setText(_s);
					_settimeframe.setTypeface(_font, Typeface.NORMAL);

					final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
					final ImagePagerAdapter adapter = new ImagePagerAdapter();
					viewPager.setAdapter(adapter);
					final CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

					circleIndicator.setViewPager(viewPager);

					set_product_description.setText(description);
					set_product_description.setTypeface(_font, Typeface.NORMAL);
					String _s1 = DataHolderClass.getInstance()
							.getShipping_fee();
					String _s2 = DataHolderClass.getInstance()
							.getShipping_period();
					String _s3 = DataHolderClass.getInstance()
							.getFree_shipping();
					String _sI = "How much does shipping cost?\nShipping on Brandsfever costs "
							+ "S$"
							+ _s1
							+ " For every order above "
							+ "S$"
							+ _s3
							+ " shipping is free.\n\nWhen will my order arrive?\nYour order is eligible for Express Shipping and will be ready for shipping within "
							+ _s2
							+ " after the campaign is over. We will notify you via email when your order is shipped.";
					set_shiping_info.setText(_sI);
					set_shiping_info.setTypeface(_font, Typeface.NORMAL);

					// palying with visibility of size chart

					try {
						for (int _k = 0; _k < _sizechartvalue.size(); _k++) {
							SingleItemDataModel _obj = _sizechartvalue.get(_k);
							if (_obj.get_propertylistName().equalsIgnoreCase(
									"Size Chart")) {
								_acharturl = _obj.get_propertylistValue();
								open_size_chart
										.setBackgroundResource(R.drawable.size);
								open_size_chart.setEnabled(true);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					// playing with size spinner
					for (int _k = 0; _k < _product_items_arraylist.size(); _k++) {
						SingleItemDataModel _check = _product_items_arraylist
								.get(_k);
						if (_check.getProduct_item_property().equalsIgnoreCase(
								"")) {
							_sizes.setBackgroundResource(R.drawable.disabled_drop);
						} else {
							_adapter = new CustomSpinnerSizeAdapter(
									SingleProductDisplay.this,
									_product_items_arraylist);
							_sizes.setAdapter(_adapter);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mProgressHUD.dismiss();
		}
	}

	// =======================================================================================================================//
	public void GetItemDetails(String _url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(_url);
		try {
			HttpResponse _httpresponse = _httpclient.execute(_httpget);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			Log.i("--------------Responsecode----------", "." + _responsecode);
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
				String _data = total.toString();
				System.out.println(_data);
				try {
					JSONObject obj = new JSONObject(_data);
					String msgs = obj.getString("msg");
					if (msgs.equalsIgnoreCase("ok")) {

						JSONObject data = obj.getJSONObject("data");
						ends_at = data.getString("ends_at");
						market_price = data.getString("market_price");
						offer_price = data.getString("sales_price");
						name = data.getString("name");
						_pk = data.getString("pk");
						description = data.getString("description");

						JSONArray _productItems = data
								.getJSONArray("product_items");
						for (int i = 0; i < _productItems.length(); i++) {
							SingleItemDataModel _singleobj = new SingleItemDataModel();
							JSONObject jsonobj = _productItems.getJSONObject(i);

							String _product_item_name = jsonobj
									.getString("product_item_name");
							String _availablestock = jsonobj
									.getString("available stock");
							String _product_item_pk = jsonobj
									.getString("product_item_pk");
							String product_item_property = jsonobj
									.getString("product_item_property");

							_singleobj.setProduct_item_name(_product_item_name);
							_singleobj.set_availablestock(_availablestock);
							_singleobj.setProduct_item_pk(_product_item_pk);
							_singleobj
									.setProduct_item_property(product_item_property);

							_product_items_arraylist.add(_singleobj);

						}
						System.out.println("arraylist size is"
								+ _product_items_arraylist.size());

						JSONArray _photos = data.getJSONArray("photos");
						SingleItemDataModel _imageobj = new SingleItemDataModel();
						ArrayList<String> _productImages = new ArrayList<String>();
						for (int k = 0; k < _photos.length(); k++) {
							JSONArray _picobj = _photos.getJSONArray(k);
							ArrayList<String> img_list = new ArrayList<String>();
							for (int l = 0; l < _picobj.length(); l++) {
								_productImages.add(_picobj.getString(l));
								img_list.add(_picobj.getString(l));
							}
							img_map.put(k, img_list);
							_imageobj.setImages_list(_productImages);
							_imglist.add(_imageobj);
						}
						JSONArray _propertyList = data
								.getJSONArray("property_list");
						for (int j = 0; j < _propertyList.length(); j++) {
							SingleItemDataModel _singleobjs = new SingleItemDataModel();
							JSONObject jsonobjs = _propertyList
									.getJSONObject(j);

							String _propertyName = jsonobjs.getString("name");
							String _propertyValue = jsonobjs.getString("value");

							_singleobjs.set_propertylistName(_propertyName);
							_singleobjs.set_propertylistValue(_propertyValue);

							_sizechartvalue.add(_singleobjs);
						}

					} else {
						System.out.println("error");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// =====================================================================================================================//
	private class ImagePagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(final ViewGroup container, final int position,
				final Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}

		@Override
		public int getCount() {
			return img_map.size();
		}

		@SuppressLint("NewApi")
		@Override
		public Object instantiateItem(final ViewGroup container,
				final int position) {
			final Context context = SingleProductDisplay.this;
			final ImageView imageView = new ImageView(context);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setAdjustViewBounds(true);
			imageView.setFitsSystemWindows(true);
			AQuery aq = new AQuery(context);
			aq.id(imageView).image("https:" + img_map.get(position).get(0));

			aq.ajax("https:" + img_map.get(position).get(0), Bitmap.class,
					new AjaxCallback<Bitmap>() {
						@Override
						public void callback(String url, Bitmap object,
								AjaxStatus status) {
							facebook_bit_map = object;
						}
					});
			System.out.println("iii---" + facebook_bit_map);

			_zoomimage = "https:" + img_map.get(position).get(0);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public boolean isViewFromObject(final View view, final Object object) {
			return view == ((ImageView) object);
		}
	}

	// =======================================================================================================================//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_menu:
			if (click_button % 2 != 0) {
			}
			slide_me.toggleLeftDrawer();
			break;
		case R.id.share:
			if (click_button % 2 == 0) {
				popupWindow_status = 1;
				share_method();
			} else {
			}
			click_button++;

			break;

		case R.id.cart_btn:
			if (click_button % 2 != 0) {
			}
			if (!(_getToken == null) && !(_getuserId == null)) {
				Intent _gotocart = new Intent(_ctx, MyCartScreen.class);
				startActivity(_gotocart);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			} else {

				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				_seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
				_seterrormsg.setText("Please login!");
				toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
			break;

		case R.id.btn_all_cat:
			ProductDisplay._list_type = "all";
			slide_me.closeRightSide();
			Intent all = new Intent(_ctx, ProductDisplay.class);
			all.putExtra("tab", "all");
			startActivity(all);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.click_to_zoom:
			Intent _zoom = new Intent(getApplicationContext(), ZoomShow.class);
			startActivity(_zoom);
			break;

		case R.id.cat_men:
			ProductDisplay._list_type = "men";
			slide_me.closeRightSide();
			Intent men = new Intent(_ctx, ProductDisplay.class);
			men.putExtra("tab", "men");
			startActivity(men);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_women:
			ProductDisplay._list_type = "women";
			slide_me.closeRightSide();
			Intent women = new Intent(_ctx, ProductDisplay.class);
			women.putExtra("tab", "women");
			startActivity(women);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_children:
			ProductDisplay._list_type = "children";
			slide_me.closeRightSide();
			Intent children = new Intent(_ctx, ProductDisplay.class);
			children.putExtra("tab", "children");
			startActivity(children);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_home:
			ProductDisplay._list_type = "home";
			slide_me.closeRightSide();
			Intent home = new Intent(_ctx, ProductDisplay.class);
			home.putExtra("tab", "home");
			startActivity(home);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_accesories:
			ProductDisplay._list_type = "accessories";
			slide_me.closeRightSide();
			Intent acc = new Intent(_ctx, ProductDisplay.class);
			acc.putExtra("tab", "accessories");
			startActivity(acc);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_login:
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				Intent _login = new Intent(_ctx, PhoneLoginScreen.class);
				startActivityForResult(_login, 1);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.puch_out_to_top,
						R.anim.push_out_to_bottom);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
				Intent _login = new Intent(_ctx, PhoneLoginScreen.class);
				startActivityForResult(_login, 1);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.puch_out_to_top,
						R.anim.login_screen_back);
			}
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

		case R.id.my_cart:
			Intent _cart = new Intent(SingleProductDisplay.this,
					MyCartScreen.class);
			startActivity(_cart);
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

		case R.id.btn_logout:
			Editor editor = _mypref.edit();
			editor.clear();
			editor.commit();
			Intent _intent = new Intent(getApplicationContext(),
					ProductDisplay.class);
			startActivity(_intent);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.scrollbutton:
			if (click_button % 2 != 0) {
			}
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				if (btn_press_count == 1) {
					_foot.setVisibility(View.VISIBLE);
					_layout.setVisibility(View.VISIBLE);
					_v.setVisibility(View.VISIBLE);
					_scroll.scrollTo(0, _scroll.getBottom());
					btn_press_count = 0;
				} else if (btn_press_count == 0) {
					_foot.setVisibility(View.GONE);
					_layout.setVisibility(View.GONE);
					_v.setVisibility(View.GONE);
					_scroll.scrollTo(0, _scroll.getTop());
					btn_press_count = 1;
				}
			} else {
				_scroll.scrollTo(0, _scroll.getTop());
			}

			break;

		case R.id.product_info:
			shipinginfo.setBackgroundResource(R.drawable.shipping_light_tab);
			productinfo.setBackgroundResource(R.drawable.prdt_info_dark_tab);
			set_shiping_info.setVisibility(View.GONE);
			set_product_description.setVisibility(View.VISIBLE);
			break;

		case R.id.shiping_info:
			shipinginfo.setBackgroundResource(R.drawable.shiping_dark_tab);
			productinfo.setBackgroundResource(R.drawable.prdt_info_light_tab);
			set_shiping_info.setVisibility(View.VISIBLE);
			set_product_description.setVisibility(View.GONE);
			break;

		case R.id.open_size_chart:
			String _charturl = _acharturl;
			DialogFragment f = new MyDialogFragment();
			Bundle args = new Bundle();
			args.putString("_sizecharturl", _charturl);
			f.setArguments(args);
			System.out.println("chart");
			f.show(getSupportFragmentManager(), "");
			break;

		case R.id.add_to_cart:
			if (click_button % 2 != 0) {
			}
			try {
				System.out.println("in cart");
				if (!(_getToken == null) && !(_getuserId == null)) {
					new AddToCartClass().execute();
				} else {
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.relativeLayout1));
					_seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText("Please login!");
					toast = new Toast(_ctx);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.buy_now:
			if (click_button % 2 != 0) {
			}
			try {
				if (!(_getToken == null) && !(_getuserId == null)
						&& _getToken.length() > 0 && _getuserId.length() > 0) {
					new BuyNowClass().execute();
				} else {
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.relativeLayout1));
					_seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText("Please login!");
					toast = new Toast(_ctx);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		case R.id.back_btn:
			Intent _gobck = new Intent(_ctx, ProductListing.class);
			startActivity(_gobck);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		default:
			break;
		}

	}

	// ===========================================================================================================================//
	private class CustomSpinnerSizeAdapter extends BaseAdapter {
		Context _ctxx;
		LayoutInflater inflater;
		public ArrayList<SingleItemDataModel> _sizedata;
		View itemView;

		public CustomSpinnerSizeAdapter(Context c,
				ArrayList<SingleItemDataModel> _arraylist) {
			this._ctxx = c;
			this._sizedata = _arraylist;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _sizedata.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView _itemsize;
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				inflater = (LayoutInflater) _ctxx.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.custom_size_spinner_phone,
						parent, false);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
				inflater = (LayoutInflater) _ctxx.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.custom_size_spinner_tab,
						parent, false);
			}
			_itemsize = (TextView) itemView.findViewById(R.id.textone);
			_itemsize.setTypeface(_font, Typeface.BOLD);
			SingleItemDataModel _sobj = _sizedata.get(position);
			itemView.setTag(_sobj.getProduct_item_pk());
			if (_sobj.get_availablestock().equalsIgnoreCase("0")) {
				_itemsize.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
						| Paint.ANTI_ALIAS_FLAG);
				System.out.println("iii--1--"
						+ _sobj.getProduct_item_property());

				_itemsize.setText(_sobj.getProduct_item_property());
			} else {
				System.out.println("iii--2--"
						+ _sobj.getProduct_item_property());
				_itemsize.setText(_sobj.getProduct_item_property());
			}

			return itemView;
		}

	}

	// =======================================================================================================================//
	@Override
	public void onItemSelected(AdapterView<?> _adap, View _v, int pos, long arg3) {

		switch (_sizes.getId()) {
		case R.id.display_size:
			_sendItemPK = pos;
			SingleItemDataModel _sdm = _product_items_arraylist.get(pos);
			if (_sdm.get_availablestock().equalsIgnoreCase("0")) {
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				_seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
				_seterrormsg.setText("Out of stock");
				toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			} else {
				_sendItemPK = pos;
			}

			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	// ************************************************************************************************************************//
	@Override
	public void onBackPressed() {
		Intent _gobck = new Intent(_ctx, ProductListing.class);
		startActivity(_gobck);
		finish();
		overridePendingTransition(R.anim.push_out_to_left,
				R.anim.push_out_to_right);
	}

	// =================================================== onActivity
	// ========================================================================//
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (type == 1) {
			mFacebook.authorizeCallback(requestCode, resultCode, data);
		} else if (type == 2) {
			System.out.println("twitter enter");

			if (resultCode == RESULT_OK) {
				AccessToken accessToken = null;
				try {
					String oauthVerifier = data.getExtras().getString(
							Const.IEXTRA_OAUTH_VERIFIER);
					accessToken = mTwitter.getOAuthAccessToken(mRequestToken,
							oauthVerifier);
					SharedPreferences pref = getSharedPreferences(
							Const.PREF_NAME, MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString(Const.PREF_KEY_ACCESS_TOKEN,
							accessToken.getToken());
					editor.putString(Const.PREF_KEY_ACCESS_TOKEN_SECRET,
							accessToken.getTokenSecret());
					editor.commit();
					if (mRequestToken != null) {
						tweetToWall();
					}
					Toast.makeText(this, "authorized", Toast.LENGTH_SHORT)
							.show();

				} catch (TwitterException e) {
					e.printStackTrace();
				}
			} else if (resultCode == RESULT_CANCELED) {
				Log.w("twitter auth", "Twitter auth canceled.");
			}
		}
	}

	// ========================================================================================================================//
	class AddToCartClass extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(SingleProductDisplay.this,
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
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		protected String doInBackground(String... arg0) {
			String _url = "https://www.brandsfever.com/api/v5/carts/";
			String _userid = _getuserId;
			String _token = _getToken;
			String _Action = "add_product";
			String _Quantity = set_quantity.getSelectedItem().toString();
			String _itemPk = _product_items_arraylist.get(_sendItemPK)
					.getProduct_item_pk();

			System.out.println("pankaj" + " " + _userid + " " + _token + " "
					+ _Action + " " + _Quantity + " " + _sendItemPK + _itemPk);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			BasicNameValuePair userid = new BasicNameValuePair("user_id",
					_userid);
			BasicNameValuePair token = new BasicNameValuePair("token", _token);
			BasicNameValuePair action = new BasicNameValuePair("action",
					_Action);
			BasicNameValuePair quantity = new BasicNameValuePair("quantity",
					_Quantity);
			BasicNameValuePair itempk = new BasicNameValuePair(
					"product_item_pk", _itemPk);
			_namevalueList.add(userid);
			_namevalueList.add(token);
			_namevalueList.add(action);
			_namevalueList.add(quantity);
			_namevalueList.add(itempk);
			_cartResponse = _AddProduct(_url, _namevalueList);
			Log.e("===RESPONSE====>", "===RESPONSE====>" + _cartResponse);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobj = new JSONObject(_cartResponse);
				String _Ret = jobj.getString("ret");
				if (_Ret.equals("0")) {
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.relativeLayout1));
					_seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText("Succesfully Added!");
					toast = new Toast(_ctx);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();

					SingleProductDisplay.this.finish();
					System.out.println("id and token is" + _getToken
							+ _getuserId);
					Intent _sendbck = new Intent(SingleProductDisplay.this,
							ProductListing.class);
					startActivity(_sendbck);
				} else {
					String _errormsg = jobj.getString("msg");
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.relativeLayout1));
					_seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText(_errormsg);
					toast = new Toast(_ctx);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressHUD.dismiss();
		}

	}

	// ======================================================================================================================//
	public String _AddProduct(String url, List<NameValuePair> _namevalueList) {
		String _Response = null;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpPost _httppost = new HttpPost(url);
		try {
			_httppost.setEntity(new UrlEncodedFormEntity(_namevalueList,
					HTTP.UTF_8));
			HttpResponse _httpresponse = _httpclient.execute(_httppost);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			Log.i("--------------Responsecode----------", "." + _responsecode);
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
				_Response = total.toString();
			} else {
				_Response = "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _Response;
	}

	// =======================================================================================================================//
	class BuyNowClass extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(SingleProductDisplay.this,
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
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		protected String doInBackground(String... params) {
			String _url = "https://www.brandsfever.com/api/v5/carts/";
			String _userid = _getuserId;
			String _token = _getToken;
			String _Action = "add_product";
			String _Quantity = set_quantity.getSelectedItem().toString();
			String _itemPk = _product_items_arraylist.get(_sendItemPK)
					.getProduct_item_pk();

			System.out.println("pankaj" + " " + _userid + " " + _token + " "
					+ _Action + " " + _Quantity + " " + _sendItemPK);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			BasicNameValuePair userid = new BasicNameValuePair("user_id",
					_userid);
			BasicNameValuePair token = new BasicNameValuePair("token", _token);
			BasicNameValuePair action = new BasicNameValuePair("action",
					_Action);
			BasicNameValuePair quantity = new BasicNameValuePair("quantity",
					_Quantity);
			BasicNameValuePair itempk = new BasicNameValuePair(
					"product_item_pk", _itemPk);
			_namevalueList.add(userid);
			_namevalueList.add(token);
			_namevalueList.add(action);
			_namevalueList.add(quantity);
			_namevalueList.add(itempk);
			_cartResponse = _AddProduct(_url, _namevalueList);
			Log.e("===RESPONSE====>", "===RESPONSE====>" + _cartResponse);
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobj = new JSONObject(_cartResponse);
				String _Ret = jobj.getString("ret");
				if (_Ret.equals("0")) {
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.relativeLayout1));
					_seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText("Successfully Added!");
					toast = new Toast(_ctx);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();
					Intent _bn = new Intent(_ctx, MyCartScreen.class);
					startActivity(_bn);
					finish();
				} else {// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					String _msg = jobj.getString("msg");
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.relativeLayout1));
					_seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText(_msg);
					toast = new Toast(_ctx);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressHUD.dismiss();
		}
	}

	// *************** Share facebook and twitter
	// **********************************************************************//

	protected void share_method() {
		// TODO Auto-generated method stub
		View choose = null;

		LayoutInflater li = LayoutInflater.from(getApplicationContext());

		choose = li.inflate(R.layout.choose_dailog_shaire, null);
		Button facebook_shaire = (Button) choose.findViewById(R.id.facebook_shaire);
		Button Twitter_shaire = (Button) choose.findViewById(R.id.Twitter_shaire);
		Button cancel = (Button) choose.findViewById(R.id.Cancel);
		final Dialog mDialog = new Dialog(SingleProductDisplay.this,
				android.R.style.Theme_Translucent_NoTitleBar);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(choose);

		mDialog.show();

		facebook_shaire.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDialog.dismiss();
				mFacebook = new Facebook(getResources()
						.getString(R.string.fbid));
				SessionStore.restore(mFacebook, getApplicationContext());

				type = 1;
				if (!mFacebook.isSessionValid()) {
					mFacebook.authorize(SingleProductDisplay.this,
							new String[] { "publish_stream", "email",
									"user_groups", "read_stream",
									"user_about_me", "offline_access" }, 1,
							new LoginDialogListener());

				} else {
					postToWall();
				}

			}
		});

		Twitter_shaire.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDialog.dismiss();
				type = 2;
				Toast.makeText(getApplicationContext(),
						"Redirecting you on twitter", 2).show();

				ConfigurationBuilder confbuilder = new ConfigurationBuilder();
				Configuration conf = confbuilder
						.setOAuthConsumerKey(Const.CONSUMER_KEY)
						.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();
				mTwitter = new TwitterFactory(conf).getInstance();
				mTwitter.setOAuthAccessToken(null);
				try {
					mRequestToken = mTwitter
							.getOAuthRequestToken(Const.CALLBACK_URL);

					Intent intent = new Intent(getApplicationContext(),
							TwitterLogin.class);
					intent.putExtra(Const.IEXTRA_AUTH_URL,
							mRequestToken.getAuthorizationURL());
					startActivityForResult(intent, 0);

				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});

	}

	// ***************************** facebook //
	// ***********************************//
	public void postToWall() {
		// post on user's wall.
		View post = null;

		LayoutInflater li = LayoutInflater.from(getApplicationContext());

		post = li.inflate(R.layout.sharedialog_share, null);
		final EditText desc;

		desc = (EditText) post.findViewById(R.id.edittextdesc);
		desc.requestFocus();

		ImageView back = (ImageView) post.findViewById(R.id.imageView1);
		ImageView post1 = (ImageView) post.findViewById(R.id.imageView2);
		ImageView share_image = (ImageView) post.findViewById(R.id.imageView3);

		final Dialog mDialog = new Dialog(SingleProductDisplay.this,
				android.R.style.Theme_Translucent_NoTitleBar);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(post);

		mDialog.show();
		if (facebook_bit_map == null) {
			facebook_bit_map = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher);
			share_image.setImageBitmap(facebook_bit_map);
		} else {
			share_image.setImageBitmap(facebook_bit_map);
		}

		desc.setText("Check out this product," + name
				+ ", via BrandsFever App http://www.brandsfever.com");

		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});

		post1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(
						mFacebook);
				Bundle params = new Bundle();
				params.putString("message", desc.getText().toString());

				params.putString("name", desc.getText().toString());
				params.putString("link", "https://www.brandsfever.com");
				params.putString("description", "Svv");
				/*
				 * Resources res = getResources(); Drawable drawable =
				 * res.getDrawable(R.drawable.fbicon);
				 */
				Bitmap bitmap;
				if (facebook_bit_map == null) {
					bitmap = BitmapFactory.decodeResource(getResources(),
							R.drawable.ic_launcher);
				} else {
					bitmap = facebook_bit_map;
				}
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] bitMapData = stream.toByteArray();
				params.putByteArray("picture", bitMapData);
				mAsyncFbRunner.request("me/photos", params, "POST",
						new WallPostListener());
				mDialog.dismiss();
			}
		});

	}

	public final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			try {
				SessionStore.save(mFacebook, SingleProductDisplay.this);

				postToWall();

			} catch (Exception error) {
				error.printStackTrace();

			}

		}

		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}

		public void onError(DialogError e) {
			// TODO Auto-generated method stub

		}

		public void onCancel() {
			// TODO Auto-generated method stub

		}

	}

	private final class WallPostListener extends BaseRequestListener {
		private Handler mRunOnUi = new Handler();

		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {
				public void run() {

					/*
					 * Toast.makeText(MainActivity.this, "Posted to Facebook",
					 * Toast.LENGTH_SHORT).show();
					 */
				}
			});
		}

		public void onIOException(IOException e) {
			// TODO Auto-generated method stub

		}

		public void onFileNotFoundException(FileNotFoundException e) {
			// TODO Auto-generated method stub

		}

		public void onMalformedURLException(MalformedURLException e) {
			// TODO Auto-generated method stub

		}

		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}
	}

	// ***************************twitter**********************************///

	public void tweetToWall() {
		// post on user's wall.
		View post = null;

		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		System.out.println("twitter share-main--");
		post = li.inflate(R.layout.twitterialogsettings, null);
		final EditText t1;
		// ImageView i=(ImageView) post.findViewById(R.id.imageView3);
		t1 = (EditText) post.findViewById(R.id.textView0);

		// t1.setText("Messege:Deemz");
		final ImageView back = (ImageView) post.findViewById(R.id.imageView1);
		final ImageView post1 = (ImageView) post.findViewById(R.id.imageView2);
		final ImageView post_image = (ImageView) post
				.findViewById(R.id.imageView_posts);
		final Dialog mDialog = new Dialog(SingleProductDisplay.this,
				android.R.style.Theme_Translucent_NoTitleBar);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(post);

		mDialog.show();

		t1.setText("Check out this product," + name
				+ ", via BrandsFever App http://www.brandsfever.com");
		t1.setTextSize(12);

		if (facebook_bit_map == null) {
			facebook_bit_map = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_launcher);
			Bitmap bm_dup = Bitmap.createScaledBitmap(facebook_bit_map, 140,
					140, false);
			post_image.setImageBitmap(bm_dup);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			facebook_bit_map.compress(CompressFormat.PNG, 0, bos);
			bitmapdata = bos.toByteArray();
		} else {
			Bitmap bm_dup = Bitmap.createScaledBitmap(facebook_bit_map, 140,
					140, false);
			post_image.setImageBitmap(bm_dup);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			facebook_bit_map.compress(CompressFormat.PNG, 0, bos);
			bitmapdata = bos.toByteArray();
		}

		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});

		post1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				twitedata = t1.getText().toString();
				new updateTwitterStatus().execute("helloooo");

			}
		});

	}

	class updateTwitterStatus extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SingleProductDisplay.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();

				Configuration conf = builder
						.setOAuthConsumerKey(Const.CONSUMER_KEY)
						.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();

				StatusUpdate ad = new StatusUpdate(twitedata);
				InputStream is = new ByteArrayInputStream(bitmapdata);
				ad.setMedia("BrandsFever", is);
				twitter4j.Status responses = mTwitter.updateStatus(ad);
				// Log.d("Status", "> " + response.getText());
			} catch (TwitterException e) {

				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();

				}
			});

		}

	}

}
