package com.dev.nutclass.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.dev.nutclass.R;
import com.dev.nutclass.activity.CooponListActivity;
import com.dev.nutclass.activity.MerchantInfoActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ConversionCodeEntity;
import com.dev.nutclass.entity.MerchantCardEntity;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

/**
 * Created by Administrator on 2016/10/12.
 */
public class MerchantCardView extends LinearLayout {
    private static final String TAG = "MerchantCardView";
    private Context mContext;
    private TextView courseNameTv;
    private TextView userNameTv;
    private TextView courseNumTv;
    private TextView userPhoneTv;
    private TextView orderTimeTv;
    private TextView addressTv;
    private TextView coursePriceTv;
    private Button makeSureBtn;
    private Button oneYuanBtn;
    private RelativeLayout orderLayout;
    private LinearLayout oneYuanLayout;
    private ImageView oneyuanIv;
    private AlertDialog mAlertDialog;
    private int from;
    private String orderId;
    public MerchantCardView(Context context) {
        super(context);
        initView();
    }



    public MerchantCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    private void initView() {
        mContext = getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_merchant,this);
        courseNameTv = (TextView) view.findViewById(R.id.tv_course_name);
        userNameTv = (TextView) view.findViewById(R.id.tv_user_name);
        courseNumTv = (TextView) view.findViewById(R.id.tv_course_num);
        userPhoneTv = (TextView) view.findViewById(R.id.tv_user_phone);
        orderTimeTv = (TextView) view.findViewById(R.id.tv_order_time);
        addressTv = (TextView) view.findViewById(R.id.tv_school_address);
        coursePriceTv = (TextView) view.findViewById(R.id.tv_course_price);
        makeSureBtn = (Button) view.findViewById(R.id.btn_merchant_make);
        oneYuanLayout = (LinearLayout) view.findViewById(R.id.ll_merchant_oneyuan);
        orderLayout = (RelativeLayout) view.findViewById(R.id.rl_merchant_order);
        oneyuanIv = (ImageView) view.findViewById(R.id.iv_icon_makeSure);
        oneYuanBtn = (Button) view.findViewById(R.id.btn_merchant_connect);
    }
    public void updateView(MerchantCardEntity merEntity){
        setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        courseNameTv.setText(merEntity.getGoods_name());
        userNameTv.setText(merEntity.getConsignee());
        courseNumTv.setText(merEntity.getOrder_sn());
        userPhoneTv.setText(merEntity.getMobile_phone());
        addressTv.setText(merEntity.getXiaoqu_name());
        coursePriceTv.setText(merEntity.getOrder_amount());
        orderTimeTv.setText(merEntity.getTime_end());
        String is_confirm = merEntity.getIs_confirm();
        orderId = merEntity.getOrder_id();

        from = merEntity.getFrom();
        if(from == Const.TYPE_FROM_MERCHANT_ORDER){
            oneYuanLayout.setVisibility(View.GONE);
            if(is_confirm.equals("1")){
                orderLayout.setVisibility(View.GONE);
                oneyuanIv.setVisibility(View.VISIBLE);
            }else if(is_confirm.equals("0")) {
                oneyuanIv.setVisibility(View.INVISIBLE);
                orderLayout.setVisibility(View.VISIBLE);
            }
                makeSureBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqMakeSure();
                    }
                });
            }else if(from == Const.TYPE_FROM_MERCHANT_ONE_YUAN) {
            orderLayout.setVisibility(View.GONE);
            if(is_confirm.equals("1")){
                oneyuanIv.setVisibility(View.VISIBLE);
                oneYuanLayout.setVisibility(View.GONE);
            }else if(is_confirm.equals("0")) {
                oneyuanIv.setVisibility(View.INVISIBLE);
                oneYuanLayout.setVisibility(View.VISIBLE);
                oneYuanBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqMakeSureNoCode();
                    }
                });
            }
        }
    }


    private void reqMakeSureNoCode() {
        String url = String.format(HttpUtil.addBaseGetParams(UrlConst.MERCHANT_ONE_YUAN_MAKESURE_URL),orderId);
//            url = "http://new.kobiko.cn/Api/Appapi/user_confirm_for_xiaoqu_yiyuan?userId=55955&order_id=2324&user_code="+verityCodeEdit;
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
                    if(conversionCodeEntity!=null){
                        String info = conversionCodeEntity.getInfo();
                        if(conversionCodeEntity.getInfo()!=null){
                            DialogUtils.showToast(mContext,info);
                        }
                        if(conversionCodeEntity.getStatus() == UrlConst.SUCCESS_CODE){
                            DialogUtils.showToast(mContext,info);
                            ((MerchantInfoActivity)mContext).updateAdapter();
                        }
                    }
                }
            }
        });
    }

    private void reqMakeSure() {
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_merchant_make,null);
        mAlertDialog.setView(view);
        MerchantVerifyView merchantVerifyView = new MerchantVerifyView(mContext);
        merchantVerifyView.updataView(new MerchantVerifyView.DialogItemClick() {
            @Override
            public void cancle() {
                mAlertDialog.cancel();
            }

            @Override
            public void confirm(String verityCodeEdit) {
                    reqCode(verityCodeEdit,orderId);
            }
        });
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(merchantVerifyView);

    }

    private void reqCode(String verityCodeEdit,String orderId) {
        String url ="";
        if(from == Const.TYPE_FROM_MERCHANT_ORDER){
//            url = "http://new.kobiko.cn/Api/Appapi/user_confirm_for_xiaoqu?userId=55955&order_id=1686&user_code="+verityCodeEdit;
            url =  String.format(HttpUtil.addBaseGetParams(UrlConst.MERCHANT_ORDER_MAKESURE_URL),verityCodeEdit,orderId);
        }
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
                    if(conversionCodeEntity!=null){
                        String info = conversionCodeEntity.getInfo();
                        if(conversionCodeEntity.getInfo()!=null){
                            DialogUtils.showToast(mContext,info);
                        }
                        if(conversionCodeEntity.getStatus() == UrlConst.SUCCESS_CODE){
                           DialogUtils.showToast(mContext,info);
                            ((MerchantInfoActivity)mContext).updateAdapter();
                            mAlertDialog.cancel();
                        }
                    }
                }
            }
        });

    }

}
