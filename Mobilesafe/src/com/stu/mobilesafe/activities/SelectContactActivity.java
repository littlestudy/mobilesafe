package com.stu.mobilesafe.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.stu.mobilesafe.R;
import com.stu.mobilesafe.domain.ContactInfo;
import com.stu.mobilesafe.engine.ContactInfoProvider;

public class SelectContactActivity extends Activity {

	private List<ContactInfo> infos;
	private LinearLayout ll_loading;
	private ListView lv_contacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contact);
		
		lv_contacts = (ListView)findViewById(R.id.lv_contacts);
		
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		ll_loading.setVisibility(View.VISIBLE);
		
		new Thread(){
			public void run() {
				infos = ContactInfoProvider.getContactInfos(SelectContactActivity.this);
				runOnUiThread(new Runnable() {
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						lv_contacts.setAdapter(new ContactAdapter());						
					}
				});
			};
		}.start();
		
		lv_contacts.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = infos.get(position).getPhone();
				Intent data= new Intent();
				data.putExtra("phone", phone);
				setResult(0, data); // 设置返回数据
				finish();  //关闭本Activity
			}
		});
	}
	
	private class ContactAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return infos.size();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.item_contact, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_contact_name);
			TextView tv_phone = (TextView) view.findViewById(R.id.tv_contact_phone);
			
			tv_name.setText(infos.get(position).getName());
			tv_phone.setText(infos.get(position).getPhone());
			
			return view;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}


		
	}
	
}
