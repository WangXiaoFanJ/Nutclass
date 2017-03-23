package com.dev.nutclass;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.Toast;

import com.dev.nutclass.activity.CardListActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.utils.LogUtil;
import com.kepler.jd.Listener.AsyncInitListener;
import com.kepler.jd.login.KeplerApiManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

public class NutClassApplication extends MultiDexApplication {
	public static final String TAG = "MyApplication";
	public  static String device_token = "";
	// 声明AMapLocationClient类对象
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.i(TAG, "onCreate");
		//友盟推送
		PushAgent mPushAgent = PushAgent.getInstance(this);
//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) {
				//注册成功会返回device token
				device_token = deviceToken;
				Log.d("===","==="+device_token);
			}
			@Override
			public void onFailure(String s, String s1) {

			}
		});
		//友盟自定义Action
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void dealWithCustomAction(Context context, UMessage msg) {
				if(msg.extra.get("type").equals("1")){
					Intent intent = new Intent();
					intent.setClass(context, CardListActivity.class);
					intent.putExtra(Const.KEY_TYPE,
							CardListActivity.TYPE_FROM_ALL_ORDER);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);

		//友盟分享各平台配置
		PlatformConfig.setWeixin("wxf4451af60a7f3501",
				"ba438da1dabe212a10f41f25c26692b1");
		// 微信 appid appsecret
		PlatformConfig.setSinaWeibo("4059899234",
				"48a6b4a2f61de6bc02ad5e8083a8a220");
		Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
		// 新浪微博 appkey appsecret
		PlatformConfig.setQQZone("1105164001",
				"gADdUXVvwRQEIknK");
		// QQ和Qzone appid appkey
		// Application配置信息
		KeplerApiManager.asyncInitSdk(this,SnsUtil.KJD_APP_KEY ,SnsUtil.KJD_APP_SECRET,new AsyncInitListener() {
			@Override
			public void onSuccess() {
				LogUtil.d(TAG, "kjd_success");
			}
			@Override
			public void onFailure() {
				LogUtil.d(TAG, "kjd_failure");
			}});
		ApplicationConfig.getInstance().initConfig(this);
		/**
		 * 在开发应用时都会和Activity打交道，而Application使用的就相对较少了。
		 * Application是用来管理应用程序的全局状态的，比如载入资源文件。
		 * 在应用程序启动的时候Application会首先创建，然后才会根据情况(Intent)启动相应的Activity或者Service。
		 * 在本文将在Application中注册未捕获异常处理器。
		 */
		// umeng推送
		// PushManager.getInstance().initPushMangaer(this);

	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		LogUtil.i(TAG, "onTerminate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		LogUtil.i(TAG, "onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		LogUtil.i(TAG, "onLowMemory");
		ApplicationConfig.getInstance().clearMemoryCache(this);
		System.gc();
	}

}
