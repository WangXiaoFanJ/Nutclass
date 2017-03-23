package com.dev.nutclass.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.CooponCardEntity;
import com.dev.nutclass.entity.PayOrderInfoEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/6.
 */
public class PayOrderInfoAdapter extends BaseAdapter{
    private List<PayOrderInfoEntity.DataBean.OrderDetailBean> list;
    private Context mContext;
    private LayoutInflater inflater;
    private         ViewHolder viewHolder = null;
    private String discountType;
    private List<PayOrderInfoEntity.DataBean.OrderDetailBean.BonusInfoListBean> bonusList;
    private View.OnClickListener changePrice;
    public void setSubTotal(View.OnClickListener changePrice){
               this.changePrice = changePrice;
            }

    public PayOrderInfoAdapter( Context context,List<PayOrderInfoEntity.DataBean.OrderDetailBean> list) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_order_info_pay,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String logoImg = list.get(position).getBrand_logo();
        String goodsThumbImg = list.get(position).getGoods_thumb();
        if(logoImg!=null){
            viewHolder.brandLogoIv.setTag(logoImg);
            if(logoImg.equals(viewHolder.brandLogoIv.getTag())){
                ImageLoader.getInstance().displayImage(list.get(position).getBrand_logo(),viewHolder.brandLogoIv, ImgConfig.getAdItemOption());
            }
        }
        viewHolder.goodsThumbIv.setTag(goodsThumbImg);

        if(goodsThumbImg.equals(viewHolder.goodsThumbIv.getTag())){
            ImageLoader.getInstance().displayImage(list.get(position).getGoods_thumb(),viewHolder.goodsThumbIv,ImgConfig.getAdItemOption());
        }
        viewHolder.brandNameTv.setText(list.get(position).getBrand_name());

