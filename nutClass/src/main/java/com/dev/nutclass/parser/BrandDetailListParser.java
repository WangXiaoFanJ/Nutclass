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

public class BrandDetailListParser extends BaseParser<BaseCardEntity> {
	
	public int from=0;
	public BrandDetailListParser(){}
	public BrandDetailListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
//			"product_id":"164",
//			"headerImageUrl":"http://www.nutclass.com/images/201506/thumb_img/164_thumb_G_1433213214315.jpg",
//			"courseName":"新东方在线职称英语vip系列课程，每满100返5元购物卡",
//			"levelInfo":4,
//			"returnFee":"5.00",
//			"price":"100.00"
			JSONArray brandArray = jsonObject.optJSONArray("list");
			if (brandArray != null && brandArray.length() > 0) {
				for (int i = 0; i < brandArray.length(); i++) {
					JSONObject itemObj = brandArray.optJSONObject(i);
					if (itemObj != null) {
						CourseCardEntity entity = new CourseCardEntity();
						entity.optJsonObjBrand(itemObj);
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
