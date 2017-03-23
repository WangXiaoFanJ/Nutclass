package com.dev.nutclass.parser;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.AgeCardEntity;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CooponCardEntity;
import com.dev.nutclass.entity.CooponNumEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.HeadCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.JDCatCardEntity;
import com.dev.nutclass.entity.JDItemCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.SchoolCardEntity;

public class JDCatListParser extends BaseParser<BaseCardEntity> {
	private JDItemCardEntity entity2;
	public int from=0;
	public JDCatListParser(){
	}
	public JDCatListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		Log.d("===","from"+from);
		try {
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONObject jsonObject = new JSONObject(jsonString);
			
			
			JSONObject dataObj=jsonObject.optJSONObject("data");
			
			if(dataObj!=null){
				JSONArray banner = dataObj.optJSONArray("jd_kepler_banner");
				BannerCardEntity entity=new BannerCardEntity();
				entity.setCardType(BaseCardEntity.CARD_TYPE_JD_BANNER);
				entity.optJsonObjJD(banner);
				list.add(entity);
				
				JSONArray cat = dataObj.optJSONArray("jd_cat_list");
				JDCatCardEntity entity1=new JDCatCardEntity(cat);
				list.add(entity1);
				
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
						entity2=new JDItemCardEntity(leftObj, rightObj);
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
