package com.dev.nutclass.parser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.utils.TextUtil;

public class UserListParser extends BaseParser<UserEntity> {

	@Override
	public Object parse(String jsonString){
		JsonDataList<UserEntity> retObj= new JsonDataList<UserEntity>();
		try {
			 
			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
			retObj.optPageBaseJson(jsonObject);
			JSONArray jsonArray=jsonObject.optJSONArray("data");
			ArrayList<UserEntity> list=new ArrayList<UserEntity>();
			if(jsonArray!=null&&jsonArray.length()>0){
				for(int i=0;i<jsonArray.length();i++){
					UserEntity feed = new UserEntity(jsonArray.optJSONObject(i));
					list.add(feed);
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
