package com.brandsfever;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.Crashlytics;
import com.dataholder.DataHolderClass;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGTH = 800;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);
		
		setContentView(R.layout.activity_splash);

		try {
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int heightPixels = displaymetrics.heightPixels;
			int widthPixels = displaymetrics.widthPixels;
		
			float widthDpi = displaymetrics.xdpi;
			float heightDpi = displaymetrics.ydpi;
			
			float widthInches = widthPixels/widthDpi;
			float heightInches = heightPixels/heightDpi;
			
			double diagonalInches = Math.sqrt((widthInches*widthInches) + (heightInches*heightInches));
			int _deviceinch = (int)Math.round(diagonalInches);
			DataHolderClass.getInstance().set_deviceHeight(heightPixels);
			DataHolderClass.getInstance().set_deviceWidth(widthPixels);
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
