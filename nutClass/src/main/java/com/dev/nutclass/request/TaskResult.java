package com.dev.nutclass.request;



public class TaskResult{
	
	private int reqCode; 
	private String reqMsg;
	private String reqResultJson;
	public TaskResult() {}
	public int getReqCode() {
		return reqCode;
	}
	public void setReqCode(int reqCode) {
		this.reqCode = reqCode;
	}
	public String getReqMsg() {
		return reqMsg;
	}
	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}
	public String getReqResultJson() {
		return reqResultJson;
	}
	public void setReqResultJson(String reqResultJson) {
		this.reqResultJson = reqResultJson;
	}

 
}
