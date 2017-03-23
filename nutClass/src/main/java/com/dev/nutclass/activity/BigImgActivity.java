package com.dev.nutclass.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.MD5;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.TitleBar;
import com.dev.nutclass.view.pager.BigImgView;
import com.dev.nutclass.view.pager.BigPicPagerAdapter;
import com.dev.nutclass.view.pager.CirclePageIndicator;

/**
 * 用于大图的预览 1、浏览模式下，可以保存图片 2、编辑模式下，可以删除图片。如果删除最后一张图，则关闭页面。
 * */
public class BigImgActivity extends BaseActivity {

	private static final String TAG = "BigPicActivity";
	private Handler handler=new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			pager.setCurrentItem(msg.arg1);
		}
		
	};
	private static final int OFFSET_LEFT=1000;
	public final static int TYPE_MODE_EDIT = 1;
	public final static int TYPE_MODE_SCAN = 2;
	private Activity mActivity;
	private CirclePageIndicator indicatorView;
	private ViewPager pager;
	private BigPicPagerAdapter adapter;
	private TitleBar titleBar;
	private ArrayList<ImageEntity> imgList;
	private ArrayList<BigImgView> imgViewList;
	private int position = 0;// 默认位置
	private int mode = TYPE_MODE_SCAN;
	private boolean isAuto=false;
	private int curPosition=1;
	private BigImgView singleImgView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big_pic);
		mActivity = this;
		initIntent();
		initView();
		initData();
		initListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initIntent() {
		Intent intent = getIntent();
		position = intent.getIntExtra(Const.KEY_POSITION, 0);
		imgList = (ArrayList<ImageEntity>) intent
				.getSerializableExtra(Const.KEY_ENTITY_LIST);
		mode = intent.getIntExtra(Const.KEY_MODE, TYPE_MODE_SCAN);
	}

	private void initView() {
		titleBar = (TitleBar) findViewById(R.id.tb_title);
		indicatorView = (CirclePageIndicator) findViewById(R.id.view_indicator);
		pager = (ViewPager) findViewById(R.id.view_pager);
		singleImgView=(BigImgView)this.findViewById(R.id.view_single);
		setOnGestureBackEnable(false);

	}

	private void initData() {
		if (imgList == null || imgList.size() <= 0) {
			DialogUtils.showToast(mActivity, R.string.tip_activity_data_error);
			finish();
			return;
		}
		if (imgList == null || imgList.size() <= 0) {
			return;
		}
		if(imgList.size()==1){
			singleImgView.setVisibility(View.VISIBLE);
			indicatorView.setVisibility(View.GONE);
			pager.setVisibility(View.GONE);
			singleImgView.updateView(imgList.get(0),3);
		}else{
			singleImgView.setVisibility(View.GONE);
			curPosition = position % imgList.size();
			imgViewList = new ArrayList<BigImgView>();
			for (int i = 0; i < imgList.size(); i++) {
				imgViewList.add(new BigImgView(BigImgActivity.this));
			}
			adapter = new BigPicPagerAdapter(imgViewList);
			pager.setAdapter(adapter);
			indicatorView.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					LogUtil.d(TAG, "onPageSelected:"+arg0);
					curPosition=arg0%imgViewList.size();
					 
					imgViewList.get(curPosition).updateView(imgList.get(curPosition),0,0,true,1);
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
					LogUtil.d(TAG, "onPageScrollStateChanged");

				}
			});
			indicatorView.setViewPager(pager,imgList.size()*OFFSET_LEFT+curPosition);
		}

	}

	private void initListener() {
		
		titleBar.setBarClickListener(new TitleBar.BarClickListener() {

			@Override
			public boolean onClickRight2() {
				// TODO Auto-generated method stub
				// 依据模式判断保存还是删除
				return true;
			}

			@Override
			public boolean onClickRight1() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onClickLeft() {
				// TODO Auto-generated method stub
				finish();
				return false;
			}

			@Override
			public boolean onClickMiddle() {
				// TODO Auto-generated method stub
				return false;
			}
		});
	}
	
	private class PagerTransformAnim implements ViewPager.PageTransformer{
		private static final float MIN_SCALE = 0.75f;  
		@Override
		public void transformPage(View view, float position) {
			// TODO Auto-generated method stub
			 int pageWidth = view.getWidth();  
		        if (position < -1) { // [-Infinity,-1)  
		                                // This page is way off-screen to the left.  
		            view.setAlpha(0);  
		        } else if (position <= 0) { // [-1,0]  
		                                    // Use the default slide transition when  
		                                    // moving to the left page  
		            view.setAlpha(1);  
		            view.setTranslationX(0);  
		            view.setScaleX(1);  
		            view.setScaleY(1);  
		        } else if (position <= 1) { // (0,1]  
		                                    // Fade the page out.  
		            view.setAlpha(1 - position);  
		            // Counteract the default slide transition  
		            view.setTranslationX(pageWidth * -position);  
		            // Scale the page down (between MIN_SCALE and 1)  
		            float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)  
		                    * (1 - Math.abs(position));  
		            view.setScaleX(scaleFactor);  
		            view.setScaleY(scaleFactor);  
		        } else { // (1,+Infinity]  
		                    // This page is way off-screen to the right.  
		            view.setAlpha(0);  
		  
		        }  
		}
	}

 
 

	/**
	 * 请求删除图片。用于本地处理
	 * */
	public void deleteImg() {

	}

	/**
	 * 将照片保存到本地
	 * */
	public void saveImg() {

	}

	@Override
	protected void handleTitleBarEvent(int eventId) {
		// TODO Auto-generated method stub

	}

}