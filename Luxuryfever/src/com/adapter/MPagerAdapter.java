package com.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.brandsfever.SwipeLeft;
import com.brandsfever.SwipeRight;

public class MPagerAdapter extends FragmentPagerAdapter {
	private Context _context;
	public static int totalPage=2;
	public MPagerAdapter(Context context, FragmentManager fm) {
		super(fm);	
		_context=context;
		
		}
	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		switch(position){
		case 0:
			f=SwipeLeft.newInstance(_context);	
			break;
		case 1:
			f=SwipeRight.newInstance(_context);	
			break;
		}
		return f;
	}
	@Override
	public int getCount() {
		return totalPage;
	}

}