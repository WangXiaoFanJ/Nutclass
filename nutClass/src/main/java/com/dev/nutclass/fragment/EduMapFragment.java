package com.dev.nutclass.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.dev.nutclass.NutClassApplication;
import com.dev.nutclass.R;
import com.dev.nutclass.activity.CourseListActivity;
import com.dev.nutclass.activity.ReqDiscountActivity;
import com.dev.nutclass.activity.SearchActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.EduMapEntity;
import com.dev.nutclass.image.control.ImgConfig;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.view.HomeNavView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EduMapFragment extends BaseFragment implements AMap.OnMarkerClickListener, LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener, View.OnClickListener {
    private static final String TAG = "EduMapFragment";
    private HomeNavView navView;
    private Context mContext;

    private float doublePointDistance;
    private String id;
    private String tel;
    private String schoolName;
    private String schoolCate;
    private String schoolAddress;
    private String markerLatitude;
    private String markerLongitude;
    private Double locationLatitude;
    private Double locationLongitude;
    private String catId;

    private Double centerLatitude;
    private Double centerLongitude;
    private AMap aMap;
    private MapView mMapView = null;
    private UiSettings mUiSettings;
    private List<LatLng> LatLngList = null;
    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;

    private TextView schoolNameTv;
    private ImageView schoolIconImage;
    private TextView distanceTv;
    private TextView backMoneyTv;
    private TextView schoolAddressTv;
    private TextView catNameTv;
//    private ImageView openNavigationIv;
    private LinearLayout courseDetailLayout;
    private LinearLayout telLayout;
    private LinearLayout allOrderlayout;
    private LinearLayout allOrderlayout02;
    private LinearLayout schoolCardLayout;
    private TextView schoolNameTv02;
    private TextView distanceTv02;
    private TextView schoolAddressTv02;
    private ImageView lockPostionBtn;
    private Marker tempMarker;
    private int tempStatus;
    private List<EduMapEntity.DataBean.SchoolListBean> schoolList;
    private LatLng locationPoint;
    private ImageView reqDiscountBtn;
    private LinearLayout reqCatLayout;
    private LinearLayout searchLayout;
    private TabLayout tabLayout;

    private RadioButton radioButton01;
    private RadioButton radioButton02;
    private RadioButton radioButton03;
    private RadioButton radioButton04;
    private RadioButton radioButton05;
    private RadioButton radioButton06;
    private RadioButton radioButton07;
    private RadioButton radioButton08;
    private RadioButton radioButton09;
    public EduMapFragment() {
        // Required empty public constructor
    }

    public static EduMapFragment newInstance() {
        EduMapFragment eduMapFragment = new EduMapFragment();

        return eduMapFragment;

    }

    public void setHomeNavView(HomeNavView navView) {
        this.navView = navView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_edu_map, null);
        String[] filters = {Const.ACTION_BROADCAST_LOGIN_SUCC,
                Const.ACTION_BROADCAST_FEED_CHANGED,
                Const.ACTION_BROADCAST_RECEIVE_MESSAGE,
                Const.ACTION_BROADCAST_FEED_RELEASE_CHANGED,
                Const.ACTION_BROADCAST_FEED_CREATE};
        registerReceiver(filters);

        //获取地图控件引用
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initView(view);
        initListener();
        init();
        return view;
    }

    private void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        schoolNameTv = (TextView) view.findViewById(R.id.tv_school_name);
        schoolIconImage = (ImageView) view.findViewById(R.id.iv_school_icon);
        distanceTv = (TextView) view.findViewById(R.id.tv_distance);
        backMoneyTv = (TextView) view.findViewById(R.id.tv_back_money);
        schoolAddressTv = (TextView) view.findViewById(R.id.tv_school_address);
        courseDetailLayout = (LinearLayout) view.findViewById(R.id.ll_course_detail);
        telLayout = (LinearLayout) view.findViewById(R.id.ll_tel);
        catNameTv = (TextView) view.findViewById(R.id.tv_cat_name);
