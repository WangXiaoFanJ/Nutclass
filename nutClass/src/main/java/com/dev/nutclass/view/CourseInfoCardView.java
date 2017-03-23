package com.dev.nutclass.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.CooponListActivity;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.activity.CourseIntroduceActivity;
import com.dev.nutclass.activity.NavActivity;
import com.dev.nutclass.activity.WebViewActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ConversionCodeEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.FQEntity;
import com.dev.nutclass.entity.SchoolEntity;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.okhttp.Request;

public class CourseInfoCardView extends LinearLayout implements OnClickListener {
    private static final String TAG = "CourseInfoCardView";
    private Context context;
    private LinearLayout bannerLayout;
    private TextView nameTv;
    private RatingBar ratingBar;
    private TextView priceTv;
    private TextView youhuiTv;
    private LinearLayout courseNumLayout;
    private TextView schoolNameTv;
    private TextView schoolAddressTv;
    private TextView pay1Tv;
    private TextView pay2Tv;
    private TextView pay3Tv;
    private TextView bonusInfo;
    private TextView getBonusTv;
    private TextView actionMarkTv;

    private ImageView navIv;
    private ImageView actionMarkIv;

    private LinearLayout imgLayout;
    private LinearLayout infoLayout;
    private LinearLayout flagLayout;
    private LinearLayout courseMoreLayout;
    private LinearLayout courseOtherLayout;
    private LinearLayout actionMarkLayout;
    private LinearLayout plusPriceBuyLayout;
    private LinearLayout plusGoodsMoreLayout;
    private LinearLayout plusGoodsFoldLayout;
    //	private TextView descTv;
    private List<FQEntity> fqList;
    private String schoolHour;
    private List<String> plusGoodsIdList;
    private CourseCardEntity entity;
    private int cardId = 1;

    public CourseInfoCardView(Context context) {
        super(context, null);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public CourseInfoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        // TODO Auto-generated constructor stub
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.card_course_info, this);
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        bannerLayout = (LinearLayout) this.findViewById(R.id.ll_banner);
        nameTv = (TextView) this.findViewById(R.id.tv_name);
        ratingBar = (RatingBar) this.findViewById(R.id.rb_rating);
        priceTv = (TextView) this.findViewById(R.id.tv_price);
        youhuiTv = (TextView) this.findViewById(R.id.tv_youhui);
        schoolNameTv = (TextView) this.findViewById(R.id.tv_school_name);
        schoolAddressTv = (TextView) this.findViewById(R.id.tv_school_address);
        pay1Tv = (TextView) this.findViewById(R.id.tv_pay1);
        pay2Tv = (TextView) this.findViewById(R.id.tv_pay2);
        pay3Tv = (TextView) this.findViewById(R.id.tv_pay3);
//		bonusInfo = (TextView) this.findViewById(R.id.tv_bonus_name);
        getBonusTv = (TextView) this.findViewById(R.id.tv_get_bonus);
        actionMarkTv = (TextView) this.findViewById(R.id.tv_mark_action);

