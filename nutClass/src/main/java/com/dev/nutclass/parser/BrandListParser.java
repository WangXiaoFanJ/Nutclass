package com.dev.nutclass.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.JsonDataList;

public class BrandListParser extends BaseParser<BaseCardEntity> {
	
	public int from=0;
	public BrandListParser(){}
	public BrandListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
//			"brand_id":"20",
//			"brand_name":"金宝贝",
//			"branch_url":"http://www.nutclass.com/data/brandlogo/1435674139667711166.jpg"
			JSONArray brandArray = jsonObject.optJSONArray("data");
			if (brandArray != null && brandArray.length() > 0) {
				for (int i = 0; i < brandArray.length(); i++) {
					JSONObject itemObj = brandArray.optJSONObject(i);
					if (itemObj != null) {
						ImageEntity entity = new ImageEntity();
						entity.setSmallPath(itemObj.optString("branch_url"));
						entity.setImgName(itemObj.optString("brand_name"));
						entity.setId(itemObj.optString("brand_id"));
						
						if (entity != null) {
							list.add(entity);
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
