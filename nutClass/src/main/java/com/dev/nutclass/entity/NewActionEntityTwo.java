package com.dev.nutclass.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */
public class NewActionEntityTwo {
    private int status;
    private String info;
    private List<DataBean> dataBeanList;
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

    public List<DataBean> getDataBeanList() {
        return dataBeanList;
    }

    public void setDataBeanList(List<DataBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
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

        public void optDataBean(JSONObject jsonObject){
            setId(jsonObject.optString("id"));
            setName(jsonObject.optString("name"));
            setBanner_img(jsonObject.optString("banner_img"));
            setBackground_color(jsonObject.optString("background_color"));
            setBackground_img(jsonObject.optString("background_img"));
            JSONArray jsonArray = jsonObject.optJSONArray("type");
            List<TypeBean> list = new ArrayList<>();
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                TypeBean typeBean = new TypeBean();
                typeBean.optTypeJson(jsonObject1);
                list.add(typeBean);
            }
            setType(list);
        }
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

        public void setBanner_img(String banner_img) {
            this.banner_img = banner_img;
        }

        public String getBanner_img_url() {
            return banner_img_url;
        }

        public void setBanner_img_url(String banner_img_url) {
            this.banner_img_url = banner_img_url;
        }

        public String getBanner_img_ratio() {
            return banner_img_ratio;
        }

        public void setBanner_img_ratio(String banner_img_ratio) {
            this.banner_img_ratio = banner_img_ratio;
        }

        public String getBanner_img1() {
            return banner_img1;
        }

        public void setBanner_img1(String banner_img1) {
            this.banner_img1 = banner_img1;
        }

        public String getBanner_img1_url() {
            return banner_img1_url;
        }

        public void setBanner_img1_url(String banner_img1_url) {
            this.banner_img1_url = banner_img1_url;
        }

        public String getBanner_img1_ratio() {
            return banner_img1_ratio;
        }

        public void setBanner_img1_ratio(String banner_img1_ratio) {
            this.banner_img1_ratio = banner_img1_ratio;
        }

        public String getFooter_img() {
            return footer_img;
        }

        public void setFooter_img(String footer_img) {
            this.footer_img = footer_img;
        }

        public String getFooter_img_url() {
            return footer_img_url;
        }

        public void setFooter_img_url(String footer_img_url) {
            this.footer_img_url = footer_img_url;
        }

        public int getBanner_img_url_login() {
            return banner_img_url_login;
        }

        public void setBanner_img_url_login(int banner_img_url_login) {
            this.banner_img_url_login = banner_img_url_login;
        }

        public int getBanner_img1_url_login() {
            return banner_img1_url_login;
        }

        public void setBanner_img1_url_login(int banner_img1_url_login) {
            this.banner_img1_url_login = banner_img1_url_login;
        }

        public String getFooter_img_ratio() {
            return footer_img_ratio;
        }

        public void setFooter_img_ratio(String footer_img_ratio) {
            this.footer_img_ratio = footer_img_ratio;
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

        public String getSelect() {
            return select;
        }

        public void setSelect(String select) {
            this.select = select;
        }

        private ShareInfoEntity share_info;

        public ShareInfoEntity getShare_info() {
            return share_info;
        }

        public void setShare_info(ShareInfoEntity share_info) {
            this.share_info = share_info;
        }

        public class ShareInfoEntity {
            private String title;
            private String context;
            private String image;
            private String url;
        }
        private List<TypeBean> type;

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


            public void optTypeJson(JSONObject jsonObject){
                setId(jsonObject.optString("id"));
                List<GoodsBean> list = new ArrayList<>();
                JSONArray jsonArray = jsonObject.optJSONArray("goods");
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject leftObj=jsonArray.optJSONObject(i++);
                    JSONObject rightObj=null;
                    if(i<jsonArray.length()){
                        rightObj=jsonArray.optJSONObject(i);
                    }else{
                        rightObj=null;
                    }
                    GoodsBean goodsBean = new GoodsBean();
                    goodsBean.optGoodsJson(leftObj,rightObj);
                    list.add(goodsBean);
                }
                setGoodsBeanList(list);
            }
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
            private List<GoodsBean> goodsBeanList;

            public List<GoodsBean> getGoodsBeanList() {
                return goodsBeanList;
            }

