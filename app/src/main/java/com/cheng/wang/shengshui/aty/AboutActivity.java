package com.cheng.wang.shengshui.aty;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.cheng.wang.shengshuiqi.R;


public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_activity);
	}
	
}
