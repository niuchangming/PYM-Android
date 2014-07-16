package com.datamodel;


public class ProductListDetailModel {
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getMarket_price() {
		return market_price;
	}
	public void setMarket_price(String market_price) {
		this.market_price = market_price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getSales_price() {
		return sales_price;
	}
	public void setSales_price(String sales_price) {
		this.sales_price = sales_price;
	}
	public String get_catagory() {
		return _catagory;
	}
	public void set_catagory(String _catagory) {
		this._catagory = _catagory;
	}
	public String market_price;
	public String name;
	public String img;
	public String sales_price;
	public String pk;
	public String _catagory;
	public int _availstock;
	public int get_availstock() {
		return _availstock;
	}
	public void set_availstock(int _availstock) {
		this._availstock = _availstock;
	}

	
	
}
