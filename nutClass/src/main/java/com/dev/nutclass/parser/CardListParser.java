package com.dev.nutclass.parser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dev.nutclass.constants.UrlConst;
import com.dev.nutclass.entity.ADAlertEntity;
import com.dev.nutclass.entity.AdCardEntity;
import com.dev.nutclass.entity.AgeCardEntity;
import com.dev.nutclass.entity.BannerCardEntity;
import com.dev.nutclass.entity.BaseCardEntity;
import com.dev.nutclass.entity.CooponCardEntity;
import com.dev.nutclass.entity.CooponNumEntity;
import com.dev.nutclass.entity.HeadCardEntity;
import com.dev.nutclass.entity.JsonDataList;
import com.dev.nutclass.entity.SchoolCardEntity;

public class CardListParser extends BaseParser<BaseCardEntity> {
	
	public int from=0;
	public CardListParser(){}
	public CardListParser(int from){
		this.from=from;
	}

	@Override
	public Object parse(String jsonString) {
		JsonDataList<BaseCardEntity> retObj = new JsonDataList<BaseCardEntity>();

		try {
			ArrayList<BaseCardEntity> list = new ArrayList<BaseCardEntity>();
			JSONObject jsonObject = new JSONObject(jsonString);
			

			JSONArray cardListArray = jsonObject.optJSONArray("data");
			if(cardListArray!=null&&cardListArray.length()>0){
				
				for(int i=0;i<cardListArray.length();i++){
					JSONObject jsonObj= cardListArray.getJSONObject(i);
					if(jsonObj==null){
						continue;
					}
					int type=jsonObj.optInt("cardType");
					BaseCardEntity entity=null;
					JSONArray cardContent=null;
					JSONObject cardObjContent=null;
					switch (type) {
					case BaseCardEntity.CARD_TYPE_AD:
						cardContent=jsonObj.optJSONArray("cardContext");
						entity=new AdCardEntity(type,cardContent);
						break;
					case BaseCardEntity.CARD_TYPE_BANNER:
						cardContent = jsonObj.optJSONArray("cardContext");
						entity = new BannerCardEntity(cardContent);
						break;
					case BaseCardEntity.CARD_TYPE_JD_BANNER:
						cardContent=jsonObj.optJSONArray("cardContext");
						entity=new BannerCardEntity(type,cardContent);
//						entity.setCardType(type);
						break;
					case BaseCardEntity.CARD_TYPE_SCHOOL:
						cardObjContent=jsonObj.optJSONObject("cardContext");
						entity=new SchoolCardEntity(cardObjContent);
						break;
					case BaseCardEntity.CARD_TYPE_HEAD:
						cardContent=jsonObj.optJSONArray("cardContext");
						entity=new HeadCardEntity(cardContent);
						break;
					case BaseCardEntity.CARD_TYPE_COOPON:
						cardObjContent=jsonObj.optJSONObject("cardContext");
						entity=new CooponCardEntity(cardObjContent);
						break;
					case BaseCardEntity.CARD_TYPE_COOPON_NUM:
						cardObjContent=jsonObj.optJSONObject("cardContext");
						entity=new CooponNumEntity(cardObjContent);
						break;
					case BaseCardEntity.CARD_TYPE_AGE:
						cardContent=jsonObj.optJSONArray("cardContext");
						entity=new AgeCardEntity(cardContent);
						break;
					case BaseCardEntity.CARD_TYPE_AD_001:
						cardContent=jsonObj.optJSONArray("cardContext");
						entity=new AdCardEntity(type,cardContent);
						break;
					case BaseCardEntity.CARD_TYPE_AD_002:
						cardObjContent=jsonObj.optJSONObject("cardContext");
						entity=new AdCardEntity(type,cardObjContent);
						break;
					case BaseCardEntity.CARD_TYPE_AD_003:
						cardContent=jsonObj.optJSONArray("cardContext");
						entity=new AdCardEntity(type,cardContent);
						break;
					case BaseCardEntity.CARD_TYPE_AD_004:
						cardContent=jsonObj.optJSONArray("cardContext");
						entity=new AdCardEntity(type,cardContent);
						break;
					case BaseCardEntity.CARD_TYPE_SCHOOL_LIST:
						cardObjContent=jsonObj.optJSONObject("cardContext");
						entity=new SchoolCardEntity(type,cardObjContent);
						break;
					case BaseCardEntity.CARD_TYPE_AlERT:
						cardObjContent=jsonObj.optJSONObject("cardContext");
						entity = new ADAlertEntity(type,cardObjContent);
						break;
						default:
						break;
					}
					if(entity.getCardType()==BaseCardEntity.CARD_TYPE_COOPON_NUM){
						retObj.setObj(entity);
					}else{
						list.add(entity);
					}
					
				}
			}
			
			
			retObj.setErrorCode(UrlConst.SUCCESS_CODE);
			retObj.setList(list);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return retObj;
	}

}
