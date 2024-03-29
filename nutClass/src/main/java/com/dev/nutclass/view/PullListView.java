package com.dev.nutclass.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.dev.nutclass.R;

public class PullListView extends ListView implements OnScrollListener {

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private PullListViewListener mListViewListener;

	// -- header view
	private PullListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullDown = true;
	private boolean mPullDowning = false; // is refreashing.

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.

	/**
	 * @param context
	 */
	public PullListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public PullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public PullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new PullListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.rl_head_content);

		addHeaderView(mHeaderView);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@SuppressLint("NewApi")
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.
		super.setAdapter(adapter);
	}

	public boolean getPullDownEnable() {
		return mEnablePullDown;
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullDownEnable(boolean enable) {
		mEnablePullDown = enable;
		if (!mEnablePullDown) { // disable, hide the content
			mHeaderView.findViewById(R.id.iv_array).setVisibility(
					View.INVISIBLE);
			mHeaderView.findViewById(R.id.pb_loading).setVisibility(
					View.INVISIBLE);
			((TextView) mHeaderView.findViewById(R.id.tv_address))
					.setText(getResources().getString(
							R.string.pull_listview_header_hint_no));
			mHeaderView.findViewById(R.id.tv_address).setVisibility(View.VISIBLE);
		} else {
			mHeaderView.findViewById(R.id.iv_array).setVisibility(View.VISIBLE);
			mHeaderView.findViewById(R.id.pb_loading).setVisibility(
					View.INVISIBLE);
		}
	}

 
	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullDowning == true) {
			mPullDowning = false;
		}
		resetHeaderHeight();
	}

	

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullDown && !mPullDowning) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(PullListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(PullListViewHeader.STATE_NORMAL);
			}
		}
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullDowning && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullDowning && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	
	public void startRefresh() {
		this.setSelection(0);
		updateHeaderHeight(mHeaderViewHeight);
		invokeOnScrolling();
		mHeaderView.setState(PullListViewHeader.STATE_REFRESHING);
	}

	public boolean getPullDowning() {
		return mPullDowning;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			} 
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullDown
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullDowning = true;
					mHeaderView.setState(PullListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onPullDown();
					}
				}
				resetHeaderHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setPullListViewListener(PullListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface PullListViewListener {
		public void onPullDown();
		public void onPullUp();
	}
}
