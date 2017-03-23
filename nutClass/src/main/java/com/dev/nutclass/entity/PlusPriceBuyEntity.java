package com.dev.nutclass.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/1.
 */
public class PlusPriceBuyEntity implements Serializable {
    private String id;
    private String goodsName;
    private String goodsImgUrl;
    private String goodsDetailUrl;
    private String marketPrice;
    private String curentPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    public String getGoodsDetailUrl() {
        return goodsDetailUrl;
    }

    public void setGoodsDetailUrl(String goodsDetailUrl) {
        this.goodsDetailUrl = goodsDetailUrl;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getCurentPrice() {
        return curentPrice;
    }

    public void setCurentPrice(String curentPrice) {
        this.curentPrice = curentPrice;
    }
}
