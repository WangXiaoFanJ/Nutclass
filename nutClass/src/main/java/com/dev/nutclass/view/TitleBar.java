package com.dev.nutclass.view;

import com.dev.nutclass.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 标题栏-提供通用的标题栏控制
 * 
 * @author MR.HUANG
 * 
 */
public final class TitleBar extends FrameLayout implements OnClickListener {

	public final String TAG = "TitleBar";

	/**
	 * TAG值，标题栏左边
	 */
	public static final int TAG_LEFT = 0;
	/**
	 * TAG值，标题栏中间
	 */
	public static final int TAG_MIDDLE = 3;

	/**
	 * TAG值，标题栏右边第一个按钮
	 */
	public static final int TAG_RIGHT1 = 1;

	/**
	 * TAG值，标题栏右边第二个按钮
	 */
	public static final int TAG_RIGHT2 = 2;
	/**
	 * TAG值，标题栏右边第三个按钮
	 */
	public static final int TAG_RIGHT3 = 3;
	/**
	 * TAG值，标题栏右边第四个按钮
	 */
	public static final int TAG_RIGHT4 = 4;

	// ********************按钮类型****************************
	/**
	 * 控件类型-空
	 */
	public static final int TYPE_NULL = 0;

	/**
	 * 控件类型-返回按钮
	 */
	public static final int TYPE_BACK_IMG = 1;

	/**
	 * 控件类型- 文字
	 */
	public static final int TYPE_MIDDLE_TXT = 2;
	/**
	 * 控件类型- 完成
	 */
	public static final int TYPE_COMPLETE_TXT = 3;
	/**
	 * 控件类型-下一步
	 */
	public static final int TYPE_NEXT_TXT = 4;
	/**
	 * 控件类型-相机
	 */
	public static final int TYPE_CAMERA_IMG = 5;
	/**
	 * 控件类型-关闭
	 */
	public static final int TYPE_CLOSE_IMG = 6;
	/**
	 * 控件类型-发布
	 */
	public static final int TYPE_RELEASE_TXT = 7;
	/**
	 * 控件类型-设置
	 */
	public static final int TYPE_SETTING_IMG = 8;
	/**
	 * 控件类型-提交
	 */
	public static final int TYPE_SUBMIT_TXT = 9;
	/**
	 * 控件类型- 保存登陆
	 */
	public static final int TYPE_RESET_TXT = 10;
	
	/**
	 * 控件类型- 购物车编辑
	 */
	public static final int TYPE_EDIT_TXT = 11;
	/**
	 * 控件类型- 搜索
	 */
	public static final int TYPE_SEARCH_IMG = 12;
	/**
	 * 控件类型- 发布市场
	 */
	public static final int TYPE_RELEASE_IMG = 13;
	/**
	 * 控件类型- 发布市场
	 */
	public static final int TYPE_LEFT_ICON_IMG = 100;
	/**
	 * 控件类型- 删除
	 */
	public static final int TYPE_DELETE_TXT = 101;

	/**
	* 分享
	* */
	public static final int TYPE_SHARE_TXT = 200;
	private View mTitlebar;
	private FrameLayout mLeftLayout;
	private FrameLayout mRightLayout1;
	private FrameLayout mRightLayout2;
	private FrameLayout mMiddleLayout;

	private Context context;

	private TextView mMiddleTv;
	private ImageView middleArrawIv;
	private boolean middleIsOpen = false;

	private BarClickListener mBarClickListener;

	public TitleBar(Context context) {
		this(context, null);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		init(attrs);
	}

	/**
	 * 在代码中动态设置titlebar左侧按钮
	 * 
	 * @param leftType
	 */
	public void setTitleLeft(int leftType) {
		setView(TAG_LEFT, createView(leftType));
	}

	/**
	 * 在代码中动态设置titlebar右侧按钮
	 * 
	 * @param rightType1
	 * @param rightType2
	 */
	public void setTitleRight1(int rightType1) {
		setView(TAG_RIGHT1, createView(rightType1));
	}

	public void setTitleRight2(int rightType2) {
		setView(TAG_RIGHT2, createView(rightType2));
	}

