package com.dev.nutclass.parser;

import com.dev.nutclass.activity.MerchantInfoActivity;
import com.dev.nutclass.constants.Const;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.MerchantCardEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/11.
 */
public class MerchantListParser extends BaseParser<BaseCardEntity> {
    private int from;
    public MerchantListParser(int from) {
        this.from =from;
    }

    @Override
    public Object parse(String jsonString)  {
        JsonDataList<BaseCardEntity> retObj = new JsonDataList<>();
        ArrayList<BaseCardEntity> list = new ArrayList<>();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
            JSONArray merchantArray = jsonObject.optJSONArray("data");
            if(merchantArray != null && merchantArray.length()>0){
                for(int i=0;i<merchantArray.length();i++){
                    JSONObject itemObj = merchantArray.optJSONObject(i);
                    if(itemObj != null){
                        if(from == Const.TYPE_FROM_MERCHANT_ORDER || from ==Const.TYPE_FROM_MERCHANT_ONE_YUAN){
                        MerchantCardEntity entity = new MerchantCardEntity(1);
                            entity.setFrom(from);
                        entity.optJsonObjOrder(itemObj);
                        if(entity!=null){
                            list.add(entity);
                             }
                        }else if(from == Const.TYPE_FROM_MERCHANT_APPOINTMENT){
                            MerchantCardEntity entity = new MerchantCardEntity(2);
                            entity.setFrom(from);
                            entity.optJsonObjAppointment(itemObj);
                            if(entity!=null){
                                list.add(entity);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        retObj.setErrorCode(UrlConst.SUCCESS_CODE);
        retObj.setList(list);
        return retObj;
    }
}
