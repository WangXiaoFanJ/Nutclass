package com.dev.nutclass.db;

import java.util.List;

import net.tsz.afinal.FinalDb;
import android.content.Context;
import android.text.TextUtils;

import com.dev.nutclass.entity.DBEntity;
import com.dev.nutclass.utils.LogUtil;
import com.dev.nutclass.utils.SharedPrefUtil;
import com.dev.nutclass.utils.TextUtil;
import com.dev.nutclass.utils.Util;

public class NutClassDB {
	private static final String TAG = NutClassDB.class.getSimpleName();
	private static NutClassDB instance;
	private Context mContext;

	private FinalDb finalDB = null;
	private final static String DBNAME = "nutclass.db";

	public synchronized static NutClassDB getInstance(Context context) {
		if (instance == null) {
			instance = new NutClassDB(context);
		}
		return instance;
	}

	public NutClassDB(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		finalDB = FinalDb.create(context, DBNAME, true);
		
	}

	private void insertDBEntity(DBEntity entity) {
		entity.setTime(System.currentTimeMillis());
		finalDB.saveBindId(entity);
	}
	private void updateDBEntity(DBEntity entity){
		entity.setTime(System.currentTimeMillis());
		finalDB.update(entity);
	}
	public DBEntity getDBEntity(int id) {
		DBEntity entity = finalDB.findById(id,
				DBEntity.class);
		return entity;
	}
	//uid  type进行查询
	public void addDBEntity(DBEntity entity){
		if(TextUtil.isNotNull(SharedPrefUtil.getInstance().getUid())){
			entity.setUid(SharedPrefUtil.getInstance().getUid());
		}else{
			entity.setUid("-1");
		}
		String strWhere="type="+entity.getType()+"&outid="+entity.getOutId();
		String whereArg="uid='"+entity.getUid()+"'";
		if(!TextUtils.isEmpty(strWhere)){
			whereArg=whereArg+" and "+strWhere;
		}
		List<DBEntity> list = finalDB.findAllByWhere(
				DBEntity.class, whereArg,"time");
		if(list==null||list.size()==0){
			insertDBEntity(entity);
		}else{
			entity.setId(list.get(0).getId());
			updateDBEntity(entity);
		}
	}

	public void deleteById(int id) {
		finalDB.deleteById(DBEntity.class, id);
	}
	public void deleteByIds(int[] ids) {
		if(ids==null||ids.length<=0){
			return;
		}
		StringBuffer strWhere=new StringBuffer();
		strWhere.append("id in (");
		for(int i=0;i<ids.length-1;i++){
			strWhere.append(ids[i]).append(",");
		}
		strWhere.append(ids[ids.length-1]).append(")");
		finalDB.deleteByWhere(DBEntity.class, strWhere.toString());
	}
	public List<DBEntity> getDBList(String strWhere) {
		String uid=SharedPrefUtil.getInstance().getUid();
		String whereArg="uid='"+uid+"'";
		if(!TextUtils.isEmpty(strWhere)){
			whereArg=whereArg+" and "+strWhere;
		}
		List<DBEntity> list = finalDB.findAllByWhere(
				DBEntity.class, whereArg,"time");
		return list;
	}
	public List<DBEntity> getDBListByType(int type) {
		String where="type='"+type+"'"; 
		return getDBList(where);
	}
	
}
