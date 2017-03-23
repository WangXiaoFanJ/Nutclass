package com.dev.nutclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.BrandCardEntity;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BrandGridAdapter extends BaseAdapter {
    private List<BrandCardEntity> list;
    private Context mContext;
    private LayoutInflater inflater;

    public BrandGridAdapter(List<BrandCardEntity> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_grid_view_brand,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String imgUrl = list.get(position).getBranch_url();
        final String brandId = list.get(position).getBrand_id();
        holder.brandLogoIv.setTag(imgUrl);
        Picasso.with(mContext).load(imgUrl).placeholder(R.drawable.icon_default_new).into(holder.brandLogoIv);
        holder.brandNameTv.setText(list.get(position).getBrand_name());
        holder.itemRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(mContext, CourseListActivity.class);
                intent.putExtra(Const.KEY_ID, brandId);
                intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BRAND);
                intent.putExtra("type_name",100);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    private class ViewHolder{
        private ImageView brandLogoIv;
        private TextView brandNameTv;
        private RelativeLayout itemRl;
        public ViewHolder(View convertView) {
            brandLogoIv = (ImageView) convertView.findViewById(R.id.iv_item_grid_brand);
            brandNameTv = (TextView) convertView.findViewById(R.id.tv_item_grid_brand);
            itemRl = (RelativeLayout) convertView.findViewById(R.id.rl_item_grid);
        }
    }
}
