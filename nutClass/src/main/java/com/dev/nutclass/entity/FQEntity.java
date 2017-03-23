package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
/**
* @author LJ
* Tag信息
*/
public class FQEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id="";
	private String name="";
	private String code="";
	private List<FQEntity> list=new ArrayList<FQEntity>();
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public List<FQEntity> getList() {
		return list;
	}
	public void setList(List<FQEntity> list) {
		this.list = list;
	}
	public void setId(String id) {
		this.id = id;
	}
	 
	public FQEntity(JSONObject jsonObject){
		//解析基本信息
		optBaseInfo(jsonObject);
	}
	public FQEntity(){
		//解析基本信息
		 
	}
	private void optBaseInfo(JSONObject baseObject){
		this.name=baseObject.optString("name");
		this.id=baseObject.optString("id");
		this.code=baseObject.optString("code");
		JSONArray fq=baseObject.optJSONArray("list");
		if(fq!=null&&fq.length()>0){
			if(list==null){
				list=new ArrayList<FQEntity>();
			}
			for(int i=0;i<fq.length();i++){
				FQEntity entity=new FQEntity();
				entity.setId(fq.optJSONObject(i).optString("key"));
				entity.setName(fq.optJSONObject(i).optString("name"));
				list.add(entity);
			}
		}
		 
	}
	 
}
