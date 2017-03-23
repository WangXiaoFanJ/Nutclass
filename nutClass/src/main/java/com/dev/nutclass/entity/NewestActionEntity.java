package com.dev.nutclass.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class NewestActionEntity {

    /**
     * status : 1
     * info :
     * data : [{"id":"2","name":"课比课大活动2","banner_img":"http://cdn1.kobiko.cn/./Uploads/2016-09-29/57ec8e28cead2.png","banner_img1":"http://cdn2.kobiko.cn/./Uploads/2016-09-29/57ec8e28cf047.png","footer_img":"./Uploads/2016-09-29/57ec8e28cf59a.png","bonus":"3","bonus_height":null,"bonus_all_height":null,"start_p_time":"1474819200","end_p_time":"1475596800","explain":"课比课第二次大活动","type":[{"id":"3","pid":"2","type_img":"http://cdn2.kobiko.cn/./Uploads/2016-09-29/57ec8e28d0224.png","is_show":"1","goods_id":"378","goods":[{"goods_id":"378","cat_id":"10","cat_list":"早教课程,大脑开发,思维训练","goods_sn":"edu000887","goods_name":"金宝贝课程套餐","click_count":"2568","brand_id":"20","goods_number":"999","market_price":"43700.00","shop_price":"43700.00","keywords":"金宝贝 早教 育乐 亲子 艺术 技能","goods_thumb":"http://cdn1.kobiko.cn/images/201506/thumb_img/378_thumb_G_1435674369986.jpg","goods_img":"http://cdn2.kobiko.cn/images/201506/goods_img/378_G_1435674369838.jpg","tag1":"1","tag2":"1","tag3":"0","tag4":"1","tag5":"1"}]},{}]}]
     */

    private int status;
    private String info;
    /**
     * id : 2
     * name : 课比课大活动2
     * banner_img : http://cdn1.kobiko.cn/./Uploads/2016-09-29/57ec8e28cead2.png
     * banner_img1 : http://cdn2.kobiko.cn/./Uploads/2016-09-29/57ec8e28cf047.png
     * footer_img : ./Uploads/2016-09-29/57ec8e28cf59a.png
     * bonus : 3
     * bonus_height : null
     * bonus_all_height : null
     * start_p_time : 1474819200
     * end_p_time : 1475596800
     * explain : 课比课第二次大活动
     * type : [{"id":"3","pid":"2","type_img":"http://cdn2.kobiko.cn/./Uploads/2016-09-29/57ec8e28d0224.png","is_show":"1","goods_id":"378","goods":[{"goods_id":"378","cat_id":"10","cat_list":"早教课程,大脑开发,思维训练","goods_sn":"edu000887","goods_name":"金宝贝课程套餐","click_count":"2568","brand_id":"20","goods_number":"999","market_price":"43700.00","shop_price":"43700.00","keywords":"金宝贝 早教 育乐 亲子 艺术 技能","goods_thumb":"http://cdn1.kobiko.cn/images/201506/thumb_img/378_thumb_G_1435674369986.jpg","goods_img":"http://cdn2.kobiko.cn/images/201506/goods_img/378_G_1435674369838.jpg","tag1":"1","tag2":"1","tag3":"0","tag4":"1","tag5":"1"}]},{}]
     */

    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String name;
        private String banner_img;
        private String banner_img_url;
        private String banner_img_ratio;
        private String banner_img1;
        private String banner_img1_url;
        private String banner_img1_ratio;
        private String footer_img;
        private String footer_img_url;
        private int banner_img_url_login;
        private int banner_img1_url_login;
        private String footer_img_ratio;
        private String bonus;
        private Object bonus_height;
        private Object bonus_all_height;
        private String start_p_time;
        private String end_p_time;
        private String explain;
        private String background_color;
        private String background_img;
        private String select;

        public String getSelect() {
            return select;
        }

        public void setSelect(String select) {
            this.select = select;
        }

        public int getBanner_img_url_login() {
            return banner_img_url_login;
        }

        public void setBanner_img_url_login(int banner_img_url_login) {
            this.banner_img_url_login = banner_img_url_login;
        }

        /**
         * id : 3
         * pid : 2
         * type_img : http://cdn2.kobiko.cn/./Uploads/2016-09-29/57ec8e28d0224.png
         * is_show : 1
         * goods_id : 378
         * goods : [{"goods_id":"378","cat_id":"10","cat_list":"早教课程,大脑开发,思维训练","goods_sn":"edu000887","goods_name":"金宝贝课程套餐","click_count":"2568","brand_id":"20","goods_number":"999","market_price":"43700.00","shop_price":"43700.00","keywords":"金宝贝 早教 育乐 亲子 艺术 技能","goods_thumb":"http://cdn1.kobiko.cn/images/201506/thumb_img/378_thumb_G_1435674369986.jpg","goods_img":"http://cdn2.kobiko.cn/images/201506/goods_img/378_G_1435674369838.jpg","tag1":"1","tag2":"1","tag3":"0","tag4":"1","tag5":"1"}]
         */

        private ShareInfoEntity share_info;

        public ShareInfoEntity getShare_info() {
            return share_info;
        }

        public void setShare_info(ShareInfoEntity share_info) {
            this.share_info = share_info;
        }

        public class ShareInfoEntity{
            private String title;
            private String context;
            private String image;
            private String url;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContext() {
                return context;
            }

            public void setContext(String context) {
                this.context = context;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
        private List<TypeBean> type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBanner_img() {
            return banner_img;
        }

        public String getBanner_img_url() {
            return banner_img_url;
        }

        public void setBanner_img_url(String banner_img_url) {
            this.banner_img_url = banner_img_url;
        }

        public String getBanner_img1_url() {
            return banner_img1_url;
        }

        public String getFooter_img_ratio() {
            return footer_img_ratio;
        }

        public void setFooter_img_ratio(String footer_img_ratio) {
            this.footer_img_ratio = footer_img_ratio;
        }

        public String getBanner_img1_ratio() {
            return banner_img1_ratio;
        }

        public void setBanner_img1_ratio(String banner_img1_ratio) {
            this.banner_img1_ratio = banner_img1_ratio;
        }

        public String getBanner_img_ratio() {
            return banner_img_ratio;
        }

        public void setBanner_img_ratio(String banner_img_ratio) {
            this.banner_img_ratio = banner_img_ratio;
        }

        public void setBanner_img1_url(String banner_img1_url) {
            this.banner_img1_url = banner_img1_url;
        }

        public String getFooter_img_url() {
            return footer_img_url;
        }

        public void setFooter_img_url(String footer_img_url) {
            this.footer_img_url = footer_img_url;
        }

        public int getBanner_img1_url_login() {
            return banner_img1_url_login;
        }

        public void setBanner_img1_url_login(int banner_img1_url_login) {
            this.banner_img1_url_login = banner_img1_url_login;
        }

        public void setBanner_img(String banner_img) {
            this.banner_img = banner_img;
        }

        public String getBanner_img1() {
            return banner_img1;
        }

        public void setBanner_img1(String banner_img1) {
            this.banner_img1 = banner_img1;
        }

        public String getFooter_img() {
            return footer_img;
        }

        public void setFooter_img(String footer_img) {
            this.footer_img = footer_img;
        }

        public String getBonus() {
            return bonus;
        }

        public void setBonus(String bonus) {
            this.bonus = bonus;
        }

        public Object getBonus_height() {
            return bonus_height;
        }

        public void setBonus_height(Object bonus_height) {
            this.bonus_height = bonus_height;
        }

        public Object getBonus_all_height() {
            return bonus_all_height;
        }

        public void setBonus_all_height(Object bonus_all_height) {
            this.bonus_all_height = bonus_all_height;
        }

        public String getStart_p_time() {
            return start_p_time;
        }

        public void setStart_p_time(String start_p_time) {
            this.start_p_time = start_p_time;
        }

        public String getEnd_p_time() {
            return end_p_time;
        }

        public void setEnd_p_time(String end_p_time) {
            this.end_p_time = end_p_time;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getBackground_color() {
            return background_color;
        }

        public void setBackground_color(String background_color) {
            this.background_color = background_color;
        }

        public String getBackground_img() {
            return background_img;
        }

        public void setBackground_img(String background_img) {
            this.background_img = background_img;
        }

        public List<TypeBean> getType() {
            return type;
        }

        public void setType(List<TypeBean> type) {
            this.type = type;
        }

        public static class TypeBean {
            private String id;
            private String pid;
            private String type_img;
            private String type_name;
            private String is_show;
            private String goods_id;


            /**
             * goods_id : 378
             * cat_id : 10
             * cat_list : 早教课程,大脑开发,思维训练
             * goods_sn : edu000887
             * goods_name : 金宝贝课程套餐
             * click_count : 2568
             * brand_id : 20
             * goods_number : 999
             * market_price : 43700.00
             * shop_price : 43700.00
             * keywords : 金宝贝 早教 育乐 亲子 艺术 技能
             * goods_thumb : http://cdn1.kobiko.cn/images/201506/thumb_img/378_thumb_G_1435674369986.jpg
             * goods_img : http://cdn2.kobiko.cn/images/201506/goods_img/378_G_1435674369838.jpg
             * tag1 : 1
             * tag2 : 1
             * tag3 : 0
             * tag4 : 1
             * tag5 : 1
             */

            private List<GoodsBean> goods;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getType_img() {
                return type_img;
            }

            public void setType_img(String type_img) {
                this.type_img = type_img;
            }

            public String getType_name() {
                return type_name;
            }

            public void setType_name(String type_name) {
                this.type_name = type_name;
            }

            public String getIs_show() {
                return is_show;
            }

            public void setIs_show(String is_show) {
                this.is_show = is_show;
            }

            public String getGoods_id() {
                return goods_id;
            }

            public void setGoods_id(String goods_id) {
                this.goods_id = goods_id;
            }

            public List<GoodsBean> getGoods() {
                return goods;
            }

            public void setGoods(List<GoodsBean> goods) {
                this.goods = goods;
            }

            public static class GoodsBean {
                private String goods_id;
                private String cat_id;
                private String cat_list;
                private String goods_sn;
                private String goods_name;
                private String click_count;
                private String brand_id;
                private String goods_number;
                private String market_price;
                private String shop_price;
                private String back_money;
                private String back_money_str;
                private String icon_type;
                private String keywords;
                private String goods_thumb;
                private String goods_img;
                private String icon_img;
                private String icon_str;
                private String sold_count;
                private String tag1;
                private String tag2;
                private String tag3;
                private String tag4;
                private String tag5;

                public String getGoods_id() {
                    return goods_id;
                }

                public void setGoods_id(String goods_id) {
                    this.goods_id = goods_id;
                }

                public String getCat_id() {
                    return cat_id;
                }

                public void setCat_id(String cat_id) {
                    this.cat_id = cat_id;
                }

                public String getCat_list() {
                    return cat_list;
                }

                public void setCat_list(String cat_list) {
                    this.cat_list = cat_list;
                }

                public String getGoods_sn() {
                    return goods_sn;
                }

                public void setGoods_sn(String goods_sn) {
                    this.goods_sn = goods_sn;
                }

                public String getGoods_name() {
                    return goods_name;
                }

                public void setGoods_name(String goods_name) {
                    this.goods_name = goods_name;
                }

                public String getClick_count() {
                    return click_count;
                }

                public void setClick_count(String click_count) {
                    this.click_count = click_count;
                }

                public String getBrand_id() {
                    return brand_id;
                }

                public void setBrand_id(String brand_id) {
                    this.brand_id = brand_id;
                }

                public String getGoods_number() {
                    return goods_number;
                }

                public void setGoods_number(String goods_number) {
                    this.goods_number = goods_number;
                }

                public String getMarket_price() {
                    return market_price;
                }

                public void setMarket_price(String market_price) {
                    this.market_price = market_price;
                }

                public String getShop_price() {
                    return shop_price;
                }

                public void setShop_price(String shop_price) {
                    this.shop_price = shop_price;
                }

                public String getBack_money() {
                    return back_money;
                }

                public void setBack_money(String back_money) {
                    this.back_money = back_money;
                }

                public String getBack_money_str() {
                    return back_money_str;
                }

                public void setBack_money_str(String back_money_str) {
                    this.back_money_str = back_money_str;
                }

                public String getIcon_type() {
                    return icon_type;
                }

                public void setIcon_type(String icon_type) {
                    this.icon_type = icon_type;
                }

                public String getIcon_img() {
                    return icon_img;
                }

                public void setIcon_img(String icon_img) {
                    this.icon_img = icon_img;
                }

                public String getKeywords() {
                    return keywords;
                }

                public void setKeywords(String keywords) {
                    this.keywords = keywords;
                }

                public String getGoods_thumb() {
                    return goods_thumb;
                }

                public void setGoods_thumb(String goods_thumb) {
                    this.goods_thumb = goods_thumb;
                }

                public String getGoods_img() {
                    return goods_img;
                }

                public void setGoods_img(String goods_img) {
                    this.goods_img = goods_img;
                }

                public String getIcon_str() {
                    return icon_str;
                }

                public void setIcon_str(String icon_str) {
                    this.icon_str = icon_str;
                }

                public String getSold_count() {
                    return sold_count;
                }

                public void setSold_count(String sold_count) {
                    this.sold_count = sold_count;
                }

                public String getTag1() {
                    return tag1;
                }

                public void setTag1(String tag1) {
                    this.tag1 = tag1;
                }

                public String getTag2() {
                    return tag2;
                }

                public void setTag2(String tag2) {
                    this.tag2 = tag2;
                }

                public String getTag3() {
                    return tag3;
                }

                public void setTag3(String tag3) {
                    this.tag3 = tag3;
                }

                public String getTag4() {
                    return tag4;
                }

                public void setTag4(String tag4) {
                    this.tag4 = tag4;
                }

                public String getTag5() {
                    return tag5;
                }

                public void setTag5(String tag5) {
                    this.tag5 = tag5;
                }
            }
        }
    }
}
