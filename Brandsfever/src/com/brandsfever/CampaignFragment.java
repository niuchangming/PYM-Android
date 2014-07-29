package com.brandsfever;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;

import com.adapter.VPagerAdapter;

public class CampaignFragment extends Fragment {

//	private int mPos = -1;
	private FragmentTabHost	mTabHost;
	private ViewPager	mViewPager;
	private HorizontalScrollView mHorizontalScroll;
	private VPagerAdapter mPagerAdapter;
	
	public CampaignFragment(){ }
	
	public CampaignFragment(int pos){
//		mPos = pos;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		if (mPos == -1 && savedInstanceState != null)
//			mPos = savedInstanceState.getInt("mPos");
		
		mTabHost = (FragmentTabHost)inflater.inflate(R.layout.fragment_campaign, null);
		mTabHost.setup(getActivity(), getChildFragmentManager());
	
		mTabHost.addTab(mTabHost.newTabSpec("All").setIndicator("All"), CampaignListFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("Women").setIndicator("Women"), CampaignListFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("Men").setIndicator("Men"), CampaignListFragment.class, null);
		
		
		return mTabHost;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		outState.putInt("mPos", mPos);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mTabHost = null;
	}
	
	private class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 30;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.grid_item, null);
			}
			
			return convertView;
		}
		
	}
}
