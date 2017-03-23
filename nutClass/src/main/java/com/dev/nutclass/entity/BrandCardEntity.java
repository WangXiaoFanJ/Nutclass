package com.dev.nutclass.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/18.
 */
public class BrandCardEntity extends BaseCardEntity {
    private String brand_id;
    private String brand_name;
    private String branch_url;
    private String img;
    private String img_width;
    private String img_height;
    private String app_jump;
    private String app_jump_key;
    private String app_jump_value;
    private String end_time;

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBranch_url() {
        return branch_url;
    }

    public void setBranch_url(String branch_url) {
        this.branch_url = branch_url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg_width() {
        return img_width;
    }

    public void setImg_width(String img_width) {
        this.img_width = img_width;
    }

    public String getImg_height() {
        return img_height;
    }

    public void setImg_height(String img_height) {
        this.img_height = img_height;
    }

    public String getApp_jump() {
        return app_jump;
    }

    public void setApp_jump(String app_jump) {
        this.app_jump = app_jump;
    }

    public String getApp_jump_key() {
        return app_jump_key;
    }

    public void setApp_jump_key(String app_jump_key) {
        this.app_jump_key = app_jump_key;
    }

    public String getApp_jump_value() {
        return app_jump_value;
    }

    public void setApp_jump_value(String app_jump_value) {
        this.app_jump_value = app_jump_value;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public void optJsonObj(JSONObject jsonObject){
        if(jsonObject == null){
            return;
        }
        setBrand_id(jsonObject.optString("brand_id"));
        setBrand_name(jsonObject.optString("brand_name"));
        setBranch_url(jsonObject.optString("branch_url"));
        setImg(jsonObject.optString("img"));
        setImg_width(jsonObject.optString("img_width"));
        setImg_height(jsonObject.optString("img_height"));
        setApp_jump(jsonObject.optString("app_jump"));
        setApp_jump_key(jsonObject.optString("app_jump_key"));
        setApp_jump_value(jsonObject.optString("app_jump_value"));
        setEnd_time(jsonObject.optString("end_time"));
    }
}
