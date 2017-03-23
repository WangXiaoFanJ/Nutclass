package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.utils.DialogUtils;

/**
 * Created by Administrator on 2016/10/13.
 */
public class MerchantVerifyView extends LinearLayout {
    private Context mContext;
    private EditText verityCodeEdit;
    private Button cancleBtn;
    private Button makeSureBtn;
    public MerchantVerifyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }


    public MerchantVerifyView(Context context) {
        super(context);
        initView();
    }
    private void initView() {
        mContext = getContext();
       View view =  LayoutInflater.from(mContext).inflate(R.layout.dialog_merchant_make,this);
        verityCodeEdit = (EditText) view.findViewById(R.id.edit_verify_code);
        cancleBtn = (Button) view.findViewById(R.id.btn_cancel);
        makeSureBtn = (Button) view.findViewById(R.id.btn_makesure);
    }
    public void updataView(final DialogItemClick listener){
        cancleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.cancle();
            }
        });
        makeSureBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String verityCodeString = verityCodeEdit.getText().toString();
                if(verityCodeString !=null){
                    listener.confirm(verityCodeString);
                }else{
                    DialogUtils.showToast(mContext,"请输入验证码");
                }

            }
        });
    }

    public interface  DialogItemClick{
        public void cancle();
        public void confirm(String verityCodeEdit);
    }

}
