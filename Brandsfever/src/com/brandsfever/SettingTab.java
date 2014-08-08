package com.brandsfever;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;

public class SettingTab extends Activity implements OnClickListener {
	Context _ctx = SettingTab.this;
	Typeface _font;
	ImageButton about_app, t_c, p_p, faq, edit_profile, change_password,
			store_credits, order_history, already_invited;
	TextView myaccount_tag, setting_tag;
	ImageButton main_menu, cart_btn;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, _mycart, mSupport, _invite, _logout;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	ImageButton fb_share,twitter_share;
	Button call_customer_care;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a >=7 && a<9) {
			setContentView(R.layout.seven_inch_setting_screen);
		} else if (a>=9) {
			setContentView(R.layout.activity_setting_tab);
		}

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");

		myaccount_tag = (TextView) findViewById(R.id.myaccount_tag);
		setting_tag = (TextView) findViewById(R.id.setting_tag);
		myaccount_tag.setTypeface(_font);
		setting_tag.setTypeface(_font);

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		about_app = (ImageButton) findViewById(R.id.about_app);
		about_app.setOnClickListener(this);
		
		already_invited = (ImageButton) findViewById(R.id.already_invited);
		already_invited.setOnClickListener(this);
		
		call_customer_care  = (Button) findViewById(R.id.call_customer_care);
		call_customer_care.setTypeface(_font, Typeface.NORMAL);
		call_customer_care.setOnClickListener(this);

		t_c = (ImageButton) findViewById(R.id.t_c);
		t_c.setOnClickListener(this);

		p_p = (ImageButton) findViewById(R.id.p_p);
		p_p.setOnClickListener(this);

		faq = (ImageButton) findViewById(R.id.faq);
		faq.setOnClickListener(this);

		edit_profile = (ImageButton) findViewById(R.id.edit_user_profile);
		edit_profile.setOnClickListener(this);
		
		fb_share = (ImageButton) findViewById(R.id.fb_share);
		fb_share.setOnClickListener(this);
		
		twitter_share = (ImageButton) findViewById(R.id.twitter_share);
		twitter_share.setOnClickListener(this);

		change_password = (ImageButton) findViewById(R.id.change_password);
		change_password.setOnClickListener(this);

		store_credits = (ImageButton) findViewById(R.id.my_store_credits);
		store_credits.setOnClickListener(this);

		order_history = (ImageButton) findViewById(R.id.order_history);
		order_history.setOnClickListener(this);

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));
		
		
		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);
		
		TextView set_user_name = (TextView)findViewById(R.id.set_user_name);
		String _username = _mypref .getString("_UserName", null);
		if(!(_username==null)){
			set_user_name.setTypeface(_font);
			set_user_name.setText("Hi! "+_username.replace("Hi!",""));
		}else{
			set_user_name.setText("Hi! Guest");
		}

		cart_btn = (ImageButton) findViewById(R.id.cart_btn);
		cart_btn.setOnClickListener(this);

		_all = (Button) findViewById(R.id.btn_all_cat);
		_all.setTypeface(_font);
		_all.setOnClickListener(this);

		_men = (Button) findViewById(R.id.cat_men);
		_men.setTypeface(_font);
		_men.setOnClickListener(this);

		_women = (Button) findViewById(R.id.cat_women);
		_women.setTypeface(_font);
		_women.setOnClickListener(this);

		_childrens = (Button) findViewById(R.id.cat_children);
		_childrens.setOnClickListener(this);

		_home = (Button) findViewById(R.id.cat_home);
		_home.setTypeface(_font);
		_home.setOnClickListener(this);

		_accessories = (Button) findViewById(R.id.cat_accesories);
		_accessories.setTypeface(_font);
		_accessories.setOnClickListener(this);

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
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": Settings/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_app:
			Intent _about = new Intent(SettingTab.this, AboutApp.class);
			startActivity(_about);
			break;
			
		case R.id.twitter_share:
			Intent _intent = new Intent(Intent.ACTION_SEND);
			_intent.setType("text/plain");
			_intent.putExtra(android.content.Intent.EXTRA_TEXT,"Hi, I am using Android BrandsfeverApp\n\n"
					+ "https://www.brandsfever.com/");
			_intent.putExtra(android.content.Intent.EXTRA_STREAM,R.drawable.ic_launcher);
			startActivity(Intent.createChooser(_intent, "Hi, I am using Android BrandsfeverApp\n\n"
					+ "https://www.brandsfever.com/"));
			break;
			
		case R.id.fb_share:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(android.content.Intent.EXTRA_TEXT,"Hi, I am using Android BrandsfeverApp\n\n"
					+ "https://www.brandsfever.com/");
			intent.putExtra(android.content.Intent.EXTRA_STREAM,R.drawable.ic_launcher);
			startActivity(Intent.createChooser(intent, "Hi, I am using Android BrandsfeverApp\n\n"
					+ "https://www.brandsfever.com/"));
			break;

		case R.id.t_c:
			Intent _tc = new Intent(SettingTab.this, TermsConditions.class);
			startActivity(_tc);
			break;

		case R.id.p_p:
			Intent _pp = new Intent(SettingTab.this, PrivatePolicy.class);
			startActivity(_pp);
			break;

		case R.id.faq:
			Intent _faq = new Intent(SettingTab.this, Faq.class);
			startActivity(_faq);
			break;

		case R.id.edit_user_profile:
			Intent _ep = new Intent(SettingTab.this, Edit_Profile.class);
			startActivity(_ep);
			break;
		case R.id.change_password:
			Intent _cp = new Intent(SettingTab.this, ChangePassword.class);
			startActivity(_cp);
			break;

		case R.id.my_store_credits:
			Intent _sc = new Intent(SettingTab.this,
					StoreCredis_SettingPage.class);
			startActivity(_sc);
			break;
			
		case R.id.already_invited:
			Intent _AI = new Intent(SettingTab.this,
					People_I_Invited.class);
			startActivity(_AI);
			break;

		case R.id.order_history:
			Intent _oh = new Intent(SettingTab.this, MyOrderHistory.class);
			startActivity(_oh);
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

		case R.id.call_customer_care:
			Intent callIntent = new Intent(Intent.ACTION_CALL);
        	callIntent.setData(Uri.parse("tel:"+"+6588456088"));
        	callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        	startActivity(callIntent);
			break;
			
		case R.id.cart_btn:
			if (!(_getToken == null) && !(_getuserId == null)) {
				Intent _gotocart = new Intent(_ctx, MyCartFragment.class);
				startActivity(_gotocart);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			} else {
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				final TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);
				_seterrormsg.setText("Please login!");
				Toast toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
			break;

		case R.id.cat_men:
			slide_me.closeRightSide();
			Intent men = new Intent(_ctx, ProductDisplay.class);
			men.putExtra("tab", "men");
			startActivity(men);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_women:
			slide_me.closeRightSide();
			Intent women = new Intent(_ctx, ProductDisplay.class);
			women.putExtra("tab", "women");
			startActivity(women);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_children:
			slide_me.closeRightSide();
			Intent children = new Intent(_ctx, ProductDisplay.class);
			children.putExtra("tab", "children");
			startActivity(children);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_home:
			slide_me.closeRightSide();
			Intent home = new Intent(_ctx, ProductDisplay.class);
			home.putExtra("tab", "home");
			startActivity(home);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cat_accesories:
			slide_me.closeRightSide();
			Intent acc = new Intent(_ctx, ProductDisplay.class);
			acc.putExtra("tab", "accessories");
			startActivity(acc);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_setting:
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
			break;

		case R.id.my_cart:
			Intent _cart = new Intent(SettingTab.this, MyCartFragment.class);
			startActivity(_cart);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;
			
			
		case R.id.btn_invite:
			Intent _invite = new Intent(_ctx, InviteFragment.class);
			startActivity(_invite);
			slide_me.closeRightSide();
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.btn_logout:
			Editor editor = _mypref.edit();
			editor.clear();
			editor.commit();
			Intent _lintent = new Intent(getApplicationContext(),
					ProductDisplay.class);
			startActivity(_lintent);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		default:
			break;
		}

	}
}
