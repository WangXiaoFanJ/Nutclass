package com.dev.nutclass.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CardListActivity;
import com.dev.nutclass.activity.PayActivity;
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

/**
 * Created by Administrator on 2016/12/2.
 */
public class WaitingPayView extends LinearLayout implements View.OnClickListener{
    private static final String TAG = "WaitingPayView";
    private CourseCardEntity entity;
    private Context context;
    private TextView brandNameTv;
    private ImageView logoIv;
    private TextView xiaoquAddTv;
    private TextView orderTimeTv;
    private TextView orderNumTv;
    private TextView cancleOrderTv;
    private TextView payOrderTv;
    private TextView orderStrTv;
    private TextView subtalTv;
    private LinearLayout orderInfoLayout;
    public WaitingPayView(Context context) {
        super(context);
        initView(context);
    }

    public WaitingPayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.card_waiting_pay, this);
//        nameTv = (TextView) this.findViewById(R.id.tv_name);
//        iconIv = (ImageView) this.findViewById(R.id.iv_icon);
//        xiaoquAddTv = (TextView) this.findViewById(R.id.tv_xiaoqu_name);
        logoIv = (ImageView) this.findViewById(R.id.iv_brand_logo);
        brandNameTv = (TextView) this.findViewById(R.id.tv_brand_name);
        orderTimeTv = (TextView) this.findViewById(R.id.tv_pay_time);
        orderNumTv = (TextView) this.findViewById(R.id.tv_order_num);
        cancleOrderTv = (TextView) this.findViewById(R.id.tv_cancle_order);
        payOrderTv = (TextView) this.findViewById(R.id.tv_pay_order);
        orderStrTv = (TextView) this.findViewById(R.id.tv_order_str);
        subtalTv = (TextView) this.findViewById(R.id.tv_subtotal);
        orderInfoLayout = (LinearLayout) this.findViewById(R.id.ll_waiting_pay);
    }

    public void updateView(CourseCardEntity entity){
        this.entity = entity;
        if (entity == null) {
            setVisibility(View.GONE);
            return;
        }
        cancleOrderTv.setOnClickListener(this);
        payOrderTv.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(entity.getLogo(), logoIv,
                ImgConfig.getPortraitOption());
        brandNameTv.setText(entity.getBrandName());
//        nameTv.setText(entity.getCourseName());
//        xiaoquAddTv.setText("校区："+entity.getAddress());
        orderTimeTv.setText(entity.getOrderTime());
        orderNumTv.setText(entity.getOrderSn());
        orderStrTv.setText("一共包含"+entity.getOrderSum()+"个课程");
        subtalTv.setText("￥"+entity.getPayFree());
        orderInfoLayout.removeAllViews();
        Log.d("===","waitpayList"+entity.getWaitingPayList().size());
                for(int i = 0;i<entity.getWaitingPayList().size();i++){
                    WaitingPayCourseView view = new WaitingPayCourseView(context);
                    view.updateView(entity.getWaitingPayList().get(i));
                    orderInfoLayout.addView(view);
                }

    }

    @Override
    public void onClick(View v) {
        if(v == payOrderTv){
            reqOrder(entity.getOrderId(),entity.getId());
        }else if (v == cancleOrderTv){
            deleteCourse();
        }
    }

    private void reqOrder(String orderId, String pid) {
        Intent intent=new Intent();
        intent.setClass(context, PayActivity.class);
        intent.putExtra(Const.KEY_TYPE, PayActivity.FROM_ORDER);
        intent.putExtra(Const.KEY_ID, orderId);
        intent.putExtra("pid", pid);
        context.startActivity(intent);
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
}
