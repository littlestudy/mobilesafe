package com.stu.mobilesafe.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.stu.mobilesafe.R;

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
	
	private AlertDialog dialog;
	
	private void showSetupPasswordDialog() {
		AlertDialog.Builder builer = new Builder(this);
		View view = View.inflate(getApplicationContext(), R.layout.dialog_setup_pwd, null);
		builer.setView(view);
		dialog = builer.create();
		dialog.show();
	}
	
	private void showEnterPasswordDialog() {
				// TODO Auto-generated method stub
				
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
