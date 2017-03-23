
package com.dev.nutclass.view;

import java.util.ArrayList;
import java.util.List;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.view.pager.BigImgView;
import com.dev.nutclass.view.pager.BigPicPagerAdapter;
import com.dev.nutclass.view.pager.CirclePageIndicator;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class BannerCardView extends LinearLayout {
	private static final String TAG = "BannerCardView";
	private Context context;
	private CirclePageIndicator indicatorView;
	private ViewPager pager;
	private BigPicPagerAdapter adapter;
	private ArrayList<BigImgView> imgViewList;
	private int curPosition=1;
	private BannerCardEntity entity;
	private BigImgView singleImgView;
	private static final int OFFSET_LEFT=1000;
	private int imgNum =0 ;
	//type=1  首页Banner,type=2 首页京东广告  type=3 详情页banner
	private int type=0;

	private boolean isAutoScroll = true;
	private final static int SCROLL_WHAT = 0;
	private int interval = 3000;
	private final static int LEFT= 1;
	private int direction = 0;
	private boolean isCycle = true;
	private boolean isBorderAnimation = true;
	private boolean isStopByTouch = false;
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case SCROLL_WHAT:
					scrollOnce();
					sendScrollMessage(interval);
				default:
					break;
			}
		}
	}
	private   Handler handler = new MyHandler();
	public BannerCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		init(context);
	}
	public BannerCardView(Context context,int type) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.type=type;
		init(context);
	}
	

	public BannerCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}

	private void init(Context context) {
		this.context=context;

//		首页Banner  640*110
//		首页京东  640*200
//		详情页  750*320
		//活动详情Banner
//		LayoutInflater.from(context).inflate(R.layout.card_banner,this);
//		int targetW=DensityUtil.getDisplayWidth(context);
//		int width=0;
//		int height=0;
//		if(type==1){
//			width=640;
//			height=110;
//		}else if(type==2){
//			width=640;
//			height=200;
//		}else if(type==3){
//			width=750;
//			height=320;
//		}
//		
//		int targetH=targetW*height/width;
//		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(targetW,targetH);
//		setLayoutParams(params);
		
//		LayoutInflater.from(context).inflate(R.layout.card_banner,this);
		
		if(type==1){
			LayoutInflater.from(context).inflate(R.layout.card_banner_75,this);
		}else if(type==2){
			LayoutInflater.from(context).inflate(R.layout.card_banner_76,this);
		}else{
			LayoutInflater.from(context).inflate(R.layout.card_banner_77,this);
		}
		indicatorView = (CirclePageIndicator)this.findViewById(R.id.view_indicator);
		pager = (ViewPager)this.findViewById(R.id.view_pager);
		singleImgView=(BigImgView)this.findViewById(R.id.view_single);
	}

	public void updateView(BannerCardEntity entity,int type) {
		updateView(entity, 0,type);
	}

	

	private void updateView(final BannerCardEntity entity, int position, final int type
			) {
		this.entity=entity;
		final List<ImageEntity> imgList=entity.getList();
		if (imgList == null || imgList.size() <= 0) {
			return;
		}
		if(imgList.size()==1){
			singleImgView.setVisibility(View.VISIBLE);
			indicatorView.setVisibility(View.GONE);
			pager.setVisibility(View.GONE);
			singleImgView.updateView(imgList.get(0),DensityUtil.getDisplayWidth(context),
					DensityUtil.getDisplayWidth(context),false,type);
			if (entity.getCardType()==BaseCardEntity.CARD_TYPE_JD_BANNER) {
				singleImgView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent=new Intent(context,CourseListActivity.class);
						intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_JD);
						intent.putExtra(Const.KEY_ID, imgList.get(0).getId());
						intent.putExtra(Const.KEY_TITLE, imgList.get(0).getImgName());
						context.startActivity(intent);
					}
				});
			}
			
		}else{
			singleImgView.setVisibility(View.GONE);
			curPosition = position % imgList.size();
			imgViewList = new ArrayList<BigImgView>();
			if(imgList.size() == 2){
				imgList.addAll(entity.getList());
				imgNum = 2;
			}
			for (int i = 0; i <imgList.size(); i++) {
				BigImgView bigImgView = new BigImgView(context);
				bigImgView.updateView(imgList.get(i),type);
				imgViewList.add(bigImgView);
			}
			adapter = new BigPicPagerAdapter(imgViewList);
			pager.setAdapter(adapter);
			startAutoScroll();
			indicatorView.setOnPageChangeListener(new OnPageChangeListener() {

				@Override
				public void onPageSelected(int arg0) {
					LogUtil.d(TAG, "onPageSelected:"+arg0);
//					curPosition=arg0%imgViewList.size();
//					imgViewList.get(curPosition).updateView(imgList.get(curPosition),type);

//					int width=imgViewList.get(curPosition).getWidth();
//					int height=imgViewList.get(curPosition).getHeight();
//					RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(width,height);
//					params.addRule(RelativeLayout.CENTER_IN_PARENT); 
//					pager.setLayoutParams(params);
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
				if(imgNum == 2){
					indicatorView.setViewPagerNew(pager,imgList.size()*OFFSET_LEFT+curPosition,imgNum);
				}
//			setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
					// TODO Auto-generated method stub
//					if (listner != null) {
//						listner.onImgClick(imgList, curPosition);
//					}
//					ImageEntity imgEntity=imgList.get(curPosition);
//					if(imgEntity==null||TextUtils.isEmpty(imgEntity.getSchema())){
//						return;
//					}
//					Uri uri=Uri.parse(imgEntity.getSchema());
//					Intent intent=new Intent();
//					intent.setPackage(ApplicationConfig.getInstance().getPackageName());
//					intent.setData(uri);
//					context.startActivity(intent);
					
//					ImageEntity imgEntity=imgList.get(curPosition);
//					if(imgEntity==null||TextUtils.isEmpty(imgEntity.getH5Url())){
//						return;
//					}
//					Intent intent=new Intent(context,WebViewActivity.class);
//					intent.putExtra(Const.KEY_URL, imgEntity.getH5Url());
//					intent.putExtra(Const.KEY_TITLE, imgEntity.getImgName());
//					context.startActivity(intent);
//				}
//			});
		}
		
	}

	//viewPager自动轮播代码
	public void startAutoScroll() {
		isAutoScroll = true;
		sendScrollMessage(interval);
		pager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				{
					if (event.getAction() == MotionEvent.ACTION_MOVE && isAutoScroll) {
						isStopByTouch = true;
						stopAutoScroll();
					} else if (event.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
						startAutoScroll();
					}
					pager.getParent().requestDisallowInterceptTouchEvent(true);
					return onTouchEvent(event);

				}
			}
		});
	}
	private void sendScrollMessage(long delayTimeInMills) {
		/** remove messages before, keeps one message is running at most **/
		handler.removeMessages(SCROLL_WHAT);
		handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
	}
	public void scrollOnce() {
		int currentItem = pager.getCurrentItem();
		int totalCount;
		if (adapter == null || (totalCount = adapter.getCount()) <= 1) {
			return;
		}

		int nextItem = (direction == LEFT) ? --currentItem : ++currentItem;
		if (nextItem < 0) {
			if (isCycle) {
				pager.setCurrentItem(totalCount - 1, isBorderAnimation);
			}
		} else if (nextItem == totalCount) {
			if (isCycle) {
				pager.setCurrentItem(0, isBorderAnimation);
			}
		} else {
			pager.setCurrentItem(nextItem, true);
		}
	}
	public void stopAutoScroll() {
		isAutoScroll = false;
		handler.removeMessages(SCROLL_WHAT);
	}
}
