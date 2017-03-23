package com.dev.nutclass.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * adapter中包含一个list，有更多数据 可以继承该类
 * 
 * @author LJ
 * @param <T>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

	protected List<T> dataList;
	protected Activity mActivity;
	protected LayoutInflater mInflater;
	private int total = 0;

	public BaseListAdapter(Context context) {
		mActivity = (Activity) context;
		mInflater = LayoutInflater.from(mActivity);
		dataList = createList();
	}

	@Override
	public final int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public final T getItem(int position) {
		if (position < dataList.size()) {
			return dataList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public final long getItemId(int position) {
		if (position == dataList.size()) {
			return 0;
		}
		return position;
	}

	/**
	 * 设置数据列表
	 * 
	 * @param datalist
	 */
	public void setList(List<T> datalist) {
		if (dataList == null) {
			dataList = createList();
		}

		if (datalist == null) {
			return;
		}

		if (dataList.size() > 0) {
			dataList.clear();
		}
		dataList.addAll(datalist);
	}

	/**
	 * 添加数据列表
	 * 
	 * @param datalist
	 */
	public void addList(List<T> datalist) {
		if (dataList == null) {
			dataList = createList();
		}
		if (datalist != null) {
			dataList.addAll(datalist);
		}
	}

	public List<T> getDataList() {
		return this.dataList;
	}

	/**
	 * 获得当前的数据数目
	 * 
	 * @return
	 */
	public int getDataSize() {
		int size = 0;
		if (dataList != null) {
			size = dataList.size();
		}
		return size;
	}

	/**
	 * 清空adapter的数据
	 */
	public void clear() {
		total = 0;
		if (dataList != null) {
			dataList.clear();
		}
	}


	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		return getItemView(position, convertView, parent);
	}

	/**
	 * 用于更新我们想要更新的item
	 * 
	 * @param itemIndex
	 *            想更新item的下标
	 * **/
	public void updateView(ListView listView, int itemIndex) {

		if (listView == null) {// 可能已经被回收
			return;
		}
		// 得到第1个可显示控件的位置
		int visiblePosition = listView.getFirstVisiblePosition();
		// 判断要更新的item是否在当前显示的范围内
		if (itemIndex < visiblePosition - listView.getHeaderViewsCount()
				|| itemIndex > listView.getLastVisiblePosition()
						- listView.getFooterViewsCount()) {
			return;
		}
		// 得到你需要更新item的View
		View view = listView.getChildAt(itemIndex - visiblePosition
				+ listView.getHeaderViewsCount());
		getItemView(itemIndex, view, null);
	}

	/**
	 * 重写返回列表项中的view，更多、加载中状态由父类控制
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	protected abstract View getItemView(int position, View convertView,
			ViewGroup parent);

	protected abstract List<T> createList();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}
