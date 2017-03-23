package com.dev.nutclass.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.PayActivity;
import com.dev.nutclass.entity.CourseCardEntity;

/**
 * Created by Administrator on 2016/9/20.
 */
public class ChangePriceView extends LinearLayout {
    private Context context;
    private Button noBtn;
    private Button yesBtn;
    private Button sureBtn;
    private Button cancleBtn;
    private LinearLayout isSame;
    private LinearLayout isCommit;
    private TextView courseName;
    private TextView courseTime;
    private TextView coursePrice;
    private CourseCardEntity entity;
    private EditText priceChanged;
    private TextView markedWords;
    private  int num;
    public ChangePriceView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.view_price_change,this);
        noBtn = (Button) this.findViewById(R.id.btn_no);
        yesBtn = (Button) this.findViewById(R.id.btn_yes);
        cancleBtn = (Button) this.findViewById(R.id.btn_cancle);
        sureBtn = (Button) this.findViewById(R.id.btn_makesure);
        isSame = (LinearLayout)this. findViewById(R.id.ll_isSame);
        isCommit = (LinearLayout)this. findViewById(R.id.ll_isCommit);
        courseName = (TextView)this. findViewById(R.id.tv_course_name);
        courseTime = (TextView) this.findViewById(R.id.tv_course_time);
        coursePrice = (TextView) this.findViewById(R.id.tv_coutse_price);
        priceChanged = (EditText) this.findViewById(R.id.et_price);
        markedWords = (TextView) this.findViewById(R.id.tv_words);
    }
    public void updateView(final CourseCardEntity entity, final DialogItemClick listener){
        this.entity = entity;
        if (entity == null) {
            return;
        }
        courseName.setText(entity.getCourseName());
        courseTime.setText(entity.getPriceEntity().getSchoolHour());
        coursePrice.setText(entity.getPriceEntity().getPrice()+"元");
        markedWords.setText("是否与"+entity.getSchoolEntity().getSchoolName()+"价格相符?");
        if(Integer.parseInt(entity.getPriceEntity().getPrice())<10){
            noBtn.setEnabled(false);
            noBtn.setBackgroundResource(R.drawable.shape_rigester_gray);
        }else{
            noBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSame.setVisibility(View.GONE);
                    isCommit.setVisibility(View.VISIBLE);
                }
            });
        }
        yesBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                listener.yes();
            }

            }
        });
        cancleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.yes();
                }
            }
        });
            if(priceChanged.length()==0){
                sureBtn.setEnabled(false);
                sureBtn.setBackgroundResource(R.drawable.shape_rigester_gray);
            }
        priceChanged.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    sureBtn.setBackgroundResource(R.drawable.shape_price_change);
                sureBtn.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
                sureBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num =(int) (Double.parseDouble(priceChanged.getText().toString()));
                        Float changeNum = Float.parseFloat(entity.getPriceEntity().getPrice() + "");

                        if (num < changeNum * 1.15 && num > changeNum * 0.85) {
                            listener.makesure(num, num/changeNum);
                        } else {
                            Toast.makeText(context, "价格调整比例不能超过15%！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public interface DialogItemClick{
        public void yes();
        public void no();
        public void makesure(int priceChanged, float rate);
    }
}
