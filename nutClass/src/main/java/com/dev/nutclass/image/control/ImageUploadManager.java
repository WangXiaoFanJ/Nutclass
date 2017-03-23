package com.dev.nutclass.image.control;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.dev.nutclass.utils.CommonUtil;

public class ImageUploadManager {

	private static final String TAG = "ImageUploadManager";

	public static final int IMAGE_ABLUM = 1;
	public static final int IMAGE_ICON = 2;
	public static final int AUDIO_SOUND = 3;
	private boolean isRunning = false;
	private Context context;
	private static ImageUploadManager instance;

	public interface ImageUploadWatcher {
		public void onUploadStart(String localPath, int from);

		public void onUploadCancel(String localPath, int from);

		public void onUploadFinished(String netPath, int from);

		public void onUploadFailure(String netPath, int from);
	}

 

	private ImageUploadManager(Context context) {
		this.context = context;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public static synchronized ImageUploadManager getInstance(Context context) {
		if (instance == null) {
			instance = new ImageUploadManager(context);
		}
		return instance;
	}

	public static final String IMAGE_TYPE = "image/*";

	/**
	 * 构建相册intent
	 * 
	 * */
	public static Intent getPhotoIntent() {

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(IMAGE_TYPE);
		intent.putExtra("return-data", false);
		return intent;
	}

	/**
	 * 构建调用系统相机 intent，同时写好文件的路径，TODO sdcard的检测
	 * 
	 * */
	public static Intent getCameraIntent() {
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra("return-data", false);
		return intent;
	}

	public static String getCameraFilePath(Context context) {
		String fileName = "camera_" + System.currentTimeMillis() + ".jpg";
		File cameraFile = new File(CommonUtil.getCamaraDir(context), fileName);
		return cameraFile.getAbsolutePath();
	}

	public static String getCameraFileName() {
		String fileName = "camera_" + System.currentTimeMillis() + ".jpg";
		return fileName;
	}

	/**
	 * 启动相机
	 * 
	 * @param request
	 * @return
	 */
	public static String startImageCapture(Activity act, int resultCode,
			String outFileName) {
		// 统计启动相机的次数
		long maxM = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		long nowM = android.os.Debug.getNativeHeapAllocatedSize() / 1024 / 1024;
		if (maxM - nowM < 2) {
			Toast.makeText(act, "内存不足,请释放部分内存再试", Toast.LENGTH_SHORT).show();
			return "";
		}

		File cameraFile = new File(CommonUtil.getCamaraDir(act), outFileName);
		Uri outputUri = Uri.fromFile(cameraFile);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		act.startActivityForResult(intent, resultCode);
		return cameraFile.getAbsolutePath();
	}

	/**
	 * 启动系统相册
	 * 
	 * @param resultCode
	 */
	public static void gotoSysPic(Activity act, int resultCode) {
		long maxM = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		long nowM = android.os.Debug.getNativeHeapAllocatedSize() / 1024 / 1024;
		if (maxM - nowM < 2) {
			Toast.makeText(act, "内存不足,请释放部分内存再试", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		intent.putExtra("return-data", false);
		act.startActivityForResult(intent, resultCode);
	}

}
