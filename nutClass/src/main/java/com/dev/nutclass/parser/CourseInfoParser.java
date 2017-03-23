package com.dev.nutclass.parser;

import org.json.JSONObject;

import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CourseCardEntity;
import com.dev.nutclass.entity.JsonResult;
/**
 * */
public class CourseInfoParser extends BaseParser<CourseCardEntity> {
	private static final String TAG="CourseInfoParser";
	@Override
	public Object parse(String jsonString){
		JsonResult<CourseCardEntity> retObj = new JsonResult<CourseCardEntity>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
			JSONObject dataObj=jsonObject.optJSONObject("data");
			if(dataObj!=null){
				CourseCardEntity entity=new CourseCardEntity();
				entity.optJsonObjInfo(dataObj);
				retObj.setRetObj(entity);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return retObj;
	}

}
