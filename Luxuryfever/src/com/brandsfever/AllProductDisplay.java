//package com.brandsfever;
//
//import java.util.ArrayList;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.ColorMatrix;
//import android.graphics.ColorMatrixColorFilter;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AbsListView.OnScrollListener;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.androidquery.AQuery;
//import com.brandsfever.luxury.R;
//import com.brandsfever.widget.BFTextView;
//import com.dataholder.DataHolderClass;
//import com.datamodel.ProductsDataModel;
//
///**
// * @modify ycb
// * @date 2015/3/8
// * 
// */
//public class AllProductDisplay extends Fragment {
//	private static final String TAG = "AllProductDisplay";
//	ViewGroup _view;
//	GridView all_products;
//	static Context ctx = null;
//	Button _scrollup;
//	// PhoneAdapter _padapter;
//	TabAdapter _tadapter;
//	int a;
//	long start, currenttime;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		a = DataHolderClass.getInstance().get_deviceInch();
//		if (a <= 6) {
//			_view = (ViewGroup) inflater.inflate(R.layout.phone_all_products,
//					null);
//		} else if (a >= 7 && a < 9) {
//			_view = (ViewGroup) inflater.inflate(
//					R.layout.seven_inch_all_products, null);
//		} else if (a >= 9) {
//			_view = (ViewGroup) inflater.inflate(
//					R.layout.ten_inch_all_products, null);
//		}
//		all_products = (GridView) _view.findViewById(R.id.all_product_list);
//		_scrollup = (Button) _view.findViewById(R.id.scrolldown);
//		_scrollup.setVisibility(View.GONE);
//		if (ctx == null) {
//			ctx = getActivity().getApplicationContext();
//		}
//		// if (a <= 6) {
//		// _padapter = new PhoneAdapter(ctx, ProductDisplay.all_prdt);
//		// all_products.setAdapter(_padapter);
//		// } else if (a >= 7) {
//		// _tadapter = new TabAdapter(ctx, ProductDisplay.all_prdt);
//		// all_products.setAdapter(_tadapter);
//		// }
//		all_products.setAdapter(new TabAdapter(ctx, ProductDisplay.all_prdt));
//
//		all_products.setOnScrollListener(new OnScrollListener() {
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				if (firstVisibleItem > 4) {
//					_scrollup.setVisibility(View.VISIBLE);
//				} else {
//					_scrollup.setVisibility(View.GONE);
//				}
//			}
//		});
//		_scrollup.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				all_products.setSelection(0);
//				_scrollup.setVisibility(View.GONE);
//			}
//		});
//
//		return _view;
//	}
//
//	class TabAdapter extends BaseAdapter implements OnClickListener {
//		Context _mcontext = null;
//		ArrayList<ProductsDataModel> data;
//		private AQuery mAQuery;
//		private ColorMatrixColorFilter colorFilter;
//
//		public TabAdapter(Context context,
//				ArrayList<ProductsDataModel> arraylist) {
//			this._mcontext = context;
//			data = arraylist;
//			mAQuery = new AQuery(getActivity());
//			ColorMatrix matrix = new ColorMatrix();
//			matrix.setSaturation(0);
//			colorFilter = new ColorMatrixColorFilter(matrix);
//		}
//
//		@Override
//		public int getCount() {
//			return data.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@SuppressLint("ResourceAsColor")
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//
//			Holder holder = null;
//			if (convertView == null) {
//				holder = new Holder();
//				convertView = LayoutInflater.from(_mcontext).inflate(
//						R.layout.products_grid_item, parent, false);
//				holder.name = (BFTextView) convertView.findViewById(R.id.name);
//				holder.discountRate = (BFTextView) convertView
//						.findViewById(R.id.discount);
//				holder.mImageView = (ImageView) convertView
//						.findViewById(R.id.product_image);
//				convertView.setTag(holder);
//			} else {
//				holder = (Holder) convertView.getTag();
//			}
//
//			// int a = DataHolderClass.getInstance().get_deviceInch();
//			long start, currenttime;
//
//			holder.mImageView.clearColorFilter();
//			ProductsDataModel obj = data.get(position);
//			long timeInMillisecond = obj.getStarts_at();
//			start = timeInMillisecond * 1000L;
//			currenttime = System.currentTimeMillis();
//
//			holder.name.setText(obj.getName());
//			if (start > currenttime) {
//				holder.discountRate.setVisibility(View.GONE);
//				holder.mImageView.setId(50000);
//				// String imgUrl = "https:" + obj.getTeaser_url();
//				// AQuery aq = new AQuery(_mcontext);
//				// aq.id(holder.mImageView).image(imgUrl);
//				// ColorMatrix matrix = new ColorMatrix();
//				// matrix.setSaturation(0);
//				// ColorMatrixColorFilter filter = new ColorMatrixColorFilter(
//				// matrix);
//				holder.mImageView.setColorFilter(colorFilter);
//			} else {
//				holder.discountRate.setVisibility(View.VISIBLE);
//				holder.mImageView.setId(position);
//				holder.discountRate.setText(obj.getDiscount_text());
//				// String imgUrl = "https:" + obj.getTeaser_url();
//				// AQuery aq = new AQuery(_mcontext);
//				// aq.id(holder.mImageView).image(imgUrl);
//			}
//
//			String imgUrl = "https:" + obj.getTeaser_url();
//			// AQuery aq = new AQuery(_mcontext);
//			mAQuery.id(holder.mImageView).image(imgUrl);
//
//			holder.mImageView.setOnClickListener(this);
//			return convertView;
//		}
//
//		private class Holder {
//			BFTextView discountRate, name;
//			ImageView mImageView;
//		}
//
//		public String getEndNotice(long endTime) {
//			long time = endTime * 1000 - System.currentTimeMillis();
//			StringBuilder sb = new StringBuilder();
//			sb.append("Ends in ");
//			int day = (int) (time / (24 * 60 * 60 * 1000));
//			sb.append(day);
//			sb.append(" Days");
//			return sb.toString();
//		}
//
//		@Override
//		public void onClick(View v) {
//			if (v.getId() == 50000) {
//				responsePopup();
//			} else {
//				ProductsDataModel cs = data.get(v.getId());
//				String _sf = cs.getShipping_fee();
//				String _sp = cs.getShipping_period();
//				String _fs = cs.getFree_shipping();
//				DataHolderClass.getInstance().setShipping_fee(_sf);
//				DataHolderClass.getInstance().setShipping_period(_sp);
//				DataHolderClass.getInstance().setFree_shipping(_fs);
//				int _s = Integer.parseInt(cs.getPk());
//				DataHolderClass.getInstance().set_mainProductsPk(_s);
//				Intent i = new Intent(_mcontext, ProductListing.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				_mcontext.startActivity(i);
//			}
//		}
//	}
//
//	// class PhoneAdapter extends BaseAdapter {
//	//
//	// Context _scontext;
//	// LayoutInflater inflater;
//	// ArrayList<ProductsDataModel> data;
//	//
//	// public PhoneAdapter(Context context,
//	// ArrayList<ProductsDataModel> arraylist) {
//	// this._scontext = context;
//	// data = arraylist;
//	// }
//	//
//	// @Override
//	// public int getCount() {
//	// return data.size();
//	// }
//	//
//	// @Override
//	// public Object getItem(int arg0) {
//	// return null;
//	// }
//	//
//	// @Override
//	// public long getItemId(int arg0) {
//	// return 0;
//	// }
//	//
//	// @Override
//	// public View getView(int position, View convertView, ViewGroup parent) {
//	// ImageView set_product_image;
//	//
//	// inflater = (LayoutInflater) _scontext
//	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	// View itemView = inflater.inflate(
//	// R.layout.phone_product_display_inflator, parent, false);
//	//
//	// LinearLayout f_l = (LinearLayout) itemView.findViewById(R.id.pr_bg);
//	// TextView nameText = (TextView)itemView.findViewById(R.id.campaign_name);
//	// set_product_image = (ImageView)
//	// itemView.findViewById(R.id.product_image);
//	// set_product_image.setTag(position);
//	//
//	// Typeface mFont = Typeface.createFromAsset(getActivity().getAssets(),
//	// "fonts/georgia.ttf");
//	// ProductsDataModel obj = data.get(position);
//	// nameText.setTypeface(mFont);
//	// nameText.setText(obj.getName());
//	//
//	// long timeInMilliseconds = obj.getEnds_at();
//	// long end = timeInMilliseconds * 1000;
//	//
//	// Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//	// cal.setTimeInMillis(end);
//	//
//	// long timeInMillisecond = obj.getStarts_at();
//	// start = timeInMillisecond * 1000;
//	// currenttime = System.currentTimeMillis();
//	//
//	// Calendar cals = Calendar.getInstance(Locale.ENGLISH);
//	// cals.setTimeInMillis(start);
//	//
//	// if (start > currenttime) {
//	//
//	// String a = "https:" + obj.getTeaser_url();
//	// set_product_image.setTag(50000);
//	// AQuery aq = new AQuery(_scontext);
//	// aq.id(set_product_image).image(a);
//	// ColorMatrix matrix = new ColorMatrix();
//	// matrix.setSaturation(0);
//	// ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//	// set_product_image.setColorFilter(filter);
//	// f_l.setBackgroundResource(R.drawable.green_base);
//	//
//	// } else {
//	// String a = "https:" + obj.getTeaser_url();
//	// AQuery aq = new AQuery(_scontext);
//	// aq.id(set_product_image).image(a);
//	// }
//	//
//	// set_product_image.setOnClickListener(new OnClickListener() {
//	//
//	// @Override
//	// public void onClick(View v) {
//	// int a = (Integer) v.getTag();
//	// if (a == 50000) {
//	// _ResponsePopup();
//	// } else {
//	// ProductsDataModel cs = data.get(a);
//	// int _s = Integer.parseInt(cs.getPk());
//	// String _sf = cs.getShipping_fee();
//	// String _sp = cs.getShipping_period();
//	// String _fs = cs.getFree_shipping();
//	// DataHolderClass.getInstance().setShipping_fee(_sf);
//	// DataHolderClass.getInstance().setShipping_period(_sp);
//	// DataHolderClass.getInstance().setFree_shipping(_fs);
//	// DataHolderClass.getInstance().set_mainProductsPk(_s);
//	// Intent i = new Intent(_scontext, ProductListing.class);
//	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	// _scontext.startActivity(i);
//	// }
//	// }
//	// });
//	//
//	// return itemView;
//	// }
//	// }
//	//
//	// class TabAdapter extends BaseAdapter {
//	// Context _mcontext = null;
//	// LayoutInflater inflater;
//	// ArrayList<ProductsDataModel> data;
//	// View itemView;
//	//
//	// public TabAdapter(Context context,
//	// ArrayList<ProductsDataModel> arraylist) {
//	// this._mcontext = context;
//	// data = arraylist;
//	// }
//	//
//	// @Override
//	// public int getCount() {
//	// return data.size();
//	// }
//	//
//	// @Override
//	// public Object getItem(int position) {
//	// return null;
//	// }
//	//
//	// @Override
//	// public long getItemId(int position) {
//	// return 0;
//	// }
//	//
//	// @SuppressLint("ResourceAsColor")
//	// @Override
//	// public View getView(int position, View convertView, ViewGroup parent) {
//	// TextView ends_in, discount_rate, t;
//	// ImageButton go_for_sale;
//	//
//	// if (a >= 7 && a < 9) {
//	// inflater = (LayoutInflater) _mcontext.getApplicationContext()
//	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	// itemView = inflater.inflate(
//	// R.layout.seven_inch_product_display_inflator, parent,
//	// false);
//	// } else if (a >= 9) {
//	// inflater = (LayoutInflater) _mcontext.getApplicationContext()
//	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	// itemView = inflater.inflate(
//	// R.layout.ten_inch_product_display_inflator, parent,
//	// false);
//	// }
//	// t = (TextView) itemView.findViewById(R.id.t);
//	// ends_in = (TextView) itemView.findViewById(R.id.set_time);
//	// discount_rate = (TextView) itemView.findViewById(R.id.set_dicount);
//	// Typeface mFont = Typeface.createFromAsset(getActivity().getAssets(),
//	// "fonts/georgia.ttf");
//	// ends_in.setTypeface(mFont);
//	// discount_rate.setTypeface(mFont, Typeface.BOLD);
//	// t.setTypeface(mFont, Typeface.BOLD);
//	// ImageView imageView = (ImageView)
//	// itemView.findViewById(R.id.set_product_img);
//	// imageView.setTag(position);
//	// go_for_sale = (ImageButton) itemView.findViewById(R.id.go_for_sale);
//	// go_for_sale.setTag(position);
//	// if (position % 7 == 0) {
//	// ends_in.setBackgroundColor(Color.rgb(142, 19, 69));
//	// } else if (position % 7 == 1) {
//	// ends_in.setBackgroundColor(Color.rgb(29, 131, 107));
//	// } else if (position % 7 == 2) {
//	// ends_in.setBackgroundColor(Color.rgb(223, 12, 99));
//	// } else if (position % 7 == 3) {
//	// ends_in.setBackgroundColor(Color.rgb(31, 72, 102));
//	// } else if (position % 7 == 4) {
//	// ends_in.setBackgroundColor(Color.rgb(200, 17, 23));
//	// } else if (position % 7 == 5) {
//	// ends_in.setBackgroundColor(Color.rgb(90, 47, 97));
//	// } else if (position % 7 == 6) {
//	// ends_in.setBackgroundColor(Color.rgb(236, 71, 42));
//	// } else if (position % 7 == 7) {
//	// ends_in.setBackgroundColor(Color.rgb(169, 194, 21));
//	// }
//	//
//	// String hours_left_str,minutes_left_str,seconds_left_str;
//	//
//	// ProductsDataModel obj = data.get(position);
//	// long timeInMilliseconds = obj.getEnds_at();
//	// long end = timeInMilliseconds * 1000;
//	// long current = System.currentTimeMillis();
//	// long diff = end - current;
//	// int dayCount = (int) diff / (24 * 60 * 60 * 1000);
//	//
//	//
//	// int hours_left = (int) ((diff / (1000 * 60 * 60)) % 24);
//	// if(String.valueOf(hours_left).length()<2){
//	// hours_left_str="0"+String.valueOf(hours_left);
//	// }else{
//	// hours_left_str=String.valueOf(hours_left);
//	// }
//	//
//	// int minutes_left = (int) ((diff / (1000 * 60)) % 60);
//	// if(String.valueOf(minutes_left).length()<2){
//	// minutes_left_str="0"+String.valueOf(minutes_left);
//	// }else{
//	// minutes_left_str=String.valueOf(minutes_left);
//	// }
//	//
//	// int seconds_left = (int) ((diff / 1000) % 60);
//	// if(String.valueOf(seconds_left).length()<2){
//	// seconds_left_str="0"+String.valueOf(seconds_left);
//	// }else{
//	// seconds_left_str=String.valueOf(seconds_left);
//	//
//	// }
//	//
//	// Calendar cal = Calendar.getInstance(Locale.ENGLISH);
//	// cal.setTimeInMillis(end);
//	// String date = DateFormat.format("dd-MMMM-yyyy", cal).toString();
//	//
//	// String s = Integer.toString(dayCount) + " Days" + " "
//	// + hours_left_str + ":"
//	// + minutes_left_str + ":"
//	// + seconds_left_str;
//	//
//	// String _to = Integer.toString(hours_left) + ":"
//	// + Integer.toString(minutes_left) + ":"
//	// + Integer.toString(seconds_left);
//	//
//	// String _endDate = date+ "\n" + _to;
//	//
//	// long timeInMillisecond = obj.getStarts_at();
//	// start = timeInMillisecond * 1000L;
//	// currenttime = System.currentTimeMillis();
//	// long diffs = start -currenttime ;
//	// int hours_lefts = (int) ((diffs / (1000 * 60 * 60)) % 24);
//	// int minutes_lefts = (int) ((diffs / (1000 * 60)) % 60);
//	// int seconds_lefts = (int) ((diffs / 1000) % 60);
//	//
//	//
//	//
//	// Calendar cals = Calendar.getInstance(Locale.ENGLISH);
//	// cals.setTimeInMillis(start);
//	// String start_date = DateFormat.format("dd-MMMM-yyyy", cals).toString();
//	// String _from = Integer.toString(hours_lefts) + ":"
//	// + Integer.toString(minutes_lefts) + ":"
//	// + Integer.toString(seconds_lefts);
//	//
//	// String _startFrom = start_date +"\n"+ _from;
//	//
//	// if(start > currenttime){
//	// RelativeLayout base_layout =
//	// (RelativeLayout)itemView.findViewById(R.id.base_layout);
//	// base_layout.setBackgroundColor(Color.parseColor("#ADFF2F"));
//	// TextView aa,ab,bb,ba;
//	// aa = (TextView)itemView.findViewById(R.id.aa);
//	// ab = (TextView)itemView.findViewById(R.id.ab);
//	// bb = (TextView)itemView.findViewById(R.id.bb);
//	// ba = (TextView)itemView.findViewById(R.id.ba);
//	//
//	// aa.setTypeface(mFont, Typeface.NORMAL);
//	// ab.setTypeface(mFont, Typeface.NORMAL);
//	// bb.setTypeface(mFont, Typeface.NORMAL);
//	// ba.setTypeface(mFont, Typeface.NORMAL);
//	//
//	// aa.setVisibility(View.VISIBLE);
//	// ab.setVisibility(View.VISIBLE);
//	// bb.setVisibility(View.VISIBLE);
//	// ba.setVisibility(View.VISIBLE);
//	//
//	// ab.setText(_startFrom);
//	// ba.setText(_endDate);
//	//
//	// t.setVisibility(View.GONE);
//	// ends_in.setText(s);
//	// ends_in.setVisibility(View.GONE);
//	// discount_rate.setText(obj.getDiscount_text());
//	// discount_rate.setVisibility(View.GONE);
//	// String a = "https:" + obj.getTeaser_url();
//	// go_for_sale.setTag(50000);
//	// go_for_sale.setVisibility(View.GONE);
//	// imageView.setTag(50000);
//	// AQuery aq = new AQuery(_mcontext);
//	// aq.id(imageView).image(a);
//	// ColorMatrix matrix = new ColorMatrix();
//	// matrix.setSaturation(0);
//	// ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//	// imageView.setColorFilter(filter);
//	// }else{
//	// ends_in.setText(s);
//	// discount_rate.setText(obj.getDiscount_text());
//	// String a = "https:" + obj.getTeaser_url();
//	// AQuery aq = new AQuery(_mcontext);
//	// aq.id(imageView).image(a);
//	// }
//	//
//	//
//	// go_for_sale.setOnClickListener(new OnClickListener() {
//	//
//	// @Override
//	// public void onClick(View v) {
//	// int a = (Integer) v.getTag();
//	// if(a == 50000){
//	// _ResponsePopup();
//	// }else{
//	// ProductsDataModel cs = data.get(a);
//	// String _sf = cs.getShipping_fee();
//	// String _sp = cs.getShipping_period();
//	// String _fs = cs.getFree_shipping();
//	// DataHolderClass.getInstance().setShipping_fee(_sf);
//	// DataHolderClass.getInstance().setShipping_period(_sp);
//	// DataHolderClass.getInstance().setFree_shipping(_fs);
//	// int _s = Integer.parseInt(cs.getPk());
//	// DataHolderClass.getInstance().set_mainProductsPk(_s);
//	// Intent i = new Intent(_mcontext, ProductListing.class);
//	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	// _mcontext.startActivity(i);
//	// }
//	// }
//	// });
//	//
//	// imageView.setOnClickListener(new OnClickListener() {
//	//
//	// @Override
//	// public void onClick(View v) {
//	// int a = (Integer) v.getTag();
//	// if(a == 50000){
//	// _ResponsePopup();
//	// }else{
//	// ProductsDataModel cs = data.get(a);
//	// String _sf = cs.getShipping_fee();
//	// String _sp = cs.getShipping_period();
//	// String _fs = cs.getFree_shipping();
//	// DataHolderClass.getInstance().setShipping_fee(_sf);
//	// DataHolderClass.getInstance().setShipping_period(_sp);
//	// DataHolderClass.getInstance().setFree_shipping(_fs);
//	// int _s = Integer.parseInt(cs.getPk());
//	// DataHolderClass.getInstance().set_mainProductsPk(_s);
//	// Intent i = new Intent(_mcontext, ProductListing.class);
//	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	// _mcontext.startActivity(i);
//	//
//	// }
//	//
//	// }
//	// });
//	// return itemView;
//	// }
//	//
//	// }
//
//	private void responsePopup() {
//		View view = View.inflate(getActivity().getBaseContext(),
//				R.layout.error_popop, null);
//		TextView _seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
//		_seterrormsg.setText("Coming Soon!!!");
//		Toast toast = new Toast(getActivity().getBaseContext());
//		toast.setGravity(Gravity.CENTER, 0, 0);
//		toast.setView(view);
//		toast.show();
//	}
//}
