package com.dev.nutclass.entity;

import java.io.Serializable;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import org.json.JSONObject;
/**
* @author LJ
* 数据库数据
*/
@Table(name = "tb_db")
public class DBEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TYPE_COURSE_SHOPPING = 1;//课程购物车
	@Id
	private int id=0;
	private int type=1;
	private String uid="";
	private String jsonStr="";
	private long time=0;
	private String outId="";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getJsonStr() {
		return jsonStr;
	}
	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getOutId() {
		return outId;
	}
	public void setOutId(String outId) {
		this.outId = outId;
	}
 
 
}
