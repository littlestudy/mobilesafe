package com.stu.mobilesafe.receiver;

import com.stu.mobilesafe.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protectingstatus = sp.getBoolean("protectingstatus", false);
		if (protectingstatus){ // 开启防盗保护
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj);
				String sender = smsMessage.getOriginatingAddress();
				String body = smsMessage.getMessageBody();
				if ("#*location*#".equals(body)){
					Log.i(TAG, "获取手机位置");					
				} else if ("#*alarm*#".equals(body)){
					Log.i(TAG, "播放报警铃声");
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(false);
					player.setVolume(1.0f, 1.0f);
					player.start();
				} else if ("#*wipedata*#".equals(body)){
					Log.i(TAG, "远程清除数据");
				} else if ("#*lockscreen*#".equals(body)){
					Log.i(TAG, "远程锁屏");
				}
				abortBroadcast();
			}
		}
	}

}






























