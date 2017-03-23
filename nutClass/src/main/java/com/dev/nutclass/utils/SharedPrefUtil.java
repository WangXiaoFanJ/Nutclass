package com.dev.nutclass.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.text.TextUtils;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.entity.UserEntity;

/**
 * preference 存储管理，提供统一管理preference的地方。
 * 
 * @author MR.Huang
 * @date 2013-11-22
 */
public class SharedPrefUtil {
	public static final String TAG = "SharedPrefUtil";
	public static final String PREFER_NAME = "com.dev.nutclass";

	private static SharedPrefUtil instance;

	private SharedPreferences mPrefer;

	private static final String KEY_SESSION = "key_session";// 登陆用户的信息，json结构

	private static final String KEY_MOBILE = "key_mobile";// 用户手机号

	private static final String KEY_USER = "key_user";

	private static final String KEY_MESSAGE = "key_msg_rec";// 是否关闭消息提醒

	private static final String KEY_IMG_CACHE_TIME = "key_img_clear";// 判断是否清理图片

	private static final String KEY_TOKEN = "key_token";// token
	private static final String KEY_CATEGORY_CACHE = "key_category";// category
	private static final String KEY_LON="key_lon";//经度
	private static final String KEY_LAT="key_lat";//纬度  只有在启动的时候才换
	private static final String KEY_LOCATION="key_location";//更换城市
	private static final String KEY_IS_FIRST="key_is_first";//判断是否第一次安装
	private static final String KEY_IS_CLICK ="key_is_click";
	private static final String KEY_VERSION_NAME="key_version_name";

	public static final  String KEY_KEEP_TIME = "key_keep_time";
	public static long LAST_TIME = System.currentTimeMillis() / 1000;

	private SharedPrefUtil() {
		mPrefer = ApplicationConfig.getInstance().getBaseContext()
				.getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
	}

	public static synchronized final SharedPrefUtil getInstance() {
		if (instance == null) {
			instance = new SharedPrefUtil();
		}
		return instance;
	}
	public void setCategoryCache(String jsonStr) {
		setString(KEY_CATEGORY_CACHE, jsonStr);
	}

	public String getCategoryCache() {
		return getString(KEY_CATEGORY_CACHE);
	}

	public String getString(String key) {
		return mPrefer.getString(key, "");
	}

	public void setString(String key, String value) {
		SharedPreferences.Editor editor = mPrefer.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void setLong(String key, long value) {
		SharedPreferences.Editor editor = mPrefer.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public long getLong(String key, long defValue) {
		return mPrefer.getLong(key, defValue);
	}

	public boolean getBoolean(String key) {
		return mPrefer.getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return mPrefer.getBoolean(key, defValue);
	}

	public void setBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = mPrefer.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public int getInt(String key, int defValue) {
		return mPrefer.getInt(key, defValue);
	}

	public void setInt(String key, int value) {
		SharedPreferences.Editor editor = mPrefer.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public boolean contains(String key) {
		return mPrefer.contains(key);
	}

	// LoginSession
	public void setUserSession(String userJson) {
		setString(KEY_SESSION, userJson);
	}	public UserEntity getSession() {
		String userJson = getString(KEY_SESSION);
		if (TextUtil.isNotNull(userJson)) {
			JSONObject json;
			try {
				json = new JSONObject(userJson);
				if (json != null) {
					UserEntity entity = new UserEntity(json);
					return entity;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * 存储图片缓存清理的时间
	 * 
	 */
	public void saveImgCacheClearTime() {
		long curtime = System.currentTimeMillis();
		SharedPreferences.Editor editor = mPrefer.edit();
		editor.putLong(KEY_IMG_CACHE_TIME, curtime);
		editor.commit();
	}
	public void  setKeepClearCacheTime(long time){
		setLong(KEY_KEEP_TIME,time);
	}
	public Long getKeepClearCacheTime(){
		return  getLong(KEY_KEEP_TIME,0);
	}
	/**
	 * 是否需要清理图片缓存
	 * 
	 */
	public boolean needImgCacheClear() {
		long curtime = System.currentTimeMillis();
		long preftime = mPrefer.getLong(KEY_IMG_CACHE_TIME, 0L);
		LogUtil.d("curtime:" + curtime + " preftime:" + preftime);
		// 间隔3小时，就再次尝试清理文件的图片缓存
		if (curtime - preftime > 10800000L) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取加密后的base64的token
	 * */
	public String getToken() {
		String token = getString(KEY_TOKEN);
		return token;
//		return "666666";
	}

	/**
	 * 1、在获取验证码的时候生成一个不完整token 2、在登陆成功的地方设置token
	 * */
	public void setToken(String value) {
		setString(KEY_TOKEN, value);
	}


	public String getUid() {
		String uid = "";
		if (isLogin()) {
			uid = getSession().getUid();
		}
		return uid;
//		return "11";
	}

	public boolean isLogin() {
		if (TextUtil.isNotNull(getToken())
				&& getSession() != null) {
			return true;
		}
		return false;
	}

	 

	public void setMobile(String mobile) {
		setString(KEY_MOBILE, mobile);
	}

	public String getMobile() {
		return getString(KEY_MOBILE);
	}
	public void setFirst() {
		setBoolean(KEY_IS_FIRST, false);
	}
	
	public boolean isFirst() {
		return getBoolean(KEY_IS_FIRST,true);
	}

	public void logout() {

	}
	public String getLon() {
		if (TextUtils.isEmpty(getString(KEY_LON))) {
			return "116.49702654684";

		}else {
			return getString(KEY_LON);
		}
	}
	public void setLon(String lon) {
		setString(KEY_LON,lon);
	}
	public String getLat() {
		if (TextUtils.isEmpty(getString(KEY_LAT))) {
			return "39.8977879699";

		}else {
			return getString(KEY_LAT);
		}
	}
	public void setLat(String lat) {
		setString(KEY_LAT,lat);
	}
	public String getLocation() {
		return getString(KEY_LOCATION);
	}
	public void setLocation(String location) {
		setString(KEY_LOCATION,location);
	}

	public void setVersionName(String versionName){
		setString(KEY_VERSION_NAME,versionName);
	}
	public String getVersionName(){
		return getString(KEY_VERSION_NAME);
	}
}
