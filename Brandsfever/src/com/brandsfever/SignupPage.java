package com.brandsfever;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;


public class SignupPage extends Fragment {
	EditText F_Name,L_Name,Password,EmailId;
	ImageButton hit_register,signup_with_fb;
    String _Fname,_Lname,_Password,_Email;
	String _ResponseRegister;

	public static Fragment newInstance(String message,int item,DialogFragment _df) {
		SignupPage f = new SignupPage();			
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup _view = (ViewGroup) inflater.inflate(R.layout.activity_signup_page, null);		
		return _view;
	}
}