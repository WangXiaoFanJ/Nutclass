package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.PlusPriceBuyEntity;
import com.dev.nutclass.entity.PromotionEntity;

/**
 * Created by Administrator on 2016/12/2.
 */
public class PlusPriceItemView extends LinearLayout {
    private Context context;
    private TextView plusPriceIconTv;
    private TextView plusPriceStrTv;
    public PlusPriceItemView(Context context) {
        super(context);
        initView(context);
    }

    public PlusPriceItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_plus_price_item, this);
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        plusPriceIconTv = (TextView) this.findViewById(R.id.tv_plus_price_icon);
        plusPriceStrTv = (TextView) this.findViewById(R.id.tv_plus_price_str);
    }
    public void updateView(PromotionEntity entity){
        Log.d("===","entity.getIcon"+entity.getIcon());
        plusPriceIconTv.setText(entity.getIcon());
        plusPriceStrTv.setText(entity.getIcon_str());
    }
}
