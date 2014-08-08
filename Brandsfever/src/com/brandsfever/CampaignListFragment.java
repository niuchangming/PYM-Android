package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.dataholder.DataHolderClass;
import com.datamodel.ProductsDataModel;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class CampaignListFragment extends Fragment implements OnRefreshListener {

	private static final String TAG = "CampaignListFragment";

	private ListView mCampaignList;
//	private Button mScrollUp;
	private ArrayList<ProductsDataModel> mCampaigns;
	private SwipeRefreshLayout mSwipeLayout;
	private PhoneAdapter mAdapter;
	private String mCategoryName;
	private boolean mIsVisible;
	
	public static CampaignListFragment newInstance(String category) {

		CampaignListFragment fragment = new CampaignListFragment(category);
		return fragment;
	}

	CampaignListFragment(){
		mCategoryName = "all";
	}
	
	CampaignListFragment(String category) {
		mCategoryName = category;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCampaigns = new ArrayList<ProductsDataModel>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mSwipeLayout = (SwipeRefreshLayout) inflater.inflate(
				R.layout.campaign_list, null);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setColorSchemeColors(android.R.color.holo_blue_bright, 
                android.R.color.holo_green_light, 
                android.R.color.holo_orange_light, 
                android.R.color.holo_red_light);
		mCampaignList = (ListView) mSwipeLayout.findViewById(R.id.campaign_list);
//		mScrollUp = (Button) mSwipeLayout.findViewById(R.id.scrolldown);
//		mScrollUp.setVisibility(View.GONE);
		mAdapter = new PhoneAdapter(getActivity(), mCampaigns);
		mCampaignList.setAdapter(mAdapter);

		mCampaignList.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
//				if (firstVisibleItem > 4) {
//					mScrollUp.setVisibility(View.VISIBLE);
//				} else {
//					mScrollUp.setVisibility(View.GONE);
//				}
			}
		});
