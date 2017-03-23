package com.dev.nutclass.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/6.
 */
public class PayOrderInfoEntity {

    /**
     * status : 1
     * info : 获取数据成功
     * data : {"user_id":"55883","is_mobile_phone":"1","mobile_phone":"18511525815","order_detail":[{"goods_id":"309","shop_price":"18360.00","goods_name":"启想艺术课周末班","goods_thumb":"http://cdn1.kobiko.cn/images/201505/thumb_img/309_thumb_G_1432458877768.jpg","brand_id":"100","cat_list":"少儿教育,绘画课程","cat_id":"10","goods_img":"images/201505/goods_img/309_G_1432458877214.jpg","is_on_sale":"1","brand_name":"启想艺术","brand_logo":"http://cdn2.kobiko.cn/1432457964591740656.jpg","goods_attr_id":"1328","attr_id":"151","attr_value":"120","attr_price":"0","xiaoqu_id":"175","back_money":"500.00","attr_name":"课时","xiaoqu_name":"启想艺术七圣中街中心","xiaoqu_addr":"朝阳区七圣中街12号院爱琴海购物中心三层","xiaoqu_addr2":"朝阳区七圣中街12号院爱琴海购物中心","discount_type":"3","origin_price":"18360.00","minus":"500.00","subtotal":"17860.00","bonus_info":"","bonus_info_list":[{"bonus_id":"33744","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33745","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33746","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33747","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33748","user_id":"55883","type_name":"注册送100元红包","type_money":1000}]},{"goods_id":"424","shop_price":"16800.00","goods_name":"罗克尼外教英语课程（100节）","goods_thumb":"http://cdn2.kobiko.cn/images/201507/thumb_img/424_thumb_G_1436887115780.jpg","brand_id":"130","cat_list":"英语教育","cat_id":"23","goods_img":"images/201507/goods_img/424_G_1436887115088.jpg","is_on_sale":"1","brand_name":"罗克尼儿童俱乐部","brand_logo":"http://cdn2.kobiko.cn/1436883024918322171.jpg","goods_attr_id":"1798","attr_id":"153","attr_value":"普通班","attr_price":"0","xiaoqu_id":"259","back_money":"1000.00","attr_name":"课程","xiaoqu_name":"罗克尼方庄芳城园中心","xiaoqu_addr":"北京市丰台区方庄芳城园一区日月天地大厦B座906-907","xiaoqu_addr2":"北京市丰台区方庄芳城园一区日月天地大厦B座","discount_type":"3","origin_price":"16800.00","minus":"1000.00","subtotal":"15800.00","bonus_info":"","bonus_info_list":[{"bonus_id":"33744","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33745","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33746","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33747","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33748","user_id":"55883","type_name":"注册送100元红包","type_money":1000}]},{"goods_id":"662","shop_price":"880.00","goods_name":"贝乐一对一英语在线VIP定制课程（绘本课程）","goods_thumb":"http://cdn1.kobiko.cn/images/201607/thumb_img/662_thumb_G_1469070288008.jpg","brand_id":"214","cat_list":"英语教育,少儿教育","cat_id":"32","goods_img":"images/201607/goods_img/662_G_1469070288047.jpg","is_on_sale":"1","brand_name":"贝乐一对一","brand_logo":"http://cdn2.kobiko.cn/1469069172199569237.png","goods_attr_id":"3778","attr_id":"151","attr_value":"40","attr_price":"0","xiaoqu_id":"542","back_money":"50.00","attr_name":"课时","xiaoqu_name":"贝乐一对一惠新东街校区","xiaoqu_addr":"北京朝阳区惠新东街11号紫光发展大厦A座九层","xiaoqu_addr2":"北京朝阳区惠新东街11号紫光发展大厦A座","discount_type":"3","origin_price":"880.00","minus":"50.00","subtotal":"830.00","bonus_info":"","bonus_info_list":[{"bonus_id":"33744","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33745","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33746","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33747","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33748","user_id":"55883","type_name":"注册送100元红包","type_money":1000}]}],"order_price":"34490.00"}
     */

