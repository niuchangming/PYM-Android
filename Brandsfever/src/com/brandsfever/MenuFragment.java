package com.brandsfever;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends ListFragment {
	private static final String TAG = "MenuFragment";
	private String[] mMenus;
	private List<String> mCategories;

	public TextView mHeaderTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMenus = getResources().getStringArray(R.array.menus);
		String[] categories = getResources().getStringArray(R.array.category);
		mCategories = Arrays.asList(categories);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ListView view = (ListView)inflater.inflate(R.layout.menu_list, null);
		LinearLayout headerView = (LinearLayout)inflater.inflate(R.layout.menu_list_header, null);
		mHeaderTextView = (TextView)headerView.findViewById(R.id.menu_header);
		mHeaderTextView.setText("Hi! Changxi");
		view.addHeaderView(headerView);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.menu_list_textview, R.id.menu_list_text_view,
				mMenus);
		setListAdapter(menuAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		position -= l.getHeaderViewsCount();
		if (position < 0) // when click on header view
			return;
		String selectedMenu = mMenus[position];
		ProductDisplay pd = null;
		if (getActivity() instanceof ProductDisplay) {
			pd = (ProductDisplay) getActivity();
		} else {
			return;
		}

		if (mCategories.contains(selectedMenu)) {
			if(mCategories.contains(pd.getCurrentMenu())){
				pd.toggle();
				pd.setCurrentMenu(selectedMenu);
			} else {
				Fragment campaign = new CampaignFragment();
				pd.setCurrentMenu(selectedMenu);
				pd.switchContent(campaign);
			}
			Log.i(TAG, pd.getCurrentMenu());
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
			if (pd.getCurrentMenu().equalsIgnoreCase(selectedMenu)){
				pd.toggle();
			} else {
				Fragment myCart = new MyCartFragment();
				if(myCart != null){
					pd.switchContent(myCart);
					pd.setCurrentMenu(selectedMenu);
				}
			}
		} else if (selectedMenu.equalsIgnoreCase("Settings")) {
			if (pd.getCurrentMenu().equalsIgnoreCase(selectedMenu)){
				pd.toggle();
			} else {
				Fragment settings = new SettingPhone();
				if (settings != null){
					pd.switchContent(settings);
					pd.setCurrentMenu(selectedMenu);
				}
			}

		} else if (selectedMenu.equalsIgnoreCase("Invite")) {
			if (pd.getCurrentMenu().equalsIgnoreCase(selectedMenu)){
				pd.toggle();
			} else {
				Fragment invite = new InviteFragment();
				if (invite != null){
					pd.switchContent(invite);
					pd.setCurrentMenu(selectedMenu);
				}
			}
		} else if (selectedMenu.equalsIgnoreCase("Logout")) {

		} else if (selectedMenu.equalsIgnoreCase("Login")) {

		}
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof ProductDisplay) {
			ProductDisplay pd = (ProductDisplay) getActivity();
			pd.switchContent(fragment);
		}
	}
}
