package com.dev.nutclass.utils;

import java.io.File;
import java.io.Serializable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CommonUtil {

	/** SD卡三种状 */
	public static enum MountStatuds {
		SD_CARD_AVAILABLE, SD_CARD_NOT_AVAILABLE, SD_CARD_SPACE_NOT_ENOUGH
	}

	/** 预设SD卡空间 (单位M) */
	public static final long CACHE_SIZE = 100;
	public static final int MB = 1024 * 1024;
	public static final String SDCARD_PATH = ("Android" + File.separator
			+ "data" + File.separator).intern();

	/** 默认为可用状 */
	public static MountStatuds SDCardStatus = MountStatuds.SD_CARD_AVAILABLE;

	/** root 路径 */
	public static String getRootPath(Context context) {
		StringBuilder sb = new StringBuilder();

		SDCardStatus = getSDCardStatus();
		switch (SDCardStatus) {
		case SD_CARD_AVAILABLE:
		case SD_CARD_SPACE_NOT_ENOUGH:
			sb.append(Environment.getExternalStorageDirectory().getPath())
					.append(File.separator).append(SDCARD_PATH)
					.append(context.getPackageName());
			break;
		case SD_CARD_NOT_AVAILABLE:
			sb.append(context.getCacheDir().getPath());
			break;
		}
		return sb.toString();
	}

	public static String getBasePath(Context context) {
		StringBuilder sb = new StringBuilder();
		SDCardStatus = getSDCardStatus();

		switch (SDCardStatus) {
		case SD_CARD_AVAILABLE:
		case SD_CARD_SPACE_NOT_ENOUGH:
			sb.append(Environment.getExternalStorageDirectory().getPath());
			break;
		case SD_CARD_NOT_AVAILABLE:
			sb.append(context.getCacheDir().getPath());
			break;
		}
		return sb.toString();
	}

	public static String getImageCacheDir(Context context) {
		File f = new File(getRootPath(context), "image".intern());
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getPath();
	}

	public static String getCacheDir(Context context) {
		File f = new File(getRootPath(context), "cache".intern());
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getPath();
	}

	public static String getCamaraDir(Context context) {
		File f = new File(getCacheDir(context), "camera".intern());
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getPath();
	}

	public static String getSplashDir(Context context) {
		File f = new File(getRootPath(context), "splash".intern());
		if (!f.exists()) {
			f.mkdirs();
		}
		return f.getPath();
	}

	public static MountStatuds getSDCardStatus() {
		MountStatuds status;
		String sdState = Environment.getExternalStorageState();
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long availCount = sf.getAvailableBlocks();
			long blockSize = sf.getBlockSize();
			long availSize = availCount * blockSize / MB;
			/** 100M内存空间大小 */
			if (availSize < CACHE_SIZE) {
				/** TODO 是否提示用户空间不够 */
				status = MountStatuds.SD_CARD_SPACE_NOT_ENOUGH;
			} else {
				status = MountStatuds.SD_CARD_AVAILABLE;
			}
		} else {
			status = MountStatuds.SD_CARD_NOT_AVAILABLE;
		}
		return status;
	}

	public static synchronized void startActivity(Context context,
			Class<?> clazz) {
		Intent intent = new Intent(context, clazz);
		context.startActivity(intent);
	}

	public static synchronized void startActivity(Context context,
			Class<?> clazz, String key, Parcelable parcelable) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(key, parcelable);
		context.startActivity(intent);
	}

	public static synchronized void startActivity(Context context,
			Class<?> clazz, String key, Serializable serializable) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(key, serializable);
		context.startActivity(intent);
	}

	public static synchronized void startActivity(Context context, String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		context.startActivity(intent);
	}

	public static synchronized void startActivity(Context context,
			Class<?> clazz, String key, Bundle bundle) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra(key, bundle);
		context.startActivity(intent);
	}

	/**
	 * 调用系统分享
	 * 
	 * @param mContext
	 * @param title
	 * @param text
	 */
	public static void systemShare(Context mContext, String title, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(Intent.createChooser(intent, "分享"));
	}

	/**
	 * 隐藏软键盘
	 */
	public static void hideSoftInputFromWindow(Context context, View view) {
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.RESULT_UNCHANGED_SHOWN);
		}
	}

	/**
	 * 根据activity当前获取焦点的控件隐藏软键盘
	 */
	public static void hideSoftInputFromActivity(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View view = activity.getCurrentFocus();
		if (view != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.RESULT_UNCHANGED_SHOWN);
		}
	}

	/**
	 * 显示软键盘
	 */
	public static void showSoftInput(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}

	/**
	 * 判断是否为空
	 */
	public static boolean isEmpty(String content) {
		return content != null && "".equals(content);
	}

	public static final int KB = 1024;

	public static String getFileSize(long size) {
		StringBuilder sb = new StringBuilder();
		int s = (int) (size / KB);

		if (s == 0) {
			sb.append(1).append("KB");
		} else {
			sb.append(s).append("KB");
		}

		return sb.toString();
	}

	/**
	 * 退出. 其它 待添加
	 */
	public static void exit(Activity activity) {
		// 自杀
		try {
			android.os.Process.killProcess(android.os.Process.myPid());
		} catch (Throwable e) {
			activity.finish();
		}
	}

	public static void putPreferences(Context context, String name, String key,
			Object value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				name, Context.MODE_PRIVATE);
		putPreferences(sharedPreferences, key, value);
	}

	/**
	 * 复制文本到系统剪切板
	 */
	public static void copy2ClipboardManager(Context context, String text) {
		ClipboardManager clipboard = (ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(text);
	}

	public static void putPreferences(SharedPreferences sharedPreferences,
			String key, Object value) {
		Editor editor = sharedPreferences.edit();
		if (value.getClass() == String.class) {
			editor.putString(key, (String) value);
		}
		if (value.getClass() == Integer.class || value.getClass() == int.class) {
			editor.putInt(key, (Integer) value);
		}
		if (value.getClass() == Long.class || value.getClass() == long.class) {
			editor.putLong(key, (Long) value);
		}
		if (value.getClass() == Boolean.class
				|| value.getClass() == boolean.class) {
			editor.putBoolean(key, (Boolean) value);
		}
		if (value.getClass() == Float.class || value.getClass() == float.class) {
			editor.putFloat(key, (Float) value);
		}
		editor.commit();
	}

	public static void setText(TextView tv, String str, String defaultStr) {
		if (TextUtils.isEmpty(str)) {
			tv.setText(defaultStr);
		} else {
			tv.setText(str);
		}
	}

	public static void setText(TextView tv, String str, int defaultStr) {
		setText(tv, str, tv.getContext().getString(defaultStr));
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @author paulburke
	 */
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

}
