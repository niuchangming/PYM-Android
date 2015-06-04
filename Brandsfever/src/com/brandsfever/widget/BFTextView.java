package com.brandsfever.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class BFTextView extends TextView {

	private static Typeface mTypeface;
	
	public BFTextView(Context context) {
		this(context, null);
	}
	
	public BFTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BFTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (mTypeface == null) {
	         mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/georgia.ttf");
	     }
	     setTypeface(mTypeface);
	}

}
