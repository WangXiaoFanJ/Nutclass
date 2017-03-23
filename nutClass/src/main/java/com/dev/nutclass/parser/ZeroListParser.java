package com.dev.nutclass.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.ZeroCardEntity;

public class ZeroListParser extends BaseParser<BaseCardEntity> {
	
	public int from=0;
	public ZeroListParser(){}
	public ZeroListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {

			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
			retObj.setErrorCode(UrlConst.SUCCESS_CODE);
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONArray actionArray = jsonObject.optJSONArray("list");
			if (actionArray != null && actionArray.length() > 0) {
				for (int i = 0; i < actionArray.length(); i++) {
					JSONObject itemObj = actionArray.optJSONObject(i);
					if (itemObj != null) {
						ZeroCardEntity feed = new ZeroCardEntity();
						feed.optJsonObj(itemObj);
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
