package com.dev.nutclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
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
import com.dev.nutclass.entity.NewActionEntityTwo;
import com.dev.nutclass.image.control.ImgConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2016/11/23.
 */
public class NewActivityTwotAdapter extends BaseAdapter {
    private Context context;
    private List<NewActionEntityTwo.DataBean.TypeBean.GoodsBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    public NewActivityTwotAdapter(Context context, List<NewActionEntityTwo.DataBean.TypeBean.GoodsBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
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
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_new_action_two,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(TextUtils.isEmpty(list.get(position).getRight_goods_id())){
           viewHolder.rightRootLayout.setVisibility(View.INVISIBLE);
        }else{
            viewHolder.rightRootLayout.setVisibility(View.VISIBLE);
        }
        viewHolder.leftName.setText(list.get(position).getLeft_goods_name());
        viewHolder.rightName.setText(list.get(position).getRight_goods_name());
        viewHolder.leftPrice.setText(list.get(position).getLeft_shop_price());
        viewHolder.rightPrice.setText(list.get(position).getRight_shop_price());
        viewHolder.leftHour.setText(list.get(position).getLeft_icon_str());
        viewHolder.rightHour.setText(list.get(position).getRight_icon_str());
        ImageLoader.getInstance().displayImage(list.get(position).getLeft_goods_thumb(),viewHolder.leftIv,ImgConfig.getAdItemOption());
        ImageLoader.getInstance().displayImage(list.get(position).getRight_goods_thumg(),viewHolder.rightIv,ImgConfig.getAdItemOption());
        ImageLoader.getInstance().displayImage(list.get(position).getLeft_icon_img(),viewHolder.leftIcon,ImgConfig.getAdItemOption());
        ImageLoader.getInstance().displayImage(list.get(position).getRight_icon_img(),viewHolder.rightIcon,ImgConfig.getAdItemOption());
        viewHolder.leftRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, CourseInfoActivity.class);
                intent.putExtra(Const.KEY_ID, list.get(position).getLeft_goods_id());
//                intent.putExtra(Const.KEY_SCHOOL_ID,entity.getSchoolId());
                context.startActivity(intent);
            }
        });
        viewHolder.rightRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, CourseInfoActivity.class);
                intent.putExtra(Const.KEY_ID, list.get(position).getRight_goods_id());
//                intent.putExtra(Const.KEY_SCHOOL_ID,entity.getSchoolId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    class ViewHolder{
        private TextView leftName;
        private TextView rightName;
        private ImageView leftIv;
        private ImageView rightIv;
        private TextView leftPrice;
        private TextView rightPrice;
        private TextView leftHour;
        private TextView rightHour;
        private LinearLayout leftRootLayout;
        private LinearLayout rightRootLayout;
        private ImageView leftIcon;
        private ImageView rightIcon;
        public ViewHolder(View convertView) {
           leftName = (TextView) convertView.findViewById(R.id.tv_left_name);
            rightName = (TextView) convertView.findViewById(R.id.tv_right_name);
            leftIv = (ImageView) convertView.findViewById(R.id.iv_left);
            rightIv = (ImageView) convertView.findViewById(R.id.iv_right);
            leftPrice = (TextView) convertView.findViewById(R.id.tv_left_price);
            rightPrice = (TextView) convertView.findViewById(R.id.tv_right_price);
            leftHour = (TextView) convertView.findViewById(R.id.tv_left_hour);
            rightHour = (TextView) convertView.findViewById(R.id.tv_right_hour);
            leftRootLayout = (LinearLayout) convertView.findViewById(R.id.ll_left_root);
            rightRootLayout = (LinearLayout) convertView.findViewById(R.id.ll_right_root);
            leftIcon = (ImageView) convertView.findViewById(R.id.iv_left_icon);
            rightIcon = (ImageView) convertView.findViewById(R.id.iv_right_icon);
        }
    }
}
