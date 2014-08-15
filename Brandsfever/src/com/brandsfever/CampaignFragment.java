package com.brandsfever;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.viewpagerindicator.TabPageIndicator;

public class CampaignFragment extends Fragment {

	private String[] mCategories;
	public ViewPager mViewPager;
	private FrameLayout mLinearLayout;

	public static CampaignFragment newInstance() {

		CampaignFragment fragment = new CampaignFragment();

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCategories = getResources().getStringArray(R.array.category);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mLinearLayout == null) {

			mLinearLayout = (FrameLayout) inflater.inflate(
					R.layout.fragment_campaign, container, false);
			FragmentPagerAdapter adapter = new CampaignAdapter(getActivity()
					.getSupportFragmentManager());
			mViewPager = (ViewPager) mLinearLayout.findViewById(R.id.pager);
			mViewPager.setAdapter(adapter);
			
			TabPageIndicator indicator = (TabPageIndicator) mLinearLayout
					.findViewById(R.id.indicator);
			indicator.setViewPager(mViewPager);
		}

		return mLinearLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class CampaignAdapter extends FragmentPagerAdapter {

		public CampaignAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			return CampaignListFragment.newInstance(mCategories[position
					% mCategories.length].toLowerCase());
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mCategories[position % mCategories.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return mCategories.length;
		}
	}
}
