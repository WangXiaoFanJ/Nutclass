package com.dev.nutclass.entity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/27.
 */
public class ActionShareEntity {
    private String status;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void optJsonObj(JSONObject jsonObject){
        setMsg(jsonObject.optString("msg"));
        setStatus(jsonObject.optString("status"));
    }
}
