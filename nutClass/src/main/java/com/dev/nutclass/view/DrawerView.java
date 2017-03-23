package com.dev.nutclass.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.utils.BitmapUtil;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.StorageUtil;

public class DrawerView extends LinearLayout {
	private static final String TAG = "DrawerView";
	private Context context;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	public int MAX_HEIGHT = 400;// DrawerView的最大高度
	public int MIN_HEIGHT = 0;// DrawerView的把手高度
	public int handleHeight=200;
	private int rotate=90;

	private LinearLayout mContainer;
	private RelativeLayout mHandle;
	private FrameLayout mContent;
	private SquareImageView scanIv;
	private ImageView rotateIv;
	private ImageView handlerIv;

	private float mLastY = -1; // save event y
	private float downY = -1; // save event y
	
	private Scroller mScroller; // used for scroll back
	private boolean isOpen=true;
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	private Bitmap scanBitmap;

	/**
	 * @param context
	 * @param attrs
	 */
	public DrawerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		initView(context, attrs);
		initListener();
	}

	private void initView(Context context, AttributeSet attrs) {
		// 初始情况，设置下拉刷新view高度为0
		handleHeight=context.getResources().getDimensionPixelSize(R.dimen.height_drawer_handler);
		MAX_HEIGHT=DensityUtil.getDisplayWidth(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, MAX_HEIGHT+handleHeight);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.view_drawer, null);
		mContainer.setGravity(Gravity.BOTTOM);
		addView(mContainer, lp);
		mHandle = (RelativeLayout) mContainer.findViewById(R.id.handle);
		mContent = (FrameLayout) mContainer.findViewById(R.id.content);
		scanIv = (SquareImageView) mContainer.findViewById(R.id.iv_img);
		rotateIv = (ImageView) mContainer.findViewById(R.id.iv_rotate);
		handlerIv = (ImageView) mContainer.findViewById(R.id.iv_handler);
		
		setVisiableHeight(MAX_HEIGHT);
		mScroller = new Scroller(context, new DecelerateInterpolator());
	}
	private void initListener(){
		rotateIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				scanBitmap=BitmapUtil.rotateBitmap(rotate, scanBitmap);
				scanIv.setImageBitmap(scanBitmap);
			}
		});
		handlerIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isOpen) {
					close();
				} else {
					open();
				}
				LogUtil.d(TAG, "click handler");
			}
		});
		mHandle.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mLastY = event.getRawY();
					downY=event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					final float deltaY = event.getRawY() - mLastY;
					mLastY = event.getRawY();
					int height = (int) (getVisiableHeight() + deltaY);
					if (height > MAX_HEIGHT) {
						height = MAX_HEIGHT;
					} else if (height < MIN_HEIGHT) {
						height = MIN_HEIGHT;
					}
					setVisiableHeight(height);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					mLastY = -1; // reset
					if(event.getRawX()>downY){
						resetHeight(true);
					}else{
						resetHeight(false);
					}
					
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			setVisiableHeight(mScroller.getCurrY());
			postInvalidate();
		}
		super.computeScroll();
	}

	public void setVisiableHeight(int height) {
		System.out.println("height="+height);
		if (height <= MIN_HEIGHT){
			isOpen=false;
			height = MIN_HEIGHT;
		}else if (height >= MAX_HEIGHT){
			height = MAX_HEIGHT;
			isOpen=true;
		}
		LinearLayout.LayoutParams contentParams = (LinearLayout.LayoutParams) mContent
				.getLayoutParams();
		contentParams.height = height;
		mContent.setLayoutParams(contentParams);
		LinearLayout.LayoutParams containerParams = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		containerParams.height=height+handleHeight;
		mContainer.setLayoutParams(containerParams);
	}

	public int getVisiableHeight() {
		return mContent.getHeight();
	}
	public void setBitmap(String path){
		scanBitmap=BitmapUtil.getSquareBitmap(context,path, DensityUtil.getDisplayWidth(context));
		scanIv.setImageBitmap(scanBitmap);
		if(!isOpen){
			open();
		}
	}
	
	public Bitmap getBitmap(){
		return scanBitmap;
	}

	/**
	 * reset height.
	 */
	private void resetHeight(boolean isOpen) {
		int height = getVisiableHeight();
		int dis = 0;
		if (isOpen) {
			dis = MAX_HEIGHT - height;
		} else {
			dis = -height + MIN_HEIGHT;
		}
		mScroller.startScroll(0, height, 0, dis, SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	// 对外控制
	public void close() {
		mScroller.startScroll(0, MAX_HEIGHT, 0, MIN_HEIGHT - MAX_HEIGHT,
				SCROLL_DURATION);
		invalidate();
	}

	// 对外控制
	public void open() {
		mScroller.startScroll(0, MIN_HEIGHT, 0, MAX_HEIGHT, SCROLL_DURATION);
		invalidate();
	}
}
