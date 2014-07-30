package com.brandsfever;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.androidquery.AQuery;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductsDataModel;
import com.google.analytics.tracking.android.Log;

public class CampaignListFragment extends Fragment {

	private static final String TAG = "CampaignListFragment";
	
	ListView campaignList;
	Button scrollUp;
	
	public static CampaignListFragment newInstance() {

		CampaignListFragment fragment = new CampaignListFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup)inflater.inflate(R.layout.phone_all_products, null);
		campaignList = (ListView)view.findViewById(R.id.all_product_list);
		scrollUp = (Button)view.findViewById(R.id.scrolldown);
		scrollUp.setVisibility(View.GONE);
		
		PhoneAdapter adapter = new PhoneAdapter(getActivity(),ProductDisplay.all_prdt);
		campaignList.setAdapter(adapter);
		
		campaignList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 4) {
					scrollUp.setVisibility(View.VISIBLE);
				} else {
					scrollUp.setVisibility(View.GONE);
				}
			}
		});
		scrollUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				campaignList.setSelection(0);
				scrollUp.setVisibility(View.GONE);
			}
		});
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
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
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.grid_item, null);
			}

			return convertView;
		}

	}
	
	
	class PhoneAdapter extends BaseAdapter {

		Context _scontext;
		LayoutInflater inflater;
		ArrayList<ProductsDataModel> data;

		public PhoneAdapter(Context context,
				ArrayList<ProductsDataModel> arraylist) {
			this._scontext = context;
			data = arraylist;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView ends_in, discount_rate;
			ImageView set_product_image;
			Button go_for_sale;

			View itemView;
			if(convertView == null){
				itemView = getActivity().getLayoutInflater().inflate(
					R.layout.phone_product_display_inflator, parent, false);
				Log.i("convertView is null");
			} else {
				itemView = convertView;
				Log.i("convertView is reused ======");
			}
			

			LinearLayout f_l = (LinearLayout) itemView.findViewById(R.id.pr_bg);
			if (position % 3 == 0) {
				f_l.setBackgroundResource(R.drawable.campaignbase_black);
			} else if (position % 3 == 1) {
				f_l.setBackgroundResource(R.drawable.campaignbase_pink);
			} else if (position % 3 == 2) {
				f_l.setBackgroundResource(R.drawable.campaignbase_brown);
			}

			ends_in = (TextView) itemView.findViewById(R.id.set_time_left);
			discount_rate = (TextView) itemView.findViewById(R.id.set_discount);
			set_product_image = (ImageView) itemView.findViewById(R.id.product_image);
			go_for_sale = (Button) itemView.findViewById(R.id.go_for_sale);
			go_for_sale.setTag(position);
			set_product_image.setTag(position);

			Typeface mFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/georgia.ttf");
			ends_in.setTypeface(mFont);
			discount_rate.setTypeface(mFont);
			ProductsDataModel obj = data.get(position);
			
			String hours_left_str,minutes_left_str,seconds_left_str;

			long timeInMilliseconds = obj.getEnds_at();
			long end = timeInMilliseconds * 1000;
			long current = System.currentTimeMillis();
			long diff = end - current;
			int dayCount = (int) diff / (24 * 60 * 60 * 1000);
			
			int hours_left = (int) ((diff / (1000 * 60 * 60)) % 24);
			if(String.valueOf(hours_left).length()<2){
				hours_left_str="0"+String.valueOf(hours_left);				
			}else{
				hours_left_str=String.valueOf(hours_left);
			}
			
			int minutes_left = (int) ((diff / (1000 * 60)) % 60);			
			if(String.valueOf(minutes_left).length()<2){
				minutes_left_str="0"+String.valueOf(minutes_left);				
			}else{
				minutes_left_str=String.valueOf(minutes_left);
			}	
			
			int seconds_left = (int) ((diff / 1000) % 60);
			if(String.valueOf(seconds_left).length()<2){
				seconds_left_str="0"+String.valueOf(seconds_left);				
			}else{
				seconds_left_str=String.valueOf(seconds_left);
			}
		
			Calendar cal = Calendar.getInstance(Locale.ENGLISH);
		    cal.setTimeInMillis(end);
		    String date = DateFormat.format("dd-MMMM-yyyy", cal).toString();

			String s = Integer.toString(dayCount) + " Days" + " "
					+  hours_left_str + ":"
					+ minutes_left_str + ":"
					+ seconds_left_str;
			
			String _to = Integer.toString(hours_left) + ":"
					+ Integer.toString(minutes_left) + ":"
					+ Integer.toString(seconds_left);
			
			String _endDate = date+ "\n" + _to;

			long timeInMillisecond = obj.getStarts_at();
			long start = timeInMillisecond * 1000;
			long currenttime = System.currentTimeMillis();
			long diffs = start - currenttime;
			int hours_lefts = (int) ((diffs / (1000 * 60 * 60)) % 24);
			int minutes_lefts = (int) ((diffs / (1000 * 60)) % 60);
			int seconds_lefts = (int) ((diffs / 1000) % 60);
			
			Calendar cals = Calendar.getInstance(Locale.ENGLISH);
		    cals.setTimeInMillis(start);
		    String start_date = DateFormat.format("dd-MMMM-yyyy", cals).toString();
			String _from =  Integer.toString(hours_lefts) + ":"
					+ Integer.toString(minutes_lefts) + ":"
					+ Integer.toString(seconds_lefts);
			
			String _startFrom = start_date +"\n"+ _from;
			
			if (start > currenttime) {
				ends_in.setText(s);
				ends_in.setVisibility(View.GONE);
				discount_rate.setText(obj.getDiscount_text());
				discount_rate.setVisibility(View.GONE);
				TextView set_from = (TextView) itemView.findViewById(R.id.set_from);
				TextView set_to = (TextView) itemView.findViewById(R.id.set_to);
				set_to.setTypeface(mFont, Typeface.NORMAL);
				set_to.setText(_endDate);

				set_from.setText(_startFrom);
				set_from.setVisibility(View.VISIBLE);
				set_to.setVisibility(View.VISIBLE);
				set_from.setTypeface(mFont, Typeface.NORMAL);

				String a = "https:" + obj.getTeaser_url();
				go_for_sale.setTag(50000);
				go_for_sale.setVisibility(View.GONE);
				set_product_image.setTag(50000);
				AQuery aq = new AQuery(_scontext);
				aq.id(set_product_image).image(a);
				ColorMatrix matrix = new ColorMatrix();
				matrix.setSaturation(0);
				ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
				set_product_image.setColorFilter(filter);
				f_l.setBackgroundResource(R.drawable.green_base);
			} else {
				ends_in.setText(s);
				discount_rate.setText(obj.getDiscount_text());
				String a = "https:" + obj.getTeaser_url();
				AQuery aq = new AQuery(_scontext);
				aq.id(set_product_image).image(a);
			}

			go_for_sale.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int a = (Integer) v.getTag();
					if (a == 50000) {
						_ResponsePopup();
					} else {

						ProductsDataModel cs = data.get(a);
						int _s = Integer.parseInt(cs.getPk());
						String _sf = cs.getShipping_fee();
						String _sp = cs.getShipping_period();
						String _fs = cs.getFree_shipping();
						DataHolderClass.getInstance().setShipping_fee(_sf);
						DataHolderClass.getInstance().setShipping_period(_sp);
						DataHolderClass.getInstance().setFree_shipping(_fs);
						DataHolderClass.getInstance().set_mainProductsPk(_s);
						Intent i = new Intent(_scontext, ProductListing.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						_scontext.startActivity(i);
					}
				}
			});
			set_product_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int a = (Integer) v.getTag();
					if (a == 50000) {
						_ResponsePopup();
					} else {
						ProductsDataModel cs = data.get(a);
						int _s = Integer.parseInt(cs.getPk());
						String _sf = cs.getShipping_fee();
						String _sp = cs.getShipping_period();
						String _fs = cs.getFree_shipping();
						DataHolderClass.getInstance().setShipping_fee(_sf);
						DataHolderClass.getInstance().setShipping_period(_sp);
						DataHolderClass.getInstance().setFree_shipping(_fs);
						DataHolderClass.getInstance().set_mainProductsPk(_s);
						Intent i = new Intent(_scontext, ProductListing.class);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						_scontext.startActivity(i);
					}
				}
			});

			return itemView;
		}
	}
	
	
	public void _ResponsePopup() {
		View view = View.inflate(getActivity().getBaseContext(),
				R.layout.error_popop, null);
		TextView _seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText("Coming Soon!!!");
		Toast toast = new Toast(getActivity().getBaseContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}
}
