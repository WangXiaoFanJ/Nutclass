package com.dev.nutclass.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.dev.nutclass.R;
import com.dev.nutclass.SnsUtil;
import com.dev.nutclass.adapter.PayOrderInfoAdapter;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.FQEntity;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.PayOrderInfoEntity;
import com.dev.nutclass.parser.SimpleInfoParser;
import com.dev.nutclass.request.OkHttpClientManager;
import com.dev.nutclass.utils.DialogUtils;
import com.dev.nutclass.utils.HttpUtil;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.PayResult;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.view.DiscountCouponView;
import com.dev.nutclass.view.FQView;
import com.dev.nutclass.view.ReqAddressView;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayActivityNew extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "PayActivityNew";
    private ListView listView;
    private String orderInfo;
    private String token;
    private String userId;
    private Context mContext;
    private TextView payTypeTv;
    private TextView totalPriceTv;
    private PayOrderInfoAdapter adapter;
    private PayOrderInfoEntity.DataBean dataBean;
    private TextView payTypeStr;
    private TextView payTv;
    private List<FQEntity> fqList;
    private Double totalPrice = 0.0;
    private String payType;
    private String payFq;
    private Map<String,String> map;
    private AlertDialog mAlertDialog;
    private List<PayOrderInfoEntity.DataBean.OrderDetailBean.BonusInfoListBean> cooponList;
    private List<PayOrderInfoEntity.DataBean.OrderDetailBean.BonusInfoListBean> cooponList02;
    private List<PayOrderInfoEntity.DataBean.OrderDetailBean.BonusInfoListBean> discontCooponList;
    private List<PayOrderInfoEntity.DataBean.OrderDetailBean.BonusInfoListBean> discontCooponList02;
    private List<PayOrderInfoEntity.DataBean.OrderDetailBean> list;
    private PayOrderInfoEntity.DataBean.OrderDetailBean.BonusInfoListBean tempCoopon;
    private PayOrderInfoEntity.DataBean.OrderDetailBean.BonusInfoListBean tempCoopon02;

    private String plusPriceBuyInfo;
    private String isMobile;
    private String mobilePhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay02);
        mContext = PayActivityNew.this;
        initView();
        initIntent();
        initData();
    }



    private void initView() {
        listView = (ListView) findViewById(R.id.list_view);
        payTv = (TextView) findViewById(R.id.tv_pay);
        totalPriceTv = (TextView) findViewById(R.id.tv_total_price);
        payTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(isMobile)) {
                    DialogUtils.showToast(mContext, "请先绑定手机号");
                    Intent intent = new Intent(mContext,RegisterActivity.class);
                    startActivity(intent);
                } else {
                    if (TextUtil.isNotNull(payType)) {
                        reqOrder(Integer.parseInt(payType), "");
                    } else {
                        DialogUtils.showToast(mContext, "请选择支付方式");
                    }

                }
            }
        });

    }

    private void initIntent() {
        orderInfo = getIntent().getStringExtra(Const.KEY_ORDER_INFO);
//        if(getIntent().hasExtra(getIntent().getStringExtra(Const.KEY_PLUS_PRICE_BUY))){
            plusPriceBuyInfo = getIntent().getStringExtra(Const.KEY_PLUS_PRICE_BUY);
            Log.d("===","plusPriceBuy:"+plusPriceBuyInfo);
//        }
        token = SharedPrefUtil.getInstance().getToken();
        userId = SharedPrefUtil.getInstance().getUid();
        map = new HashMap<>();
        map.put("orderInfo",orderInfo);
        map.put("token",token);
        map.put("userId",userId);
        if(TextUtil.isNotNull(plusPriceBuyInfo)){
            map.put("plus_price_buy",plusPriceBuyInfo);
        }
    }

    private void initData() {
        cooponList = new ArrayList<>();
        cooponList02 = new ArrayList<>();
        discontCooponList = new ArrayList<>();
        discontCooponList02 = new ArrayList<>();
        tempCoopon = new PayOrderInfoEntity.DataBean.OrderDetailBean.BonusInfoListBean();
        list = new ArrayList<>();
        reqData();
//        reqfqData();
    }


    private void reqData() {
        String url = UrlConst.REQ_ORDER_INFO_URL;
//        String url = String.format(UrlConst.REQ_ORDER_INFO_URL,token,userId,orderInfo);
        OkHttpClientManager.postAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                LogUtil.d(TAG, "error e=" + e.getMessage());
            }

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "response=" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.optString("status");
                    String info = jsonObject.optString("info");
                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    PayOrderInfoEntity.DataBean dataBean = new PayOrderInfoEntity.DataBean();
                    dataBean.optOrderInfoJson(dataObj);


                    if (dataObj != null && status.equals("1")) {
//                        Gson gson = new Gson();
//                        dataBean= gson.fromJson(dataObj.toString(), PayOrderInfoEntity.DataBean.class);
                        list = dataBean.getOrder_detail();
                        Log.d("===", "databean:" + list.size() + list.toString());
                        setTotalPriceFirst(list);
                        isMobile = dataBean.getIs_mobile_phone();
                        mobilePhone = dataBean.getMobile_phone();
                        Log.d("===","ismobile"+isMobile+"..."+mobilePhone);
                        String isPlusPriceBuy ="0";
                        for(int i = 0;i<dataBean.getOrder_detail().size();i++){
                            if(TextUtil.isNotNull(dataBean.getOrder_detail().get(i).getPlus_price_buy_money())){
                                isPlusPriceBuy = "1";
                            }
                        }

                        if(isPlusPriceBuy .equals("1") &&!dataBean.getIs_address().equals("1")){
                                showAddDialog();
                        }
                        updata(dataBean);
                    }else{
                        DialogUtils.showToast(mContext,info);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        },map);
    }

    private void showAddDialog() {
        mAlertDialog = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_dialog_is_address,null);
        mAlertDialog.setView(view);
        mAlertDialog.setCanceledOnTouchOutside(false);
        ReqAddressView reqAdressView = new ReqAddressView(mContext);
        reqAdressView.updateView(new ReqAddressView.DialogItemClick() {
            @Override
            public void close() {
                mAlertDialog.cancel();
            }

            @Override
            public void commit() {
                Intent intent = new Intent(mContext,AddAddressActivity.class);
                startActivity(intent);
                mAlertDialog.cancel();
            }
        });
