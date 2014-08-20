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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class OrderDeliveryActiviy extends SherlockFragmentActivity implements
		OnClickListener {
	private static final String TAG = "OrderDeliveryActivity";
	CheckBox mCheckBillingAddress;
	LinearLayout mShippingAddressLayout;
	Context _ctx = OrderDeliveryActiviy.this;
	TextView title_tag, billing_addrress, sameas_billing_tag;
	Typeface mFont;
	Button send_to_this_address;
	SharedPreferences _mypref;
	String mToken = "";
	String mUserID = "";
	EditText first_name, last_name, address, zipcode, country, phone_nummber,
			bfirst_name, blast_name, baddress, bzipcode, bcountry,
			bphone_nummber;
	String pnumber = "", fname = "", lname = "", saddress = "", scountry = "",
			zcode = "";
	String _fname, _lname, _address, _zipcode, _country, _pnmber;
	String _ResponseFromServer;
	int color, colors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (DataHolderClass.getInstance().get_deviceInch() < 7) {
			setContentView(R.layout.activity_order_delivery__screen);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceInch() < 9) {
			setContentView(R.layout.seven_inch_order_delivery);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
			setContentView(R.layout.order_delivery_screen_tablet);
		}
		mCheckBillingAddress = (CheckBox) findViewById(R.id.checkBox1);
		mShippingAddressLayout = (LinearLayout) findViewById(R.id.four);

		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater()
				.inflate(R.layout.action_bar, null);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		final ImageButton actionBarMenu = (ImageButton) findViewById(R.id.action_bar_left);
		actionBarMenu.setImageDrawable(getResources().getDrawable(
				R.drawable.back_button));
		actionBarMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		final ImageButton actionBarCart = (ImageButton) findViewById(R.id.action_bar_right);
		actionBarCart.setImageDrawable(getResources().getDrawable(
				R.drawable.cart_btn_bg));
		actionBarCart.setVisibility(View.INVISIBLE);

		mFont = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		color = Integer.parseInt("8e1345", 16) + 0xFF000000;
		colors = Integer.parseInt("ffffff", 16) + 0xFF000000;
		title_tag = (TextView) findViewById(R.id.heading);
		billing_addrress = (TextView) findViewById(R.id.billing_addrress);
		billing_addrress.setTypeface(mFont, Typeface.BOLD);
		sameas_billing_tag = (TextView) findViewById(R.id.sameas_billing_tag);
		sameas_billing_tag.setTypeface(mFont, Typeface.BOLD);
		title_tag.setTypeface(mFont, Typeface.BOLD);
		mCheckBillingAddress.setTypeface(mFont, Typeface.BOLD);

		mCheckBillingAddress
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mShippingAddressLayout.setVisibility(View.GONE);
						} else {
							mShippingAddressLayout.setVisibility(View.VISIBLE);
						}

					}
				});

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		mUserID = _mypref.getString("ID", null);
		mToken = _mypref.getString("TOKEN", null);

		new GetShipingAddress().execute();

		first_name = (EditText) findViewById(R.id.first_name);
		last_name = (EditText) findViewById(R.id.last_name);
		address = (EditText) findViewById(R.id.address);
		zipcode = (EditText) findViewById(R.id.zipcode);
		country = (EditText) findViewById(R.id.country);
		phone_nummber = (EditText) findViewById(R.id.phone_nummber);

		first_name.setTypeface(mFont, Typeface.NORMAL);
		last_name.setTypeface(mFont, Typeface.NORMAL);
		address.setTypeface(mFont, Typeface.NORMAL);
		zipcode.setTypeface(mFont, Typeface.NORMAL);
		country.setTypeface(mFont, Typeface.NORMAL);
		phone_nummber.setTypeface(mFont, Typeface.NORMAL);

		bfirst_name = (EditText) findViewById(R.id.bfirst_name);
		blast_name = (EditText) findViewById(R.id.blast_name);
		baddress = (EditText) findViewById(R.id.baddress);
		bzipcode = (EditText) findViewById(R.id.bzipcode);
		bcountry = (EditText) findViewById(R.id.bcountry);
		bphone_nummber = (EditText) findViewById(R.id.bphone_nummber);

		bfirst_name.setTypeface(mFont, Typeface.NORMAL);
		blast_name.setTypeface(mFont, Typeface.NORMAL);
		baddress.setTypeface(mFont, Typeface.NORMAL);
		bzipcode.setTypeface(mFont, Typeface.NORMAL);
		bcountry.setTypeface(mFont, Typeface.NORMAL);
		bphone_nummber.setTypeface(mFont, Typeface.NORMAL);

		send_to_this_address = (Button) findViewById(R.id.send_to_this_address);
		send_to_this_address.setOnClickListener(this);
		send_to_this_address.setTypeface(mFont, Typeface.NORMAL);
	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": shipping-address/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.send_to_this_address:
			_fname = first_name.getText().toString();
			_lname = last_name.getText().toString();
			_address = address.getText().toString();
			_zipcode = zipcode.getText().toString();
			_country = country.getText().toString();
			_pnmber = phone_nummber.getText().toString();
			if (_fname.length() > 0 && !(_fname.equalsIgnoreCase(""))
					&& !(_fname.equalsIgnoreCase(null)) && _lname.length() > 0
					&& !(_lname.equalsIgnoreCase(""))
					&& !(_lname.equalsIgnoreCase(null))
					&& _address.length() > 0
					&& !(_address.equalsIgnoreCase(""))
					&& !(_address.equalsIgnoreCase(null))
					&& _zipcode.length() > 0
					&& !(_zipcode.equalsIgnoreCase(""))
					&& !(_zipcode.equalsIgnoreCase(null))
					&& _country.length() > 0
					&& !(_country.equalsIgnoreCase(""))
					&& !(_country.equalsIgnoreCase(null))
					&& _pnmber.length() > 0 && !(_pnmber.equalsIgnoreCase(""))
					&& !(_pnmber.equalsIgnoreCase(null))) {

				String _sendnext = _fname + "" + _lname + "\n" + _address
						+ "\n" + _zipcode + "," + _country + "\n"
						+ "Phone:(+65)" + _pnmber;
				DataHolderClass.getInstance().setBill_ship_address(_sendnext);

				new CreateOrderOfUser().execute();
			} else if (_fname.length() == 0) {
				responsePopup("Please enter\nfirst name!");
			} else if (_lname.length() == 0) {
				responsePopup("Please enter\nlast name");
			} else if (_address.length() == 0) {
				responsePopup("Please enter address!");
			} else if (_zipcode.length() == 0) {
				responsePopup("Please enter zipcode!");
			} else if (_country.length() == 0) {
				responsePopup("Please enter\ncountry name!");
			} else if (_pnmber.length() == 0) {
				responsePopup("Please enter\ncontact number!");
			}
			break;
		default:
			break;
		}

	}

	class GetShipingAddress extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(OrderDeliveryActiviy.this,
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
		protected String doInBackground(String... params) {
			String url = "https://www.brandsfever.com/api/v5/shipping-address/?user_id="
					+ mUserID + "&token=" + mToken;
			GetAddress(url);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected void onPostExecute(String result) {
			if (!(fname.equalsIgnoreCase("null")) && fname.length() > 0) {
				first_name.setText(fname);
				bfirst_name.setText(fname);
			} else {
				first_name.setText("");
				bfirst_name.setText("");
			}
			if (!(lname.equalsIgnoreCase("null")) && lname.length() > 0) {
				last_name.setText(lname);
				blast_name.setText(lname);
			} else {
				last_name.setText("");
				blast_name.setText("");
			}
			if (!(saddress.equalsIgnoreCase("null")) && saddress.length() > 0) {
				address.setText(saddress);
				baddress.setText(saddress);
			} else {
				address.setText("");
				baddress.setText("");
			}
			if (!(zcode.equalsIgnoreCase("null")) && zcode.length() > 0) {
				zipcode.setText(zcode);
				bzipcode.setText(zcode);
			} else {
				zipcode.setText("");
				bzipcode.setText("");
			}
			if (!(scountry.equalsIgnoreCase("null")) && scountry.length() > 0) {
				country.setText(scountry);
				bcountry.setText(scountry);
			} else {
				country.setText("");
				bcountry.setText("");
			}
			if (!(pnumber.equalsIgnoreCase("null")) && pnumber.length() > 0) {
				phone_nummber.setText(pnumber);
				bphone_nummber.setText(pnumber);
			} else {
				phone_nummber.setText("");
				bphone_nummber.setText("");
			}

			mProgressHUD.dismiss();

		}
	}

	private void GetAddress(String url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpclient = HttpsClient.getNewHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			int responsecode = httpresponse.getStatusLine().getStatusCode();

			if (responsecode == 200) {
				InputStream inputstream = httpresponse.getEntity().getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				String content = total.toString();
				JSONObject jobj = new JSONObject(content);
				String ret = jobj.getString("ret");
				if (ret.equals("0")) {
					pnumber = jobj.getString("phone_number");
					fname = jobj.getString("first_name");
					lname = jobj.getString("last_name");
					saddress = jobj.getString("address");
					scountry = jobj.getString("country");
					zcode = jobj.getString("zipcode");

					String sendnext = fname + "" + lname + "\n" + saddress
							+ "\n" + zcode + "," + scountry + "\n"
							+ "Phone:(+65)" + pnumber;
					DataHolderClass.getInstance()
							.setBill_ship_address(sendnext);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CreateOrderOfUser extends AsyncTask<String, String, String>
			implements OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(OrderDeliveryActiviy.this,
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
		public void onCancel(DialogInterface dialog) {
		}

		@Override
		protected String doInBackground(String... params) {
			String createCartUrl = "https://www.brandsfever.com/api/v5/orders/";
			String sendCartpk = DataHolderClass.getInstance()
					.getFinal_cart_pk();
			BasicNameValuePair senduser_id = new BasicNameValuePair("user_id",
					mUserID);
			BasicNameValuePair sendtoken = new BasicNameValuePair("token",
					mToken);
			BasicNameValuePair sendcart_pk = new BasicNameValuePair("cart_pk",
					sendCartpk);
			BasicNameValuePair sendshipping_first_name = new BasicNameValuePair(
					"shipping-first_name", _fname);
			BasicNameValuePair sendshipping_last_name = new BasicNameValuePair(
					"shipping-last_name", _lname);
			BasicNameValuePair sendshipping_address = new BasicNameValuePair(
					"shipping-address", _address);
			BasicNameValuePair sendshipping_zipcode = new BasicNameValuePair(
					"shipping-zipcode", _zipcode);
			BasicNameValuePair sendshipping_phone_number = new BasicNameValuePair(
					"shipping-phone_number", _pnmber);
			BasicNameValuePair sendshipping_district = new BasicNameValuePair(
					"shipping-district", _country);
			BasicNameValuePair sendshipping_country = new BasicNameValuePair(
					"shipping-country", _country);
			BasicNameValuePair sendbilling_first_name = new BasicNameValuePair(
					"billing-first_name", _fname);
			BasicNameValuePair sendbilling_last_name = new BasicNameValuePair(
					"billing-last_name", _lname);
			BasicNameValuePair sendbilling_address = new BasicNameValuePair(
					"billing-address", _address);
			BasicNameValuePair sendbilling_zipcode = new BasicNameValuePair(
					"billing-zipcode", _zipcode);
			BasicNameValuePair sendbilling_phone_number = new BasicNameValuePair(
					"billing-phone_number", _pnmber);
			BasicNameValuePair sendbilling_district = new BasicNameValuePair(
					"billing-district", _country);
			BasicNameValuePair sendbilling_country = new BasicNameValuePair(
					"billing-country", _country);
			List<NameValuePair> namevalueList = new ArrayList<NameValuePair>();
			namevalueList.add(senduser_id);
			namevalueList.add(sendtoken);
			namevalueList.add(sendcart_pk);
			namevalueList.add(sendshipping_first_name);
			namevalueList.add(sendshipping_last_name);
			namevalueList.add(sendshipping_address);
			namevalueList.add(sendshipping_zipcode);
			namevalueList.add(sendshipping_phone_number);
			namevalueList.add(sendshipping_district);
			namevalueList.add(sendshipping_country);
			namevalueList.add(sendbilling_first_name);
			namevalueList.add(sendbilling_last_name);
			namevalueList.add(sendbilling_address);
			namevalueList.add(sendbilling_zipcode);
			namevalueList.add(sendbilling_phone_number);
			namevalueList.add(sendbilling_district);
			namevalueList.add(sendbilling_country);
			_ResponseFromServer = SendData(createCartUrl, namevalueList);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject _obj = new JSONObject(_ResponseFromServer);
				String _ret = _obj.getString("ret");
				if (_ret.equalsIgnoreCase("0")) {
					JSONObject obj = _obj.getJSONObject("data");
					String _orderPK = obj.getString("order_pk");
					DataHolderClass.getInstance().set_orderpk(_orderPK);
					Intent _sendforpayment = new Intent(_ctx,
							PaymentActivity.class);
					startActivity(_sendforpayment);
					overridePendingTransition(R.anim.push_out_to_right,
							R.anim.push_out_to_left);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressHUD.dismiss();
		}
	}

	public String SendData(String url, List<NameValuePair> namevalueList) {
		String response = null;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpclient = HttpsClient.getNewHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(namevalueList,
					HTTP.UTF_8));
			HttpResponse httpresponse = httpclient.execute(httppost);
			int responsecode = httpresponse.getStatusLine().getStatusCode();

			if (responsecode == 200) {
				InputStream inputstream = httpresponse.getEntity()
						.getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				response = total.toString();
			} else {
				response = "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public void responsePopup(String msg) {
		View view = View.inflate(OrderDeliveryActiviy.this,
				R.layout.error_popop, null);
		TextView msgView = (TextView) view.findViewById(R.id._seterrormsg);
		msgView.setText(msg);
		Toast toast = new Toast(OrderDeliveryActiviy.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
