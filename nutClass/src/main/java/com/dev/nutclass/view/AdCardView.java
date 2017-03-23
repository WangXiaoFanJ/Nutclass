package com.dev.nutclass.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.utils.DensityUtil;

public class AdCardView extends LinearLayout {

	private Context context;
	private LinearLayout container;

	public AdCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_ad, this);
		container = (LinearLayout) this.findViewById(R.id.ll_container);
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
		LinearLayout rowLayout=null;
		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		rowParams.bottomMargin=5;
		for (int i = 0; i < adList.size(); i++) {
			LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(DensityUtil.getDisplayWidth(context)/4,
					DensityUtil.getDisplayWidth(context)/4);
			if(i%4==0){
				rowLayout=new LinearLayout(context);
				rowLayout.setLayoutDirection(HORIZONTAL);
			}
			if(i%4==3){
				cellParams.rightMargin=0;
			}else{
				cellParams.rightMargin=5;
			}
			AdItemView itemView = new AdItemView(context, adList.get(i),i);
			rowLayout.addView(itemView,cellParams);
			if(i%4==3||i==adList.size()-1){
				container.addView(rowLayout, rowParams);
			}
			
			 
		}

	}
}
