package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dev.nutclass.R;

/**
 * Created by Administrator on 2016/10/8.
 */
public class DiscountCouponView extends LinearLayout{
    private Context mContext;
    private ImageView closeIv;
    private Button conversionBtn;
    private EditText leftEt;
    private EditText rightEt;
    private Context context;
    public DiscountCouponView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.view_discount_coupon,this);
        closeIv = (ImageView) this.findViewById(R.id.iv_close);
        conversionBtn = (Button) this.findViewById(R.id.btn_conversion);
        leftEt = (EditText) this.findViewById(R.id.edit_left);
        rightEt = (EditText) this.findViewById(R.id.edit_right);
    }
    public void updataView(final DialogItemClick listener){
        closeIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener !=null){
                    listener.close();}
            }
        });
        conversionBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener !=null){
                    String leftString = leftEt.getText().toString();
                    String rightString = rightEt.getText().toString();
                    listener.commit(leftString,rightString);
                }
            }
        });
    }
   public interface  DialogItemClick{
        public void close();
        public void commit(String leftEdit,String rightEdit);
    }

}
