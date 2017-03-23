package com.dev.nutclass.activity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextClock;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ConversionCodeEntity;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
import com.dev.nutclass.view.MyPopupWindow;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

public class ReqDiscountActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ReqDiscountActivity";
    private TextView schoolNameTv;
    private TextView schoolAddressTv;
    private TextView schoolCateTv;
    private EditText userPhoneTv;
    private EditText priceEdit;
    private EditText contentEdit;
    private Button commitBtn;
    private Context mContext;
    private ImageView findSimIv;
    private ImageView makeSureIv;
    private AlertDialog mAlertDialog;

    private String schoolName;
    private String schoolAddress;
    private String cateName;
    private String phone;
    private String schoolTel;
    private String price;
    private String content;
    MyPopupWindow menuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = ReqDiscountActivity.this;
        setContentView(R.layout.activity_req_discount);
        initView();
        initIntent();
        init();
    }

    private void init() {
        if (SharedPrefUtil.getInstance().getMobile() != null) {
            userPhoneTv.setText(SharedPrefUtil.getInstance().getMobile());
        }

    }

    private void initView() {
        schoolNameTv = (TextView) findViewById(R.id.tv_school_name);
        schoolAddressTv = (TextView) findViewById(R.id.tv_school_address);
        schoolCateTv = (TextView) findViewById(R.id.tv_school_cate);
        userPhoneTv = (EditText) findViewById(R.id.tv_user_phone);
        priceEdit = (EditText) findViewById(R.id.edit_price);
        contentEdit = (EditText) findViewById(R.id.edit_content);
        commitBtn = (Button) findViewById(R.id.btn_commit);

        commitBtn.setOnClickListener(this);
    }

    private void initIntent() {
        Intent intent = getIntent();
        schoolName = intent.getStringExtra("name");
        schoolAddress = intent.getStringExtra("address");
        cateName = intent.getStringExtra("cateName");
        schoolTel = intent.getStringExtra("tel");
        if (schoolName != null) {
            schoolNameTv.setText(schoolName);
        }
        if (schoolAddress != null) {
            schoolAddressTv.setText(schoolAddress);
        }
        if (cateName != null) {
            schoolCateTv.setText(cateName);
        }
    }

    @Override
    protected void handleTitleBarEvent(int eventId) {

    }

    @Override
    public void onClick(View v) {
        if (v == commitBtn) {
            String url = "http://new.kobiko.cn/Api/Appapi/save_user_to_poi" +
                    "?userId=%s&token=%s&brand_name=%s&brand_addr=%s&cat_name=%s&mobile_phone=%s" +
                    "&brand_tel=%s&price=%s&content=%s";
            String userId = SharedPrefUtil.getInstance().getUid();
            String token = SharedPrefUtil.getInstance().getToken();
            price = priceEdit.getText().toString();
            content = contentEdit.getText().toString();
            phone = userPhoneTv.getText().toString();
            if (!TextUtil.isNotNull(SharedPrefUtil.getInstance().getMobile())){
                   Intent intent = new Intent(mContext,LoginTogetherActivity.class);
                startActivity(intent);
            } else {
                    url = String.format(
                            HttpUtil.addBaseGetParams(url), userId, token, schoolName, schoolAddress
                            , cateName, phone, schoolTel, price, content);
                    OkHttpClientManager.getAsynNoAdd(url, new OkHttpClientManager.ResultCallback<String>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.e(TAG, "error e=" + e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            ConversionCodeEntity entity = gson.fromJson(response, ConversionCodeEntity.class);
                            if (entity.getStatus() == UrlConst.SUCCESS_CODE) {
                                showMDialog();
                            }
                        }
                    });
            }
        } else if (v == makeSureIv) {
            menuWindow.dismiss();
            finish();
        } else if (v == findSimIv) {
            Intent intent = new Intent(mContext,SearchActivity.class);
            intent.putExtra(Const.KEY_KEYWORD,cateName);
            startActivity(intent);
            finish();
        }
    }

    private void showMDialog() {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.view_discount_result, null);
        menuWindow= new MyPopupWindow(ReqDiscountActivity.this,mMenuView);
        makeSureIv = ((ImageView) mMenuView.findViewById(R.id.iv_makeSure));
        findSimIv = (ImageView) mMenuView.findViewById(R.id.iv_find_similar);
        makeSureIv.setOnClickListener(this);
        findSimIv.setOnClickListener(this);
        menuWindow.showAtLocation(ReqDiscountActivity.this.findViewById(R.id.relative_parent), Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        popWindowSetting();

//        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext,R.style.MyDialogStyle);
//        mAlertDialog = dialog.create();
//        View view = LayoutInflater.from(mContext).inflate(R.layout.view_discount_result, null);
//        mAlertDialog.setView(view);
//        makeSureIv = (ImageView) view.findViewById(R.id.iv_makeSure);
//        findSimIv = (ImageView) view.findViewById(R.id.iv_find_similar);
//        makeSureIv.setOnClickListener(this);
//        findSimIv.setOnClickListener(this);
//        mAlertDialog.show();
    }

    private void popWindowSetting() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }
}
