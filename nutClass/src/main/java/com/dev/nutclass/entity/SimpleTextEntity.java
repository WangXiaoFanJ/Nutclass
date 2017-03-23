package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;

import com.dev.nutclass.utils.TextUtil;
/**
* @author LJ
* 简单文本信息项
*/
public class SimpleTextEntity extends BaseCardEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String TYPE_CATEGORY = "1";
	public static final String TYPE_CATEGORY_CHILD = "2";
	public static final String TYPE_CITY= "3";
	private String id="";
	private String desc="";
	private String type=TYPE_CATEGORY;
	private String name="";
	private boolean isSelected=false;
	private ArrayList<ImageEntity> brandList;
	 
	public SimpleTextEntity(){
		//解析基本信息
		setCardType(BaseCardEntity.CARD_TYPE_SIMPLE_TEXT);
	}
	public SimpleTextEntity(JSONObject jsonObject){
		//解析基本信息
		optBaseInfo(jsonObject);
		setCardType(BaseCardEntity.CARD_TYPE_SIMPLE_TEXT);
	}
	private void optBaseInfo(JSONObject baseObject){
		this.id=baseObject.optString("cid");
		this.desc=baseObject.optString("cname");
		if(type==TYPE_CATEGORY){
			JSONArray array=baseObject.optJSONArray("brand_list");
			if(array!=null&&array.length()>0){
				int length=array.length();
				brandList=new ArrayList<ImageEntity>();
				for(int index=0;index<length;index++){
					JSONObject obj=array.optJSONObject(index);
					ImageEntity entity=new ImageEntity();
					entity.setId(obj.optString("brand_id"));
					entity.setName(obj.optString("brand_name"));
					entity.setSmallPath(obj.optString("brand_logo"));
					brandList.add(entity);
				}
			}
		}
		
	}
	public void optAgeInfo(JSONObject baseObject){
		this.id=baseObject.optString("id");
		this.desc=baseObject.optString("age");
		this.name=baseObject.optString("name");
		
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public ArrayList<ImageEntity> getBrandList() {
		return brandList;
	}
	public void setBrandList(ArrayList<ImageEntity> brandList) {
		this.brandList = brandList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
