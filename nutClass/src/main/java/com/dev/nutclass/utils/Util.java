package com.dev.nutclass.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.LoginTogetherActivity;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.SingleItemCardEntity;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.squareup.okhttp.Request;

public class Util {
	private static final String TAG = "Util";

	/**
	 * 校验手机号码 根据实际开发于2009年9月7日最新统计：
	 * 中国电信发布中国3G号码段:中国联通185,186;中国移动188,187;中国电信189,180共6个号段。
	 * 3G业务专属的180-189号段已基本分配给各运营商使用,
	 * 其中180、189分配给中国电信,187、188归中国移动使用,185、186属于新联通。
	 * 中国移动拥有号码段：139、138、137、136、135
	 * 、134、159、158、157（3G）、152、151、150、188（3G）、187（3G）;14个号段
	 * 中国联通拥有号码段：130、131、132、155、156（3G）、186（3G）、185（3G）;6个号段
	 * 中国电信拥有号码段：133、153、189（3G）、180（3G）;4个号码段 移动:
	 * 2G号段(GSM网络)有139,138,137,136,135,134(0-8),159,158,152,151,150
	 * 3G号段(TD-SCDMA网络)有157,188,187 147是移动TD上网卡专用号段. 联通:
	 * 2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185 电信:
	 * 2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,180
	 * 
	 * //181号段为继133、153、189、180号段之后中国电信移动业务推出的3G号段
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		// Pattern p =
		// Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		LogUtil.i(TAG, "mobile=" + mobiles + ",matche=" + m.matches());
		return m.matches();
	}

	/**
	 * 添加位置信息的参数
	 * 
	 * @param reqUrl
	 * @return
	 */
	public static String addLoactionParams(String reqUrl) {

		String lat = String.valueOf(ApplicationConfig.getInstance().getLat());// 纬度
		String lon = String.valueOf(ApplicationConfig.getInstance().getLng());// 经度
		reqUrl = removeParam(reqUrl, "lat");
		reqUrl = removeParam(reqUrl, "lng");
		StringBuilder sBuilder = new StringBuilder(reqUrl);
		sBuilder.append("&lat=").append(lat);
		sBuilder.append("&lng=").append(lon);
		return sBuilder.toString();
	}

