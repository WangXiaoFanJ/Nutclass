package com.dev.nutclass.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AblumsRectView extends LinearLayout {
	private static final String TAG = "AblumsRectView";
	private Context mContext;
	private LayoutInflater mInflater;

	private static final int DEFAULT_COLS = 3;// 默认三列
	public int MAXSIZE = 9;
	private int mColumn = DEFAULT_COLS;
	private int mRow = 0;
	private List<ImageEntity> mItemList;

	private List<ImageEntity> mRawList;// 保存原始数据（主要用于超过最大数进入大图浏览的情况）

	private View mRootLayout;
	private LinearLayout mContainer;
	private TableLayout mTableLayout;

	private boolean mShowArrow = false;// 是否显示箭头
	private boolean showDel = false;// 是否显示删除

	public void setShowArrow(boolean show) {
		this.mShowArrow = show;
	}

	private boolean isShowAddView = true;// 是否显示Addview

	public void setShowAddView(boolean show) {
		this.isShowAddView = show;
	}

	public void setMax(int max) {
		this.MAXSIZE = max;
	}

	public int getMax() {
		return this.MAXSIZE;
	}

	public int getColumn() {
		return mColumn;
	}

	public void setColumn(int mColumn) {
		this.mColumn = mColumn;
	}

	private static final String VIEW_TAG_IMG = "img";// 普通ImgView 的tag
	private static final String VIEW_TAG_ADD = "add";// add 的tag
	private static final String VIEW_TAG_DEL = "del";// del 的tag

	private OnItemClickListener onItemClickListener;

	public interface OnItemClickListener {

		// 修改图片
		public void albumItemClicked(int pos);

		// 添加图片
		public void addItemClicked();

		// 删除图片
		public void deleteItemClicked(int pos);

	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public AblumsRectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public AblumsRectView(Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	private void init(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(getContext());
		mInflater.inflate(R.layout.group_container, this);
		initViews();
	}

	private void initViews() {
		mRootLayout = findViewById(R.id.root_layout);
		mContainer = (LinearLayout) findViewById(R.id.layout_container);

		mTableLayout = new TableLayout(mContext);
		mTableLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		mTableLayout.setGravity(Gravity.CENTER_VERTICAL);
		mTableLayout.setClickable(false);
		mTableLayout.setFocusable(false);

		mContainer.addView(mTableLayout);

	}

	/**
	 * init image path
	 * 
	 * @param albumUrlList
	 *            BigPic
	 * @param thumbUrlList
	 *            thumbPic
	 * */

	public boolean firstPic = true;

	public void updateUI(List<ImageEntity> list) {
		if ((list == null || list.isEmpty()) && !isShowAddView) {
			setVisibility(View.GONE);
			return;
		}
		setVisibility(View.VISIBLE);
		if (mItemList == null) {
			mItemList = new ArrayList<ImageEntity>();
		}
		if (mRawList == null) {
			mRawList = new ArrayList<ImageEntity>();
		}
		mItemList.clear();
		mRawList.clear();
		if (list != null) {
			mItemList.addAll(list);
			mRawList.addAll(list);
		}

		if (mItemList.size() <= MAXSIZE) {
			if (isShowAddView && mItemList.size() < MAXSIZE) {
				mItemList.add(new ImageEntity());
			}
		}
		mContainer.setVisibility(View.VISIBLE);
		// 对多张图的处理
		mTableLayout.removeAllViews();
		if (mItemList != null && !mItemList.isEmpty()) {// 更新view的内容
			mRow = (mItemList.size() + mColumn - 1) / mColumn;// 算行数，初始化完毕
			mTableLayout.setGravity(Gravity.CENTER_VERTICAL);
			initTableView(mItemList);
			mTableLayout.setClickable(false);
			mTableLayout.setFocusable(false);
		}
	}

	private void initTableView(List<ImageEntity> ablumsList) {
		for (int i = 0; i < mRow; i++) {
			TableRow tr = new TableRow(getContext());
			TableLayout.LayoutParams params = new TableLayout.LayoutParams(
					TableRow.LayoutParams.FILL_PARENT,
					TableRow.LayoutParams.WRAP_CONTENT);
			params.weight = 1;
			tr.setLayoutParams(params);
			tr.setGravity(Gravity.CENTER_VERTICAL);
			tr.setClickable(false);
			tr.setFocusable(false);
			for (int j = 0; j < mColumn; j++) {
				View view = null;
				int pos = i * mColumn + j;

				if (i * mColumn + j < ablumsList.size()) {
					ImageEntity item = ablumsList.get(pos);
					view = getItemView(item, pos);
					view.setId(pos);
				} else {
					view = getEmptyView();
					view.setId(pos);
				}
				view.setClickable(true);
				view.setFocusable(true);

				if (view.getVisibility() != View.VISIBLE) {
					view.setOnClickListener(null);
				}
				tr.addView(view);
			}
			mTableLayout.addView(tr);
		}
	}

	/**
	 * 获取空view
	 * 
	 * @return
	 */
	private View getEmptyView() {
		TableRow.LayoutParams params = new TableRow.LayoutParams(0,
				TableRow.LayoutParams.FILL_PARENT);
		View view = View.inflate(getContext(), R.layout.ablum_item, null);
		params.weight = 1;
		view.setLayoutParams(params);
		view.setVisibility(View.INVISIBLE);
		return view;
	}

	/**
	 * 获取item view
	 * 
	 * @param item
	 * @return
	 */
	private View getItemView(ImageEntity ablum, int pos) {

		View view = View.inflate(getContext(), R.layout.ablum_item, null);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_ablum_item);
		iv.setScaleType(ScaleType.CENTER_CROP);// add by Mr.Huang 解决图片拉伸问题
		View viewAdd = view.findViewById(R.id.ll_add);
		View ablumItem = view.findViewById(R.id.ll_ablum_item);
		View deleteIv = view.findViewById(R.id.iv_ablum_delete);

		TextView tv_mask = (TextView) view.findViewById(R.id.tv_album_mask);
		tv_mask.setVisibility(View.GONE);

		TableRow.LayoutParams params = new TableRow.LayoutParams(0,
				TableRow.LayoutParams.FILL_PARENT);
		params.weight = 1;
		params.topMargin = getResources().getDimensionPixelSize(
				R.dimen.common_1_5);
		params.leftMargin = getResources().getDimensionPixelSize(
				R.dimen.common_1_5);
		params.bottomMargin = getResources().getDimensionPixelSize(
				R.dimen.common_1_5);
		params.rightMargin = getResources().getDimensionPixelSize(
				R.dimen.common_1_5);
		view.setLayoutParams(params);
		deleteIv.setOnClickListener(new MyOnClickListener(VIEW_TAG_DEL,pos));
		deleteIv.setTag(VIEW_TAG_DEL);
		if(showDel){
			deleteIv.setVisibility(View.VISIBLE);
		}else{
			deleteIv.setVisibility(View.GONE);
		}
		
		if (ablum.isEmpty()) {
			view.setOnClickListener(new MyOnClickListener(VIEW_TAG_ADD,pos));
			viewAdd.setVisibility(View.VISIBLE);
			ablumItem.setVisibility(View.GONE);
		} else {
			view.setOnClickListener(new MyOnClickListener(VIEW_TAG_IMG,pos));
			ablumItem.setVisibility(View.VISIBLE);
			viewAdd.setVisibility(View.GONE);
			if (TextUtil.isNotNull(ablum.getImgPath())) {
				ImageLoader.getInstance().displayImage(
						"file:///" + ablum.getImgPath(), iv,ImgConfig.getFeedOption());
			}else if(TextUtil.isNotNull(ablum.getSmallPath())){
				ImageLoader.getInstance().displayImage(
						ablum.getSmallPath(), iv,ImgConfig.getFeedOption());
			}
		}
		return view;
	}

	public boolean isShowDel() {
		return showDel;
	}

	public void setShowDel(boolean showDel) {
		this.showDel = showDel;
	}

	class MyOnClickListener implements OnClickListener{
		private int pos=-1;
		private String viewTag="";
		public MyOnClickListener(String viewTag){
			this.viewTag=viewTag;
		}
		public MyOnClickListener(String viewTag,int pos){
			this.pos=pos;
			this.viewTag=viewTag;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == mRootLayout) {
				return;
			}
			if (onItemClickListener != null) {
				if (viewTag.equals(VIEW_TAG_ADD)) {
					onItemClickListener.addItemClicked();
				}else if (viewTag.equals(VIEW_TAG_IMG)) {
					onItemClickListener.albumItemClicked(pos);
				}else if(viewTag.equals(VIEW_TAG_DEL)){
					onItemClickListener.deleteItemClicked(pos);
				}
				
			}
		}
	}
}