package com.brandsfever;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.dataholder.DataHolderClass;

public class MyDialogFragment extends DialogFragment implements OnClickListener {
	String _getcharturl;
	WebView _setsizechart;
	ImageButton _close;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.chart_dialog_fragment, container);

		_getcharturl = getArguments().getString("_sizecharturl");
		_setsizechart = (WebView) view.findViewById(R.id.webView1);
		try {
			_setsizechart.loadUrl(_getcharturl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_close = (ImageButton) view.findViewById(R.id.cancel_size_chart);
		_close.setOnClickListener(this);
		return view;

	}

	@Override
	public void onStart() {
		super.onStart();
		if (getDialog() == null)
			return;
		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			int dialogWidth = 420;
			int dialogHeight = 480;
			getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
			getDialog().setCanceledOnTouchOutside(false);

		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7) {
			int dialogWidth = 650;
			int dialogHeight = 850;
			getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
			getDialog().setCanceledOnTouchOutside(false);

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_size_chart:
			getDialog().dismiss();
			break;

		default:
			break;
		}
	}
}