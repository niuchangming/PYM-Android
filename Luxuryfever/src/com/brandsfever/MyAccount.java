package com.brandsfever;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.brandsfever.luxury.R;
import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;

public class MyAccount extends FragmentActivity implements OnClickListener {
	Button faq, editprofile, changepassword, seeorderhistory, invitedpeople,
			seestorecredits;
	TextView myaccount_tag;
	Context _ctx = MyAccount.this;;
	Typeface _font;
	ImageButton main_menu, back_btn, cart_btn;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women, _login,
			_settings, _mycart, mSupport, _invite, _logout;

	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	int color, colors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_my_account);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		myaccount_tag = (TextView) findViewById(R.id.myaccount_tag);
		myaccount_tag.setTypeface(_font, Typeface.BOLD);

		faq = (Button) findViewById(R.id.faq);
		faq.setTypeface(_font);
		faq.setOnClickListener(this);

		editprofile = (Button) findViewById(R.id.editprofile);
		editprofile.setTypeface(_font);
		editprofile.setOnClickListener(this);

		changepassword = (Button) findViewById(R.id.changedpassword);
		changepassword.setTypeface(_font);
		changepassword.setOnClickListener(this);

		seeorderhistory = (Button) findViewById(R.id.orderhistory);
		seeorderhistory.setTypeface(_font);
		seeorderhistory.setOnClickListener(this);

		invitedpeople = (Button) findViewById(R.id.seeinvitedpeople);
		invitedpeople.setTypeface(_font);
		invitedpeople.setOnClickListener(this);

		seestorecredits = (Button) findViewById(R.id.seestorecredits);
		seestorecredits.setTypeface(_font);
		seestorecredits.setOnClickListener(this);

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));

		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String username = _mypref.getString("_UserName", null);
        set_user_name.setTypeface(_font);
		if (username != null) {
			set_user_name.setText("Hi! "+username);
		} else {
			set_user_name.setText("Hi! Guest");
		}

		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		back_btn = (ImageButton) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(this);

		cart_btn = (ImageButton) findViewById(R.id.cart_btn);
		cart_btn.setOnClickListener(this);

		_all = (Button) findViewById(R.id.btn_all_cat);
		_all.setTypeface(_font);
		_all.setOnClickListener(this);

		_men = (Button) findViewById(R.id.cat_shoes);
		_men.setTypeface(_font);
		_men.setOnClickListener(this);

		_women = (Button) findViewById(R.id.cat_handbags);
		_women.setTypeface(_font);
		_women.setOnClickListener(this);

		_login = (Button) findViewById(R.id.btn_login);
		_login.setVisibility(View.GONE);

		_settings = (Button) findViewById(R.id.btn_setting);
		_settings.setTypeface(_font);
		_settings.setOnClickListener(this);

		_mycart = (Button) findViewById(R.id.my_cart);
		_mycart.setTypeface(_font);
		_mycart.setOnClickListener(this);

		mSupport = (Button) findViewById(R.id.btn_support);
		mSupport.setTypeface(_font);
		mSupport.setOnClickListener(this);
		
		_invite = (Button) findViewById(R.id.btn_invite);
		_invite.setTypeface(_font);
		_invite.setOnClickListener(this);

		_logout = (Button) findViewById(R.id.btn_logout);
		_logout.setTypeface(_font);
		_logout.setOnClickListener(this);

		_all.setTextColor(colors);
		_men.setTextColor(colors);
		_women.setTextColor(colors);
		_settings.setTextColor(color);
		_mycart.setTextColor(colors);
		mSupport.setTextColor(colors);
		_invite.setTextColor(colors);

	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": MyAccount/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.faq:
			Intent _faq = new Intent(MyAccount.this, Faq.class);
			startActivity(_faq);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.editprofile:
			Intent _editp = new Intent(MyAccount.this, Edit_Profile.class);
			startActivity(_editp);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.changedpassword:
			Intent _cp = new Intent(MyAccount.this, ChangePassword.class);
			startActivity(_cp);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.orderhistory:
			Intent _oh = new Intent(MyAccount.this, MyOrderHistory.class);
			startActivity(_oh);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.seeinvitedpeople:
			Intent _Si = new Intent(MyAccount.this, People_I_Invited.class);
			startActivity(_Si);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.seestorecredits:
			Intent _sc = new Intent(MyAccount.this,
					StoreCredis_SettingPage.class);
			startActivity(_sc);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.main_menu:
			slide_me.toggleLeftDrawer();
			break;
		case R.id.btn_all_cat:
			slide_me.closeRightSide();
			Intent all = new Intent(_ctx, ProductDisplay.class);
			all.putExtra("tab", "all");
			startActivity(all);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_shoes:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent men = new Intent(_ctx, ProductDisplay.class);
				men.putExtra("tab", "men");
				startActivity(men);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_handbags:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent women = new Intent(_ctx, ProductDisplay.class);
				women.putExtra("tab", "women");
				startActivity(women);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.btn_setting:
			if (slide_me.isClosed()) {

				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
					Intent _phonesetting = new Intent(_ctx, SettingPhone.class);
					startActivity(_phonesetting);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
					finish();
				} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
						&& DataHolderClass.getInstance().get_deviceInch() < 9) {
					Intent _tabsetting = new Intent(_ctx, SettingTab.class);
					startActivity(_tabsetting);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
					finish();
				} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
					Intent _tabsetting = new Intent(_ctx, SettingTab.class);
					startActivity(_tabsetting);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
					finish();
				}
			}
			break;

		case R.id.my_cart:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				Intent _cart = new Intent(_ctx, MyCartScreen.class);
				startActivity(_cart);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.btn_support:
			if(slide_me.isClosed()){
				slide_me.setEnabled(false);
			}
			else {
				slide_me.setEnabled(true);
				Intent support = new Intent(_ctx,SupportActivity.class);
				startActivity(support);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;
			
		case R.id.btn_invite:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				Intent _invite = new Intent(_ctx, InviteSction_Screen.class);
				startActivity(_invite);
				slide_me.closeRightSide();
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.btn_logout:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				Editor editor = _mypref.edit();
				editor.clear();
				editor.commit();
				Intent _intent = new Intent(getApplicationContext(),
						ProductDisplay.class);
				startActivity(_intent);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			}
			break;

		case R.id.back_btn:
			finish();
			break;

		case R.id.cart_btn:
			if (!(_getToken == null) && !(_getuserId == null)) {
				Intent _gotocart = new Intent(_ctx, MyCartScreen.class);
				startActivity(_gotocart);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			} else {

			}
			break;

		default:
			break;
		}

	}

	// ============================================================================================================================//
	@Override
	public void onBackPressed() {
		finish();
	}
}