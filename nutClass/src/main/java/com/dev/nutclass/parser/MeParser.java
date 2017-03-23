package com.dev.nutclass.parser;

import org.json.JSONObject;

import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.UserEntity;
/**
 * */
public class MeParser extends BaseParser<UserEntity> {
	private static final String TAG="CourseInfoParser";
	@Override
	public Object parse(String jsonString){
		JsonResult<UserEntity> retObj = new JsonResult<UserEntity>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
			JSONObject dataObj=jsonObject.optJSONObject("data");
			if(dataObj!=null){
				UserEntity entity=new UserEntity();
				entity.optUserInfo(dataObj);
				retObj.setRetObj(entity);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return retObj;
	}

}
