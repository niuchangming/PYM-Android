package com.brandsfever;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.brandsfever.luxury.R;

public class DialogFragmentWindow extends DialogFragment implements OnClickListener {
	Button login,signup;

    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dialog_fragment_window, container);
//        ViewPager _mpager = (ViewPager) view.findViewById(R.id.login_signup_viewPager);
//        login = (Button) view.findViewById(R.id.login);
//        signup = (Button) view.findViewById(R.id.signup);
        return view;
    }
        
//        Button cancel_dialog =(Button) view.findViewById(R.id.cancel_dialouge);
//        cancel_dialog.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				dismiss();
//				
//			}
//		});
        
//        List fragments = getFragments();
//        LoginPagerAdapter ama = new LoginPagerAdapter(getChildFragmentManager(), fragments);
//        _mpager.setAdapter(ama);
//        /********************************************************************************************************/
//        _mpager.setOnPageChangeListener(new OnPageChangeListener() {			
//			@Override
//			public void onPageSelected(int position) {
//				// TODO Auto-generated method stub
//				 switch(position){
//                 case 0:                	 
//                	 login.setTextColor(Color.BLACK);
//                	 signup.setBackgroundColor(getResources().getColor(android.R.color.transparent));
//                	 break;
//                 case 1:
//                	 login.setBackgroundColor(color.black);    
//                	 signup.setTextColor(Color.BLUE); 
//                     break;
//                 }
//             }
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//			@Override
//			public void onPageScrolled(int pos, float arg1, int arg2) {
////				switch(pos){
////                case 0:                	 
////               	 login.setBackgroundColor(color.holo_purple);    
////               	 signup.setBackgroundColor(color.black);  
////                    break;
////                case 1:
////               	 login.setBackgroundColor(color.black);    
////               	 signup.setBackgroundColor(color.holo_purple); 
////                    break;
////                }
//				
//			}			
//		});
///************************************************************************************************************************/
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        return view;
//     }
//    @Override
//    public void onStart()
//    {
//      super.onStart();
//      if (getDialog() == null)
//        return;
//      int dialogWidth = 615;
//      int dialogHeight = 730;
//      getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
//      getDialog().setCanceledOnTouchOutside(false);
//      }
//     
//
//    @SuppressWarnings({ "unchecked", "rawtypes" })
//	private List getFragments(){
//        List fList = new ArrayList();
//            fList.add(Login_page.newInstance("Fragment 1",1,this));
//            fList.add(Signup_page.newInstance("Fragment 2",2,this));
//
//        return fList;
//    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
    
}
