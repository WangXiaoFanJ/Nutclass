package com.dev.nutclass.utils;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.zip.CRC32;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.MediaConst;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 存储卡管理类
 * 
 * @author MaXingliang
 * 
 */
public class StorageUtil {
	private static final String TAG = "StorageUtil";

	public static final int TYPE_IMG_CACHE_DIR = 100;
	public static final int TYPE_IMG_SAVE_DIR = 101;
	public static final int TYPE_SOUND_CACHE_DIR = 200;

	/**
	 * 
	 * */
	public static final float SCALE_SCALE_ICON = 1.0f;
	public static final float SCALE_DEFAULT = 250.0f / 454;

	public static final String IMAGE_TYPE = "image/*";

	/**
	 * 图片缓存的清理参数
	 */
	public static int IMAGE_CACHE_LIMIT = 500;
	public static int IMAGE_CACHE_CLEAR_LIMIT = 200;
	public static long IMAGE_CACHE_EXPIRE = 1L;

	private StorageUtil() {
	}

	public static String getExternalStorage() {
		return Environment.getExternalStorageDirectory().toString();
	}

	public static String getDirByType(Context context, int type) {
		String dir = "/";
		String filePath = "";
		switch (type) {
		case TYPE_IMG_CACHE_DIR:
			filePath = MediaConst.PATH_IMG_CACHE;
			break;
		case TYPE_SOUND_CACHE_DIR:
			filePath = MediaConst.PATH_SOUND_CACHE;
			break;
		case TYPE_IMG_SAVE_DIR:
			filePath = MediaConst.PATH_IMG_SAVE;
		default:
			break;
		}

		File file = new File(filePath);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		if (file.exists()) {
			if (file.isDirectory()) {
				dir = file.getPath();
			}
		} else {
			dir="";
		}
		return dir;
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
	 * 返回内部可用储存空间和SDCard可用储存空间
	 */
	public static long getStorage(String path) {
		if (TextUtils.isEmpty(path)) {
			return -1;
		}
		long blockSize = 0;
		long availableBlocks = 0;
		long storageSpaceSize = 0;

		try {
			StatFs stat = new StatFs(path);
			blockSize = stat.getBlockSize();
			availableBlocks = stat.getAvailableBlocks();
			storageSpaceSize = availableBlocks * blockSize;
		} catch (Exception e) {
			LogUtil.d(TAG, "获取内存空间：" + e.toString());
			storageSpaceSize = -1;
		}

		return storageSpaceSize;
	}

	// 检查应用安装空间
	public static boolean isInstallStorage(long fileSize, boolean isSdCard) {
		if (isSdCard) {
			return (fileSize * 1.2) <= getStorage(Environment
					.getDataDirectory().getPath());
		} else {
			return isSDCardExist()
					&& (fileSize * 1.2) <= getStorage(Environment
							.getExternalStorageDirectory().getPath());
		}
	}

	/**
	 * 判断存储空间是否足够
	 * 
	 * @param needSize
	 * @return
	 */
	public static boolean checkExternalSpace(Context context, float needSize) {
		boolean flag = false;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long availCount = sf.getAvailableBlocks();
			long restSize = availCount * blockSize;
			if (restSize > needSize) {
				flag = true;
			} else {
				Toast.makeText(context, "SDCard空间不足", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(context, "请插入SDCard", Toast.LENGTH_SHORT).show();
		}
		return flag;
	}

	/**
	 * 根据URL获取图片在文件系统中的文件名
	 * 
	 * @param url
	 * @return
	 */
	public static String getTempFileName(String url) {
		String tempFileName = null;
		if (null != url && !"".equals(url.trim())) {
			tempFileName = url.replace('/', '_').replace(':', '_')
					.replace('?', '_').replace(".png", "").replace(".jpg", "")
					.replace(".webp", "");
		}
		return tempFileName;
	}

	/**
	 * 根据图片清理参数，清理图片缓存
	 */
	public static void clearImgFileCache(Context context) {
		if (!isSDCardExist()) {
			return;
		}
		if (SharedPrefUtil.getInstance().needImgCacheClear()) {
			final String imgDir = getDirByType(context, TYPE_IMG_CACHE_DIR);
			new Thread() {
				public void run() {
					File imgDirFile = new File(imgDir);
					excuteClear(imgDirFile);
				}
			}.start();
			SharedPrefUtil.getInstance().saveImgCacheClearTime();
		}
	}

	/**
	 * 清理图片缓存
	 */
	private static void excuteClear(File dirFile) {
		File[] arrayOfFile = null;
		long now = 0L;
		int fileLength = 0;
		if ((dirFile.exists()) && (dirFile.isDirectory())) {
			now = new Date().getTime();
			arrayOfFile = dirFile.listFiles();
			if (arrayOfFile != null)
				fileLength = arrayOfFile.length;
		}
		// 如果缓存图片的数量小于图片缓存上限
		if (fileLength < IMAGE_CACHE_LIMIT) {
			return;
		}

		// 对文件按时间排序
		Arrays.sort(arrayOfFile, new FileTimeCompartor());

		int clearNum = 0;
		int needClearTotal = fileLength - IMAGE_CACHE_CLEAR_LIMIT;
		for (int i = 0; i < fileLength; i++) {
			File paramFile = arrayOfFile[i];
			// 如果缓存图片的时间为IMAGE_CACHE_EXPIRE以内，即最经常使用的
			if ((now - paramFile.lastModified()) / 3600000L < IMAGE_CACHE_EXPIRE) {
				break;
			}
			if ((paramFile.exists()) && (paramFile.isFile())) {
				paramFile.delete();
				clearNum++;
			}
			// 如果缓存图片的数量已经小于图片缓存上限，不再清理
			if (clearNum >= needClearTotal) {
				break;
			}
		}
		LogUtil.d("all files =" + fileLength + ",need delete ="
				+ needClearTotal + ", delete num =" + clearNum);
	}

	private static class FileTimeCompartor implements Comparator<File> {
		@Override
		public int compare(File object1, File object2) {
			if (object1.lastModified() - object2.lastModified() < 0) {
				return -1;
			} else if (object1.lastModified() - object2.lastModified() > 0) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	/**
	 * 照片存储
	 * 
	 * */
	/** SD卡三种状 */
	public static enum MountStatuds {
		SD_CARD_AVAILABLE, SD_CARD_NOT_AVAILABLE, SD_CARD_SPACE_NOT_ENOUGH
	}
	/** 预设SD卡空间 (单位M) */
	public static final long CACHE_SIZE = 100;
	public static final int MB = 1024 * 1024;
	/** 默认为可用状 */
	public static MountStatuds SDCardStatus = MountStatuds.SD_CARD_AVAILABLE;
	public static final String SDCARD_PATH = ("Android" + File.separator
			+ "data" + File.separator).intern();
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
	public static String getCameraFilePath(Context context) {
		String fileName = "camera_" + System.currentTimeMillis() + ".jpg";
		File cameraFile = new File(getCamaraDir(context), fileName);
		return cameraFile.getAbsolutePath();
	}

	public static String getCameraFileName() {
		String fileName = "camera_" + System.currentTimeMillis() + ".jpg";
		return fileName;
	}
	public static String getCamaraDir(Context context) {
		File f = new File(getCacheDir(context), "camera".intern());
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
	private static final String PHOTO_URL_CRC = "http://ww%d.sinaimg.cn/%s/%s.%s";
	private static final String PHOTO_URL_CRC_BACKUP = "http://wb%d.sina.cn/%s/%s.%s";
	private static final String PHOTO_URL = "http://ss%d.sinaimg.cn/%s/%s&690";
	private static final String HEAD_PIC_URL = "http://tp%d.sinaimg.cn/%d/%d/%s";

	public static final String PIC_TYPE_THUMB = "thumbnail";
	public static final String PIC_TYPE_ORIGNAL = "orignal";
	public static final String PIC_TYPE_LARGE = "large";
	public static String getPid2Url(String pid){
		return getPid2Url(pid,PIC_TYPE_THUMB);
	}
	public static String getPid2Url(String pid,String picType){
		String url = PHOTO_URL_CRC;
		String result="";
		if(TextUtils.isEmpty(pid)||pid.equals("null")){
			return null;
		}
		if(pid.charAt(9)=='w'){
			if(picType==PIC_TYPE_ORIGNAL){
				picType=PIC_TYPE_LARGE;
			}
			CRC32 crc32 = new CRC32();
			crc32.update(pid.getBytes());
			long hv=crc32.getValue();
			long zone=hv%4+1;
			String ext=pid.charAt(21)=='g'?"gif":"jpg";
			result=String.format(url,zone,picType,pid,ext);
			return result;
		}else{
			int num=(Integer.parseInt(pid.substring(pid.length()-2),16))%16+1;
			return String.format(PHOTO_URL, num,picType,pid);
		}
		 
	}

}
