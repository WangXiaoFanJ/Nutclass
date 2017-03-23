package com.dev.nutclass.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dev.nutclass.R;
import com.dev.nutclass.adapter.ActionGridViewAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.NewestActionEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DensityUtil;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.view.NewestActionGridView;
import com.dev.nutclass.view.TitleBar;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.Request;

import com.umeng.socialize.UMShareAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewestActionActivity extends  BaseActivity implements View.OnClickListener {
    public  final  static String TAG = "NewestActionActivity";
    public static String background_color;
    private TitleBar titleBar;
    private Context mContext;
    private String head_img;
    private String head_img_url;
    private String head_img2;
    private String head_img_url2;
    private  String id;
    private List<NewestActionEntity.DataBean.TypeBean> typeBeanList;
    private List<NewestActionEntity.DataBean.TypeBean.GoodsBean>   goodsBeanList = new ArrayList<>();
    private JSONObject jsonObject;
    private JSONObject jsonObject2;
    private int status;
    private   NewestActionEntity.DataBean dataBean;
    private String info;

    private ImageView headIv01;
    private ImageView headIv02;
    private ImageView footIv;

    private ScrollView scrollView;
    private LinearLayout stopTopTabLayout;
    private LinearLayout runTabLayout;
    private   String imgUrl;
    private NewestActionGridView newestActionGridView;
    private ActionGridViewAdapter adapter;


    private int[] location = new int[2];
    private int[] location2 = new int[2];
    private int lastY = 0;
    private int touchEventId = -9983761;
    private ProgressDialog progressDialog = null;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == touchEventId) {
                if (lastY != scrollView.getScrollY()) {
                    //scrollview一直在滚动，会触发
                    handler.sendMessageDelayed(
                            handler.obtainMessage(touchEventId, scrollView), 5);
                    lastY = scrollView.getScrollY();
                    runTabLayout.getLocationOnScreen(location);
                    stopTopTabLayout.getLocationOnScreen(location2);
                    //动的到静的位置时，静的显示。动的实际上还是网上滚动，但我们看到的是静止的那个
                    if (location[1] <= location2[1]) {
                        stopTopTabLayout.setVisibility(View.VISIBLE);
                    } else {
                        //静止的隐藏了
                        stopTopTabLayout.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        intTitle();
        initView();
        initIntent();
        initData();
        initListener();
    }

    private void intTitle() {

    }

    private void initView() {
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        stopTopTabLayout = (LinearLayout) findViewById(R.id.ll_stop_top_tab);
        runTabLayout  = (LinearLayout) findViewById(R.id.ll_run_tab);
        headIv01 = (ImageView) findViewById(R.id.iv_head_01);
        headIv02 = (ImageView) findViewById(R.id.iv_head_02);
        footIv = (ImageView) findViewById(R.id.iv_foot);
        newestActionGridView = (NewestActionGridView) findViewById(R.id.grid_view_action);

        mContext = NewestActionActivity.this;
        titleBar = (TitleBar) findViewById(R.id.tb_title);
        titleBar.removeTitleRight1();
        progressDialog = ProgressDialog.show(mContext,"请稍等...","数据正在获取中...");
    }
    private void initIntent() {
        id = getIntent().getStringExtra("id");
    }
    private void initData() {
        reqData();
        scrollView.smoothScrollTo(0,20);
        newestActionGridView.setFocusable(false);
    }

    private void initListener() {
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //必须两个都搞上，不然会有瑕疵。
                //没有这段，手指按住拖动的时候没有效果
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    handler.sendMessageDelayed(
                            handler.obtainMessage(touchEventId, v), 5);
                }
                //没有这段，手指松开scroll继续滚动的时候，没有效果
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.sendMessageDelayed(
                            handler.obtainMessage(touchEventId, v), 5);
                }
                return false;
            }
        });

    }

    private void reqData() {
        String url = UrlConst.NEWEST_ACTION_URL+id;
        OkHttpClientManager.getAsynNoAdd(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(String response) {
                {
                    Log.d(TAG,"response="+response);
                    typeBeanList= null;
                    try {
                        jsonObject= new JSONObject(response);
                        status= jsonObject.optInt("status");
                        info = jsonObject.optString("info");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(status==UrlConst.SUCCESS_CODE){
                       JSONArray jsonArray= jsonObject.optJSONArray("data");
                        try {
                            jsonObject2 = jsonArray.getJSONObject(0);
                            Gson gson = new Gson();
                            dataBean = gson.fromJson(jsonObject2.toString(), NewestActionEntity.DataBean.class);
                            head_img= dataBean.getBanner_img();
                            head_img_url = dataBean.getBanner_img_url();
                            head_img2 = dataBean.getBanner_img1();
                            head_img_url2 = dataBean.getBanner_img1_url();
                            background_color = dataBean.getBackground_color();
                            typeBeanList =dataBean.getType();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        DialogUtils.showToast(mContext,info);
                        progressDialog.dismiss();
                    }
                    if(typeBeanList!=null && typeBeanList.size()>0){
                        update(typeBeanList);
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void update(List<NewestActionEntity.DataBean.TypeBean> dataBeanList) {
        titleBar.setMiddleText(dataBean.getName());
        showImage();
        showTab(dataBeanList,runTabLayout);
        showTab(dataBeanList,stopTopTabLayout);

        Log.d("===","iamge"+dataBean.getBackground_img());
        if(TextUtil.isNotNull(dataBean.getBackground_img())){
            ImageLoader.getInstance().loadImage(dataBean.getBackground_img(), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    BitmapDrawable drawable = new BitmapDrawable(bitmap);
                    scrollView.setBackgroundDrawable(drawable);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });
        }else{
            scrollView.setBackgroundColor(Color.parseColor(dataBean.getBackground_color()));
        }
//        scrollView.setBackgroundResource(R.drawable.new_action_bg);
        goodsBeanList.addAll(dataBeanList.get(0).getGoods());
        adapter = new ActionGridViewAdapter(goodsBeanList,mContext);
        newestActionGridView.setAdapter(adapter);
    }
    private void showImage() {
        if(TextUtil.isNotNull(head_img)){
//            ImageLoader.getInstance().displayImage(head_img,
//                    headIv01, ImgConfig.getAdItemOption());
            Glide.with(mContext).load(head_img).placeholder(R.drawable.icon_default_new).into(headIv01);
            setPicLinearLayout(headIv01,Float.parseFloat(dataBean.getBanner_img_ratio()));
        }
        if(TextUtil.isNotNull(head_img2)){
            headIv02.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(head_img2,
//                    headIv02, ImgConfig.getAdItemOption());
            Glide.with(mContext).load(head_img2).placeholder(R.drawable.icon_default_new).into(headIv02);
            setPicLinearLayout(headIv02,Float.parseFloat(dataBean.getBanner_img1_ratio()));
        }
        if(TextUtil.isNotNull(head_img_url)){
            imgUrl  = String.format(head_img_url+"user_Id=%s&token=%s&mobile_phone=%s",SharedPrefUtil.getInstance().getUid(),SharedPrefUtil.getInstance().getToken(),SharedPrefUtil.getInstance().getMobile());
            headIv01.setOnClickListener(this);
        }
        if(TextUtil.isNotNull(head_img_url2)){
            imgUrl  = String.format(head_img_url2+"user_Id=%s&token=%s&mobile_phone=%s",SharedPrefUtil.getInstance().getUid(),SharedPrefUtil.getInstance().getToken(),SharedPrefUtil.getInstance().getMobile());
            headIv02.setOnClickListener(this);
        }
        if(TextUtil.isNotNull(dataBean.getFooter_img())){
            footIv.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(dataBean.getFooter_img(),
                    footIv, ImgConfig.getAdItemOption());
            setPicLinearLayout(footIv,Float.parseFloat(dataBean.getFooter_img_ratio()));
        }
        head_img_url = dataBean.getFooter_img_url();
        if(TextUtil.isNotNull(head_img_url)){
            footIv.setOnClickListener(this);
        }

    }
    private List<TextView> viewList = new ArrayList<>();
    private List<TextView> viewList2 = new ArrayList<>();
    private void showTab(final List<NewestActionEntity.DataBean.TypeBean> dataBeanList, final LinearLayout layout) {
        layout.removeAllViews();
//        dataBean.setSelect(dataBeanList.get(0).getId());
        final String bgColor = dataBean.getBackground_color();
        for (int i = 0;i<dataBeanList.size();i++){
            final View view = LayoutInflater.from(mContext).inflate(R.layout.text_item_new_action,null);
            LinearLayout.LayoutParams  itemParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
            itemParams.weight = 1;
            final int finalI = i;
            final TextView tabTv = (TextView) view.findViewById(R.id.tv_item);
            tabTv.setBackgroundColor(Color.parseColor(bgColor));
            tabTv.setText(dataBeanList.get(i).getType_name());
            if(layout == runTabLayout){
                viewList.add(tabTv);
                viewList.get(0).setTextColor(Color.parseColor(bgColor));
                viewList.get(0).setBackgroundColor(getResources().getColor(R.color.color_white));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.sendMessageDelayed(
                                handler.obtainMessage(touchEventId, v), 5);
                        for (int j = 0; j<viewList.size();j++){
                            if(v == layout.getChildAt(j)){
                                viewList.get(j).setTextColor(Color.parseColor(bgColor));
                                viewList.get(j).setBackgroundColor(getResources().getColor(R.color.color_white));
                                viewList2.get(j).setTextColor(Color.parseColor(bgColor));
                                viewList2.get(j).setBackgroundColor(getResources().getColor(R.color.color_white));
                            }else {
                                viewList.get(j).setTextColor(getResources().getColor(R.color.color_white));
                                viewList.get(j).setBackgroundColor(Color.parseColor(bgColor));
                                viewList2.get(j).setTextColor(getResources().getColor(R.color.color_white));
                                viewList2.get(j).setBackgroundColor(Color.parseColor(bgColor));
                            }
                        }
                        goodsBeanList.clear();
                        goodsBeanList.addAll(typeBeanList.get(finalI).getGoods());
                        adapter.notifyDataSetChanged();
                    }
                });

            }else if(layout == stopTopTabLayout){
                viewList2.add(tabTv);
                viewList2.get(0).setTextColor(Color.parseColor(bgColor));
                viewList2.get(0).setBackgroundColor(getResources().getColor(R.color.color_white));
//                viewList2.get(0).setTextColor(getResources().getColor(R.color.color_white));
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handler.sendMessageDelayed(
                                handler.obtainMessage(touchEventId, v), 5);
                        for (int j = 0; j<viewList2.size();j++){
                            if(v == layout.getChildAt(j)){
                                viewList.get(j).setTextColor(Color.parseColor(bgColor));
                                viewList.get(j).setBackgroundColor(getResources().getColor(R.color.color_white));
                                viewList2.get(j).setTextColor(Color.parseColor(bgColor));
                                viewList2.get(j).setBackgroundColor(getResources().getColor(R.color.color_white));
//                                viewList.get(j).setTextColor(Color.parseColor(bgColor));
//                                viewList2.get(j).setTextColor(Color.parseColor(bgColor));
                            }else {
                                viewList.get(j).setTextColor(getResources().getColor(R.color.color_white));
                                viewList.get(j).setBackgroundColor(Color.parseColor(bgColor));
                                viewList2.get(j).setTextColor(getResources().getColor(R.color.color_white));
                                viewList2.get(j).setBackgroundColor(Color.parseColor(bgColor));
//                                viewList.get(j).setTextColor(getResources().getColor(R.color.color_white));
//                                viewList2.get(j).setTextColor(getResources().getColor(R.color.color_white));
                            }
                        }
                        goodsBeanList.clear();
                        goodsBeanList.addAll(typeBeanList.get(finalI).getGoods());
                        adapter.notifyDataSetChanged();
                    }
                });

            }


            layout.addView(view,itemParams);
        }


    }
    private void setPicLinearLayout(ImageView imgView,float ratio){
        int targetW= DensityUtil.getDisplayWidth(mContext);
        int targetH= (int) (targetW/ratio);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(targetW,targetH);
//        params.addRule(LinearLayout.CENTER_IN_PARENT);
        imgView.setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if (v == headIv01){
//            Intent intent = new Intent(mContext, WebViewActivity.class);
//            intent.putExtra(Const.KEY_URL, head_img_url);
//            mContext.startActivity(intent);
            int loginID = dataBean.getBanner_img_url_login();
            if(loginID== UrlConst.SUCCESS_CODE){
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Const.KEY_URL, imgUrl);
                intent.putExtra(Const.KEY_TITLE_H5,dataBean.getShare_info().getTitle());
                intent.putExtra(Const.KEY_CONTEXT_H5,dataBean.getShare_info().getContext());
                intent.putExtra(Const.KEY_IMAGE_H5,dataBean.getShare_info().getImage());
                intent.putExtra(Const.KEY_URL_H5,dataBean.getShare_info().getUrl());
//            Log.d("===","url:"+entity.getH5Url());
//						intent.putExtra(Const.KEY_TITLE, "dd");
                mContext.startActivity(intent);}
            else if(loginID == 0){
                Intent intent = new Intent(mContext, LoginTogetherActivity.class);
                DialogUtils.showToast(mContext,"请登录");
                mContext.startActivity(intent);
            }
        }else if (v == headIv02){
            int loginID = dataBean.getBanner_img1_url_login();
            if(loginID== UrlConst.SUCCESS_CODE){
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Const.KEY_URL, imgUrl);
                intent.putExtra(Const.KEY_TITLE_H5,dataBean.getShare_info().getTitle());
                intent.putExtra(Const.KEY_CONTEXT_H5,dataBean.getShare_info().getContext());
                intent.putExtra(Const.KEY_IMAGE_H5,dataBean.getShare_info().getImage());
                intent.putExtra(Const.KEY_URL_H5,dataBean.getShare_info().getUrl());
//            Log.d("===","url:"+entity.getH5Url());
//						intent.putExtra(Const.KEY_TITLE, "dd");
                mContext.startActivity(intent);
            }else if(loginID == 0){
                Intent intent = new Intent(mContext, LoginTogetherActivity.class);
                DialogUtils.showToast(mContext,"请登录");
                mContext.startActivity(intent);
            }
        }else if(v == footIv){
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra(Const.KEY_URL, dataBean.getFooter_img_url());
            mContext.startActivity(intent);
        }
    }
    @Override
    protected void handleTitleBarEvent(int eventId) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
