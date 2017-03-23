package com.dev.nutclass.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.utils.DensityUtil;

public class AdCard004View extends LinearLayout {

	private Context context;
	private LinearLayout container;
	private ImageView leftIv;
	private ImageView rightIv;
	private HorizontalScrollView scrollView;
	private int cellX=0;
	

	public AdCard004View(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_ad_004, this);
		container = (LinearLayout) this.findViewById(R.id.ll_container);
		leftIv=(ImageView)this.findViewById(R.id.iv_left);
		leftIv.setBackgroundColor(Color.TRANSPARENT);
		rightIv=(ImageView)this.findViewById(R.id.iv_right);
		rightIv.setBackgroundColor(Color.TRANSPARENT);
		scrollView=(HorizontalScrollView)this.findViewById(R.id.hs_container);
		cellX=DensityUtil.getDisplayWidth(context)/5;
	}

	@SuppressLint("NewApi")
	public void updateView(AdCardEntity entity) {
		List<ImageEntity> adList=null;
		
		if(entity!=null){
			adList=entity.getList();
		}else{
			return;
		}
		if(adList==null||adList.size()<=0){
			return;
		}
		container.removeAllViews();

//		if (entity != null && entity.getList() != null) {
//			for (int i = 0; i < entity.getList().size(); i++) {
//				AdCardItemView itemView = new AdCardItemView(context, entity
//						.getList().get(i),i);
//				container.addView(itemView, params);
//			}
//		}
		final int count=adList.size();
		for (int i = 0; i < adList.size(); i++) {
			LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams((DensityUtil.getDisplayWidth(context))/4,
					LayoutParams.WRAP_CONTENT);
			
			AdItemView itemView = new AdItemView(context, adList.get(i),i);
			container.addView(itemView,cellParams);

		}
		leftIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int x=scrollView.getScrollX();
				int num=x/cellX;
				if(x%cellX==0&&num!=0){
					scrollView.scrollTo((num-1)*cellX, 0);
				}else{
					scrollView.scrollTo(num*cellX, 0);
				}
				
				
			}
		});
		rightIv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int x=scrollView.getScrollX();
				int width=DensityUtil.getDisplayWidth(context)-leftIv.getWidth()-rightIv.getWidth();
				int num=(x+width)/cellX;
				if(num!=count){
					scrollView.scrollTo((num+1)*cellX-width, 0);
				}else{
					scrollView.scrollTo(num*cellX-width, 0);
				}
				
			}
		});

	}
}
