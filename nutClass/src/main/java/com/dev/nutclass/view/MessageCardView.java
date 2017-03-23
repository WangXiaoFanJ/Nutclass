package com.dev.nutclass.view;

import java.util.List;

import org.json.JSONObject;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.adapter.AlbumDirAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.StorageUtil;
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

public class MessageCardView extends LinearLayout implements OnClickListener {

	private Context context;
	private RoundedImageView avaterIv;
	private TextView nameTv;
	private ImageView genderIv;
	private TextView attentTv;
	private TextView followTv;
	private TextView likeTv;
	private LinearLayout attendLayout;
	private LinearLayout followLayout;
	private LinearLayout likeLayout;
	private View likeLine;
	
	private UserEntity userEntity;

	public MessageCardView(Context context,ImageEntity imgEntity) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_msg, this);
		avaterIv = (RoundedImageView) this.findViewById(R.id.iv_avatar);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		followTv = (TextView) this.findViewById(R.id.tv_follow);
		likeTv = (TextView) this.findViewById(R.id.tv_like);
		followLayout=(LinearLayout)this.findViewById(R.id.ll_follow);
		likeLayout=(LinearLayout)this.findViewById(R.id.ll_like);
		likeLine=(View)this.findViewById(R.id.view_divider);
	}
	private void initListener(){
		attendLayout.setOnClickListener(this);
		followLayout.setOnClickListener(this);
		likeLayout.setOnClickListener(this);
	}
	public void updateView(UserEntity userEntity){
		this.userEntity=userEntity;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

}
