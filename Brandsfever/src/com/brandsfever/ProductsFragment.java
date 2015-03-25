package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.brandsfever.widget.BFTextView;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductsDataModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class ProductsFragment extends Fragment implements
		OnRefreshListener<GridView> {

	private static final String TAG = "CampaignListFragment";
	private PullToRefreshGridView mCampaignGridView;
	// private Button mScrollUp;
	private ArrayList<ProductsDataModel> mCampaigns;
	// private SwipeRefreshLayout mSwipeLayout;
	private BaseAdapter mAdapter;
	private String mCategoryUrl;
	private boolean mIsVisible;
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
		mCampaigns = new ArrayList<ProductsDataModel>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View mSwipeLayout = inflater.inflate(R.layout.products_gridview,
				container, false);
		// This method has no effect
		// mSwipeLayout.setOnRefreshListener(this);
		// mSwipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
		// android.R.color.holo_green_light,
		// android.R.color.holo_orange_light,
		// android.R.color.holo_red_light);

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
		// mScrollUp = (Button) mSwipeLayout.findViewById(R.id.scrolldown);
		// mScrollUp.setVisibility(View.GONE);

		mCampaignGridView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// if (firstVisibleItem > 4) {
				// mScrollUp.setVisibility(View.VISIBLE);
				// } else {
				// mScrollUp.setVisibility(View.GONE);
				// }
			}
		});
		// mScrollUp.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// mCampaignList.setSelection(0);
		// mScrollUp.setVisibility(View.GONE);
		// }
		// });

		return mSwipeLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if (mCategoryUrl == null) {
			Bundle args = getArguments();
			String category = args.getString("category");

			mCategoryUrl = "https://api-1.brandsfever.com/campaigns/channel/"
					+ getActivity().getResources().getString(
							R.string.channel_code);
			if (category != null && category.length() > 0) {
				mCategoryUrl = mCategoryUrl + "/category/" + category;
			}
			mIsFirstLoad = true;
		}

		if (mAdapter == null) {
			mAdapter = new TabAdapter(getActivity(), mCampaigns);
		}
		mCampaignGridView.setAdapter(mAdapter);

		if (mIsFirstLoad) {
			new LoadProduct(mCategoryUrl).execute();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		mIsVisible = isVisibleToUser;
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
		// ((ViewGroup) mSwipeLayout.getParent()).removeView(mSwipeLayout);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	// public void responsePopup() {
	// View view = View.inflate(getActivity().getBaseContext(),
	// R.layout.error_popop, null);
	// TextView seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
	// seterrormsg.setText("Coming Soon!!!");
	// Toast toast = new Toast(getActivity().getBaseContext());
	// toast.setGravity(Gravity.CENTER, 0, 0);
	// toast.setView(view);
	// toast.show();
	// }

	class LoadProduct extends
			AsyncTask<Void, Void, ArrayList<ProductsDataModel>> implements
			OnCancelListener {
		ProgressHUD mProgressHUD;
		String mCategoryURL;

		LoadProduct(String category) {
			mCategoryURL = category;
		}

		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(getActivity(), "Loading", true,
					true, this);
			DisplayMetrics displaymetrics = new DisplayMetrics();
			int displayHeight = displaymetrics.heightPixels;
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams wmlp = mProgressHUD.getWindow()
					.getAttributes();
			wmlp.y = displayHeight / 4;
			mProgressHUD.getWindow().setAttributes(wmlp);
			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		public void onCancel(DialogInterface arg0) {
		}

		@Override
		protected ArrayList<ProductsDataModel> doInBackground(Void... arg0) {

			ArrayList<ProductsDataModel> campaigns = getProducts(mCategoryURL);
			return campaigns;
		}

		protected void onProgressUpdate(String... values) {
			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<ProductsDataModel> result) {

			if (mIsFirstLoad) {
				mIsFirstLoad = false;
			} else {
				Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT)
						.show();
			}
			mProgressHUD.dismiss();

			if (result != null) {
				mCampaigns.clear();
				mCampaigns.addAll(result);
				// if (mCampaigns != null && mCampaigns.size() > 0) {
				// Calendar cal = Calendar.getInstance(Locale.ENGLISH);
				// }

				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}
			}
			mCampaignGridView.onRefreshComplete();
		}
	}

	public ArrayList<ProductsDataModel> getProducts(String url) {

		ArrayList<ProductsDataModel> campaigns = null;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpclient = HttpsClient.getNewHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			int responsecode = httpresponse.getStatusLine().getStatusCode();
			if (responsecode == 200) {
				campaigns = new ArrayList<ProductsDataModel>();
				InputStream inputstream = httpresponse.getEntity().getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						inputstream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				String data = total.toString();
				JSONObject obj = new JSONObject(data);
				JSONArray get_campaigns = obj.getJSONArray("campaigns");
				for (int i = 0; i < get_campaigns.length(); i++) {
					JSONObject jsonobj = get_campaigns.getJSONObject(i);
					long ends_at = jsonobj.getLong("ends_at");
					String teaser_url = jsonobj.getString("teaser_url");
					String teaser_square_url = jsonobj
							.getString("teaser_square_url");
					String name = jsonobj.getString("name");
					long starts_at = jsonobj.getLong("starts_at");
					String pk = jsonobj.getString("pk");
					String express = jsonobj.getString("express");
					String discount_text = jsonobj.getString("discount_text");
					String shipping_fee = jsonobj.getString("shipping_fee");
					String shipping_period = jsonobj
							.getString("shipping_period");
					String free_shipping = jsonobj.getString("free_shipping");

					ProductsDataModel campaign = new ProductsDataModel();
					campaign.setEnds_at(ends_at);
					campaign.setTeaser_url(teaser_url);
					campaign.setTeaser_square_url(teaser_square_url);
					campaign.setName(name);
					campaign.setStarts_at(starts_at);
					campaign.setPk(pk);
					campaign.setExpress(express);
					campaign.setDiscount_text(discount_text);
					campaign.setFree_shipping(free_shipping);
					campaign.setShipping_fee(shipping_fee);
					campaign.setShipping_period(shipping_period);

					campaigns.add(campaign);
				}
			} else {
				Log.e(TAG, "error");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return campaigns;
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

		@SuppressLint({ "ResourceAsColor", "NewApi" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = LayoutInflater.from(_mcontext).inflate(
						R.layout.products_grid_item, parent, false);
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

			// int a = DataHolderClass.getInstance().get_deviceInch();

			holder.mImageView.clearColorFilter();
			ProductsDataModel obj = data.get(position);
			long timeInMillisecond = obj.getStarts_at();
			long start = timeInMillisecond * 1000L;
			long currenttime = System.currentTimeMillis();

			holder.name.setText(obj.getName());
			if (start > currenttime) {
				holder.discountRate.setText("Coming Soon!!!");
				holder.mImageView.setId(-1);
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
			// if (v.getId() == 50000) {
			// responsePopup();
			// } else {
			if (v.getId() != -1) {
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

	@Override
	public void onRefresh(PullToRefreshBase<GridView> refreshView) {
		if (mCategoryUrl != null)
			new LoadProduct(mCategoryUrl).execute();
	}
}
