package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.dev.nutclass.ApplicationConfig;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;

 /**
 * @author LJ
 * 基本的用户信息(登录用户的信息)
 */
public class AddUserInfoEntity implements Serializable {
	private static final long serialVersionUID = -4108673652430439788L;
	/**
	 * 需要本地保存的数据
	 * */
	//返回建议名字
	private String name;//
	private String portrait;//
	private boolean isDuplicate=false;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public boolean isDuplicate() {
		return isDuplicate;
	}
	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}
	
 
	
}
