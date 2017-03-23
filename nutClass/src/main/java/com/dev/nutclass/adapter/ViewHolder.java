package com.dev.nutclass.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



/** Viewholder的简化
 * @ClassName: ViewHolder
 * @Description: TODO
 * @author Mr.LJ
 * @date 2014-11-04 上午9:56:29
 */
public class ViewHolder {
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;
 
	private ViewHolder(Context context, ViewGroup parent, int layoutId,int position) {
		
		this.mPosition = position;
		this.mContext=context;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}
	public View getConvertView() {
		return mConvertView;
	}
	
	public int getPosition() {
		return mPosition;
	}
	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}
}
