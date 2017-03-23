/*
 * Copyright (C) 2011 Patrik Akerfeldt
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dev.nutclass.view.pager;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;

import com.dev.nutclass.R;
import com.dev.nutclass.utils.LogUtil;

/**
 * Draws circles (one for each view). The current view position is filled and
 * others are only stroked.
 */
public class CirclePageIndicator extends View implements PageIndicator {
	private static final int INVALID_POINTER = -1;
	private static final String TAG = "CirclePageIndicator";

	private float mRadius;
	//表示页面数量的点
	private final Paint mPaintPageFill = new Paint(ANTI_ALIAS_FLAG);
	//对表示页面数量的点加边框
	private final Paint mPaintStroke = new Paint(ANTI_ALIAS_FLAG);
	//表示滑动选取页面的点
	private final Paint mPaintFill = new Paint(ANTI_ALIAS_FLAG);
	private ViewPager mViewPager;
	private ViewPager.OnPageChangeListener mListener;
	private int mCurrentPage;
	private int mSnapPage;
	private float mPageOffset;
	private int mScrollState;
	private int mOrientation;
	private boolean mCentered;
	private boolean mSnap;//true:点不随滑动而移动；false：点随滑动而移动
	private int mTouchSlop;
	private float mLastMotionX = -1;
	private int mActivePointerId = INVALID_POINTER;
	private boolean mIsDragging;
	private float interval=0;//点与点的中心之间的间隙
	private  int count;
	private int num;
	public CirclePageIndicator(Context context) {
		this(context, null);
	}

	public CirclePageIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, R.attr.vpiCirclePageIndicatorStyle);
	}

	public CirclePageIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode())
			return;

		// Load defaults from resources
		final Resources res = getResources();
		final int defaultPageColor = res.getColor(R.color.circle_page_indicator_page_color);
		final int defaultFillColor = res.getColor(R.color.circle_page_indicator_fill_color);
		final int defaultOrientation = res.getInteger(R.integer.circle_indicator_orientation);
		final int defaultStrokeColor = res.getColor(R.color.circle_page_indicator_stroke_color);
		final float defaultStrokeWidth = res.getDimension(R.dimen.circle_indicator_stroke_width);
		final float defaultRadius = res.getDimension(R.dimen.circle_indicator_radius);
		final boolean defaultCentered = res.getBoolean(R.bool.circle_indicator_centered);
		final boolean defaultSnap = res.getBoolean(R.bool.circle_indicator_snap);

		// Retrieve styles attributes
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.CirclePageIndicator, defStyle, 0);

		mCentered = a.getBoolean(R.styleable.CirclePageIndicator_centered,defaultCentered);
		mOrientation = a.getInt(R.styleable.CirclePageIndicator_android_orientation,defaultOrientation);
		mPaintPageFill.setStyle(Style.FILL);
		mPaintPageFill.setColor(a.getColor(R.styleable.CirclePageIndicator_pageColor, defaultPageColor));
		mPaintStroke.setStyle(Style.STROKE);
		mPaintStroke.setColor(a.getColor(R.styleable.CirclePageIndicator_strokeColor,defaultStrokeColor));
		mPaintStroke.setStrokeWidth(a.getDimension(R.styleable.CirclePageIndicator_strokeWidth,defaultStrokeWidth));
		mPaintFill.setStyle(Style.FILL);
		mPaintFill.setColor(a.getColor(R.styleable.CirclePageIndicator_fillColor, defaultFillColor));
		mRadius = a.getDimension(R.styleable.CirclePageIndicator_radius,defaultRadius);
		mSnap = a.getBoolean(R.styleable.CirclePageIndicator_snap, defaultSnap);

		Drawable background = a.getDrawable(R.styleable.CirclePageIndicator_android_background);
		if (background != null) {
			setBackgroundDrawable(background);
		}
		a.recycle();
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = ViewConfigurationCompat
				.getScaledPagingTouchSlop(configuration);
		interval=mRadius*4;
	}

	public void setCentered(boolean centered) {
		mCentered = centered;
		invalidate();
	}

	public boolean isCentered() {
		return mCentered;
	}

	public void setPageColor(int pageColor) {
		mPaintPageFill.setColor(pageColor);
		invalidate();
	}

	public int getPageColor() {
		return mPaintPageFill.getColor();
	}

	public void setFillColor(int fillColor) {
		mPaintFill.setColor(fillColor);
		invalidate();
	}

	public int getFillColor() {
		return mPaintFill.getColor();
	}

	public void setOrientation(int orientation) {
		switch (orientation) {
		case HORIZONTAL:
		case VERTICAL:
			mOrientation = orientation;
			requestLayout();
			break;

		default:
			throw new IllegalArgumentException(
					"Orientation must be either HORIZONTAL or VERTICAL.");
		}
	}

	public int getOrientation() {
		return mOrientation;
	}

	public void setStrokeColor(int strokeColor) {
		mPaintStroke.setColor(strokeColor);
		invalidate();
	}

	public int getStrokeColor() {
		return mPaintStroke.getColor();
	}

	public void setStrokeWidth(float strokeWidth) {
		mPaintStroke.setStrokeWidth(strokeWidth);
		invalidate();
	}

	public float getStrokeWidth() {
		return mPaintStroke.getStrokeWidth();
	}

	public void setRadius(float radius) {
		mRadius = radius;
		invalidate();
	}

	public float getRadius() {
		return mRadius;
	}

	public void setSnap(boolean snap) {
		mSnap = snap;
		invalidate();
	}

	public boolean isSnap() {
		return mSnap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mViewPager == null) {
			return;
		}
		if(num == 2){
			count = (((BigPicPagerAdapter)mViewPager.getAdapter()).getSize())/2;
		}else{
			count = ((BigPicPagerAdapter)mViewPager.getAdapter()).getSize();
		}

		if (count == 0) {
			return;
		}
		int offsetPage=mCurrentPage%count;
		int totalSize;
		//距离上下左右的padding值
		int startPadding;
		int endPadding;
		
		//用于定位圆点的中心点
		float fixCoordinate;//固定坐标（Horizontal对应的是y。Vertical对应的是x）
		float changeCoordinate;
		if (mOrientation == HORIZONTAL) {
			totalSize = getWidth();
			startPadding = getPaddingLeft();
			endPadding = getPaddingRight();
			fixCoordinate=getHeight()/2;
		} else {
			totalSize = getHeight();
			startPadding = getPaddingTop();
			endPadding = getPaddingBottom();
			fixCoordinate=getWidth()/2;
		}

		changeCoordinate = startPadding + mRadius;
		if (mCentered) {
			changeCoordinate = (totalSize/2
					- ((count-1) * interval)/2);
		}

		float dX;
		float dY;

		float pageRadius = mRadius;
		//计算是否有边缘宽度
		if (mPaintStroke.getStrokeWidth() > 0) {
			pageRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
		}

		// Draw stroked circles
		for (int iLoop = 0; iLoop < count; iLoop++,changeCoordinate+=interval) {
			if (mOrientation == HORIZONTAL) {
				dX = changeCoordinate;
				dY = fixCoordinate;
			} else {
				dX = fixCoordinate;
				dY = changeCoordinate;
			}
			// Only paint fill if not completely transparent
			if (mPaintPageFill.getAlpha() > 0) {
				canvas.drawCircle(dX, dY, pageRadius, mPaintPageFill);
			}

			// Only paint stroke if a stroke width was non-zero
			if (pageRadius != mRadius) {
				canvas.drawCircle(dX, dY, pageRadius, mPaintStroke);
			}
		}

		// Draw the filled circle according to the current scroll
		float cx = (mSnap ? offsetPage : offsetPage) * interval+mRadius;
		if (!mSnap) {
			cx += mPageOffset * interval;
		}
