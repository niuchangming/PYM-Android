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

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class SupportFragment extends Fragment {

	private static final String TAG = "SupportFragment";
	Button mSubmit;
	Typeface mFont;

	private EditText mOrderId;
	private EditText mIssue;
	private EditText mDetail;
	private EditText mEmail;
	private LinearLayout mView;

	public static SupportFragment newInstance() {
		SupportFragment fragment = new SupportFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {

			mView = (LinearLayout) inflater.inflate(R.layout.fragment_support,
					container, false);

			mFont = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/georgia.ttf");
			TextView title = (TextView) mView.findViewById(R.id.support_title);
			title.setTypeface(mFont, Typeface.ITALIC);

			mOrderId = (EditText) mView.findViewById(R.id.order_id);
			mIssue = (EditText) mView.findViewById(R.id.issue);
			mDetail = (EditText) mView.findViewById(R.id.detail);
			mEmail = (EditText) mView.findViewById(R.id.email);

			mSubmit = (Button) mView.findViewById(R.id.support_submit);
			mSubmit.setTypeface(mFont);
			mSubmit.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (mOrderId.getText().toString().length() < 1) {
						showMessage("Please input your Order Identifier.");
					} else if (mIssue.getText().toString().length() < 1) {
						showMessage("Please input your issue.");
					} else if (!isValidEmail(mEmail.getText().toString())) {
						showMessage("Please input a valid Email address.");
					} else {
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
								4);
						nameValuePairs.add(new BasicNameValuePair(
								"order_number", mOrderId.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("issue",
								mIssue.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("detail",
								mDetail.getText().toString()));
						nameValuePairs.add(new BasicNameValuePair("email",
								mEmail.getText().toString()));

						SubmitSupport submit = new SubmitSupport();
						submit.execute(nameValuePairs);
					}
				}
			});
		}
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((ViewGroup) mView.getParent()).removeView(mView);
	}

	class SubmitSupport extends AsyncTask<List<NameValuePair>, String, String>
			implements OnCancelListener {

		private ProgressHUD mProgressHUD;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(getActivity(), "Loading", true,
					true, this);

		}

		@Override
		public void onCancel(DialogInterface dialog) {

		}

		@Override
		protected String doInBackground(List<NameValuePair>... params) {
			String supportUrl = "https://www.brandsfever.com/api/v5/customer-support/";
			String result = postData(supportUrl, params[0]);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mProgressHUD.dismiss();
			showMessage(result);
		}

	}

	private String postData(String url, List<NameValuePair> nameValuePairs) {
		String response = null;

		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpClient = HttpsClient.getNewHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			if (responseCode == 200) {
				InputStream inputStream = httpResponse.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					total.append(line);
				}

				JSONObject responseObject = new JSONObject(total.toString());
				String responseMsg = responseObject.getString("msg");
				if (responseMsg.equalsIgnoreCase("OK")) {
					response = "Thanks for your feedback. Happy shopping with Brandsfever:)";
				} else {
					response = "Your Order Identifier is invalid.Please check and try again.";
				}
			} else {
				response = "Please try again later.";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	private void showMessage(String message) {
		View view = View.inflate(getActivity(), R.layout.error_popop, null);
		TextView msg = (TextView) view.findViewById(R.id._seterrormsg);
		msg.setText(message);
		Toast toast = new Toast(getActivity());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
						.matches();
	}
}
