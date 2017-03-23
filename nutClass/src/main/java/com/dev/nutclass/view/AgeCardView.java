package com.dev.nutclass.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.AgeCardEntity;
import com.dev.nutclass.entity.SimpleTextEntity;
import com.dev.nutclass.utils.DensityUtil;

public class AgeCardView extends LinearLayout {

	private Context context;
	private LinearLayout container;

	public AgeCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_age, this);
		container = (LinearLayout) this.findViewById(R.id.ll_container);
	}

	@SuppressLint("NewApi")
	public void updateView(final AgeCardEntity entity) {
		
		
		if(entity==null){
			return;
		}
		final List<SimpleTextEntity> adList=entity.getAgeList();
		
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
		
		for (int i = 0; i < adList.size(); i++) {
			LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(DensityUtil.getDisplayWidth(context)/4,
					LayoutParams.WRAP_CONTENT);
			if(i%4==0){
				rowLayout=new LinearLayout(context);
				rowLayout.setLayoutDirection(HORIZONTAL);
			}
			final SimpleTextEntity sentity=adList.get(i);
			View view=LayoutInflater.from(context).inflate(R.layout.item_two_line, null);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(context,CourseListActivity.class);
					intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_SCHOOL);
					intent.putExtra(Const.KEY_ID, "0");
					intent.putExtra(Const.KEY_TITLE,sentity.getName());
					intent.putExtra(Const.KEY_AGE, sentity.getId());
					
					context.startActivity(intent);
				}
			});
			TextView nameTv=(TextView)view.findViewById(R.id.tv_name);
			TextView descTv=(TextView)view.findViewById(R.id.tv_desc);
			nameTv.setText(adList.get(i).getName());
			descTv.setText(adList.get(i).getDesc());
			rowLayout.addView(view,cellParams);
			if(i%4==3||i==adList.size()-1){
				container.addView(rowLayout, rowParams);
			}
			
			 
		}

	}
}
