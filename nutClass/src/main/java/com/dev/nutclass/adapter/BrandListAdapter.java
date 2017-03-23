package com.dev.nutclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseInfoActivity;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.entity.BrandCardEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.view.NewestActionGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */
public class BrandListAdapter extends BaseAdapter {
    private Context mContext;
    private List<List<BrandCardEntity>> lists;
    private LayoutInflater inflater;
    private List<BrandCardEntity> gridViewList;
    public BrandListAdapter(Context mContext, List<List<BrandCardEntity>> lists) {
        this.mContext = mContext;
        this.lists = lists;
        inflater = LayoutInflater.from(mContext);
        gridViewList =  new ArrayList<>();
    }

    @Override
    public int getCount() {
        return lists.size();
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
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.view_list_brand,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String imgUrl = lists.get(position).get(0).getImg();
        Log.d("===","path:"+imgUrl);
        gridViewList.clear();
        if(!TextUtils.isEmpty(imgUrl)){
            holder.adImageView.setVisibility(View.VISIBLE);
//                Picasso.with(mContext).load(imgUrl).into(holder.adImageView);
            ImageLoader.getInstance().displayImage(imgUrl, holder.adImageView, ImgConfig.getCardImgOption(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    int width;
                    int height;
                    width = Integer.parseInt(lists.get(position).get(0).getImg_width());
                    height = Integer.parseInt(lists.get(position).get(0).getImg_height());
                    int targetW = 0;
                    int targetH = 0;
                    targetW=DensityUtil.getDisplayWidth(mContext);
                    targetH = targetW*height/width;
                    Log.d("===","width:"+targetW);
                    RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(targetW,targetH);
                    holder.adImageView.setLayoutParams(params);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
            for (int i = 0;i<lists.get(position).size();i++){
                if(i!=0){
                    gridViewList.add(lists.get(position).get(i));
                }
            }
            final String appJump = lists.get(position).get(0).getApp_jump();
            final String appKey = lists.get(position).get(0).getApp_jump_key();
            final String appValue = lists.get(position).get(0).getApp_jump_value();
            final String brandId = lists.get(position).get(0).getBrand_id();
            holder.adImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(appJump.equals("1")){
                        if(appKey.equals("brand_id")){
                            Intent intent=new Intent();
                            intent.setClass(mContext, CourseListActivity.class);
                            intent.putExtra(Const.KEY_ID, appValue);
                            intent.putExtra(Const.KEY_TYPE,CourseListActivity.TYPE_FROM_BRAND);
                            mContext.startActivity(intent);
                        }else if(appKey.equals("xiaoqu_id")){
                            Intent intent = new Intent();
                            intent.setClass(mContext, CourseListActivity.class);
                            intent.putExtra(Const.KEY_ID, brandId);
                            intent.putExtra(Const.KEY_TYPE, CourseListActivity.TYPE_FROM_COURSE);
                            mContext.startActivity(intent);
                        }else if(appKey.equals("goods_id")){
                            Intent intent = new Intent();
                            intent.setClass(mContext, CourseInfoActivity.class);
                            intent.putExtra(Const.KEY_ID, brandId);
                            intent.putExtra(Const.KEY_SCHOOL_ID,0);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });
        }else{
            holder.adImageView.setVisibility(View.GONE);
            gridViewList.addAll(lists.get(position));
        }
        BrandGridAdapter adapter = new BrandGridAdapter(gridViewList,mContext);
        holder.gridVeiw.setAdapter(adapter);
        return convertView;
    }
    private  class ViewHolder{
        private ImageView adImageView;
        private NewestActionGridView gridVeiw;

        public ViewHolder(View convertView) {
            adImageView = (ImageView) convertView.findViewById(R.id.iv_brand_ad);
            gridVeiw = (NewestActionGridView) convertView.findViewById(R.id.grid_view_brand);
        }
    }
}