    private String status;
    private String info;
    /**
     * user_id : 55883
     * is_mobile_phone : 1
     * mobile_phone : 18511525815
     * order_detail : [{"goods_id":"309","shop_price":"18360.00","goods_name":"启想艺术课周末班","goods_thumb":"http://cdn1.kobiko.cn/images/201505/thumb_img/309_thumb_G_1432458877768.jpg","brand_id":"100","cat_list":"少儿教育,绘画课程","cat_id":"10","goods_img":"images/201505/goods_img/309_G_1432458877214.jpg","is_on_sale":"1","brand_name":"启想艺术","brand_logo":"http://cdn2.kobiko.cn/1432457964591740656.jpg","goods_attr_id":"1328","attr_id":"151","attr_value":"120","attr_price":"0","xiaoqu_id":"175","back_money":"500.00","attr_name":"课时","xiaoqu_name":"启想艺术七圣中街中心","xiaoqu_addr":"朝阳区七圣中街12号院爱琴海购物中心三层","xiaoqu_addr2":"朝阳区七圣中街12号院爱琴海购物中心","discount_type":"3","origin_price":"18360.00","minus":"500.00","subtotal":"17860.00","bonus_info":"","bonus_info_list":[{"bonus_id":"33744","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33745","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33746","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33747","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33748","user_id":"55883","type_name":"注册送100元红包","type_money":1000}]},{"goods_id":"424","shop_price":"16800.00","goods_name":"罗克尼外教英语课程（100节）","goods_thumb":"http://cdn2.kobiko.cn/images/201507/thumb_img/424_thumb_G_1436887115780.jpg","brand_id":"130","cat_list":"英语教育","cat_id":"23","goods_img":"images/201507/goods_img/424_G_1436887115088.jpg","is_on_sale":"1","brand_name":"罗克尼儿童俱乐部","brand_logo":"http://cdn2.kobiko.cn/1436883024918322171.jpg","goods_attr_id":"1798","attr_id":"153","attr_value":"普通班","attr_price":"0","xiaoqu_id":"259","back_money":"1000.00","attr_name":"课程","xiaoqu_name":"罗克尼方庄芳城园中心","xiaoqu_addr":"北京市丰台区方庄芳城园一区日月天地大厦B座906-907","xiaoqu_addr2":"北京市丰台区方庄芳城园一区日月天地大厦B座","discount_type":"3","origin_price":"16800.00","minus":"1000.00","subtotal":"15800.00","bonus_info":"","bonus_info_list":[{"bonus_id":"33744","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33745","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33746","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33747","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33748","user_id":"55883","type_name":"注册送100元红包","type_money":1000}]},{"goods_id":"662","shop_price":"880.00","goods_name":"贝乐一对一英语在线VIP定制课程（绘本课程）","goods_thumb":"http://cdn1.kobiko.cn/images/201607/thumb_img/662_thumb_G_1469070288008.jpg","brand_id":"214","cat_list":"英语教育,少儿教育","cat_id":"32","goods_img":"images/201607/goods_img/662_G_1469070288047.jpg","is_on_sale":"1","brand_name":"贝乐一对一","brand_logo":"http://cdn2.kobiko.cn/1469069172199569237.png","goods_attr_id":"3778","attr_id":"151","attr_value":"40","attr_price":"0","xiaoqu_id":"542","back_money":"50.00","attr_name":"课时","xiaoqu_name":"贝乐一对一惠新东街校区","xiaoqu_addr":"北京朝阳区惠新东街11号紫光发展大厦A座九层","xiaoqu_addr2":"北京朝阳区惠新东街11号紫光发展大厦A座","discount_type":"3","origin_price":"880.00","minus":"50.00","subtotal":"830.00","bonus_info":"","bonus_info_list":[{"bonus_id":"33744","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33745","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33746","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33747","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33748","user_id":"55883","type_name":"注册送100元红包","type_money":1000}]}]
     * order_price : 34490.00
     */

    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String user_id;
        private String is_mobile_phone;
        private String mobile_phone;
        private String order_price;
        private String is_address;

        public String getIs_address() {
            return is_address;
        }

        public void setIs_address(String is_address) {
            this.is_address = is_address;
        }

