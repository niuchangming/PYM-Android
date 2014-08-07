package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.dataholder.DataHolderClass;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class Splash extends Activity {
	String _url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);

		try {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int displayHeight = displaymetrics.heightPixels;
			int displayWidth = displaymetrics.widthPixels;
			double x = Math.pow(displaymetrics.widthPixels
					/ displaymetrics.xdpi, 2);
			double y = Math.pow(displaymetrics.heightPixels
					/ displaymetrics.ydpi, 2);
			double screenInches = Math.sqrt(x + y);
			int _deviceinch = (int) (screenInches);
			DataHolderClass.getInstance().set_deviceHeight(displayHeight);
			DataHolderClass.getInstance().set_deviceWidth(displayWidth);
			DataHolderClass.getInstance().set_deviceInch(_deviceinch);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new GetImageFromServer().execute();
	}

	private class GetImageFromServer extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(Splash.this, "", true, true, this);
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
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = "https://www.brandsfever.com/api/v5/launching-page/";
			GetImage(url);
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			mProgressHUD.dismiss();
		}
	}

	// ================================================================================================================//
	public void GetImage(String url_get_launcher_image) {
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
				String _urls = total.toString();
				try {
					JSONObject obj = new JSONObject(_urls);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						_url = obj.getString("launching_image");
						Intent i = new Intent(Splash.this,
								ProfileImage.class);
						i.putExtra("_ImageUrl", _url);
						startActivity(i);
						Splash.this.finish();
					} else {
						// server response error
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// hit error
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
