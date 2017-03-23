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

public class HomeListParser extends BaseParser<BaseCardEntity> {
	
	public int from=0;
	public HomeListParser(){}
	public HomeListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();
		try {
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONObject jsonObject = new JSONObject(jsonString);

			JSONArray bannerArray = jsonObject.optJSONArray("banner_list");
			if(bannerArray!=null&&bannerArray.length()>0){
				BannerCardEntity entity=new BannerCardEntity();
				List<ImageEntity> imgList=new ArrayList<ImageEntity>();
				for(int i=0;i<bannerArray.length();i++){
					ImageEntity imgEntity=new ImageEntity();
					imgEntity.setSmallPath(bannerArray.optString(i));
					imgList.add(imgEntity);
				}
				entity.setList(imgList);
				list.add(entity);
			}
			
			JSONArray courseArray = jsonObject.optJSONArray("goods_list");
			if (courseArray != null && courseArray.length() > 0) {
				for (int i = 0; i < courseArray.length(); i++) {
					JSONObject itemObj = courseArray.optJSONObject(i);
					if (itemObj != null) {
						BaseCardEntity feed = new CourseCardEntity(itemObj);
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
