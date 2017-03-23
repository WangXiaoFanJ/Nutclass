package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dev.nutclass.R;

/**
 * Created by Administrator on 2016/12/3.
 */
public class ReqAddressView extends LinearLayout {
    private Context context;
    private Button makeSureBtn;
    private Button cancleBtn;
    public ReqAddressView(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public ReqAddressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_dialog_is_address,this);
        makeSureBtn = (Button) this.findViewById(R.id.btn_makesure);
        cancleBtn = (Button) this.findViewById(R.id.btn_cancle);
    }
    public void updateView(final DialogItemClick listener){
        makeSureBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.commit();
            }
        });
        cancleBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.close();
            }
        });
    }
    public interface  DialogItemClick{
        public void close();
        public void commit();
    }
}
