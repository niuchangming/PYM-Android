 package com.brandsfever;


import android.content.SharedPreferences;

import android.graphics.Typeface;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.brandsfever.luxury.R;
import com.facebook.android.Facebook;

public class Login_page extends Fragment  {
	EditText username,password;
	Button signin,signup;
	ImageButton login_with_fb;
	TextView forgotpassword;
	String _getusername,_getpassword;
    SharedPreferences _mypref;
    String _ResponseFromServer,_SocailResponseFromServer;
    Login_page context;
    static DialogFragment _df;
    Toast toast;
    ViewGroup _view;
    Facebook facebook;
    Typeface _font;
	String _userFBemail;
	public static final Login_page newInstance(String message,int item,DialogFragment _df){
		Login_page f = new Login_page();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 _view = (ViewGroup) inflater.inflate(R.layout.activity_login_page, null);			
	    context = this;		
	    

		return _view;
	}
}
