package com.dataholder;

public class DataHolderClass {
	private static DataHolderClass dataObject= null;
	private DataHolderClass()
	{
		// left blank intentionally
	}
	
	public static DataHolderClass getInstance()
	{
		if(dataObject==null)
			dataObject = new DataHolderClass();
		return dataObject;
	}
	private int _deviceHeight;
	private int _deviceWidth;
	private int _deviceInch;
	private int _mainProductsPk;
	private int _subProductsPk;
	private String shipping_fee="";
	private String shipping_period="";
	private String free_shipping="";
	private String bill_ship_address="";
	private String final_cart_pk="";
	private String _orderpk="";
	private String _paymentpk = "";
	private String _payementid;
	private String _creditpk="";
	private double _creditAmount=0;
	private String username="";
	private String countryCode;
	private boolean paypalEnabled;
	
	public boolean isPaypalEnabled() {
		return paypalEnabled;
	}

	public void setPaypalEnabled(boolean paypalEnabled) {
		this.paypalEnabled = paypalEnabled;
	}

	public String get_creditpk() {
		return _creditpk;
	}

	public void set_creditpk(String _creditpk) {
		this._creditpk = _creditpk;
	}

	public String get_orderpk() {
		return _orderpk;
	}

	public void set_orderpk(String _orderpk) {
		this._orderpk = _orderpk;
	}

	public String getFinal_cart_pk() {
		return final_cart_pk;
	}

	public void setFinal_cart_pk(String final_cart_pk) {
		this.final_cart_pk = final_cart_pk;
	}

	public String getBill_ship_address() {
		return bill_ship_address;
	}

	public void setBill_ship_address(String bill_ship_address) {
		this.bill_ship_address = bill_ship_address;
	}

	public String getShipping_fee() {
		return shipping_fee;
	}

	public void setShipping_fee(String shipping_fee) {
		this.shipping_fee = shipping_fee;
	}

	public String getShipping_period() {
		return shipping_period;
	}

	public void setShipping_period(String shipping_period) {
		this.shipping_period = shipping_period;
	}

	public String getFree_shipping() {
		return free_shipping;
	}

	public void setFree_shipping(String free_shipping) {
		this.free_shipping = free_shipping;
	}
	
	
	
	public int get_subProductsPk() {
		return _subProductsPk;
	}

	public void set_subProductsPk(int _subProductsPk) {
		this._subProductsPk = _subProductsPk;
	}
	
	public int get_mainProductsPk() {
		return _mainProductsPk;
	}

	public void set_mainProductsPk(int _mainProductsPk) {
		this._mainProductsPk = _mainProductsPk;
	}

	public int get_deviceInch() {
		return _deviceInch;
	}

	public void set_deviceInch(int _deviceInch) {
		this._deviceInch = _deviceInch;
	}
	
	
	
	public int get_deviceHeight() {
		return _deviceHeight;
	}

	public void set_deviceHeight(int _deviceHeight) {
		this._deviceHeight = _deviceHeight;
	}

	public int get_deviceWidth() {
		return _deviceWidth;
	}

	public void set_deviceWidth(int _deviceWidth) {
		this._deviceWidth = _deviceWidth;
	}

	public double get_creditAmount() {
		return _creditAmount;
	}

	public void set_creditAmount(double _creditAmount) {
		this._creditAmount = _creditAmount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String get_paymentpk() {
		return _paymentpk;
	}

	public void set_paymentpk(String _paymentpk) {
		this._paymentpk = _paymentpk;
	}

	public String get_payementid() {
		return _payementid;
	}

	public void set_payementid(String _payementid) {
		this._payementid = _payementid;
	}
	
}
