package com.dev.nutclass.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.entity.ActionCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.SimpleTextEntity;

public class SimpleTextListParser extends BaseParser<BaseCardEntity> {
	
	public String type="";
	public SimpleTextListParser(){}
	public SimpleTextListParser(String type){
		this.type=type;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);

			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONArray categoryArray = jsonObject.optJSONArray("data");
			if (categoryArray != null && categoryArray.length() > 0) {
				for (int i = 0; i < categoryArray.length(); i++) {
					JSONObject itemObj = categoryArray.optJSONObject(i);
					if (itemObj != null) {
						SimpleTextEntity feed = new SimpleTextEntity(itemObj);
						feed.setType(type);
						if (feed != null) {
							list.add(feed);
						}
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
