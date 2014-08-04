package com.brandsfever;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dataholder.DataHolderClass;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Facebook.DialogListener;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;

public class SettingPhone extends FragmentActivity implements OnClickListener {
	Context _ctx = SettingPhone.this;
	Typeface _font;
	Button about_app_phone, T_C, private_policy, myaccount, call_customer_care;
	ImageButton main_menu, cart_btn;
	SimpleSideDrawer slide_me;
	Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, _mycart, mSupport, _invite, _logout;
	TextView setting_tag;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	ImageButton share_on_fb, share_on_twitter;
	int color, colors;

	int type = 0;
	byte[] bitmapdata;
	private Facebook mFacebook;
	public String oauthVerifier;
	private String twitedata;
	protected Twitter mTwitter;
	ProgressDialog pDialog;
	protected RequestToken mRequestToken;
	public Bitmap facebook_bit_map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_setting_phone);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		share_on_fb = (ImageButton) findViewById(R.id.share_on_fb);
		share_on_fb.setOnClickListener(this);
		share_on_twitter = (ImageButton) findViewById(R.id.share_on_twitter);
		share_on_twitter.setOnClickListener(this);
		setting_tag = (TextView) findViewById(R.id.setting_tag);
		setting_tag.setTypeface(_font);

		about_app_phone = (Button) findViewById(R.id.about_app_phone);
		about_app_phone.setTypeface(_font, Typeface.NORMAL);
		about_app_phone.setOnClickListener(this);

		T_C = (Button) findViewById(R.id.T_C);
		T_C.setTypeface(_font, Typeface.NORMAL);
		T_C.setOnClickListener(this);

		private_policy = (Button) findViewById(R.id.private_policy);
		private_policy.setTypeface(_font, Typeface.NORMAL);
		private_policy.setOnClickListener(this);

		myaccount = (Button) findViewById(R.id.myaccount);
		myaccount.setTypeface(_font, Typeface.NORMAL);
		myaccount.setOnClickListener(this);

		call_customer_care = (Button) findViewById(R.id.call_customer_care);
		call_customer_care.setTypeface(_font, Typeface.NORMAL);
		call_customer_care.setOnClickListener(this);

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));
		
		main_menu = (ImageButton) findViewById(R.id.main_menu);
		main_menu.setOnClickListener(this);

		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String _username = _mypref.getString("_UserName", null);
		if (!(_username == null)) {
			set_user_name.setTypeface(_font);
			set_user_name.setText("Hi! " + _username.replace("Hi!", ""));
		} else {
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
		_childrens.setTypeface(_font);
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

		_all.setTextColor(colors);
		_men.setTextColor(colors);
		_women.setTextColor(colors);
		_childrens.setTextColor(colors);
		_home.setTextColor(colors);
		_accessories.setTextColor(colors);
		_settings.setTextColor(color);
		_mycart.setTextColor(colors);
		mSupport.setTextColor(colors);
		_invite.setTextColor(colors);
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.about_app_phone:
			Intent _about = new Intent(SettingPhone.this, AboutApp.class);
			startActivity(_about);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.T_C:
			Intent _tc = new Intent(SettingPhone.this, TermsConditions.class);
			startActivity(_tc);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.call_customer_care:
			/*Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:" + "+6588456088"));
			callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(callIntent);*/
			_directCall();
			break;

		case R.id.share_on_twitter:
			type = 2;
			Toast.makeText(getApplicationContext(),
					"Redirecting you on twitter", 2).show();

			ConfigurationBuilder confbuilder = new ConfigurationBuilder();
			Configuration conf = confbuilder
					.setOAuthConsumerKey(Const.CONSUMER_KEY)
					.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();
			mTwitter = new TwitterFactory(conf).getInstance();
			mTwitter.setOAuthAccessToken(null);
			try {
				mRequestToken = mTwitter
						.getOAuthRequestToken(Const.CALLBACK_URL);

				Intent intent = new Intent(getApplicationContext(),
						TwitterLogin.class);
				intent.putExtra(Const.IEXTRA_AUTH_URL,
						mRequestToken.getAuthorizationURL());
				startActivityForResult(intent, 0);

			} catch (TwitterException e) {
				e.printStackTrace();
			}
			break;

		case R.id.share_on_fb:
			mFacebook = new Facebook(getResources().getString(R.string.fbid));
			SessionStore.restore(mFacebook, getApplicationContext());

			type = 1;
			if (!mFacebook.isSessionValid()) {
				mFacebook.authorize(SettingPhone.this, new String[] {
						"publish_stream", "email", "user_groups",
						"read_stream", "user_about_me", "offline_access" }, 1,
						new LoginDialogListener());

			} else {
				postToWall();
			}
			break;

		case R.id.private_policy:
			Intent _pp = new Intent(SettingPhone.this, PrivatePolicy.class);
			startActivity(_pp);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.myaccount:
			Intent _ma = new Intent(SettingPhone.this, MyAccount.class);
			startActivity(_ma);
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			break;

		case R.id.main_menu:
			slide_me.toggleLeftDrawer();
			break;
		case R.id.btn_all_cat:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent all = new Intent(_ctx, ProductDisplay.class);
				all.putExtra("tab", "all");
				startActivity(all);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_men:
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

		case R.id.cat_women:
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

		case R.id.cat_children:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent children = new Intent(_ctx, ProductDisplay.class);
				children.putExtra("tab", "children");
				startActivity(children);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_home:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent home = new Intent(_ctx, ProductDisplay.class);
				home.putExtra("tab", "home");
				startActivity(home);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
				finish();
			}
			break;

		case R.id.cat_accesories:
			if (slide_me.isClosed()) {
				slide_me.setEnabled(false);
			} else {
				slide_me.setEnabled(true);
				slide_me.closeRightSide();
				Intent acc = new Intent(_ctx, ProductDisplay.class);
				acc.putExtra("tab", "accessories");
				startActivity(acc);
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
					System.out.println("in phone");
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
				Intent _cart = new Intent(_ctx, MyCartFragment.class);
				startActivity(_cart);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
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
				Intent _lintent = new Intent(getApplicationContext(),
						ProductDisplay.class);
				startActivity(_lintent);
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			}
			break;

		case R.id.cart_btn:
			if (!(_getToken == null) && !(_getuserId == null)) {
				Intent _gotocart = new Intent(_ctx, MyCartFragment.class);
				startActivity(_gotocart);
				finish();
				overridePendingTransition(R.anim.push_out_to_right,
						R.anim.push_out_to_left);
			} else {
				Toast.makeText(_ctx, "please login", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}

	}
	
	// ********************************************************************************************************************//
	public void _directCall(){
		try {
			View view = View.inflate(getBaseContext(),
					R.layout.call_settings,null);			
				final PopupWindow pwindo = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
					pwindo.showAtLocation(view, Gravity.CENTER, 0, 0);		
					
					
					Button cancel_password = (Button) view.findViewById(R.id.cancel_password);
					cancel_password.setTypeface(_font, Typeface.NORMAL);
					cancel_password.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							pwindo.dismiss();
							
						}
					});
					
					Button reset_password = (Button) view.findViewById(R.id.reset_password);
					reset_password.setTypeface(_font, Typeface.BOLD);
					reset_password.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							pwindo.dismiss();
							Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setData(Uri.parse("tel:" + "+6588456088"));
							callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(callIntent);
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ********************************************************************************************************************//

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (type == 1) {
			mFacebook.authorizeCallback(requestCode, resultCode, data);
		} else if (type == 2) {
			System.out.println("twitter enter");

			if (resultCode == RESULT_OK) {
				AccessToken accessToken = null;
				try {
					String oauthVerifier = data.getExtras().getString(
							Const.IEXTRA_OAUTH_VERIFIER);
					accessToken = mTwitter.getOAuthAccessToken(mRequestToken,
							oauthVerifier);
					SharedPreferences pref = getSharedPreferences(
							Const.PREF_NAME, MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString(Const.PREF_KEY_ACCESS_TOKEN,
							accessToken.getToken());
					editor.putString(Const.PREF_KEY_ACCESS_TOKEN_SECRET,
							accessToken.getTokenSecret());
					editor.commit();
					if (mRequestToken != null) {
						tweetToWall();
					}
					Toast.makeText(this, "authorized", Toast.LENGTH_SHORT)
							.show();

				} catch (TwitterException e) {
					e.printStackTrace();
				}
			} else if (resultCode == RESULT_CANCELED) {
				Log.w("twitter auth", "Twitter auth canceled.");
			}
		}
	}

	// ***************************** facebook //// ***********************************//
	public void postToWall() {
		// post on user's wall.
		View post = null;

		LayoutInflater li = LayoutInflater.from(getApplicationContext());

		post = li.inflate(R.layout.sharedialog_share, null);
		final EditText desc;

		desc = (EditText) post.findViewById(R.id.edittextdesc);
		desc.requestFocus();

		ImageView back = (ImageView) post.findViewById(R.id.imageView1);
		ImageView post1 = (ImageView) post.findViewById(R.id.imageView2);
		ImageView share_image = (ImageView) post.findViewById(R.id.imageView3);

		final Dialog mDialog = new Dialog(SettingPhone.this,
				android.R.style.Theme_Translucent_NoTitleBar);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(post);

		mDialog.show();
		facebook_bit_map = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		share_image.setImageBitmap(facebook_bit_map);

		desc.setText("Check out the BrandsFever App. Download this cool app from Playstore:https://play.google.com/store/apps/details?id=com.brandsfever&hl=en");

		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});

		post1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(
						mFacebook);
				Bundle params = new Bundle();
				params.putString("message", desc.getText().toString());

				params.putString("name", desc.getText().toString());
				params.putString("link", "https://www.brandsfever.com");
				params.putString("description", "Svv");
				/*
				 * Resources res = getResources(); Drawable drawable =
				 * res.getDrawable(R.drawable.fbicon);
				 */

				facebook_bit_map = BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher);

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				facebook_bit_map.compress(Bitmap.CompressFormat.PNG, 100,
						stream);
				byte[] bitMapData = stream.toByteArray();
				params.putByteArray("picture", bitMapData);
				mAsyncFbRunner.request("me/photos", params, "POST",
						new WallPostListener());
				mDialog.dismiss();
				
				String _errormsg = "Posted Successfully";
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.relativeLayout1));
				TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);

				_seterrormsg.setText(_errormsg);
				Toast toast = new Toast(_ctx);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
		});

	}

	public final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			try {
				SessionStore.save(mFacebook, SettingPhone.this);

				postToWall();

			} catch (Exception error) {
				error.printStackTrace();

			}

		}

		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}

		public void onError(DialogError e) {
			// TODO Auto-generated method stub

		}

		public void onCancel() {
			// TODO Auto-generated method stub

		}

	}

	private final class WallPostListener extends BaseRequestListener {
		private Handler mRunOnUi = new Handler();

		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {
				public void run() {

					/*
					 * Toast.makeText(MainActivity.this, "Posted to Facebook",
					 * Toast.LENGTH_SHORT).show();
					 */
				}
			});
		}

		public void onIOException(IOException e) {
			// TODO Auto-generated method stub

		}

		public void onFileNotFoundException(FileNotFoundException e) {
			// TODO Auto-generated method stub

		}

		public void onMalformedURLException(MalformedURLException e) {
			// TODO Auto-generated method stub

		}

		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}
	}

	// ***************************twitter**********************************///

	public void tweetToWall() {
		// post on user's wall.
		View post = null;

		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		System.out.println("twitter share-main--");
		post = li.inflate(R.layout.twitterialogsettings, null);
		final EditText t1;
		// ImageView i=(ImageView) post.findViewById(R.id.imageView3);
		t1 = (EditText) post.findViewById(R.id.textView0);

		// t1.setText("Messege:Deemz");
		final ImageView back = (ImageView) post.findViewById(R.id.imageView1);
		final ImageView post1 = (ImageView) post.findViewById(R.id.imageView2);
		final ImageView post_image = (ImageView) post
				.findViewById(R.id.imageView_posts);
		final Dialog mDialog = new Dialog(SettingPhone.this,
				android.R.style.Theme_Translucent_NoTitleBar);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(post);

		mDialog.show();

		t1.setText("Check out the BrandsFever App. Download this cool app from Playstore:https://play.google.com/store/apps/details?id=com.brandsfever&hl=en");
		t1.setTextSize(12);

		facebook_bit_map = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_launcher);
		Bitmap bm_dup = Bitmap.createScaledBitmap(facebook_bit_map, 140, 140,
				false);
		post_image.setImageBitmap(bm_dup);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm_dup.compress(CompressFormat.PNG, 0, bos);
		bitmapdata = bos.toByteArray();

		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});

		post1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
				twitedata = t1.getText().toString();
				new updateTwitterStatus().execute("helloooo");

			}
		});

	}

	class updateTwitterStatus extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SettingPhone.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();

				Configuration conf = builder
						.setOAuthConsumerKey(Const.CONSUMER_KEY)
						.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();

				StatusUpdate ad = new StatusUpdate(twitedata);
				InputStream is = new ByteArrayInputStream(bitmapdata);
				ad.setMedia("BrandsFever", is);
				twitter4j.Status responses = mTwitter.updateStatus(ad);
				// Log.d("Status", "> " + response.getText());
			} catch (TwitterException e) {

				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();

				}
			});

		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
}
