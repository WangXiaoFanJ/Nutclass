package com.dev.nutclass.view;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
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

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/12.
 */
public class MerchantAppMView extends LinearLayout {
    private final static String TAG = "MerchantAppMView";
    private Context mContext;
    private TextView schoolNameTv;
    private TextView schoolPhoneTv;
    private TextView addressTv;
    private TextView linkmanTv;
    private TextView keepPhoneTv;
    private TextView commitTimeTv;
    private ImageView makeSureIv;
    private Button makeSureBtn;
    private AlertDialog mAlertDialog;
    private SimpleDateFormat sf;
    private String goodsId;
    public MerchantAppMView(Context context) {
        super(context);
        initView();
    }


    public MerchantAppMView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    private void initView() {
        mContext = getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_appm_merchant,this);
        schoolNameTv = (TextView) view.findViewById(R.id.tv_school_name);
        schoolPhoneTv = (TextView) view.findViewById(R.id.tv_school_phone);
        addressTv = (TextView) view.findViewById(R.id.tv_address);
        linkmanTv = (TextView) view.findViewById(R.id.tv_linkman);
        commitTimeTv = (TextView) view.findViewById(R.id.tv_commit_time);
        makeSureBtn = (Button) view.findViewById(R.id.btn_merchant_makesure);
        makeSureIv = (ImageView) view.findViewById(R.id.icon_merchant_make);
        keepPhoneTv = (TextView) view.findViewById(R.id.tv_keep_phone);
    }

    public void updataView(MerchantCardEntity entity){
        setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        schoolNameTv.setText(entity.getGoods_name());
        schoolPhoneTv.setText(entity.getXiaoqu_tel());
        addressTv.setText(entity.getXiaoqu_name());
        linkmanTv.setText(entity.getNick_name());
        keepPhoneTv.setText(entity.getMobile_phone());
        commitTimeTv.setText(entity.getC_time());
        goodsId = entity.getGoods_id();
        String is_back = entity.getIs_back();
        if(is_back.equals("0")){
            makeSureBtn.setVisibility(View.VISIBLE);
            makeSureIv.setVisibility(View.INVISIBLE);
            makeSureBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    reqMakeSure();
                }
            });
        }else if (is_back.equals("1")){
            makeSureIv.setVisibility(View.VISIBLE);
            makeSureBtn.setVisibility(View.GONE);
        }

    }
    private void reqMakeSure() {
//        mAlertDialog = new AlertDialog.Builder(mContext).create();
//        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_merchant_make,null);
//        mAlertDialog.setView(view);
//        MerchantVerifyView merchantVerifyView = new MerchantVerifyView(mContext);
//        merchantVerifyView.updataView(new MerchantVerifyView.DialogItemClick() {
//            @Override
//            public void cancle() {
//                mAlertDialog.cancel();
//            }
//
//            @Override
//            public void confirm(String verityCodeEdit) {
                reqCode(goodsId);
//            }
//        });
//        mAlertDialog.show();
//        mAlertDialog.getWindow().setContentView(merchantVerifyView);

    }
    private void reqCode(String orderId) {
        String url = String.format(HttpUtil.addBaseGetParams(UrlConst.MERCHANT_APPOINTMENT_MAKESURE_URL),orderId);
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

}
