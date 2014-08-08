package com.brandsfever;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.datamodel.faceclass;
import com.progressbar.ProgressHUD;

import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Util;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

public class Facebook_Invite extends Activity {
	Facebook facebook;
	public ArrayList<faceclass> faceclassobj = new ArrayList<faceclass>();
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);

	}
	public void getdata(){
		JSONObject jsonObj = null;
		try {
			jsonObj = Util.parseJson(facebook.request("me/friends"));
			JSONArray jArray = jsonObj.getJSONArray("data");
			for (int k = 0; k < jArray.length(); k++) {
				JSONObject obj1 = jArray.getJSONObject(k);
				faceclass obj = new faceclass();

				obj.setId(obj1.getString("id"));
				obj.setName(obj1.getString("name"));
				faceclassobj.add(obj);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} catch (FacebookError e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.phone_facebook__invite);
		try {
			facebook = new Facebook(getResources().getString(R.string.fbid));
			SessionStore.restore(facebook, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new GetFriendList().execute();
	}
	
	@Override
	public void onStart(){
		super.onStart();
		
		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.SCREEN_NAME, this.getString(R.string.app_name)+": facebook/invite/?device=2");
		tracker.send(MapBuilder.createAppView().build());
	}
	
	class GetFriendList extends AsyncTask<String,String,String> implements OnCancelListener{
		   ProgressHUD mProgressHUD;
			@Override
			protected void onPreExecute() {
				mProgressHUD = ProgressHUD.show(Facebook_Invite.this,"Loading", true,true,this);
				DisplayMetrics displaymetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				int displayHeight = displaymetrics.heightPixels;
				mProgressHUD.getWindow().setGravity(Gravity.CENTER);
				WindowManager.LayoutParams wmlp = mProgressHUD.getWindow().getAttributes();
				wmlp.y =displayHeight / 4;
				mProgressHUD.getWindow().setAttributes(wmlp);
				mProgressHUD.setCancelable(false);
				super.onPreExecute();
				
			}
			@Override
			public void onCancel(DialogInterface arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			protected String doInBackground(String... params) {
				getdata();
				return null;
			}
			@Override
			protected void onPostExecute(String result) {
				mProgressHUD.dismiss();
			}
		
	}
}
