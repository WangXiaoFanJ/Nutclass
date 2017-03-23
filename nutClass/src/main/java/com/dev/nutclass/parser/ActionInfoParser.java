package com.dev.nutclass.parser;

import org.json.JSONObject;

import com.dev.nutclass.entity.ActionInfoCardEntity;
import com.dev.nutclass.entity.JsonResult;
/**
 * */
public class ActionInfoParser extends BaseParser<ActionInfoCardEntity> {
	private static final String TAG="ActionInfoParser";
	@Override
	public Object parse(String jsonString){
		JsonResult<ActionInfoCardEntity> retObj = new JsonResult<ActionInfoCardEntity>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
			JSONObject dataObj=jsonObject.optJSONObject("data");
			if(dataObj!=null){
				ActionInfoCardEntity entity=new ActionInfoCardEntity();
				entity.optJsonObj(dataObj);
				retObj.setRetObj(entity);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return retObj;
	}

}
