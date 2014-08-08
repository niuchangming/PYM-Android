package com.brandsfever;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.dataholder.DataHolderClass;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGTH = 800;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this,
						ProductDisplay.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGTH);
	}

}
