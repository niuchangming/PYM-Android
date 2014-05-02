package com.datamodel;

public class InvitePeopleInfo {
	String _PendingInvitedEmail = "";
	String _NumOfPendingInvitations = "";
	String _fbId = "";
	String _fbNo="";
	public String get_fbId() {
		return _fbId;
	}

	public void set_fbId(String _fbId) {
		this._fbId = _fbId;
	}

	public String get_fbNo() {
		return _fbNo;
	}

	public void set_fbNo(String _fbNo) {
		this._fbNo = _fbNo;
	}

	

	public String get_PendingInvitedEmail() {
		return _PendingInvitedEmail;
	}

	public void set_PendingInvitedEmail(String _PendingInvitedEmail) {
		this._PendingInvitedEmail = _PendingInvitedEmail;
	}

	public String get_NumOfPendingInvitations() {
		return _NumOfPendingInvitations;
	}

	public void set_NumOfPendingInvitations(String _NumOfPendingInvitations) {
		this._NumOfPendingInvitations = _NumOfPendingInvitations;
	}

}
