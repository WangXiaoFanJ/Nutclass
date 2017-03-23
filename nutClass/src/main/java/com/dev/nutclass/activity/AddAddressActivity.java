package com.dev.nutclass.activity;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.dev.nutclass.R;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ConversionCodeEntity;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;


public class AddAddressActivity extends BaseActivity {
    public static  final  String TAG= "AddAddressActivity";
    private Context mContext;
    private EditText nameEdit;
    private EditText phoneEdit;
    private EditText phoneEdit02;
    private EditText addressEdit;
    private EditText addressEdit02;
    private Button makeSureBtn;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        mContext= AddAddressActivity.this;
        initView();
//        addAddress();
    }

    private void initView() {
        nameEdit = (EditText) findViewById(R.id.edit_user_name);
        phoneEdit = (EditText) findViewById(R.id.edit_user_phone);
        phoneEdit02 = (EditText) findViewById(R.id.edit_user_phone2);
        addressEdit = (EditText) findViewById(R.id.edit_address);
        addressEdit02 = (EditText) findViewById(R.id.edit_address02);
        makeSureBtn = (Button) findViewById(R.id.btn_makesure);
        makeSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress();
            }
        });
    }
    private void addAddress() {
        address = nameEdit.getText().toString()+","+phoneEdit.getText().toString()+","+phoneEdit02.getText().toString()+
                ","+addressEdit.getText().toString()+","+addressEdit02.getText().toString();
        String url = UrlConst.REQ_ADD_ADDRESS_URL;
        String userId = SharedPrefUtil.getInstance().getUid();
        String token = SharedPrefUtil.getInstance().getToken();
        if(TextUtil.isNotNull(nameEdit.getText().toString())&&TextUtil.isNotNull(phoneEdit.getText().toString())
                &&TextUtil.isNotNull(addressEdit.getText().toString())){
            url = String.format(
                    HttpUtil.addBaseGetParams(UrlConst.REQ_ADD_ADDRESS_URL), userId,token,address);
            OkHttpClientManager.getAsynNoAdd(url, new OkHttpClientManager.ResultCallback<String>() {
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
                                finish();
//                                Intent intent = new Intent(context, CooponListActivity.class);
//                                context.startActivity(intent);
//                                mAlertDialog.cancel();
                            }
                        }
                    }

                }

            });
        }else{
            DialogUtils.showToast(mContext,"请完善收货地址");
        }

    }

    @Override
    protected void handleTitleBarEvent(int eventId) {

    }

}