	public void removeTitleRight2() {
		if (mRightLayout2 != null) {
			mRightLayout2.removeAllViews();
		}
	}

	public void removeTitleRight1() {
		if (mRightLayout1 != null) {
			mRightLayout1.removeAllViews();
		}
	}
	public void removeTitleLeft() {
		if (mLeftLayout != null) {
			mLeftLayout.removeAllViews();
		}
	}

	/**
	 * 初始化各控件
	 */
	private void init(AttributeSet attrs) {

		mTitlebar = LayoutInflater.from(getContext()).inflate(
				R.layout.view_title_tab, this);
		mLeftLayout = (FrameLayout) findViewById(R.id.fl_title_left);
		mRightLayout1 = (FrameLayout) findViewById(R.id.fl_title_right1);
		mRightLayout2 = (FrameLayout) findViewById(R.id.fl_title_right2);
		mMiddleLayout = (FrameLayout) findViewById(R.id.fl_title_middle);
		// 根据设置参数创建控件
		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.TitleBar, 0, 0);

			int leftType = a.getInt(R.styleable.TitleBar_left_type, TYPE_NULL);
			int rightType1 = a.getInt(R.styleable.TitleBar_right_type1,
					TYPE_NULL);
			int rightType2 = a.getInt(R.styleable.TitleBar_right_type2,
					TYPE_NULL);
			String middleText = a.getString(R.styleable.TitleBar_middle_text);

			a.recycle();

			mLeftLayout.setOnClickListener(this);
			mRightLayout1.setOnClickListener(this);
			mRightLayout2.setOnClickListener(this);
			mMiddleLayout.setOnClickListener(this);

