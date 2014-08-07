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
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
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
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;
import com.viewpagerindicator.CirclePageIndicator;

public class SingleProductDisplay extends SherlockFragmentActivity implements
		OnClickListener, OnItemSelectedListener {

	private static final String TAG = "SingleProductDisplay";

	Context _ctx = SingleProductDisplay.this;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.activity_single_product_display);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.single_product_display_seven_inch);
		} else if (a >= 9) {
			setContentView(R.layout.single_product_display_tab);
		}

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

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID",null);
		_getToken = _mypref.getString("TOKEN", null);

		scrollbutton = (Button) findViewById(R.id.scrollbutton);
		scrollbutton.setOnClickListener(this);

		_scroll = (ScrollView) findViewById(R.id.scroll);
		_scroll.setSmoothScrollingEnabled(true);

		pk = DataHolderClass.getInstance().get_subProductsPk();
		new SingleItemDetails().execute();
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": products/" + pk + "/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

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

				_pname.setText(name);
				_pname.setTypeface(_font, Typeface.BOLD);
				try {
					set_sales_price.setText(offer_price.replace("GD", "$"));
					set_sales_price.setTypeface(_font, Typeface.BOLD);
					set_market_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
							| Paint.ANTI_ALIAS_FLAG);
					set_market_price.setText(market_price.replace("GD", "$"));
					set_market_price.setTypeface(_font, Typeface.BOLD);
				} catch (Exception e) {
					e.printStackTrace();
				}

				long timeInMilliseconds = Long.valueOf(ends_at);
				long end = timeInMilliseconds * 1000;
				long current = System.currentTimeMillis();
				long diff = end - current;
				int dayCount = (int) diff / (24 * 60 * 60 * 1000);
				int hours_left = (int) ((diff / (1000 * 60 * 60)) % 24);
				int minutes_left = (int) ((diff / (1000 * 60)) % 60);
				int seconds_left = (int) ((diff / 1000) % 60);
				String _s = "Sale ends in" + " " + Integer.toString(dayCount)
						+ " Days" + " " + Integer.toString(hours_left) + ":"
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
				String _s1 = DataHolderClass.getInstance().getShipping_fee();
				String _s2 = DataHolderClass.getInstance().getShipping_period();
				String _s3 = DataHolderClass.getInstance().getFree_shipping();
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

				for (int _k = 0; _k < _sizechartvalue.size(); _k++) {
					SingleItemDataModel _obj = _sizechartvalue.get(_k);
					if (_obj.get_propertylistName().equalsIgnoreCase(
							"Size Chart")) {
						_acharturl = _obj.get_propertylistValue();
						open_size_chart.setEnabled(true);
						open_size_chart.setVisibility(View.VISIBLE);
					}
				}

				int firstAvailablePosition = -1;
				boolean isSizeAvailable = false;

				for (int i = 0; i < _product_items_arraylist.size(); i++) {
					SingleItemDataModel model = _product_items_arraylist.get(i);
					if (!model.getProduct_item_property().equalsIgnoreCase("")) {
						if (!isSizeAvailable) {
							isSizeAvailable = true;
						}
					}

					if (!model.get_availablestock().equalsIgnoreCase("0")) {
						if (firstAvailablePosition == -1)
							firstAvailablePosition = i;
					}
				}

				if (isSizeAvailable) {
					_sizes.setVisibility(View.VISIBLE);
					_adapter = new CustomSpinnerSizeAdapter(
							SingleProductDisplay.this, _product_items_arraylist);
					_sizes.setAdapter(_adapter);
					if (firstAvailablePosition != -1)
						_sizes.setSelection(firstAvailablePosition);
				}

			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {

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
				String _s = "Sale ends in" + " " + Integer.toString(dayCount)
						+ " Days" + " " + Integer.toString(hours_left) + ":"
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
				String _s1 = DataHolderClass.getInstance().getShipping_fee();
				String _s2 = DataHolderClass.getInstance().getShipping_period();
				String _s3 = DataHolderClass.getInstance().getFree_shipping();
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
				for (int _k = 0; _k < _sizechartvalue.size(); _k++) {
					SingleItemDataModel _obj = _sizechartvalue.get(_k);
					if (_obj.get_propertylistName().equalsIgnoreCase(
							"Size Chart")) {
						_acharturl = _obj.get_propertylistValue();
						open_size_chart.setBackgroundResource(R.drawable.size);
						open_size_chart.setEnabled(true);
					}
				}

				// playing with size spinner
				int firstAvailablePosition = -1;
				boolean isSizeAvailable = false;

				for (int i = 0; i < _product_items_arraylist.size(); i++) {
					SingleItemDataModel model = _product_items_arraylist.get(i);
					if (!model.getProduct_item_property().equalsIgnoreCase("")) {
						if (!isSizeAvailable) {
							isSizeAvailable = true;
						}
					}

					if (!model.get_availablestock().equalsIgnoreCase("0")) {
						if (firstAvailablePosition == -1)
							firstAvailablePosition = i;
					}
				}

				if (isSizeAvailable) {
					_sizes.setVisibility(View.VISIBLE);
					_adapter = new CustomSpinnerSizeAdapter(
							SingleProductDisplay.this, _product_items_arraylist);
					_sizes.setAdapter(_adapter);
					if (firstAvailablePosition != -1)
						_sizes.setSelection(firstAvailablePosition);
				}

			}
			mProgressHUD.dismiss();
		}
	}

	public void GetItemDetails(String _url) {
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
				String _data = total.toString();
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

							String _availablestock = jsonobj
									.getString("available stock");
							String _product_item_pk = jsonobj
									.getString("product_item_pk");
							String product_item_property = jsonobj
									.getString("product_item_property");

							_singleobj.setProduct_item_name(name);
							_singleobj.set_availablestock(_availablestock);
							_singleobj.setProduct_item_pk(_product_item_pk);
							_singleobj
									.setProduct_item_property(product_item_property);

							_product_items_arraylist.add(_singleobj);

						}

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

					} 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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

			_zoomimage = "https:" + img_map.get(position).get(0);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public boolean isViewFromObject(final View view, final Object object) {
			return view == ((ImageView) object);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share:
			if (click_button % 2 == 0) {
				popupWindow_status = 1;
				share_method();
			} else {
			}
			click_button++;

			break;

		case R.id.click_to_zoom:
			Intent _zoom = new Intent(getApplicationContext(), ZoomShow.class);
			startActivity(_zoom);
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
			f.show(getSupportFragmentManager(), "");
			break;

		case R.id.add_to_cart:
			if (click_button % 2 != 0) {
			}
			try {
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
			finish();
			break;

		default:
			break;
		}

	}

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
			return _sizedata.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int arg0) {
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
				_itemsize.setText(_sobj.getProduct_item_property());
			} else {
				_itemsize.setText(_sobj.getProduct_item_property());
			}

			return itemView;
		}
	}

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

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
		}

		return super.onOptionsItemSelected(item);
	}


	private void directToCart() {

		SharedPreferences mypref = getApplicationContext()
				.getSharedPreferences("mypref", 0);
		String username = mypref.getString("UserName", null);
		
		if (username != null) { // check login status
			Intent gotocart = new Intent(SingleProductDisplay.this, MyCartActivity.class);
			startActivity(gotocart);
		} else {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.error_popop,
					(ViewGroup) findViewById(R.id.relativeLayout1));
			final TextView msgTextView = (TextView) view
					.findViewById(R.id._seterrormsg);
			msgTextView.setText("Please login!");
			Toast toast = new Toast(SingleProductDisplay.this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.show();
		}
	}
	@Override
	public void onBackPressed() {
		finish();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (type == 1) {
			mFacebook.authorizeCallback(requestCode, resultCode, data);
		} else if (type == 2) {

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
					Intent _bn = new Intent(_ctx, MyCartFragment.class);
					startActivity(_bn);
					finish();
				} else {
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

	// Share facebook and twitter

	protected void share_method() {
		View choose = null;

		LayoutInflater li = LayoutInflater.from(getApplicationContext());

		choose = li.inflate(R.layout.choose_dailog_shaire, null);
		Button facebook_shaire = (Button) choose
				.findViewById(R.id.facebook_shaire);
		Button Twitter_shaire = (Button) choose
				.findViewById(R.id.Twitter_shaire);
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
				mDialog.dismiss();
			}
		});

	}

	// facebook //
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
				mDialog.dismiss();
			}
		});

		post1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

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

		}

		public void onError(DialogError e) {

		}

		public void onCancel() {

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

		}

		public void onFileNotFoundException(FileNotFoundException e) {

		}

		public void onMalformedURLException(MalformedURLException e) {

		}

		public void onFacebookError(FacebookError e) {

		}
	}

	// twitter

	public void tweetToWall() {
		// post on user's wall.
		View post = null;

		LayoutInflater li = LayoutInflater.from(getApplicationContext());
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
				mDialog.dismiss();
			}
		});

		post1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
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
