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
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;

public class SettingPhone extends Fragment implements OnClickListener {
	Typeface _font;
	Button about_app_phone, T_C, private_policy, myaccount, call_customer_care;
	ImageButton main_menu, cart_btn;
	SimpleSideDrawer slide_me;
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_setting_phone, container, false);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getActivity().getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		share_on_fb = (ImageButton) view.findViewById(R.id.share_on_fb);
		share_on_fb.setOnClickListener(this);
		share_on_twitter = (ImageButton) view.findViewById(R.id.share_on_twitter);
		share_on_twitter.setOnClickListener(this);
		setting_tag = (TextView)view.findViewById(R.id.setting_tag);
		setting_tag.setTypeface(_font);

		about_app_phone = (Button) view.findViewById(R.id.about_app_phone);
		about_app_phone.setTypeface(_font, Typeface.NORMAL);
		about_app_phone.setOnClickListener(this);

		T_C = (Button) view.findViewById(R.id.T_C);
		T_C.setTypeface(_font, Typeface.NORMAL);
		T_C.setOnClickListener(this);

		private_policy = (Button) view.findViewById(R.id.private_policy);
		private_policy.setTypeface(_font, Typeface.NORMAL);
		private_policy.setOnClickListener(this);

		myaccount = (Button) view.findViewById(R.id.myaccount);
		myaccount.setTypeface(_font, Typeface.NORMAL);
		myaccount.setOnClickListener(this);

		call_customer_care = (Button) view.findViewById(R.id.call_customer_care);
		call_customer_care.setTypeface(_font, Typeface.NORMAL);
		call_customer_care.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(getActivity());
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": Settings/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.about_app_phone:
			Intent about = new Intent(getActivity(), AboutApp.class);
			startActivity(about);
			break;

		case R.id.T_C:
			Intent tc = new Intent(getActivity(), TermsConditions.class);
			startActivity(tc);
			break;

		case R.id.call_customer_care:
			directCall();
			break;

		case R.id.share_on_twitter:
			type = 2;
			Toast.makeText(getActivity().getApplicationContext(),
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

				Intent intent = new Intent(getActivity().getApplicationContext(),
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
			SessionStore.restore(mFacebook, getActivity().getApplicationContext());

			type = 1;
			if (!mFacebook.isSessionValid()) {
				mFacebook.authorize(getActivity(), new String[] {
						"publish_stream", "email", "user_groups",
						"read_stream", "user_about_me", "offline_access" }, 1,
						new LoginDialogListener());

			} else {
				postToWall();
			}
			break;

		case R.id.private_policy:
			Intent _pp = new Intent(getActivity(), PrivatePolicy.class);
			startActivity(_pp);
			break;

		case R.id.myaccount:
			Intent ma = new Intent(getActivity(), MyAccount.class);
			startActivity(ma);
			break;

		default:
			break;
		}

	}
	
	public void directCall(){
		try {
			View view = View.inflate(getActivity().getBaseContext(),
					R.layout.call_settings,null);			
				final PopupWindow pwindo = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
					pwindo.showAtLocation(view, Gravity.CENTER, 0, 0);		
					
					
					Button cancel_password = (Button) view.findViewById(R.id.cancel_password);
					cancel_password.setTypeface(_font, Typeface.NORMAL);
					cancel_password.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							pwindo.dismiss();
						}
					});
					
					Button reset_password = (Button) view.findViewById(R.id.reset_password);
					reset_password.setTypeface(_font, Typeface.BOLD);
					reset_password.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							pwindo.dismiss();
							Intent callIntent = new Intent(Intent.ACTION_CALL);
							callIntent.setData(Uri.parse("tel:" + "+6563029010"));
							callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(callIntent);
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (type == 1) {
			mFacebook.authorizeCallback(requestCode, resultCode, data);
		} else if (type == 2) {

			if (resultCode == Activity.RESULT_OK) {
				AccessToken accessToken = null;
				try {
					String oauthVerifier = data.getExtras().getString(
							Const.IEXTRA_OAUTH_VERIFIER);
					accessToken = mTwitter.getOAuthAccessToken(mRequestToken,
							oauthVerifier);
					SharedPreferences pref = getActivity().getSharedPreferences(
							Const.PREF_NAME, Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = pref.edit();
					editor.putString(Const.PREF_KEY_ACCESS_TOKEN,
							accessToken.getToken());
					editor.putString(Const.PREF_KEY_ACCESS_TOKEN_SECRET,
							accessToken.getTokenSecret());
					editor.commit();
					if (mRequestToken != null) {
						tweetToWall();
					}
					Toast.makeText(getActivity(), "authorized", Toast.LENGTH_SHORT)
							.show();

				} catch (TwitterException e) {
					e.printStackTrace();
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.w("twitter auth", "Twitter auth canceled.");
			}
		}
	}

	public void postToWall() {
		// post on user's wall.
		View post = null;

		LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());

		post = li.inflate(R.layout.sharedialog_share, null);
		final EditText desc;

		desc = (EditText) post.findViewById(R.id.edittextdesc);
		desc.requestFocus();

		ImageView back = (ImageView) post.findViewById(R.id.imageView1);
		ImageView post1 = (ImageView) post.findViewById(R.id.imageView2);
		ImageView share_image = (ImageView) post.findViewById(R.id.imageView3);

		final Dialog mDialog = new Dialog(getActivity(),
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
				mDialog.dismiss();
			}
		});

		post1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(
						mFacebook);
				Bundle params = new Bundle();
				params.putString("message", desc.getText().toString());

				params.putString("name", desc.getText().toString());
				params.putString("link", "https://www.brandsfever.com");
				params.putString("description", "Svv");

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
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) getView().findViewById(R.id.relativeLayout1));
				TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);

				_seterrormsg.setText(_errormsg);
				Toast toast = new Toast(getActivity());
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
		});

	}

	public final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			try {
				SessionStore.save(mFacebook, getActivity());

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

	public void tweetToWall() {
		// post on user's wall.
		View post = null;

		LayoutInflater li = LayoutInflater.from(getActivity().getApplicationContext());
		post = li.inflate(R.layout.twitterialogsettings, null);
		final EditText t1;
		t1 = (EditText) post.findViewById(R.id.textView0);

		// t1.setText("Messege:Deemz");
		final ImageView back = (ImageView) post.findViewById(R.id.imageView1);
		final ImageView post1 = (ImageView) post.findViewById(R.id.imageView2);
		final ImageView post_image = (ImageView) post
				.findViewById(R.id.imageView_posts);
		final Dialog mDialog = new Dialog(getActivity(),
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
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
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
			} catch (TwitterException e) {

			}
			return null;
		}

		protected void onPostExecute(String file_url) {

			pDialog.dismiss();
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getActivity().getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();

				}
			});

		}

	}

}
