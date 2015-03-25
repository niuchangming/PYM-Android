package com.datamodel;

public class ProductsDataModel {
	public long ends_at;
	public String teaser_url;
	public String teaser_square_url;
	public String name;
	public long starts_at;
	public String pk;
	public String express;
	public String discount_text;
	public String category;
	public String shipping_fee;
	public String free_shipping;
	public String shipping_period;
	
	public String getTeaser_square_url() {
		return teaser_square_url;
	}
	public void setTeaser_square_url(String teaser_square_url) {
		this.teaser_square_url = teaser_square_url;
	}
	public long getEnds_at() {
		return ends_at;
	}
	public void setEnds_at(long ends_at) {
		this.ends_at = ends_at;
	}
	public String getTeaser_url() {
		return teaser_url;
	}
	public void setTeaser_url(String teaser_url) {
		this.teaser_url = teaser_url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getStarts_at() {
		return starts_at;
	}
	public void setStarts_at(long starts_at) {
		this.starts_at = starts_at;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getExpress() {
		return express;
	}
	public void setExpress(String express) {
		this.express = express;
	}
	public String getDiscount_text() {
		return discount_text;
	}
	public void setDiscount_text(String discount_text) {
		this.discount_text = discount_text;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getShipping_fee() {
		return shipping_fee;
	}
	public void setShipping_fee(String shipping_fee) {
		this.shipping_fee = shipping_fee;
	}
	public String getFree_shipping() {
		return free_shipping;
	}
	public void setFree_shipping(String free_shipping) {
		this.free_shipping = free_shipping;
	}
	public String getShipping_period() {
		return shipping_period;
	}
	public void setShipping_period(String shipping_period) {
		this.shipping_period = shipping_period;
	}
}