        courseNumLayout = (LinearLayout) this.findViewById(R.id.ll_course_num);
        imgLayout = (LinearLayout) this.findViewById(R.id.ll_desc_container);
        infoLayout = (LinearLayout) this.findViewById(R.id.ll_course_info);
        flagLayout = (LinearLayout) this.findViewById(R.id.ll_flag);
        courseMoreLayout = (LinearLayout) this.findViewById(R.id.ll_course_more);
        courseOtherLayout = (LinearLayout) this.findViewById(R.id.ll_course_other);
        actionMarkLayout = (LinearLayout) this.findViewById(R.id.ll_mark_action);
        plusPriceBuyLayout = (LinearLayout) this.findViewById(R.id.ll_plus_price_buy);
        plusGoodsMoreLayout = (LinearLayout) this.findViewById(R.id.ll_plus_price_more);
        plusGoodsFoldLayout = (LinearLayout) this.findViewById(R.id.ll_fold_plus_price);
//		descTv=(TextView)this.findViewById(R.id.tv_desc);
        navIv = (ImageView) this.findViewById(R.id.iv_nav);
        actionMarkIv = (ImageView) this.findViewById(R.id.iv_mark_action);
        plusGoodsIdList = new ArrayList<>();
    }

    @SuppressLint("NewApi")
    public void updateView(CourseCardEntity entity) {
        this.entity = entity;
        if (entity == null) {
            setVisibility(View.GONE);
            return;
        }
//		if(entity.getDescImgList()!=null&&entity.getDescImgList().size()>0){
//			for(int i=0;i<entity.getDescImgList().size();i++){
//				ImageView img=new ImageView(getContext());
//				img.setScaleType(ImageView.ScaleType.FIT_XY);
//				LinearLayout.LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.height_banner));
//				params.bottomMargin=10;
//				ImageLoader.getInstance().displayImage(entity.getDescImgList().get(i), img);
//				imgLayout.addView(img,params);
//			}
//		}
//		if(TextUtil.isNotNull(entity.getDesc())){
//			descTv.setText(entity.getDesc());
//			descTv.setVisibility(View.VISIBLE);
//		}else{
//			descTv.setVisibility(View.GONE);
//		}
        //请求分期实体
        reqFQEntity();
        imgLayout.setVisibility(View.GONE);
//		descTv.setVisibility(View.GONE);
        nameTv.setText(entity.getCourseName());
        priceTv.setText("￥" + entity.getPriceMax());
        youhuiTv.setText("赠送" + entity.getPriceReturn() + "元购物卡");

        if (entity.getIs_pro().equals("6")) {
            youhuiTv.setVisibility(View.GONE);
            getBonusTv.setVisibility(View.VISIBLE);
            actionMarkLayout.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(entity.getIs_pro_img(), actionMarkIv);
            actionMarkTv.setText(entity.getIs_pro_text());
        } else {
            actionMarkLayout.setVisibility(View.GONE);
            youhuiTv.setVisibility(View.VISIBLE);
            getBonusTv.setVisibility(View.GONE);
            if (entity.getIs_pro().equals("1")) {
                actionMarkLayout.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(entity.getIs_pro_img(), actionMarkIv);
//				Bitmap bitmap= ImageLoader.getInstance().loadImageSync(entity.getIs_pro_img());
//				Drawable drawable =new BitmapDrawable(bitmap);
///// 这一步必须要做,否则不会显示.
//				drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//				actionMarkTv.setCompoundDrawables(drawable,null,null,null);
                actionMarkTv.setText(entity.getIs_pro_text());
            }
        }

        ratingBar.setRating(entity.getLevel());
        bannerLayout.removeAllViews();
        BannerCardView bannerView = new BannerCardView(context);
        bannerView.updateView(entity.getBannerCardEntity(), 2);
        bannerLayout.addView(bannerView);
        flagLayout.removeAllViews();
//		bonusInfo.setText(entity.getBonus_name());

        List<View> viewList = new ArrayList<View>();
        if (TextUtil.isNotNull(entity.getAge())) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_flag_age, null);
            TextView tv = (TextView) view.findViewById(R.id.tv_age);
            tv.setText(entity.getAge());
            viewList.add(view);
        }
        if (entity.getFlag1() == 1) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.icon_detail_1);
            viewList.add(iv);

        }

        if (entity.getFlag2() == 1) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.icon_detail_2);
            viewList.add(iv);
        }
        if (entity.getFlag3() == 1) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.icon_detail_3);
            viewList.add(iv);
        }
        if (entity.getFlag4() == 1) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.icon_detail_4);
            viewList.add(iv);
        }
        if (entity.getFlag5() == 1) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.icon_detail_5);
            viewList.add(iv);
        }
        LinearLayout rowLayout1 = null;
        LinearLayout.LayoutParams rowParams = null;
        int size = (DensityUtil.getDisplayWidth(context) - 60) / 3;
        for (int i = 0; i < viewList.size(); i++) {
            if (i % 3 == 0) {
                rowLayout1 = new LinearLayout(context);
                rowLayout1.setLayoutDirection(HORIZONTAL);
                rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT);

            }
            LinearLayout.LayoutParams cellParams = new LinearLayout.LayoutParams(size, LayoutParams.WRAP_CONTENT);
            rowLayout1.addView(viewList.get(i), cellParams);
            if (i % 3 == 0) {
                flagLayout.addView(rowLayout1, rowParams);
            }
        }


        if (flagLayout.getChildCount() == 0) {
            flagLayout.setVisibility(View.GONE);
        } else {
            flagLayout.setVisibility(View.VISIBLE);
        }

        if (entity.getPlusPriceBuyList() == null || entity.getPlusPriceBuyList().size() == 0) {
            plusPriceBuyLayout.setVisibility(View.GONE);
            plusGoodsFoldLayout.setVisibility(View.GONE);
            plusGoodsMoreLayout.setVisibility(View.GONE);
        } else {
            plusGoodsIdList.clear();
            plusGoodsFoldLayout.setVisibility(View.GONE);
            plusGoodsMoreLayout.setVisibility(View.VISIBLE);
            plusPriceBuyLayout.removeAllViews();
            for(int i = 0;i<2;i++){
               final String plusGoodsUrl =  entity.getPlusPriceBuyList().get(i).getGoodsDetailUrl();
                PlusPriceBuyView view = new PlusPriceBuyView(context);
                view.updateView(entity.getPlusPriceBuyList().get(i), new PlusPriceBuyView.CheckBoxClick() {
                    @Override
                    public void yesCheckBoxClick(String id) {
                            plusGoodsIdList.add(id);
                    }

                    @Override
                    public void noCheckBoxClick(String id) {
                        if(plusGoodsIdList!=null &&plusGoodsIdList.size()>0){
                            for(int i =0;i<plusGoodsIdList.size();i++){
                                if(plusGoodsIdList.get(i).equals(id)){
                                    plusGoodsIdList.remove(i);
                                }
                            }
                        }
                    }
                });
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtil.isNotNull(plusGoodsUrl)){
                            Intent intent = new Intent(context, WebViewActivity.class);
                            String webUrl = plusGoodsUrl;
                            intent.putExtra(Const.KEY_URL, webUrl);
                            intent.putExtra(Const.KEY_TITLE, "加价购商品");
                            intent.putExtra(Const.KEY_FROM, "course");
                            context.startActivity(intent);
                        }
                    }
                });
                plusPriceBuyLayout.addView(view);
            }

