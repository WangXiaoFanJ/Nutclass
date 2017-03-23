package com.dev.nutclass.view;

import java.util.List;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.adapter.AlbumDirAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.SimpleTextEntity;
import com.dev.nutclass.utils.DialogUtils;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class SimpleTextCardView extends LinearLayout implements OnClickListener{
	
	private Context context;
	private LinearLayout rootLayout;
	private TextView descTv;
	private RecyclerItemClickListener itemClick=null;
	private SimpleTextEntity entity;
	public SimpleTextCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context=context;
		initView();
	}
	public SimpleTextCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context=context;
		initView();
	}
	private void initView(){
		LayoutInflater.from(context).inflate(R.layout.item_category, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		rootLayout=(LinearLayout)this.findViewById(R.id.ll_root);
		descTv=(TextView) this.findViewById(R.id.tv_desc);
	}
	public void updateView(final SimpleTextEntity entity,final RecyclerItemClickListener itemClick){
		this.entity=entity;
		descTv.setText(entity.getDesc());
		if(entity.getType().equals(SimpleTextEntity.TYPE_CATEGORY)){
			if(entity.isSelected()){
				descTv.setBackgroundColor(Color.TRANSPARENT);
			}else{
				descTv.setBackgroundResource(R.color.color_10);
			}
			
		}else{
			descTv.setBackgroundColor(Color.TRANSPARENT);
		}
		this.itemClick=itemClick;
		rootLayout.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(entity.getType().equals(SimpleTextEntity.TYPE_CATEGORY)){
			if(itemClick!=null){
				itemClick.onItemClick(rootLayout,entity);
			}
		}else if(entity.getType().equals(SimpleTextEntity.TYPE_CATEGORY_CHILD)){
//			DialogUtils.showToast(getContext(), "进入课程列表页");
			Intent intent=new Intent(context,CourseListActivity.class);
			intent.putExtra(Const.KEY_ID,entity.getId());
			intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_CATEGORY);
			context.startActivity(intent);
		}
	}
}
