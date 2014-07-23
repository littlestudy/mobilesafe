package com.stu.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;

import com.stu.mobilesafe.R;

public class SmsReceiver extends BroadcastReceiver {

	private static final String TAG = "SmsReceiver";
	private LocationManager lm;
	private MyListener listener;
	private SharedPreferences sp;
	private DevicePolicyManager dpm;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean protectingstatus = sp.getBoolean("protectingstatus", false);
		if (protectingstatus){ // 开启防盗保护
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])obj);
				String sender = smsMessage.getOriginatingAddress();
				String body = smsMessage.getMessageBody();
				if ("#*location*#".equals(body)){
					Log.i(TAG, "获取手机位置");
					String safenumber = sp.getString("safenumber", "");
					lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
					listener = new MyListener(safenumber);
					lm.requestLocationUpdates("gps", 0, 0, listener);
					abortBroadcast();
				} else if ("#*alarm*#".equals(body)){
					Log.i(TAG, "播放报警铃声");
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(false);
					player.setVolume(1.0f, 1.0f);
					player.start();
					abortBroadcast();
				} else if ("#*wipedata*#".equals(body)){
					Log.i(TAG, "远程清除数据");
					dpm.wipeData(0);
					abortBroadcast();
				} else if ("#*lockscreen*#".equals(body)){
					Log.i(TAG, "远程锁屏");
					dpm.resetPassword("123", 0);
					dpm.lockNow();
					abortBroadcast();
				}
				
			}
		}
	}
	
	private class MyListener implements LocationListener{
		private String safenumber;
		
		public MyListener(String safenumber){
			this.safenumber = safenumber;
		}

		//当位置变化的时候调用的方法
		@Override
		public void onLocationChanged(Location location) {
			String latitude = "latitude:"+location.getLatitude();
			String longitude  = "longitude:"+location.getLongitude();
			String accuarcy = "accuarcy:"+location.getAccuracy();
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(safenumber, null, latitude + "-" + longitude + "-" + accuarcy
					, null, null);
			lm.removeUpdates(listener);
			listener = null;
		}

		//当状态变化的时候调用的方法。可用--》不可以  不可以--》可用
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		//当一个位置提供者可用的时候
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		//当一个位置提供者不可用的时候
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
	}

}






























