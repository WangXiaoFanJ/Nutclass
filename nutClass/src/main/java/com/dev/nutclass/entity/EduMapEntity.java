package com.dev.nutclass.entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class EduMapEntity {
    private int status;
    private String info;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int map_lv;

        private List<SchoolListBean> school_list;

        public int getMap_lv() {
            return map_lv;
        }

        public void setMap_lv(int map_lv) {
            this.map_lv = map_lv;
        }

        public List<SchoolListBean> getSchool_list() {
            return school_list;
        }

        public void setSchool_list(List<SchoolListBean> school_list) {
            this.school_list = school_list;
        }
        public  void optEduMapJson(JSONObject jsonObject){
            setMap_lv(jsonObject.optInt("map_lv"));
            JSONArray jsonArray =jsonObject.optJSONArray("school_list");
            List<SchoolListBean> list = new ArrayList<>();
            if(jsonArray!=null){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);
                    SchoolListBean schoolListBean = new SchoolListBean();
                    list.add(schoolListBean);
                    schoolListBean.optSchoolistJson(jsonObject1);
                }
                setSchool_list(list);
            }
        }
        public static class SchoolListBean {
            private String id;
            private String name;
            private String address;
            private String icon;
            private String back_money;
            private String distance;
            private String latitude;
            private String longitude;
            private String cat_name;
            private String tel;
            private int in_status;

            public int getIn_status() {
                return in_status;
            }

            public void setIn_status(int in_status) {
                this.in_status = in_status;
            }

            public String getTel() {
                return tel;
            }

            public void setTel(String tel) {
                this.tel = tel;
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

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getBack_money() {
                return back_money;
            }

            public void setBack_money(String back_money) {
                this.back_money = back_money;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getCat_name() {
                return cat_name;
            }

            public void setCat_name(String cat_name) {
                this.cat_name = cat_name;
            }

            public void optSchoolistJson(JSONObject jsonObject1) {
                setId(jsonObject1.optString("id"));
                setName(jsonObject1.optString("name"));
                setAddress(jsonObject1.optString("address"));
                setIcon(jsonObject1.optString("icon"));
                setBack_money(jsonObject1.optString("back_money"));
                setDistance(jsonObject1.optString("distance"));
                setLatitude(jsonObject1.optString("latitude"));
                setLongitude(jsonObject1.optString("longitude"));
                setCat_name(jsonObject1.optString("cat_name"));
                setTel(jsonObject1.optString("tel"));
                setIn_status(jsonObject1.optInt("in_status"));
            }
        }
    }
}
