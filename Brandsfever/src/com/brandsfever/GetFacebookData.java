package com.brandsfever;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.service.textservice.SpellCheckerService.Session;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.datamodel.faceclass;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

public class GetFacebookData extends Activity implements OnClickListener {
	ListView listview, listview2;

	SubCategoryCustomAdapter12345yu objcustomadapter;
	Facebook facebook;
	public ArrayList<faceclass> faceclassobj = new ArrayList<faceclass>();
	EditText editText1;
	protected String[] stlist1;
	protected String[] stlist2;
	protected int search;
	Session ss;
	public String tokenvalue = "", useridvalue = "";
	Typeface tf;
	String _fbid;
	HashMap<String, String> takensession;
	ImageButton post_to_wall, cancel_post;
	SharedPreferences _mypref;
	String _getToken = "";
	String _getuserId = "";
	String _msg;

	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);

	}

	public final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			try {

				SessionStore.save(facebook, GetFacebookData.this);
				facebook();
			} catch (Exception error) {
				error.printStackTrace();

			}

		}

		public void onCancel() {

		}

		@Override
		public void onFacebookError(FacebookError e) {

		}

		@Override
		public void onError(DialogError e) {

		}

	}

	public void facebook() {

		AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(GetFacebookData.this);

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setTitle("Getting Facebook Information");
				dialog.setCancelable(false);
				dialog.setIndeterminate(true);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				JSONObject jsonObj = null;
				try {

					try {

						try {
							jsonObj = Util.parseJson(facebook
									.request("me/friends"));
						} catch (FacebookError e) {
							e.printStackTrace();
						}

						JSONArray jArray = jsonObj.getJSONArray("data");
						for (int k = 0; k < jArray.length(); k++) {
							JSONObject obj1 = jArray.getJSONObject(k);
							faceclass obj = new faceclass();

							obj.setId(obj1.getString("id"));
							obj.setName(obj1.getString("name"));
							faceclassobj.add(obj);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				dialog.dismiss();
				objcustomadapter = new SubCategoryCustomAdapter12345yu(
						GetFacebookData.this, R.layout.listcategory,
						faceclassobj);
				listview.setAdapter(objcustomadapter);
			}

		};
		updateTask.execute((Void[]) null);
	}

	private void post() {
		AsyncTask<Void, Void, Void> updateTask1 = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(GetFacebookData.this);

			@Override
			protected void onPreExecute() {
				dialog.setTitle("Checking you on BrandsFever");
				dialog.setCancelable(false);
				dialog.setIndeterminate(true);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				dialog.dismiss();
			}
		};
		updateTask1.execute((Void[]) null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_get_facebook_data);
		listview = (ListView) findViewById(R.id.listView1);
		listview2 = (ListView) findViewById(R.id.listView2);
		editText1 = (EditText) findViewById(R.id.editText1);
		editText1.setCursorVisible(true);

		_mypref = getApplicationContext().getSharedPreferences("mypref", 0);
		_getuserId = _mypref.getString("ID", null);
		_getToken = _mypref.getString("TOKEN", null);

		post_to_wall = (ImageButton) findViewById(R.id.post_to_wall);
		post_to_wall.setOnClickListener(this);
		cancel_post = (ImageButton) findViewById(R.id.cancel_post);
		cancel_post.setOnClickListener(this);

		tf = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		try {
			facebook = new Facebook(getResources().getString(R.string.fbid));
			SessionStore.restore(facebook, this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!facebook.isSessionValid()) {
			Toast.makeText(getApplicationContext(),
					"Redirecting you on facebook", Toast.LENGTH_SHORT).show();
			facebook.authorize(GetFacebookData.this, new String[] {
					"publish_stream", "email", "user_groups", "read_stream",
					"user_about_me", "offline_access" }, 1,
					new LoginDialogListener());

		} else {

			facebook();
		}
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});
		editText1.addTextChangedListener(new TextWatcher() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				editText1.setCursorVisible(true);
				String st = editText1.getText().toString();

				int j = 0;
				int size = faceclassobj.size();
				stlist1 = new String[size];
				stlist2 = new String[size];

				for (int i = 0; i < size; i++) {
					faceclass obj = faceclassobj.get(i);

					editText1.setSelection(editText1.getText().length());
					if (obj.getName().toLowerCase()
							.startsWith(st.toLowerCase())) {
						stlist1[j] = obj.getId();
						stlist2[j] = obj.getName();
						j++;
					}
				}
				search = j;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				editText1.setCursorVisible(false);
				MyCustomAdapter1 adapter = new MyCustomAdapter1();
				listview2.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
				listview2.setAdapter(adapter);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)
				+ ": facebook/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}

	class MyCustomAdapter1 extends BaseAdapter {
		Context c;
		ArrayList<String> img, ttl;

		MyCustomAdapter1() {

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = GetFacebookData.this.getLayoutInflater();
			View row = inflater.inflate(R.layout.listcategory, parent, false);
			TextView t1 = (TextView) row.findViewById(R.id.textView1);
			t1.setTypeface(tf);
			ImageView image = (ImageView) row.findViewById(R.id.imageView1);
			String ss = stlist1[position];
			AQuery aq = new AQuery(getApplicationContext());
			t1.setText(stlist2[position]);
			aq.id(image).image("http://graph.facebook.com/" + ss + "/picture");

			return row;
		}

		@Override
		public int getCount() {
			return search;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	class SubCategoryCustomAdapter12345yu extends ArrayAdapter<faceclass> {

		Context context;
		int layoutResourceId;
		public ArrayList<faceclass> obj_Notification;

		public SubCategoryCustomAdapter12345yu(Context context,
				int textViewResourceId, ArrayList<faceclass> objects) {
			super(context, textViewResourceId, objects);
			this.layoutResourceId = textViewResourceId;
			this.context = context;
			this.obj_Notification = objects;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;

			NotificationjavanHolder holder = null;
			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);

				holder = new NotificationjavanHolder();
				holder.imageview1 = (ImageView) row
						.findViewById(R.id.imageView1);
				holder.checkbox = (CheckBox) row.findViewById(R.id.imageView2);
				holder.name = (TextView) row.findViewById(R.id.textView1);
				holder.name.setTypeface(tf);
				row.setTag(holder);
			} else {
				holder = (NotificationjavanHolder) row.getTag();
			}

			faceclass obj = obj_Notification.get(position);
			holder.name.setText(obj.getName());
			holder.name.setTag(position);
			holder.checkbox.setTag(position);
			holder.checkbox
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							int getPosition = (Integer) buttonView.getTag();
							_fbid = faceclassobj.get(getPosition).getId();
						}
					});
			holder.name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					int a = (Integer) arg0.getTag();
					faceclass obj = faceclassobj.get(a);
					postToWall(obj.getId());
					new sendinvitaions().execute();
				}
			});
			try {
				AQuery aq = new AQuery(getApplicationContext());
				aq.id(holder.imageview1)
						.image("http://graph.facebook.com/" + obj.getId()
								+ "/picture");
			} catch (OutOfMemoryError e) {

				e.printStackTrace();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return row;
		}

		class NotificationjavanHolder {
			ImageView imageview1;
			CheckBox checkbox;
			TextView name;
		}
	}

	public void postToWall(final String userid) {
		View post = null;
		LayoutInflater li = LayoutInflater.from(getApplicationContext());
		post = li.inflate(R.layout.sharedialog, null);
		final TextView t1;
		t1 = (TextView) post.findViewById(R.id.textView0);
		t1.setText("Messege:Brandsfever");
		ImageView back = (ImageView) post.findViewById(R.id.imageView1);
		final Dialog mDialog = new Dialog(GetFacebookData.this,
				android.R.style.Theme_Translucent_NoTitleBar);

		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(post);
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(facebook);
		Bundle params = new Bundle();
		params.putString("message", "Hi, I am using Android BrandsfeverApp\n\n"
				+ "https://www.brandsfever.com/");

		mAsyncFbRunner.request(((userid == null) ? "me" : _fbid) + "/feed",
				params, "POST", new WallPostListener());
		back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
	}

	private final class WallPostListener extends BaseRequestListener {
		private Handler mRunOnUi = new Handler();

		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {
				public void run() {
					new sendinvitaions().execute();
				}
			});
		}

		public void onIOException(IOException e) {
		}

		public void onFileNotFoundException(FileNotFoundException e) {
		}

		public void onMalformedURLException(MalformedURLException e) {
		}

		public void onFacebookError(FacebookError e) {
		}
	}

	class sendinvitaions extends AsyncTask<Void, Void, Void> {
		ProgressDialog dlog = new ProgressDialog(GetFacebookData.this);
		private String jsonrsultdata;

		@Override
		protected void onPreExecute() {
			dlog.setMessage("Please Wait...");
			dlog.setIndeterminate(true);
			dlog.setCancelable(false);
			dlog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {

			String url = "https://www.brandsfever.com/api/v5/invitations/";

			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);

			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						3);
				nameValuePairs
						.add(new BasicNameValuePair("user_id", _getuserId));
				nameValuePairs.add(new BasicNameValuePair("token", _getToken));
				nameValuePairs
						.add(new BasicNameValuePair("channel", "facebook"));
				nameValuePairs.add(new BasicNameValuePair(
						"invitee_identifiers", _fbid));
				nameValuePairs.add(new BasicNameValuePair("request_id",
						"4657865"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				jsonrsultdata = EntityUtils.toString(response.getEntity());

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				JSONObject obj = new JSONObject(jsonrsultdata);
				String msg = obj.getString("msg");
				if (msg.equals("OK")) {
					_msg = "Request successfully sent!";
					_ResponsePopup();
					finish();
				} else {
					_msg = "Request successfully sent!";
					_ResponsePopup();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dlog.cancel();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_post:
			finish();
			break;

		case R.id.post_to_wall:
			postToWall(_fbid);

			break;

		default:
			break;
		}
	}

	// ************************************************************************************************************************//
	public void _ResponsePopup() {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.error_popop,
				(ViewGroup) findViewById(R.id.pop));
		TextView _seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText(_msg);
		Toast toast = new Toast(GetFacebookData.this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	// ********************************************************************************************************************//
	@Override
	public void onBackPressed() {
		Intent _s = new Intent(GetFacebookData.this, InviteFragment.class);
		startActivity(_s);
		finish();
	}
}
