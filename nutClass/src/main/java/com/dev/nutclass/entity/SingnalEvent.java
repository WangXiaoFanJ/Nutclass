package com.dev.nutclass.entity;

public class SingnalEvent {
	public static final String SINGNAL_UPDATE_USERINFO="singnal_update_userinfo";
	
	public SingnalEvent(String singnal) {
		this.singnal = singnal;
	}
	public SingnalEvent() {
	}
	private String singnal="";
	public String getSingnal() {
		return singnal;
	}
	public void setSingnal(String singnal) {
		this.singnal = singnal;
	}

}
