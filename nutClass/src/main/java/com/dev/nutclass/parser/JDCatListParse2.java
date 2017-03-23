package com.dev.nutclass.parser;

import android.util.Log;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JDCatCardEntity;
import com.dev.nutclass.entity.JDItemCardEntity;
import com.dev.nutclass.entity.JsonDataList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/23.
 */
public class JDCatListParse2 extends BaseParser<BaseCardEntity> {
    @Override
    public Object parse(String jsonString) throws JSONException {
        {
            JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
            try {
                ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
                JSONObject jsonObject = new JSONObject(jsonString);


                JSONObject dataObj=jsonObject.optJSONObject("data");

                if(dataObj!=null){
                    JSONArray banner = dataObj.optJSONArray("jd_kepler_banner");
                    BannerCardEntity entity=new BannerCardEntity();
                    entity.setCardType(BaseCardEntity.CARD_TYPE_JD_BANNER);
                    entity.optJsonObjJD(banner);
//                    list.add(entity);

                    JSONArray cat = dataObj.optJSONArray("jd_cat_list");
                    JDCatCardEntity entity1=new JDCatCardEntity(cat);
//                    list.add(entity1);

                    JSONArray item = dataObj.optJSONArray("jd_kepler_list");

                    if(item!=null&&item.length()>0){
                        for (int i = 0; i < item.length(); i++) {
                            JSONObject leftObj=item.optJSONObject(i++);
                            JSONObject rightObj=null;
                            if(i<item.length()){
                                rightObj=item.optJSONObject(i);
                            }else{
                                rightObj=null;
                            }
                            JDItemCardEntity    entity2=new JDItemCardEntity(leftObj, rightObj);
                            list.add(entity2);

                        }
                    }

                }


                retObj.setErrorCode(UrlConst.SUCCESS_CODE);
                retObj.setList(list);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return retObj;
        }
    }
}
