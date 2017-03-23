package com.dev.nutclass.view;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dev.nutclass.R;

public class PullListViewHeader extends LinearLayout {
	private static final String NAMESPACE = "http://www.weibo.com/apk/res/PullListViewHeaderFooter";
	private static final String ATTR_ROTATE = "type";

	private LinearLayout mContainer;
	private ImageView arrawIv;
	private ProgressBar mProgressBar;
	private TextView descTv;
	private int mState = STATE_NORMAL;

	private Animation mRotateAnim;
	private Animation mRotateUndoAnim;

	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;//常态
	public final static int STATE_READY = 1;//准备刷新
	public final static int STATE_REFRESHING = 2;//正在刷新
	public PullListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public PullListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_pull_listview_header_footer, null);
		addView(mContainer, lp);
		arrawIv = (ImageView) findViewById(R.id.iv_array);
		descTv = (TextView) findViewById(R.id.tv_address);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
		
		mContainer.setGravity(Gravity.BOTTOM);
		arrawIv.setImageResource(R.drawable.z_arrow_down);
		descTv.setText(R.string.pull_listview_header_hint_normal);

		mRotateAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateAnim.setFillAfter(true);
		mRotateUndoAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUndoAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUndoAnim.setFillAfter(true);
	}
	
	public void setState(int state) {
		if (state == mState)
			return;

		if (state == STATE_REFRESHING) { // 显示进度
			arrawIv.clearAnimation();
			arrawIv.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else { // 显示箭头图片
			arrawIv.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				arrawIv.startAnimation(mRotateUndoAnim);
			}else if (mState == STATE_REFRESHING) {
				arrawIv.clearAnimation();
			}
			descTv.setText(R.string.pull_listview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				arrawIv.clearAnimation();
				arrawIv.startAnimation(mRotateAnim);
				descTv.setText(R.string.pull_listview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			// 正在加载...
			descTv.setText(R.string.pull_listview_header_hint_loading);
			break;
		default:
		}

		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}
}
