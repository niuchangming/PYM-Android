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
import com.brandsfever.luxury.R;
import com.brandsfever.widget.BFTextView;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductListDetailModel;

public class DynamicDisplayFragment extends Fragment {

	private static final String TAG = "DynamicDisplayFragment";

	public ArrayList<ProductListDetailModel> mDataList = new ArrayList<ProductListDetailModel>();
	Context _mctx;
	private String tab_name;
	static String s;
	GridView mGridView;
	Button mScrollup;
	PhoneAdpter mPAdapter;
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

		// if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
		// mPAdapter = new PhoneAdpter(getActivity(),
		// R.layout.phone_grid_inflator, mDataList);
		// mGridView.setAdapter(mPAdapter);
		// } else if (DataHolderClass.getInstance().get_deviceInch() >= 7
		// && DataHolderClass.getInstance().get_deviceInch() <= 8) {
		// mPAdapter = new PhoneAdpter(getActivity(),
		// R.layout.phone_grid_inflator, mDataList);
		// mGridView.setAdapter(mPAdapter);
		// } else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
		// mTAdapter = new TabAdapter(getActivity(),
		// R.layout.grid_row_inflator, mDataList);
		// mGridView.setAdapter(mTAdapter);
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
				holder.mImageView = (ImageView) convertView
						.findViewById(R.id.prdt_img);
				holder.soldOutImageView = (ImageView) convertView
						.findViewById(R.id.soldout_img);
				holder.noGoods = (BFTextView) convertView
						.findViewById(R.id.no_goods);
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
				holder.noGoods.setVisibility(View.GONE);
				holder.pBar.setVisibility(View.VISIBLE);
				String imageUrl = "https:" + obj.getImg();
				mAQuery.progress(holder.pBar);
				mAQuery.id(holder.mImageView).progress(R.id.progress)
						.image(imageUrl, false, true);
			} else {
				holder.noGoods.setVisibility(View.VISIBLE);
				holder.pBar.setVisibility(View.GONE);
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

	// private class PhoneAdpter extends BaseAdapter {
	//
	// private Context context;
	// private int layoutResourceId;
	// public ArrayList<ProductListDetailModel> data = new
	// ArrayList<ProductListDetailModel>();
	//
	// public PhoneAdpter(Context context, int layoutResourceId,
	// ArrayList<ProductListDetailModel> arraylist) {
	// this.layoutResourceId = layoutResourceId;
	// this.context = context;
	// this.data = arraylist;
	// }
	//
	// @Override
	// public int getCount() {
	// return data.size();
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
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// View itemView = convertView;
	// TextView name, salesprice, marketprice;
	// if (itemView == null) {
	// LayoutInflater inflater = ((Activity) context)
	// .getLayoutInflater();
	// itemView = inflater.inflate(layoutResourceId, parent, false);
	// }
	//
	// name = (TextView) itemView.findViewById(R.id.prdt_name);
	// salesprice = (TextView) itemView
	// .findViewById(R.id.prdt_sales_price);
	// marketprice = (TextView) itemView
	// .findViewById(R.id.prdt_mrkt_price);
	// try {
	// mFont = Typeface.createFromAsset(getActivity().getAssets(),
	// "fonts/georgia.ttf");
	// salesprice.setTypeface(mFont, Typeface.BOLD);
	// marketprice.setTypeface(mFont, Typeface.BOLD);
	// name.setTypeface(mFont, Typeface.NORMAL);
	// } catch (Exception e) {
	// Log.e(TAG, "Could not get typeface 'fonts/georgia.ttf' because"
	// + e.getMessage());
	// }
	//
	// ProductListDetailModel obj = data.get(position);
	//
	// name.setText(obj.getName());
	// salesprice.setText(obj.getSales_price().replace("GD", "$"));
	// marketprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
	// | Paint.ANTI_ALIAS_FLAG);
	// marketprice.setText(obj.getMarket_price().replace("GD", "$"));
	// String imageUrl = "https:" + obj.getImg();
	// ImageView imageView = (ImageView) itemView
	// .findViewById(R.id.prdt_img);
	// imageView.setTag(position);
	// AQuery aq = new AQuery(context);
	// aq.id(imageView).progress(R.id.progress)
	// .image(imageUrl, false, true);
	//
	// imageView.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// int a = (Integer) v.getTag();
	// ProductListDetailModel cs = data.get(a);
	// int _s = Integer.parseInt(cs.getPk());
	// DataHolderClass.getInstance().set_subProductsPk(_s);
	// Intent i = new Intent(context, SingleProductDisplay.class);
	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// context.startActivity(i);
	//
	// }
	// });
	//
	// ImageView soldOutImageView = (ImageView) itemView
	// .findViewById(R.id.soldout_img);
	// if (obj.get_availstock() == 0) {
	// soldOutImageView.setVisibility(View.VISIBLE);
	// } else {
	// soldOutImageView.setVisibility(View.GONE);
	// }
	// return itemView;
	// }
	//
	// }
	//
	// private class TabAdapter extends BaseAdapter {
	//
	// private Context context;
	// private int layoutResourceId;
	// public ArrayList<ProductListDetailModel> _data = new
	// ArrayList<ProductListDetailModel>();
	//
	// public TabAdapter(Context context, int layoutResourceId,
	// ArrayList<ProductListDetailModel> _listarray) {
	//
	// this.layoutResourceId = layoutResourceId;
	// this.context = context;
	// this._data = _listarray;
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
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// View itemView = convertView;
	// TextView _name, _salesprice, _marketprice;
	// if (itemView == null) {
	// LayoutInflater inflater = ((Activity) context)
	// .getLayoutInflater();
	// itemView = inflater.inflate(layoutResourceId, parent, false);
	// }
	//
	// _name = (TextView) itemView.findViewById(R.id.prdt_name);
	// _salesprice = (TextView) itemView
	// .findViewById(R.id.prdt_sales_price);
	// _marketprice = (TextView) itemView
	// .findViewById(R.id.prdt_mrkt_price);
	// try {
	// mFont = Typeface.createFromAsset(getActivity().getAssets(),
	// "fonts/georgia.ttf");
	// _salesprice.setTypeface(mFont);
	// _marketprice.setTypeface(mFont);
	// _name.setTypeface(mFont);
	// } catch (Exception e) {
	// Log.e(TAG, "Could not get typeface 'fonts/georgia.ttf' because"
	// + e.getMessage());
	// }
	// ProductListDetailModel obj = _data.get(position);
	// LinearLayout _lm = (LinearLayout) itemView
	// .findViewById(R.id.custom_layout);
	// if (obj.get_availstock() == 0) {
	// _lm.setBackgroundResource(R.drawable.img);
	// } else {
	// _lm.setBackgroundColor(Color.WHITE);
	// }
	//
	// _name.setText(obj.getName());
	// _salesprice.setText(obj.getSales_price().replace("GD", "$"));
	// _marketprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG
	// | Paint.ANTI_ALIAS_FLAG);
	// _marketprice.setText(obj.getMarket_price().replace("GD", "$"));
	// String a = "https:" + obj.getImg();
	// ImageView imageView = (ImageView) itemView
	// .findViewById(R.id.prdt_img);
	// imageView.setTag(position);
	// AQuery aq = new AQuery(context);
	// aq.id(imageView).image(a);
	//
	// imageView.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// int a = (Integer) v.getTag();
	// ProductListDetailModel cs = _data.get(a);
	// int _s = Integer.parseInt(cs.getPk());
	// DataHolderClass.getInstance().set_subProductsPk(_s);
	// Intent i = new Intent(context, SingleProductDisplay.class);
	// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// context.startActivity(i);
	// }
	// });
	//
	// return itemView;
	// }
	// }
}
