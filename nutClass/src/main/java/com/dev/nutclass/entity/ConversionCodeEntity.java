package com.dev.nutclass.entity;

/**
 * Created by Administrator on 2016/10/9.
 */
public class ConversionCodeEntity{
    /**
     * status : 0
     * data :
     * info : 优惠券已使用
     */

    private int status;
    private String data;
    private String info;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