//        openNavigationIv = (ImageView) view.findViewById(R.id.iv_open_navigation);
        allOrderlayout = (LinearLayout) view.findViewById(R.id.ll_all_info);
        allOrderlayout02 = (LinearLayout) view.findViewById(R.id.ll_all_info_02);
        schoolNameTv02 = (TextView) view.findViewById(R.id.tv_school_name_02);
        distanceTv02 = (TextView) view.findViewById(R.id.tv_distance_02);
        schoolAddressTv02 = (TextView) view.findViewById(R.id.tv_school_address_02);
        lockPostionBtn = (ImageView) view.findViewById(R.id.btn_lock_position);
        reqDiscountBtn = (ImageView) view.findViewById(R.id.btn_want_discount);
        reqCatLayout = (LinearLayout) view.findViewById(R.id.ll_cat);
        schoolCardLayout = (LinearLayout) view.findViewById(R.id.ll_card_school);
        searchLayout = (LinearLayout) view.findViewById(R.id.ll_search);

//        radioButton01 = (RadioButton) view.findViewById(R.id.radio_01);
//        radioButton02 = (RadioButton) view.findViewById(R.id.radio_02);
//        radioButton03 = (RadioButton) view.findViewById(R.id.radio_03);
//        radioButton04 = (RadioButton) view.findViewById(R.id.radio_04);
//        radioButton05 = (RadioButton) view.findViewById(R.id.radio_05);
//        radioButton06 = (RadioButton) view.findViewById(R.id.radio_06);
//        radioButton07 = (RadioButton) view.findViewById(R.id.radio_07);
//        radioButton08 = (RadioButton) view.findViewById(R.id.radio_08);
//        radioButton09 = (RadioButton) view.findViewById(R.id.radio_09);
    }

    private void initListener() {
        courseDetailLayout.setOnClickListener(this);
        telLayout.setOnClickListener(this);
//        openNavigationIv.setOnClickListener(this);
        lockPostionBtn.setOnClickListener(this);
        reqDiscountBtn.setOnClickListener(this);
        reqCatLayout.setOnClickListener(this);
        schoolCardLayout.setOnClickListener(this);
        searchLayout.setOnClickListener(this);

//        radioButton01.setOnClickListener(this);
//        radioButton01.setChecked(true);
//        radioButton02.setOnClickListener(this);
//        radioButton03.setOnClickListener(this);
//        radioButton04.setOnClickListener(this);
//        radioButton05.setOnClickListener(this);
//        radioButton06.setOnClickListener(this);
//        radioButton07.setOnClickListener(this);
//        radioButton08.setOnClickListener(this);
//        radioButton09.setOnClickListener(this);
    }


    private void init() {
        initTabLayout();
        catId = "0";
        centerLatitude = Double.parseDouble(SharedPrefUtil.getInstance().getLat());
        centerLongitude = Double.parseDouble(SharedPrefUtil.getInstance().getLon());
        schoolList = new ArrayList<>();
        LatLngList = new ArrayList<>();
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
        reqData(Double.parseDouble(SharedPrefUtil.getInstance().getLat()),Double.parseDouble(SharedPrefUtil.getInstance().getLon()), "0");
    }

    private void initTabLayout() {
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_text_selected));
        tabLayout.setSelectedTabIndicatorHeight(1);