//        DiscountCouponView discountCouponView = new DiscountCouponView(context);
//        discountCouponView.updataView(new DiscountCouponView.DialogItemClick() {
//            @Override
//            public void close() {
//                mAlertDialog.cancel();
//            }
//
//            @Override
//            public void commit(String leftEdit, String rightEdit) {
//                reqCode(leftEdit,rightEdit);
//            }
//        });
        mAlertDialog.show();
        mAlertDialog.getWindow().setContentView(reqAdressView);
    }

    private void setTotalPriceFirst(List<PayOrderInfoEntity.DataBean.OrderDetailBean> list) {
        for (int i = 0;i<list.size();i++){
            totalPrice+= Double.parseDouble(list.get(i).getSubtotal());
        }
        totalPriceTv.setText("￥"+totalPrice);
    }

    private void updata(PayOrderInfoEntity.DataBean dataBean) {
        list = dataBean.getOrder_detail();
        adapter = new PayOrderInfoAdapter(mContext, list);
        View view = LayoutInflater.from(mContext).inflate(R.layout.foot_view_orderinfo_pay, null);
        TextView phoneNum = (TextView) view.findViewById(R.id.tv_phone_num);
        if (dataBean.getIs_mobile_phone().equals("1")) {
            phoneNum.setText(dataBean.getMobile_phone());
        }
        payTypeStr = (TextView) view.findViewById(R.id.tv_pay_type_str);
        payTypeTv = (TextView) view.findViewById(R.id.tv_pay_type);
        payTypeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqFQ();

            }
        });
        listView.addFooterView(view,null,false);
        listView.setAdapter(adapter);
        adapter.setSubTotal(this);
    }


    @Override
    protected void handleTitleBarEvent(int eventId) {

    }

    //private void changePayType() {
