package com.brandsfever.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class BFTextView extends TextView {

	public BFTextView(Context context, AttributeSet attrs, int defStyle){
		super(context,attrs,defStyle);
		init();
	}
	
	public BFTextView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	public BFTextView(Context context) {
		super(context);
		init();
	}

	private void init(){
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/georgia.ttf");
		setTypeface(tf);
	}
}