//        tabLayout.setTabTextColors(R.color.color_text_normal,R.color.color_text_selected);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        List<String> titleList =new ArrayList<>();
        titleList.add("全部");
        titleList.add("早教幼儿");
        titleList.add("语言培训");
        titleList.add("素质教育");
        titleList.add("课外辅导");
        titleList.add("游学营队");
        titleList.add("在线教育");
        titleList.add("亲子摄影");
        titleList.add("其他");
        for (int i = 0;i<titleList.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(titleList.get(i)));
        }
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.equals(tabLayout.getTabAt(0))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude, "0");
                    catId ="0";
                }else if(tab.equals(tabLayout.getTabAt(1))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude, "1");
                    catId ="1";
                }else if (tab.equals(tabLayout.getTabAt(2))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude,"2");
                    catId ="2";
                }else if (tab.equals(tabLayout.getTabAt(3))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude, "3");
                    catId ="3";
                } else if (tab.equals(tabLayout.getTabAt(4))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude, "4");
                    catId ="4";
                }else if (tab.equals(tabLayout.getTabAt(5))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude, "5");
                    catId ="5";
                }else if (tab.equals(tabLayout.getTabAt(6))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude, "6");
                    catId ="6";
                }else if (tab.equals(tabLayout.getTabAt(7))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude, "7");
                    catId ="7";
                }else if (tab.equals(tabLayout.getTabAt(8))){
                    aMap.clear();
                    reqData(centerLatitude,centerLongitude, "8");
                    catId ="8";
                }else if (tab.equals(tabLayout.getTabAt(9))){

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpMap() {
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnCameraChangeListener(this);
        //地图定位所需代码
        aMap.setLocationSource(this);// 设置定位监听
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    private void reqData(Double centerLatitude, Double centerLongtude, String cat) {
        String url = UrlConst.REQ_EDU_MAP_URL;
        String urseId = SharedPrefUtil.getInstance().getUid();
        String token = SharedPrefUtil.getInstance().getToken();
        String phone = SharedPrefUtil.getInstance().getMobile();
        String device_token = NutClassApplication.device_token;
        url = String.format(HttpUtil.addBaseGetParams(url), urseId,centerLongtude, centerLatitude,device_token,token,phone,cat);

        OkHttpClientManager.getAsynNoAdd(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                LogUtil.d(TAG, "error e=" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int status = jsonObject.optInt("status");
                    String info = jsonObject.optString("info");
                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    EduMapEntity.DataBean data = new EduMapEntity.DataBean();
                    data.optEduMapJson(dataObj);
                    if (dataObj != null && status == UrlConst.SUCCESS_CODE) {
                        schoolList = data.getSchool_list();
                        //显示第一条数据
//                        showFirstData();
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(data.getMap_lv()));
                        //绘制marker点
                        drawMarker(schoolList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void showFirstData() {
        id = schoolList.get(0).getId();
        tel = schoolList.get(0).getTel();
        markerLatitude = schoolList.get(0).getLatitude();
        markerLongitude = schoolList.get(0).getLongitude();
        if (schoolList.get(0).getIn_status() == 1) {
            allOrderlayout.setVisibility(View.VISIBLE);
//            openNavigationIv.setVisibility(View.VISIBLE);
            allOrderlayout02.setVisibility(View.GONE);
            schoolNameTv.setText(schoolList.get(0).getName());
            ImageLoader.getInstance().displayImage(schoolList.get(0).getIcon(), schoolIconImage, ImgConfig.getCardImgOption());
            distanceTv.setText(schoolList.get(0).getDistance());
            backMoneyTv.setText(schoolList.get(0).getBack_money());
            schoolAddressTv.setText(schoolList.get(0).getAddress());
            catNameTv.setText(schoolList.get(0).getCat_name());
        } else {
            allOrderlayout.setVisibility(View.GONE);
//            openNavigationIv.setVisibility(View.GONE);
            allOrderlayout02.setVisibility(View.VISIBLE);
            schoolNameTv02.setText(schoolList.get(0).getName());
            schoolAddressTv02.setText(schoolList.get(0).getAddress());
            distanceTv02.setText(schoolList.get(0).getDistance());
        }


    }


    private void drawMarker(List<EduMapEntity.DataBean.SchoolListBean> schoolList) {
        LatLngList.clear();
        LatLng latLng = null;
        for (int i = 0; i < schoolList.size(); i++) {
            if (schoolList.get(i).getLatitude() != null && schoolList.get(i).getLongitude() != null) {
                latLng = new LatLng(Double.parseDouble(schoolList.get(i).getLatitude()), Double.parseDouble(schoolList.get(i).getLongitude()));
                LatLngList.add(latLng);
            }
//            final LatLng finalLatLng = latLng;
            if (latLng != null) {
                if (schoolList.get(i).getIn_status() == 1) {
                    aMap.addMarker(new MarkerOptions().
                            position(latLng).
                            icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_enter_noclick))));
                } else if (schoolList.get(i).getIn_status() == 2) {
                    aMap.addMarker(new MarkerOptions().
                            position(latLng).
                            icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_noenter_noclick))));
                }
            }
//            ImageTask task = new ImageTask(new OnImageSuccessListener() {
//
//                @Override
//                public void onImageSuccess(Bitmap bitmap) {
//
//                }
//            });
//            task.execute(schoolList.get(i).getIcon());
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (tempMarker != null) {
            if (tempStatus == 1) {
                tempMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_enter_noclick)));
            } else if (tempStatus == 2) {
                tempMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_noenter_noclick)));
            }

        }
        for (int i = 0; i < schoolList.size(); i++) {
            if (marker.getPosition().equals(LatLngList.get(i))) {
                tempStatus = schoolList.get(i).getIn_status();
                tempMarker = marker;
                if (schoolList.get(i).getIn_status() == 1) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_enter_clicked)));
                    allOrderlayout.setVisibility(View.VISIBLE);
