package com.datamodel;

import java.util.ArrayList;


public class SingleItemDataModel {
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnds_at() {
		return ends_at;
	}
	public void setEnds_at(String ends_at) {
		this.ends_at = ends_at;
	}
	public String getOffer_price() {
		return offer_price;
	}
	public void setOffer_price(String offer_price) {
		this.offer_price = offer_price;
	}
	public String getMarket_price() {
		return market_price;
	}
	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getProduct_item_property() {
		return product_item_property;
	}
	public void setProduct_item_property(String product_item_property) {
		this.product_item_property = product_item_property;
	}
	public String getProduct_item_pk() {
		return product_item_pk;
	}
	public void setProduct_item_pk(String product_item_pk) {
		this.product_item_pk = product_item_pk;
	}
	public String get_availablestock() {
		return _availablestock;
	}
	public void set_availablestock(String _availablestock) {
		this._availablestock = _availablestock;
	}
	public String getProduct_item_name() {
		return product_item_name;
	}
	public void setProduct_item_name(String product_item_name) {
		this.product_item_name = product_item_name;
	}
	public String get_propertylistName() {
		return _propertylistName;
	}
	public void set_propertylistName(String _propertylistName) {
		this._propertylistName = _propertylistName;
	}
	public String get_propertylistValue() {
		return _propertylistValue;
	}
	public void set_propertylistValue(String _propertylistValue) {
		this._propertylistValue = _propertylistValue;
	}
	public String pk;
	public String name;
	public String ends_at;
	public String offer_price;
	public String market_price;
	public String description;
	public String product_item_property;
	public String product_item_pk;
	public String _availablestock;
	public String product_item_name;
	public String _propertylistName;
	public String _propertylistValue;
	public ArrayList<String> images_list;
	public ArrayList<String> getImages_list() {
		return images_list;
	}
	public void setImages_list(ArrayList<String> images_list) {
		this.images_list = images_list;
	}
	
}
