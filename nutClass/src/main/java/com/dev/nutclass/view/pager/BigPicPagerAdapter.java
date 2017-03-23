package com.dev.nutclass.view.pager;

import java.util.List;

import com.dev.nutclass.utils.LogUtil;

import android.graphics.Point;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

public class BigPicPagerAdapter extends PagerAdapter {

	private List<BigImgView> imgViewList=null;
	public static final String TAG="BigPicPagerAdapter";
	
	public BigPicPagerAdapter(List<BigImgView> imgViewList) {
		super();
		this.imgViewList = imgViewList;
	}
	
	public int getSize(){
		if(imgViewList==null){
			return 0;
		}
		return imgViewList.size();
	}

	@Override
	public int getCount() {
		if (imgViewList != null) {
			return Integer.MAX_VALUE;
		}
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		LogUtil.d(TAG, "remove position:" + position);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return super.getPageTitle(position%getSize());
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		position=position%getSize();
		if(position<0){
			position = getSize()+position; 
		}
		View view = imgViewList.get(position);
		ViewParent vp =view.getParent();
        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
            System.out.println(position+" remove");
        }else{
        	System.out.println(position+" not remove");
        }
        container.addView(view);
//		imgViewList.get(position).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.d("===","点击跳转");
//			}
//		});
		LogUtil.d(TAG, "add position:" + position);
		return view;
	}

}
