package com.dev.nutclass.image.crop;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.MediaConst;
import com.dev.nutclass.utils.BitmapUtil;
import com.dev.nutclass.utils.FileUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.StorageUtil;

/**
 * 裁剪界面
 * 
 */
public class CropImageActivity extends Activity implements OnClickListener {

	private static final String TAG = "CropImageActivity";
	private Activity mActivity;
	private CropImageView mImageView;
	private Bitmap mBitmap;

	private CropImage mCrop;

	private Button mSave;
	private Button mCancel, rotateLeft, rotateRight;
	private String mPath = "";
	public int screenWidth = 0;
	public int screenHeight = 0;
	private float scale;

	private ProgressBar mProgressBar;

	public static final int SHOW_PROGRESS = 2000;

	public static final int REMOVE_PROGRESS = 2001;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case SHOW_PROGRESS:
				mProgressBar.setVisibility(View.VISIBLE);
				break;
			case REMOVE_PROGRESS:
				mHandler.removeMessages(SHOW_PROGRESS);
				mProgressBar.setVisibility(View.INVISIBLE);
				break;
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop_image);
		mActivity = this;
		init();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mBitmap != null) {
			mBitmap = null;
		}
	}

	private void init() {
		getWindowWH();
		mPath = getIntent().getStringExtra(MediaConst.KEY_PATH);
		scale = getIntent().getFloatExtra(MediaConst.KEY_SCALE,
				MediaConst.SCALE_DEFAULT);
		LogUtil.i(TAG, "得到的图片的路径是 = " + mPath);
		mImageView = (CropImageView) findViewById(R.id.gl_modify_avatar_image);
		mSave = (Button) this.findViewById(R.id.gl_modify_avatar_save);
		mCancel = (Button) this.findViewById(R.id.gl_modify_avatar_cancel);
		rotateLeft = (Button) this
				.findViewById(R.id.gl_modify_avatar_rotate_left);
		rotateRight = (Button) this
				.findViewById(R.id.gl_modify_avatar_rotate_right);
		mSave.setOnClickListener(this);
		mCancel.setOnClickListener(this);
		rotateLeft.setOnClickListener(this);
		rotateRight.setOnClickListener(this);

		try {
			Bitmap cameraBitmap = createBitmap(mPath, screenWidth, screenHeight);
			;
			if (cameraBitmap == null) {
				Toast.makeText(mActivity, "没有找到图片", Toast.LENGTH_SHORT).show();
				finish();
				return;
			}
			/**
			 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
			 */
			File localFile = new File(mPath);
			int degree = FileUtil
					.readPictureDegree(localFile.getAbsolutePath());
			/**
			 * 把图片旋转为正的方向
			 */
			mBitmap = BitmapUtil.rotateBitmap(degree, cameraBitmap);
			if (mBitmap == null) {
				Toast.makeText(mActivity, "解析解析失败", Toast.LENGTH_SHORT).show();
				finish();
				return;
			} else {
				resetImageView(mBitmap);
			}
		} catch (Exception e) {
			Toast.makeText(mActivity, "没有找到图片", Toast.LENGTH_SHORT).show();
			finish();
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		addProgressbar();
	}

	/**
	 * 获取屏幕的高和宽
	 */
	private void getWindowWH() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	private void resetImageView(Bitmap b) {
		mImageView.clear();
		mImageView.setImageBitmap(b);
		mImageView.setImageBitmapResetBase(b, true);
		mCrop = new CropImage(this, mImageView, mHandler, scale);
		mCrop.crop(b);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gl_modify_avatar_cancel:
			// mCrop.cropCancel();
			finish();
			break;
		case R.id.gl_modify_avatar_save:
			String fileName = "crop_" + System.currentTimeMillis() + ".jpg";
			File cameraFile = new File(StorageUtil.getDirByType(
					CropImageActivity.this, StorageUtil.TYPE_IMG_SAVE_DIR),
					fileName);
			String filePath = cameraFile.getAbsolutePath();

			// 保存截取后图片
			FileUtil.saveToFile(mCrop.cropAndSave(), filePath);

			if (mPath != null && mPath.contains("camera_")) {
				// 删除新照需要裁切的照片,避免删除相册中照片
				FileUtil.deleteFile(mPath);
			}

			LogUtil.i(TAG, "截取后图片的路径是 = " + filePath);
			Intent intent = new Intent();
			intent.putExtra(MediaConst.KEY_PATH, filePath);
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.gl_modify_avatar_rotate_left:
			mCrop.startRotate(270.f);
			break;
		case R.id.gl_modify_avatar_rotate_right:
			mCrop.startRotate(90.f);
			break;

		}
	}

	protected void addProgressbar() {
		mProgressBar = new ProgressBar(this);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.CENTER;
		addContentView(mProgressBar, params);
		mProgressBar.setVisibility(View.INVISIBLE);
	}

	public Bitmap createBitmap(String path, int w, int h) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		try {
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			opts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			opts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			opts.outHeight = destHeight;
			opts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, opts);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
			LogUtil.e(TAG, "decodeFile OutOfMemoryError!", e);
			// 图片再放缩一次解析
			try {
				opts.inSampleSize = 2 * opts.inSampleSize;
				opts.inJustDecodeBounds = false;
				return BitmapFactory.decodeFile(path, opts);
			} catch (Exception e1) {
				// TODO: handle exception
				e1.printStackTrace();
			} catch (OutOfMemoryError e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}

		}
		return null;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mPath != null && mPath.contains("camera_")) {
			// 删除新照需要裁切的照片,避免删除相册中照片
			FileUtil.deleteFile(mPath);
		}
		super.onDestroy();
	}

}