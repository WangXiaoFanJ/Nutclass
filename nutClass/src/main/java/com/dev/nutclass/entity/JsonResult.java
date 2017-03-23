package com.dev.nutclass.entity;



import org.json.JSONException;
import org.json.JSONObject;


public class JsonResult<T> {
	
	private int errorCode; 
	private String errorMsg;
	private T retObj;
	public JsonResult() {}

	public JsonResult(int errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	public JsonResult(JSONObject jsonObj) throws JSONException {
		super();
		optBaseJsonResult(jsonObj);
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public T getRetObj() {
		return retObj;
	}
	public void setRetObj(T retObj) {
		this.retObj = retObj;
	}

	public void optBaseJsonResult(JSONObject jsonObj){
		if (jsonObj != null) {
			int errorCode = jsonObj.optInt("status");
			String errorMsg = jsonObj.optString("info", "");
			
			//网络信息解析
			this.setErrorCode(errorCode);
			this.setErrorMsg(errorMsg);
		}
	}
}
