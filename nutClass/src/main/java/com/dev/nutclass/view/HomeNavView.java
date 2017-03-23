package com.dev.nutclass.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.LogUtil;

public class HomeNavView extends FrameLayout implements OnClickListener {
	private static final String TAG = "BottomBar";
	private Context mContext;
	private List<ItemView> mItemList;
	private LayoutInflater mFactory;
	private LinearLayout mRootLayout;
	private int mSelected = -1;
	
	

	
	public HomeNavView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		initView();
	}
	public HomeNavView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	private void initView(){
		mItemList = new ArrayList<ItemView>();

		mFactory = LayoutInflater.from(mContext);
		mRootLayout = new LinearLayout(mContext);
		mRootLayout.setOrientation(LinearLayout.HORIZONTAL);

		addItem(1, mContext.getString(R.string.home_nav_home),
				R.drawable.selector_home_nav_home);
		
		addItem(2, mContext.getString(R.string.home_nav_brand),
				R.drawable.selector_home_nav_brand);
		addItem(3, mContext.getString(R.string.home_nav_community),
				R.drawable.selector_home_nav_community);
		addItem(4, mContext.getString(R.string.home_nav_shopping),
				R.drawable.selector_home_nav_shopping);
		addItem(5, mContext.getString(R.string.home_nav_me),
				R.drawable.selector_home_nav_me);
		

//		addItem(2, mContext.getString(R.string.home_nav_category),
//				R.drawable.selector_home_nav_category);
//
//		addItem(3, mContext.getString(R.string.home_nav_shopping),
//				R.drawable.selector_home_nav_shopping);
//
//		addItem(4, mContext.getString(R.string.home_nav_me),
//				R.drawable.selector_home_nav_me);

		mRootLayout.setWeightSum(mItemList.size());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		this.addView(mRootLayout, params);
	}

	public void addItem(int id, String text, int res) {
		LinearLayout ll = new LinearLayout(mContext);
		ll.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT, (float) 1.0);
		mRootLayout.addView(ll, params);

		View child = mFactory.inflate(R.layout.item_home_nav, null);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		ll.addView(child, params);

		child.setClickable(true);
		child.setOnClickListener(this);
		child.setTag(id-1);

		ItemView it = new ItemView(id, text, res, child);
		mItemList.add(it);
	}

	public void setSelected(int index) {
		if (index >= 0 && index < mItemList.size()) {
			mSelected = index;
			updateState(index);
		}
	}

	public int getSelected() {
		return mSelected;
	}

	private void updateState(int selected) {
		if (selected < 0 || selected >= mItemList.size())
			return;
		for (int i = 0; i < mItemList.size(); i++) {
			if (i == selected) {
				mItemList.get(i).setSelected(true);
			} else {
				mItemList.get(i).setSelected(false);
			}
		}
		mSelected = selected;
	}

	@Override
	public void onClick(View v) {
		LogUtil.d(TAG, "onClick=" + v.getId());
		int tag=(Integer) v.getTag();
		for (int i = 0; i < mItemList.size(); i++) {
			if (tag==i) {
				boolean isChange = true;
				if (mOnTabClickListener != null) {
					isChange = mOnTabClickListener.onTabClick(i);
				}
				if (isChange) {
					updateState(i);
				}
				break;
			}
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}

	public OnTabClickListener mOnTabClickListener;

	public OnTabClickListener getOnBarTabClickListener() {
		return mOnTabClickListener;
	}

	public void setOnBarTabClickListener(OnTabClickListener mOnTabClickListener) {
		this.mOnTabClickListener = mOnTabClickListener;
	}

	public interface OnTabClickListener {
		public boolean onTabClick(int pos);
	}

	class ItemView {
		private final int id;
		private final TextView navTv;
		private final ImageView navIv;

		public ItemView(int id, String s, int d, View v) {
			this.id = id;
			navTv = (TextView) v.findViewById(R.id.bottom_txt);
			navIv = (ImageView)v.findViewById(R.id.bottom_img);
			navTv.setText(s);
			navIv.setImageResource(d);
		}

		public void setSelected(boolean b) {
			navTv.setSelected(b);
			navIv.setSelected(b);
		}

		public int getId() {
			return id;
		}

	}

}
