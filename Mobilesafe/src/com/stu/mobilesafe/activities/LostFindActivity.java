package com.stu.mobilesafe.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.stu.mobilesafe.R;

public class LostFindActivity extends Activity {
	private static final String TAG = "LostFindActivity";
	private SharedPreferences sp;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 判断用户是否进行过设置向导，
		boolean finishsetup = sp.getBoolean("finishsetup", false);
		
		if (finishsetup){ // 如果配置过 就显示正常的ui界面
			//已经完成过设置向导 ，加载正常的ui界面。
			setContentView(R.layout.activity_lostfind);
		} else {
			//定向页面到设置向导界面。
			Intent intent = new Intent();
			intent.setClass(this, Setup1Activity.class);
			startActivity(intent);
			this.finish();
		}
	}
	
	public void reEntrySetup(View view){
		Intent intent = new Intent();
		intent.setClass(this, Setup1Activity.class);
		startActivity(intent);
		this.finish();
	}
}
