package com.brandsfever;

import java.io.BufferedReader;
import java.io.IOException;
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

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dataholder.DataHolderClass;
import com.datamodel.StoreCreditDetails;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class StoreCreditWindow extends DialogFragment implements
		OnClickListener {
	ListView store_credit_list;
	Button cancel_dialouge;
	SharedPreferences mPref;
	String mToken = "";
	String mUserID = "";
	CreditAdapter mAdapter;
	String _ResponseFromServer;
	StoreCreditWindow _lctx;
	Typeface mFont;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.activity_dialog_fragment_window,
				container);
		_lctx = this;
		mFont = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/georgia.ttf");
		cancel_dialouge = (Button) view.findViewById(R.id.cancel_dialouge);
		cancel_dialouge.setOnClickListener(this);
		store_credit_list = (ListView) view
				.findViewById(R.id.store_credit_list);

		mPref = getActivity().getSharedPreferences("mypref", 0);
		mUserID = mPref.getString("ID", null);
		mToken = mPref.getString("TOKEN", null);

		mAdapter = new CreditAdapter(getActivity(),
				PaymentActivity._storeCredits);
		store_credit_list.setAdapter(mAdapter);

		store_credit_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View _view,
					int position, long arg3) {
				StoreCreditDetails storeCredits = PaymentActivity._storeCredits
						.get(position);
				String getpk = storeCredits.getPk();
				double getcreditamount = Double.parseDouble(storeCredits.getAmount());
				DataHolderClass.getInstance().set_creditAmount(getcreditamount);
				DataHolderClass.getInstance().set_creditpk(getpk);
				new UseStoreCredit().execute();
				dismiss();
				PaymentActivity activity = (PaymentActivity) getActivity();
				if(activity != null){
					activity.getOrderList();
				}
			}
		});

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_dialouge:
			dismiss();
			break;

		default:
			break;
		}

	}

	private class CreditAdapter extends BaseAdapter {
		private Context mContext;
		LayoutInflater inflater;
		public ArrayList<StoreCreditDetails> mData;
		View itemView;

		public CreditAdapter(Context c, ArrayList<StoreCreditDetails> _arraylist) {
			mContext = c;
			this.mData = _arraylist;
		}

		@Override
		public int getCount() {
			return mData.size();
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
			TextView grantedby, amount, validtill;

			if (DataHolderClass.getInstance().get_deviceInch() < 7) {
				inflater = (LayoutInflater) mContext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(
						R.layout.store_credit_inflatorphone, parent, false);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
				inflater = (LayoutInflater) mContext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(R.layout.store_credit_tab, parent,
						false);
			}

			grantedby = (TextView) itemView.findViewById(R.id.Set_Granted_by);
			grantedby.setTypeface(mFont);
			amount = (TextView) itemView.findViewById(R.id.set_amount);
			amount.setTypeface(mFont);
			validtill = (TextView) itemView.findViewById(R.id.set_validity);
			validtill.setTypeface(mFont);

			StoreCreditDetails _Sc = mData.get(position);
			if (_Sc.getIs_redeemable().equals("true")) {
				grantedby.setText(_Sc.getGranted_by());
				Double _tot = Double.valueOf(_Sc.getAmount());
				amount.setText("S$" + String.valueOf(_tot) + "0");
				validtill.setText(_Sc.getExpired_at());
			}

			return itemView;
		}

	}

	class UseStoreCredit extends AsyncTask<String, String, String> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(getActivity(), "Loading", true,
					true, this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getActivity().getWindowManager().getDefaultDisplay()
					.getMetrics(displaymetrics);
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
			String s = DataHolderClass.getInstance().get_creditpk();
			String opk = DataHolderClass.getInstance().get_orderpk();
			String url = "https://www.brandsfever.com/api/v5/orders/" + opk
					+ "/discount/";
			String apply_action = "store_credit_apply";
			String store_credits = s;
			BasicNameValuePair uidPair = new BasicNameValuePair("user_id",
					mUserID);
			BasicNameValuePair tokenPair = new BasicNameValuePair("token", mToken);
			BasicNameValuePair actionPair = new BasicNameValuePair(
					"apply_action", apply_action);
			BasicNameValuePair creditsPair = new BasicNameValuePair(
					"store_credits", store_credits);
			List<NameValuePair> namevalueList = new ArrayList<NameValuePair>();
			namevalueList.add(uidPair);
			namevalueList.add(tokenPair);
			namevalueList.add(actionPair);
			namevalueList.add(creditsPair);
			_ResponseFromServer = SendData(url, namevalueList);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
		}

		@Override
		protected void onPostExecute(String result) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
