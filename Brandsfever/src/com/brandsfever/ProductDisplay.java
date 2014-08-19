package com.brandsfever;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.crashlytics.android.Crashlytics;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class ProductDisplay extends SlidingFragmentActivity {

	private static final String TAG = "ProductDisplay";

	private String mCurrentMenu;
	private Fragment mContent;
	private int mBackButtonCount;
	private MenuFragment mMenu;
	private List<String> mCategories;
	public CampaignFragment mCampaignFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Crashlytics.start(this);

		setContentView(R.layout.content_frame);

		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

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
					toggle();
					if (getSlidingMenu().isMenuShowing()) {
						if (mMenu != null) {
							mMenu.resetMenu();
						}
					}
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

		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		if (mContent == null) {
			mCurrentMenu = "All";
			mCampaignFragment = CampaignFragment.newInstance();
			mContent = mCampaignFragment;
		}

		if (mMenu == null) {
			mMenu = new MenuFragment();
		}

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, mMenu).commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);
		sm.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				if (mMenu != null)
					mMenu.resetMenu();
			}
		});
		String[] categories = getResources().getStringArray(R.array.category);
		mCategories = Arrays.asList(categories);
	}

	public void switchContent(final Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}

	@Override
	public void onStart() {
		super.onStart();
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": campaigns/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				startActivity(getIntent());
				if (resultCode == RESULT_CANCELED) {
					// Do nothing?
				}
			}
		}
	}

	@Override
	public void onBackPressed() {

		if (mBackButtonCount >= 1) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			Toast.makeText(this, "Press the back button once again to exit.",
					Toast.LENGTH_SHORT).show();
			mBackButtonCount++;
		}
	}

	private void directToCart() {

		SharedPreferences mypref = getApplicationContext()
				.getSharedPreferences("mypref", 0);
		String username = mypref.getString("UserName", null);

		if (username != null) { // check login status
			Intent gotocart = new Intent(ProductDisplay.this,
					MyCartActivity.class);
			startActivity(gotocart);
		} else {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.error_popop,
					(ViewGroup) findViewById(R.id.relativeLayout1));
			final TextView msgTextView = (TextView) view
					.findViewById(R.id._seterrormsg);
			msgTextView.setText("Please login!");
			Toast toast = new Toast(ProductDisplay.this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.show();
		}
	}

	public String getCurrentMenu() {
		return mCurrentMenu;
	}

	public void setCurrentMenu(String currentMenu) {
		mCurrentMenu = currentMenu;

		if (mContent instanceof CampaignFragment) {
			((CampaignFragment) mContent).mViewPager.setCurrentItem(
					mCategories.indexOf(mCurrentMenu), true);
		}
	}

	public void resetCurrentMenu() {
		
		if(mContent instanceof CampaignFragment){
			int index = ((CampaignFragment)mContent).mViewPager.getCurrentItem();
			if (index < mMenu.mCategories.size()){
				mCurrentMenu = mMenu.mCategories.get(index);
				Log.e(TAG,"CurrentMenu: "+mCurrentMenu);
			}
		}
	}

}
