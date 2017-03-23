package com.dev.nutclass.parser;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CommentCardEntity;
import com.dev.nutclass.entity.JsonDataList;


public class CommentParser extends BaseParser<BaseCardEntity> {

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
			retObj.optPageBaseJson(jsonObject);
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONArray commonObj = jsonObject.optJSONArray("data");
			if(commonObj!=null&&commonObj.length()>0){
				for(int i=0;i<commonObj.length();i++){
					CommentCardEntity comment = new CommentCardEntity(
							commonObj.optJSONObject(i));
					if (comment != null) {
						list.add(comment);
					}
				}
			}
			retObj.setList(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return retObj;
	}

}
