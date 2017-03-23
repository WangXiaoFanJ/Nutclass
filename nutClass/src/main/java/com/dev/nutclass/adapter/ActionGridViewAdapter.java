package com.dev.nutclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.NewestActionEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.TextUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class ActionGridViewAdapter extends BaseAdapter{
    private List<NewestActionEntity.DataBean.TypeBean.GoodsBean> mlist;
    private Context mContext;

    public ActionGridViewAdapter(List<NewestActionEntity.DataBean.TypeBean.GoodsBean> mlist, Context mContext) {
        this.mlist = mlist;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mlist.size();
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
        ViewHolder viewHolder;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_action_newest,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.leftPrice.setText(mlist.get(position).getShop_price());
        viewHolder.rightPrice.setText(mlist.get(position).getMarket_price());
        viewHolder.iconStrTv.setText(mlist.get(position).getGoods_name());
        viewHolder.soldCountTv.setText(mlist.get(position).getSold_count());
        viewHolder.contentTv.setText(mlist.get(position).getIcon_str());
//        ImageLoader.getInstance().displayImage(mlist.get(position).getGoods_img(),
//                viewHolder.imageView, ImgConfig.getAdItemOption());
        String imgUrl = mlist.get(position).getGoods_img();
        viewHolder.imageView.setTag(imgUrl);
        if(imgUrl.equals(viewHolder.imageView.getTag())){
            Picasso.with(mContext).load(imgUrl).resize(200,200).placeholder(R.drawable.icon_default_new).into(viewHolder.imageView);
        }

        if((TextUtil.isNotNull(mlist.get(position).getIcon_img()))){
            viewHolder.leftTopIv.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(mlist.get(position).getIcon_img(),viewHolder.leftTopIv,ImgConfig.getAdItemOption());

        }
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, CourseInfoActivity.class);
                intent.putExtra(Const.KEY_ID, mlist.get(position).getGoods_id());
//                intent.putExtra(Const.KEY_SCHOOL_ID,entity.getSchoolId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView imageView ;
        TextView leftPrice;
        TextView rightPrice;
        LinearLayout linearLayout;
        ImageView leftTopIv;
        TextView iconStrTv;
        TextView soldCountTv;
        TextView contentTv;
        public ViewHolder(View convertView) {
            this.imageView = (ImageView) convertView.findViewById(R.id.iv_pic);
            leftPrice = (TextView) convertView.findViewById(R.id.tv_price_left);
            rightPrice = (TextView) convertView.findViewById(R.id.tv_price_right);
            linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_new);
            leftTopIv = (ImageView) convertView.findViewById(R.id.iv_left_top);
            iconStrTv = (TextView) convertView.findViewById(R.id.tv_icon_str);
            soldCountTv = (TextView) convertView.findViewById(R.id.tv_sold_count);
            contentTv = (TextView) convertView.findViewById(R.id.tv_content);
        }
    }
}
