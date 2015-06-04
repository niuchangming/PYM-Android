package com.brandsfever;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.adapter.MPagerAdapter;
import com.androidquery.AQuery;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.viewpagerindicator.CirclePageIndicator;

public class ProfileImage extends FragmentActivity implements OnClickListener {
	String _seturl;
	ImageView brands_profile;
	LinearLayout footer;
	ViewPager _mViewPager;
	MPagerAdapter _adapter;
    Button skip;
	Timer timer;
    Integer currentPage= 0;
	Integer NUM_PAGES = 2;
	Context _ctx;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_profile_image);
		
		Intent i = getIntent();
		_seturl = i.getStringExtra("_ImageUrl");
		
		footer  = (LinearLayout)findViewById(R.id.footer);
		footer.setVisibility(View.INVISIBLE);
		_mViewPager = (ViewPager) findViewById(R.id.ProfileViewPager);
		skip = (Button) findViewById(R.id.skip);
		skip.setOnClickListener(this);
		
		brands_profile = (ImageView)findViewById(R.id.brands_profile);
		AQuery aq = new AQuery(_ctx);
		aq.id(brands_profile).image(_seturl);
		footer.setVisibility(View.VISIBLE);
		
		try {
			footer.setVisibility(View.VISIBLE);
			_adapter = new MPagerAdapter(getApplicationContext(),getSupportFragmentManager());
		     _mViewPager.setAdapter(_adapter);	 
		       Timer swipeTimer = new Timer();
		        swipeTimer.schedule(new TimerTask() {
		            @Override
		            public void run() {
		                runOnUiThread(new Runnable() {
		                    @Override
		                    public void run() {
		                        if (currentPage == NUM_PAGES) {
		                            currentPage = 0;
		                        }
		                        _mViewPager.setCurrentItem(currentPage++);
		                    }
		                });
		            }
		        }, 0, 1500);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final CirclePageIndicator circleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        circleIndicator.setViewPager(_mViewPager);

		
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": profileimage/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	@Override
	public void onClick(View v) {
	switch (v.getId()) {
	case R.id.skip:
		Intent _i = new Intent(ProfileImage.this,ProductDisplay.class);
		startActivity(_i);
		finish();
		break;

	default:
		break;
	}
		
	}
}
