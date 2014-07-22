package com.stu.mobilesafe.activities;

import com.stu.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Activity extends BaseSetupActivity {

	
	private EditText et_setup3_phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		
		et_setup3_phone = (EditText)findViewById(R.id.et_setup3_phone);
		String safenumber = sp.getString("safenumber", "");
		if (!TextUtils.isEmpty(safenumber)){
			et_setup3_phone.setText(safenumber);
		}
	}
	
	public void next(View view){
		String safenumber = et_setup3_phone.getText().toString().trim();
		
		if (TextUtils.isEmpty(safenumber)){
			Toast.makeText(getApplicationContext(), "请设置号码", 0).show();
			return ;
		}
		
		Editor editor = sp.edit();
		editor.putString("safenumber", safenumber);
		editor.commit();
		
		loadActivity(Setup4Activity.class);
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}	
		
	public void pre(View view){
		loadActivity(Setup2Activity.class);
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
	
	public void selectContact(View view){
		Intent intent = new Intent(this, SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null){
			String phone = data.getStringExtra("phone");
			et_setup3_phone.setText(phone);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
