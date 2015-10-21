package com.example.mysocket;

public class MessageVo {

	public static final int MESSAGE_FROM = 0;
	public static final int MESSAGE_TO =1;
	
	private int dir;
	private String content;
	private String time;
	
	public MessageVo(int dir,String content,String time){
		
		this.dir = dir;
		this.content = content;
		this.time = time;
		
	}
	
	public int getDir(){
		return dir;
	}
	public void setDir(int dir){
		this.dir = dir;
	}
	
	public String getContent(){
		return content;
	}
	
	public void setContent(String content){
		this.content = content;
	}
	
	public String getTime(){
		return time;
	}
	
	public void setTime(String time){
		this.time = time;
	}
	
	
}
