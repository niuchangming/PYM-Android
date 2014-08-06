package com.brandsfever;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class AboutApp extends SherlockFragmentActivity {
	TextView aboutapp, set_about_app;
	Typeface _font;
	int color, colors;
	String _aboutData;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.about_app_phone);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_about_app);
		} else if (a >= 9) {
			setContentView(R.layout.activity_about_app);
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
			}
		});

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		aboutapp = (TextView) findViewById(R.id.about_tag);
		aboutapp.setTypeface(_font);

		set_about_app = (TextView) findViewById(R.id.set_about_app);
		set_about_app.setMovementMethod(new ScrollingMovementMethod());
		set_about_app.setTypeface(_font);
		new GetAboutAppFromServer().execute();
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": /about/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	private class GetAboutAppFromServer extends
			AsyncTask<String, String, String> implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(AboutApp.this, "Loading", true,
					true, this);
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
			// TODO Auto-generated method stub
			String _url_AboutApp = "https://www.brandsfever.com/api/v5/about/";
			GetContent(_url_AboutApp);
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			set_about_app.setText(_aboutData);
			mProgressHUD.dismiss();

		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}
	}

	public void GetContent(String url_get_launcher_image) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(url_get_launcher_image);
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
				String _content = total.toString();
				try {
					JSONObject obj = new JSONObject(_content);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						_aboutData = obj.getString("about");

					} else {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
