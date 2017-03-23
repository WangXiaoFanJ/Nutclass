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
import com.dev.nutclass.activity.CooponListActivity;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.adapter.RecyclerItemClickListener;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ConversionCodeEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

public class CoursePayCardView extends LinearLayout implements OnClickListener {
	private static final String TAG = "CursorCardView";
	private Context context;
	private RoundedImageView iconIv;
	private ImageView closeIv;
	private TextView nameTv;
	
	private TextView locationTv;
	private TextView disTv;
	private TextView bookTv;
	private TextView buyTv;
	private TextView telTv;
	private TextView keyTv;
	private RatingBar ratingBar;
	
	private TextView priceTv;
	private TextView youhuiTv;
	private TextView orderIdTv;
	private TextView orderTimeTv;
	private TextView couponTv;
	private TextView deleteTv;
	 

	private CourseCardEntity entity;

	private RecyclerItemClickListener itemClickListener;

	public CoursePayCardView(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public CoursePayCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub

	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.card_course_pay, this);
		iconIv = (RoundedImageView) this.findViewById(R.id.iv_icon);
		closeIv = (ImageView) this.findViewById(R.id.iv_close);
		nameTv = (TextView) this.findViewById(R.id.tv_name);
		
		locationTv = (TextView) this.findViewById(R.id.tv_location);
		disTv = (TextView) this.findViewById(R.id.tv_dis);
		bookTv = (TextView) this.findViewById(R.id.tv_book);
		buyTv = (TextView) this.findViewById(R.id.tv_buy);
		telTv = (TextView) this.findViewById(R.id.tv_tel);
		keyTv = (TextView) this.findViewById(R.id.tv_key);
		
		priceTv = (TextView) this.findViewById(R.id.tv_price_total);
		youhuiTv = (TextView) this.findViewById(R.id.tv_return_card);
		orderIdTv = (TextView) this.findViewById(R.id.tv_order_id);
		orderTimeTv = (TextView) this.findViewById(R.id.tv_order_time);
		couponTv = (TextView) this.findViewById(R.id.tv_coupon);
		ratingBar = (RatingBar) this.findViewById(R.id.rb_rating);
		deleteTv = (TextView) this.findViewById(R.id.tv_delete);
		setOnClickListener(this);
	}

	public void updateView(CourseCardEntity entity, final int position,
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
		if (TextUtils.isEmpty(entity.getPriceReturn())) {
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
		locationTv.setVisibility(View.GONE);
		if (TextUtils.isEmpty(entity.getDistance())) {
			disTv.setVisibility(View.INVISIBLE);
		} else {
			disTv.setText(entity.getDistance()+"m");
			disTv.setVisibility(View.VISIBLE);
		}
		disTv.setVisibility(View.GONE);
		if (entity.getLevel() == 0) {
			ratingBar.setVisibility(View.INVISIBLE);
		} else {
			ratingBar.setVisibility(View.VISIBLE);
			ratingBar.setRating(entity.getLevel());
		}
		ratingBar.setVisibility(View.GONE);
		if (entity.getOrderStatus().equals(
				CourseCardEntity.STATUS_ORDER_1)) {
			bookTv.setVisibility(View.GONE);
			buyTv.setVisibility(View.VISIBLE);
			buyTv.setText("支付");
			deleteTv.setVisibility(View.VISIBLE);
			deleteTv.setText("删除");
			deleteTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//删除课程
					deleteCourse();
				}
			});
			
		} else if (entity.getOrderStatus().equals(
				CourseCardEntity.STATUS_ORDER_2)) {
			bookTv.setVisibility(View.GONE);
			buyTv.setVisibility(View.VISIBLE);
			buyTv.setText("评论");
			
		} else if (entity.getOrderStatus().equals(
				CourseCardEntity.STATUS_ORDER_3)) {
			bookTv.setVisibility(View.GONE);
			buyTv.setVisibility(View.INVISIBLE);
		}
		bookTv.setVisibility(View.GONE);
		
		this.itemClickListener = itemClickListener;
		closeIv.setVisibility(View.GONE);
		
		orderIdTv.setText(entity.getOrderSn());
		orderTimeTv.setText(entity.getOrderTime());
		couponTv.setText(entity.getCoupon());

		

	}

	private void deleteCourse() {
		String url = String.format(HttpUtil.addBaseGetParams( UrlConst.ORDER_REMOVE_URL),entity.getOrderId());
		OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {

			}

			@Override
			public void onResponse(String response) {
				LogUtil.d(TAG, "response=" + response);
				Gson gson = new Gson();
				//该conversionCodeEntity为通用验证entity;
				ConversionCodeEntity conversionCodeEntity = gson.fromJson(response, ConversionCodeEntity.class);
				if(conversionCodeEntity!=null){
					String info = conversionCodeEntity.getInfo();
					if(conversionCodeEntity.getInfo()!=null){
						DialogUtils.showToast(context,info);
						((CardListActivity)context).refreshAdapter();
					}
				}
			}
		});
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
