package com.brandsfever;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SupportActivity extends FragmentActivity {

	private static final String TAG = "SupportActivity";
	
	private String mOrderId;
	private String mIssue;
	private String mDetails;
	private String mEmail;
	
	Button mAll, mMen, mWomen, mChildren, mHome, mAccessories, mLogin, mSettings, mCart, mInvite, mLogout;
	Typeface mFont;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_support);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SupportFragment()).commit();
		}
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class SupportFragment extends Fragment {

		Button mSubmit;
		Typeface mFont;
		public SupportFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_support,
					container, false);
			
			mFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/georgia.ttf");
			
			TextView title = (TextView) rootView.findViewById(R.id.support_title);
			title.setTypeface(mFont,Typeface.ITALIC);
			
			
			mSubmit = (Button)rootView.findViewById(R.id.support_submit);
			mSubmit.setTypeface(mFont);
			mSubmit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d(TAG, "Submit is clicked.");
//					getActivity().getFragmentManager().beginTransaction().remove(SupportFragment.this).commit();
				}
			});
			
			return rootView;
		}
	}

}
