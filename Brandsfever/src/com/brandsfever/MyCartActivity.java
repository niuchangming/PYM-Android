package com.brandsfever;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.androidquery.AQuery;
import com.brandsfever.widget.PinnedSectionListView;
import com.brandsfever.widget.PinnedSectionListView.PinnedSectionListAdapter;
import com.dataholder.DataHolderClass;
import com.datamodel.Cart;
import com.datamodel.OrderInfoModel;
import com.niu.utils.JSONParser;
import com.niu.utils.JSONParser.ParserListener;
import com.progressbar.ProgressHUD;
import com.ssl.HttpsClient;
import com.ssl.TrustAllCertificates;

public class MyCartActivity extends SherlockFragmentActivity implements OnClickListener, ParserListener{
	private TextView payable_amount;
	private PinnedSectionListView cartListView;
	private Button mContinueShopping, mCheckOutCart;
	private String mToken;
	private String mUserId;
	private MyCartAdapter mOrderAdapter;
	private List<Cart> carts = new ArrayList<Cart>();
	private List<OrderInfoModel> orders = new ArrayList<OrderInfoModel>();
	private Typeface mTypeface;
	private SharedPreferences mPref;

	int mDisplayItems = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		int a = DataHolderClass.getInstance().get_deviceInch();
		if (a < 7) {
			setContentView(R.layout.activity_my_cart_screen);
		} else if (a >= 7 && a < 9) {
			setContentView(R.layout.seven_inch_my_cart_screen);
		} else if (a >= 9) {
			setContentView(R.layout.my_cart_screen_tab);
		}
		
