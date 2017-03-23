package com.dev.nutclass.entity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import org.json.JSONObject;

public class JsonDataList<T> extends JsonResult<JsonDataList<T>>{
	
	private int total;//总数
	private int page;//页数
	private int pageNum;//每页个数
	private int maxId;//最后一条Id
	
	private ArrayList<T> mList;
	private ArrayList<ArrayList<BaseCardEntity>> brandList;
	private T obj;

	public ArrayList<T> getList() {
		return mList;
	}
	public void setList(ArrayList<T> mList) {
		this.mList = mList;
	}

	public ArrayList<ArrayList<BaseCardEntity>> getBrandList() {
		return brandList;
	}

	public void setBrandList(ArrayList<ArrayList<BaseCardEntity>> brandList) {
		this.brandList = brandList;
	}

	public int getTotal() {
		return total;
	}
	public void setTotal(int totalCount) {
		this.total = totalCount;
	}
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	//分页信息解析
	public void optPageBaseJson(JSONObject jsonObj){
		try {
			if (jsonObj != null) {
				this.setTotal(jsonObj.optInt("total",-1));
				this.setPage(jsonObj.optInt("page",-1));
				this.setMaxId(jsonObj.optInt("cursor",-1));
				this.setPageNum(jsonObj.optInt("num",-1));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	public int getMaxId() {
		return maxId;
	}
	public void setMaxId(int maxId) {
		this.maxId = maxId;
	}
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}

}
