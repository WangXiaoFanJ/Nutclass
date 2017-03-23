package com.dev.nutclass.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.util.Log;

/**
 * 日志工具类
 * 
 * @author Mr.Huang
 * 
 */
public class LogUtil {

	public static int LOG_D = 1;
	public static int LOG_I = 2;
	public static int LOG_W = 3;
	public static int LOG_E = 4;
	public static int LOG_V = 5;

	private static String LOG_TAG = "LogUtilDef";
	/**
	 * log 开关，false：关闭log。
	 */
	private static boolean DEBUG = true;//log打印
	private static boolean WRITE_FILE = false;//写日志文件

	public static void d(String tag, String msg) {
		log(tag, msg, LOG_D);
	}

	public static void d(String msg) {
		log(LOG_TAG, msg, LOG_D);
	}

	public static void i(String tag, String msg) {
		log(tag, msg, LOG_I);
	}

	public static void i(String msg) {
		log(LOG_TAG, msg, LOG_I);
	}

	public static void w(String tag, String msg, Throwable tr) {
		log(tag, msg, tr, LOG_W);
	}

	public static void w(Throwable tr) {
		log(LOG_TAG, "", tr, LOG_W);
	}

	public static void w(String msg, Throwable tr) {
		log(LOG_TAG, msg, tr, LOG_W);
	}

	public static void w(String tag, String msg) {
		log(tag, msg, LOG_W);
	}

	public static void e(String tag, String msg, Throwable tr) {
		log(tag, msg, tr, LOG_E);
	}

	public static void e(String tag, String msg) {
		log(tag, msg, LOG_E);
	}

	public static void v(Throwable tr) {
		log(LOG_TAG, "", tr, LOG_V);
	}
	public static void v(String tag, String msg, Throwable tr) {
		log(tag, msg, tr, LOG_V);
	}

	public static void v(String tag, String msg) {
		log(tag, msg, LOG_V);
	}

	public static void e(Throwable tr) {
		log(LOG_TAG, "", tr, LOG_E);
	}

	private static void log(String tag, String msg, int logType) {
		// 如果需要关闭log，直接返回
		if (!DEBUG) {
			return;
		}
		if (msg == null) {
			msg = "null";
		}
		if (LOG_D == logType) {
			Log.d(tag, msg);
		} else if (LOG_I == logType) {
			Log.i(tag, msg);
		} else if (LOG_W == logType) {
			Log.w(tag, msg);
		} else if (LOG_E == logType) {
			Log.e(tag, msg);
		} else if (LOG_V == logType) {
			Log.v(tag, msg);
		} else {
			Log.d(tag, msg);
		}
	}

	private static void log(String tag, String msg, Throwable tr, int logType) {
		// 如果需要关闭log，直接返回
		if (!WRITE_FILE) {
			return;
		}
		log(tag, msg + '\n' + getStackTraceString(tr), logType);
	}

	private static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		tr.printStackTrace(pw);
		return sw.toString();
	}
}
