package com.dev.nutclass.view;

import java.util.List;

import org.json.JSONObject;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.ActionListActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.activity.MarketListActivity;
import com.dev.nutclass.activity.WebViewActivity;
import com.dev.nutclass.adapter.AlbumDirAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.HeadCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.StorageUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class HeadCardView extends LinearLayout implements OnClickListener {

	private Context context;
	private LinearLayout leftLayout;
	private LinearLayout rightLayout;
	private HeadCardEntity entity;
	

	public HeadCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		
		initView();
		initData();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_head, this);
		leftLayout = (LinearLayout) this.findViewById(R.id.ll_left);
		rightLayout = (LinearLayout) this.findViewById(R.id.ll_right);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
	}
	private void initListener(){
		leftLayout.setOnClickListener(this);
		rightLayout.setOnClickListener(this);
	}
	
	public void updateView(HeadCardEntity entity){
		this.entity=entity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==leftLayout){
			if(Util.checkLogin(context)){
				Intent intent=new Intent(context,WebViewActivity.class);
				intent.putExtra(Const.KEY_URL, HttpUtil.addToken(entity.getH5Url()));
				intent.putExtra(Const.KEY_TITLE, "问卷调查");
				context.startActivity(intent);
			}
		}else if(v==rightLayout){
			Intent intent=new Intent(context,CourseListActivity.class);
			intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_SCHOOL);
			intent.putExtra(Const.KEY_ID, "");//周边ID传空
			context.startActivity(intent);
		}
		
		
	}
	private void initData(){
	}

}
