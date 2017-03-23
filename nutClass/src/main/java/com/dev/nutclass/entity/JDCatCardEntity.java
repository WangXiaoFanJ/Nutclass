package com.dev.nutclass.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;


public class JDCatCardEntity extends BaseCardEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<BaseCardEntity>  plist;


	private List<BaseCardEntity>  clist;
	
	private String selectedP;
	private String selectedC;

	

	public JDCatCardEntity() {
		setCardType(CARD_TYPE_JD_CAT_CARD);
	}

	public JDCatCardEntity(JSONArray jsonArray) {
		setCardType(CARD_TYPE_JD_CAT_CARD);
		optJsonObj(jsonArray);
	}

	

	
	public void optJsonObj(JSONArray jsonArray) {
		if (jsonArray == null)
			return;
		if (plist == null) {
			plist = new ArrayList<BaseCardEntity>();
		}
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject jsonObj=jsonArray.optJSONObject(i);
			SingleItemCardEntity entity=new SingleItemCardEntity();
			entity.setId(jsonObj.optString("id"));
			entity.setDesc(jsonObj.optString("name"));
			plist.add(entity);
			
			JSONArray arr=jsonObj.optJSONArray("arr");
			if (arr != null&&arr.length()>0){
				
				if (clist == null) {
					clist = new ArrayList<BaseCardEntity>();
				}
				for (int j = 0; j < arr.length(); j++) {
					JSONObject arrObj=arr.optJSONObject(j);
					if(arrObj==null){
						continue;
					}
					SingleItemCardEntity cEntity=new SingleItemCardEntity();
					cEntity.setId(arrObj.optString("l_id"));
					cEntity.setDesc(arrObj.optString("l_name"));
					cEntity.setTag(entity.getId());
					if(i==0&&j==0){
						selectedP=entity.getId();
						selectedC=cEntity.getId();
						
					}
					clist.add(cEntity);
				}
			}
			
		}

	}

	public List<BaseCardEntity> getClist(String tag) {
		if(TextUtils.isEmpty(tag)||clist==null){
			return null;
		}
		
		List<BaseCardEntity>  tmplist=new ArrayList<BaseCardEntity>();
		for(int i=0;i<clist.size();i++){
			if(((SingleItemCardEntity)(clist.get(i))).getTag().equals(tag)){
				tmplist.add(clist.get(i));
			}
		}
		return tmplist;
	}

	public void setClist(List<BaseCardEntity> clist) {
		this.clist = clist;
	}
	public List<BaseCardEntity> getPlist() {
		
		return plist;
	}

	public void setPlist(List<BaseCardEntity> plist) {
		this.plist = plist;
	}

	public String getSelectedP() {
		return selectedP;
	}

	public void setSelectedP(String selectedP) {
		this.selectedP = selectedP;
	}

	public String getSelectedC() {
		return selectedC;
	}

	public void setSelectedC(String selectedC) {
		this.selectedC = selectedC;
	}
	 
}
