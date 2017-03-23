package com.dev.nutclass.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/8.
 */
public class ADAlertEntity extends BaseCardEntity {
    private int tan_status;
    private String img;
    private String app_jump_value;
    private String app_jump_key;

    public int getTan_status() {
        return tan_status;
    }

    public void setTan_status(int tan_status) {
        this.tan_status = tan_status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getApp_jump_value() {
        return app_jump_value;
    }

    public void setApp_jump_value(String app_jump_value) {
        this.app_jump_value = app_jump_value;
    }

    public String getApp_jump_key() {
        return app_jump_key;
    }

    public void setApp_jump_key(String app_jump_key) {
        this.app_jump_key = app_jump_key;
    }
    public ADAlertEntity(int type,JSONObject jsonObject) {
        setCardType(type);
        optJsonObj(jsonObject);
    }

    private void optJsonObj(JSONObject jsonObj) {
        if (jsonObj == null)
            return;
        setTan_status(jsonObj.optInt("tan_status"));
        setImg(jsonObj.optString("img"));
        setApp_jump_value(jsonObj.optString("app_jump_value"));
        setApp_jump_key(jsonObj.optString("app_jump_key"));
    }
}