//                    openNavigationIv.setVisibility(View.VISIBLE);
                    allOrderlayout02.setVisibility(View.GONE);
                    schoolNameTv.setText(schoolList.get(i).getName());
                    ImageLoader.getInstance().displayImage(schoolList.get(i).getIcon(), schoolIconImage, ImgConfig.getCardImgOption());
                    distanceTv.setText(schoolList.get(i).getDistance());
                    backMoneyTv.setText(schoolList.get(i).getBack_money());
                    schoolAddressTv.setText(schoolList.get(i).getAddress());
                    catNameTv.setText(schoolList.get(i).getCat_name());
                } else if (schoolList.get(i).getIn_status() == 2) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_noenter_clicked)));
                    allOrderlayout.setVisibility(View.GONE);
//                    openNavigationIv.setVisibility(View.GONE);
                    allOrderlayout02.setVisibility(View.VISIBLE);
                    schoolNameTv02.setText(schoolList.get(i).getName());
                    schoolAddressTv02.setText(schoolList.get(i).getAddress());
                    distanceTv02.setText(schoolList.get(i).getDistance());
                }

                id = schoolList.get(i).getId();
                tel = schoolList.get(i).getTel();
                schoolName = schoolList.get(i).getName();
                schoolCate = schoolList.get(i).getCat_name();
                markerLatitude = schoolList.get(i).getLatitude();
                markerLongitude = schoolList.get(i).getLongitude();
                schoolAddress = schoolList.get(i).getAddress();
            }
        }
        return true;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(mContext);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocation(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                locationLatitude = amapLocation.getLatitude();//获取纬度
                locationLongitude = amapLocation.getLongitude();//获取经度
                locationPoint = new LatLng(locationLatitude,  locationLongitude);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//                reqData(locationLatitude,locationLongitude);
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v == courseDetailLayout||v==schoolCardLayout) {
            if (TextUtil.isNotNull(id)) {
                Intent intent = new Intent();
                intent.setClass(mContext, CourseListActivity.class);
                intent.putExtra(Const.KEY_ID, id);
                intent.putExtra(Const.KEY_TYPE, CourseListActivity.TYPE_FROM_COURSE);
                mContext.startActivity(intent);
            } else {
                DialogUtils.showToast(mContext, "请选择标记点");
            }

        } else if (v == telLayout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(tel);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                            + tel));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }
