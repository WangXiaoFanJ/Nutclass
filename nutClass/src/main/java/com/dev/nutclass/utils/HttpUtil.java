package com.dev.nutclass.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Proxy;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


public class HttpUtil {

	private static final int TIME_OUT_CONNECTION = 30000;
	private static final int TIME_OUT_SOCKET = 30000;

	public static String inputStream2String(InputStream in) throws IOException {
		if (in == null)
			return "";

		final int size = 128;
		byte[] buffer = new byte[size];

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int cnt = 0;
		while ((cnt = in.read(buffer)) > -1) {
			baos.write(buffer, 0, cnt);
		}
		baos.flush();

		in.close();
		baos.close();

		return baos.toString();
	}

	/**
	 * 获取网络状态
	 * 
	 * @return 网络状态：State.*
	 */
	public static State getConnectionState(Context context) {
		ConnectivityManager sManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = sManager.getActiveNetworkInfo();
		if (info != null) {
			return info.getState();
		}
		return State.UNKNOWN;
	}

	/**
	 * 网络连接是否已连接好
	 * 
	 * @return
	 */
	public static boolean isConnected(Context context) {
		return State.CONNECTED.equals(getConnectionState(context));
	}

 

	public enum NetworkState {
		NOTHING, MOBILE, WIFI
	}

	public static NetworkState getNetworkState(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return NetworkState.NOTHING;
		} else {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				return NetworkState.MOBILE;
			} else {
				return NetworkState.WIFI;
			}
		}
	}

	public static HttpURLConnection getHttpUrlConnection(URL url,
			Context context) throws ProtocolException, IOException {
		// lyang add
		HttpURLConnection httpConnection;
		if (isWapNet(context)) {// wap 网络
			String tempUrl = url.toString();
			int offset = tempUrl.startsWith("https") ? 8 : 7;
			if (offset == 7) {// http开头的
				int contentBeginIdx = tempUrl.indexOf('/', offset);
				StringBuffer urlStringBuffer = new StringBuffer(
						"http://10.0.0.172");
				urlStringBuffer.append(tempUrl.substring(contentBeginIdx));
				URL urltemp = new URL(urlStringBuffer.toString());
				httpConnection = (HttpURLConnection) urltemp.openConnection();
				httpConnection.setRequestProperty("X-Online-Host",
						tempUrl.substring(offset, contentBeginIdx));
				// Log.e("net ", "wap");
			} else {// wap 网络 访问https
				httpConnection = (HttpURLConnection) url.openConnection();
			}
		} else {
			String[] hostAndPort = getProxyHostAndPort(context);
			String host = hostAndPort[0];
			int port = Integer.parseInt(hostAndPort[1]);

			if (host != null && host.length() != 0 && port != -1) {// 电信wap
																	// 普通移动net网络
				InetSocketAddress isa = new InetSocketAddress(host, port);
				java.net.Proxy proxy = new java.net.Proxy(
						java.net.Proxy.Type.HTTP, isa);
				httpConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {// wifi 网络
				httpConnection = (HttpURLConnection) url.openConnection();
			}
		}

		httpConnection.setDoInput(true);
		httpConnection.setConnectTimeout(30000);
		httpConnection.setReadTimeout(30000);
		httpConnection.setRequestProperty("Accept", "*, */*");
		httpConnection.setRequestProperty("accept-charset", "utf-8");
		httpConnection.setRequestMethod("GET");
		return httpConnection;
	}

	public static String[] getProxyHostAndPort(Context context) {
		if (getNetworkState(context) == NetworkState.WIFI) {
			return new String[] { "", "-1" };
		} else {
			return new String[] { Proxy.getDefaultHost(),
					"" + Proxy.getDefaultPort() };
		}
	}

	public static boolean isWapNet(Context context) {

		String currentAPN = "";
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return false;
		}
		if (info.getType() == ConnectivityManager.TYPE_WIFI) {
			return false;
		}
		currentAPN = info.getExtraInfo();
		if (currentAPN == null || currentAPN.equals("")) {
			return false;
		} else {
			if (currentAPN.equals("cmwap") || currentAPN.equals("uniwap")
					|| currentAPN.equals("3gwap")) {

				return true;
			} else {
				return false;
			}

		}

	}

	public static class APNWrapper {
		public String name;
		public String apn;
		public String proxy;
		public int port;

		public String getApn() {
			return apn;
		}

		public String getName() {
			return name;
		}

		public int getPort() {
			return port;
		}

		public String getProxy() {
			return proxy;
		}

		APNWrapper() {
		}

		public String toString() {
			return "{name=" + name + ";apn=" + apn + ";proxy=" + proxy
					+ ";port=" + port + "}";
		}
	}

	public static APNWrapper getAPN(Context ctx) {
		TelephonyManager telManager = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telManager.getSimOperator();
		// String phoneSystem = getPhoneSystem();
		APNWrapper wrapper = new APNWrapper();
		Cursor cursor = null;
		try {
			cursor = ctx.getContentResolver().query(
					Uri.parse("content://telephony/carriers/preferapn"),
					new String[] { "name", "apn", "proxy", "port" }, null,
					null, null);
		} catch (Exception e) {
			// 为了解决在4.2系统上禁止非系统进程获取apn相关信息，会抛出安全异常
			// java.lang.SecurityException: No permission to write APN settings
		}
		if (cursor != null) {
			cursor.moveToFirst();
			if (cursor.isAfterLast()) {
				wrapper.name = "N/A";
				wrapper.apn = "N/A";
			} else {
				wrapper.name = cursor.getString(0) == null ? "" : cursor
						.getString(0).trim();
				wrapper.apn = cursor.getString(1) == null ? "" : cursor
						.getString(1).trim();
			}
			cursor.close();
		} else {
			wrapper.name = "N/A";
			wrapper.apn = "N/A";
		}
		wrapper.proxy = android.net.Proxy.getDefaultHost();
		wrapper.proxy = TextUtils.isEmpty(wrapper.proxy) ? "" : wrapper.proxy;
		wrapper.port = android.net.Proxy.getDefaultPort();
		wrapper.port = wrapper.port > 0 ? wrapper.port : 80;
		return wrapper;
	}

	 
	public static String addBaseGetParams(String url) {
		StringBuffer sb=new StringBuffer(url);
//		sb.append("&token="+SharedPrefUtil.getInstance().getToken());
		return sb.toString();
	}
	public static String addToken(String url){
		StringBuffer sb=new StringBuffer(url);
		sb.append("&userId="+SharedPrefUtil.getInstance().getUid()+"&token="+SharedPrefUtil.getInstance().getToken());
		sb.append("&longitude="+SharedPrefUtil.getInstance().getLon()+"&latitude="+SharedPrefUtil.getInstance().getLat());
		return sb.toString();
	}
	

}
