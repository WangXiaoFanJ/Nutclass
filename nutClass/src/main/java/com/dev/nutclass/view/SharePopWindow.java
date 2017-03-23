package com.dev.nutclass.view;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.dev.nutclass.R;

public class SharePopWindow extends PopupWindow implements OnClickListener{
	
	private Context context;
	private ImageView weixinIv;
	private ImageView qqIv;
	private LinearLayout deleteLayout;
	private View rootLayout;
	private OnItemClickListener itemClickListener;
	public SharePopWindow(Context context,boolean isOwner,OnItemClickListener itemClickListener) {
		this.context = context;
		this.itemClickListener=itemClickListener;
		initView(isOwner);
	}
	private void initView(boolean isOwner){
		rootLayout = LayoutInflater.from(context).inflate(R.layout.view_share_pop, null);
		setContentView(rootLayout);
		setAnimationStyle(R.style.MyPopWindow);
		weixinIv=(ImageView)rootLayout.findViewById(R.id.iv_weixin);
		qqIv=(ImageView)rootLayout.findViewById(R.id.iv_qq);
		deleteLayout=(LinearLayout)rootLayout.findViewById(R.id.ll_delete);
		if(isOwner){
			deleteLayout.setVisibility(View.VISIBLE);
		}else {
			deleteLayout.setVisibility(View.GONE);
		}
		weixinIv.setOnClickListener(this);
		qqIv.setOnClickListener(this);
		deleteLayout.setOnClickListener(this);
		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());  
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==weixinIv){
			itemClickListener.onItemClick(0);
		}else if(v==qqIv){
			itemClickListener.onItemClick(1);
		}else if(v==deleteLayout){
			itemClickListener.onItemClick(2);
		}
	}
	public interface OnItemClickListener{
		public void onItemClick(int type);
	}
	
}