	// 替换某个参数的参数值
	public static String replaceParams(String url, String key, String value) {
		if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
			url = url.replaceAll("(" + key + "=[^&]*)", key + "=" + value);
		}
		return url;
	}

	// 删除某个参数
	public static String removeParam(String url, String param) {
		String reg = null;
		reg = "(" + param + "=[^&]*&?)";
		url = url.replaceAll(reg, "");
		if (url.endsWith("&")) {
			url = url.substring(0, url.lastIndexOf("&"));
		}
		return url;
	}

	public static String getDistatce(double lat1, double lon1) {
		double lat = ApplicationConfig.getInstance().getLat();// 纬度
		double lon = ApplicationConfig.getInstance().getLng();// 经度

		double R = 6371;
		double distance = 0.0;
		double dLat = (lat - lat1) * Math.PI / 180;
		double dLon = (lon - lon1) * Math.PI / 180;
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(lat1 * Math.PI / 180)
				* Math.cos(lon * Math.PI / 180) * Math.sin(dLon / 2)
				* Math.sin(dLon / 2);
		distance = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * R;
		if (distance > 1000) {
			String result = String.format("%.2f km", distance / 1000);
			return result;
		}
		return String.format("%.0f m", distance);
	}

	public static String formatDistance(String dist) {

		try {
			double distance = Double.valueOf(dist);
			if (distance > 1000) {
				String result = String.format("%.2f km", distance / 1000);
				return result;
			}
			return distance + " m";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dist + " m";
	}

	// 调用web浏览器
	public static boolean openBrowser(Context context, String url) {
		try {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(url);
			intent.setData(content_url);
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	public static String getImei(Context context) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telephonyManager.getDeviceId();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "";
	}

	public static String getSimNum(Context context) {
		try {
			TelephonyManager telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telMgr.getSimSerialNumber();
		} catch (Exception e) {
			// Should never happen!
			e.printStackTrace();
		}
		return "";
	}

	public static String getImsi(Context context) {
		try {
			TelephonyManager telMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			return telMgr.getSubscriberId();
		} catch (Exception e) {
			// Should never happen!
			e.printStackTrace();
		}
		return "";
	}

	public static int getAndroidSDK() {
		// TODO Auto-generated method stub
		int version = 0;
		try {
			version = android.os.Build.VERSION.SDK_INT;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;

	}

	/**
	 * 是否插入sdcard
	 * 
	 * @return
	 */
	public static boolean isSDCardExist() {
		boolean sdCardExist = false;
		sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		return sdCardExist;
	}

	/**
	 * 显示软键盘
	 * 
	 * @param activity
	 * @param view
	 */
	public static void showSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); // 显示软键盘
	}

	/**
	 * 隐藏软键盘
	 */
	public static void hideSoftInput(Activity activity, View view) {
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 判断是否显示软键盘
	 * 
	 * @param isShowSoftPan
	 *            是否显示软键盘
	 * */
	public static void setInputMethodVisibility(Activity activity,
			boolean isShowSoftPan, TextView editText) {
		InputMethodManager inputManager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if ((inputManager == null) || (editText == null)) {
			return;
		}
		if (isShowSoftPan) {
			activity.getWindow()
					.setSoftInputMode(
							WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
									| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			inputManager.showSoftInput(editText, 0);
		} else {
			if (inputManager.isActive(editText)) {
				activity.getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
										| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				inputManager.hideSoftInputFromWindow(editText.getWindowToken(),
						0);

			}
		}
	}

	/**
	 * 返回资源图片的bitmap
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap decodeResource(Context context, int resId) {
		try {
			Bitmap bitmap = null;
			Resources r;
			if (context == null) {
				r = Resources.getSystem();
			} else {
				r = context.getResources();
			}
			bitmap = BitmapFactory.decodeResource(r, resId);
			return bitmap;
		} catch (Exception e) {
			LogUtil.w(e);
		} catch (OutOfMemoryError error) {
			// 实在要出现内存不足的问题，清空缓存，调GC，没办法
			LogUtil.w(error);
			System.gc();
		}
		return null;
	}

	/**
	 * 安装成功后自动创建桌面图标
	 * */
	public static void createShortCut(Activity act, int iconResId,
			int appnameResId) {

		// com.android.launcher.permission.INSTALL_SHORTCUT

		Intent shortcutintent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重复创建
		shortcutintent.putExtra("duplicate", false);
		// 需要现实的名称
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				act.getString(appnameResId));
		// 快捷图片
		Parcelable icon = Intent.ShortcutIconResource.fromContext(
				act.getApplicationContext(), iconResId);
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		// 点击快捷图片，运行的程序主入口
		shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
				new Intent(act.getApplicationContext(), act.getClass()));
		// 发送广播
		act.sendBroadcast(shortcutintent);
	}

	// 检查是否登陆
	public static boolean checkLogin(Context context) {
		if (!SharedPrefUtil.getInstance().isLogin()) {
			Intent intent = new Intent(context, LoginTogetherActivity.class);
			context.startActivity(intent);
			return false;}
		return true;

	}

	// 退出登陆
	public static void loginOut(Context context) {
		SharedPrefUtil.getInstance().setMobile("");
		SharedPrefUtil.getInstance().setToken("");
		SharedPrefUtil.getInstance().setUserSession("");
	}

	//
	public static String decryptToken(Context context, String token) {
		InputStream inPublic;
		try {
			inPublic = context.getResources().getAssets()
					.open("rsa_public_key.pem");
			PublicKey publicKey;
			publicKey = RSAUtils.loadPublicKey(inPublic);
			byte[] beforedecrypt = Base64.decode(token.getBytes(),
					Base64.DEFAULT);
			byte[] decryptByte = RSAUtils.decrypt(beforedecrypt, publicKey);
			return new String(decryptByte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String encryptToken(Context context, UserEntity userEntity) {
		String encryptContent = null;
		if (userEntity == null) {
			encryptContent = String.format(UrlConst.TOKEN_FORMAT, "",
					SharedPrefUtil.getInstance().getMobile(), getTimeStamp(),
					ApplicationConfig.getInstance().getImei(), "0");
		} else {
			encryptContent = String.format(UrlConst.TOKEN_FORMAT, userEntity
					.getUid(), SharedPrefUtil.getInstance().getMobile(),
					getTimeStamp(), ApplicationConfig.getInstance().getImei(),
					"0");
		}
		try {
			LogUtil.d(TAG, "encryptContent=" + encryptContent);
			// 从文件中得到私钥
			InputStream inPublic = context.getResources().getAssets()
					.open("rsa_public_key.pem");
			PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
			byte[] encryptByte = RSAUtils.encrypt(encryptContent.getBytes(),
					publicKey);
			String encryptStr = new String(Base64.encode(encryptByte,
					Base64.DEFAULT));
			return encryptStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getTimeStamp() {
		long time = System.currentTimeMillis() / 1000;
		time = time + 1800;
		SharedPrefUtil.LAST_TIME = time - 600;
		return String.valueOf(time);
	}

	public static Map<String, String> splitToken(String decryptToken) {
		if (TextUtils.isEmpty(decryptToken)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		String[] str = decryptToken.split("&");
		if (str != null && str.length > 0) {
			for (int i = 0; i < str.length; i++) {
				String tmp = str[i];
				if (TextUtils.isEmpty(tmp)) {
					continue;
				}
				String[] item = tmp.split("=");
				if (item == null || item.length == 0) {
					continue;
				}
				if (item.length == 1) {
					map.put(item[0], "");
				} else {
					map.put(item[0], item[1]);
				}
			}
		}
		return map;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected())
			{
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED)
				{
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}

	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static void setGender(Context context, ImageView genderIv, int i) {
		if (i == 1) {
			genderIv.setImageResource(R.drawable.gender_male);
		} else {
			genderIv.setImageResource(R.drawable.gender_female);
		}
	}

	public static void setFollow(Context context, Button followBtn,
			final boolean isFollow) {
		if (isFollow) {
			followBtn.setText(context.getResources().getString(
					R.string.text_follow_ed));
			followBtn.setBackgroundResource(R.drawable.btn_pink_dis);
		} else {
			followBtn.setText(context.getResources().getString(
					R.string.text_follow));
			followBtn
					.setBackgroundResource(R.drawable.selector_btn_common_pink);
		}
	}

	public static boolean isOwner(String uid) {
		String ownerId = SharedPrefUtil.getInstance().getUid();
		if (TextUtil.isNotNull(ownerId) && TextUtil.isNotNull(uid)
				&& ownerId.equals(uid)) {
			return true;
		}
		return false;
	}

	/**
	 * Cache Category & Tag
	 * */

	public static List<BaseCardEntity> categoryList;

	public static void parseCategoryCache(String json) {
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null) {
				JSONArray dataArray = jsonObj.optJSONArray("data");
				if (dataArray != null) {
					categoryList = new ArrayList<BaseCardEntity>();
					for (int i = 0; i < dataArray.length(); i++) {// 解析p1
						JSONObject jsonP1 = dataArray.optJSONObject(i);
						SingleItemCardEntity entity = new SingleItemCardEntity();
						entity.setId(jsonP1.optString("cid"));
						entity.setDesc(jsonP1.optString("value"));
						categoryList.add(entity);
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<BaseCardEntity> getCategoryList() {
		if (categoryList != null && categoryList.size() > 0) {
			return categoryList;
		}
		return null;

	}

	/**
	 * Cache Category & Tag end
	 * */
	/**
	 * Cache City begin
	 * */

	public static void cacheCity() {
		// String cityCacheStr=SharedPrefUtil.getInstance().getCityCache();
		// if(!TextUtils.isEmpty(cityCacheStr)){
		// parseCityCache(cityCacheStr);
		// return;
		// }
		// String url = HttpUtil.addBaseGetParams(UrlConst.GET_CITY_CACHE);
		String url = HttpUtil.addBaseGetParams("");
		LogUtil.d(TAG, "cacheCity=" + url);
		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						// SimpleInfoParser parser = new SimpleInfoParser();
						// JsonResult<String> result = (JsonResult<String>)
						// parser
						// .parse(response);
						// if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
						// SharedPrefUtil.getInstance().setCityCache(response);
						// parseCityCache(response);
						// }
					}
				});
	}

	public static void cacheCategory() {
		String url = UrlConst.MARKET_CATEGORY_URL;
		OkHttpClientManager.getAsyn(url,
				new OkHttpClientManager.ResultCallback<String>() {

					@Override
					public void onError(Request request, Exception e) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "error e=" + e.getMessage());
					}

					@Override
					public void onResponse(String response) {
						// TODO Auto-generated method stub
						LogUtil.d(TAG, "response=" + response);
						SimpleInfoParser parser = new SimpleInfoParser();
						JsonResult<String> result = (JsonResult<String>) parser
								.parse(response);
						if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
							SharedPrefUtil.getInstance().setCategoryCache(
									response);
							parseCategoryCache(response);
						}
					}
				});
	}

	public static void initAppData(Context context) {
		// Util.cacheCity();
		// Util.cacheTag();
//		SnsUtil.getInstance(context).initSns();
		Util.cacheRegion(context);
		Util.cachePrice(context);
		Util.cacheFilter(context);
		Util.cacheAge(context);
		Util.cacheScope(context);
//		Util.cacheCategory();
	}

	public static List<BaseCardEntity> regionList = null;
	public static Map<String, List<BaseCardEntity>> regionSecondList = null;

	public static void cacheRegion(Context context) {
		// String[]
		// region_ids=context.getResources().getStringArray(R.array.region_ids);
		// String[]
		// region_name=context.getResources().getStringArray(R.array.region_name);
		// if(region_ids!=null&&region_name!=null&&region_ids.length==region_name.length){
		// for(int i=0;i<region_ids.length;i++){
		// SingleItemCardEntity entity=new SingleItemCardEntity();
		// entity.setId(region_ids[i]);
		// entity.setDesc(region_name[i]);
		// regionList.add(entity);
		// }
		// }
		String regionStr = getFromAssets(context, "region.txt");
		if (regionStr == null) {
			return;
		} else {
			parseRegion(regionStr);
		}
	}

	public static void parseRegion(String regionJson) {
		try {
			JSONObject jsonObj = new JSONObject(regionJson);
			if (jsonObj != null) {
				JSONArray dataArray = jsonObj.optJSONArray("district");

				if (dataArray != null) {
					regionList = new ArrayList<BaseCardEntity>();
					regionSecondList = new HashMap<String, List<BaseCardEntity>>();
					for (int i = 0; i < dataArray.length(); i++) {// 解析p1

						JSONObject jsonP1 = dataArray.optJSONObject(i);
						SingleItemCardEntity p1 = new SingleItemCardEntity();
						p1.setId(jsonP1.optString("district_id"));
						p1.setDesc(jsonP1.optString("name"));
						p1.setParent(true);
						if(i==0){
							p1.setSelected(true);
						}
						regionList.add(p1);
						JSONArray jsonP2Array = jsonP1.optJSONArray("list");
						if (jsonP2Array != null) {// 解析p2

							List<BaseCardEntity> tmpList = new ArrayList<BaseCardEntity>();
							for (int j = 0; j < jsonP2Array.length(); j++) {
								JSONObject jsonP2 = jsonP2Array
										.optJSONObject(j);
								SingleItemCardEntity p2 = new SingleItemCardEntity();
								p2.setId(jsonP2.optString("id"));
								p2.setDesc(jsonP2.optString("name"));
								tmpList.add(p2);
							}
							regionSecondList.put(p1.getId(), tmpList);
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static List<BaseCardEntity> priceList = new ArrayList<BaseCardEntity>();

	public static void cachePrice(Context context) {
		priceList.clear();
		String[] price = context.getResources().getStringArray(
				R.array.price_filter);
		if (price != null) {
			for (int i = 0; i < price.length; i++) {
				SingleItemCardEntity entity = new SingleItemCardEntity();
				entity.setId(String.valueOf(i+1));
				entity.setDesc(price[i]);
				priceList.add(entity);
			}
		}
	}
	public static List<BaseCardEntity> filterList = new ArrayList<BaseCardEntity>();
	public static List<BaseCardEntity> ageList = new ArrayList<BaseCardEntity>();
	
	public static void cacheAge(Context context) {
		ageList.clear();
		String[] filter = context.getResources().getStringArray(
				R.array.age_filter2);
		if (filter != null) {
			for (int i = 0; i < filter.length; i++) {
				SingleItemCardEntity entity = new SingleItemCardEntity();
				entity.setId(String.valueOf(i));
				entity.setDesc(filter[i]);
				ageList.add(entity);
			}
		}
	}
	public static void cacheFilter(Context context) {
		filterList.clear();
		String[] filter = context.getResources().getStringArray(
				R.array.filter);
		if (filter != null) {
			for (int i = 0; i < filter.length; i++) {
				SingleItemCardEntity entity = new SingleItemCardEntity();
				entity.setId(String.valueOf(i+1));
				entity.setDesc(filter[i]);
				filterList.add(entity);
			}
		}
	}

	public static List<BaseCardEntity> scopeList = new ArrayList<BaseCardEntity>();

	public static void cacheScope(Context context) {
		scopeList.clear();
		String[] price = context.getResources().getStringArray(
				R.array.scope_filter);
		if (price != null) {
			for (int i = 0; i < price.length; i++) {
				SingleItemCardEntity entity = new SingleItemCardEntity();
				if(i==0){
					entity.setId("1");
				}else{
					entity.setId("2");
				}
				
				entity.setDesc(price[i]);
				scopeList.add(entity);
			}
		}
	}

	public static String getFromAssets(Context context, String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(context
					.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void location(Context context,
			AMapLocationListener listener) {
		// 初始化定位
		AMapLocationClient mLocationClient = new AMapLocationClient(context);
		
		if(listener==null){
			Log.e("AmapError", "listener is null");
			// 设置定位回调监听
			mLocationClient.setLocationListener(new AMapLocationListener() {

				@Override
				public void onLocationChanged(AMapLocation amapLocation) {
					// TODO Auto-generated method stub
					if (amapLocation != null) {
						if (amapLocation.getErrorCode() == 0) {
							// 定位成功回调信息，设置相关消息
							amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
							amapLocation.getLatitude();// 获取纬度
							amapLocation.getLongitude();// 获取经度
							// amapLocation.getAccuracy();//获取精度信息
							// SimpleDateFormat df = new
							// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							// Date date = new Date(amapLocation.getTime());
							// df.format(date);//定位时间
							// amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
							// amapLocation.getCountry();//国家信息
							// amapLocation.getProvince();//省信息
							// amapLocation.getCity();//城市信息
							amapLocation.getDistrict();// 城区信息
							// amapLocation.getStreet();//街道信息
							// amapLocation.getStreetNum();//街道门牌号信息
							// amapLocation.getCityCode();//城市编码
							// amapLocation.getAdCode();//地区编码
							SharedPrefUtil.getInstance().setLon(
									String.valueOf(amapLocation.getLongitude()));
							SharedPrefUtil.getInstance().setLat(
									String.valueOf(amapLocation.getLatitude()));
							if (TextUtil.isNotNull(amapLocation.getDistrict())) {
								SharedPrefUtil.getInstance().setLocation(
										amapLocation.getDistrict());
							} else {
								SharedPrefUtil.getInstance().setLocation("北京");
							}
							Log.e("AmapError", "lon:"
									+ amapLocation.getLongitude() + ", lat:"
									+ amapLocation.getLatitude()+"district:"+amapLocation.getDistrict());
						} else {
							// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
							Log.e("AmapError", "location Error, ErrCode:"
									+ amapLocation.getErrorCode() + ", errInfo:"
									+ amapLocation.getErrorInfo());
						}
					}
				}
			});
		}else{
			Log.e("AmapError", "listener is no null");
			mLocationClient.setLocationListener(listener);
		}
		
		// 声明mLocationOption对象
		AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
		// 初始化定位参数
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(true);
		
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(60*1000);
		// 给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		// 启动定位
		mLocationClient.startLocation();
	}
}
