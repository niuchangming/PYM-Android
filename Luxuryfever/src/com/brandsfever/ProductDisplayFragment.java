package com.brandsfever;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.brandsfever.luxury.R;
import com.brandsfever.widget.BFTextView;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductsDataModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * @modify ycb
 * @date 2015/3/8
 * 
 */
public class ProductDisplayFragment extends Fragment implements
		OnRefreshListener<GridView> {

	public static final String DISPLAY_TAG = "display_tag";
	public static final int ALL = 0;
	public static final int HAND_BAGS = 1;
	public static final int SHOES = 2;

	PullToRefreshGridView mGridView;
	Button scrollTopBtn;
	long start, currenttime;
	private TabAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.products_grid, container, false);
		mGridView = (PullToRefreshGridView) view
				.findViewById(R.id.products_gridview);
		mGridView.setOnRefreshListener(this);
		scrollTopBtn = (Button) view.findViewById(R.id.scroll_to_top);
		scrollTopBtn.setVisibility(View.GONE);

		mGridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem > 4) {
					scrollTopBtn.setVisibility(View.VISIBLE);
				} else {
					scrollTopBtn.setVisibility(View.GONE);
				}
			}
		});
		scrollTopBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mGridView.getRefreshableView().setSelection(0);
				scrollTopBtn.setVisibility(View.GONE);
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		ArrayList<ProductsDataModel> productList = null;
		int tag = getArguments().getInt(DISPLAY_TAG, ALL);
		switch (tag) {
		case HAND_BAGS:
			productList = ((ProductDisplay) getActivity()).handbags_prdt;
			break;
		case SHOES:
			productList = ((ProductDisplay) getActivity()).shoes_prdt;
			break;
		default:
			productList = ((ProductDisplay) getActivity()).all_prdt;
			break;
		}
		mAdapter = new TabAdapter(getActivity(), productList);
		mGridView.setAdapter(mAdapter);
		super.onActivityCreated(savedInstanceState);
	}

	class TabAdapter extends BaseAdapter implements OnClickListener {
		Context _mcontext = null;
		ArrayList<ProductsDataModel> data;
		private AQuery mAQuery;
		private ColorMatrixColorFilter colorFilter;

		public TabAdapter(Context context,
				ArrayList<ProductsDataModel> arraylist) {
			this._mcontext = context;
			data = arraylist;
			mAQuery = new AQuery(getActivity());
			ColorMatrix matrix = new ColorMatrix();
			matrix.setSaturation(0);
			colorFilter = new ColorMatrixColorFilter(matrix);
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

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(_mcontext).inflate(
						R.layout.products_grid_item, null);
				holder.name = (BFTextView) convertView.findViewById(R.id.name);
				// holder.endsIn = (BFTextView) convertView
				// .findViewById(R.id.endtime);
				holder.discountRate = (BFTextView) convertView
						.findViewById(R.id.discount);
				holder.noGoods = (BFTextView) convertView
						.findViewById(R.id.no_goods);
				holder.mImageView = (ImageView) convertView
						.findViewById(R.id.product_image);
				holder.pBar = (ProgressBar) convertView
						.findViewById(R.id.progress);
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			// int a = DataHolderClass.getInstance().get_deviceInch();
			long start, currenttime;

			holder.mImageView.clearColorFilter();
			ProductsDataModel obj = data.get(position);
			long timeInMillisecond = obj.getStarts_at();
			start = timeInMillisecond * 1000L;
			currenttime = System.currentTimeMillis();

			holder.name.setText(obj.getName());
			// holder.endsIn.setText(getEndNotice(obj.getEnds_at()));
			if (start > currenttime) {
				holder.discountRate.setVisibility(View.GONE);
				holder.mImageView.setId(50000);
				// String imgUrl = "https:" + obj.getTeaser_url();
				// AQuery aq = new AQuery(_mcontext);
				// aq.id(holder.mImageView).image(imgUrl);
				// ColorMatrix matrix = new ColorMatrix();
				// matrix.setSaturation(0);
				// ColorMatrixColorFilter filter = new ColorMatrixColorFilter(
				// matrix);
				holder.mImageView.setColorFilter(colorFilter);
			} else {
				holder.discountRate.setVisibility(View.VISIBLE);
				holder.mImageView.setId(position);
				holder.discountRate.setText(obj.getDiscount_text());
				// String imgUrl = "https:" + obj.getTeaser_url();
				// AQuery aq = new AQuery(_mcontext);
				// aq.id(holder.mImageView).image(imgUrl);
			}

			if (obj.getTeaser_square_url() != null
					&& !obj.getTeaser_square_url().isEmpty()) {
				holder.pBar.setVisibility(View.VISIBLE);
				holder.noGoods.setVisibility(View.GONE);
				String imgUrl = "https:" + obj.getTeaser_square_url();
				// AQuery aq = new AQuery(_mcontext);
				mAQuery.progress(holder.pBar);
				mAQuery.id(holder.mImageView).image(imgUrl);
			} else {
				holder.noGoods.setVisibility(View.VISIBLE);
				holder.pBar.setVisibility(View.GONE);
			}
			holder.mImageView.setOnClickListener(this);
			return convertView;
		}

		private class Holder {
			BFTextView discountRate, name, noGoods;
			ImageView mImageView;
			ProgressBar pBar;
		}

		public String getEndNotice(long endTime) {
			long time = endTime * 1000 - System.currentTimeMillis();
			StringBuilder sb = new StringBuilder();
			sb.append("Ends in ");
			int day = (int) (time / (24 * 60 * 60 * 1000));
			sb.append(day);
			sb.append(" Days");
			return sb.toString();
		}

		@Override
		public void onClick(View v) {
			if (v.getId() == 50000) {
				responsePopup();
			} else {
				ProductsDataModel cs = data.get(v.getId());
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
	}

	private void responsePopup() {
		View view = View.inflate(getActivity().getBaseContext(),
				R.layout.error_popop, null);
		TextView _seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		_seterrormsg.setText("Coming Soon!!!");
		Toast toast = new Toast(getActivity().getBaseContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		// fix me
		mGridView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mGridView.onRefreshComplete();
			}
		}, 3000);
	}
}
