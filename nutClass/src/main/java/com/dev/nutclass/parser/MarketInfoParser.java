package com.dev.nutclass.parser;

import org.json.JSONObject;

import com.dev.nutclass.entity.ActionInfoCardEntity;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.MarketInfoCardEntity;
/**
 * */
public class MarketInfoParser extends BaseParser<MarketInfoCardEntity> {
	private static final String TAG="ActionInfoParser";
	@Override
	public Object parse(String jsonString){
		JsonResult<MarketInfoCardEntity> retObj = new JsonResult<MarketInfoCardEntity>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
			JSONObject dataObj=jsonObject.optJSONObject("data");
			if(dataObj!=null){
				MarketInfoCardEntity entity=new MarketInfoCardEntity();
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
