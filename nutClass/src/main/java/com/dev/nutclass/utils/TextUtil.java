package com.dev.nutclass.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class TextUtil {
	public static boolean isNotNull(String text) {
		return (!TextUtils.isEmpty(text) && !"null".equalsIgnoreCase(text));
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param full
	 *            需要显示的所有字符串
	 * @param highLight
	 *            需要高亮的字符串
	 * @param colerId需要高亮显示的颜色资源id
	 * @return
	 */
	public static SpannableString highLightText(Context context, String full,
			String highLight, int colerId) {
		if (TextUtils.isEmpty(full)) {
			return new SpannableString("");
		}
		SpannableString ss = new SpannableString(full);
		if (TextUtils.isEmpty(highLight)
				|| (!TextUtils.isEmpty(highLight) && !full.contains(highLight))) {
			return ss;
		}
		int start = full.indexOf(highLight);
		int length = highLight.length();
		int color = context.getResources().getColor(colerId);
		ss.setSpan(new ForegroundColorSpan(color), start, start + length,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	public static SpannableString highLightText(Context context,
			SpannableString ss, String full, String highLight, int colerId,
			boolean last) {

		if (TextUtils.isEmpty(highLight) || (!TextUtils.isEmpty(highLight))) {
			return ss;
		}
		int start;
		if (last) {
			start = full.lastIndexOf(highLight);
		} else {
			start = full.indexOf(highLight);
		}

		int length = highLight.length();
		int color = context.getResources().getColor(colerId);
		ss.setSpan(new ForegroundColorSpan(color), start, start + length,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param full
	 *            需要显示的所有字符串
	 * @param highLight
	 *            需要高亮的字符串数组
	 * @param colerId需要高亮显示的颜色资源id
	 * @return
	 */
	public static SpannableString highLightText(Context context, String full,
			String[] highLights, int colerId) {
		if (TextUtils.isEmpty(full)) {
			return new SpannableString("");
		}
		SpannableString ss = new SpannableString(full);
		int start = 0;
		int length = 0;
		for (String highLight : highLights) {
			if (TextUtils.isEmpty(highLight) || !full.contains(highLight)) {
				continue;
			}
			start = full.indexOf(highLight, start + length);
			length = highLight.length();
			int color = context.getResources().getColor(colerId);
			ss.setSpan(new ForegroundColorSpan(color), start, start + length,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		return ss;
	}

	public static SpannableString highLightText(Context context, String full,
			String[] highLights, int[] colerIds) {
		if (TextUtils.isEmpty(full)) {
			return new SpannableString("");
		}
		SpannableString ss = new SpannableString(full);
		int start = 0;
		int length = 0;
		if (highLights.length != colerIds.length) {
			return ss;
		}
		int index = 0;
		for (String highLight : highLights) {
			if (TextUtils.isEmpty(highLight) || !full.contains(highLight)) {
				continue;
			}
			start = full.indexOf(highLight, start + length);
			length = highLight.length();
			int color = context.getResources().getColor(colerIds[index]);
			ss.setSpan(new ForegroundColorSpan(color), start, start + length,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			index++;
		}

		return ss;
	}

	// 左补齐
	public static String leftPad(String str, String pad, int len) {
		String newStr = (str == null ? "" : str);
		while (newStr.length() < len) {
			newStr = pad + newStr;
		}
		if (newStr.length() > len) {
			newStr = newStr.substring(newStr.length() - len);
		}
		return newStr;
	}

	// 右补齐
	public static String rightPad(String str, String pad, int len) {
		String newStr = (str == null ? "" : str);
		while (newStr.length() < len) {
			newStr = newStr + pad;
		}
		if (newStr.length() > len) {
			newStr = newStr.substring(0, len);
		}
		return newStr;
	}

	/* 这是一个静态函数，不用声明对象就可以用的，如你的类名为Test,在任何情况下都可以调用Test.isHave函数 */
	public static boolean isContains(String[] strs, String s) {
		if (strs == null || s == null) {
			return false;// 返回false
		}
		/*
		 * 此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串
		 */
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].indexOf(s) != -1) {// 循环查找字符串数组中的每个字符串中是否包含所有查找的内容
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;// 没找到返回false
	}

	/**
	 * 数字处理
	 */
	public static String numberFormat(int num) {
		if (num < 99) {
			return num + "";
		} else {
			num = num / 10000;
			return num + "+";
		}
	}
	public static String getCurDate(){
		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date =new Date();
		String str=formate.format(date);
		return str;
	}
	/**
	 * 将时间戳转为代表"距现在多久之前"的字符串
	 * 
	 * @param timeStr
	 *            时间戳
	 * @return
	 */
	public static String getStandardDate(String timeStr) {
		if (TextUtils.isEmpty(timeStr)) {
			return "";
		}

//		StringBuffer sb = new StringBuffer();
//
//		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date dd = null;
//		try {
//			dd = formate.parse(timeStr);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if (dd == null) {
//			return "";
//		}
//		long time = System.currentTimeMillis() - dd.getTime();
//		long mill = (long) Math.ceil(time / 1000);// 秒前
//
//		long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前
//
//		long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时
//
//		long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前
//
//		if (day - 1 > 0) {
//			sb.append(day + "天");
//		} else if (hour - 1 > 0) {
//			if (hour >= 24) {
//				sb.append("1天");
//			} else {
//				sb.append(hour + "小时");
//			}
//		} else if (minute - 1 > 0) {
//			if (minute == 60) {
//				sb.append("1小时");
//			} else {
//				sb.append(minute + "分钟");
//			}
//		} else if (mill - 1 > 0) {
//			if (mill == 60) {
//				sb.append("1分钟");
//			} else {
//				sb.append(mill + "秒");
//			}
//		} else {
//			sb.append("刚刚");
//		}
//		if (!sb.toString().equals("刚刚")) {
//			sb.append("前");
//		}
//		return sb.toString();
		return timeStr;
	}
}