			setView(TAG_LEFT, createView(leftType));
			setView(TAG_RIGHT1, createView(rightType1));
			setView(TAG_RIGHT2, createView(rightType2));
			setMiddleTextView(middleText);

		}
	}

	/**
	 * titleBar背景颜色
	 */
	public void setBackgroundColorResource(int resId) {
		mTitlebar.setBackgroundResource(resId);
	}

	public void setMiddleText(String text) {
		setMiddleText(text, false, false);
	}
	public void setMiddleText(String text, boolean showArray, boolean isOpen) {
		if (mMiddleTv != null) {
			mMiddleTv.setText(text);
		}
		if (showArray) {
			middleArrawIv.setVisibility(View.VISIBLE);
			if (isOpen) {
				middleArrawIv.setImageResource(R.drawable.titlebar_album_dwon);
			} else {
				middleArrawIv
						.setImageResource(R.drawable.titlebar_album_normal);
			}
		} else {
			middleArrawIv.setVisibility(View.GONE);
		}
	}

	public String getMiddleText() {
		if (mMiddleTv != null) {
			return mMiddleTv.getText().toString();
		}
		return "";
	}

	private void setMiddleTextView(String text) {
		View view = (View) createView(TYPE_MIDDLE_TXT);
		mMiddleTv = (TextView) view.findViewById(R.id.view_text);
		middleArrawIv = (ImageView) view.findViewById(R.id.iv_arraw);
		middleArrawIv.setVisibility(View.GONE);
		if (view != null) {
			mMiddleTv.setText(text);
			setView(TAG_MIDDLE, view);
		}
	}

	@SuppressLint("NewApi")
	private View createView(int type) {
		View v = null;
		switch (type) {
		// 返回按钮
		case TYPE_BACK_IMG: {
			v = createImageView(R.drawable.titlebar_back);
			break;
		}
		// 标题文本
		case TYPE_MIDDLE_TXT: {
			v = LayoutInflater.from(context).inflate(
					R.layout.view_title_textview, null);
			v.setTag(TYPE_MIDDLE_TXT);
			break;
		}
		case TYPE_COMPLETE_TXT: {
			v = createTextView(R.string.text_titlebar_complete);
			break;
		}
		case TYPE_RESET_TXT: {
			v = createTextView(R.string.text_titlebar_reset);
			break;
		}
			case TYPE_SHARE_TXT:{
				v = createImageView(R.drawable.shareimage);
				break;
			}
		case TYPE_NEXT_TXT: {
			v = createTextView(R.string.text_titlebar_next);
			break;
		}
		case TYPE_CAMERA_IMG: {
			v = createImageView(R.drawable.selector_titlebar_camera);
			break;
		}
		case TYPE_CLOSE_IMG: {
			v = createImageView(R.drawable.titlebar_close);
			break;
		}
		case TYPE_RELEASE_TXT: {
			v = createTextView(R.string.text_titlebar_release);
			break;
		}
		case TYPE_EDIT_TXT: {
			v = createTextView(R.string.text_titlebar_edit);
			break;
		}
		case TYPE_DELETE_TXT: {
			v = createTextView(R.string.text_titlebar_del);
			break;
		}
		case TYPE_SETTING_IMG: {
			v = createImageView(R.drawable.selector_titlebar_setting);
			break;
		}
		case TYPE_SEARCH_IMG: {
			v = createImageView(R.drawable.nav_icon_search);
			break;
		}
		case TYPE_RELEASE_IMG: {
			v = createImageView(R.drawable.titlebar_release);
			break;
		}
		case TYPE_SUBMIT_TXT: {
			v = createTextView(R.string.text_titlebar_submit);
			break;
		}
		case TYPE_LEFT_ICON_IMG: {
			v = createImageView(R.drawable.titlebar_left_log);
			break;
		}
		default:
			break;
		}
		return v;
	}

	private View createImageView(int resid) {
		ImageView v = new ImageView(context);
		v.setImageResource(resid);
		return v;
	}

	private View createTextView(int resid) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_title_textview_btn, null);

		TextView tv = (TextView) view.findViewById(R.id.tv_name);
		tv.setText(resid);
		return view;
	}

	/**
	 * 给title bar设置特定的控件
	 * 
	 * @param position
	 *            控件位置：TAG_LEFT, TAG_MIDDLE, OR TAG_RIGHT
	 * @param view
	 *            添加的控件
	 */
	private void setView(int position, View view) {
		if (view == null) {
			return;
		}
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		switch (position) {
		case TAG_LEFT: {
			mLeftLayout.removeAllViews();
			mLeftLayout.addView(view);
			break;
		}
		case TAG_MIDDLE: {
			mMiddleLayout.removeAllViews();
			mMiddleLayout.addView(view);
			break;
		}

		case TAG_RIGHT1: {
			mRightLayout1.removeAllViews();
			mRightLayout1.addView(view, params);
			break;
		}

		case TAG_RIGHT2: {
			mRightLayout2.removeAllViews();
			mRightLayout2.addView(view, params);
			break;
		}
		default:
			break;
		}
	}

	// 处理已知类型的控件的点击事件
	@Override
	public void onClick(View v) {
		if (doBarClick(v))
			return;
		if (v == mLeftLayout) {
			if(mLeftLayout.getChildCount()>0){
				((Activity) context).finish();
			}
		}

	}

	private boolean doBarClick(View v) {
		boolean handled = false;
		if (mBarClickListener != null) {
			if (v == mRightLayout1) {
				handled = mBarClickListener.onClickRight1();
			} else if (v == mRightLayout2) {
				handled = mBarClickListener.onClickRight2();
			} else if (v == mLeftLayout) {
				handled = mBarClickListener.onClickLeft();
			} else if (v == mMiddleLayout) {
				handled = mBarClickListener.onClickMiddle();
			}
		}
		return handled;
	}

	public void setBarClickListener(BarClickListener listener) {
		mBarClickListener = listener;
	}

	public View getChildView(int type) {
		View view = null;
		switch (type) {
		case TAG_LEFT:
			view = mLeftLayout.getChildAt(0);
			break;
		case TAG_RIGHT1:
			view = mRightLayout1.getChildAt(0);
			break;
		case TAG_RIGHT2:
			view = mRightLayout2.getChildAt(0);
			break;
		default:
			break;
		}
		return view;
	}

	public static interface BarClickListener {
		public boolean onClickRight1();

		public boolean onClickRight2();

		public boolean onClickLeft();

		public boolean onClickMiddle();

	}
}
