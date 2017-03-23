package com.dev.nutclass.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/11.
 */
public class MerchantCardEntity extends  BaseCardEntity {
    public MerchantCardEntity(int i) {
        if (i == 1){
            setCardType(BaseCardEntity.CARD_TYPE_MERCHANT_ORDER);
        }else if (i ==2){
            setCardType(BaseCardEntity.CARD_TYPE_MERCHANT_APPOITNMENT);
        }

    }

    private String id;
    private String order_id;
    private String goods_id;
    private String goods_name;
    private String order_sn;
    private String order_amount;
    private String consignee;
    private String mobile_phone;
    private String time_end;
    private String xiaoqu_name;
    private String is_confirm;
    private int  from = -1;


    //商家后台预约参数
    private String c_time;
    private String nick_name;
    private String xiaoqu_tel;
    private String is_back;

    public String getC_time() {
        return c_time;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getIs_back() {
        return is_back;
    }

    public void setIs_back(String is_back) {
        this.is_back = is_back;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getXiaoqu_tel() {
        return xiaoqu_tel;
    }

    public void setXiaoqu_tel(String xiaoqu_tel) {
        this.xiaoqu_tel = xiaoqu_tel;
    }

    public String getXiaoqu_name() {
        return xiaoqu_name;
    }

    public void setXiaoqu_name(String xiaoqu_name) {
        this.xiaoqu_name = xiaoqu_name;
    }

    public String getIs_confirm() {
        return is_confirm;
    }

    public void setIs_confirm(String is_confirm) {
        this.is_confirm = is_confirm;
    }
//解析商家后台订单和一元购数据
    public void optJsonObjOrder(JSONObject jsonObj){
        if(jsonObj == null){
            return;
        }
        setId(jsonObj.optString("id"));
        setOrder_id(jsonObj.optString("order_id"));
        setGoods_name(jsonObj.optString("goods_name"));
        setOrder_sn(jsonObj.optString("order_sn"));
        setOrder_amount(jsonObj.optString("order_amount"));
        setConsignee(jsonObj.optString("consignee"));
        setMobile_phone(jsonObj.optString("mobile_phone"));
        setTime_end(jsonObj.optString("time_end"));
        setXiaoqu_name(jsonObj.optString("xiaoqu_name"));
        setIs_confirm(jsonObj.optString("is_confirm"));
    }

    //解析商家后台预约数据
    public void optJsonObjAppointment(JSONObject jsonObj){
        if(jsonObj == null){
            return;
        }
        setId(jsonObj.optString("id"));
        setC_time(jsonObj.optString("c_time"));
        setGoods_name(jsonObj.optString("goods_name"));
        setNick_name(jsonObj.optString("nick_name"));
        setMobile_phone(jsonObj.optString("mobile_phone"));
        setXiaoqu_tel(jsonObj.optString("xiaoqu_tel"));
        setXiaoqu_name(jsonObj.optString("xiaoqu_name"));
        setIs_back(jsonObj.optString("is_back"));
        setGoods_id(jsonObj.optString("goods_id"));
    }
}
