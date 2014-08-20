package com.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.brandsfever.R;
import com.dataholder.DataHolderClass;
import com.datamodel.PaymentScreenOrderModel;

public class PaymentScreenDataAdapter extends BaseAdapter {
	Context _scontext;
	Typeface _font;
	ArrayList<PaymentScreenOrderModel> data;

	public PaymentScreenDataAdapter(Context context,
			ArrayList<PaymentScreenOrderModel> arraylist) {
		this._scontext = context;
		data = arraylist;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView set_campaign_name, set_product_name, set_quantity_tag, set_quantity, set_unitprice_tag, set_unitprice, set_totalprice_tag, set_totalprice;
		ImageView set_product_img;

		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) _scontext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
				view = inflater.inflate(
						R.layout.payment_order_info_inflator_phone, parent,
						false);
				set_quantity_tag = (TextView) view
						.findViewById(R.id.set_quantity_tag);
				set_quantity_tag.setTypeface(_font, Typeface.NORMAL);
				set_unitprice_tag = (TextView) view
						.findViewById(R.id.set_unitprice_tag);
				set_unitprice_tag.setTypeface(_font, Typeface.NORMAL);
				set_totalprice_tag = (TextView) view
						.findViewById(R.id.set_totalprice_tag);
				set_totalprice_tag.setTypeface(_font, Typeface.NORMAL);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
					&& DataHolderClass.getInstance().get_deviceInch() < 9) {
				view = inflater.inflate(
						R.layout.seven_inch_payment_list_inflator, parent,
						false);
			} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
				view = inflater.inflate(
						R.layout.order_list_inflator_tablet_ten_inch, parent,
						false);
			}
		}
		LinearLayout f_l = (LinearLayout) view.findViewById(R.id.list_bg);
		f_l.setBackgroundColor(Color.WHITE);
		_font = Typeface.createFromAsset(_scontext.getAssets(),
				"fonts/georgia.ttf");
		set_campaign_name = (TextView) view
				.findViewById(R.id.set_campaign_name);
		set_campaign_name.setTypeface(_font, Typeface.NORMAL);

		set_product_name = (TextView) view.findViewById(R.id.set_product_name);
		set_product_name.setTypeface(_font, Typeface.NORMAL);

		set_quantity = (TextView) view.findViewById(R.id.set_quantity);
		set_quantity.setTypeface(_font, Typeface.NORMAL);

		set_unitprice = (TextView) view.findViewById(R.id.set_unitprice);
		set_unitprice.setTypeface(_font, Typeface.NORMAL);

		set_totalprice = (TextView) view.findViewById(R.id.set_totalprice);
		set_totalprice.setTypeface(_font, Typeface.NORMAL);

		set_product_img = (ImageView) view.findViewById(R.id.set_product_img);

		if (position < data.size()) {
			PaymentScreenOrderModel obj = data.get(position);
			set_campaign_name.setText(obj.getCampaign());
			set_product_name.setText(obj.getName());
			set_quantity.setText(obj.getQuantity());
			set_unitprice.setText("S$" + obj.getUnit_price());
			Double tot = Integer.valueOf(obj.getQuantity())
					* Double.valueOf(obj.getUnit_price());
			set_totalprice.setText("S$" + String.valueOf(tot) + "0");
			String a = "https:" + obj.getImage();
			AQuery aq = new AQuery(_scontext);
			aq.id(set_product_img).image(a);
		}

		return view;

	}

}
