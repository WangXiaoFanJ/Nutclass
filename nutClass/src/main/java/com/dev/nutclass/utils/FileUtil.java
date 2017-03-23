package com.dev.nutclass.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;




import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

public class FileUtil {
	private static final String TAG = "FileUtil";
	//删除文件夹
	public static int deleteDirectory(File dirFile) {
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		int size = 0;
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return 0;
		}
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				size += files[i].length();
				files[i].delete();
			} // 删除子目录
			else {
				deleteDirectory(files[i]);
			}
		}
		// 删除当前目录
		dirFile.delete();
		return size;
	}
	

	public static void tranverseDirectory(File dirFile, ArrayList<File> fileArray) {
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return;
		}
		if (dirFile.isDirectory()) {
			File[] files = dirFile.listFiles();
			for(int i = 0; i < files.length; i++) {
				if (files[i].isFile()) {
					fileArray.add(files[i]);
				} else {
					tranverseDirectory(files[i], fileArray);
				}
			}
		}
		
	}
	
	//计算文件或者文件夹的总大小
	public static long getFileSize(File f) {
		//如果是文件
		if (f.isFile()) {
			return  f.length();	
		}
		//如果是文件夹
		long size = 0;
		File flist[] = f.listFiles();
		if (flist == null) {
			return 0;
		}
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}
	//构建文件的绝对路径
//	public static  String getFilePath(String filename){
//		  return ImageConst.PATH_SAVE+filename;
//    };
   
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LogUtil.i(TAG, "degree="+degree);
		return degree;
	}
	//删除文件
	public static boolean deleteFile(String filePath) {
		if (!TextUtils.isEmpty(filePath)) {
			File f = new File(filePath);
			if (f.exists()) {
				return f.delete();
			}	
		}
		return false;
	}
	//保存文件
	public static boolean saveToFile(Bitmap bm,String filePath) {
		
		return saveToFile(bm,100,filePath);
	}
	/**
	 * 保存文件
	 * @param bm 
	 * @param quality  压缩比例
	 * @param filePath
	 * @return
	 */
	public static boolean saveToFile(Bitmap bm,int quality,String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			bm.compress(CompressFormat.JPEG, quality, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	//拷贝文件
	public static void copyfile(File fromFile, File toFile,Boolean rewrite) throws IOException {
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }
        try {
            FileInputStream fosfrom = new FileInputStream(fromFile);
            FileOutputStream fosto = new FileOutputStream(toFile,true);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
        } catch (Exception ex) {
            LogUtil.e(TAG, "read file error:"+ex.getMessage());   
        }
    }
	/**
	 * 格式化文件大小，精确到一位小数，最小单位是M
	 * 
	 * @param context
	 * @param number
	 * @return
	 */
	public static String formatSize(long number) {
		float result = number * 1.0f / (1024.0f * 1024.0f);
		String suffix = "M";

		if (result > 900) {
			suffix = "G";
			result = result / 1024;
		}
		String value;
		if (result < 1) {
			if (result < 0.1) {// 不足0.1M时，补足0.1M
				result = 0.1f;
			}
			value = String.format("%.1f", result);
		} else if (result < 10) {
			value = String.format("%.1f", result);
		} else if (result < 100) {
			value = String.format("%.1f", result);
		} else {
			value = String.format("%.1f", result);
		}
		return value + suffix;
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

		File cameraFile = new File(StorageUtil.getCamaraDir(act), outFileName);
		Uri outputUri = Uri.fromFile(cameraFile);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		act.startActivityForResult(intent, resultCode);
		return cameraFile.getAbsolutePath();
	}

	
}