//		LogUtil.d(TAG, "mCurrentPage="+mCurrentPage+",offsetPage="+offsetPage);
		if (mOrientation == HORIZONTAL) {
			dX = startPadding + cx;
			dY = fixCoordinate;
		} else {
			dX = fixCoordinate;
			dY = startPadding + cx;
		}
		canvas.drawCircle(dX, dY, mRadius, mPaintFill);
	}


	@Override
	public void setViewPager(ViewPager view) {
		if (mViewPager == view) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}
		if (view.getAdapter() == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		mViewPager = view;
		mViewPager.setOnPageChangeListener(this);
		invalidate();
	}

	@Override
	public void setViewPager(ViewPager view, int initialPosition) {
		LogUtil.d(TAG, "setViewPager");
		setViewPager(view);
		setCurrentItem(initialPosition);
	}

	public void setViewPagerNew(ViewPager view, int initialPosition,int imgNum){
		LogUtil.d(TAG, "setViewPager");
		setViewPager(view);
		setCurrentItem(initialPosition);
		num = imgNum;
	}

	@Override
	public void setCurrentItem(int item) {
		if (mViewPager == null) {
			throw new IllegalStateException("ViewPager has not been bound.");
		}
		mViewPager.setCurrentItem(item);
		mCurrentPage = item;
		invalidate();
	}

	@Override
	public void notifyDataSetChanged() {
		invalidate();
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		mScrollState = state;

		if (mListener != null) {
			mListener.onPageScrollStateChanged(state);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		mCurrentPage = position;
		mPageOffset = positionOffset;
		invalidate();

		if (mListener != null) {
			mListener.onPageScrolled(position, positionOffset,
					positionOffsetPixels);
		}
	}

	@Override
	public void onPageSelected(int position) {
		if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
			mCurrentPage = position;
			mSnapPage = position;
			invalidate();
		}

		if (mListener != null) {
			mListener.onPageSelected(position);
		}
	}

	@Override
	public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
		mListener = listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mOrientation == HORIZONTAL) {
			setMeasuredDimension(measureLong(widthMeasureSpec),
					measureShort(heightMeasureSpec));
		} else {
			setMeasuredDimension(measureShort(widthMeasureSpec),
					measureLong(heightMeasureSpec));
		}
	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureLong(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
			// We were told how big to be
			result = specSize;
		} else {
			// Calculate the width according the views count
			final int count = ((BigPicPagerAdapter)mViewPager.getAdapter()).getSize();
			result = (int) (getPaddingLeft() + getPaddingRight()
					+ 2*mRadius + (count - 1) * interval);
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureShort(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the height
			result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom());
			// Respect AT_MOST value if that was what is called for by
			// measureSpec
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		SavedState savedState = (SavedState) state;
		super.onRestoreInstanceState(savedState.getSuperState());
		mCurrentPage = savedState.currentPage;
		mSnapPage = savedState.currentPage;
		requestLayout();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		SavedState savedState = new SavedState(superState);
		savedState.currentPage = mCurrentPage;
		return savedState;
	}

	static class SavedState extends BaseSavedState {
		int currentPage;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			currentPage = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(currentPage);
		}

		@SuppressWarnings("UnusedDeclaration")
		public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
