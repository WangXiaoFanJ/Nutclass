package com.dev.nutclass.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.entity.ActionCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;

public class ActionListParser extends BaseParser<BaseCardEntity> {
	
	public int from=0;
	public ActionListParser(){}
	public ActionListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);

			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONArray actionArray = jsonObject.optJSONArray("data");
			if (actionArray != null && actionArray.length() > 0) {
				for (int i = 0; i < actionArray.length(); i++) {
					JSONObject itemObj = actionArray.optJSONObject(i);
					if (itemObj != null) {
						BaseCardEntity feed = new ActionCardEntity(itemObj);
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
