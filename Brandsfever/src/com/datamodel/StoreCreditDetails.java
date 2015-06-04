package com.datamodel;

public class StoreCreditDetails {

	public String getGranted_by() {
		return granted_by;
	}
	public void setGranted_by(String granted_by) {
		this.granted_by = granted_by;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getExpired_at() {
		return expired_at;
	}
	public void setExpired_at(String expired_at) {
		this.expired_at = expired_at;
	}
	
	public String amount;
	public String expired_at;
	public String granted_by;
	public String redeemed_order;
	public String getRedeemed_order() {
		return redeemed_order;
	}
	public void setRedeemed_order(String redeemed_order) {
		this.redeemed_order = redeemed_order;
	}
	public String getIs_redeemable() {
		return is_redeemable;
	}
	public void setIs_redeemable(String is_redeemable) {
		this.is_redeemable = is_redeemable;
	}
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getRedeemed_at() {
		return redeemed_at;
	}
	public void setRedeemed_at(String redeemed_at) {
		this.redeemed_at = redeemed_at;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String is_redeemable;
	public String pk;
	public String redeemed_at;
	public String state;
	public String channel;
}
