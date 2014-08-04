package com.brandsfever;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

public class MyAccount extends FragmentActivity implements OnClickListener {
	Button faq, editprofile, changepassword, seeorderhistory, invitedpeople,
			seestorecredits;
	TextView myaccount_tag;
	Typeface _font;

	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	int color, colors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			Intent faq = new Intent(this, Faq.class);
			startActivity(faq);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.editprofile:
			Intent editp = new Intent(this, Edit_Profile.class);
			startActivity(editp);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.changedpassword:
			Intent cp = new Intent(this, ChangePassword.class);
			startActivity(cp);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.orderhistory:
			Intent oh = new Intent(this, MyOrderHistory.class);
			startActivity(oh);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.seeinvitedpeople:
			Intent Si = new Intent(this, People_I_Invited.class);
			startActivity(Si);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.seestorecredits:
			Intent sc = new Intent(this,
					StoreCredis_SettingPage.class);
			startActivity(sc);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}