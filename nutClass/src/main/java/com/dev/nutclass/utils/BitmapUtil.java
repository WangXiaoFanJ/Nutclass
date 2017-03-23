package com.dev.nutclass.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.text.TextUtils;

public class BitmapUtil {
	private final static String TAG = "BitmapUtil";

	public BitmapUtil() {
	}

	/**
	 * 返回资源图片的bitmap
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap decodeResource(Context context, int resId) {
		try {
			Bitmap bitmap = null;
			Resources r;
			if (context == null) {
				r = Resources.getSystem();
			} else {
				r = context.getResources();
			}
			bitmap = BitmapFactory.decodeResource(r, resId);
			return bitmap;
		} catch (Exception e) {
			LogUtil.w(e);
		} catch (OutOfMemoryError error) {
			// 实在要出现内存不足的问题，清空缓存，调GC，没办法
			LogUtil.w(error);
			System.gc();
		}
		return null;
	}

	/**
	 * 模糊效果
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImage(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int newColor = 0;

		int[][] colors = new int[9][3];
		for (int i = 1, length = width - 1; i < length; i++) {
			for (int k = 1, len = height - 1; k < len; k++) {
				for (int m = 0; m < 9; m++) {
					int s = 0;
					int p = 0;
					switch (m) {
					case 0:
						s = i - 1;
						p = k - 1;
						break;
					case 1:
						s = i;
						p = k - 1;
						break;
					case 2:
						s = i + 1;
						p = k - 1;
						break;
					case 3:
						s = i + 1;
						p = k;
						break;
					case 4:
						s = i + 1;
						p = k + 1;
						break;
					case 5:
						s = i;
						p = k + 1;
						break;
					case 6:
						s = i - 1;
						p = k + 1;
						break;
					case 7:
						s = i - 1;
						p = k;
						break;
					case 8:
						s = i;
						p = k;
					}
					pixColor = bmp.getPixel(s, p);
					colors[m][0] = Color.red(pixColor);
					colors[m][1] = Color.green(pixColor);
					colors[m][2] = Color.blue(pixColor);
				}

				for (int m = 0; m < 9; m++) {
					newR += colors[m][0];
					newG += colors[m][1];
					newB += colors[m][2];
				}

				newR = (int) (newR / 9F);
				newG = (int) (newG / 9F);
				newB = (int) (newB / 9F);

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				newColor = Color.argb(255, newR, newG, newB);
				bitmap.setPixel(i, k, newColor);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		return bitmap;
	}

	/**
	 * 柔化效果(高斯模糊)(优化后比上面快三倍)
	 * 
	 * @param bmp
	 * @return
	 */
	public static Bitmap blurImageAmeliorate(Bitmap bmp) {
		long start = System.currentTimeMillis();
		// 高斯矩阵
		int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };

