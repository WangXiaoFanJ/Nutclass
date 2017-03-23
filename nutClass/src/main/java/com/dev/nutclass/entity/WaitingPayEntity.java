package com.dev.nutclass.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */
public class WaitingPayEntity {
        private String goods_id;
        private String goods_name;
        private String goods_price;
        private String goods_attr;
        private String school_name;
        private String goods_thumb;
        private String school_addr;

    public WaitingPayEntity(JSONObject jsonObject) {
        optJsonObj(jsonObject);
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

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getGoods_attr() {
            return goods_attr;
        }

        public void setGoods_attr(String goods_attr) {
            this.goods_attr = goods_attr;
        }

        public String getSchool_name() {
            return school_name;
        }

        public void setSchool_name(String school_name) {
            this.school_name = school_name;
        }

        public String getGoods_thumb() {
            return goods_thumb;
        }

        public void setGoods_thumb(String goods_thumb) {
            this.goods_thumb = goods_thumb;
        }

        public String getSchool_addr() {
            return school_addr;
        }

        public void setSchool_addr(String school_addr) {
            this.school_addr = school_addr;
        }

        private List<PromotionEntity> promotionBeanList;

        public List<PromotionEntity> getPromotionBeanList() {
            return promotionBeanList;
        }

        public void setPromotionBeanList(List<PromotionEntity> promotionBeanList) {
            this.promotionBeanList = promotionBeanList;
        }

    public void optJsonObj(JSONObject jsonObject){
        setGoods_name(jsonObject.optString("goods_name"));
        setGoods_id(jsonObject.optString("goods_id"));
        setGoods_price(jsonObject.optString("goods_price"));
        setGoods_thumb(jsonObject.optString("goods_thumb"));
        setSchool_addr(jsonObject.optString("school_addr"));
        setSchool_name(jsonObject.optString("school_name"));
        setGoods_attr(jsonObject.optString("goods_attr"));
        JSONArray jsonArray = jsonObject.optJSONArray("promotion_list");
        if(jsonArray!=null && jsonArray.length()>0){
            List<PromotionEntity> list1 = new ArrayList<>();
            for (int j = 0;j<jsonArray.length();j++){
                JSONObject promotionObj = jsonArray.optJSONObject(j);
                PromotionEntity promotionBean = new PromotionEntity(promotionObj);
//						promotionBean.optJsonObj(promotionObj);
                list1.add(promotionBean);
            }
            setPromotionBeanList(list1);
        }
    }
}
