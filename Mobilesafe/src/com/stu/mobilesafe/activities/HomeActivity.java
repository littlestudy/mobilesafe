package com.stu.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stu.mobilesafe.R;
import com.stu.mobilesafe.utils.Md5Utils;

public class HomeActivity extends Activity {

	private GridView gv_home;
	private static final String[] names = { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private static final int[] icons = { R.drawable.safe,
			R.drawable.callmsgsafe_selector, R.drawable.app,
			R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
			R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings };
	protected static final String TAG = "HomeActivity";
	
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		gv_home = (GridView)findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter());
		
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:// 手机防盗。
						// 判断用户是否设置过密码
					String savedPassword = sp.getString("password", null); 
					if (TextUtils.isEmpty(savedPassword)){
						// 没有设置过密码
						Log.i(TAG, "没有设置过密码，弹出设置密码对话框");
						showSetupPasswordDialog();
					} else {
						// 设置过密码
						Log.i(TAG, "设置过密码,弹出输入密码对话框");
						showEnterPasswordDialog();
					}
					break;
				}
			}

		});
	}
	
	//享元模式
	private AlertDialog dialog;
	private EditText et_password;
	private EditText et_password_confirm;
	private Button bt_ok;
	private Button bt_cancel;
	
	private void showSetupPasswordDialog() {
		// 自定义内容的对话框
		AlertDialog.Builder builer = new Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_setup_pwd, null);
		et_password = (EditText) view.findViewById(R.id.et_password);
		et_password_confirm = (EditText) view.findViewById(R.id.et_password_confirm);
		bt_ok = (Button)view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		
		bt_ok.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				String password_confirm = et_password_confirm.getText().toString().trim();
				if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password_confirm)){
					Toast.makeText(getApplicationContext(), "密码不能为空，请重新输入！", 0).show();
					return;
				}
				
				if (!password.equals(password_confirm)){
					Toast.makeText(getApplicationContext(), "两次输入的密码不相同！", 0).show();
					return;
				}
				
				Editor editor = sp.edit();
				editor.putString("password", Md5Utils.encode(password));
				editor.commit();
				dialog.dismiss();
			}
		});
		
		bt_cancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		builer.setView(view);
		dialog = builer.create();
		dialog.show();
	}
	
	private void showEnterPasswordDialog() {
		// 自定义内容的对话框
		AlertDialog.Builder builer = new Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_enter_pwd, null);
		et_password = (EditText) view.findViewById(R.id.et_password);		
		bt_ok = (Button)view.findViewById(R.id.bt_ok);
		bt_cancel = (Button) view.findViewById(R.id.bt_cancel);
		
		bt_ok.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				//用户输入的密码。
				String password = et_password.getText().toString().trim();
				if (TextUtils.isEmpty(password)){
					Toast.makeText(getApplicationContext(), "密码不能为空，请重新输入！", 0).show();
					return;
				}
				//得到原来保存的密码加密后的密文
				String savedPassword = sp.getString("password", "");
				
				if (!savedPassword.equals(Md5Utils.encode(password))){
					Toast.makeText(getApplicationContext(), "输入密码不正确！", 0).show();
					return;
				}
				
				//密码正确，进入手机防盗的界面。
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, LostFindActivity.class);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		
		bt_cancel.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		builer.setView(view);
		dialog = builer.create();
		dialog.show();		
	}
	
	// Default Simple Basic Base
	private class HomeAdapter extends BaseAdapter{
		@Override
		public int getCount() { // 返回有多少个条目
			return names.length;
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		// 返回每个条目的view对象
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.item_home, null);
			ImageView iv = (ImageView) view.findViewById(R.id.iv_home_icon);
			TextView tv = (TextView) view.findViewById(R.id.tv_home_name);
			
			iv.setImageResource(icons[position]);
			tv.setText(names[position]);
			return view;
		}
		
	}
}
