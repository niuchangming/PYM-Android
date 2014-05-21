package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.dataholder.DataHolderClass;
import com.datamodel.InvitePeopleInfo;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.navdrawer.SimpleSideDrawer;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class People_I_Invited extends FragmentActivity implements
		OnClickListener {
	SimpleSideDrawer slide_me;
	Context _ctx = People_I_Invited.this;
	Button _all, _men, _women, _childrens, _home, _accessories, _login,
			_settings, mSupport, _mycart, _invite, _logout;
	ImageButton main_menu, back_btn, cart_btn;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	Typeface _font;
	String _reinviteEmail = "", _ResponseFromServer;
	InviteAdapter _adapter;
	FacebookInviteAdapter _fbadapter;
	ImageButton re_invite_selected;
	ListView set_email_invite, set_facebook_invite;
	TextView select, email, invitation;
	Button pending_invitations, facebook_invitations, accepted_invitations;
	RelativeLayout four, five,six,footer_set;
	View footer_view;
	TextView Your_Invitations_tag;
	
	
	TextView facebookuser,fb_state,nameinvitation,emailinvitation;
	
	private ArrayList<InvitePeopleInfo> pendingemail = new ArrayList<InvitePeopleInfo>();
	private ArrayList<InvitePeopleInfo> pendingfacebook = new ArrayList<InvitePeopleInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.phone_people__i__invited);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_people_invited);
		} else if (a >= 9) {
			setContentView(R.layout.ten_inch_people_invited);
		}

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);
		set_email_invite = (ListView) findViewById(R.id.set_email_invite);
		set_facebook_invite = (ListView) findViewById(R.id.set_facebook_invite);

		Your_Invitations_tag = (TextView) findViewById(R.id.Your_Invitations_tag);
		Your_Invitations_tag.setTypeface(_font, Typeface.NORMAL);

		select = (TextView) findViewById(R.id.select);
		select.setTypeface(_font, Typeface.NORMAL);
		email = (TextView) findViewById(R.id.email);
		email.setTypeface(_font, Typeface.NORMAL);
		invitation = (TextView) findViewById(R.id.invitation);
		invitation.setTypeface(_font, Typeface.NORMAL);
		
		fb_state = (TextView) findViewById(R.id.fb_state);
		fb_state.setTypeface(_font, Typeface.NORMAL);
		facebookuser = (TextView) findViewById(R.id.facebookuser);
		facebookuser.setTypeface(_font, Typeface.NORMAL);
		
		emailinvitation = (TextView) findViewById(R.id.emailinvitation);
		emailinvitation.setTypeface(_font, Typeface.NORMAL);
		nameinvitation = (TextView) findViewById(R.id.nameinvitation);
		nameinvitation.setTypeface(_font, Typeface.NORMAL);
		
		
		
		
		
		
		footer_view=(View)findViewById(R.id.foot);

		re_invite_selected = (ImageButton) findViewById(R.id.re_invite_selected);
		re_invite_selected.setOnClickListener(this);
		pending_invitations = (Button) findViewById(R.id.pending_invitations);
		int color = Integer.parseInt("000000", 16) + 0xFF000000;
		pending_invitations.setTextColor(color);
		pending_invitations.setTypeface(_font, Typeface.NORMAL);
		pending_invitations.setOnClickListener(this);
		facebook_invitations = (Button) findViewById(R.id.facebook_invitations);
		facebook_invitations.setOnClickListener(this);
		facebook_invitations.setTypeface(_font, Typeface.NORMAL);
		accepted_invitations = (Button) findViewById(R.id.accepted_invitations);
		accepted_invitations.setOnClickListener(this);
		accepted_invitations.setTypeface(_font, Typeface.NORMAL);

		four = (RelativeLayout) findViewById(R.id.four);
		four.setVisibility(View.VISIBLE);
		five = (RelativeLayout) findViewById(R.id.five);
		five.setVisibility(View.GONE);
		six = (RelativeLayout) findViewById(R.id.six);
		footer_set = (RelativeLayout) findViewById(R.id.footer_set);
		
		six.setVisibility(View.GONE);
		new GetRequestCount().execute();

		slide_me = new SimpleSideDrawer(this);
		slide_me.setLeftBehindContentView(R.layout.menu_bar);
		slide_me.setBackgroundColor(Color.parseColor("#000000"));

		TextView set_user_name = (TextView) findViewById(R.id.set_user_name);
		String _username = _mypref.getString("_UserName", null);
		if (!(_username == null)) {
			set_user_name.setTypeface(_font);
			set_user_name.setText("Hi! "+_username.replace("Hi!",""));
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
	}

	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": invitations/?user_id="+_getuserId+"/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	// **************************************************************************************************************//
	private class GetRequestCount extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(People_I_Invited.this, "Loading",
					true, true, this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int displayHeight = displaymetrics.heightPixels;
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams wmlp = mProgressHUD.getWindow()
					.getAttributes();
			wmlp.y = displayHeight / 4;
			mProgressHUD.getWindow().setAttributes(wmlp);
			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected String doInBackground(String... params) {
			String _inviteUrl = "https://www.brandsfever.com/api/v5/invitations/?user_id="
					+ _getuserId + "&token=" + _getToken;
			GetInvitations(_inviteUrl);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			_adapter = new InviteAdapter(People_I_Invited.this, pendingemail);
			set_email_invite.setAdapter(_adapter);
			_fbadapter = new FacebookInviteAdapter(People_I_Invited.this,
					pendingfacebook);
			set_facebook_invite.setAdapter(_fbadapter);
			mProgressHUD.dismiss();
		}
	}

	// *************************************************************************************************************************************//
	public void GetInvitations(String _urls) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(_urls);
		try {
			HttpResponse _httpresponse = _httpclient.execute(_httpget);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			Log.i("--------------Https Responsecode----------", "."
					+ _responsecode);
			if (_responsecode == 200) {
				InputStream _inputstream = _httpresponse.getEntity()
						.getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						_inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				String _content = total.toString();
				System.out.println(_content);
				try {
					JSONObject obj = new JSONObject(_content);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						JSONObject obj1 = obj.getJSONObject("invitations");
						JSONObject _facebook = obj1
								.getJSONObject("by_facebook");
						JSONObject _facebookpending = _facebook
								.getJSONObject("pending");
						Map<String, String> maps = new HashMap<String, String>();
						Iterator iters = _facebookpending.keys();
						while (iters.hasNext()) {
							String key = (String) iters.next();
							String value = _facebookpending.getString(key);
							System.out.println(" facebook values are" + key
									+ value);
							InvitePeopleInfo _ipi = new InvitePeopleInfo();
							_ipi.set_fbId(key);
							_ipi.set_fbNo(value);
							pendingfacebook.add(_ipi);
							maps.put(key, value);
						}
						_facebookpending.length();
						JSONArray facebookaccepted = _facebook
								.getJSONArray("accepted");
						facebookaccepted.length();
						for (int i = 0; i < facebookaccepted.length(); i++) {

						}
						JSONObject _byemail = obj1.getJSONObject("by_email");
						JSONObject _pendingemail = _byemail
								.getJSONObject("pending");
						Map<String, String> map = new HashMap<String, String>();
						Iterator iter = _pendingemail.keys();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							String value = _pendingemail.getString(key);
							System.out.println("values are" + key + value);
							InvitePeopleInfo _ipl = new InvitePeopleInfo();
							_ipl.set_PendingInvitedEmail(key);
							_ipl.set_NumOfPendingInvitations(value);
							pendingemail.add(_ipl);
							map.put(key, value);
						}
						_pendingemail.length();
						JSONArray _acceptedemail = _byemail
								.getJSONArray("accepted");
						_acceptedemail.length();

					} else {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// *****************************************************************************************************************//
	class InviteAdapter extends BaseAdapter {
		Context _scontext;
		LayoutInflater inflater;
		ArrayList<InvitePeopleInfo> data;
		Integer mIndex = -1;
		View itemView;

		public InviteAdapter(Context context,
				ArrayList<InvitePeopleInfo> arraylist) {
			this._scontext = context;
			data = arraylist;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			TextView invitee_email, number_of_invitations;
			CheckBox checks;
			int a = DataHolderClass.getInstance().get_deviceInch();
			if (a <= 6) {
				inflater = (LayoutInflater) _scontext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.phone_people_invited,
						parent, false);
			} else if (a >= 7 && a < 9) {
				inflater = (LayoutInflater) _scontext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(
						R.layout.seven_inch_people_invite_inflator, parent,
						false);
			} else if (a >= 9) {
				inflater = (LayoutInflater) _scontext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(
						R.layout.ten_inch_people_invited_inflator, parent,
						false);
			}

			invitee_email = (TextView) itemView.findViewById(R.id.set_email);
			invitee_email.setTypeface(_font, Typeface.NORMAL);
			number_of_invitations = (TextView) itemView
					.findViewById(R.id.number_of_invitation);
			number_of_invitations.setTypeface(_font, Typeface.NORMAL);
			checks = (CheckBox) itemView.findViewById(R.id.checks);
			InvitePeopleInfo obj = data.get(position);
			invitee_email.setText(obj.get_PendingInvitedEmail());
			number_of_invitations.setText(obj.get_NumOfPendingInvitations());
			checks.setTag(position);
			invitee_email.setTag(position);
			if (mIndex == position)
				checks.setChecked(true);
			else
				checks.setChecked(false);

			checks.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					int getPosition = (Integer) buttonView.getTag();
					InvitePeopleInfo _obj = data.get(getPosition);
					_reinviteEmail = _obj.get_PendingInvitedEmail();
					System.out.println("my email is" + _reinviteEmail);
					System.out.println("pos is" + getPosition);
					setCheckedButton(position);
				}

				private void setCheckedButton(int pos) {
					mIndex = pos;
					notifyDataSetChanged();
				}
			});

			return itemView;
		}

	}

	// ***************************************************************************************************************//
	class FacebookInviteAdapter extends BaseAdapter {
		Context _scontext;
		LayoutInflater inflater;
		ArrayList<InvitePeopleInfo> _data;
		View itemView;

		public FacebookInviteAdapter(Context context,
				ArrayList<InvitePeopleInfo> arraylist) {
			this._scontext = context;
			_data = arraylist;
		}

		@Override
		public int getCount() {
			return _data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView _setNumber;
			ImageView _setImage;
			int _a = DataHolderClass.getInstance().get_deviceInch();
			if (_a <= 6) {
				inflater = (LayoutInflater) _scontext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.phone_fb_invite, parent,
						false);
			} else if (_a >= 7 && _a < 9) {
				inflater = (LayoutInflater) _scontext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.seven_inch_fb_invite,
						parent, false);
			} else if (_a >= 9) {
				inflater = (LayoutInflater) _scontext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.ten_inch_fb_invite,
						parent, false);
			}

			_setNumber = (TextView) itemView.findViewById(R.id.set_number);
			_setNumber.setTypeface(_font);
			_setImage = (ImageView) itemView.findViewById(R.id.set_user_image);
			InvitePeopleInfo obj = _data.get(position);
			_setNumber.setText(obj.get_fbNo());
			String userID = obj.get_fbId();
			String _imagePath = "http://graph.facebook.com/" + userID
					+ "/picture?type=small";
			AQuery aq = new AQuery(_scontext);
			aq.id(_setImage).image(_imagePath);

			return itemView;
		}

	}

	// ****************************************************************************************************************//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.re_invite_selected:
			if (_reinviteEmail.length() > 0) {
				new SendReInvite().execute();
			} else {
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.error_popop,
						(ViewGroup) findViewById(R.id.pop));
				TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);
				_seterrormsg.setText("Please select one email to re-invite!");
				Toast toast = new Toast(People_I_Invited.this);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();
			}
			break;

		case R.id.pending_invitations:
			four.setVisibility(View.VISIBLE);
			re_invite_selected.setVisibility(View.VISIBLE);
			footer_view.setBackgroundColor(Color.GRAY);
			footer_set.setBackgroundColor(Color.TRANSPARENT);
			int color = Integer.parseInt("000000", 16) + 0xFF000000;
			pending_invitations.setTextColor(color);
			
			five.setVisibility(View.GONE);
			six.setVisibility(View.GONE);
			int colors = Integer.parseInt("FFFFFF", 16) + 0xFF000000;
			facebook_invitations.setTextColor(colors);
			accepted_invitations.setTextColor(colors);
			break;
		case R.id.facebook_invitations:
			four.setVisibility(View.GONE);
			six.setVisibility(View.GONE);
			re_invite_selected.setVisibility(View.GONE);
			footer_view.setBackgroundColor(Color.WHITE);
			footer_set.setBackgroundColor(Color.WHITE);
			int colorm = Integer.parseInt("FFFFFF", 16) + 0xFF000000;
			pending_invitations.setTextColor(colorm);
			five.setVisibility(View.VISIBLE);
			int colorss = Integer.parseInt("000000", 16) + 0xFF000000;
			facebook_invitations.setTextColor(colorss);
			accepted_invitations.setTextColor(colorm);
			break;
			
		case R.id.accepted_invitations:
			four.setVisibility(View.GONE);
			footer_set.setBackgroundColor(Color.WHITE);
			footer_view.setBackgroundColor(Color.WHITE);
			five.setVisibility(View.GONE);
			re_invite_selected.setVisibility(View.GONE);
			six.setVisibility(View.VISIBLE);
			accepted_invitations.setTextColor(Color.parseColor("#000000"));
			facebook_invitations.setTextColor(Color.parseColor("#FFFFFF"));
			pending_invitations.setTextColor(Color.parseColor("#FFFFFF"));
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
			overridePendingTransition(R.anim.push_out_to_right,
					R.anim.push_out_to_left);
			finish();
			break;

		case R.id.cart_btn:
			if (!(_getToken == null) && !(_getuserId == null)) {
				Intent _gotocart = new Intent(_ctx, MyCartScreen.class);
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

	// ******************************************************************************************************************//
	class SendReInvite extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(People_I_Invited.this, "Loading",
					true, true, this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int displayHeight = displaymetrics.heightPixels;
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams wmlp = mProgressHUD.getWindow()
					.getAttributes();
			wmlp.y = displayHeight / 4;
			mProgressHUD.getWindow().setAttributes(wmlp);
			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected String doInBackground(String... params) {
			String _url = "https://www.brandsfever.com/api/v5/invitations/";
			BasicNameValuePair _uid = new BasicNameValuePair("user_id",
					_getuserId);
			BasicNameValuePair _ut = new BasicNameValuePair("token", _getToken);
			BasicNameValuePair _apply_action = new BasicNameValuePair(
					"channel", "email");
			BasicNameValuePair _store_credits = new BasicNameValuePair(
					"invitee_identifiers", _reinviteEmail);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_uid);
			_namevalueList.add(_ut);
			_namevalueList.add(_apply_action);
			_namevalueList.add(_store_credits);
			_ResponseFromServer = SendData(_url, _namevalueList);
			Log.e("===RESPONSE====>", "===RESPONSE====>" + _ResponseFromServer);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject _ss = new JSONObject(_ResponseFromServer);
				String _ret = _ss.getString("ret");
				if (_ret.equalsIgnoreCase("0")) {
					LayoutInflater inflater = getLayoutInflater();
					View view = inflater.inflate(R.layout.error_popop,
							(ViewGroup) findViewById(R.id.pop));
					TextView _seterrormsg = (TextView) view
							.findViewById(R.id._seterrormsg);
					_seterrormsg.setText("Invite Sent Successfully!");
					Toast toast = new Toast(People_I_Invited.this);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(view);
					toast.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			mProgressHUD.dismiss();
		}

	}

	// *************************************************************************************************************************//
	public String SendData(String url, List<NameValuePair> _namevalueList) {
		String _Response = null;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpPost _httppost = new HttpPost(url);
		try {
			_httppost.setEntity(new UrlEncodedFormEntity(_namevalueList,
					HTTP.UTF_8));
			HttpResponse _httpresponse = _httpclient.execute(_httppost);
			int _responsecode = _httpresponse.getStatusLine().getStatusCode();
			Log.i("--------------Responsecode----------", "." + _responsecode);
			if (_responsecode == 200) {
				InputStream _inputstream = _httpresponse.getEntity()
						.getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						_inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				_Response = total.toString();
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				_Response = "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _Response;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

}
