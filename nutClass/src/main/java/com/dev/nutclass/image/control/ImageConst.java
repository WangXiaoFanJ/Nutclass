package com.dev.nutclass.image.control;

import android.os.Environment;

import com.dev.nutclass.ApplicationConfig;
public interface ImageConst {
	/**
	 * 用于相册，相机 裁图
	 * */
	public static final String KEY_SCALE = "scale";
	public static final float SCALE_SCALE_ICON = 1.0f;
	public static final float SCALE_DEFAULT = 250.0f/454;
	
	public static final String IMAGE_TYPE = "image/*";
	
	public static final String KEY_PATH = "path";
	public static final String KEY_LIST = "list";
	
	// 图片类型
	public static final int TYPE_GIF = 1;
	public static final int TYPE_IMAGE = 0;
	public static final String EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().toString();
	//ImageCache目录
	public static final String PATH_APP = EXTERNAL_STORAGE+"/"+ApplicationConfig.getInstance().getPackageName();
	public static final String PATH_CACHE = ApplicationConfig.getInstance().getCacheDir();
	public static final String PATH_SAVE = PATH_APP + "/Save/";
	


}
