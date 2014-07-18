package com.stu.mobilesafe.activities;

import com.stu.mobilesafe.R;

import android.os.Bundle;
import android.view.View;

public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}
	
	public void next(View view){
		loadActivity(Setup2Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showNext() {
		next(null);
	}

	@Override
	public void showPre() {		
	}	
}
