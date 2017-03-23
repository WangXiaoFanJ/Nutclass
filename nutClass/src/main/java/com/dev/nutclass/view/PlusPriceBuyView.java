package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.PlusPriceBuyEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/12/1.
 */
public class PlusPriceBuyView extends LinearLayout {
    private static final String TAG = "PlusPriceBuyView";
    private PlusPriceBuyEntity entity;
    private Context mContext;
    private ImageView imgIv;
    private TextView nameTv;
    private TextView marketPriceTv;
    private TextView curPriceTv;
    private CheckBox checkBox;
    public PlusPriceBuyView(Context context) {
        super(context);
        initView(context);
    }

    public PlusPriceBuyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private void initView(Context context) {
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.view_plus_price_buy,this);
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        imgIv = (ImageView) this.findViewById(R.id.iv_img_url);
        nameTv = (TextView) this.findViewById(R.id.tv_goods_name);
        marketPriceTv = (TextView) this.findViewById(R.id.tv_goods_price);
        curPriceTv = (TextView) this.findViewById(R.id.tv_goods_cur_price);
        checkBox = (CheckBox) this.findViewById(R.id.checkbox);
    }
    public void updateView(final PlusPriceBuyEntity entity , final CheckBoxClick listener){
        this.entity = entity;
        ImageLoader.getInstance().displayImage(entity.getGoodsImgUrl(), imgIv,
                ImgConfig.getPortraitOption());
        Log.d("===","url:"+entity.getGoodsImgUrl());
        nameTv.setText(entity.getGoodsName());
        marketPriceTv.setText("￥"+entity.getMarketPrice());
        curPriceTv.setText("课比课价格：￥"+entity.getCurentPrice());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    listener.yesCheckBoxClick(entity.getId());
                }else{
                    listener.noCheckBoxClick(entity.getId());
                }
            }
        });
    }
    public interface CheckBoxClick{
        public void yesCheckBoxClick(String id);
        public void noCheckBoxClick(String id);
    }
}