		init();
		setupActionBar();
		new GetAllCarts().execute();
	}
	
	private void init(){
		payable_amount = (TextView) findViewById(R.id.payable_amount);
		cartListView = (PinnedSectionListView) findViewById(R.id.mycartlist);
		mCheckOutCart = (Button) findViewById(R.id.checkout_cart);
		mCheckOutCart.setOnClickListener(this);
		mContinueShopping = (Button) findViewById(R.id.continue_shopping);
		mContinueShopping.setOnClickListener(this);
		
		mTypeface = Typeface.createFromAsset(getAssets(), "fonts/georgia.ttf");
		payable_amount.setTypeface(mTypeface, Typeface.NORMAL);
		mContinueShopping.setTypeface(mTypeface, Typeface.NORMAL);
		mCheckOutCart.setTypeface(mTypeface, Typeface.NORMAL);
		
		cartListView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
		    @SuppressLint("NewApi")
			@Override
		    public void onGlobalLayout() {
		    	cartListView.setOffsetLeft(cartListView.getMeasuredWidth() - 96 * getResources().getDisplayMetrics().density);
		    	if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
		    		cartListView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    	} else {
		    		cartListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
		    	}
		    }
		});
		mPref = this.getSharedPreferences("mypref", 0);
		mUserId = mPref.getString("ID", null);
		mToken = mPref.getString("TOKEN", null);
		
		mOrderAdapter = new MyCartAdapter();
		cartListView.setAdapter(mOrderAdapter);
	}
	
	@SuppressLint("InflateParams")
	private void setupActionBar(){
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.header_bar, null);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);

		final ImageButton actionBarBack = (ImageButton) findViewById(R.id.header_bar_left);
		actionBarBack.setImageDrawable(getResources().getDrawable(R.drawable.back_button));
		actionBarBack.setOnClickListener(this);
		
		TextView titleTV = (TextView)findViewById(R.id.header_bar_title);
		titleTV.setText("Shopping Cart");
	}

	class GetAllCarts extends AsyncTask<Void, Void, List<OrderInfoModel>> implements OnCancelListener {
		ProgressHUD mProgressHUD;
		
		@Override
		protected void onPreExecute() {
			mDisplayItems = 0;
			mProgressHUD = ProgressHUD.show(MyCartActivity.this, "Loading", true, true, this);
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}
		
		@Override
		public void onCancel(DialogInterface dialog) {}
		
		@Override
		protected List<OrderInfoModel> doInBackground(Void... arg0) {
			orders.clear();
			getCarts();
			return orders;
		}
		
		@Override
		protected void onPostExecute(List<OrderInfoModel> result) {
			if (result != null && result.size() > 0) {
				mOrderAdapter.notifyDataSetChanged();
			} else {
				responsePopup("Your cart \n is empty");
			}
			mProgressHUD.dismiss();
		}
	}
	
	public void getCarts() {
		String url = "https://www.brandsfever.com/api/v5/carts/merge/?user_id=" 
					+ mUserId + "&token=" + mToken + "&channels=10,40" + "&country_code=" 
					+ DataHolderClass.getInstance().getCountryCode();
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
				String content = total.toString();
				JSONObject obj = new JSONObject(content);
				String ret = obj.getString("ret");
				String msg = obj.getString("msg");
				if (ret.equals("0") && msg.equalsIgnoreCase("ok")) {
					JSONParser.getInstance(MyCartActivity.this).start(content, Cart.class);
				}else{
					responsePopup("Please login");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void responsePopup(String msg) {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.error_popop, null);
		TextView msgView = (TextView) view.findViewById(R.id._seterrormsg);
		msgView.setText(msg);
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.checkout_cart:
			if (orders.size() > 0) {
				String channel = "";
				for(Cart cart : carts){
					if(cart.isChecked){
						switch(cart.channel_code){
						case 10:
							channel += 30 + ",";
							break;
						case 40:
							channel += 110 + ",";
							break;
						case 80:
							channel += 140 + ",";
							break;
							default:
								channel += cart.channel_code + ",";
								break;
						}
					}
				}
				if(channel.length() == 0){
					responsePopup("You have to select one channel at least.");
					return;
				}
				Intent checkout = new Intent(this, OrderDeliveryActiviy.class);
				checkout.putExtra("channel", channel.subSequence(0, channel.length() - 1));
				startActivity(checkout);
				this.overridePendingTransition(R.anim.push_out_to_right, R.anim.push_out_to_left);
			} else {
				responsePopup("your cart is empty");
			}
			break;
		case R.id.header_bar_left:
		case R.id.continue_shopping:
			finish();
			break;
		}
	}
	
	class MyCartAdapter extends BaseAdapter implements PinnedSectionListAdapter {
		LayoutInflater inflater;
		
		public MyCartAdapter(){
			inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return orders.size();
		}

		@Override
		public Object getItem(int position) {
			return orders.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			OrderInfoModel order = orders.get(position);
			
			if (DataHolderClass.getInstance().get_deviceInch() < 7) {
				if(order.cellType == OrderInfoModel.HEADER){
					convertView = inflater.inflate(R.layout.cart_header, parent, false);
					setCellView4Header(order, convertView);
				}else if(order.isFooter){
					convertView = inflater.inflate(R.layout.cart_footer, parent, false);
					setCellView4Footer(order, convertView);
				}else{
					convertView = inflater.inflate(R.layout.cart_inflator_phone, parent, false);
					setCellView4Normal(order, convertView);
				}
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7 && DataHolderClass.getInstance().get_deviceInch() < 9) {
				convertView = inflater.inflate(R.layout.seven_inch_cart_inflator, parent, false);
				setCellView4Normal(order, convertView);
			}else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
				convertView = inflater.inflate(R.layout.my_cart_inflator, parent, false);
				setCellView4Normal(order, convertView);
			}

			return convertView;
		}
		
		private void setCellView4Header(final OrderInfoModel order, View convertView){
			TextView headerViewTV = (TextView) convertView.findViewById(R.id.cart_header_tv);
			CheckBox checkBX = (CheckBox) convertView.findViewById(R.id.channel_checkbox);
			checkBX.setChecked(carts.get(order.getSection()).isChecked);
			checkBX.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					carts.get(order.getSection()).setChecked(isChecked);
					updateBillAmount();
				}
			});
			switch(order.channel){
			case 10:
				headerViewTV.setText(MyCartActivity.this.getResources().getString(R.string.channel_30));
				break;
			case 40:
				headerViewTV.setText(MyCartActivity.this.getResources().getString(R.string.channel_110));
				break;
			}
		}
		
		private void setCellView4Normal(final OrderInfoModel order, View convertView){
			TextView quantity_tag = (TextView) convertView.findViewById(R.id.Quantity_tag);
			quantity_tag.setTypeface(mTypeface);
			TextView setQuantity_text = (TextView) convertView.findViewById(R.id.setQuantity_text);
			setQuantity_text.setTypeface(mTypeface);
			TextView total_tag = (TextView) convertView.findViewById(R.id.total_tag);
			total_tag.setTypeface(mTypeface);
			TextView total_amount = (TextView) convertView.findViewById(R.id._total_amount);
			total_amount.setTypeface(mTypeface);
			TextView product_name = (TextView) convertView.findViewById(R.id.set_product_name);
			product_name.setTypeface(mTypeface);
			TextView unitprice_tag = (TextView) convertView.findViewById(R.id.unitprice_tag);
			unitprice_tag.setTypeface(mTypeface);
			TextView unit_price = (TextView) convertView.findViewById(R.id.set_unit_price);
			unit_price.setTypeface(mTypeface);
			Button delBtn = (Button) convertView.findViewById(R.id.cart_item_del_btn);
			Button add_quantity = (Button) convertView.findViewById(R.id.add_quantity);
			add_quantity.setTypeface(mTypeface);
			Button subtract_quantity = (Button) convertView.findViewById(R.id.subtract_quantity);
			subtract_quantity.setTypeface(mTypeface);
			ImageView orderImg = (ImageView) convertView.findViewById(R.id.my_oder_img);

			add_quantity.setTag(order);
			subtract_quantity.setTag(order);
			setQuantity_text.setText(order.getQuantity());
			product_name.setText(order.getProduct_name());
			unit_price.setText(order.getSales_price());
			total_amount.setText(order.getTotal_price());
			
			AQuery aq = new AQuery(MyCartActivity.this);
			aq.id(orderImg).image("https:" + order.getProduct_image());
			
			delBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					final String sendAction = "remove_product";
					final String sendquantity = "";
					final String senditempk = order.getProduct_item_pk();
					View view = View.inflate(MyCartActivity.this, R.layout.call_settings, null);
					final PopupWindow pwindo = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
					pwindo.showAtLocation(view, Gravity.CENTER, 0, 0);

					TextView message = (TextView) view.findViewById(R.id.textview_popup);
					message.setText("Are you sure you want to delete this \n product?");
					Button cancel_password = (Button) view.findViewById(R.id.cancel_password);
					cancel_password.setTypeface(mTypeface, Typeface.NORMAL);
					cancel_password.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pwindo.dismiss();
						}
					});

					Button reset_password = (Button) view.findViewById(R.id.reset_password);
					reset_password.setText("OK");
					reset_password.setTypeface(mTypeface, Typeface.BOLD);
					reset_password.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							v.setTag(order);
							pwindo.dismiss();
							new RemoveOrUpdateProduct(v, sendAction, sendquantity, senditempk).execute();
						}
					});

				}
			});

			add_quantity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String sendAction = "update_product";
					OrderInfoModel obj = (OrderInfoModel) v.getTag();
					int temp = Integer.parseInt(obj.getQuantity());
					int temap1 = temp + 1;
					String sendquantity = String.valueOf(temap1);
					String senditempk = obj.getProduct_item_pk();
					new RemoveOrUpdateProduct(v, sendAction, sendquantity, senditempk).execute();
				}
			});

			subtract_quantity.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String sendAction = "update_product";
					OrderInfoModel obj = (OrderInfoModel) v.getTag();
					int temp = Integer.parseInt(obj.getQuantity());
					int temap1 = temp - 1;
					if(temap1 > 0){
						String sendquantity = temap1 + "";
						String senditempk = obj.getProduct_item_pk();
						new RemoveOrUpdateProduct(v, sendAction, sendquantity, senditempk).execute();
					}
				}
			});
		}
		
		private void setCellView4Footer(OrderInfoModel order, View convertView){
			TextView saveAmounTV = (TextView)convertView.findViewById(R.id.save_amount_tv);
			saveAmounTV.setTypeface(mTypeface);
			TextView totalAmounTV = (TextView)convertView.findViewById(R.id.total_amount_tv);
			totalAmounTV.setTypeface(mTypeface);
			TextView totalWeighTV = (TextView)convertView.findViewById(R.id.total_weight_tv);
			totalWeighTV.setTypeface(mTypeface);
			TextView shipTV = (TextView) convertView.findViewById(R.id.ship_tv);
			shipTV.setTypeface(mTypeface);
			TextView subTotalTV = (TextView) convertView.findViewById(R.id.subtotal_tv);
			subTotalTV.setTypeface(mTypeface);
			
			saveAmounTV.setText(carts.get(order.getSection()).getTotal_save());
			totalAmounTV.setText(carts.get(order.getSection()).subtotal_price);
			totalWeighTV.setText(carts.get(order.getSection()).getWeight() + "g");
			shipTV.setText(carts.get(order.getSection()).shipping_fee);
			subTotalTV.setText(carts.get(order.getSection()).getTotal_price());
		}

		@Override
		public int getItemViewType(int position) {
			return orders.get(position).cellType;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public boolean isItemViewTypePinned(int cellType) {
			return cellType == OrderInfoModel.HEADER;
		}

	}
	
	public void updateBillAmount(){
		Currency currency = Currency.getInstance(new Locale("en", DataHolderClass.getInstance().getCountryCode()));
		String currencyCode = currency.getCurrencyCode();
		double totalAmount = 0;
		try{
			for(Cart cart : carts){
				if(cart.isChecked){
					String amountStr = cart.getTotal_price().replace(currencyCode, "").replace(",", "");
					totalAmount += Double.parseDouble(amountStr);
				}
			}
		}catch(java.lang.NumberFormatException e){
			Toast.makeText(this, "Total parsing error.", Toast.LENGTH_LONG).show();
		}
		payable_amount.setText("Order Total: " + currencyCode + Double.valueOf(new DecimalFormat("#.##").format(totalAmount)));
	}
	
	public class RemoveOrUpdateProduct extends AsyncTask<String, String, String> implements OnCancelListener {
		ProgressHUD mProgressHUD;
		String mPK;
		String mAction;
		String mProductQuantity;
		View senderView;
		OrderInfoModel order;
		
		public RemoveOrUpdateProduct(View senderView, String action, String quan, String sendpk) {
			mAction = action;
			mPK = sendpk;
			mProductQuantity = quan;
			this.senderView = senderView;
			this.order = (OrderInfoModel) senderView.getTag();
		}
		
		@Override
		protected void onPreExecute() {
			mProgressHUD = ProgressHUD.show(MyCartActivity.this, "Loading", true, true, this);
			mProgressHUD.getWindow().setGravity(Gravity.CENTER);
			mProgressHUD.setCancelable(false);
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String url = "https://www.brandsfever.com/api/v5/carts/";
			List<NameValuePair> namevalueList = new ArrayList<NameValuePair>();
			BasicNameValuePair useridPair = new BasicNameValuePair("user_id", mUserId);
			BasicNameValuePair tokenPair = new BasicNameValuePair("token",mToken);
			BasicNameValuePair actionPair = new BasicNameValuePair("action", mAction);
			BasicNameValuePair quantityPair = new BasicNameValuePair("quantity", mProductQuantity);
			BasicNameValuePair itempkPair = new BasicNameValuePair("product_item_pk", mPK);
			if(this.order != null){
				BasicNameValuePair channelPair = new BasicNameValuePair("channel", this.order.channel + "");
				namevalueList.add(channelPair);
			}
			namevalueList.add(useridPair);
			namevalueList.add(tokenPair);
			namevalueList.add(actionPair);
			namevalueList.add(quantityPair);
			namevalueList.add(itempkPair);
			return updateProduct(url, namevalueList);
		}
		
		@Override
		public void onCancel(DialogInterface dialog) {}
		
		@Override
		protected void onPostExecute(String result) {
			try {
				JSONObject jobj = new JSONObject(result);
				String _Ret = jobj.getString("ret");
				if (_Ret.equals("0")) {
					orders.clear();
					mOrderAdapter.notifyDataSetChanged();
					new GetAllCarts().execute();
					//可直接更新product的最新数量，不需要刷新。
					//可以本地计数也可server返回成功信息时计数; 后者比较好，前者在不同用户操作同一账号时计数会不一致。
//					if(mAction.equals("update_product")){
//						((TextView) ((LinearLayout)this.senderView.getParent())
//								.findViewById(R.id.setQuantity_text)).setText(mProductQuantity);
//						OrderInfoModel order = (OrderInfoModel) this.senderView.getTag();
//						order.setQuantity(mProductQuantity);
//					}else{
//						OrderInfoModel order = (OrderInfoModel) senderView.getTag();
//						orders.remove(order);
//						mOrderAdapter.notifyDataSetChanged();
//					}
				} else {
					responsePopup(jobj.getString("msg"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mProgressHUD.dismiss();
		}
	}
	
	public String updateProduct(String url, List<NameValuePair> _namevalueList) {
		String response = null;
		TrustAllCertificates cert = new TrustAllCertificates();
		cert.trustAllHosts();
		HttpClient httpClient = HttpsClient.getNewHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(_namevalueList, HTTP.UTF_8));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			if (responseCode == 200) {
				InputStream inputStream = httpResponse.getEntity().getContent();
				BufferedReader r = new BufferedReader(new InputStreamReader(
						inputStream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = r.readLine()) != null) {
					total.append(line);
				}
				response = total.toString();
			} else {
				response = "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onParseSuccess(Object obj) {
		if(obj instanceof ArrayList){
			carts = (List<Cart>) obj;
			if(carts == null || carts.size() == 0) return;
			
			for(int i = 0; i < carts.size(); i++){
				carts.get(i).setChecked(true);
				for(int j = 0; j < carts.get(i).cart_items.size(); j++){
					carts.get(i).cart_items.get(j).section = i;
					carts.get(i).cart_items.get(j).setChannel(carts.get(i).channel_code);
					carts.get(i).cart_items.get(j).cellType = OrderInfoModel.NORMAL;
					carts.get(i).cart_items.get(j).isFooter = false;
					
					if(j==0){
						OrderInfoModel topModel = new OrderInfoModel();
						topModel.cellType = OrderInfoModel.HEADER;
						topModel.channel = carts.get(i).channel_code;
						topModel.section = i;
						topModel.isFooter = false;
						orders.add(topModel);
					}
					
					orders.add(carts.get(i).cart_items.get(j));
					
					if(j == carts.get(i).cart_items.size() - 1){
						OrderInfoModel footerModel = new OrderInfoModel();
						footerModel.cellType = OrderInfoModel.NORMAL;
						footerModel.channel = carts.get(i).channel_code;
						footerModel.section = i;
						footerModel.isFooter = true;
						orders.add(footerModel);
					}
				}
			}
			updateBillAmount();
		}else{
			Toast.makeText(this, "Parse failed.", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onParseError(String err) {
		Toast.makeText(this, err, Toast.LENGTH_LONG).show();
	}
}
