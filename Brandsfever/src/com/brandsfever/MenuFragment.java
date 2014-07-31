package com.brandsfever;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuFragment extends ListFragment {
	private static final String TAG = "MenuFragment";
	private String[] mMenus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mMenus = getResources().getStringArray(R.array.menus);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.menu_list_textview,android.R.id.text1, mMenus);
		setListAdapter(menuAdapter);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		if(position < mMenus.length){ // The category is clicked.
			if(getActivity() instanceof ProductDisplay){
				((ProductDisplay)getActivity()).toggle();
			}
		}
	}
	
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof ProductDisplay){
			// TODO:
		}
	}
}
