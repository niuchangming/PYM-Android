package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class PayWithPaypal_Screen extends SherlockFragmentActivity {
	private static final String TAG = "PayWithPaypal_Screen";
	Context _ctx = PayWithPaypal_Screen.this;
	WebView mPaypalWebView;
	String mPaypalUrl;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	Typeface _font;
	int color, colors;
	boolean isPaid;
	JSONObject mOrderDetail;
	private Handler paymentHandler;
	private Runnable paymentRun;
	private static final int HANDLER_DELAY = 1000 * 6;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
				.inflate(R.layout.action_bar, null);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		final ImageButton actionBarMenu = (ImageButton) findViewById(R.id.action_bar_left);
		actionBarMenu.setImageDrawable(getResources().getDrawable(
				R.drawable.menu_bg));
		actionBarMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				paymentHandler.removeCallbacks(paymentRun);
				finish();
			}
		});
		final ImageButton actionBarCart = (ImageButton) findViewById(R.id.action_bar_right);
		actionBarCart.setImageDrawable(getResources().getDrawable(
				R.drawable.cart_btn_bg));
		actionBarCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Cart is clicked");
			}
		});

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;

		mPaypalUrl = "https://www.brandsfever.com/api/v5/payments/paypal/?token="
				+ _getToken
				+ "&user_id="
				+ _getuserId
				+ "&order_id="
				+ DataHolderClass.getInstance().get_orderpk();

		new SendForPayment().execute();

		String detail = getIntent().getExtras().getString("OrderDetailKey");
		try {
			mOrderDetail = new JSONObject(detail);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		paymentHandler = new Handler();
		paymentRun = new Runnable() {

			@Override
			public void run() {
				String order_pk = DataHolderClass.getInstance().get_orderpk();
				String stateUrl = "https://www.brandsfever.com/api/v5/orders/"
						+ order_pk + "/states/?token=" + _getToken
						+ "&user_id=" + _getuserId;
				int state = fetchOrderState(stateUrl);
				// state: -1 -- unknown, 0-- waiting for payment 1:success
				if (state == 1) {
					onPurchaseCompleted();
					paymentHandler.removeCallbacks(this);
				} else {
					paymentHandler.postDelayed(this, HANDLER_DELAY);
				}

			}

		};
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": payments/paypal/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	private int fetchOrderState(String url) {
		int paymentState = -1;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpclient = HttpsClient.getNewHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			int responsecode = httpresponse.getStatusLine().getStatusCode();

			if (responsecode == 200) {
				InputStream inputstream = httpresponse.getEntity().getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				String content = total.toString();
				try {
					JSONObject jobj = new JSONObject(content);
					String ret = jobj.getString("ret");
					if (ret.equals("0")) {
						String statenumber = jobj.getString("state");
						paymentState = Integer.parseInt(statenumber);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception _e) {
			_e.printStackTrace();
		}

		return paymentState;
	}

	private void onPurchaseCompleted() {

		if (mOrderDetail != null) {

			EasyTracker tracker = EasyTracker.getInstance(this);

			try {

				tracker.send(MapBuilder.createTransaction(
						mOrderDetail.getString("identifier"), "Brandsfever",
						mOrderDetail.getDouble("total_price_with_discounts"),
						mOrderDetail.getDouble("tax_price"),
						mOrderDetail.getDouble("shipping_fee"), "SGD").build());

				JSONArray itemArray = mOrderDetail.getJSONArray("item_list");
				for (int i = 0; i < itemArray.length(); i++) {
					JSONObject item = itemArray.getJSONObject(i);
					tracker.send(MapBuilder.createItem(
							mOrderDetail.getString("identifier"),
							item.getString("name"), item.getString("pk"),
							item.getString("campaign"),
							item.getDouble("unit_price"),
							item.getLong("quantity"), "SGD").build());
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	class SendForPayment extends AsyncTask<Void, Void, Boolean> implements
			OnCancelListener {
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
			webSettings.setTextZoom((int) (webSettings.getTextZoom() * 0.5));
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
					paymentHandler.postDelayed(paymentRun, HANDLER_DELAY);
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
		paymentHandler.removeCallbacks(paymentRun);
		finish();
	}

}