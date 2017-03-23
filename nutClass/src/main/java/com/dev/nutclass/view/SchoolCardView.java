package com.dev.nutclass.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.SchoolCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SchoolCardView extends LinearLayout implements OnClickListener {
	private static final String TAG = "SchoolCardView";
	private Context context;
	private RoundedImageView iconIv;
	private TextView nameTv;
	private TextView youhuiTv;
	private TextView disTv;
	private TextView tagTv;
	private TextView scopeTv;
	private TextView flag1Iv;
	private TextView flag2Iv;
	private TextView flag3Iv;
	private TextView flag4Iv;
	private LinearLayout llFlags;

	private SchoolCardEntity entity;


	public SchoolCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public SchoolCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub

	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.card_school, this);
		iconIv = (RoundedImageView) this.findViewById(R.id.iv_icon);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		youhuiTv = (TextView) this.findViewById(R.id.tv_price);
		disTv = (TextView) this.findViewById(R.id.tv_distance);
		tagTv = (TextView) this.findViewById(R.id.tv_tag);
		scopeTv = (TextView) this.findViewById(R.id.tv_scope);
		flag1Iv = (TextView) this.findViewById(R.id.iv_flag1);
		flag2Iv = (TextView) this.findViewById(R.id.iv_flag2);
		flag3Iv = (TextView) this.findViewById(R.id.iv_flag3);
		flag4Iv = (TextView) this.findViewById(R.id.iv_flag4);
		llFlags= (LinearLayout) this.findViewById(R.id.ll_flag);
		setOnClickListener(this);
	}

	public void updateView(SchoolCardEntity entity) {
		this.entity = entity;
		if (entity == null) {
			setVisibility(View.GONE);
			return;
		}
		ImageLoader.getInstance().displayImage(entity.getIcon(), iconIv,
				ImgConfig.getPortraitOption());
		nameTv.setText(entity.getName());
		
		if (TextUtils.isEmpty(entity.getPriceReturn())) {
			youhuiTv.setVisibility(View.INVISIBLE);
		} else {
			youhuiTv.setText(entity.getPriceReturn());
			youhuiTv.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(entity.getDistance())) {
			disTv.setVisibility(View.INVISIBLE);
		} else {
			disTv.setText(entity.getDistance());
			disTv.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(entity.getTag())) {
			tagTv.setVisibility(View.INVISIBLE);
		} else {
			tagTv.setText(entity.getTag());
			tagTv.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(entity.getScope())) {
			scopeTv.setVisibility(View.INVISIBLE);
		} else {
			scopeTv.setText(entity.getScope());
			scopeTv.setVisibility(View.VISIBLE);
		}
		scopeTv.setVisibility(View.GONE);
		
		updateFlag();
		
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(context, CourseListActivity.class);
		intent.putExtra(Const.KEY_ID, entity.getId());
		intent.putExtra(Const.KEY_TYPE, CourseListActivity.TYPE_FROM_COURSE);
		context.startActivity(intent);

	}
	private void updateFlag(){
		if(entity.getFlag1()==1){
			flag1Iv.setVisibility(View.VISIBLE);
			flag1Iv.setText(entity.getPriceReturn());
		}else{
			flag1Iv.setVisibility(View.GONE);
		}
		if(entity.getFlag2()==1){
			flag2Iv.setVisibility(View.VISIBLE);
			flag2Iv.setText("专享课程");
		}else{
			flag2Iv.setVisibility(View.GONE);
		}
		if(entity.getFlag3()==1){
			flag3Iv.setVisibility(View.VISIBLE);
		}else{
			flag3Iv.setVisibility(View.GONE);
		}
		if(entity.getFlag4()==1){
			flag4Iv.setVisibility(View.VISIBLE);
		}else{
			flag4Iv.setVisibility(View.GONE);
		}
		if(entity.getFlag1()==0&&entity.getFlag2()==0&&entity.getFlag3()==0&&entity.getFlag4()==0){
			llFlags.setVisibility(View.GONE);
		}
	}
}
