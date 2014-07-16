package com.brandsfever;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import com.androidquery.AQuery;
import com.brandsfever.luxury.R;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductsDataModel;

public class WomenProductDisplay extends Fragment {
	ViewGroup _view;
	ListView woman_product_list;
	static Context ctx = null;
	Button _scrollup;
	PhoneAdapter _padapter;
	TabAdapter _tadapter;
	int a;
	long start, currenttime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		a = DataHolderClass.getInstance().get_deviceInch();
		if (a <= 6) {
			_view = (ViewGroup) inflater.inflate(
					R.layout.phone_women_product_display, null);
		} else if (a >= 7 && a < 9) {
			_view = (ViewGroup) inflater.inflate(
					R.layout.seven_inch_woman_product_display, null);
		} else if (a >= 9) {
			_view = (ViewGroup) inflater.inflate(
					R.layout.ten_inch_woman_product_display, null);
			System.out.println("in ten inch");
		}
		woman_product_list = (ListView) _view
				.findViewById(R.id.woman_product_list);
		_scrollup = (Button) _view.findViewById(R.id.scrolldown);
		_scrollup.setVisibility(View.GONE);
		if (ctx == null) {
			ctx = getActivity().getApplicationContext();
		}
		if (a <= 6) {
			_padapter = new PhoneAdapter(ctx, ProductDisplay.women_prdt);
			woman_product_list.setAdapter(_padapter);
		} else if (a >= 7) {
			_tadapter = new TabAdapter(ctx, ProductDisplay.women_prdt);
			woman_product_list.setAdapter(_tadapter);
		}

