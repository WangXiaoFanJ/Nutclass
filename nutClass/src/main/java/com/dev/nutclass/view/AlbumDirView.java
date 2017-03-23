package com.dev.nutclass.view;

import java.util.List;

import com.dev.nutclass.R;
import com.dev.nutclass.adapter.AlbumDirAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.entity.ImageEntity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class AlbumDirView extends LinearLayout {
	
	private Context context;
	private RecyclerView recyclerView;
	private LinearLayout rootLayout;
	public boolean isShow=false;
	public boolean isRunning=false;
	private DirAnimatorListener showAnimatorListener;
	private DirAnimatorListener hideAnimatorListener;
	private int duration=500;
	private float offsetY=-500f;
	private AlbumDirAdapter dirAdapter=null;

	public AlbumDirView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context=context;
		initView();
	}
	private void initView(){
		LayoutInflater.from(context).inflate(R.layout.view_album_dir, this);
		rootLayout=(LinearLayout)this.findViewById(R.id.ll_root);
		recyclerView=(RecyclerView)this.findViewById(R.id.id_recyclerview);
		recyclerView.setTranslationY(offsetY);
		rootLayout.setAlpha(0.0f);
		showAnimatorListener=new DirAnimatorListener(true);
		hideAnimatorListener=new DirAnimatorListener(false);
		setVisibility(View.GONE);
	}
	public void update(List<ImageEntity> list,RecyclerItemClickListener itemClick){
		dirAdapter=new AlbumDirAdapter(context, list,itemClick);
		recyclerView.setAdapter(dirAdapter);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
		mLayoutManager.setOrientation(LinearLayout.VERTICAL);
		recyclerView.setLayoutManager(mLayoutManager);
	}
	public void show(){
		
		final ObjectAnimator anim = ObjectAnimator//
				.ofFloat(recyclerView, "translationY", offsetY, 0.0F);
		
		final ObjectAnimator animAlpha = ObjectAnimator//
				.ofFloat(rootLayout, "alpha", 0.0F, 1.0F);
		AnimatorSet set=new AnimatorSet();
		set.playTogether(anim,animAlpha);
		set.setDuration(duration);
		set.addListener(showAnimatorListener);
		set.start();
		isShow=true;
	}
	public void hide(){
		final ObjectAnimator anim = ObjectAnimator//
				.ofFloat(recyclerView, "translationY", 0.0F, offsetY);
		final ObjectAnimator animAlpha = ObjectAnimator//
				.ofFloat(rootLayout, "alpha", 1.0F, 0.0F);
		AnimatorSet set=new AnimatorSet();
		set.playTogether(anim,animAlpha);
		set.setDuration(duration);
		set.addListener(hideAnimatorListener);
		set.start();
		isShow=false;
	}
	class DirAnimatorListener implements AnimatorListener{
		private boolean isShow=false;
		public DirAnimatorListener(boolean isShow){
			this.isShow=isShow;
		}

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			isRunning=true;
			if(isShow){
				setVisibility(View.VISIBLE);
			}else{
				setVisibility(View.VISIBLE);
			}
		}
		
		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationEnd(Animator animation) {
			// TODO Auto-generated method stub
			isRunning=false;
			if(isShow){
				setVisibility(View.VISIBLE);
			}else{
				setVisibility(View.GONE);
			}
		}
		
		@Override
		public void onAnimationCancel(Animator animation) {
			// TODO Auto-generated method stub
			isRunning=false;
		}
		
	}

}
