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
import com.brandsfever.luxury.R;
import com.dataholder.DataHolderClass;
import com.datamodel.PaymentScreenOrderModel;

public class PaymentScreenDataAdapter extends BaseAdapter {
	Context _scontext;
	LayoutInflater _inflater;
	View _view;
	Typeface _font;
	ArrayList<PaymentScreenOrderModel> data;

	public PaymentScreenDataAdapter(Context context,
			ArrayList<PaymentScreenOrderModel> arraylist) {
		this._scontext = context;
		data = arraylist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView set_campaign_name, set_product_name, set_quantity_tag, set_quantity, set_unitprice_tag, set_unitprice, set_totalprice_tag, set_totalprice;
		ImageView set_product_img;
		if (DataHolderClass.getInstance().get_deviceInch() <= 6) {
			_inflater = (LayoutInflater) _scontext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater.inflate(
					R.layout.payment_order_info_inflator_phone, parent, false);
			set_quantity_tag = (TextView) _view
					.findViewById(R.id.set_quantity_tag);
			set_quantity_tag.setTypeface(_font, Typeface.NORMAL);
			set_unitprice_tag = (TextView) _view
					.findViewById(R.id.set_unitprice_tag);
			set_unitprice_tag.setTypeface(_font, Typeface.NORMAL);
			set_totalprice_tag = (TextView) _view
					.findViewById(R.id.set_totalprice_tag);
			set_totalprice_tag.setTypeface(_font, Typeface.NORMAL);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 7
				&& DataHolderClass.getInstance().get_deviceInch() < 9) {
			_inflater = (LayoutInflater) _scontext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater.inflate(
					R.layout.seven_inch_payment_list_inflator, parent, false);
		} else if (DataHolderClass.getInstance().get_deviceInch() >= 9) {
			_inflater = (LayoutInflater) _scontext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			_view = _inflater
					.inflate(R.layout.order_list_inflator_tablet_ten_inch,
							parent, false);
		}
		LinearLayout f_l = (LinearLayout) _view.findViewById(R.id.list_bg);
		if (position % 2 == 0) {
			f_l.setBackgroundColor(Color.parseColor("#ffffff"));
		} else {
			f_l.setBackgroundColor(Color.parseColor("#AFEEEE"));
		}
		_font = Typeface.createFromAsset(_scontext.getAssets(),
				"fonts/georgia.ttf");
		set_campaign_name = (TextView) _view
				.findViewById(R.id.set_campaign_name);
		set_campaign_name.setTypeface(_font, Typeface.NORMAL);

		set_product_name = (TextView) _view.findViewById(R.id.set_product_name);
		set_product_name.setTypeface(_font, Typeface.NORMAL);

		set_quantity = (TextView) _view.findViewById(R.id.set_quantity);
		set_quantity.setTypeface(_font, Typeface.NORMAL);

		set_unitprice = (TextView) _view.findViewById(R.id.set_unitprice);
		set_unitprice.setTypeface(_font, Typeface.NORMAL);

		set_totalprice = (TextView) _view.findViewById(R.id.set_totalprice);
		set_totalprice.setTypeface(_font, Typeface.NORMAL);

		set_product_img = (ImageView) _view.findViewById(R.id.set_product_img);

		if(position < data.size()){
			PaymentScreenOrderModel obj = data.get(position);
			set_campaign_name.setText(obj.getCampaign());
			set_product_name.setText(obj.getName());
			set_quantity.setText(obj.getQuantity());
			set_unitprice.setText("S$"+obj.getUnit_price());
			Double tot=Integer.valueOf(obj.getQuantity()) * Double.valueOf(obj.getUnit_price());
			set_totalprice.setText("S$"+String.valueOf(tot)+"0");
			String a = "https:" + obj.getImage();
			AQuery aq = new AQuery(_scontext);
			aq.id(set_product_img).image(a);
		}
		
		return _view;

	}

}
