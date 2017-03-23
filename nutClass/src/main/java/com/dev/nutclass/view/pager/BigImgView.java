package com.dev.nutclass.view.pager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.activity.NewestActionActivity;
import com.dev.nutclass.activity.NewestActionActivity02;
import com.dev.nutclass.activity.WebViewActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class BigImgView extends RelativeLayout {
	public static final String TAG = "BigImgView";
	private Context context;
	private ImageView imgView;
	// 是否正在加载
	private boolean isLoading = false;
	public BigImgView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public BigImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public BigImgView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);

	}
	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.view_pager_img, this);
		imgView = (ImageView) findViewById(R.id.iv_big_pic);

	}

	public void updateView(ImageEntity entity,int type) {
		updateView(entity, DensityUtil.getDisplayWidth(context),
				DensityUtil.getDisplayWidth(context),false,type);
	}

	public void updateView(final ImageEntity entity, final int width,
						   final int height, boolean onclick, final int type) {
//		ImageLoader.getInstance().displayImage(entity.getSmallPath(), imgView,
//				ImgConfig.getCardImgOption());
		Log.d("===","type:"+type);
			ImageLoader.getInstance().displayImage(entity.getSmallPath(), imgView,
					ImgConfig.getCardImgOption(), new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
							// TODO Auto-generated method stub
							int width ;
							int height ;
							if(entity.getCardType() == 2){
								width = Integer.parseInt(entity.getImg_width());
								height = Integer.parseInt(entity.getImg_height());
							}else{
								width=arg2.getWidth();
								height=arg2.getHeight();
							}
							int targetW=0;
							int targetH=0;
							if(width>=height){

							}else{

							}
							targetW=DensityUtil.getDisplayWidth(context);
							targetH=targetW*height/width;
							RelativeLayout.LayoutParams params=new LayoutParams(targetW,targetH);
							params.addRule(RelativeLayout.CENTER_IN_PARENT);
							imgView.setLayoutParams(params);
						}

						@Override
						public void onLoadingCancelled(String arg0, View arg1) {
							// TODO Auto-generated method stub

						}
					});

		
		if (onclick) {
			imgView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((Activity) context).finish();
				}
			});
		} else {
			setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if (entity == null || TextUtils.isEmpty(entity.getH5Url())) {
//						return;

					if (entity != null &&type==1&& entity.getH5_jump().equals("1")){
						Intent intent = new Intent(context, WebViewActivity.class);
						intent.putExtra(Const.KEY_URL, entity.getH5Url());
						Log.d("===","url:"+entity.getH5Url());
//						intent.putExtra(Const.KEY_TITLE, "dd");
						context.startActivity(intent);
					}else if (entity !=null &&type==1&&entity.getApp_jump().equals("1")){
						if(entity.getApp_jump_key().equals("brand_id")){
							Intent intent=new Intent();
							intent.setClass(context, CourseListActivity.class);
							intent.putExtra(Const.KEY_ID, entity.getApp_jump_value());
							intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BRAND);
							context.startActivity(intent);
						}else if(entity.getApp_jump_key().equals("xiaoqu_id")){
							Intent intent = new Intent();
							intent.setClass(context, CourseListActivity.class);
							intent.putExtra(Const.KEY_ID, entity.getApp_jump_value());
							intent.putExtra(Const.KEY_TYPE, CourseListActivity.TYPE_FROM_COURSE);
							context.startActivity(intent);
						}else if(entity.getApp_jump_key().equals("goods_id")){
							Intent intent = new Intent();
							intent.setClass(context, CourseInfoActivity.class);
							intent.putExtra(Const.KEY_ID, entity.getApp_jump_value());
							intent.putExtra(Const.KEY_SCHOOL_ID,0);
							context.startActivity(intent);
						}else if(entity.getApp_jump_key().equals("cuxiao_id")){
							int id = Integer.parseInt(entity.getApp_jump_value());
							Log.d("===","id:"+id);
							if(id > 0){
//								if(id==24){
									Intent intent = new Intent(context, NewestActionActivity02.class);
									intent.putExtra("id",entity.getApp_jump_value());
									context.startActivity(intent);
//								}else {
//									Intent intent = new Intent(context, NewestActionActivity.class);
//									intent.putExtra("id",entity.getApp_jump_value());
//									context.startActivity(intent);
//								}
							}
						}
//						Intent intent = new Intent(context, NewestActionActivity.class);
//						intent.putExtra("id",entity.getApp_jump_value());
//						context.startActivity(intent);
					}else{
						return;
					}
//					if (entity != null && !TextUtils.isEmpty(entity.getH5Url())){
//						Intent intent = new Intent(context, WebViewActivity.class);
//						intent.putExtra(Const.KEY_URL, entity.getH5Url());
//						intent.putExtra(Const.KEY_TITLE, entity.getImgName());
//						context.startActivity(intent);
//					}else if (entity !=null && TextUtils.isEmpty(entity.getH5Url())&&type==1&&entity.getIs_promotion()>0){
//						Intent intent = new Intent(context, NewestActionActivity.class);
//						intent.putExtra("id",entity.getIs_promotion());
//						context.startActivity(intent);
//					}else if (entity != null && TextUtils.isEmpty(entity.getH5Url())&&type==1&&entity.getIs_promotion()==0){
//						if(SharedPrefUtil.getInstance().isLogin()){
//							((HomeActivity)context).changeFragment();
//						}else{
//							Intent intent = new Intent(context, LoginTogetherActivity.class);
//							context.startActivity(intent);
//						}
//					}else{
//						return;
//					}
				}
			});
			
		}
		// if (isLoading) {
		// LogUtil.d(TAG, "isloading:" + entity.getBigPath());
		// return;
		// } else {
		// ImageLoader.getInstance().displayImage(
		// StorageUtil.getPid2Url(entity.getBigPath(),
		// StorageUtil.PIC_TYPE_LARGE), imgView,
		// ImgConfig.getBigImgOption(), new ImageLoadingListener() {
		//
		// @Override
		// public void onLoadingStarted(String arg0, View arg1) {
		// // TODO Auto-generated method stub
		// loadingPb.setVisibility(View.VISIBLE);
		// isLoading = true;
		// }
		//
		// @Override
		// public void onLoadingFailed(String arg0, View arg1,
		// FailReason arg2) {
		// // TODO Auto-generated method stub
		// loadingPb.setVisibility(View.GONE);
		// isLoading = false;
		// }
		//
		// @Override
		// public void onLoadingComplete(String arg0, View arg1,
		// Bitmap arg2) {
		// // TODO Auto-generated method stub
		// loadingPb.setVisibility(View.GONE);
		// isLoading = false;
		// // setParams(arg2, arg1, width, height);
		// }
		//
		// @Override
		// public void onLoadingCancelled(String arg0, View arg1) {
		// // TODO Auto-generated method stub
		// loadingPb.setVisibility(View.GONE);
		// isLoading = false;
		// }
		// });
		// }
	}

	/**
	 * 设置宽高，以高或者宽为基准填满屏幕
	 *
	 * @param bmp
	 * @param arg1
	 * @param widthP
	 *            父类宽度
	 * @param heigthP
	 *            父类高度
	 */
	// private void setParams(Bitmap bmp, View arg1, int widthP, int heightP) {
	// // if (imgH <= 0 || imgH <=0) return;
	//
	// final int bmpW = bmp.getWidth();
	// final int bmpH = bmp.getHeight();
	// LogUtil.i(TAG, "bmpW × bmpH=" + bmpW + " × " + bmpH);
	// if (bmpW <= 0 || bmpH <= 0)
	// return;
	//
	// final int densityW = widthP;
	// final int densityH = heightP;
	// LogUtil.i(TAG, "densityW × densityH=" + densityW + " × " + densityH);
	// if (densityW <= 0 || densityH <= 0)
	// return;
	//
	// RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) arg1
	// .getLayoutParams();
	// if (densityW * bmpH >= densityH * bmpW) {// 图片的宽高比小于于屏幕的宽高比，则以屏幕高为准缩放
	// LogUtil.i(TAG, "---use densityH---");
	// int height = densityH;
	// int width = height * bmpW / bmpH;// 定高，宽度按照比列设定
	// params.height = height;
	// params.width = width;
	// LogUtil.i(TAG, "imgW × imgH=" + width + " × " + height);
	// } else if (densityW * bmpH < densityH * bmpW) {//
	// 图片的宽高比大于屏幕的宽高比，则以屏幕宽为准缩放
	// LogUtil.i(TAG, "---use densityW---");
	// int width = densityW;// 定宽，高度按照比列设定
	// int height = width * bmpH / bmpW;
	// LogUtil.i(TAG, "imgW × imgH=" + width + " × " + height);
	// params.height = height;
	// params.width = width;
	// }
	//
	// arg1.setLayoutParams(params);
	//
	// }

}
