package com.brandsfever;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;

public class ZoomShow extends Activity implements OnClickListener {
	ImageView imageView1;
	ImageView crossbutton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zoomshow);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		crossbutton = (ImageView) findViewById(R.id.crossbutton);
		crossbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		try {
			AQuery aq = new AQuery(getApplicationContext());
			aq.id(imageView1).image(SingleProductDisplay._zoomimage);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": zoomShow/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}

	}
	
}
