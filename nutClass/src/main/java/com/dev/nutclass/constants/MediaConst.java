package com.dev.nutclass.constants;

import com.dev.nutclass.ApplicationConfig;

import android.os.Environment;


public interface MediaConst {
	public static final String EXTERNAL_STORAGE = Environment
			.getExternalStorageDirectory().toString();
	public static final String PATH_APP = EXTERNAL_STORAGE + "/"
			+ ApplicationConfig.getInstance().getPackageName();
 
	//图片缓存
	public static final String PATH_IMG_CACHE = PATH_APP + "/.ImgCache/";
	//音频缓存
	public static final String PATH_SOUND_CACHE = PATH_APP + "/.SoundCache/";
	//图片保存
	public static final String PATH_IMG_SAVE = PATH_APP + "/image/";
	
	/**
	 * 用于相册，相机 裁图
	 * */
	public static final String KEY_SCALE = "scale";
	public static final float SCALE_SCALE_ICON = 1.0f;
	public static final float SCALE_DEFAULT = 250.0f/454;
	
	public static final String IMAGE_TYPE = "image/*";
	
	public static final String KEY_PATH = "path";
	public static final String KEY_LIST = "list";
 
}
