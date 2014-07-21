package com.stu.mobilesafe.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.stu.mobilesafe.R;

public class Setup2Activity extends BaseSetupActivity {

	private static final String TAG = "Setup2Activity";
	
	private TelephonyManager tm;
	private ImageView iv_setup2_status;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		
		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		iv_setup2_status = (ImageView)findViewById(R.id.iv_setup2_status);
		
		String savedSim = sp.getString("sim", "");
		Log.i(TAG, "savedSim:" + savedSim);
		if (TextUtils.isEmpty(savedSim)){ 
			iv_setup2_status.setImageResource(R.drawable.unlock);
		} else {
			iv_setup2_status.setImageResource(R.drawable.lock);
		}
	}
	
	public void next(View view){
		String savedSim = sp.getString("sim", "");
		if (TextUtils.isEmpty(savedSim)){ 
			Toast.makeText(getApplicationContext(), "没有绑定sim号码，请绑定！", 0).show();
			return;
		}
		
		loadActivity(Setup3Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}
	
	public void pre(View view){
		loadActivity(Setup1Activity.class);
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
	
	public void bindSim(View v){
		String savedSim = sp.getString("sim", "");
		if (TextUtils.isEmpty(savedSim)){ // 没有存储sim号码，进行存储操作
			String sim = tm.getSimSerialNumber();
			Editor editor = sp.edit();
			editor.putString("sim", sim);
			editor.commit();
			iv_setup2_status.setImageResource(R.drawable.lock);
		} else { // 已经存储了sim号码，接触存储
			Editor editor = sp.edit();
			editor.putString("sim", null);
			editor.commit();
			iv_setup2_status.setImageResource(R.drawable.unlock);
		}
		
	}
}
