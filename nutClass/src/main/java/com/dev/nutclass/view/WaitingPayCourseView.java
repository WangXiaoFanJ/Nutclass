package com.dev.nutclass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.ImageLoaderConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.entity.WaitingPayEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2016/12/2.
 */
public class WaitingPayCourseView extends LinearLayout {
    private Context context;
    private ImageView iconIv;
    private TextView nameTv;
    private TextView xiaoquNameTv;
    private TextView goodsPriceTv;
    private TextView goodsAttrTv;
    private LinearLayout plusPriceItemLayout;
    public WaitingPayCourseView(Context context) {
        super(context);
        initView(context);
    }


    public WaitingPayCourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.card_waiting_pay_course, this);
        setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        iconIv = (ImageView) this.findViewById(R.id.iv_icon);
        nameTv = (TextView) this.findViewById(R.id.tv_name);
        xiaoquNameTv = (TextView) this.findViewById(R.id.tv_xiaoqu_name);
        goodsPriceTv = (TextView) this.findViewById(R.id.tv_price_goods);
        goodsAttrTv = (TextView) this.findViewById(R.id.tv_attr_value);
        plusPriceItemLayout = (LinearLayout) this.findViewById(R.id.ll_plus_price_item);
    }
    public void updateView(WaitingPayEntity entity){
        ImageLoader.getInstance().displayImage(entity.getGoods_thumb(), iconIv,
                ImgConfig.getPortraitOption());
        nameTv.setText(entity.getGoods_name());
        xiaoquNameTv.setText("校区："+entity.getSchool_addr());
        goodsPriceTv.setText("￥"+entity.getGoods_price());
        goodsAttrTv.setText(entity.getGoods_attr());
        plusPriceItemLayout.removeAllViews();
        if(entity.getPromotionBeanList()!=null){
            for (int i = 0;i<entity.getPromotionBeanList().size();i++){
                PlusPriceItemView view = new PlusPriceItemView(context);
                Log.d("===","promotionList_icon:"+entity.getPromotionBeanList().get(i).getIcon());
                view.updateView(entity.getPromotionBeanList().get(i));
                plusPriceItemLayout.addView(view);
            }

        }

    }
}
