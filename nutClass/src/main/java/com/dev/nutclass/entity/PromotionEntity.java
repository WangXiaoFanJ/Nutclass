package com.dev.nutclass.entity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/2.
 */
public class PromotionEntity implements Serializable {
    private String icon;
    private String icon_str;
    public PromotionEntity(JSONObject jsonObj){
        optJsonObj(jsonObj);
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_str() {
        return icon_str;
    }

    public void setIcon_str(String icon_str) {
        this.icon_str = icon_str;
    }

    public void optJsonObj(JSONObject jsonObject) {
        setIcon(jsonObject.optString("inco"));
        setIcon_str(jsonObject.optString("inco_str"));
    }
}
