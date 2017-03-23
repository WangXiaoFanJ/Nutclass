package com.dev.nutclass.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.FeedCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.JsonDataList;

public class CircleListParser extends BaseParser<BaseCardEntity> {
	
	public int from=0;
	public CircleListParser(){}
	public CircleListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONObject jsonObject = new JSONObject(jsonString);

			
			
			JSONArray courseArray = jsonObject.optJSONArray("data");
			if (courseArray != null && courseArray.length() > 0) {
				for (int i = 0; i < courseArray.length(); i++) {
					JSONObject itemObj = courseArray.optJSONObject(i);
					if (itemObj != null) {
						BaseCardEntity feed = new FeedCardEntity(itemObj);
						if (feed != null) {
							list.add(feed);
						}
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