        viewHolder.goodsNameTv.setText(list.get(position).getGoods_name());
        viewHolder.xiaoquNameTv.setText(list.get(position).getXiaoqu_name());
        viewHolder.originPriceTv.setText("￥"+list.get(position).getShop_price());
        viewHolder.attrValueTv.setText(list.get(position).getAttr_value()+list.get(position).getAttr_name());
        viewHolder.xiaoquAddrTv.setText(list.get(position).getXiaoqu_addr());
        viewHolder.attrValues2Tv.setText(list.get(position).getAttr_value()+list.get(position).getAttr_name());
        viewHolder.subTotalTv.setText("￥"+list.get(position).getSubtotal());
        bonusList  = list.get(position).getBonus_info_list();
        discountType = list.get(position).getDiscount_type();
        if(list.get(position).getBonus_info_list()!=null && list.get(position).getBonus_info_list().size()>0){
            viewHolder.bonusNumTv.setVisibility(View.VISIBLE);
            viewHolder.bonusNumTv.setText(list.get(position).getBonus_info_list().size()+"张可用");
        }else {
            viewHolder.bonusNumTv.setVisibility(View.GONE);
        }
        if(TextUtil.isNotNull(list.get(position).getPlus_price_buy_money())){
            viewHolder.plusPriceBuyLayout.setVisibility(View.VISIBLE);
            viewHolder.plusPriceNumTv.setText("X"+list.get(position).getPlus_price_buy_total());
            viewHolder.plusPirceTotalTv.setText("￥"+list.get(position).getPlus_price_buy_money());
            if(list.get(position).getPlus_price_buy_total().equals("0")){
                viewHolder.plusPriceBuyLayout.setVisibility(View.GONE);
            }
        }else{
            viewHolder.plusPriceBuyLayout.setVisibility(View.GONE);
        }
        if(discountType.equals("1")){
            if(list.get(position).getBonus_info().getType_money()!=null){
                viewHolder.youhuiStrTv.setText(list.get(position).getBonus_info().getType_name()+">");
                if(list.get(position).getBonus_info_list()!=null){
                    viewHolder.youhuiStrTv.setOnClickListener(changePrice);
                    if(TextUtil.isNotNull(list.get(position).getBonusStr())){
                        viewHolder.subTotalTv.setText("￥"+list.get(position).getSubTotalShow());
                        viewHolder.youhuiStrTv.setText(list.get(position).getBonusStr()+">");
                    }else{
                        Log.d("===","绑定默认优惠券");
                        list.get(position).setBonusId(list.get(position).getBonus_info().getBonus_id());
                        viewHolder.subTotalTv.setText("￥"+list.get(position).getSubtotal());
                        viewHolder.youhuiStrTv.setText(list.get(position).getBonus_info().getType_name());
                    }
                }
            }else{
                Log.d("===","bonus_info是空的");
                viewHolder.youhuiStrTv.setText("立减￥"+list.get(position).getMinus()+"元>");
            }
        }else if (discountType.equals("2")){
        viewHolder.youhuiStrTv.setText("立减￥"+list.get(position).getMinus()+"元>");
        }else if (discountType.equals("3")){
            if(TextUtil.isNotNull(list.get(position).getMimusShow())){
                viewHolder.youhuiStrTv.setText("立减￥"+list.get(position).getMimusShow()+"元>");
                viewHolder.subTotalTv.setText("￥"+list.get(position).getSubTotalShow());
            }else{
                viewHolder.youhuiStrTv.setText("立减￥"+list.get(position).getMinus()+"元>");
                viewHolder.subTotalTv.setText("￥"+list.get(position).getSubtotal());
            }
            viewHolder.youhuiStrTv.setOnClickListener(changePrice);
        }
        viewHolder.subTotalTv.setTag(list.get(position).getGoods_img());
        viewHolder.youhuiStrTv.setTag(position);


//        viewHolder.youhuiStrTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewHolder.subTotalTv.getTag().equals(list.get(position).getGoods_img())){
//                    viewHolder.subTotalTv.setText("100");
//                }
//
//            }
//        });
        return convertView;
    }


    class ViewHolder{
        private ImageView brandLogoIv;
        private TextView brandNameTv;
        private ImageView goodsThumbIv;
        private TextView goodsNameTv;
        private TextView xiaoquNameTv;
        private TextView originPriceTv;
        private TextView attrValueTv;
        private TextView xiaoquAddrTv;
        private TextView attrValues2Tv;
        private TextView subTotalTv;
        private TextView youhuiStrTv;
        private LinearLayout plusPriceBuyLayout;
        private TextView plusPriceNumTv;
        private TextView plusPirceTotalTv;
        private TextView bonusNumTv;
        public ViewHolder(View convertView) {
            brandLogoIv = (ImageView) convertView.findViewById(R.id.iv_brand_logo);
            brandNameTv = (TextView) convertView.findViewById(R.id.tv_brand_name);
            goodsThumbIv = (ImageView) convertView.findViewById(R.id.iv_goods_thumb);
            goodsNameTv = (TextView) convertView.findViewById(R.id.tv_goods_name);
            xiaoquNameTv = (TextView) convertView.findViewById(R.id.tv_xiaoqu_name);
            originPriceTv = (TextView) convertView.findViewById(R.id.tv_origin_price);
            attrValueTv = (TextView) convertView.findViewById(R.id.tv_attr_value);
            xiaoquAddrTv = (TextView) convertView.findViewById(R.id.tv_xiaoqu_addr);
            attrValues2Tv = (TextView) convertView.findViewById(R.id.tv_attr_values);
            subTotalTv = (TextView) convertView.findViewById(R.id.tv_subtotal);
            youhuiStrTv = (TextView) convertView.findViewById(R.id.tv_youhui_str);
            bonusNumTv = (TextView) convertView.findViewById(R.id.tv_bonus_num);
            plusPriceBuyLayout = (LinearLayout) convertView.findViewById(R.id.rl_plus_price_buy);
            plusPriceNumTv = (TextView) convertView.findViewById(R.id.tv_plus_price_num);
            plusPirceTotalTv = (TextView) convertView.findViewById(R.id.tv_plus_price_total);
        }
    }

}
