package com.brandsfever;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.brandsfever.widget.BFTextView;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductListDetailModel;

/**
 * @modify ycb
 * @date 2015/3/7
 * 
 */

public class DynamicDisplayFragment extends Fragment {

	private static final String TAG = "DynamicDisplayFragment";

	public ArrayList<ProductListDetailModel> mDataList = new ArrayList<ProductListDetailModel>();
	Context _mctx;
	private String tab_name;
	static String s;
	GridView mGridView;
	Button mScrollup;
	PhoneAdpter mPAdapter;
	// TabAdapter mTAdapter;
	Typeface mFont;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tab_name = getArguments().getString("name");
		_mctx = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.activity_dynamic_display_fragment, container, false);
		mScrollup = (Button) view.findViewById(R.id.scrolldown);
		mScrollup.setVisibility(View.GONE);
		mGridView = (GridView) view.findViewById(R.id.gridView1);
		_filterDynamicData();

		/**
		 * at here ,it is need not do this. just need make more layout of the
		 * same name in different folder,and then use it;
		 */
		// if (DataHolderClass.getInstance().get_deviceInch() <= 7) {
		// mPAdapter = new PhoneAdpter(getActivity(),
		// R.layout.phone_grid_inflator, mDataList);
		// mGridView.setAdapter(mPAdapter);
		// } else if (DataHolderClass.getInstance().get_deviceInch() > 7
		// && DataHolderClass.getInstance().get_deviceInch() <= 8) {
		// mPAdapter = new PhoneAdpter(getActivity(),
		// R.layout.phone_grid_inflator, mDataList);
		// mGridView.setAdapter(mPAdapter);
		// } else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
		// mPAdapter = new PhoneAdpter(getActivity(),
		// R.layout.phone_grid_inflator, mDataList);
		// mGridView.setAdapter(mPAdapter);
		// // mTAdapter = new TabAdapter(getActivity(),
		// // R.layout.grid_row_inflator, mDataList);
		// // mGridView.setAdapter(mTAdapter);
		// }
		mPAdapter = new PhoneAdpter(getActivity(),
				R.layout.phone_grid_inflator, mDataList);
		mGridView.setAdapter(mPAdapter);

		mGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 4) {
					mScrollup.setVisibility(View.VISIBLE);
				} else {
					mScrollup.setVisibility(View.GONE);
				}
			}
		});

		mScrollup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGridView.setSelection(0);
				mScrollup.setVisibility(View.GONE);
			}
		});
		return view;
	}

	public void _filterDynamicData() {

		if (!tab_name.equalsIgnoreCase("All Products")) {

			for (int i = 0; i < ProductListing.mProductList.size(); i++) {
				if (ProductListing.mProductList.get(i).get_catagory()
						.equalsIgnoreCase(tab_name)) {
					mDataList.add(ProductListing.mProductList.get(i));
				}
			}
		} else {
			mDataList = ProductListing.mProductList;
		}

	}

	private class PhoneAdpter extends BaseAdapter implements OnClickListener {

		private Context context;
		private int layoutResourceId;
		public ArrayList<ProductListDetailModel> data = new ArrayList<ProductListDetailModel>();
		private AQuery mAQuery;

		public PhoneAdpter(Context context, int layoutResourceId,
				ArrayList<ProductListDetailModel> arraylist) {
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = arraylist;
			mAQuery = new AQuery(context);
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		/**
		 * User an holder to recyle use the item view, and this is just the
		 * first step to make it better.
		 * 
		 */
		private class Holder {
			private BFTextView name, salesPrice, marketPrice, noGoods;
			private ImageView mImageView, soldOutImageView;
			private ProgressBar pBar;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						layoutResourceId, parent, false);
				holder.name = (BFTextView) convertView
						.findViewById(R.id.prdt_name);
				holder.salesPrice = (BFTextView) convertView
						.findViewById(R.id.prdt_sales_price);
				holder.marketPrice = (BFTextView) convertView
						.findViewById(R.id.prdt_mrkt_price);
				holder.noGoods = (BFTextView) convertView
						.findViewById(R.id.no_goods);
				holder.mImageView = (ImageView) convertView
						.findViewById(R.id.prdt_img);
				holder.soldOutImageView = (ImageView) convertView
						.findViewById(R.id.soldout_img);
				holder.pBar = (ProgressBar) convertView
						.findViewById(R.id.progress);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			ProductListDetailModel obj = data.get(position);

			holder.name.setText(obj.getName());
			holder.salesPrice.setText(obj.getSales_price().replace("GD", "$"));
			holder.marketPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
					| Paint.ANTI_ALIAS_FLAG);
			holder.marketPrice
					.setText(obj.getMarket_price().replace("GD", "$"));
			if (obj.getImg() != null && !obj.getImg().isEmpty()) {
				holder.pBar.setVisibility(View.VISIBLE);
				holder.noGoods.setVisibility(View.GONE);
				String imageUrl = "https:" + obj.getImg();
				mAQuery.progress(holder.pBar);
				mAQuery.id(holder.mImageView).progress(R.id.progress)
						.image(imageUrl, false, true);
			} else {
				holder.pBar.setVisibility(View.GONE);
				holder.noGoods.setVisibility(View.VISIBLE);
			}

			holder.mImageView.setId(position);
			holder.mImageView.setOnClickListener(this);

			holder.soldOutImageView
					.setVisibility(obj.get_availstock() == 0 ? View.VISIBLE
							: View.GONE);
			return convertView;
		}

		@Override
		public void onClick(View v) {
			ProductListDetailModel cs = data.get(v.getId());
			int _s = Integer.parseInt(cs.getPk());
			DataHolderClass.getInstance().set_subProductsPk(_s);
			Intent i = new Intent(context, SingleProductDisplay.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);

		}

	}

	// private class TabAdapter extends BaseAdapter implements OnClickListener {
	//
	// private Context context;
	// private int layoutResourceId;
	// public ArrayList<ProductListDetailModel> _data = new
	// ArrayList<ProductListDetailModel>();
	// private AQuery mAQuery;
	//
	// public TabAdapter(Context context, int layoutResourceId,
	// ArrayList<ProductListDetailModel> _listarray) {
	//
	// this.layoutResourceId = layoutResourceId;
	// this.context = context;
	// this._data = _listarray;
	// mAQuery = new AQuery(context);
	// }
	//
	// @Override
	// public int getCount() {
	// return _data.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// return null;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return 0;
	// }
	//
	// private class Holder {
	// private BFTextView name, salesPrice, marketPrice;
	// private ImageView mImageView, soldOutImageView;
	// private View layout;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// Holder holder = null;
	// if (convertView == null) {
	// convertView = LayoutInflater.from(context).inflate(
	// layoutResourceId, parent, false);
	// holder = new Holder();
	// holder.name = (BFTextView) convertView
	// .findViewById(R.id.prdt_name);
	// holder.salesPrice = (BFTextView) convertView
	// .findViewById(R.id.prdt_sales_price);
	// holder.marketPrice = (BFTextView) convertView
	// .findViewById(R.id.prdt_mrkt_price);
	// holder.layout = convertView.findViewById(R.id.custom_layout);
	// holder.mImageView = (ImageView) convertView
	// .findViewById(R.id.prdt_img);
	// holder.soldOutImageView = (ImageView) convertView
	// .findViewById(R.id.soldout_img);
	// convertView.setTag(holder);
	// } else {
	// holder = (Holder) convertView.getTag();
	// }
	//
	// ProductListDetailModel obj = _data.get(position);
	//
	// // if (obj.get_availstock() == 0) {
	// // _lm.setBackgroundResource(R.drawable.img);
	// // } else {
	// // _lm.setBackgroundColor(Color.WHITE);
	// // }
	//
	// holder.name.setText(obj.getName());
	// holder.salesPrice.setText(obj.getSales_price().replace("GD", "$"));
	// holder.marketPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
	// | Paint.ANTI_ALIAS_FLAG);
	// holder.marketPrice
	// .setText(obj.getMarket_price().replace("GD", "$"));
	// String a = "https:" + obj.getImg();
	//
	// mAQuery.id(holder.mImageView).image(a);
	//
	// holder.mImageView.setId(position);
	// holder.mImageView.setOnClickListener(this);
	//
	// holder.soldOutImageView
	// .setVisibility(obj.get_availstock() == 0 ? View.VISIBLE
	// : View.GONE);
	// return convertView;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// ProductListDetailModel cs = _data.get(v.getId());
	// int _s = Integer.parseInt(cs.getPk());
	// DataHolderClass.getInstance().set_subProductsPk(_s);
	// Intent i = new Intent(context, SingleProductDisplay.class);
	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// context.startActivity(i);
	// }
	// }
}