//    AlertDialog.Builder builder = new AlertDialog.Builder(
//            PayActivityNew.this);
//    final AlertDialog mAlertDialog = builder.create();
//    FQView fqView = new FQView(mContext);
//    Log.d("===","fqList:"+fqList.size());
//    fqView.updateView(fqList,1,
//            new FQView.DialogItemClick() {
//
//                @Override
//                public void ok(String type, int fq1, int fq2) {
//                    // TODO Auto-generated method stub
//                    if (type.equals("3")) {
//                        Log.d("===","paytype:"+dataBean.getPayType());
//                        dataBean.setPayType("3");
//                        mAlertDialog.cancel();
//                        payTypeStr.setText("支付宝支付");
//                    } else if (type.equals("5")) {
//                        dataBean.setPayType("5");
//                        mAlertDialog.cancel();
//                        payTypeStr.setText("微信支付");
//                    } else {
//                        payTypeStr.setText("信用卡分期");
//                        dataBean.setPayType(fqList
//                                .get(fq1).getId());
//                        dataBean.setPayFQ(fqList.get(fq1)
//                                .getList().get(fq2).getId());
//                        mAlertDialog.cancel();
//                    }
//
//                }
//
//                @Override
//                public void cancel() {
//                    // TODO Auto-generated method stub
//                    mAlertDialog.cancel();
//                }
//            });
//    mAlertDialog.show();
//    mAlertDialog.getWindow().setContentView(fqView);
//
//}
    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if (v.getId() == R.id.tv_youhui_str) {
            if (tag != null && tag instanceof Integer) {
                final int position = (Integer) tag;

//                list.get(position).setSubtotal("100");
                if (list.get(position).getDiscount_type().equals("1")) {
                    discontCooponList.clear();
                    tempCoopon.setBonus_id(list.get(position).getBonus_info().getBonus_id());
                    tempCoopon.setUser_id(list.get(position).getBonus_info().getBonus_id());
                    tempCoopon.setType_money(list.get(position).getBonus_info().getType_money());
                    tempCoopon.setType_name(list.get(position).getBonus_info().getType_name());
                    discontCooponList.add(tempCoopon);
                    if(list.get(position).getBonus_info_list().get(0).getBonus_id()!=null){
                        discontCooponList.addAll(list.get(position).getBonus_info_list());
                    }
                    if (discontCooponList != null && discontCooponList.size() > 0) {
                        String[] cooponName = new String[discontCooponList.size() + 1];
                        cooponName[0] = "请选择优惠券";
                        for (int i = 0; i < discontCooponList.size(); i++) {
                            cooponName[i + 1] = discontCooponList.get(i).getType_name();
                        }
                        DialogUtils.showItemsDialog(mContext, cooponName,
                                new DialogUtils.ItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(DialogInterface dialog,
                                                               String text, int which) {
                                        // TODO Auto-generated method stub
                                        if (which == 0) {
//
                                        } else {
                                            totalPrice = totalPrice+(Double.parseDouble(list.get(position).getSubtotal())-Double.parseDouble(list.get(position).getOrigin_price()) * Double.parseDouble(discontCooponList.get(which - 1).getType_money()) / 10);
                                            list.get(position).setSubtotal(String.valueOf(Double.parseDouble(list.get(position).getOrigin_price()) * Double.parseDouble(discontCooponList.get(which - 1).getType_money()) / 10));
                                            list.get(position).setBonusId(discontCooponList.get(which - 1).getBonus_id());
                                            list.get(position).setBonusStr(discontCooponList.get(which - 1).getType_name());
                                            list.get(position).setSubTotalShow(String.valueOf(Double.parseDouble(list.get(position).getOrigin_price()) * Double.parseDouble(discontCooponList.get(which - 1).getType_money()) / 10));
                                            Log.d("===", "改变成功" + discontCooponList.get(which - 1).getBonus_id());
                                            adapter.notifyDataSetChanged();

                                            totalPriceTv.setText("￥"+totalPrice);
                                        }
                                    }

                                });
                    } else {
                        DialogUtils.showToast(mContext, "您暂时没有优惠券");
                    }
                } else if (list.get(position).getDiscount_type().equals("3")) {
                    cooponList = list.get(position).getBonus_info_list();
                    //cooponList02对比数组
                    if (cooponList02 != null && cooponList02.size() > 0) {
                        for (int i = 0; i < cooponList02.size(); i++) {
                            for (int j = 0; j < cooponList.size(); j++) {
                                if (cooponList02.get(i).getBonus_id().equals(cooponList.get(j).getBonus_id())) {
                                    cooponList.remove(j);
                                }
                            }
                        }
                    }
//
                    if (cooponList != null && cooponList.size() > 0) {
                        final String[] cooponName = new String[cooponList.size() + 1];
                        cooponName[0] = "请选择优惠券";
                        for (int i = 0; i < cooponList.size(); i++) {
                            cooponName[i + 1] = cooponList.get(i).getType_name();
//
                        }


                        DialogUtils.showItemsDialog(mContext, cooponName,
                                new DialogUtils.ItemSelectedListener() {

                                    @Override
                                    public void onItemSelected(DialogInterface dialog,
                                                               String text, int which) {
                                        // TODO Auto-generated method stub
                                        String bonusId = list.get(position).getBonusNum();
                                        if (which == 0) {
//                                            String cooponId = "";
//                                            list.get(position).setMinus("请选择优惠券");
//
//                                            if(bonusId!=null){
//                                                for(int i=0;i<cooponList02.size();i++){
//                                                    if(bonusId == cooponList02.get(i).getBonus_id()){
//                                                        cooponList.add(cooponList02.get(i));
//                                                        cooponList02.remove(cooponList02.get(i));
//
//                                                        Log.d("===","不使用");
//                                                    }
//                                                }
//                                            }
                                        } else {
//                                            list.get(position).setMinus(cooponList.get(which - 1).getType_name());

                                            if (bonusId != null && cooponList != null && cooponList02.size() > 0) {
                                                for (int i = 0; i < cooponList02.size(); i++) {
                                                    if (bonusId.equals(cooponList02.get(i).getBonus_id())) {
                                                        Log.d("===", "更换"+cooponList.get(which - 1).getType_money());
                                                        totalPrice = totalPrice+
                                                                (Double.parseDouble(list.get(position).getBinusMoney())- Double.parseDouble(cooponList.get(which - 1).getType_money()));
                                                        list.get(position).setBinusMoney(cooponList.get(which - 1).getType_money());
                                                        list.get(position).setBonusId(cooponList.get(which - 1).getBonus_id());
                                                        list.get(position).setMimusShow(String.valueOf(Double.parseDouble(list.get(position).getMinus())+Double.parseDouble(cooponList.get(which-1).getType_money())));
                                                        list.get(position).setSubTotalShow(String.valueOf(Double.parseDouble(list.get(position).getSubtotal()) - Double.parseDouble(cooponList.get(which - 1).getType_money())));
                                                        cooponList02.add(cooponList.get(which - 1));
                                                        cooponList.add(cooponList02.get(i));
                                                        cooponList.remove(cooponList.get(which - 1));
                                                        cooponList02.remove(cooponList02.get(i));
                                                        totalPriceTv.setText("￥"+totalPrice);
                                                        Log.d("===","totalPrice:"+totalPrice);
                                                    }
                                                }
                                            } else {
                                                totalPrice = totalPrice - Double.parseDouble(cooponList.get(which - 1).getType_money());
                                                list.get(position).setBinusMoney(cooponList.get(which - 1).getType_money());
                                                list.get(position).setBonusId(cooponList.get(which - 1).getBonus_id());
                                                list.get(position).setMimusShow(String.valueOf(Double.parseDouble(list.get(position).getMinus())+Double.parseDouble(cooponList.get(which-1).getType_money())));
                                                list.get(position).setSubTotalShow(String.valueOf(Double.parseDouble(list.get(position).getSubtotal()) - Double.parseDouble(cooponList.get(which - 1).getType_money())));
                                                Log.d("===", "添加");
                                                cooponList02.add(cooponList.get(which - 1));
                                                cooponList.remove(cooponList.get(which - 1));
                                                totalPriceTv.setText("￥"+totalPrice);
                                            }
                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                    } else {
                        DialogUtils.showToast(mContext, "您暂时没有优惠券");
                    }
                }

            }

        }
    }

    private void reqFQ() {
//        String url = HttpUtil.addBaseGetParams(String.format(
//                UrlConst.GET_FQ_LIST, pid, "",orderId));
        final String fqUrl = "http://new.kobiko.cn/Api/Appapi/get_bank_fq?goods_id=1036&attr_id=1036&order_sn=6427";
        OkHttpClientManager.getAsyn(fqUrl,
                new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        // TODO Auto-generated method stub
                        LogUtil.d(TAG, "error e=" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtil.d(TAG, "response=" + response);
                        JSONObject baseJson;
                        try {
                            baseJson = new JSONObject(response);
                            if (baseJson != null
                                    && baseJson.optInt("status", 0) == 1) {
                                JSONArray dataJson = baseJson
                                        .optJSONArray("data");
                                if (dataJson != null && dataJson.length() > 0) {
                                    final List<FQEntity> fqList = new ArrayList<FQEntity>();
                                    for (int i = 0; i < dataJson.length(); i++) {
                                        FQEntity fqEntity = new FQEntity(
                                                dataJson.optJSONObject(i));
                                        fqList.add(fqEntity);
                                    }
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            PayActivityNew.this);
                                    final AlertDialog mAlertDialog = builder
                                            .create();
                                    FQView fqView = new FQView(mContext);
                                    fqView.updateView(fqList, 1,
                                            new FQView.DialogItemClick() {

                                                @Override
                                                public void ok(String type,
                                                               int fq1, int fq2) {
                                                    // TODO Auto-generated
                                                    // method stub
                                                    if (type.equals("3")) {
                                                        payType = "3";
                                                        mAlertDialog.cancel();
                                                        payTypeStr.setText("支付宝支付");
//                                                        reqOrder(Integer.parseInt(type),"");
                                                    } else if (type.equals("5")) {
                                                        payType = "5";
                                                        mAlertDialog.cancel();
                                                        payTypeStr.setText("微信支付");
//                                                        reqOrder(Integer.parseInt(type),"");
                                                    } else {
                                                        payTypeStr.setText("信用卡分期");
                                                        payType = fqList
                                                                .get(fq1).getId();
                                                        payFq = fqList.get(fq1)
                                                                .getList().get(fq2).getId();
//                                                        dataBean.setPayType(fqList
//                                                                .get(fq1).getId());
//                                                        dataBean.setPayFQ(fqList.get(fq1)
//                                                                .getList().get(fq2).getId());
//                                                        reqOrder(Integer.parseInt(fqList.get(fq1).getId()),fqList.get(fq1).getList().get(fq2).getId());
                                                        mAlertDialog.cancel();
                                                    }
                                                    Log.d("===", "pay:" + payType + payFq);
                                                }

                                                @Override
                                                public void cancel() {
                                                    // TODO Auto-generated
                                                    // method stub
                                                    mAlertDialog.cancel();
                                                }
                                            });
                                    mAlertDialog.show();
                                    mAlertDialog.getWindow().setContentView(
                                            fqView);


                                } else {
                                    DialogUtils.showToast(mContext,
                                            baseJson.optString("info"));
                                }

                            } else {
                                DialogUtils.showToast(mContext,
                                        baseJson.optString("info"));
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });

    }

    private static final int SDK_PAY_FLAG = 1;
    private IWXAPI api;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        DialogUtils.showToast(mContext, "支付成功");
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            DialogUtils.showToast(mContext, "支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            DialogUtils.showToast(mContext, "支付失败,订单已存放在待付款页面");

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /*
   * 网络请求数据
   */
    private void reqOrder(final int type, final String pay_fq) {
        StringBuilder orderListId =  new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String goodsId = list.get(i).getGoods_id();
            String xiaoquId = list.get(i).getXiaoqu_id();
            String attrId = list.get(i).getGoods_attr_id();
            String retIds = null;

            if (list.get(i).getBonusId() != null) {
                String bonusId = list.get(i).getBonusId();
                retIds = goodsId + "-" + xiaoquId + "-" + attrId + "-" + bonusId;
            } else {
                if(list.get(i).getDiscount_type().equals("1")&&list.get(i).getBonus_info().getBonus_id()!=null){
                    retIds = goodsId + "-" + xiaoquId + "-" + attrId + "-" + list.get(i).getBonus_info().getBonus_id();
                }else{
                    retIds = goodsId + "-" + xiaoquId + "-" + attrId + "-" + "0";
                }

            }
            orderListId.append(retIds);
            if (list.size() > 1 && i < list.size() - 1) {
                orderListId.append(",");
            }

        }
        api = WXAPIFactory.createWXAPI(getApplicationContext(),
                SnsUtil.WEIXIN_APP_ID);

        String url = null;
//        if (this.type == FROM_DETAIL) {
//        Log.d("===","orderListId"+orderListId+dataBean.getMobile_phone());
        url = HttpUtil.addBaseGetParams(String.format(
                UrlConst.COURSE_ORDER_NEW_URL,plusPriceBuyInfo, orderListId,
                mobilePhone, type,
                SharedPrefUtil.getInstance().getVersionName(),
                payFq));
//        }
//        else {
//            url = HttpUtil.addBaseGetParams(String.format(
//                    UrlConst.COURSE_ORDER_BY_ORDERID_URL, orderId, type,pay_fq));
//        }
        OkHttpClientManager.getAsyn(url,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        // TODO Auto-generated method stub
                        LogUtil.d(TAG, "error e=" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        LogUtil.d(TAG, "response=" + response);
                        SimpleInfoParser parser = new SimpleInfoParser();
                        JsonResult<String> result = (JsonResult<String>) parser
                                .parse(response);
                        if (result.getErrorCode() == UrlConst.SUCCESS_CODE) {
                            JSONObject jsonData;
                            try {
                                jsonData = new JSONObject(response);
                                JSONObject json = jsonData
                                        .optJSONObject("data");
                                if (type == 3) {// 支付宝支付
                                    String orderInfo = json
                                            .optString("orderInfo");
                                    String sign = json.optString("sign");
                                    try {
                                        orderInfo = URLDecoder.decode(
                                                orderInfo, "utf-8");
                                        // sign = URLDecoder.decode(
                                        // sign, "utf-8");
                                    } catch (UnsupportedEncodingException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                    /**
                                     * 完整的符合支付宝参数规范的订单信息
                                     */
                                    final String payInfo = orderInfo
                                            + "&sign=\"" + sign + "\"&"
                                            + getSignType();

                                    Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {
                                            // 构造PayTask 对象
                                            PayTask alipay = new PayTask(
                                                    (Activity) mContext);
                                            // 调用支付接口，获取支付结果
                                            String result = alipay.pay(
                                                    payInfo, true);

                                            Message msg = new Message();
                                            msg.what = SDK_PAY_FLAG;
                                            msg.obj = result;
                                            mHandler.sendMessage(msg);
                                        }
                                    };

                                    // 必须异步调用
                                    Thread payThread = new Thread(
                                            payRunnable);
                                    payThread.start();
                                } else if (type == 5) {// 微信支付
                                    if (null != json) {
                                        PayReq req = new PayReq();
                                        // req.appId = "wxf8b4f85f3a794e77";
                                        // // 测试用appId
                                        req.appId = json.optString("appid");
                                        req.partnerId = json
                                                .optString("partnerid");
                                        req.prepayId = json
                                                .optString("prepayid");
                                        req.nonceStr = json
                                                .optString("noncestr");
                                        req.timeStamp = json
                                                .optString("timestamp");
                                        req.packageValue = json
                                                .optString("package");
                                        req.sign = json.optString("sign");
                                        req.extData = "app data"; // optional
                                        Toast.makeText(mContext, "正常调起支付",
                                                Toast.LENGTH_SHORT).show();
                                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                        // api.registerApp(SnsUtil.WEIXIN_APP_ID);
                                        boolean a = api.sendReq(req);
                                        Log.d("PAY_GET", "a=" + a);
                                    } else {
                                        Log.d("PAY_GET",
                                                "返回错误"
                                                        + json.getString("retmsg"));
                                        Toast.makeText(
                                                mContext,
                                                "返回错误"
                                                        + json.getString("retmsg"),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    DialogUtils.showToast(mContext,
                                            jsonData.optString("info"));
                                    String url = jsonData.optString("data");
                                    Intent intent = new Intent(mContext,
                                            WebViewActivity.class);
                                    intent.putExtra(Const.KEY_FROM, "1");
                                    intent.putExtra(Const.KEY_URL, url);
                                    intent.putExtra(Const.KEY_TITLE,
                                            "信用卡支付");
                                    mContext.startActivity(intent);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }
                        DialogUtils.showToast(mContext, result.getErrorMsg());
                    }
                });


    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    public Double getTotalPrice(int position) {
        for (int i = 0;i<list.size();i++){
            if(list.get(position).getDiscount_type().equals("1")){
                if(TextUtil.isNotNull(list.get(position).getBonusStr())){
                    totalPrice+=Double.parseDouble(list.get(position).getSubTotalShow());
                }else{
                    totalPrice+=Double.parseDouble(list.get(position).getSubtotal());
                }
            }else if (list.get(position).getDiscount_type().equals("2")){
                totalPrice+=Double.parseDouble(list.get(position).getSubtotal());
            }else if (list.get(position).getDiscount_type().equals("3")){
                if(TextUtil.isNotNull(list.get(position).getBonusId())){
                    totalPrice+=Double.parseDouble(list.get(position).getSubTotalShow());
                    Log.d("===","有优惠券");
                }else{
                    totalPrice+=Double.parseDouble(list.get(position).getSubtotal());
                    Log.d("===","无优惠券");
                }
            }
        }
        return totalPrice;
    }
}

