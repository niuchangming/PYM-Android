package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dataholder.DataHolderClass;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class SendInviteActiviy extends SherlockFragmentActivity implements
		OnItemClickListener, OnClickListener {
	private static final String TAG = "SendInviteScreen";
	ListView _getemails;
	Context _ctx = SendInviteActiviy.this;
	ArrayList<String> _storeEmails = new ArrayList<String>();
	Typeface _font;
	private String _email;
	String _ResponseFromServer;
	Button cancel_invite;
	Button proceed_to_invite;
	private PopupWindow _invitewindow;
	String _retValue;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	int color, colors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			setContentView(R.layout.phone_send_invite_screen);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_send_invite);
		} else if (a >= 9) {
			setContentView(R.layout.ten_inch_send_invite);
		}
		_getemails = (ListView) findViewById(R.id.get_email_list);
		_getemails.setOnItemClickListener(this);

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
				finish();
			}
		});
		final ImageButton actionBarCart = (ImageButton) findViewById(R.id.action_bar_right);
		actionBarCart.setImageDrawable(getResources().getDrawable(
				R.drawable.cart_btn_bg));
		actionBarCart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i(TAG, "Cart is clicked");
			}
		});

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);
		try {
			new GetContactsFromDevice().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": invitations/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	class GetContactsFromDevice extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(SendInviteActiviy.this, "Loading",
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
			try {
				ContentResolver cr = getContentResolver();
				Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
						null, null, null, null);
				if (cur.getCount() > 0) {
					while (cur.moveToNext()) {
						String name = cur
								.getString(cur
										.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
						if (Integer
								.parseInt(cur.getString(cur
										.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
						}
						_storeEmails.add(name);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					SendInviteActiviy.this, android.R.layout.simple_list_item_1,
					_storeEmails);
			_getemails.setAdapter(arrayAdapter);
			mProgressHUD.dismiss();
			super.onPostExecute(result);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View _view, int position,
			long arg3) {
		_email = _storeEmails.get(position);
		_InitializePopup();
	}

	public void _InitializePopup() {

		try {
			LayoutInflater inflater = (LayoutInflater) SendInviteActiviy.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.send_invite_popup,
					(ViewGroup) findViewById(R.id.invite_popup));
			_invitewindow = new PopupWindow(layout, 500, 220, true);
			_invitewindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

			_invitewindow.isOutsideTouchable();
			_invitewindow.setOutsideTouchable(true);

			TextView _text = (TextView) layout.findViewById(R.id.invite_email);
			_text.setText(_email);
			cancel_invite = (Button) layout.findViewById(R.id.cancel_invite);
			cancel_invite.setOnClickListener(this);
			proceed_to_invite = (Button) layout
					.findViewById(R.id.proceed_invite);
			proceed_to_invite.setOnClickListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class SendEmailInvite extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(SendInviteActiviy.this, "Loading",
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
					"invitee_identifiers", _email);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_uid);
			_namevalueList.add(_ut);
			_namevalueList.add(_apply_action);
			_namevalueList.add(_store_credits);
			_ResponseFromServer = SendData(_url, _namevalueList);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject obj = new JSONObject(_ResponseFromServer);
				_retValue = obj.getString("ret");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (_retValue.equals("0") || _retValue.equals("502")) {
				_invitewindow.dismiss();
				_ResponsePopup();
				Intent _sucess = new Intent(SendInviteActiviy.this,
						InviteFragment.class);
				startActivity(_sucess);
				finish();
			}
			mProgressHUD.dismiss();
		}

	}

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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_invite:
			_invitewindow.dismiss();
			break;
		case R.id.proceed_invite:
			new SendEmailInvite().execute();
			break;
		default:
			break;
		}

	}

	public void _ResponsePopup() {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.error_popop,
				(ViewGroup) findViewById(R.id.pop));
		TextView _seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText("Invite Send Successfully!");
		Toast toast = new Toast(SendInviteActiviy.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(SendInviteActiviy.this, InviteFragment.class);
		startActivity(i);
	}

}
