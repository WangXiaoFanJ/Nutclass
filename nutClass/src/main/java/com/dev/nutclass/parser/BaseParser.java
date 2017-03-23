package com.dev.nutclass.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.JsonResult;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.TextUtil;

public abstract class BaseParser<T> implements IParser {
	public static final String TAG = "BaseParser";
	public static String replacement = "";
	protected Context mContext;

	public abstract Object parse(String jsonString) throws JSONException;

	public BaseParser() {
	}

	public BaseParser(Context context) {
		this.mContext = context;
	}

	@Override
	public Object parse(InputStream in) throws IOException, JSONException {
//		String jsonString = UrlHttpReqHelper.inputStream2String(in);
//		LogUtil.d(TAG, "Parser Response Content :" + jsonString);
//		return parse(jsonString);
		return null;
	}


	/**
	 * 返回的json是"errorCode":{ "errorCode":"0", "errorMsg":"失败原因" }
	 * 使用该法解析出errorCode，判断errorMsg为成功时，返回data的内容
	 * 
	 * @param jsonString
	 * @return JsonResult<T>
	 */
	protected JsonResult<T> handleJsonBaseInfo(String jsonString) {
		JsonResult<T> jsonResult = new JsonResult<T>();
		jsonResult.setErrorCode(UrlConst.SUCCESS_CODE);
		try {
			JSONObject result = new JSONObject(jsonString);
			int errorCode = result.optInt("status", UrlConst.SUCCESS_CODE);
			String errorMsg = result.optString("info", "request success");
			jsonResult.setErrorCode(errorCode);
			jsonResult.setErrorMsg(errorMsg);
			LogUtil.d(TAG, getClass().getSimpleName() + " result-->errorCode="
					+ errorCode + ",errorMsg=" + errorMsg);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonResult;
	}
	/**
	 * 返回的json是"errorCode":{ "errorCode":"0", "errorMsg":"失败原因" }
	 * 使用该法解析出errorCode，判断errorMsg为成功时，返回JsonResult的内容
	 * 
	 * @param jsonString
	 * @return JsonResult
	 */
	protected JsonResult<String> handleSimpleJsonBaseInfo(String jsonString,
			String optKey) {
		JsonResult<String> jsonResult = new JsonResult<String>();
		jsonResult.setErrorCode(UrlConst.SUCCESS_CODE);
		try {
			JSONObject result = new JSONObject(jsonString);
			int errorCode = result.optInt("status", UrlConst.SUCCESS_CODE);
			String errorMsg = result.optString("info", "request success");
			jsonResult.setErrorCode(errorCode);
			jsonResult.setErrorMsg(errorMsg);
			if (TextUtil.isNotNull(optKey)) {
				JSONObject dataObj=result.optJSONObject("data");
				if(dataObj!=null){
					jsonResult.setRetObj(dataObj.optString(optKey));
				}
			}
			LogUtil.d(TAG, getClass().getSimpleName() + " result-->errorCode="
					+ errorCode + ",errorMsg=" + errorMsg);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	/**
	 * 返回的json是"errorCode":{ "errorCode":"0", "errorMsg":"失败原因" }
	 * 使用该法解析出errorCode，判断errorMsg为成功时，返回data的内容
	 * 
	 * @param jsonString
	 * @return boolean
	 */
	protected boolean isParseFailure(String jsonString) {
		try {
			JSONObject result = new JSONObject(jsonString);
			int errorCode = result.optInt("code", UrlConst.SUCCESS_CODE);
			String errorMsg = result.optString("msg", "request success");
			LogUtil.d(TAG, getClass().getSimpleName() + " result-->errorCode="
					+ errorCode + ",errorMsg=" + errorMsg);
			if (errorCode != UrlConst.SUCCESS_CODE) {
				return true;
			}
			return false;
		} catch (JSONException e) {
			return false;
		}
	}
}
