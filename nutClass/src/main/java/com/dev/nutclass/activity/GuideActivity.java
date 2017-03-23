package com.dev.nutclass.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.request.TaskResult;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.nostra13.universalimageloader.core.ImageLoader;



public class GuideActivity extends Activity implements OnClickListener,OnPageChangeListener {

	private final String TAG = "GuideActivity";
	private Activity mActivity;
	private ViewPager mViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	private List<ImageView> mViewItems;
	private Button mStartBtn;
	
	private LinearLayout mDotsContainer;
	// 底部dot指示图片
	private List<ImageView> mDotItems;
	// 记录当前选中位置
	private int mCurrentIndex = 0;
	
	// 引导图片资源
//	private static final int[] mPicsRes = { R.drawable.guide_1, R.drawable.guide_2,R.drawable.guide_3, R.drawable.guide_4 };
	private static final int[] mPicsRes = { R.drawable.introduce_img_1, R.drawable.introduce_img_2,R.drawable.introduce_img_3};
	
	private View mInfoView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏，注意一定要在绘制view之前调用这个方法，不然会出现  
        //AndroidRuntimeException: requestFeature() must be called before adding content 这个错误。  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);   //全屏显示  
		setContentView(R.layout.activity_guide);
		mActivity = this;
		//初始化View
		initView();
		// 初始化数据
		initData();
		//初始化Listener
		initListener();
//		reqData();


	}
	private void initView(){
		mInfoView = findViewById(R.id.layout_info);
		mStartBtn = (Button)findViewById(R.id.btn_start);
		mViewPager = (ViewPager) findViewById(R.id.viewpager);	
		mDotsContainer = (LinearLayout)findViewById(R.id.layout_dot);
		
		mStartBtn.setVisibility(View.GONE);
	}

	private void initListener(){
		// 初始化Adapter
		mViewPagerAdapter = new ViewPagerAdapter(mViewItems);
		mViewPager.setAdapter(mViewPagerAdapter);
		// 绑定回调
		mViewPager.setOnPageChangeListener(this);
		mStartBtn.setOnClickListener(this);
	}
	private void initData() {

		mViewItems = new ArrayList<ImageView>(mPicsRes.length);
		mDotItems = new ArrayList<ImageView>(mPicsRes.length);
		mDotsContainer.removeAllViews();
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		for (int i = 0; i < mPicsRes.length; i++) {
			
			// 初始化引导图片列表
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setImageResource(mPicsRes[i]);
			mViewItems.add(i,iv);
			
			//初始化dot指示图片
			ImageView dot = new ImageView(this);
			dot.setLayoutParams(mParams);
			dot.setImageResource(R.drawable.selector_dot);
			dot.setPadding(10, 10, 10, 10);
			dot.setEnabled(true);// 都设为灰色
			dot.setSelected(false);// 都设为灰色
			dot.setOnClickListener(this);
			dot.setTag(i);// 设置位置tag，方便取出与当前位置对应

			mDotsContainer.addView(dot, i, mDotsContainer.getLayoutParams());
			mDotItems.add(i, dot);
		}

		mCurrentIndex = 0;
		mDotItems.get(mCurrentIndex).setSelected(true);// 设置为白色，即选中状态
	}

	/**
	 * 设置当前的引导页
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= mViewItems.size()) {
			return;
		}

		mViewPager.setCurrentItem(position);
	}

	/**
	 * 这只当前引导小点的选中
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon >= mViewItems.size() || mCurrentIndex == positon) {
			return;
		}

		mDotItems.get(positon).setSelected(true);
		mDotItems.get(mCurrentIndex).setSelected(false);

		mCurrentIndex = positon;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0) {
		if (arg0 < 0 || arg0 >= mViewItems.size()) {
			return;
		}
		// 设置底部小点选中状态
		setCurDot(arg0);
		
		
		mViewItems.get(arg0).setImageResource(mPicsRes[arg0]);
	    if (mViewItems != null && arg0 >= mViewItems.size()-1) {
    		mStartBtn.setVisibility(View.VISIBLE);
		}else {
			mStartBtn.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (mStartBtn == v) {
			SharedPrefUtil.getInstance().setFirst();
			Intent intent = new Intent (mActivity,HomeActivity.class);			
			startActivity(intent);			
			mActivity.finish();
			return;
		}
		int position = (Integer) v.getTag();
		if (position < 0 || position >= mViewItems.size() || mCurrentIndex == position) {
			return;
		}
		
		setCurView(position);
		setCurDot(position);
	}

	public class ViewPagerAdapter extends PagerAdapter {

		// 界面列表
		private List<ImageView> views;

		public ViewPagerAdapter(List<ImageView> views) {
			this.views = views;
		}

		// 销毁arg1位置的界面
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(views.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		// 获得当前界面数
		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}

			return 0;
		}

		// 初始化arg1位置的界面
		@Override
		public Object instantiateItem(View arg0, int arg1) {

			((ViewPager) arg0).addView(views.get(arg1), 0);

			return views.get(arg1);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

	}
	@Override
	public void onDestroy() {
		LogUtil.d(TAG, "onDestroy");

		

		super.onDestroy();
	}
	
	
	
	
//	private void refreshUI(){
//		mDotsContainer.removeAllViews();
//		mViewItems = new ArrayList<ImageView>(mPicsRes.length);
//		mDotItems = new ArrayList<ImageView>(mPicsRes.length);
//
//		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.FILL_PARENT,
//				LinearLayout.LayoutParams.FILL_PARENT);
//
//		for (int i = 0; i < mPicsRes.length; i++) {
//			
//			// 初始化引导图片列表
//			ImageView iv = new ImageView(this);
//			iv.setLayoutParams(mParams);
//			iv.setScaleType(ScaleType.FIT_XY);
//			iv.setImageResource(mPicsRes[i]);
//			mViewItems.add(i,iv);
//			
//			//初始化dot指示图片
//			ImageView dot = new ImageView(this);
//			dot.setLayoutParams(mParams);
//			dot.setImageResource(R.drawable.selector_dot);
//			dot.setPadding(10, 10, 10, 10);
//			dot.setEnabled(true);// 都设为灰色
//			dot.setSelected(false);// 都设为灰色
//			dot.setOnClickListener(this);
//			dot.setTag(i);// 设置位置tag，方便取出与当前位置对应
//
//			mDotsContainer.addView(dot, i, mDotsContainer.getLayoutParams());
//			mDotItems.add(i, dot);
//		}
//		mCurrentIndex = 0;
//		mDotItems.get(mCurrentIndex).setSelected(true);// 设置为白色，即选中状态
//		
//		//初始化Listener
//		initListener();
//		setCurView(mCurrentIndex);
//		//默认加载第一张图片
//		
//
//	}

	
	
}
