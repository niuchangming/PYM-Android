package com.brandsfever;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductListDetailModel;

public class DynamicDisplayFragment extends Fragment {
	public ArrayList<ProductListDetailModel> _dataList = new ArrayList<ProductListDetailModel>();
	Context _mctx;
	private String tab_name;
	static String s;
	GridView gridView;
	Button _scrollup;
	PhoneAdpter _padapter;
	TabAdapter _tadapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tab_name = getArguments().getString("name");
		Log.i("SWIPE", "tab name i frganment" + tab_name);
		_mctx = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View _view = inflater.inflate(
				R.layout.activity_dynamic_display_fragment, container, false);
		_scrollup = (Button) _view.findViewById(R.id.scrolldown);
		_scrollup.setVisibility(View.GONE);
		gridView = (GridView) _view.findViewById(R.id.gridView1);
		_filterDynamicData();

		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			_padapter = new PhoneAdpter(getActivity(),
					R.layout.phone_grid_inflator, _dataList);
			gridView.setAdapter(_padapter);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceInch() <= 8) {
			_padapter = new PhoneAdpter(getActivity(),
					R.layout.seven_inch_grid_inflator, _dataList);
			gridView.setAdapter(_padapter);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
			_tadapter = new TabAdapter(getActivity(),
					R.layout.grid_row_inflator, _dataList);
			gridView.setAdapter(_tadapter);
		}

		gridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
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
				gridView.setSelection(0);
				_scrollup.setVisibility(View.GONE);
			}
		});
		return _view;
	}

	public void _filterDynamicData() {
		// for(int i=0;i<ProductListing._ProductObjList.size();i++)
		// {
		// if(ProductListing._ProductObjList.get(i).get_catagory().equalsIgnoreCase(tab_name))
		// {
		// _dataList.add(ProductListing._ProductObjList.get(i));
		// }else{
		// _dataList.add(ProductListing._ProductObjList.get(i));
		// }
		// }
		if (!tab_name.equalsIgnoreCase("All Products")) {

			for (int i = 0; i < ProductListing._ProductObjList.size(); i++) {
				if (ProductListing._ProductObjList.get(i).get_catagory()
						.equalsIgnoreCase(tab_name)) {
					_dataList.add(ProductListing._ProductObjList.get(i));
				}
			}
		} else {
			_dataList = ProductListing._ProductObjList;
		}

	}

	// ==============================================================================================================//
	private class PhoneAdpter extends BaseAdapter {

		private Context context;
		private int layoutResourceId;
		public ArrayList<ProductListDetailModel> data = new ArrayList<ProductListDetailModel>();

		public PhoneAdpter(Context context, int layoutResourceId,
				ArrayList<ProductListDetailModel> _arraylist) {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = _arraylist;
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
			// TODO Auto-generated method stub
			View itemView = convertView;
			TextView _name, _salesprice, _marketprice;
			if (itemView == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				itemView = inflater.inflate(layoutResourceId, parent, false);
			}

			_name = (TextView) itemView.findViewById(R.id.prdt_name);
			_salesprice = (TextView) itemView
					.findViewById(R.id.prdt_sales_price);
			_marketprice = (TextView) itemView
					.findViewById(R.id.prdt_mrkt_price);
			Typeface mFont = Typeface.createFromAsset(
					getActivity().getAssets(), "fonts/georgia.ttf");
			_salesprice.setTypeface(mFont, Typeface.BOLD);
			_marketprice.setTypeface(mFont, Typeface.BOLD);
			_name.setTypeface(mFont, Typeface.NORMAL);

			ProductListDetailModel obj = data.get(position);

			_name.setText(obj.getName());
			_salesprice.setText(obj.getSales_price().replace("GD", "$"));
			int color = Integer.parseInt("B22222", 16) + 0xFF000000;
			_salesprice.setTextColor(color);
			_marketprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
					| Paint.ANTI_ALIAS_FLAG);
			_marketprice.setText(obj.getMarket_price().replace("GD", "$"));
			String a = "https:" + obj.getImg();
			System.out.println("value of a is+" + a);
			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.prdt_img);
			// ResizableImageView imageView = new
			// ResizableImageView(getActivity().getBaseContext());
			imageView.setTag(position);
			AQuery aq = new AQuery(context);
			aq.id(imageView).image(a);

			imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int a = (Integer) v.getTag();
					ProductListDetailModel cs = data.get(a);
					int _s = Integer.parseInt(cs.getPk());
					DataHolderClass.getInstance().set_subProductsPk(_s);
					System.out.println("pk is" + _s);
					Intent i = new Intent(context, SingleProductDisplay.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.push_out_to_left, R.anim.push_out_to_right);

				}
			});

			return itemView;
		}

	}

	// ==============================================================================================================//

	private class TabAdapter extends BaseAdapter {

		private Context context;
		private int layoutResourceId;
		public ArrayList<ProductListDetailModel> _data = new ArrayList<ProductListDetailModel>();

		public TabAdapter(Context context, int layoutResourceId,
				ArrayList<ProductListDetailModel> _listarray) {

			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this._data = _listarray;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return _data.size();
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
			// TODO Auto-generated method stub
			View itemView = convertView;
			TextView _name, _salesprice, _marketprice;
			if (itemView == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				itemView = inflater.inflate(layoutResourceId, parent, false);
			}

			_name = (TextView) itemView.findViewById(R.id.prdt_name);
			_salesprice = (TextView) itemView
					.findViewById(R.id.prdt_sales_price);
			_marketprice = (TextView) itemView
					.findViewById(R.id.prdt_mrkt_price);
			Typeface mFont = Typeface.createFromAsset(
					getActivity().getAssets(), "fonts/georgia.ttf");
			_salesprice.setTypeface(mFont);
			_marketprice.setTypeface(mFont);
			_name.setTypeface(mFont);

			ProductListDetailModel obj = _data.get(position);
			LinearLayout _lm = (LinearLayout) itemView
					.findViewById(R.id.custom_layout);
			if (obj.get_availstock() == 0) {
				_lm.setBackgroundResource(R.drawable.img);
			} else {
				_lm.setBackgroundColor(Color.WHITE);
			}

			_name.setText(obj.getName());
			_salesprice.setText(obj.getSales_price().replace("GD", "$"));
			_marketprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
					| Paint.ANTI_ALIAS_FLAG);
			_marketprice.setText(obj.getMarket_price().replace("GD", "$"));
			String a = "https:" + obj.getImg();
			System.out.println("value of a is+" + a);
			ImageView imageView = (ImageView) itemView
					.findViewById(R.id.prdt_img);
			imageView.setTag(position);
			AQuery aq = new AQuery(context);
			aq.id(imageView).image(a);

			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int a = (Integer) v.getTag();
					ProductListDetailModel cs = _data.get(a);
					int _s = Integer.parseInt(cs.getPk());
					DataHolderClass.getInstance().set_subProductsPk(_s);
					System.out.println("pk is" + _s);
					Intent i = new Intent(context, SingleProductDisplay.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					getActivity().overridePendingTransition(
							R.anim.push_out_to_left, R.anim.push_out_to_right);

				}
			});

			return itemView;
		}
	}
}
