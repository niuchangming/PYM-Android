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
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	CreditAdapter _adapter;
	String _ResponseFromServer;
	StoreCreditWindow _lctx;
	Typeface _font;;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.activity_dialog_fragment_window,
				container);
		_lctx = this;
		_font = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/georgia.ttf");
		cancel_dialouge = (Button) view.findViewById(R.id.cancel_dialouge);
		cancel_dialouge.setOnClickListener(this);
		store_credit_list = (ListView) view
				.findViewById(R.id.store_credit_list);

		_mypref = getActivity().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		_adapter = new CreditAdapter(getActivity(), PaymentActivity._storeCredits);
		store_credit_list.setAdapter(_adapter);

		store_credit_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View _view,
					int position, long arg3) {
				StoreCreditDetails _Sc = PaymentActivity._storeCredits
						.get(position);
				String getpk = _Sc.getPk();
				double getcreditamount = Double.parseDouble(_Sc.getAmount());
				DataHolderClass.getInstance().set_creditAmount(getcreditamount);
				DataHolderClass.getInstance().set_creditpk(getpk);
				new UseStoreCredit().execute();
				dismiss();
				startActivity(getActivity().getIntent());

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
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
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
			if (_Sc.getIs_redeemable().equals("true")) {
				_grantedby.setText(_Sc.getGranted_by());
				Double _tot = Double.valueOf(_Sc.getAmount());
				_amount.setText("S$" + String.valueOf(_tot) + "0");
				_validtill.setText(_Sc.getExpired_at());
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
			String _s = DataHolderClass.getInstance().get_creditpk();
			String _opk = DataHolderClass.getInstance().get_orderpk();
			String _url = "https://www.brandsfever.com/api/v5/orders/" + _opk
					+ "/discount/";
			String apply_action = "store_credit_apply";
			String store_credits = _s;
			BasicNameValuePair _uid = new BasicNameValuePair("user_id",
					_getuserId);
			BasicNameValuePair _ut = new BasicNameValuePair("token", _getToken);
			BasicNameValuePair _apply_action = new BasicNameValuePair(
					"apply_action", apply_action);
			BasicNameValuePair _store_credits = new BasicNameValuePair(
					"store_credits", store_credits);
			List<NameValuePair> _namevalueList = new ArrayList<NameValuePair>();
			_namevalueList.add(_uid);
			_namevalueList.add(_ut);
			_namevalueList.add(_apply_action);
			_namevalueList.add(_store_credits);
			_ResponseFromServer = SendData(_url, _namevalueList);
			return null;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
		}

		@Override
		protected void onPostExecute(String result) {
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
			} else {
				_Response = "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _Response;
	}

}
