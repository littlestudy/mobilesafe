package com.stu.mobilesafe.activities;

import com.stu.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;

public class Setup4Activity extends BaseSetupActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
	}
	
	public void next(View view){
		//在sp里面存放一个finishsetup  ---》true
		Editor editor = sp.edit();
		editor.putBoolean("finishsetup", true);
		editor.commit();
		loadActivity(LostFindActivity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	
	public void pre(View view){
		loadActivity(Setup3Activity.class);
		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
	
	@Override
	public void showNext() {
		next(null);
	}

	@Override
	public void showPre() {
		pre(null);
	}
}