//		mScrollUp.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mCampaignList.setSelection(0);
//				mScrollUp.setVisibility(View.GONE);
//			}
//		});

		if (mCampaigns.isEmpty()) {
			new LoadProduct(mCategoryName).execute();
		} 

		return mSwipeLayout;
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
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
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
			if (convertView == null) {
				itemView = getActivity().getLayoutInflater().inflate(
						R.layout.phone_product_display_inflator, parent, false);
			} else {
				itemView = convertView;
			}

			if(position >= data.size())
				return itemView;

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

			String hours_left_str, minutes_left_str, seconds_left_str;

			long timeInMilliseconds = obj.getEnds_at();
			long end = timeInMilliseconds * 1000;
			long current = System.currentTimeMillis();
			long diff = end - current;
			int dayCount = (int) diff / (24 * 60 * 60 * 1000);

			int hours_left = (int) ((diff / (1000 * 60 * 60)) % 24);
			if (String.valueOf(hours_left).length() < 2) {
				hours_left_str = "0" + String.valueOf(hours_left);
			} else {
				hours_left_str = String.valueOf(hours_left);
			}

			int minutes_left = (int) ((diff / (1000 * 60)) % 60);
			if (String.valueOf(minutes_left).length() < 2) {
				minutes_left_str = "0" + String.valueOf(minutes_left);
			} else {
				minutes_left_str = String.valueOf(minutes_left);
			}

			int seconds_left = (int) ((diff / 1000) % 60);
			if (String.valueOf(seconds_left).length() < 2) {
				seconds_left_str = "0" + String.valueOf(seconds_left);
			} else {
				seconds_left_str = String.valueOf(seconds_left);
			}

			Calendar cal = Calendar.getInstance(Locale.ENGLISH);
			cal.setTimeInMillis(end);
			String date = DateFormat.format("dd-MMMM-yyyy", cal).toString();

			String s = Integer.toString(dayCount) + " Days" + " "
					+ hours_left_str + ":" + minutes_left_str + ":"
					+ seconds_left_str;

			String _to = Integer.toString(hours_left) + ":"
					+ Integer.toString(minutes_left) + ":"
					+ Integer.toString(seconds_left);

			String _endDate = date + "\n" + _to;

			long timeInMillisecond = obj.getStarts_at();
			long start = timeInMillisecond * 1000;
			long currenttime = System.currentTimeMillis();
			long diffs = start - currenttime;
			int hours_lefts = (int) ((diffs / (1000 * 60 * 60)) % 24);
			int minutes_lefts = (int) ((diffs / (1000 * 60)) % 60);
			int seconds_lefts = (int) ((diffs / 1000) % 60);

			Calendar cals = Calendar.getInstance(Locale.ENGLISH);
			cals.setTimeInMillis(start);
			String start_date = DateFormat.format("dd-MMMM-yyyy", cals)
					.toString();
			String _from = Integer.toString(hours_lefts) + ":"
					+ Integer.toString(minutes_lefts) + ":"
					+ Integer.toString(seconds_lefts);

			String _startFrom = start_date + "\n" + _from;

			if (start > currenttime) {
				ends_in.setText(s);
				ends_in.setVisibility(View.GONE);
				discount_rate.setText(obj.getDiscount_text());
				discount_rate.setVisibility(View.GONE);
				TextView set_from = (TextView) itemView
						.findViewById(R.id.set_from);
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
				ColorMatrixColorFilter filter = new ColorMatrixColorFilter(
						matrix);
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
						responsePopup();
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
						responsePopup();
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

	public void responsePopup() {
		View view = View.inflate(getActivity().getBaseContext(),
				R.layout.error_popop, null);
		TextView seterrormsg = (TextView) view.findViewById(R.id._seterrormsg);
		seterrormsg.setText("Coming Soon!!!");
		Toast toast = new Toast(getActivity().getBaseContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}

	class LoadProduct extends AsyncTask<Void, Void, Void> implements
			OnCancelListener {
//		ProgressHUD mProgressHUD;
		String mCategoryName;

		LoadProduct(String category) {
			mCategoryName = category;
		}

		@Override
		protected void onPreExecute() {
//			mProgressHUD = ProgressHUD.show(getActivity(), "Loading", true,
//					true, this);
//			DisplayMetrics displaymetrics = new DisplayMetrics();
//			int displayHeight = displaymetrics.heightPixels;
//			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
//			WindowManager.LayoutParams wmlp = mProgressHUD.getWindow()
//					.getAttributes();
//			wmlp.y = displayHeight / 4;
//			mProgressHUD.getWindow().setAttributes(wmlp);
//			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}

		@Override
		public void onCancel(DialogInterface arg0) {
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			mCampaigns.clear();

			String url_campaigns = "https://api-1.brandsfever.com/campaigns/channel/"
					+ getActivity().getResources().getString(
							R.string.channel_code);
			if (mCategoryName != null && mCategoryName.length() > 0) {
				url_campaigns = url_campaigns + "/category/" + mCategoryName;
			}
			GetProducts(url_campaigns);
			return null;
		}

		protected void onProgressUpdate(String... values) {
//			mProgressHUD.setMessage(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
//			mProgressHUD.dismiss();
			if(mIsVisible)
				Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
			mAdapter.notifyDataSetChanged();
		}
	}

	public void GetProducts(String url) {
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
				try {
					JSONObject obj = new JSONObject(data);
					JSONArray get_campaigns = obj.getJSONArray("campaigns");
					for (int i = 0; i < get_campaigns.length(); i++) {
						JSONObject jsonobj = get_campaigns.getJSONObject(i);
						long ends_at = jsonobj.getLong("ends_at");
						String teaser_url = jsonobj.getString("teaser_url");
						String name = jsonobj.getString("name");
						long starts_at = jsonobj.getLong("starts_at");
						String pk = jsonobj.getString("pk");
						String express = jsonobj.getString("express");
						String discount_text = jsonobj
								.getString("discount_text");
						String shipping_fee = jsonobj.getString("shipping_fee");
						String shipping_period = jsonobj
								.getString("shipping_period");
						String free_shipping = jsonobj
								.getString("free_shipping");

						ProductsDataModel campaign = new ProductsDataModel();

						campaign.setEnds_at(ends_at);
						campaign.setTeaser_url(teaser_url);
						campaign.setName(name);
						campaign.setStarts_at(starts_at);
						campaign.setPk(pk);
						campaign.setExpress(express);
						campaign.setDiscount_text(discount_text);
						campaign.setFree_shipping(free_shipping);
						campaign.setShipping_fee(shipping_fee);
						campaign.setShipping_period(shipping_period);

						mCampaigns.add(campaign);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Log.e(TAG, "error");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		new LoadProduct(mCategoryName).execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 2000);
	}

}