            public void setGoodsBeanList(List<GoodsBean> goodsBeanList) {
                this.goodsBeanList = goodsBeanList;
            }

            public static class GoodsBean {
                private String left_goods_thumb;
                private String right_goods_thumg;
                private String left_goods_img;
                private String right_goods_img;
                private String left_icon_str;
                private String right_icon_str;
                private String left_goods_name;
                private String right_goods_name;
                private String left_icon_img;
                private String right_icon_img;
                private String left_shop_price;
                private String right_shop_price;
                private String left_goods_id;
                private String right_goods_id;

                public String getRight_goods_id() {
                    return right_goods_id;
                }

                public void setRight_goods_id(String right_goods_id) {
                    this.right_goods_id = right_goods_id;
                }

                public String getLeft_goods_id() {
                    return left_goods_id;
                }

                public void setLeft_goods_id(String left_goods_id) {
                    this.left_goods_id = left_goods_id;
                }

                public String getLeft_goods_img() {
                    return left_goods_img;
                }

                public void setLeft_goods_img(String left_goods_img) {
                    this.left_goods_img = left_goods_img;
                }

                public String getRight_goods_img() {
                    return right_goods_img;
                }

                public void setRight_goods_img(String right_goods_img) {
                    this.right_goods_img = right_goods_img;
                }

                public String getLeft_goods_thumb() {
                    return left_goods_thumb;
                }

                public void setLeft_goods_thumb(String left_goods_thumb) {
                    this.left_goods_thumb = left_goods_thumb;
                }

                public String getRight_goods_thumg() {
                    return right_goods_thumg;
                }

                public void setRight_goods_thumg(String right_goods_thumg) {
                    this.right_goods_thumg = right_goods_thumg;
                }

                public String getLeft_icon_str() {
                    return left_icon_str;
                }

                public void setLeft_icon_str(String left_icon_str) {
                    this.left_icon_str = left_icon_str;
                }

                public String getRight_icon_str() {
                    return right_icon_str;
                }

                public void setRight_icon_str(String right_icon_str) {
                    this.right_icon_str = right_icon_str;
                }

                public String getLeft_goods_name() {
                    return left_goods_name;
                }

                public void setLeft_goods_name(String left_goods_name) {
                    this.left_goods_name = left_goods_name;
                }

                public String getRight_goods_name() {
                    return right_goods_name;
                }

                public void setRight_goods_name(String right_goods_name) {
                    this.right_goods_name = right_goods_name;
                }

                public String getLeft_icon_img() {
                    return left_icon_img;
                }

                public void setLeft_icon_img(String left_icon_img) {
                    this.left_icon_img = left_icon_img;
                }

                public String getRight_icon_img() {
                    return right_icon_img;
                }

                public void setRight_icon_img(String right_icon_img) {
                    this.right_icon_img = right_icon_img;
                }

                public String getLeft_shop_price() {
                    return left_shop_price;
                }

                public void setLeft_shop_price(String left_shop_price) {
                    this.left_shop_price = left_shop_price;
                }

                public String getRight_shop_price() {
                    return right_shop_price;
                }

                public void setRight_shop_price(String right_shop_price) {
                    this.right_shop_price = right_shop_price;
                }

                public void optGoodsJson(JSONObject leftJson,JSONObject rightJson){
                    if(leftJson!=null){
                        setLeft_goods_name(leftJson.optString("goods_name"));
                        setLeft_goods_thumb(leftJson.optString("goods_thumb"));
                        setLeft_icon_img(leftJson.optString("icon_img"));
                        setLeft_icon_str(leftJson.optString("icon_str"));
                        setLeft_shop_price(leftJson.optString("shop_price"));
                        setLeft_goods_img(leftJson.optString("goods_img"));
                        setLeft_goods_id(leftJson.optString("goods_id"));
                    }
                    if(rightJson!=null){
                        setRight_goods_name(rightJson.optString("goods_name"));
                        setRight_goods_thumg(rightJson.optString("goods_thumb"));
                        setRight_icon_img(rightJson.optString("icon_img"));
                        setRight_icon_str(rightJson.optString("icon_str"));
                        setRight_shop_price(rightJson.optString("shop_price"));
                        setRight_goods_img(rightJson.optString("goods_img"));
                        setRight_goods_id(rightJson.optString("goods_id"));
                    }
                }

            }
        }
        }
}
