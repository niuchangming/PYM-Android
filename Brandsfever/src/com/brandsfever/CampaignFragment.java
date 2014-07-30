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

	private static final String[] CONTENT = new String[] { "All", "Women",
			"Men", "Children", "Home", "Accessories" };

	public CampaignFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		FragmentPagerAdapter adapter = new CampaignAdapter(getActivity()
				.getSupportFragmentManager());

		FrameLayout linearLayout = (FrameLayout) inflater.inflate(
				R.layout.fragment_campaign, null);

		ViewPager pager = (ViewPager) linearLayout.findViewById(R.id.pager);
		pager.setAdapter(adapter);

		TabPageIndicator indicator = (TabPageIndicator) linearLayout
				.findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		return linearLayout;
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

			return CampaignListFragment.newInstance(CONTENT[position
					% CONTENT.length]);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}

		@Override
		public int getCount() {
			return CONTENT.length;
		}
	}
}
