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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CardListActivity;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CourseCardView extends LinearLayout implements OnClickListener {
	private static final String TAG = "CursorCardView";
	private Context context;
	private RoundedImageView iconIv;
	private ImageView closeIv;
	private TextView nameTv;
	private TextView priceTv;
	private TextView youhuiTv;
	private TextView locationTv;
	private TextView disTv;
	private TextView bookTv;
	private TextView buyTv;
	private TextView telTv;
	private TextView keyTv;
	private RatingBar ratingBar;
	private RelativeLayout rootLayout;
	private LinearLayout shoppingTelLayout;
	private TextView shoppingTelTv;

	private CourseCardEntity entity;

	private RecyclerItemClickListener itemClickListener;

	public CourseCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public CourseCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub

	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.card_course, this);
		rootLayout=(RelativeLayout)this.findViewById(R.id.ll_root);
		iconIv = (RoundedImageView) this.findViewById(R.id.iv_icon);
		closeIv = (ImageView) this.findViewById(R.id.iv_close);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		priceTv = (TextView) this.findViewById(R.id.tv_price);
		youhuiTv = (TextView) this.findViewById(R.id.tv_youhui);
		locationTv = (TextView) this.findViewById(R.id.tv_location);
		disTv = (TextView) this.findViewById(R.id.tv_dis);
		bookTv = (TextView) this.findViewById(R.id.tv_book);
		buyTv = (TextView) this.findViewById(R.id.tv_buy);
		telTv = (TextView) this.findViewById(R.id.tv_tel);
		keyTv = (TextView) this.findViewById(R.id.tv_key);

		ratingBar = (RatingBar) this.findViewById(R.id.rb_rating);
		
		shoppingTelTv = (TextView) this.findViewById(R.id.tv_shopping_tel);
		shoppingTelLayout=(LinearLayout)this.findViewById(R.id.ll_shopping_tel);
		setOnClickListener(this);
	}
	
	public void setBg(int resId){
		rootLayout.setBackgroundResource(resId);
	}

	public void updateView(final CourseCardEntity entity, 
			final RecyclerItemClickListener itemClickListener) {
		this.entity = entity;
		if (entity == null) {
			setVisibility(View.GONE);
			return;
		}
		ImageLoader.getInstance().displayImage(entity.getIcon(), iconIv,
				ImgConfig.getPortraitOption());
		nameTv.setText(entity.getCourseName());
		if (TextUtils.isEmpty(entity.getPriceMax())) {
			priceTv.setVisibility(View.INVISIBLE);
		} else {
			priceTv.setText(entity.getPriceMax());
			priceTv.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(entity.getPriceReturn())||entity.getPriceReturn().equals("0.00")) {
			youhuiTv.setVisibility(View.INVISIBLE);
		} else {
			youhuiTv.setText(entity.getPriceReturn());
			youhuiTv.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(entity.getAddress())) {
			locationTv.setVisibility(View.INVISIBLE);
		} else {
			locationTv.setText(entity.getAddress());
			locationTv.setVisibility(View.VISIBLE);
		}
		if (TextUtils.isEmpty(entity.getDistance())) {
			disTv.setVisibility(View.INVISIBLE);
		} else {
			disTv.setText(entity.getDistance());
			disTv.setVisibility(View.VISIBLE);
		}
		if (entity.getLevel() == 0) {
			ratingBar.setVisibility(View.INVISIBLE);
		} else {
			ratingBar.setVisibility(View.VISIBLE);
			ratingBar.setRating(entity.getLevel());
		}
		ratingBar.setVisibility(View.GONE);
		shoppingTelLayout.setVisibility(View.GONE);
		if (entity.getOrderStatus().equals(CourseCardEntity.STATUS_ORDER_0)) {
			if (entity.getFrom()==CardListActivity.TYPE_FROM_BOOK) {
				disTv.setVisibility(View.GONE);
				buyTv.setVisibility(View.GONE);
				keyTv.setVisibility(View.VISIBLE);
				telTv.setVisibility(View.VISIBLE);
				keyTv.setText(entity.getKeyStr());
				telTv.setText(entity.getTel());
			}else if(entity.getFrom()==CardListActivity.TYPE_FROM_SHOPPING){
				shoppingTelLayout.setVisibility(View.VISIBLE);
				shoppingTelTv.setText(entity.getTel());
				disTv.setVisibility(View.VISIBLE);
				disTv.setText(entity.getAddress());
				locationTv.setVisibility(View.GONE);
				bookTv.setVisibility(View.GONE);
				buyTv.setVisibility(View.GONE);
			}else{
				bookTv.setVisibility(View.VISIBLE);
				buyTv.setVisibility(View.VISIBLE);
				bookTv.setText("预约试听");
				buyTv.setText("预约/购买");
			}
			
		} else if (entity.getOrderStatus().equals(
				CourseCardEntity.STATUS_ORDER_1)) {
			bookTv.setVisibility(View.INVISIBLE);
			buyTv.setVisibility(View.VISIBLE);
			buyTv.setText("付款");
			
		} else if (entity.getOrderStatus().equals(
				CourseCardEntity.STATUS_ORDER_2)) {
			bookTv.setVisibility(View.INVISIBLE);
			buyTv.setVisibility(View.VISIBLE);
			buyTv.setText("评论");
			
		} else if (entity.getOrderStatus().equals(
				CourseCardEntity.STATUS_ORDER_3)) {
			bookTv.setVisibility(View.INVISIBLE);
			buyTv.setVisibility(View.INVISIBLE);
		}
		bookTv.setVisibility(View.GONE);
		this.itemClickListener = itemClickListener;
		if (entity.isEdit()) {
			closeIv.setVisibility(View.VISIBLE);
			closeIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 点击删除
					if (itemClickListener != null) {
						itemClickListener.onItemClick(null, entity);
					}
				}
			});
		} else {
			closeIv.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (entity.getOrderStatus().equals(CourseCardEntity.STATUS_ORDER_0)) {
			Intent intent = new Intent();
			intent.setClass(context, CourseInfoActivity.class);
			intent.putExtra(Const.KEY_ID, entity.getId());
			intent.putExtra(Const.KEY_SCHOOL_ID,entity.getSchoolId());
			context.startActivity(intent);
		} else {
			// 点击删除
			if (itemClickListener != null) {
				itemClickListener.onItemClick(null, entity);
			}
		}

	}
}
