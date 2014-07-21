package com.stu.mobilesafe.test;

import java.util.List;

import com.stu.mobilesafe.domain.ContactInfo;
import com.stu.mobilesafe.engine.ContactInfoProvider;

import android.test.AndroidTestCase;

public class TestContactInfoProvider extends AndroidTestCase {

	public void test() throws Exception{
		List<ContactInfo> infos = ContactInfoProvider.getContactInfos(getContext());
		for(ContactInfo info : infos){
			System.out.println(info);
		}
	}
}
