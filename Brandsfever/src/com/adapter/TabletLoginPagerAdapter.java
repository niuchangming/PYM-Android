package com.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.brandsfever.PhoneLoginPage;
import com.brandsfever.PhoneSignupPage;

public class TabletLoginPagerAdapter extends FragmentPagerAdapter {
	private Context _context;
	public TabletLoginPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		_context=context;
	}
	@Override
	public Fragment getItem(int position) {
		 Fragment f = new Fragment();
		 switch(position){
	        case 0:
	            f=PhoneLoginPage.newInstance(_context);
	            break;
	        case 1:
	            f=PhoneSignupPage.newInstance(_context);
	            break;
	        }
	        return f;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