//			for(int i=0;i<entity.getPlusPriceBuyList().size();i++){
//				PlusPriceBuyView view = new PlusPriceBuyView(context);
//				view.updateView(entity.getPlusPriceBuyList().get(i));
//				plusPriceBuyLayout.addView(view);
//			}
        }
        if (entity.getCourseList() == null || entity.getCourseList().size() == 0) {
            courseMoreLayout.setVisibility(View.GONE);
            courseOtherLayout.setVisibility(View.GONE);
        } else {
            courseMoreLayout.setVisibility(View.VISIBLE);
            courseOtherLayout.setVisibility(View.VISIBLE);
            courseOtherLayout.removeAllViews();
            for (int i = 0; i < entity.getCourseList().size(); i++) {
                CourseCardView view = new CourseCardView(context);

                view.setBg(R.color.bg_main_activity_white);
                view.updateView(entity.getCourseList().get(i), null);
                final int finalI = i;
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent();
                        intent.setClass(context, CourseInfoActivity.class);
                        intent.putExtra(Const.KEY_ID, CourseInfoCardView.this.entity.getCourseList().get(finalI).getId());
                        context.startActivity(intent);
                    }
                });
                courseOtherLayout.addView(view);
                View line = new View(context);
                line.setBackgroundResource(R.color.list_divider_line);
                LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, 2);
                courseOtherLayout.addView(line, lineParams);

            }
        }


        selectSchool(0);
        pay1Tv.setOnClickListener(this);
        pay2Tv.setOnClickListener(this);
        pay3Tv.setOnClickListener(this);
        schoolNameTv.setOnClickListener(this);
        navIv.setOnClickListener(this);
        infoLayout.setOnClickListener(this);
        courseMoreLayout.setOnClickListener(this);
        plusGoodsMoreLayout.setOnClickListener(this);
        plusGoodsFoldLayout.setOnClickListener(this);
        getBonusTv.setOnClickListener(this);
        selectPay(1);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v == schoolNameTv || v == navIv) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // 指定下拉列表的显示数据
            final String[] cities = new String[entity.getSchoolList().size()];
            for (int i = 0; i < entity.getSchoolList().size(); i++) {
                cities[i] = entity.getSchoolList().get(i).getSchoolName();
            }
            // 设置一个下拉的列表选择项
            builder.setItems(cities, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectSchool(which);
                }
            });
            builder.show();
        } else if (v == infoLayout) {
//			Intent intent=new Intent(context,CourseIntroduceActivity.class);
//			intent.putExtra(Const.KEY_ENTITY, entity);
//			context.startActivity(intent);
            Intent intent = new Intent(context, WebViewActivity.class);
            String webUrl = UrlConst.COURSE_DETAIL_WEB_URL + entity.getId();
            intent.putExtra(Const.KEY_URL, webUrl);
            intent.putExtra(Const.KEY_TITLE, "课程详情");
            intent.putExtra(Const.KEY_FROM, "course");
            context.startActivity(intent);
        } else if (v == plusGoodsMoreLayout) {
            plusGoodsIdList.clear();
            plusPriceBuyLayout.removeAllViews();
            plusGoodsMoreLayout.setVisibility(View.GONE);
            plusGoodsFoldLayout.setVisibility(View.VISIBLE);
            for(int i=0;i<entity.getPlusPriceBuyList().size();i++){
                final String plusGoodsUrl =  entity.getPlusPriceBuyList().get(i).getGoodsDetailUrl();
                PlusPriceBuyView view = new PlusPriceBuyView(context);
                view.updateView(entity.getPlusPriceBuyList().get(i), new PlusPriceBuyView.CheckBoxClick() {
                    @Override
                    public void yesCheckBoxClick(String id) {
                        Log.d("===","id:"+id);
//                        if(plusGoodsIdList!=null && plusGoodsIdList.size()>0){
//                            for(int i =0;i<plusGoodsIdList.size();i++){
//                                if(!plusGoodsIdList.get(i).equals(id)){
//                                    plusGoodsIdList.add(id);
//                                }
//                            }
//                        }else{
                            plusGoodsIdList.add(id);
//                        }
                        Log.d("===","plusGoodsList:"+plusGoodsIdList.size());
                    }

                    @Override
                    public void noCheckBoxClick(String id) {
                        if(plusGoodsIdList!=null &&plusGoodsIdList.size()>0){
                            for(int i =0;i<plusGoodsIdList.size();i++){
                                if(plusGoodsIdList.get(i).equals(id)){
                                    plusGoodsIdList.remove(i);
                                }
                            }
                        }
                    }
                });
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(TextUtil.isNotNull(plusGoodsUrl)){
                            Intent intent = new Intent(context, WebViewActivity.class);
                            String webUrl = plusGoodsUrl;
                            Log.d("===","webURl:"+webUrl);
                            intent.putExtra(Const.KEY_URL, webUrl);
                            intent.putExtra(Const.KEY_TITLE, "加价购商品");
                            intent.putExtra(Const.KEY_FROM, "course");
                            context.startActivity(intent);
                        }
                    }
                });
                plusPriceBuyLayout.addView(view);
            }
        }else if(v == plusGoodsFoldLayout){
            plusGoodsIdList.clear();
            plusGoodsMoreLayout.setVisibility(View.VISIBLE);
            plusGoodsFoldLayout.setVisibility(View.GONE);
            plusPriceBuyLayout.removeAllViews();
            for(int i=0;i<2;i++) {
                PlusPriceBuyView view = new PlusPriceBuyView(context);
                final String plusGoodsUrl = entity.getPlusPriceBuyList().get(i).getGoodsDetailUrl();
                view.updateView(entity.getPlusPriceBuyList().get(i), new PlusPriceBuyView.CheckBoxClick() {
                    @Override
                    public void yesCheckBoxClick(String id) {
                        Log.d("===", "id:" + id);
                            plusGoodsIdList.add(id);
                    }

                    @Override
                    public void noCheckBoxClick(String id) {
                        if (plusGoodsIdList != null && plusGoodsIdList.size() > 0) {
                            for (int i = 0; i < plusGoodsIdList.size(); i++) {
                                if (plusGoodsIdList.get(i).equals(id)) {
                                    plusGoodsIdList.remove(i);
                                }
                            }
                        }
                    }
                });
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtil.isNotNull(plusGoodsUrl)) {
                            Intent intent = new Intent(context, WebViewActivity.class);
                            String webUrl = plusGoodsUrl;
                            intent.putExtra(Const.KEY_URL, webUrl);
                            intent.putExtra(Const.KEY_TITLE, "加价购商品");
                            intent.putExtra(Const.KEY_FROM, "course");
                            context.startActivity(intent);
                        }
                    }
                });
                plusPriceBuyLayout.addView(view);
            }

        } else if (v == getBonusTv) {
            reqBonusFromUrl();
        } else if (v == pay1Tv) {
            selectPay(1);
        } else if (v == pay2Tv) {
            selectPay(2);
        } else if (v == pay3Tv) {
//			reqFQ();
            reqDiaLog(fqList);
//			View view=LayoutInflater.from(getContext()).inflate(R.layout.view_pay_dialog, null);
//			AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
//			final AlertDialog mAlertDialog = builder.create();
//	        mAlertDialog.show();
//	        mAlertDialog.getWindow().setContentView(view);
//	        
//
//	        final RadioGroup rg1=(RadioGroup)view.findViewById(R.id.radioGroup1);
//	        final RadioGroup rg2=(RadioGroup)view.findViewById(R.id.radioGroup2);
//	        final RadioButton rb=(RadioButton)view.findViewById(R.id.radio1_0);
//	        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//				
//				@Override
//				public void onCheckedChanged(RadioGroup group, int checkedId) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//	        LinearLayout payLayout=(LinearLayout)view.findViewById(R.id.ll_pay_type);
//	        payLayout.setVisibility(View.GONE);
//	        
//	        
//	       
//	        Button okBtn=(Button)view.findViewById(R.id.btn_ok);
//	        okBtn.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//					if(rg1.getCheckedRadioButtonId()==R.id.radio1_0){
//						entity.setPayType("6");
//					}else{
//						entity.setPayType("7");
//					}
//					if(rg2.getCheckedRadioButtonId()==R.id.radio2_0){
//						entity.setPayFQ("3");
//					}else if(rg2.getCheckedRadioButtonId()==R.id.radio2_1){
//						entity.setPayFQ("6");
//					}else{
//						entity.setPayFQ("12");
//						
//					}
//					mAlertDialog.cancel();
//					selectPay(3);
//				}
//			});
//	        Button cancel=(Button)view.findViewById(R.id.btn_cancel);
//	        cancel.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					mAlertDialog.cancel();
//				}
//			});
        }

    }

    private void reqBonusFromUrl() {
        String url = String.format(UrlConst.REQ_BONUS_RUL, entity.getBonus_id(), entity.getId());
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                LogUtil.d(TAG, "error e=" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                {
                    LogUtil.d(TAG, "response=" + response);
                    Gson gson = new Gson();
                    ConversionCodeEntity conversionCodeEntity = gson.fromJson(response, ConversionCodeEntity.class);
                    if (conversionCodeEntity != null) {
                        if (conversionCodeEntity.getStatus() == UrlConst.SUCCESS_CODE) {
                            if (conversionCodeEntity.getData() != null)
                                DialogUtils.showToast(context, conversionCodeEntity.getInfo());
                        } else {
                            if (conversionCodeEntity.getInfo() != null)
                                DialogUtils.showToast(context, conversionCodeEntity.getInfo());
                        }
                    }
                }
            }

        });
    }

    //	private void selectCard(int i) {
