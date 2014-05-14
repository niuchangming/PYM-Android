package com.brandsfever;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adapter.PhoneLoginPager;
import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

public class PhoneLoginScreen extends FragmentActivity implements
		OnClickListener {
	private ViewPager _mViewPager;
	private PhoneLoginPager _adapter;
	ImageButton close_login_page;
	RelativeLayout trans;
	TextView textView1,textView2;
	Typeface _font;
	EditText dummy;
	InputMethodManager imm;
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			setContentView(R.layout.activity_phone_login_screen);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceInch() < 9) {
			setContentView(R.layout.login_screen_seven_inch_tablet);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
			setTheme(android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			setContentView(R.layout.login_screen_ten_inch_tablet);
		}
		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		close_login_page = (ImageButton) findViewById(R.id.close_login_page);
		close_login_page.setOnClickListener(this);
		
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			
		
		textView1 = (TextView)findViewById(R.id.textView1);
		dummy=(EditText)findViewById(R.id.editText1);
		textView1.setOnClickListener(this);
		textView1.setTypeface(_font);
		int color = Integer.parseInt("8e1345", 16)+0xFF000000;
		textView1.setTextColor(color);
		textView2 = (TextView)findViewById(R.id.textView2);
		textView2.setOnClickListener(this);
		textView2.setTypeface(_font);
		int colors = Integer.parseInt("000000", 16)+0xFF000000;
		textView2.setTextColor(colors);

		setUpView();
		setTab();
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": login/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	public void refreshPage() {
		Intent returnIntent = new Intent();
		setResult(RESULT_OK, returnIntent);
		PhoneLoginScreen.this.finish();
	}

	private void setUpView() {
		_mViewPager = (ViewPager) findViewById(R.id.viewPager);
		_adapter = new PhoneLoginPager(getApplicationContext(),
				getSupportFragmentManager());
		_mViewPager.setAdapter(_adapter);
		_mViewPager.setCurrentItem(0);
	}

	private void setTab() {
		_mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int position) {
				switch (position) {
				case 0:
					findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
					int color = Integer.parseInt("8e1345", 16)+0xFF000000;
					textView1.setTextColor(color);
					
					int colors = Integer.parseInt("000000", 16)+0xFF000000;
					textView2.setTextColor(colors);
					break;

				case 1:
					findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
					int colord = Integer.parseInt("8e1345", 16)+0xFF000000;
					textView2.setTextColor(colord);
					
					int colorsd = Integer.parseInt("000000", 16)+0xFF000000;
					textView1.setTextColor(colorsd);
					break;
				}
			}

		});

	}

	public void _ChangeTabs() {
       findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
		findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
		_mViewPager.setCurrentItem(1);
		int color = Integer.parseInt("8e1345", 16)+0xFF000000;
		textView2.setTextColor(color);
		
		int colors = Integer.parseInt("000000", 16)+0xFF000000;
		textView1.setTextColor(colors);
	}
	
	
	//=======================================Hide the softkeypad in fragments================================================//
	
		public static void closeKeyboard(Context c, IBinder windowToken) {
		    InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
		    mgr.hideSoftInputFromWindow(windowToken, 0);
		}

	// =======================================================================================================================//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close_login_page:
			PhoneLoginScreen.this.finish();
			//overridePendingTransition(0, R.anim.puch_login_down);
			overridePendingTransition(R.anim.puch_out_to_top,R.anim.push_out_to_bottom);
			break;
			
		case R.id.textView1:
			imm.hideSoftInputFromWindow(dummy.getWindowToken(), 0);
			_mViewPager.setCurrentItem(0);
			int color = Integer.parseInt("8e1345", 16)+0xFF000000;
			textView1.setTextColor(color);
			
			int colors = Integer.parseInt("000000", 16)+0xFF000000;
			textView2.setTextColor(colors);
			break;
			
		case R.id.textView2:
			imm.hideSoftInputFromWindow(dummy.getWindowToken(), 0);
			_mViewPager.setCurrentItem(1);
			int color1 = Integer.parseInt("8e1345", 16)+0xFF000000;
			textView2.setTextColor(color1);
			
			int colors1 = Integer.parseInt("000000", 16)+0xFF000000;
			textView1.setTextColor(colors1);
			break;

		default:
			break;
		}

	}
}
