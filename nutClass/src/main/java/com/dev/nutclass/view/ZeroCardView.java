package com.dev.nutclass.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CardListActivity;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.activity.ZeroInfoActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.ZeroCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DialogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ZeroCardView extends LinearLayout implements OnClickListener {
	private static final String TAG = "ZeroCardView";
	private Context context;
	private ImageView stateIv;
	private ImageView iconIv;
	private TextView nameTv;
	private TextView scapeTv;
	private TextView priceTv;
	private TextView stateTv;
	private TextView timeTv;
	private TextView personCountTv;

	private ZeroCardEntity entity;

	public ZeroCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ZeroCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub

	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.card_zero, this);
		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		stateIv = (ImageView) this.findViewById(R.id.iv_state);
		iconIv = (ImageView) this.findViewById(R.id.iv_icon);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		scapeTv = (TextView) this.findViewById(R.id.tv_scape);
		priceTv = (TextView) this.findViewById(R.id.tv_price);
		stateTv = (TextView) this.findViewById(R.id.tv_state);
		timeTv = (TextView) this.findViewById(R.id.tv_time);
		personCountTv = (TextView) this.findViewById(R.id.tv_pesron_count);
		setOnClickListener(this);
	}

	public void updateView(ZeroCardEntity entity) {
		this.entity = entity;
		if (entity == null) {
			setVisibility(View.GONE);
			return;
		}
		ImageLoader.getInstance().displayImage(
				entity.getZeroActivityImageUrl(), iconIv,
				ImgConfig.getPortraitOption());
		nameTv.setText(entity.getZeroActivityTitle());
		scapeTv.setText("全国通用");
		priceTv.setText("市场价："+entity.getZeroActivityPrice());
		personCountTv.setVisibility(View.GONE);
		if(entity.getType()==0){
			stateTv.setText("立即0元抢");
			stateTv.setBackgroundResource(R.drawable.radius_red_bg);
			stateIv.setImageResource(R.drawable.icon_zero_running);
			personCountTv.setVisibility(View.VISIBLE);
			timeTv.setText("还剩0天12时47分05秒 结束");
			personCountTv.setText(entity.getPersonCount()+"人参与");
		}else if(entity.getType()==1){
			stateTv.setBackgroundResource(R.drawable.radius_green_bg);
			stateTv.setText("09月17日00点开始");
			stateIv.setImageResource(R.drawable.icon_zero_next);
			timeTv.setText("距离活动开始还有0天12时47分05秒");
		}else if(entity.getType()==2){
			stateTv.setBackgroundResource(R.drawable.radius_gray_40_bg);
			stateTv.setText("已结束");
			stateIv.setImageResource(R.drawable.icon_zero_over);
			timeTv.setText("已结束");
		}
		if(entity.getFrom()==CardListActivity.TYPE_FROM_ZERO){
			timeTv.setVisibility(View.INVISIBLE);
			personCountTv.setVisibility(View.INVISIBLE);
			timeTv.setVisibility(View.INVISIBLE);
			scapeTv.setVisibility(View.GONE);
			priceTv.setVisibility(View.GONE);
		}
//		stateTv.setText("ddd");
//		timeTv.setText(entity.getzero);
//		if(entity.getT)
//		personCountTv.setOnClickListener(this);
		 

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		Intent intent = new Intent();
//		intent.setClass(context, CourseInfoActivity.class);
//		intent.putExtra(Const.KEY_ID, entity.getId());
//		context.startActivity(intent);
		if(entity.getFrom()==CardListActivity.TYPE_FROM_ZERO){//来自Me
			Intent intent = new Intent();
			intent.setClass(context, ZeroInfoActivity.class);
			intent.putExtra(Const.KEY_ID, entity.getZeroActivityId());
			context.startActivity(intent);
		}else{
			switch (entity.getType()) {
			case 0:
				Intent intent = new Intent();
				intent.setClass(context, ZeroInfoActivity.class);
				intent.putExtra(Const.KEY_ENTITY, entity);
				context.startActivity(intent);
				break;
			case 1:
				DialogUtils.showToast(context, "活动即将开始");
				break;
			case 2:
				DialogUtils.showToast(context, "活动已经结束");
				break;
			default:
				break;
			}
		}
		
		
		
	}
}
