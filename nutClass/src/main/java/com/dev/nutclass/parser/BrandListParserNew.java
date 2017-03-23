package com.dev.nutclass.parser;

import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.BrandCardEntity;
import com.dev.nutclass.entity.JsonDataList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */
public class BrandListParserNew extends BaseParser<BaseCardEntity> {
    private int status;
    private String info;

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

    private  List<BrandCardEntity> list2;
    public BrandListParserNew() {
    }

    @Override
    public Object parse(String jsonString){
        List<List<BrandCardEntity>> brandList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            setInfo(jsonObject.optString("status"));
            setStatus(jsonObject.optInt("status"));
            JSONArray brandArray = jsonObject.optJSONArray("data");
            if(brandArray != null&brandArray.length()>0){
                for(int i = 0;i<brandArray.length();i++){
                    JSONArray brandArray2 = brandArray.getJSONArray(i);
                    if(brandArray2 != null && brandArray2.length()>0){
                        list2 = new ArrayList<BrandCardEntity>();
                        for(int j = 0;j<brandArray2.length();j++){
                            JSONObject itemObj = brandArray2.optJSONObject(j);
                            if(itemObj !=null){
                                BrandCardEntity entity = new BrandCardEntity();
                                entity.optJsonObj(itemObj);
                                list2.add(entity);
                            }
                        }
                    }
                    brandList.add(list2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return brandList;
    }
}