        /**
         * goods_id : 309
         * shop_price : 18360.00
         * goods_name : 启想艺术课周末班
         * goods_thumb : http://cdn1.kobiko.cn/images/201505/thumb_img/309_thumb_G_1432458877768.jpg
         * brand_id : 100
         * cat_list : 少儿教育,绘画课程
         * cat_id : 10
         * goods_img : images/201505/goods_img/309_G_1432458877214.jpg
         * is_on_sale : 1
         * brand_name : 启想艺术
         * brand_logo : http://cdn2.kobiko.cn/1432457964591740656.jpg
         * goods_attr_id : 1328
         * attr_id : 151
         * attr_value : 120
         * attr_price : 0
         * xiaoqu_id : 175
         * back_money : 500.00
         * attr_name : 课时
         * xiaoqu_name : 启想艺术七圣中街中心
         * xiaoqu_addr : 朝阳区七圣中街12号院爱琴海购物中心三层
         * xiaoqu_addr2 : 朝阳区七圣中街12号院爱琴海购物中心
         * discount_type : 3
         * origin_price : 18360.00
         * minus : 500.00
         * subtotal : 17860.00
         * bonus_info :
         * bonus_info_list : [{"bonus_id":"33744","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33745","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33746","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33747","user_id":"55883","type_name":"注册送100元红包","type_money":1000},{"bonus_id":"33748","user_id":"55883","type_name":"注册送100元红包","type_money":1000}]
         */

        private List<OrderDetailBean> order_detail;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getIs_mobile_phone() {
            return is_mobile_phone;
        }

