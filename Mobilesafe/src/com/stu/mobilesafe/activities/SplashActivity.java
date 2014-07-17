package com.stu.mobilesafe.activities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stu.mobilesafe.R;
import com.stu.mobilesafe.utils.StreamTools;

public class SplashActivity extends Activity {
	
	protected static final String TAG = "SplashActivity";
	
	protected static final int SERVER_CODE_ERROR = 2001;
	protected static final int URL_ERROR = 2002;
	protected static final int NETWORK_ERROR = 2003;
	protected static final int JSON_ERROR = 2004;
	protected static final int SHOW_UPDATE_DIALOG = 2005;
	
	private TextView tv_splash_version; 
	private RelativeLayout rl_splash_root;
	private TextView tv_splash_progress;
	private String description;
	private String path;
	
	private Handler handler = new Handler(){ //创建一个消息处理器
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SERVER_CODE_ERROR:
				Toast.makeText(getApplicationContext(), "获取更新信息失败，错误码："+SERVER_CODE_ERROR, 1).show();
				loadMainUI();
				break;
			case URL_ERROR:
				Toast.makeText(getApplicationContext(), "获取更新信息失败，错误码："+URL_ERROR, 1).show();
				loadMainUI();
				break;
			case NETWORK_ERROR:
				Toast.makeText(getApplicationContext(), "您的网络不给力啊，再试下下哈哈", 1).show();
				loadMainUI();
				break;
			case JSON_ERROR:
				Toast.makeText(getApplicationContext(), "获取更新信息失败，错误码："+JSON_ERROR, 1).show();
				loadMainUI();
				break;
			case SHOW_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		initView();
		
		playAnimation(); //播放一个动画效果。
		
		checkVersion(); //连接服务器检查更新信息
	}
	
    /**
     * 显示更新提醒对话框
     */	
	protected void showUpdateDialog(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setOnCancelListener(new OnCancelListener() {			
			@Override
			public void onCancel(DialogInterface dialog) {
				loadMainUI();
			}
		});
		builder.setTitle("更新提醒")
			.setMessage(description)
			.setPositiveButton("立刻升级",new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//判断sd卡是否存在  
					
					//下载apk，替换安装。
					FinalHttp fh = new FinalHttp();
					String saveFilepath =  Environment.getExternalStorageDirectory().getAbsolutePath() + "/xx.apk";
					HttpHandler handler = fh.download(path, saveFilepath, false, new AjaxCallBack<File>() { //调用download方法开始下载
						@Override
						public void onLoading(long count, long current) {
							super.onLoading(count, current);
							int progress = (int) (current * 100 / count);
							tv_splash_progress.setText("下载进度：" + progress + "%");							
						}
						
						@Override
						public void onSuccess(File t) {
							super.onSuccess(t);
							Toast.makeText(getApplicationContext(), "下载成功，替换安装", 0).show();
//							<action android:name="android.intent.action.VIEW" />
//			                <category android:name="android.intent.category.DEFAULT" />
//			                <data android:scheme="content" />
//			                <data android:scheme="file" />
//			                <data android:mimeType="application/vnd.android.package-archive" />
							
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							intent.addCategory("android.intent.category.DEFAULT");							
							intent.setDataAndType(Uri.fromFile(t), "application/vnd.android.package-archive");
							startActivity(intent);
						}
						
						@Override
						public void onFailure(Throwable t, int errorNo, String strMsg) {
							super.onFailure(t, errorNo, strMsg);
							Toast.makeText(getApplicationContext(), "下载失败", 0).show();
							loadMainUI();
						}
						
					});
				}
			})
			.setNegativeButton("下次再说", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					loadMainUI();
				}
			})
			.show();
	}
	
	/**
     * 进入主界面
     */
	protected void loadMainUI(){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent); 
		finish(); //关闭掉splash界面。
	}
	
	private void checkVersion(){
		Log.i(TAG, "checkVersion");
		new Thread(){
			public void run() {
				Message msg = Message.obtain();
				long startTime = System.currentTimeMillis();
				
				try {
					URL url = new URL(getResources().getString(R.string.serverurl));
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET"); //区分大小写
					conn.setConnectTimeout(2000); //设置连接超时时间
					int code = conn.getResponseCode();//获取服务器状态码
					if (code == 200){//请求成功
						Log.i(TAG, "code:" + code);
						System.out.println(code);
						InputStream is = conn.getInputStream();//json 字符串。
						String result = StreamTools.readStream(is);
						Log.i(TAG, "result:" + result);
						JSONObject jsonObj = new JSONObject(result);
						String version = (String) jsonObj.get("version");
						description = jsonObj.getString("description");
						path = jsonObj.getString("path");
						Log.i(TAG, "result:" + result);
						Log.i(TAG, "version:" + version);
						Log.i(TAG, "description:" + description);
						Log.i(TAG, "path:" + path);
						if (getVersion().equals(version)){ //判断 服务器的版本号 和 客户端的版本号是否一致。
							Log.i(TAG,"版本号相同，进入主界面");
							SystemClock.sleep(2000);
							loadMainUI();
						} else {
							Log.i(TAG,"版本号不相同，弹出更新提醒对话框");
							msg.what = SHOW_UPDATE_DIALOG;
						}
					} else { //状态码不正确。
						Log.i(TAG, "code:" + code);
						msg.what = SERVER_CODE_ERROR;
					}
				} catch (MalformedURLException e) {//路径错误（协议错误了）//自定义一些错误码。
					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (IOException e) { //访问网络出错
					e.printStackTrace();
					msg.what = NETWORK_ERROR;
				} catch (JSONException e) {  //解析json文件出错了。
					e.printStackTrace();
					msg.what = JSON_ERROR;
				} finally {					
					long endTime = System.currentTimeMillis();
					long dTime = startTime - endTime;
					if (dTime < 2000){
						SystemClock.sleep(2000 - dTime);
					}
					handler.sendMessage(msg);
				}
			};
		}.start();
	}
	
	private void initView(){
		tv_splash_version = (TextView)findViewById(R.id.tv_splash_version);
		rl_splash_root = (RelativeLayout)findViewById(R.id.rl_splash_root);
		tv_splash_progress = (TextView)findViewById(R.id.tv_splash_progress);
		
		tv_splash_version.setText("版本号：" + getVersion());		
	}
	
	private void playAnimation(){
		AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
		aa.setDuration(2000);
		rl_splash_root.startAnimation(aa);
	}	
	
	/**
     * 获取应用程序的版本号信息
     * @return 应用程序的版本号
     */	
	private String getVersion(){		
		PackageManager pm = getPackageManager(); //获取一个系统的包管理器。
		try {
			PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0); //清单文件 manifest.xml文件的所有的信息
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// can't reach 不可能发生的异常
			return "";
		}
	}
}











































