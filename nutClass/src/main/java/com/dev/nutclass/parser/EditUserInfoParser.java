package com.dev.nutclass.parser;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.entity.UserEntity;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;
/**
 * 1、解析用户登陆成功以及用户修改个人信息成功之后的返回数据
 * 2、保存Token以及Session
 * */
public class EditUserInfoParser extends BaseParser<UserEntity> {
	private static final String TAG="EditUserInfoParser";
	@Override
	public Object parse(String jsonString){
		JsonResult<UserEntity> retObj = new JsonResult<UserEntity>();
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			retObj.optBaseJsonResult(jsonObject);
			JSONObject dataObj=jsonObject.optJSONObject("data");
			if(dataObj!=null){
				String token=dataObj.optString("token");
				UserEntity user= new UserEntity();
				user.optUserInfoEdit(dataObj);
				retObj.setRetObj(user);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return retObj;
	}

}
