package com.dev.nutclass.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.MarketCardEntity;
import com.dev.nutclass.entity.ZeroCardEntity;

public class MarketListParser extends BaseParser<BaseCardEntity> {
	
	public int from=0;
	public MarketListParser(){}
	public MarketListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {
			retObj.setErrorCode(UrlConst.SUCCESS_CODE);
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONArray actionArray = new JSONArray(jsonString);
			if (actionArray != null && actionArray.length() > 0) {
				for (int i = 0; i < actionArray.length(); i++) {
					JSONObject itemObj = actionArray.optJSONObject(i);
					if (itemObj != null) {
						MarketCardEntity feed = new MarketCardEntity();
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