//		card1Tv.setBackgroundColor(Color.TRANSPARENT);
//		card2Tv.setBackgroundColor(Color.TRANSPARENT);
//		card3Tv.setBackgroundColor(Color.TRANSPARENT);
//		card4Tv.setBackgroundColor(Color.TRANSPARENT);
//		card1Tv.setTextColor(getResources().getColor(R.color.color_60));
//		card2Tv.setTextColor(getResources().getColor(R.color.color_60));
//		card3Tv.setTextColor(getResources().getColor(R.color.color_60));
//		card4Tv.setTextColor(getResources().getColor(R.color.color_60));
//		switch (i) {
//		case 1:
//			card1Tv.setBackgroundResource(R.drawable.radius_red_with_stroke_bg);
//			card1Tv.setTextColor(getResources().getColor(R.color.color_red));
//			cardId = 1;
//			entity.setCardName("京东");
//			break;
//		case 2:
//			card2Tv.setBackgroundResource(R.drawable.radius_red_with_stroke_bg);
//			card2Tv.setTextColor(getResources().getColor(R.color.color_red));
//			cardId = 4;
//			entity.setCardName("苏宁易购");
//			break;
//		case 3:
//			card3Tv.setBackgroundResource(R.drawable.radius_red_with_stroke_bg);
//			card3Tv.setTextColor(getResources().getColor(R.color.color_red));
//			cardId = 2;
//			entity.setCardName("唯品会");
//			break;
//		case 4:
//			card4Tv.setBackgroundResource(R.drawable.radius_red_with_stroke_bg);
//			card4Tv.setTextColor(getResources().getColor(R.color.color_red));
//			cardId = 3;
//			entity.setCardName("1号店");
//			break;
//
//		default:
//			break;
//		}
//
//		entity.setRetIds(entity.getId() + "-"
//				+ entity.getSchoolEntity().getId() + "-"
//				+ entity.getPriceEntity().getId() + "-" + cardId);
//	}
    public List<String> getPlusGoodsIdList(){
        List<String> list = new ArrayList<>();
        Log.d("===","plusGoodsIdListss:"+plusGoodsIdList.size());
        list.addAll(plusGoodsIdList);
        return list;
    }

    private void selectPay(int i) {

        pay1Tv.setTextColor(getResources().getColor(R.color.color_60));
        pay2Tv.setTextColor(getResources().getColor(R.color.color_60));
        pay3Tv.setTextColor(getResources().getColor(R.color.color_60));
        switch (i) {
            case 1:
                pay1Tv.setTextColor(getResources().getColor(R.color.color_red));

                entity.setPayType("3");
                break;
            case 2:
                pay2Tv.setTextColor(getResources().getColor(R.color.color_red));
                entity.setPayType("5");
                break;
            case 3:
                pay3Tv.setTextColor(getResources().getColor(R.color.color_red));
                break;

            default:
                break;
        }


    }

    // 变化课程节数、学校名、电话、地址
    public void selectSchool(int index) {
        entity.setSchoolEntity(entity.getSchoolList().get(index));
        schoolAddressTv.setText(entity.getSchoolEntity().getSchoolAddr());
        schoolNameTv.setText(entity.getSchoolEntity().getSchoolName());
        selectCourseNum(0);
        schoolHour= entity.getSchoolEntity().getPriceList().get(0).getSchoolHour();
    }

    // 变化价格、优惠、更新课结束背景
    private void selectCourseNum(int i) {
        SchoolEntity sEntity = entity.getSchoolEntity();
        if (sEntity != null && sEntity.getPriceList() != null
                && sEntity.getPriceList().size() >= i) {
            for (int j = 0; j < sEntity.getPriceList().size(); j++) {
                if (j == i) {
                    sEntity.getPriceList().get(j).setSelected(true);
                    schoolHour= sEntity.getPriceList().get(j).getSchoolHour();
                } else {
                    sEntity.getPriceList().get(j).setSelected(false);
                }
            }
            updateCourseNum();
        } else {
            courseNumLayout.removeAllViews();
            DialogUtils.showToast(context, "该校区没有课时数据");
        }
    }

    public String getSchoolHour(){
        return  schoolHour;
    }
    private void updateCourseNum() {
        courseNumLayout.removeAllViews();
        selectPay(1);
        SchoolEntity sEntity = entity.getSchoolEntity();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        for (int j = 0; j < sEntity.getPriceList().size(); j++) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                    .inflate(
                            context.getResources().getLayout(
                                    R.layout.view_course_num_item), null);
            layout.setTag(j);
            TextView tv = (TextView) layout.findViewById(R.id.tv_course_num);
            layout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    selectCourseNum((int) (v.getTag()));
                }
            });
            tv.setText(sEntity.getPriceList().get(j).getSchoolHour());
            if (sEntity.getPriceList().get(j).isSelected()) {
                entity.setPriceEntity(entity.getSchoolEntity().getPriceList()
                        .get(j));
                tv.setBackgroundResource(R.drawable.radius_red_with_stroke_bg);
                priceTv.setText("￥" + entity.getPriceEntity().getPrice());
                if (entity.getIsPromotion() == 1) {
                    youhuiTv.setText(entity.getPriceEntity().getBackMoneyStr());
                    if (entity.getPriceEntity().getBackMoney().equals("0") || entity.getPriceEntity().getBackMoney().equals("")) {
                        youhuiTv.setVisibility(View.GONE);
                    }
                } else {
                    {
                        youhuiTv.setText("立减" + entity.getPriceEntity().getBackMoney()
                                + "元");
                        if (entity.getPriceEntity().getBackMoney().equals("0")) {
                            youhuiTv.setVisibility(View.GONE);
                        }
                    }
                }
                tv.setTextColor(getResources().getColor(R.color.color_red));
                entity.setRetIds(entity.getId() + "-"
                        + entity.getSchoolEntity().getId() + "-"
                        + entity.getPriceEntity().getId() + "-" + cardId);
            } else {
                tv.setBackgroundColor(Color.TRANSPARENT);
                tv.setTextColor(getResources().getColor(R.color.color_60));
            }
            params.setMargins(
                    getResources().getDimensionPixelSize(R.dimen.common_10), 0,
                    getResources().getDimensionPixelSize(R.dimen.common_10), 0);
            courseNumLayout.addView(layout, params);
        }
    }

    public void reqFQEntity() {
        String url = HttpUtil.addBaseGetParams(String.format(UrlConst.GET_FQ_LIST, entity.getId(),
                entity.getId(), entity.getPriceEntity().getId(), ""));
        Log.d("===", "url" + url);
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                LogUtil.d(TAG, "error e=" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                JSONObject baseJson;
                try {
                    baseJson = new JSONObject(response);
                    if (baseJson != null
                            && baseJson.optInt("status", 0) == 1) {
                        JSONArray dataJson = baseJson
                                .optJSONArray("data");
                        if (dataJson != null && dataJson.length() > 0) {
                            fqList = new ArrayList<FQEntity>();
                            for (int i = 0; i < dataJson.length(); i++) {
                                FQEntity fqEntity = new FQEntity(dataJson.optJSONObject(i));
                                fqList.add(fqEntity);
                            }
//							entity.setPayType(fqList.get(1).getId());
                            entity.setPayFQ(fqList.get(1).getList().get(1).getId());
                            entity.setPayFQList(fqList);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void reqDiaLog(final List<FQEntity> fqList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog mAlertDialog = builder.create();
        FQView fqView = new FQView(context);
        fqView.updateView(fqList, 0, new FQView.DialogItemClick() {

            @Override
            public void ok(String type, int fq1, int fq2) {
                // TODO Auto-generated method stub
                entity.setPayType(fqList.get(fq1).getId());
                entity.setPayFQ(fqList.get(fq1).getList().get(fq2).getId());
                entity.setPayFQList(fqList);
                mAlertDialog.cancel();
                selectPay(3);
            }

            @Override
            public void cancel() {
                // TODO Auto-generated method stub
                mAlertDialog.cancel();
            }
        });
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(fqView);
    }

    // 通过att以及goods请求分期信息
    private void reqFQ() {
        String url = HttpUtil.addBaseGetParams(String.format(
                UrlConst.GET_FQ_LIST, entity.getId(), entity.getPriceEntity().getId(), ""));
        OkHttpClientManager.getAsyn(url,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        // TODO Auto-generated method stub
                        LogUtil.d(TAG, "error e=" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtil.d(TAG, "response=" + response);
                        JSONObject baseJson;
                        try {
                            baseJson = new JSONObject(response);
                            if (baseJson != null
                                    && baseJson.optInt("status", 0) == 1) {
                                JSONArray dataJson = baseJson
                                        .optJSONArray("data");
                                if (dataJson != null && dataJson.length() > 0) {
                                    final List<FQEntity> fqList = new ArrayList<FQEntity>();
                                    for (int i = 0; i < dataJson.length(); i++) {
                                        FQEntity fqEntity = new FQEntity(dataJson.optJSONObject(i));
                                        fqList.add(fqEntity);
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    final AlertDialog mAlertDialog = builder.create();
                                    FQView fqView = new FQView(context);
                                    fqView.updateView(fqList, 0, new FQView.DialogItemClick() {

                                        @Override
                                        public void ok(String type, int fq1, int fq2) {
                                            // TODO Auto-generated method stub
                                            entity.setPayType(fqList.get(fq1).getId());
                                            entity.setPayFQ(fqList.get(fq1).getList().get(fq2).getId());
                                            entity.setPayFQList(fqList);
                                            mAlertDialog.cancel();
                                            selectPay(3);
                                        }

                                        @Override
                                        public void cancel() {
                                            // TODO Auto-generated method stub
                                            mAlertDialog.cancel();
                                        }
                                    });
                                    mAlertDialog.show();
                                    mAlertDialog.getWindow().setContentView(fqView);


                                } else {
                                    DialogUtils.showToast(context, baseJson.optString("info"));
                                }


                            } else {
                                DialogUtils.showToast(context, baseJson.optString("info"));
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
    }
}
