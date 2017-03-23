package com.dev.nutclass.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dev.nutclass.R;
import com.dev.nutclass.adapter.CardListAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.parser.BaseParser;
import com.dev.nutclass.parser.MerchantListParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.TextUtil;
import com.squareup.okhttp.Request;

import org.json.JSONException;

import java.util.ArrayList;

public class MerchantInfoActivity extends BaseActivity implements View.OnClickListener {
    private final static String TAG = "MerchantInfoActivity";
    private Button leftBtn;
    private Button rightBtn;
    private Context mContext;
    private ImageView closeIv;
    private RecyclerView recyclerView;
    private CardListAdapter adapter;

    //拥有判断来源
    private int type;
    private int type_merchant_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_info);
        intiView();
        initIntent();
        initData();
    }

    private void intiView() {
        mContext = this;
        type_merchant_btn = Const.TYPE_MERCHANT_BTN_RIGHT;
        leftBtn = (Button) findViewById(R.id.btn_merchant_left);
        rightBtn = (Button) findViewById(R.id.btn_merchant_right);
        closeIv = (ImageView) findViewById(R.id.iv_close);
        recyclerView = (RecyclerView) findViewById(R.id.card_list);
        LinearLayoutManager mannager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mannager);

        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        closeIv.setOnClickListener(this);
    }
    private void initIntent() {
        type = getIntent().getIntExtra(Const.KEY_TYPE_MERCHANT,0);
        Log.d("===","type:"+type);
    }

    private void initData() {
        reqData();
    }

    @Override
    protected void handleTitleBarEvent(int eventId) {

    }

    @Override
    public void onClick(View v) {
        if(v == leftBtn){
            leftBtn.setBackgroundResource(R.drawable.shape_merchant_btn_left_bg);
            leftBtn.setTextColor(mContext.getResources().getColor(R.color.color_white));
            rightBtn.setBackgroundResource(R.drawable.shape_merchant_btn_right_white);
            rightBtn.setTextColor(mContext.getResources().getColor(R.color.color_merchant_btn));
            type_merchant_btn = Const.TYPE_MERCHANT_BTN_LEFT;
            reqData();
        }else if(v == rightBtn){
            leftBtn.setBackgroundResource(R.drawable.shape_merchant_btn_left_white);
            leftBtn.setTextColor(mContext.getResources().getColor(R.color.color_merchant_btn));
            rightBtn.setBackgroundResource(R.drawable.shape_merchant_btn_right_bg);
            rightBtn.setTextColor(mContext.getResources().getColor(R.color.color_white));
            type_merchant_btn = Const.TYPE_MERCHANT_BTN_RIGHT;
            reqData();
        }else if(v == closeIv){
            finish();
        }
    }

    private void reqData() {
        String url = getUrl();
        if(TextUtils.isEmpty(url)){
           return;
        }
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                LogUtil.d(TAG, "error e=" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                LogUtil.d(TAG, "response=" + response);
                BaseParser<BaseCardEntity> parser = null;
                    parser = new MerchantListParser(type);

                JsonDataList<BaseCardEntity> result;
                try {
                    result = (JsonDataList<BaseCardEntity>) parser.parse(response);
                    if(result.getErrorCode() == UrlConst.SUCCESS_CODE){
                        ArrayList<BaseCardEntity> list = result.getList();
                        if(list!=null && list.size()>0){
                            recyclerView.setVisibility(View.VISIBLE);
                            update(list);
                        }else{
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });



    }

    private void update(ArrayList<BaseCardEntity> list) {
        adapter = new CardListAdapter(mContext,list);
        recyclerView.setAdapter(adapter);
    }
    public void updateAdapter(){
        reqData();
    }
    public String getUrl() {
        String url = null;
        switch (type){
            case Const.TYPE_FROM_MERCHANT_ORDER:
                if(type_merchant_btn == Const.TYPE_MERCHANT_BTN_LEFT){
                    url = UrlConst.MERCHANT_ORDER_YES_URL;
                }else{
                url = UrlConst.MERCHANT_ORDER_NO_URL;
            }
                break;
            case Const.TYPE_FROM_MERCHANT_ONE_YUAN:
                leftBtn.setText("已联系");
                rightBtn.setText("未联系");
                if(type_merchant_btn == Const.TYPE_MERCHANT_BTN_LEFT){
                    url = UrlConst.MERCHANT_ONE_YUAN_YES_URL;
                }else{
                    url = UrlConst.MERCHANT_ONE_YUAN_NO_URL;
                }
                break;
            case Const.TYPE_FROM_MERCHANT_APPOINTMENT:
                if(type_merchant_btn == Const.TYPE_MERCHANT_BTN_LEFT){
                    url =UrlConst.MERCHANT_APPOITNMENT_YES_URL;
                }else{
                    url = UrlConst.MERCHANT_APPOINTMENT_NO_URL;
                }
        }
        return url;
    }
}
