package com.brandsfever;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SwipeLeft extends Fragment{
	
	public static Fragment newInstance(Context context) {
		SwipeLeft f = new SwipeLeft();	
		
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_swipe_left, null);
		Typeface _font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/georgia.ttf");
		TextView t = (TextView)root.findViewById(R.id.t);
		TextView t1 = (TextView)root.findViewById(R.id.t1);
		t.setTypeface(_font);
		t1.setTypeface(_font);
		
		return root;
	}

}