        public void setIs_mobile_phone(String is_mobile_phone) {
            this.is_mobile_phone = is_mobile_phone;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public String getOrder_price() {
            return order_price;
        }

        public void setOrder_price(String order_price) {
            this.order_price = order_price;
        }

        public List<OrderDetailBean> getOrder_detail() {
            return order_detail;
        }

        public void setOrder_detail(List<OrderDetailBean> order_detail) {
            this.order_detail = order_detail;
        }


        public void optOrderInfoJson(JSONObject jsonObject){
            setUser_id(jsonObject.optString("user_id"));
            setIs_mobile_phone(jsonObject.optString("is_mobile_phone"));
            setMobile_phone(jsonObject.optString("mobile_phone"));
            setIs_address(jsonObject.optString("is_address"));
            JSONArray jsonArray = jsonObject.optJSONArray("order_detail");
            List<OrderDetailBean> list = new ArrayList<>();
            if(jsonArray!=null){
                for(int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    OrderDetailBean orderDetailBean = new OrderDetailBean();
                    list.add(orderDetailBean);
                    orderDetailBean.optOrderDetailObj(jsonObject1);
                }
                setOrder_detail(list);
            }

        }
        public static class OrderDetailBean {
            private String goods_id;
            private String shop_price;
            private String goods_name;
            private String goods_thumb;
            private String brand_id;
            private String cat_list;
            private String cat_id;
            private String goods_img;
            private String is_on_sale;
            private String brand_name;
            private String brand_logo;
            private String goods_attr_id;
            private String attr_id;
            private String attr_value;
            private String attr_price;
            private String xiaoqu_id;
            private String back_money;
            private String attr_name;
            private String xiaoqu_name;
            private String xiaoqu_addr;
            private String xiaoqu_addr2;
            private String discount_type;
            private String origin_price;
            private String minus;
            private String subtotal;
            private BonusInfo bonus_info;
            private String plus_price_buy_total;
            private String plus_price_buy_money;

            public String getPlus_price_buy_total() {
                return plus_price_buy_total;
            }

            public void setPlus_price_buy_total(String plus_price_buy_total) {
                this.plus_price_buy_total = plus_price_buy_total;
            }

            public String getPlus_price_buy_money() {
                return plus_price_buy_money;
            }

            public void setPlus_price_buy_money(String plus_price_buy_money) {
                this.plus_price_buy_money = plus_price_buy_money;
            }

            /**
             * bonus_id : 33744
             * user_id : 55883
             * type_name : 注册送100元红包
             * type_money : 1000
             */
            private String bonusId;
            private String bonusStr;

            private String mimusShow;
            private String subTotalShow;
            private String binusMoney;

            public String getBinusMoney() {
                return binusMoney;
            }

            public void setBinusMoney(String binusMoney) {
                this.binusMoney = binusMoney;
            }

            public String getMimusShow() {
                return mimusShow;
            }

            public void setMimusShow(String mimusShow) {
                this.mimusShow = mimusShow;
            }
            public String getSubTotalShow() {
                return subTotalShow;
            }

            public void setSubTotalShow(String subTotalShow) {
                this.subTotalShow = subTotalShow;
            }

            public String getBonusStr() {
                return bonusStr;
            }

            public void setBonusStr(String bonusStr) {
                this.bonusStr = bonusStr;
            }

            public void setBonusId(String bonus_id){
                bonusId = bonus_id;
            }
            public String getBonusNum(){
                return bonusId;
            }

            private List<BonusInfoListBean> bonus_info_list;

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public String getShop_price() {
                return shop_price;
            }

            public void setShop_price(String shop_price) {
                this.shop_price = shop_price;
            }

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getGoods_thumb() {
                return goods_thumb;
            }

            public void setGoods_thumb(String goods_thumb) {
                this.goods_thumb = goods_thumb;
            }

            public String getBrand_id() {
                return brand_id;
            }

            public void setBrand_id(String brand_id) {
                this.brand_id = brand_id;
            }

            public String getCat_list() {
                return cat_list;
            }

            public void setCat_list(String cat_list) {
                this.cat_list = cat_list;
            }

            public String getCat_id() {
                return cat_id;
            }

            public void setCat_id(String cat_id) {
                this.cat_id = cat_id;
            }

            public String getGoods_img() {
                return goods_img;
            }

            public void setGoods_img(String goods_img) {
                this.goods_img = goods_img;
            }

            public String getIs_on_sale() {
                return is_on_sale;
            }

            public void setIs_on_sale(String is_on_sale) {
                this.is_on_sale = is_on_sale;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getBrand_logo() {
                return brand_logo;
            }

            public void setBrand_logo(String brand_logo) {
                this.brand_logo = brand_logo;
            }

            public String getGoods_attr_id() {
                return goods_attr_id;
            }

            public void setGoods_attr_id(String goods_attr_id) {
                this.goods_attr_id = goods_attr_id;
            }

            public String getAttr_id() {
                return attr_id;
            }

            public void setAttr_id(String attr_id) {
                this.attr_id = attr_id;
            }

            public String getAttr_value() {
                return attr_value;
            }

            public void setAttr_value(String attr_value) {
                this.attr_value = attr_value;
            }

            public String getAttr_price() {
                return attr_price;
            }

            public void setAttr_price(String attr_price) {
                this.attr_price = attr_price;
            }

            public String getXiaoqu_id() {
                return xiaoqu_id;
            }

            public void setXiaoqu_id(String xiaoqu_id) {
                this.xiaoqu_id = xiaoqu_id;
            }

            public String getBack_money() {
                return back_money;
            }

            public void setBack_money(String back_money) {
                this.back_money = back_money;
            }

            public String getAttr_name() {
                return attr_name;
            }

            public void setAttr_name(String attr_name) {
                this.attr_name = attr_name;
            }

            public String getXiaoqu_name() {
                return xiaoqu_name;
            }

            public void setXiaoqu_name(String xiaoqu_name) {
                this.xiaoqu_name = xiaoqu_name;
            }

            public String getXiaoqu_addr() {
                return xiaoqu_addr;
            }

            public void setXiaoqu_addr(String xiaoqu_addr) {
                this.xiaoqu_addr = xiaoqu_addr;
            }

            public String getXiaoqu_addr2() {
                return xiaoqu_addr2;
            }

            public void setXiaoqu_addr2(String xiaoqu_addr2) {
                this.xiaoqu_addr2 = xiaoqu_addr2;
            }

            public String getDiscount_type() {
                return discount_type;
            }

            public void setDiscount_type(String discount_type) {
                this.discount_type = discount_type;
            }

            public String getOrigin_price() {
                return origin_price;
            }

            public void setOrigin_price(String origin_price) {
                this.origin_price = origin_price;
            }

            public String getMinus() {
                return minus;
            }

            public void setMinus(String minus) {
                this.minus = minus;
            }

            public String getSubtotal() {
                return subtotal;
            }

            public void setSubtotal(String subtotal) {
                this.subtotal = subtotal;
            }
            public String getBonusId() {
                return bonusId;
            }


            public BonusInfo getBonus_info() {
                return bonus_info;
            }

            public void setBonus_info(BonusInfo bonus_info) {
                this.bonus_info = bonus_info;
            }


            public static class BonusInfo{
                private String bonus_id;
                private String user_id;
                private String type_name;
                private String type_money;

                public void optBonusInfoData(JSONObject jsonObject){
                    setBonus_id(jsonObject.optString("bonus_id"));
                    setUser_id(jsonObject.optString("user_id"));
                    setType_name(jsonObject.optString("type_name"));
                    setType_money(jsonObject.optString("type_money"));
                }
                public String getBonus_id() {
                    return bonus_id;
                }

                public void setBonus_id(String bonus_id) {
                    this.bonus_id = bonus_id;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getType_name() {
                    return type_name;
                }

                public void setType_name(String type_name) {
                    this.type_name = type_name;
                }

                public String getType_money() {
                    return type_money;
                }

                public void setType_money(String type_money) {
                    this.type_money = type_money;
                }
            }

            public List<BonusInfoListBean> getBonus_info_list() {
                return bonus_info_list;
            }

            public void setBonus_info_list(List<BonusInfoListBean> bonus_info_list) {
                this.bonus_info_list = bonus_info_list;
            }

            public void optOrderDetailObj(JSONObject jsonObject){
                setGoods_id(jsonObject.optString("goods_id"));
                setShop_price(jsonObject.optString("shop_price"));
                setGoods_name(jsonObject.optString("goods_name"));
                setGoods_thumb(jsonObject.optString("goods_thumb"));
                setBrand_id(jsonObject.optString("brand_id"));
                setCat_list(jsonObject.optString("cat_list"));
                setCat_id(jsonObject.optString("cat_id"));
                setGoods_img(jsonObject.optString("goods_img"));
                setIs_on_sale(jsonObject.optString("is_on_sale"));
                setBrand_name(jsonObject.optString("brand_name"));
                setBrand_logo(jsonObject.optString("brand_logo"));
                setGoods_attr_id(jsonObject.optString("goods_attr_id"));
                setAttr_id(jsonObject.optString("attr_id"));
                setAttr_value(jsonObject.optString("attr_value"));
                setAttr_price(jsonObject.optString("attr_price"));
                setXiaoqu_id(jsonObject.optString("xiaoqu_id"));
                setBack_money(jsonObject.optString("back_money"));
                setAttr_name(jsonObject.optString("attr_name"));
                setXiaoqu_name(jsonObject.optString("xiaoqu_name"));
                setXiaoqu_addr(jsonObject.optString("xiaoqu_addr"));
                setXiaoqu_addr2(jsonObject.optString("xiaoqu_addr2"));
                setDiscount_type(jsonObject.optString("discount_type"));
                setOrigin_price(jsonObject.optString("origin_price"));
                setMinus(jsonObject.optString("minus"));

                setPlus_price_buy_total(jsonObject.optString("plus_price_buy_total"));
                setPlus_price_buy_money(jsonObject.optString("plus_price_buy_money"));

                setSubtotal(jsonObject.optString("subtotal"));
                if(jsonObject.toString().contains("bonus_info: \"\"")){
                    //不解析这个字段，或者如果是实体对的话设置为“”
                }else{
                    JSONObject jsonObject1 = jsonObject.optJSONObject("bonus_info");
                    BonusInfo bonusInfo = new BonusInfo();
                    setBonus_info(bonusInfo);
                    if(jsonObject1!=null){
                        bonusInfo.optBonusInfoData(jsonObject1);
                    }
                }
                JSONArray jsonArray = jsonObject.optJSONArray("bonus_info_list");
                List<BonusInfoListBean> list = new ArrayList<>();
                if(jsonArray!=null){
                    for(int i =0;i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                      BonusInfoListBean bonusInfoListBean =  new BonusInfoListBean();
                        bonusInfoListBean.optBonusInfoListObj(jsonObject1);
                        list.add(bonusInfoListBean);
                    }
                    setBonus_info_list(list);
                }

            }

            public static class BonusInfoListBean {
                private String bonus_id;
                private String user_id;
                private String type_name;
                private String type_money;

                public String getBonus_id() {
                    return bonus_id;
                }

                public void setBonus_id(String bonus_id) {
                    this.bonus_id = bonus_id;
                }

                public String getUser_id() {
                    return user_id;
                }

                public void setUser_id(String user_id) {
                    this.user_id = user_id;
                }

                public String getType_name() {
                    return type_name;
                }

                public void setType_name(String type_name) {
                    this.type_name = type_name;
                }

                public String getType_money() {
                    return type_money;
                }

                public void setType_money(String type_money) {
                    this.type_money = type_money;
                }

                public void optBonusInfoListObj(JSONObject jsonObject){
                    setUser_id(jsonObject.optString("user_id"));
                    setBonus_id(jsonObject.optString("bonus_id"));
                    setType_name(jsonObject.optString("type_name"));
                    setType_money(jsonObject.optString("type_money"));
                }
            }
        }
    }

}
