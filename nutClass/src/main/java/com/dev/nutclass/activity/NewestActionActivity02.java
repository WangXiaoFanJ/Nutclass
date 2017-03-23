package com.dev.nutclass.activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dev.nutclass.ImageLoaderConfig;
import com.dev.nutclass.R;
import com.dev.nutclass.adapter.NewActivityTwotAdapter;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.NewActionEntityTwo;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.view.TitleBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewestActionActivity02 extends BaseActivity {
    public final static String TAG = "NewestActionActivity02";
    private Context mContext;
    private ListView listView;
    private TitleBar titleBar;
    private  List<NewActionEntityTwo.DataBean> list;
    private List<NewActionEntityTwo.DataBean.TypeBean.GoodsBean> goodsLists;
    private LinearLayout rootLayout;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newest_action02);
        mContext = NewestActionActivity02.this;
        initView();
        initIntent();
        initData();
    }

    private void initIntent() {
        id = getIntent().getStringExtra("id");
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_view);
        titleBar = (TitleBar) findViewById(R.id.tb_title);
        rootLayout = (LinearLayout) findViewById(R.id.rl_root);
    }
    private void initData() {
        reqData();
    }
    private void reqData() {
        String url = UrlConst.NEWEST_ACTION_URL;
        url = url+id;
        OkHttpClientManager.getAsynNoAdd(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d(TAG,"error e="+e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response=" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String info = jsonObject.optString("info");
                    if(status == UrlConst.SUCCESS_CODE){
                        JSONArray dataArray = jsonObject.optJSONArray("data");
                        list = new ArrayList<NewActionEntityTwo.DataBean>();
                        for (int i = 0;i<dataArray.length();i++){
                            NewActionEntityTwo.DataBean data = new NewActionEntityTwo.DataBean();
                            JSONObject jsonObject02 = dataArray.optJSONObject(i);
                            data.optDataBean(jsonObject02);
                            list.add(data);
                        }
                        if(TextUtil.isNotNull(list.get(0).getBackground_img())){
                            ImageLoader.getInstance().loadImage(list.get(0).getBackground_img(), new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                                    BitmapDrawable drawable = new BitmapDrawable(bitmap);
                                    rootLayout.setBackgroundDrawable(drawable);
                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }else{
                            rootLayout.setBackgroundColor(Color.parseColor(list.get(0).getBackground_color()));
                        }
                        titleBar.setMiddleText(list.get(0).getName());
                        goodsLists= list.get(0).getType().get(0).getGoodsBeanList();
                        Log.d("===","list:"+goodsLists.size());
                        updata(goodsLists);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void updata(List<NewActionEntityTwo.DataBean.TypeBean.GoodsBean> goodsLists) {
        NewActivityTwotAdapter adapter = new NewActivityTwotAdapter(mContext,goodsLists);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.view_head_action_two,null);
        ImageView iv = (ImageView) headView.findViewById(R.id.iv_head_view);
        ImageLoader.getInstance().displayImage(list.get(0).getBanner_img(),iv,ImgConfig.getAdItemOption());
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void handleTitleBarEvent(int eventId) {

    }
}
