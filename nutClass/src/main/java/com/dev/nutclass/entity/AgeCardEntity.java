package com.dev.nutclass.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AgeCardEntity extends BaseCardEntity {

	// 1 已使用 2 未使用 3 已过期
	private List<SimpleTextEntity> ageList=new ArrayList<SimpleTextEntity>();



	public List<SimpleTextEntity> getAgeList() {
		return ageList;
	}



	public void setAgeList(List<SimpleTextEntity> ageList) {
		this.ageList = ageList;
	}



	public AgeCardEntity(JSONArray jsonObj){
		setCardType(CARD_TYPE_AGE);
		optJsonObj(jsonObj);
	}

	

	/*
	 * my_bonus_count: 5,
	use_bonus: 0,
	not_use_bonus: 5,
	time_old_bonus: 0
	 */
	// 课程列表页Course数据解析
	public void optJsonObj(JSONArray jsonObj) {
		if (jsonObj == null)
			return;
		for(int i=0;i<jsonObj.length();i++){
			JSONObject obj;
			try {
				obj = jsonObj.getJSONObject(i);
				if(obj!=null){
					SimpleTextEntity entity=new SimpleTextEntity();
					entity.optAgeInfo(obj);
					ageList.add(entity);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		 
	}

}
