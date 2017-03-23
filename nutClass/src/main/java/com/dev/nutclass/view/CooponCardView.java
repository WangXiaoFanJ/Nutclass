package com.dev.nutclass.view;

import java.util.List;

import org.json.JSONObject;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.ActionInfoActivity;
import com.dev.nutclass.activity.ActionListActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.adapter.AlbumDirAdapter;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.ActionCardEntity;
import com.dev.nutclass.entity.CooponCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DialogUtils;
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
import android.widget.LinearLayout.LayoutParams;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class CooponCardView extends LinearLayout implements OnClickListener {

	private Context context;
	private TextView titleTv;
	private TextView priceTv;
	private TextView limitTv;
	private TextView useTimeTv;
	private TextView sourceTv;
	private TextView limitStrTv;
	private CooponCardEntity entity;
	private TextView cdBonusTv;

	public CooponCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		this.context = context;
		initView();
		initListener();
	}
	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.card_coopon, this);
		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		titleTv = (TextView) this.findViewById(R.id.tv_title);
		priceTv = (TextView) this.findViewById(R.id.tv_price);
		limitTv = (TextView) this.findViewById(R.id.tv_limit);
		useTimeTv = (TextView) this.findViewById(R.id.tv_time);
		sourceTv = (TextView) this.findViewById(R.id.tv_source);
		limitStrTv = (TextView) this.findViewById(R.id.tv_limit_str);
		cdBonusTv = (TextView) this.findViewById(R.id.tv_zd_bonus);
	}
	private void initListener(){
		setOnClickListener(this);
	}
	public void updateView(CooponCardEntity entity){
		this.entity=entity;
		if(entity==null){
			return;
		}
		titleTv.setText(entity.getName());
		priceTv.setText(entity.getMoney());
		limitTv.setText(entity.getLimit());
		useTimeTv.setText(entity.getUseTime());
		sourceTv.setText(entity.getUseSource());
		limitStrTv.setText(entity.getBonus_type_name());
		cdBonusTv.setText(entity.getZd_goods_bonus());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		DialogUtils.showToast(context, "热门活动详情");
//		Intent intent=new Intent(context,ActionInfoActivity.class);
//		intent.putExtra(Const.KEY_ID, entity.getId());
//		context.startActivity(intent);
	}
	 

}
