package com.stu.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 开机检测sim的广播接收者
 * @author yaomin
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	private static final String TAG = "BootCompleteReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "收到广播");
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protectingstatus = sp.getBoolean("protectingstatus", false);
		if (protectingstatus){ // 开启防盗保护
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = tm.getSimSerialNumber();
			String bindSim = sp.getString("sim", "");
			if (!currentSim.equals(bindSim + "123")){ // sim 发生改变	（这里在bindSim后边加“123”是为了模拟sim不同的情况）
				Log.i(TAG, "sim 发生改变");
				SmsManager smsManger = SmsManager.getDefault();
				String safenumber = sp.getString("safenumber", "");
				Log.i(TAG, "safename" + safenumber);
				smsManger.sendTextMessage(safenumber, null, "sim changed", null, null);
			}
		} 
	}

}
