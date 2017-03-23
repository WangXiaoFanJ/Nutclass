package com.dev.nutclass.image.control;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.MediaConst;
import com.dev.nutclass.utils.StorageUtil;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.IoUtils.CopyListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImgConfig {
	public static void initImgConfig(Context context) {
		File cacheDir = new File(StorageUtil.getDirByType(context,
				StorageUtil.TYPE_IMG_CACHE_DIR));
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(720, 1280)
				// max width, max height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				// You can pass your own memory cache
				// 将保存的时候的URI名称用MD5 加密
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 缓存的文件数量
				.diskCache(new UnlimitedDiskCache(cacheDir))
				// 自定义缓存路径
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		ImageLoader.getInstance().init(config);
	}

	// 人物头像的加载
	public static DisplayImageOptions getPortraitOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_default_new) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.icon_default_new) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.icon_default_new) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();
		return options;
	}

	// 大图的加载
	public static DisplayImageOptions getBigImgOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_default_new) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.icon_default_new) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.icon_default_new) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.displayer(new RoundedBitmapDisplayer(20)) // 设置显示风格这里是圆角矩形
				.build();
		return options;
	}

	// 相册的加载
	public static DisplayImageOptions getAlbumImgOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_default_new) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.icon_default_new) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.icon_default_new) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();
		return options;
	}

	// Card图片的加载
	public static DisplayImageOptions getCardImgOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_default_new) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.icon_default_new) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.icon_default_new) // 加载失败时的图片
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				
				.build();
		return options;
	}

	// AdItem图片的加载
	public static DisplayImageOptions getAdItemOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.showImageOnLoading(R.drawable.icon_default_new) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.icon_default_new) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.icon_default_new) // 加载失败时的图片
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();
		return options;
	}

	// Feed图片的加载
	public static DisplayImageOptions getFeedOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true) // 启用内存缓存
				.cacheOnDisk(true) // 启用外存缓存
				.showImageOnLoading(R.drawable.icon_default_new) // 加载图片时的图片
				.showImageForEmptyUri(R.drawable.icon_default_new) // 没有图片资源时的默认图片
				.showImageOnFail(R.drawable.icon_default_new) // 加载失败时的图片
				.considerExifParams(true) // 启用EXIF和JPEG图像格式
				.build();
		return options;
	}
}
