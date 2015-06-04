package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.brandsfever.widget.BFTextView;
import com.dataholder.DataHolderClass;
import com.datamodel.Campaign;
import com.datamodel.ProductsDataModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.niu.utils.JSONParser;
import com.niu.utils.JSONParser.ParserListener;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

public class ProductsFragment extends Fragment implements
		OnRefreshListener<StickyGridHeadersGridView>, ParserListener {

	private static final String TAG = "CampaignListFragment";
	private PullToRefreshGridView mCampaignGridView;
	private List<ProductsDataModel> products = new ArrayList<ProductsDataModel>();
	private BaseAdapter mAdapter;
	private String mCategoryUrl;
	private boolean mIsFirstLoad;

	public static ProductsFragment newInstance(String category) {
		ProductsFragment fragment = new ProductsFragment();

		Bundle args = new Bundle();
		args.putString("category", category);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View mSwipeLayout = inflater.inflate(R.layout.products_gridview,
				container, false);
		mCampaignGridView = (PullToRefreshGridView) mSwipeLayout
				.findViewById(R.id.campaign_gridview);
		BFTextView emptyView = new BFTextView(getActivity());
		emptyView.setPadding(20, 0, 0, 20);
		emptyView.setLayoutParams(new LayoutParams(-1, -1));
		emptyView.setGravity(Gravity.CENTER);
		emptyView.setTextSize(20);
		emptyView.setText(getString(R.string.empty_text));
		mCampaignGridView.setOnRefreshListener(this);
		mCampaignGridView.setEmptyView(emptyView);

		return mSwipeLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mCategoryUrl == null) {
			Bundle args = getArguments();
			String category = args.getString("category");
			
			mCategoryUrl = "https://api-1.brandsfever.com/campaigns/category/";
			if (category != null && category.length() > 0) {
				mCategoryUrl = mCategoryUrl + category + "/merge?channels=30&channels=110";
			}
			mIsFirstLoad = true;
		}

		if (mAdapter == null) {
			mAdapter = new TabAdapter();
		}
		mCampaignGridView.setAdapter(mAdapter);

		if (mIsFirstLoad) {
			new LoadProduct(mCategoryUrl).execute();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	class LoadProduct extends AsyncTask<Void, Void, List<ProductsDataModel>> implements OnCancelListener {
		ProgressHUD mProgressHUD;
		String mCategoryURL;

		LoadProduct(String category) {
			mCategoryURL = category;
		}

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(getActivity(), "Loading", true, true, this);
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		public void onCancel(DialogInterface arg0) {
			
		}

		@Override
		protected List<ProductsDataModel> doInBackground(Void... arg0) {
			products.clear();
			getProducts(mCategoryURL);
			return products;
		}

		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(List<ProductsDataModel> result) {
			if (mIsFirstLoad) {
				mIsFirstLoad = false;
			} else {
				Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
			}
			mProgressHUD.dismiss();

			if (result != null) {
				mAdapter.notifyDataSetChanged();
			}
			mCampaignGridView.onRefreshComplete();
		}
	}

	public void getProducts(String url) {
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpclient = HttpsClient.getNewHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			int responsecode = httpresponse.getStatusLine().getStatusCode();
			if (responsecode == 200) {
				InputStream inputstream = httpresponse.getEntity().getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				String data = total.toString();
				if(this.getActivity() != null){
					JSONParser.getInstance(this).start(data, Campaign.class);
				} 
			} else {
				Log.e(TAG, "error");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class TabAdapter extends BaseAdapter implements OnClickListener, StickyGridHeadersSimpleAdapter {
		private AQuery mAQuery;
		private ColorMatrixColorFilter colorFilter;

		public TabAdapter() {
			mAQuery = new AQuery(getActivity());
			ColorMatrix matrix = new ColorMatrix();
			matrix.setSaturation(0);
			colorFilter = new ColorMatrixColorFilter(matrix);
		}

		@Override
		public int getCount() {
			return products.size();
		}

		@Override
		public Object getItem(int position) {
			return products.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.products_grid_item, parent, false);
				holder.name = (BFTextView) convertView.findViewById(R.id.name);
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

			holder.mImageView.clearColorFilter();
			ProductsDataModel obj = products.get(position);
			long timeInMillisecond = obj.getStarts_at();
			long start = timeInMillisecond * 1000L;
			long currenttime = System.currentTimeMillis();

			holder.name.setText(obj.getName());
			if (start > currenttime) {
				holder.discountRate.setText("Coming Soon!!!");
				holder.mImageView.setId(-1);
				holder.mImageView.setColorFilter(colorFilter);
			} else {
				holder.discountRate.setVisibility(View.VISIBLE);
				holder.mImageView.setId(position);
				holder.discountRate.setText(obj.getDiscount_text());
			}

			if (obj.getTeaser_square_url() != null
					&& obj.getTeaser_square_url().length() > 0) {
				holder.pBar.setVisibility(View.VISIBLE);
				holder.noGoods.setVisibility(View.GONE);
				String imgUrl = "https:" + obj.getTeaser_square_url();
				mAQuery.progress(holder.pBar);
				mAQuery.id(holder.mImageView).image(imgUrl);
			} else {
				holder.pBar.setVisibility(View.GONE);
				holder.noGoods.setVisibility(View.VISIBLE);
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
			if (v.getId() != -1) {
				ProductsDataModel cs = products.get(v.getId());
				String _sf = cs.getShipping_fee();
				String _sp = cs.getShipping_period();
				String _fs = cs.getFree_shipping();
				DataHolderClass.getInstance().setShipping_fee(_sf);
				DataHolderClass.getInstance().setShipping_period(_sp);
				DataHolderClass.getInstance().setFree_shipping(_fs);
				int _s = Integer.parseInt(cs.getPk());
				DataHolderClass.getInstance().set_mainProductsPk(_s);
				Intent i = new Intent(getActivity(), ProductListing.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(i);
			}
		}

		@Override
		public long getHeaderId(int position) {
			return products.get(position).getSection();
		}

		@Override
		public View getHeaderView(int position, View convertView, ViewGroup parent) {
			HeaderViewHolder mHeaderHolder;
			if (convertView == null) {
				mHeaderHolder = new HeaderViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.sticky_gridview_header, parent, false);
				mHeaderHolder.mTextView = (TextView) convertView
						.findViewById(R.id.sticky_header);
				convertView.setTag(mHeaderHolder);
			} else {
				mHeaderHolder = (HeaderViewHolder) convertView.getTag();
			}
			
			switch(products.get(position).section){
				case 30:
					mHeaderHolder.mTextView.setText(getActivity().getResources().getString(R.string.channel_30));
					break;
				case 110:
					mHeaderHolder.mTextView.setText(getActivity().getResources().getString(R.string.channel_110));
					break;
			}

			return convertView;
		}
		
		public class HeaderViewHolder {
			public TextView mTextView;
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<StickyGridHeadersGridView> refreshView) {
		if (mCategoryUrl != null)
			new LoadProduct(mCategoryUrl).execute();
	}

	@Override
	public void onParseSuccess(Object obj) {
		if(obj instanceof ArrayList){
			@SuppressWarnings("unchecked")
			List<Campaign> campaigns = (List<Campaign>) obj;
			for(Campaign cam : campaigns){
				for(ProductsDataModel product : cam.campaign_items){
					product.setSection(cam.channel);
					products.add(product);
				}
			}
		}else{
			Toast.makeText(getActivity(), "Parse failed.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onParseError(String err) {
		Toast.makeText(this.getActivity(), err, Toast.LENGTH_LONG).show();
	}
	
}
