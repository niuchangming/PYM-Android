package com.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.niu.annotation.FieldExclude;

public class PaymentScreenOrderModel implements Parcelable{
	public String name;
	public String campaign;
	public String image;
	public String unit_price;
	public String pk;
	public String quantity;
	public String total_price;
	
	public PaymentScreenOrderModel() {}
	public PaymentScreenOrderModel(Parcel source) {
		name = source.readString();
		campaign = source.readString();
		image = source.readString();
		unit_price = source.readString();
		pk = source.readString();
		quantity = source.readString();
		total_price = source.readString();
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCampaign() {
		return campaign;
	}
	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUnit_price() {
		return unit_price;
	}
	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(campaign);
		dest.writeString(image);
		dest.writeString(unit_price);
		dest.writeString(pk);
		dest.writeString(quantity);
		dest.writeString(total_price);
	}
	
	@FieldExclude
	public static final Parcelable.Creator<PaymentScreenOrderModel> CREATOR = new Parcelable.Creator<PaymentScreenOrderModel>() {

		@Override
		public PaymentScreenOrderModel createFromParcel(Parcel source) {
			return new PaymentScreenOrderModel(source);
		}

		@Override
		public PaymentScreenOrderModel[] newArray(int size) {
			return new PaymentScreenOrderModel[size];
		}
	};

}
