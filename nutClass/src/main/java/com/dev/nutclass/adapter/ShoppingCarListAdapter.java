package com.dev.nutclass.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.fragment.ShoppingFragment;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.view.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/4.
 */
public class ShoppingCarListAdapter extends BaseAdapter {
    private Map<Integer,Integer> map = new HashMap<>();
    private CheckBox checkBoxAll;
    private List<BaseCardEntity> list;
    private Context mContext;
    private LayoutInflater inflater;
    private Handler handler;
    private Double totalPrice = 0.0;
    private String plusPriceBuyInfo;
    private StringBuffer stringBuffer;
    private RecyclerItemClickListener itemClickListener;
    public ShoppingCarListAdapter(Map<Integer,Integer> map, List<BaseCardEntity> list, Context mContext,
                                  CheckBox checkBoxAll, Handler handler,RecyclerItemClickListener itemClickListener) {
        this.list = list;
        this.mContext = mContext;
        this.checkBoxAll = checkBoxAll;
        this.map = map;
        this.handler = handler;
        this.itemClickListener = itemClickListener;
        inflater = LayoutInflater.from(mContext);
//        stringBuffer = new StringBuffer();
    }

    @Override
    public int getCount() {
        return  list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_shopping_car,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final CourseCardEntity entity = (CourseCardEntity) list.get(position);
        ImageLoader.getInstance().displayImage(entity.getIcon(), viewHolder.iconIv,
                ImgConfig.getPortraitOption());
        ImageLoader.getInstance().displayImage(entity.getLogo(), viewHolder.logoIv,
                ImgConfig.getPortraitOption());
        viewHolder.nameTv.setText(entity.getCourseName());
        viewHolder.priceTv.setText("￥"+entity.getPriceMax());
//        viewHolder.schoolTelTv.setText(entity.getTel());
        viewHolder.disTv.setText(entity.getAddress());
        viewHolder.brandName.setText(entity.getBrandName());
        if(TextUtil.isNotNull(entity.getPriceReturn())){
            viewHolder.minusActivityLayout.setVisibility(View.VISIBLE);
            viewHolder.minusPriceTv.setText("支付立减"+entity.getPriceReturn()+"元");
            if(entity.getPriceReturn().equals("0")){
                viewHolder.minusActivityLayout.setVisibility(View.GONE);
            }
        }else{
            viewHolder.minusActivityLayout.setVisibility(View.GONE);
        }

        viewHolder.courseTimeTv.setText(entity.getSchoolHour());
        if(TextUtil.isNotNull(entity.getPlus_price_buy())){
//            plusPriceBuyInfo = entity.getPlus_price_buy();
            if(position==0){
//                stringBuffer.append(entity.getPlus_price_buy());
                plusPriceBuyInfo = entity.getPlus_price_buy();
            }else{
                plusPriceBuyInfo = plusPriceBuyInfo+","+entity.getPlus_price_buy();
//                stringBuffer.append(","+entity.getPlus_price_buy());
            }

            viewHolder.plusPriceBuyLayout.setVisibility(View.VISIBLE);
            viewHolder.plusPriceBuyNumTv.setText("X"+entity.getPlus_price_total_num());
            viewHolder.plusPriceTotalMoneyTv.setText("￥"+entity.getPlus_price_total_money());
        }else{
            viewHolder.plusPriceBuyLayout.setVisibility(View.GONE);
        }
        if(entity.isEdit()){
            viewHolder.closeIv.setVisibility(View.VISIBLE);
            viewHolder.closeIv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // 点击删除
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(null, entity);
                    }
                }
            });
        }else{
            viewHolder.closeIv.setVisibility(View.GONE);
        }
        viewHolder.checkBox.setTag(position);
        if(map.containsKey(position)){
            viewHolder.checkBox.setChecked(true);
        }else{
            viewHolder.checkBox.setChecked(false);
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!map.containsKey(viewHolder.checkBox.getTag())){
                        map.put((Integer) viewHolder.checkBox.getTag(),position);
                    }
                    if (getCount() == map.size()){
                        checkBoxAll.setChecked(true);
                    }
                    handler.sendMessage(handler.obtainMessage(0,getTotalPrice()));
                }else{
                    map.remove(viewHolder.checkBox.getTag());
                    checkBoxAll.setChecked(false);
                    handler.sendMessage(handler.obtainMessage(0,getTotalPrice()));
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        private RoundedImageView iconIv;
        private TextView  nameTv;
        private TextView priceTv;
//        private TextView schoolTelTv;
        private TextView disTv;
        private CheckBox checkBox;
        private ImageView closeIv;
        private TextView plusPriceBuyNumTv;
        private RelativeLayout plusPriceBuyLayout;
        private TextView plusPriceTotalMoneyTv;
        private  TextView minusPriceTv;
        private ImageView logoIv;
        private TextView brandName;
        private TextView courseTimeTv;
        private RelativeLayout minusActivityLayout;
        public ViewHolder(View convertView) {
            iconIv = (RoundedImageView) convertView.findViewById(R.id.iv_icon);
            nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            priceTv = (TextView) convertView.findViewById(R.id.tv_price);
//            schoolTelTv = (TextView) convertView.findViewById(R.id.tv_shopping_tel);
            disTv = (TextView) convertView.findViewById(R.id.tv_dis);
            checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            closeIv = (ImageView) convertView.findViewById(R.id.iv_close);
            plusPriceBuyNumTv = (TextView) convertView.findViewById(R.id.tv_plus_price_num);
            plusPriceBuyLayout = (RelativeLayout) convertView.findViewById(R.id.rl_plus_price_buy);
            plusPriceTotalMoneyTv = (TextView) convertView.findViewById(R.id.tv_plus_price_total);
            minusPriceTv = (TextView) convertView.findViewById(R.id.tv_minus_price);
            logoIv = (ImageView) convertView.findViewById(R.id.iv_brand_logo);
            brandName = (TextView) convertView.findViewById(R.id.tv_brand_name);
            courseTimeTv = (TextView) convertView.findViewById(R.id.tv_course_time);
            minusActivityLayout = (RelativeLayout) convertView.findViewById(R.id.rl_minus_activity);
        }
    }
    public float getTotalPrice(){
        float totalPrice = 0;
        CourseCardEntity data ;
        for(int i=0;i<list.size();i++){
            if(map.get(i)!=null &&map.get(i)==i){
                data = (CourseCardEntity) list.get(i);
                totalPrice+= Float.parseFloat(data.getPriceMax());
            }
        }
        return totalPrice;
    }
    public List<BaseCardEntity> getList(){
        return list;
    }
    public void removeItem(int pos){
        list.remove(pos);
        handler.sendMessage(handler.obtainMessage(0,getTotalPrice()));
        notifyDataSetChanged();
        if(list.size() ==0){
            handler.sendEmptyMessage(1);
        }
    }
    public String getPlusPriceBuyInfo(){
        if(TextUtil.isNotNull(plusPriceBuyInfo)){
//            Log.d("===","StringBuffer:"+stringBuffer.toString());
            Log.d("===","plusPriceBuyInfoShopping:"+plusPriceBuyInfo);
            return plusPriceBuyInfo;
        }else{
            return null;
        }

    }
    public String getPayOrderInfo(){
        CourseCardEntity data ;
        StringBuilder string = new StringBuilder();
        for(int i=0;i<list.size();i++){
            if(map.get(i)!=null &&map.get(i)==i){
                data = (CourseCardEntity) list.get(i);
               string.append(data.getRetIds());
                if(list.size()>1 && i<list.size()-1){
                    string.append(",");
                }
            }
        }
      return string.toString();
    }
}

