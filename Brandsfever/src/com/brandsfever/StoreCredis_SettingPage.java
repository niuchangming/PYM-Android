package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dataholder.DataHolderClass;
import com.datamodel.StoreCreditDetails;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class StoreCredis_SettingPage extends SherlockFragmentActivity {
	Typeface _font;
	ListView set_store_credits, set_store_credits_dummy;
	TextView store_credit_tag, granted_by_tag, amount_tag, valid_till_tag;
	public static ArrayList<StoreCreditDetails> _storeCredits = new ArrayList<StoreCreditDetails>();
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	CreditAdapter _adapter;
	int color, colors;

	static final String[] FRUITS = new String[] { "", "", "", "", "", "", "",
			"", "", "", "", "", "" };
	ArrayAdapter<String> codeLearnArrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			setContentView(R.layout.activity_store_credis__setting_page);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceHeight() < 9) {
			setContentView(R.layout.seven_inch_store_credits);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
			setContentView(R.layout.activity_see_my_store_credits_settings);
		}

		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
				.inflate(R.layout.action_bar, null);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		final ImageButton actionBarLeft = (ImageButton) findViewById(R.id.action_bar_left);
		actionBarLeft.setImageDrawable(getResources().getDrawable(
				R.drawable.back_button));
		actionBarLeft.setOnClickListener(new View.OnClickListener() {

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
				directToCart();
			}
		});

		_font = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		store_credit_tag = (TextView) findViewById(R.id.store_credit_tag);
		granted_by_tag = (TextView) findViewById(R.id.granted_by_tag);
		amount_tag = (TextView) findViewById(R.id.amount_tag);
		valid_till_tag = (TextView) findViewById(R.id.valid_till_tag);
		set_store_credits = (ListView) findViewById(R.id.set_store_credits);
		set_store_credits_dummy = (ListView) findViewById(R.id.set_store_credits_dummy);

		codeLearnArrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, FRUITS);

		store_credit_tag.setTypeface(_font);
		granted_by_tag.setTypeface(_font);
		amount_tag.setTypeface(_font);
		valid_till_tag.setTypeface(_font);

		try {
			new GetStoreCredit().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": storecredits/?user_id=" + _getuserId + "/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	private void directToCart() {

		SharedPreferences mypref = getApplicationContext()
				.getSharedPreferences("mypref", 0);
		String username = mypref.getString("UserName", null);
		
		if (username != null) { // check login status
			Intent gotocart = new Intent(StoreCredis_SettingPage.this, MyCartActivity.class);
			startActivity(gotocart);
		} else {
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.error_popop,
					(ViewGroup) findViewById(R.id.relativeLayout1));
			final TextView msgTextView = (TextView) view
					.findViewById(R.id._seterrormsg);
			msgTextView.setText("Please login!");
			Toast toast = new Toast(StoreCredis_SettingPage.this);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.setView(view);
			toast.show();
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	class GetStoreCredit extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(StoreCredis_SettingPage.this,
					"Loading", true, true, this);
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
		public void onCancel(DialogInterface arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected String doInBackground(String... params) {
			_storeCredits.clear();
			String _url = "https://www.brandsfever.com/api/v5/storecredits/?user_id="
					+ _getuserId + "&token=" + _getToken;
			Get_Store_Credits(_url);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			_adapter = new CreditAdapter(StoreCredis_SettingPage.this,
					_storeCredits);
			if (_storeCredits.size() < 1) {
				set_store_credits_dummy.setVisibility(0);
				set_store_credits_dummy.setAdapter(codeLearnArrayAdapter);
				mProgressHUD.dismiss();
				View view = View.inflate(getBaseContext(),
						R.layout.error_popop, null);
				TextView _seterrormsg = (TextView) view
						.findViewById(R.id._seterrormsg);
				_seterrormsg.setText("No store credit in your \n account");
				Toast toast = new Toast(getBaseContext());
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(view);
				toast.show();

			} else {
				set_store_credits.setVisibility(0);
				set_store_credits.setAdapter(_adapter);
				mProgressHUD.dismiss();
			}

		}
	}

	public void Get_Store_Credits(String url_get_store_credits) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient _httpclient = HttpsClient.getNewHttpClient();
		HttpGet _httpget = new HttpGet(url_get_store_credits);
		try {
			HttpResponse _httpresponse = _httpclient.execute(_httpget);
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
				String G_P = total.toString();
				try {
					JSONObject obj = new JSONObject(G_P);
					String ret = obj.getString("ret");
					if (ret.equals("0")) {
						JSONArray get_credit_details = obj
								.getJSONArray("store_credits");
						for (int i = 0; i < get_credit_details.length(); i++) {
							JSONObject jsonobj = get_credit_details
									.getJSONObject(i);
							String one = jsonobj.getString("granted_by");
							String two = jsonobj.getString("expired_at");
							String three = jsonobj.getString("amount");
							String four = jsonobj.getString("redeemed_order");
							String five = jsonobj.getString("is_redeemable");
							String six = jsonobj.getString("pk");
							String seven = jsonobj.getString("redeemed_at");
							String eight = jsonobj.getString("state");
							String nine = jsonobj.getString("channel");

							StoreCreditDetails _credits = new StoreCreditDetails();
							_credits.setGranted_by(one);
							_credits.setExpired_at(two);
							_credits.setAmount(three);
							_credits.setChannel(nine);
							_credits.setIs_redeemable(five);
							_credits.setPk(six);
							_credits.setState(eight);
							_credits.setRedeemed_at(seven);
							_credits.setRedeemed_order(four);

							_storeCredits.add(_credits);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CreditAdapter extends BaseAdapter {
		private Context mContext;
		LayoutInflater inflater;
		public ArrayList<StoreCreditDetails> _data;
		View itemView;

		public CreditAdapter(Context c, ArrayList<StoreCreditDetails> _arraylist) {
			mContext = c;
			this._data = _arraylist;
		}

		@Override
		public int getCount() {
			return _data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView _grantedby, _amount, _validtill;

			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				inflater = (LayoutInflater) mContext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(
						R.layout.store_credit_inflatorphone, parent, false);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
					&& DataHolderClass.getInstance().get_deviceHeight() < 9) {
				inflater = (LayoutInflater) mContext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(
						R.layout.seven_inch_credit_inflator, parent, false);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
				inflater = (LayoutInflater) mContext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.store_credit_tab, parent,
						false);
			}

			_grantedby = (TextView) itemView.findViewById(R.id.Set_Granted_by);
			_grantedby.setTypeface(_font);
			_amount = (TextView) itemView.findViewById(R.id.set_amount);
			_amount.setTypeface(_font);
			_validtill = (TextView) itemView.findViewById(R.id.set_validity);
			_validtill.setTypeface(_font);

			StoreCreditDetails _Sc = _data.get(position);
			_grantedby.setText(_Sc.getGranted_by());
			Double _amt = Double.valueOf(_Sc.getAmount());
			_amount.setText("S$" + _amt + "0");
			_validtill.setText(_Sc.getExpired_at());

			return itemView;
		}

	}
}