		int width = bmp.getWidth();
		int height = bmp.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);

		int pixR = 0;
		int pixG = 0;
		int pixB = 0;

		int pixColor = 0;

		int newR = 0;
		int newG = 0;
		int newB = 0;

		int delta = 16; // 值越小图片会越亮，越大则越暗

		int idx = 0;
		int[] pixels = new int[width * height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);
		for (int i = 1, length = height - 1; i < length; i++) {
			for (int k = 1, len = width - 1; k < len; k++) {
				idx = 0;
				for (int m = -1; m <= 1; m++) {
					for (int n = -1; n <= 1; n++) {
						pixColor = pixels[(i + m) * width + k + n];
						pixR = Color.red(pixColor);
						pixG = Color.green(pixColor);
						pixB = Color.blue(pixColor);

						newR = newR + (int) (pixR * gauss[idx]);
						newG = newG + (int) (pixG * gauss[idx]);
						newB = newB + (int) (pixB * gauss[idx]);
						idx++;
					}
				}

				newR /= delta;
				newG /= delta;
				newB /= delta;

				newR = Math.min(255, Math.max(0, newR));
				newG = Math.min(255, Math.max(0, newG));
				newB = Math.min(255, Math.max(0, newB));

				pixels[i * width + k] = Color.argb(255, newR, newG, newB);

				newR = 0;
				newG = 0;
				newB = 0;
			}
		}

		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		long end = System.currentTimeMillis();
		LogUtil.d(TAG, "used time=" + (end - start));
		return bitmap;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
		if (bitmap == null) {
			return bitmap;
		}
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		;
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = null;
		try {
			resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			if (bitmap != null && !bitmap.equals(resizedBitmap)
					&& !bitmap.isRecycled()) {
				bitmap.recycle();
				bitmap = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		return resizedBitmap;
	}

	/**
	 * bitmap 图片的大小
	 * 
	 * @param bitmap
	 * @return
	 */
	@SuppressLint("NewApi")
	public static long getBitmapSize(Bitmap bitmap) {
		if (bitmap == null) {
			return 0;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();

	}

	/**
	 * 返回resize之后的bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getResizePicFile(String filePath, int width, int height) {
		if (TextUtils.isEmpty(filePath)) {
			return "";
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 构建文件上传的文件名
		File newFile = new File(filePath + "_newfile_"
				+ System.currentTimeMillis() + ".jpg");
		FileOutputStream out = null;
		Bitmap bitmap = null;
		int quality = 100;
		try {
			// 1 测量大小
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			if (options.mCancel || options.outWidth == -1
					|| options.outHeight == -1) {
				return null;
			}
			// 2 输出bitmap
			// options.inSampleSize = computeSampleSize(options, -1, width
			// * height);
			options.inSampleSize = calculateInSampleSize(options, width, height);
			// LogUtil.i("tanqian", " imsamplesize = " + options.inSampleSize +
			// " width = " + width + " height = " + height +" filePath " +
			// filePath);
			options.inJustDecodeBounds = false;
			// 使用RGB_565，每个像素使用2个字节，节省一半内存空间
			options.inPreferredConfig = Bitmap.Config.RGB_565;

			try {
				bitmap = BitmapFactory.decodeFile(filePath, options);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OutOfMemoryError e) {
				// TODO Auto-generated catch block
				LogUtil.e(TAG, "decodeFile OutOfMemoryError!", e);
				// 图片再放缩一次解析
				options.inSampleSize = 2 * options.inSampleSize;
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeFile(filePath, options);
			}

			if (bitmap != null) {
				out = new FileOutputStream(newFile);
				long size = getBitmapSize(bitmap);
				LogUtil.i(TAG, "bitmap size = " + size);
				if (size > 300 * 1024) {
					quality = 50;
				} else if (size > 100 * 1024) {
					quality = 70;
				}
				LogUtil.i(TAG, "compress quality=" + quality);
				bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
				out.flush();
			}

			return newFile.getAbsolutePath();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bitmap != null && !bitmap.isRecycled()) {
					bitmap.recycle();
					bitmap = null;
				}
				out.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return newFile.getAbsolutePath();
	}

	/**
	 * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
	 * object when decoding bitmaps using the decode* methods from
	 * {@link BitmapFactory}. This implementation calculates the closest
	 * inSampleSize that will result in the final decoded bitmap having a width
	 * and height equal to or larger than the requested width and height. This
	 * implementation does not ensure a power of 2 is returned for inSampleSize
	 * which can be faster when decoding but results in a larger bitmap which
	 * isn't as useful for caching purposes.
	 * 
	 * @param options
	 *            An options object with out* params already populated (run
	 *            through a decode* method with inJustDecodeBounds==true
	 * @param reqWidth
	 *            The requested width of the resulting bitmap
	 * @param reqHeight
	 *            The requested height of the resulting bitmap
	 * @return The value to be used for inSampleSize
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (reqWidth == 0 || reqHeight == 0) {
			return inSampleSize;
		}

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	/**
	 * 根据View(主要是ImageView)的宽和高来获取图片的缩略图
	 * 
	 * @param path
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
	public static Bitmap decodeThumbBitmapForFile(String filePath,
			int viewWidth, int viewHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 设置为true,表示解析Bitmap对象，该对象不占内存
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// 设置缩放比例
		options.inSampleSize = calculateInSampleSize(options, viewWidth,
				viewHeight);

		// 设置为false,解析Bitmap对象加入到内存中
		options.inJustDecodeBounds = false;

		Bitmap bitmap = null;

		try {
			bitmap = BitmapFactory.decodeFile(filePath, options);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			LogUtil.e(TAG, "decodeFile OutOfMemoryError!", e);
			// 图片再放缩一次解析
			options.inSampleSize = 2 * options.inSampleSize;
			options.inJustDecodeBounds = false;

			try {
				bitmap = BitmapFactory.decodeFile(filePath, options);
			} catch (Exception e2) {
				// TODO: handle exception
			} catch (OutOfMemoryError e3) {

			}
		}
		return bitmap;
	}

	/**
	 * 返回resize之后的bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getResizeBitmap(byte[] bytes, int width, int height) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		try {
			Bitmap bitmap = null;
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 1 测量大小
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
			if (options.mCancel || options.outWidth == -1
					|| options.outHeight == -1) {
				return null;
			}

			// 2 输出bitmap
			options.inSampleSize = computeSampleSize(options, -1, width
					* height);
			options.inJustDecodeBounds = false;
			// 使用RGB_565，每个像素使用2个字节，节省一半内存空间
			options.inPreferredConfig = Bitmap.Config.RGB_565;

			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
					options);
			return bitmap;

		} catch (Exception e) {
			LogUtil.w(e);
		} catch (OutOfMemoryError error) {
			// 实在要出现内存不足的问题，清空缓存，调GC，没办法
			LogUtil.w(error);
			System.gc();
		}
		return null;
	}

	public static int[] getBitmapInfo(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		// 1 测量大小
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int[] info = new int[2];
		info[0] = options.outWidth;
		info[1] = options.outHeight;
		return info;
	}

	public static Bitmap getSquareBitmap(Context context, String filePath,
			int reqWidth) {
		Bitmap bitmap = getResizeBitmap(filePath,
				DensityUtil.getDisplayWidth(context),
				DensityUtil.getDisplayHeight(context));
		int size = bitmap.getWidth() > bitmap.getHeight() ? bitmap.getHeight()
				: bitmap.getWidth();
		float scale = reqWidth*1.0f / size;
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		int x = bitmap.getWidth() > bitmap.getHeight() ? (bitmap.getWidth() - size) / 2
				: 0;
		int y = bitmap.getWidth() > bitmap.getHeight() ? 0 : (bitmap
				.getHeight() - size) / 2;
		LogUtil.d(TAG, "scale="+scale+",size="+size);
		bitmap = Bitmap.createBitmap(bitmap, x, y, size, size, matrix, false);
		return bitmap;
	}

	/**
	 * 返回resize之后的bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getResizeBitmap(String filePath, int width, int height) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		try {
			Bitmap bitmap = null;
			BitmapFactory.Options options = new BitmapFactory.Options();
			// 1 测量大小
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			if (options.mCancel || options.outWidth == -1
					|| options.outHeight == -1) {
				return null;
			}
			// 2 输出bitmap
			options.inSampleSize = computeSampleSize(options, -1, width
					* height);
			options.inJustDecodeBounds = false;
			// 使用RGB_565，每个像素使用2个字节，节省一半内存空间
			options.inPreferredConfig = Bitmap.Config.RGB_565;

			bitmap = BitmapFactory.decodeFile(filePath, options);
			return bitmap;

		} catch (Exception e) {
			LogUtil.w(e);
		} catch (OutOfMemoryError error) {
			// 实在要出现内存不足的问题，清空缓存，调GC，没办法
			LogUtil.w(error);
			System.gc();
		}
		return null;
	}

	private static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 16) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize += 1;
			}
			if (roundedSize != 1) {
				roundedSize = roundedSize - 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
}