		woman_product_list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 4) {
					_scrollup.setVisibility(View.VISIBLE);
				} else {
					_scrollup.setVisibility(View.GONE);
				}
			}
		});
		_scrollup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				woman_product_list.setSelection(0);
				_scrollup.setVisibility(View.GONE);
			}
		});
		return _view;
	}

	// **********************************************************************************************************************//
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
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView ends_in, discount_rate;
			ImageView set_product_image;
			Button go_for_sale;

			inflater = (LayoutInflater) _scontext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(
					R.layout.phone_product_display_inflator, parent, false);

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
			set_product_image = (ImageView) itemView
					.findViewById(R.id.product_image);
			go_for_sale = (Button) itemView.findViewById(R.id.go_for_sale);
			go_for_sale.setTag(position);
			set_product_image.setTag(position);

			Typeface mFont = Typeface.createFromAsset(
					getActivity().getAssets(), "fonts/georgia.ttf");
			ends_in.setTypeface(mFont);
			discount_rate.setTypeface(mFont);
			ProductsDataModel obj = data.get(position);

			String hours_left_str,minutes_left_str,seconds_left_str;
			
			long timeInMilliseconds = obj.getEnds_at();
			long end = timeInMilliseconds * 1000;
			long current = System.currentTimeMillis();
			long diff = end - current;
			int dayCount = (int) diff / (24 * 60 * 60 * 1000);
			
			//***************** hours converting **************//
			
			int hours_left = (int) ((diff / (1000 * 60 * 60)) % 24);
			if(String.valueOf(hours_left).length()<2){
				hours_left_str="0"+String.valueOf(hours_left);				
			}else{
				hours_left_str=String.valueOf(hours_left);
			}
			
			//***************** minuts converting **************//
			
			int minutes_left = (int) ((diff / (1000 * 60)) % 60);			
			if(String.valueOf(minutes_left).length()<2){
				minutes_left_str="0"+String.valueOf(minutes_left);				
			}else{
				minutes_left_str=String.valueOf(minutes_left);
			}	
			
			//***************** seconds converting **************//
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
					+ hours_left_str + ":"
					+ minutes_left_str + ":"
					+ seconds_left_str;
			
			String _to = Integer.toString(hours_left) + ":"
					+ Integer.toString(minutes_left) + ":"
					+ Integer.toString(seconds_left);
			
			String _endDate = date+ "\n" + _to;

			long timeInMillisecond = obj.getStarts_at();
			start = timeInMillisecond * 1000;
			currenttime = System.currentTimeMillis();
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

	// **********************************************************************************************************************//
	class TabAdapter extends BaseAdapter {
		Context _mcontext = null;
		LayoutInflater inflater;
		ArrayList<ProductsDataModel> data;
		View itemView;

		public TabAdapter(Context context,
				ArrayList<ProductsDataModel> arraylist) {
			this._mcontext = context;
			data = arraylist;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView ends_in, discount_rate, t;
			ImageButton go_for_sale;
			
			if (a >= 7 && a < 9) {
				inflater = (LayoutInflater) _mcontext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(
						R.layout.seven_inch_product_display_inflator, parent,
						false);
			} else if (a >= 9) {
				inflater = (LayoutInflater) _mcontext.getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				itemView = inflater.inflate(
						R.layout.ten_inch_product_display_inflator, parent,
						false);
				System.out.println("in ten inch");
			}
			t = (TextView) itemView.findViewById(R.id.t);
			ends_in = (TextView) itemView.findViewById(R.id.set_time);
			discount_rate = (TextView) itemView.findViewById(R.id.set_dicount);
			Typeface mFont = Typeface.createFromAsset(
					getActivity().getAssets(), "fonts/georgia.ttf");
			ends_in.setTypeface(mFont);
			discount_rate.setTypeface(mFont, Typeface.BOLD);
			t.setTypeface(mFont, Typeface.BOLD);
			ImageView imageView = (ImageView) itemView.findViewById(R.id.set_product_img);
			imageView.setTag(position);
			go_for_sale = (ImageButton) itemView.findViewById(R.id.go_for_sale);
			go_for_sale.setTag(position);
			if (position % 7 == 0) {
				ends_in.setBackgroundColor(Color.rgb(142, 19, 69));
			} else if (position % 7 == 1) {
				ends_in.setBackgroundColor(Color.rgb(29, 131, 107));
			} else if (position % 7 == 2) {
				ends_in.setBackgroundColor(Color.rgb(223, 12, 99));
			} else if (position % 7 == 3) {
				ends_in.setBackgroundColor(Color.rgb(31, 72, 102));
			} else if (position % 7 == 4) {
				ends_in.setBackgroundColor(Color.rgb(200, 17, 23));
			} else if (position % 7 == 5) {
				ends_in.setBackgroundColor(Color.rgb(90, 47, 97));
			} else if (position % 7 == 6) {
				ends_in.setBackgroundColor(Color.rgb(236, 71, 42));
			} else if (position % 7 == 7) {
				ends_in.setBackgroundColor(Color.rgb(169, 194, 21));
			}

			String hours_left_str,minutes_left_str,seconds_left_str;
			
			ProductsDataModel obj = data.get(position);
			long timeInMilliseconds = obj.getEnds_at();
			long end = timeInMilliseconds * 1000;
			long current = System.currentTimeMillis();
			long diff = end - current;
			int dayCount = (int) diff / (24 * 60 * 60 * 1000);
			
			//***************** hours converting **************//
			
			int hours_left = (int) ((diff / (1000 * 60 * 60)) % 24);
			if(String.valueOf(hours_left).length()<2){
				hours_left_str="0"+String.valueOf(hours_left);				
			}else{
				hours_left_str=String.valueOf(hours_left);
			}
			
			//***************** minuts converting **************//
			
			int minutes_left = (int) ((diff / (1000 * 60)) % 60);			
			if(String.valueOf(minutes_left).length()<2){
				minutes_left_str="0"+String.valueOf(minutes_left);				
			}else{
				minutes_left_str=String.valueOf(minutes_left);
			}	
			
			//***************** seconds converting **************//
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
			start = timeInMillisecond * 1000L;
			currenttime = System.currentTimeMillis();
			long diffs =    start -currenttime ;
			int hours_lefts = (int) ((diffs / (1000 * 60 * 60)) % 24);
			int minutes_lefts = (int) ((diffs / (1000 * 60)) % 60);
			int seconds_lefts = (int) ((diffs / 1000) % 60);
			
			Calendar cals = Calendar.getInstance(Locale.ENGLISH);
		    cals.setTimeInMillis(start);
		    String start_date = DateFormat.format("dd-MMMM-yyyy", cals).toString();
		    System.out.println("start date is" + start_date);
			
			String _from =  Integer.toString(hours_lefts) + ":"
					+ Integer.toString(minutes_lefts) + ":"
					+ Integer.toString(seconds_lefts);
			
			String _startFrom = start_date +"\n"+ _from;
			
			if(start > currenttime){
				RelativeLayout base_layout = (RelativeLayout)itemView.findViewById(R.id.base_layout);
				base_layout.setBackgroundColor(Color.parseColor("#ADFF2F"));
				TextView aa,ab,bb,ba;
				aa = (TextView)itemView.findViewById(R.id.aa);
				ab = (TextView)itemView.findViewById(R.id.ab);
				bb = (TextView)itemView.findViewById(R.id.bb);
				ba = (TextView)itemView.findViewById(R.id.ba);
				
				aa.setTypeface(mFont, Typeface.NORMAL);
				ab.setTypeface(mFont, Typeface.NORMAL);
				bb.setTypeface(mFont, Typeface.NORMAL);
				ba.setTypeface(mFont, Typeface.NORMAL);
				
				aa.setVisibility(View.VISIBLE);
				ab.setVisibility(View.VISIBLE);
				bb.setVisibility(View.VISIBLE);
				ba.setVisibility(View.VISIBLE);
				
				ab.setText(_startFrom);
				ba.setText(_endDate);
				
				t.setVisibility(View.GONE);
				ends_in.setText(s);
				ends_in.setVisibility(View.GONE);
				discount_rate.setText(obj.getDiscount_text());
				discount_rate.setVisibility(View.GONE);
				String a = "https:" + obj.getTeaser_url();
				go_for_sale.setTag(50000);
				go_for_sale.setVisibility(View.GONE);
				imageView.setTag(50000);
				AQuery aq = new AQuery(_mcontext);
				aq.id(imageView).image(a);
				ColorMatrix matrix = new ColorMatrix();
				matrix.setSaturation(0);
				ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
				imageView.setColorFilter(filter);				
			}else{
				ends_in.setText(s);
				discount_rate.setText(obj.getDiscount_text());
				String a = "https:" + obj.getTeaser_url();
				AQuery aq = new AQuery(_mcontext);
				aq.id(imageView).image(a);
			}
			

			go_for_sale.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int a = (Integer) v.getTag();
					if(a == 50000){
						_ResponsePopup();
					}else{
					ProductsDataModel cs = data.get(a);
					String _sf = cs.getShipping_fee();
					String _sp = cs.getShipping_period();
					String _fs = cs.getFree_shipping();
					DataHolderClass.getInstance().setShipping_fee(_sf);
					DataHolderClass.getInstance().setShipping_period(_sp);
					DataHolderClass.getInstance().setFree_shipping(_fs);
					int _s = Integer.parseInt(cs.getPk());
					DataHolderClass.getInstance().set_mainProductsPk(_s);
					Intent i = new Intent(_mcontext, ProductListing.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					_mcontext.startActivity(i);
				
					}
				}
			});

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int a = (Integer) v.getTag();
					if(a == 50000){
						_ResponsePopup();
					}else{
					ProductsDataModel cs = data.get(a);
					String _sf = cs.getShipping_fee();
					String _sp = cs.getShipping_period();
					String _fs = cs.getFree_shipping();
					DataHolderClass.getInstance().setShipping_fee(_sf);
					DataHolderClass.getInstance().setShipping_period(_sp);
					DataHolderClass.getInstance().setFree_shipping(_fs);
					int _s = Integer.parseInt(cs.getPk());
					DataHolderClass.getInstance().set_mainProductsPk(_s);
					Intent i = new Intent(_mcontext, ProductListing.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					_mcontext.startActivity(i);
					
					}

				}
			});
			return itemView;
		}
	}
	// *********************************************************************************************************************//
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
