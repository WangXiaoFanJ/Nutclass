package com.dev.nutclass.parser;

import com.dev.nutclass.entity.JsonResult;




public class SimpleInfoParser extends BaseParser<String> {
	private String key="";
	public SimpleInfoParser(){}
	public SimpleInfoParser(String key){
		this.key=key;
	}
	@Override
	public Object parse(String jsonString){
		JsonResult<String> retObj = handleSimpleJsonBaseInfo(jsonString,key);
		return retObj;
	}
}