//        else if (v == openNavigationIv) {
//            if (TextUtil.isNotNull(markerLatitude) && TextUtil.isNotNull(markerLongitude)) {
//                Util.location(mContext,
//                        new AMapLocationListener() {
//
//                            @Override
//                            public void onLocationChanged(
//                                    AMapLocation amapLocation) {
//                                // TODO Auto-generated method stub
//                                if (amapLocation != null) {
//                                    if (amapLocation.getErrorCode() == 0) {
//                                        Intent intent = new Intent(mContext,
//                                                NavActivity.class);
//                                        intent.putExtra("src_lon", String
//                                                .valueOf(amapLocation
//                                                        .getLongitude()));
//                                        intent.putExtra("src_lat", String
//                                                .valueOf(amapLocation
//                                                        .getLatitude()));
//                                        intent.putExtra("dst_lon", markerLongitude);
//                                        intent.putExtra("dst_lat", markerLatitude);
//                                        startActivity(intent);
//                                        Log.e("AmapError", "lon:"
//                                                + amapLocation.getLongitude() + ", lat:"
//                                                + amapLocation.getLatitude() + "district:" + amapLocation.getDistrict());
//                                    } else {
//                                        // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                                        Log.e("AmapError",
//                                                "location Error, ErrCode:"
//                                                        + amapLocation
//                                                        .getErrorCode()
//                                                        + ", errInfo:"
//                                                        + amapLocation
//                                                        .getErrorInfo());
//                                    }
//                                }
//                            }
//                        });
//            } else {
//                DialogUtils.showToast(mContext, "位置信息不全，请选择正确的标记");
//            }
//        }
    else if (v == lockPostionBtn) {
            if (locationLatitude != null && locationLongitude != null) {
                LatLng latLng = new LatLng(locationLatitude, locationLongitude);
                changeCamera(CameraUpdateFactory.changeLatLng(latLng), null);
//                changeCamera(
//                        CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                                latLng, 18, 0, 30)), null);
            }


        } else if (v == reqDiscountBtn) {
            Intent intent = new Intent(mContext, ReqDiscountActivity.class);
            intent.putExtra("tel", tel);
            intent.putExtra("address", schoolAddress);
            intent.putExtra("name", schoolName);
            intent.putExtra("cateName", schoolCate);
            startActivity(intent);
        }else if (v ==reqCatLayout){
            Intent intent = new Intent(mContext,SearchActivity.class);
            intent.putExtra(Const.KEY_KEYWORD,schoolCate);
            startActivity(intent);
        }else if(v == searchLayout){
            Intent intent = new Intent(mContext,SearchActivity.class);
            startActivity(intent);
        }
//        }else if (v == radioButton01){
////            DialogUtils.showToast(mContext,"全部");
//            reqData(centerLatitude,centerLongitude, "0");
//            catId ="0";
//        }else if(v == radioButton02){
////            DialogUtils.showToast(mContext,"radio02");
//            reqData(centerLatitude,centerLongitude, "1");
//            catId ="1";
//        } else if(v == radioButton03){
////            DialogUtils.showToast(mContext,"radio02");
//            reqData(centerLatitude,centerLongitude, "2");
////            reqData(Double.parseDouble(SharedPrefUtil.getInstance().getLat()),Double.parseDouble(SharedPrefUtil.getInstance().getLon()),"2");
//            catId ="2";
//        }else if(v == radioButton04){
////            DialogUtils.showToast(mContext,"radio02");
//            reqData(centerLatitude,centerLongitude, "3");
//            catId ="3";
//        }else if(v == radioButton05){
////            DialogUtils.showToast(mContext,"radio02");
//            reqData(centerLatitude,centerLongitude, "4");
//            catId ="4";
//        }else if(v == radioButton06){
////            DialogUtils.showToast(mContext,"radio02");
//            reqData(centerLatitude,centerLongitude, "5");
//            catId ="5";
//        }else if(v == radioButton07){
////            DialogUtils.showToast(mContext,"radio02");
//            reqData(centerLatitude,centerLongitude, "6");
//            catId ="6";
//        }
//        else if(v == radioButton08){
////            DialogUtils.showToast(mContext,"radio02");
//            reqData(centerLatitude,centerLongitude, "7");
//            catId ="7";
//        }
//        else if(v == radioButton09){
////            DialogUtils.showToast(mContext,"radio02");
//            reqData(centerLatitude,centerLongitude, "8");
//            catId ="8";
//        }
    }

    //camer拖动时重写方法
    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng centerLatLng = aMap.getCameraPosition().target;
        centerLatitude = centerLatLng.latitude;
        centerLongitude = centerLatLng.longitude;
        if (centerLatLng != null && locationPoint != null) {
            doublePointDistance = AMapUtils.calculateLineDistance(centerLatLng, locationPoint);
            Log.d("===", "中心点坐标latitude:" + centerLatitude + "long:" + centerLongitude + "distance" + doublePointDistance);
            if (doublePointDistance > 1000) {
                aMap.clear();
                reqData(centerLatitude, centerLongitude, catId);
                locationPoint = centerLatLng;
            }
        }
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        aMap.moveCamera(update);
    }

}
