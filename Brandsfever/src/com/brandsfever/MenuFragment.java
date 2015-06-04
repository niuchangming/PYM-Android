package com.brandsfever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment extends ListFragment {
	private static final String TAG = "MenuFragment";

	private ArrayList<String> mMenus = new ArrayList<String>();
	private List<String> mLoginList;
	private List<String> mLogoutList;
	public	List<String> mCategories;
	private String mUserName;
	private TextView mHeaderTextView;
	private MenuListAdapter mMenuAdapter;
	private Typeface mTypeface;
	private ListView mListView;
	private LinearLayout mHeaderView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String[] categories = getResources().getStringArray(R.array.category);
		String[] loginList = getResources().getStringArray(R.array.login);
		String[] logoutList = getResources().getStringArray(R.array.logout);
		mCategories = Arrays.asList(categories);
		mLoginList = Arrays.asList(loginList);
		mLogoutList = Arrays.asList(logoutList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mListView == null) {
			mListView = (ListView) inflater.inflate(R.layout.menu_list, null);
			mHeaderView = (LinearLayout) inflater.inflate(
					R.layout.menu_list_header, null);
			mHeaderTextView = (TextView) mHeaderView
					.findViewById(R.id.menu_header);
		}
		return mListView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mTypeface == null) {
			mTypeface = Typeface.createFromAsset(getActivity().getAssets(),
					"fonts/georgia.ttf"); // setting a custom TypeFace
			mHeaderTextView.setTypeface(mTypeface);
			mListView.addHeaderView(mHeaderView);
		}

		if (mMenuAdapter == null) {
			mMenuAdapter = new MenuListAdapter(getActivity(),
					R.layout.menu_list_textview, mMenus);
			setListAdapter(mMenuAdapter);
		}
		resetMenu();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((ViewGroup) mListView.getParent()).removeView(mListView);
	}

	public void resetMenu() {

		if (getActivity() != null) {
			SharedPreferences pref = getActivity().getApplicationContext()
					.getSharedPreferences("mypref", 0);
			String userName = pref.getString("UserName", null);

			boolean updateToLogin = false;
			if ((mUserName == null) && (userName != null)) { // first set
																// username
				updateToLogin = true;
				mUserName = userName;
			} else if ((mUserName != null)
					&& (!mUserName.equalsIgnoreCase(userName))) { // change
																	// username
				if (userName != null) {
					updateToLogin = true;
					mUserName = userName;
				} else {
					mUserName = userName;
				}
			}

			if (updateToLogin) {

				if (mHeaderTextView != null) {
					mHeaderTextView.setText("Hi! " + mUserName);
				}
				mMenus.clear();
				mMenus.addAll(mCategories);
				mMenus.addAll(mLogoutList);
				mMenuAdapter.notifyDataSetChanged();
			} else {
				if (mUserName == null & userName == null) { // init to login
					mHeaderTextView.setText("Hi! Guest");
					mMenus.clear();
					mMenus.addAll(mCategories);
					mMenus.addAll(mLoginList);
					mMenuAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		position -= l.getHeaderViewsCount();
		if (position < 0) // when click on header view
			return;
		final String selectedMenu = mMenus.get(position);
		final ProductDisplay pd = (ProductDisplay) getActivity();
		if (pd == null) {
			return;
		}

		if (mCategories.contains(selectedMenu)) {
			if (mCategories.contains(pd.getCurrentMenu())) {
				pd.toggle();
				pd.setCurrentMenu(selectedMenu);
			} else {

				if (pd.mCampaignFragment == null) {
					CampaignFragment campaign = CampaignFragment.newInstance();
					pd.mCampaignFragment = campaign;
				}
				pd.switchContent(pd.mCampaignFragment);
				Handler h = new Handler();
				h.postDelayed(new Runnable() {
					public void run() {
						pd.setCurrentMenu(selectedMenu);
					}
				}, 50);
			}
		} else if (selectedMenu.equalsIgnoreCase("Support")) {
			if (pd.getCurrentMenu().equalsIgnoreCase(selectedMenu)) {
				pd.toggle();
			} else {
				Fragment support = new SupportFragment();
				if (support != null) {
					pd.switchContent(support);
					pd.setCurrentMenu(selectedMenu);
				}
			}
		} else if (selectedMenu.equalsIgnoreCase("MyCart")) {
			if (pd.getCurrentMenu().equalsIgnoreCase(selectedMenu)) {
				pd.toggle();
			} else {
				if(getActivity() != null){
					Intent gotocart = new Intent(getActivity(), MyCartActivity.class);
					startActivity(gotocart);
				}
				/*Fragment myCart = new MyCartFragment();
				if (myCart != null) {
					pd.switchContent(myCart);
					pd.setCurrentMenu(selectedMenu);
				}*/
			}
		} else if (selectedMenu.equalsIgnoreCase("Settings")) {
			if (pd.getCurrentMenu().equalsIgnoreCase(selectedMenu)) {
				pd.toggle();
			} else {
				Fragment settings = new SettingPhone();
				if (settings != null) {
					pd.switchContent(settings);
					pd.setCurrentMenu(selectedMenu);
				}
			}

		} else if (selectedMenu.equalsIgnoreCase("Invite")) {
			if (pd.getCurrentMenu().equalsIgnoreCase(selectedMenu)) {
				pd.toggle();
			} else {
				Fragment invite = new InviteFragment();
				if (invite != null) {
					pd.switchContent(invite);
					pd.setCurrentMenu(selectedMenu);
				}
			}
		} else if (selectedMenu.equalsIgnoreCase("Logout")) {

			mMenus.clear();
			mMenus.addAll(mCategories);
			mMenus.addAll(mLoginList);
			mMenuAdapter.notifyDataSetChanged();
			mHeaderTextView.setText("Hi! Guest");
			mUserName = null;

			SharedPreferences pref = getActivity().getApplicationContext()
					.getSharedPreferences("mypref", 0);
			Editor editor = pref.edit();
			editor.clear();
			editor.commit();
			pd.toggle();
			responsePopup("Logout successfully");
		} else if (selectedMenu.equalsIgnoreCase("Login")) {

			if (getActivity() != null) {
				Intent oh = new Intent(getActivity(), PhoneLoginActivity.class);
				startActivity(oh);
				Handler h = new Handler();
				h.postDelayed(new Runnable() {
					public void run() {
						pd.toggle();
					}
				}, 200);
			}
		}
	}

	public void responsePopup(String message) {
		View view = View.inflate(getActivity().getBaseContext(),
				R.layout.error_popop, null);
		TextView textView = (TextView) view.findViewById(R.id._seterrormsg);
		textView.setText(message);
		Toast toast = new Toast(getActivity().getBaseContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	private class MenuListAdapter extends ArrayAdapter<String> {

		public MenuListAdapter(Context context, int resource, List<String> items) {
			super(context, resource, items);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView v = (TextView) convertView;

			if (v == null) {
				LayoutInflater vi;
				vi = LayoutInflater.from(getContext());
				v = (TextView) vi.inflate(R.layout.menu_list_textview, null);
			}

			String menu = getItem(position);
			if (mCategories.contains(menu) && (!menu.equalsIgnoreCase("All"))) {
				v.setText("\t" + menu);
			} else {
				v.setText(menu);
			}

			return v;
		}
	}

}
