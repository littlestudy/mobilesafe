package com.stu.mobilesafe.activities;

import com.stu.mobilesafe.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SplashActivity extends Activity {
	
	private TextView tv_splash_version; 
	private RelativeLayout rl_splash_root;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		initView();
		
		playAnimation();
		
	}
	
	
	private void initView(){
		tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
		rl_splash_root = (RelativeLayout)findViewById(R.id.rl_splash_root);
		
		tv_splash_version.setText("版本号：" + getVersion());		
	}
	
	private void playAnimation(){
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(2000);
		rl_splash_root.startAnimation(aa);
	}	
	
	/**
     * 获取应用程序的版本号信息
     * @return 应用程序的版本号
     */	
	private String getVersion(){		
		PackageManager pm = getPackageManager(); //获取一个系统的包管理器。
		try {
			PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0); //清单文件 manifest.xml文件的所有的信息
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// can't reach 不可能发生的异常
			return "";
		}
	}
}
