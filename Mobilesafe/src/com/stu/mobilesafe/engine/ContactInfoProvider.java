package com.stu.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.stu.mobilesafe.domain.ContactInfo;

public class ContactInfoProvider {

	/**
	 * 获取系统里面所有联系人信息
	 * @param context 上下文
	 * @return
	 */
	public static List<ContactInfo> getContactInfos(Context context){
		ContentResolver resolver = context.getContentResolver();
		
		List<ContactInfo> infos = new ArrayList<>();
		Uri raw_contactUri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		// 查询raw_contacts表，将id查询出来
		Cursor cursor = resolver.query(raw_contactUri, new String[]{"contact_id"}, null, null, null);
		while (cursor.moveToNext()){
			String id = cursor.getString(0);
			ContactInfo info = new ContactInfo();
			Cursor dataCursor = resolver.query(dataUri, new String[]{"data1", "mimetype"} 
										, "raw_contact_id=?", new String[]{id}, null);
			while(dataCursor.moveToNext()){
				String data1 = dataCursor.getString(0);
				String mimetype = dataCursor.getString(1);
				if ("vnd.android.cursor.item/name".equals(mimetype)){
					info.setName(data1);
				} else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)){
					info.setPhone(data1);
				}
			}
			dataCursor.close();
			infos.add(info);
		}
		cursor.close();
		
		return infos;
	}
}


























