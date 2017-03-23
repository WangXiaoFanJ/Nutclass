package com.dev.nutclass.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.activity.CardListActivity;
import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.ImageEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.MarketCardEntity;
import com.dev.nutclass.entity.ZeroCardEntity;

public class MeCourseListParser extends BaseParser<BaseCardEntity> {
	
	public int from=-1;
	public MeCourseListParser(){}
	public MeCourseListParser(int from){
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
						if(from==-1){
							BaseCardEntity feed = new CourseCardEntity(itemObj);
							if (feed != null) {
								list.add(feed);
							}
						}else{
							if(from==CardListActivity.TYPE_FROM_ZERO){
								ZeroCardEntity feed = new ZeroCardEntity();
								feed.optUserInfoJsonObj(itemObj);
								if (feed != null) {
									((ZeroCardEntity)feed).setFrom(from);
									list.add(feed);
								}
							}else if(from==CardListActivity.TYPE_FROM_BOOK){
								CourseCardEntity feed = new CourseCardEntity();
								feed.optUserInfoBookJsonObj(itemObj);
								if (feed != null) {
									((CourseCardEntity)feed).setFrom(from);
									list.add(feed);
								}
							}else if(from==CardListActivity.TYPE_FROM_WAIT_COMMENT){
								CourseCardEntity feed = new CourseCardEntity();
								feed.optUserInfoJsonObj(itemObj);
								if (feed != null) {
									((CourseCardEntity)feed).setFrom(from);
									list.add(feed);
								}
							}else if(from==CardListActivity.TYPE_FROM_ALL_ORDER||from==CardListActivity.TYPE_FROM_WAIT_PAY){
								CourseCardEntity feed = new CourseCardEntity();
								feed.optUserInfoJsonObj02(itemObj);
								if (feed != null) {
									((CourseCardEntity)feed).setFrom(from);
									list.add(feed);
								}

							}else if(from==CardListActivity.TYPE_FROM_MARKET){
								MarketCardEntity feed = new MarketCardEntity(itemObj);
								feed.setMine(true);
								if (feed != null) {
									list.add(feed);
								}
							}
							
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
