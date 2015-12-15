package com.example.mysocket;

public class Msg {

	public static final int MESSAGE_FROM = 0;
	public static final int MESSAGE_TO =1;
	
	public static final int MESSAGE_MSG = 0;
	public static final int MESSAGE_AUD = 1;
	public static final int MESSAGE_PIC = 2;
	
	private int dir;
	private String content;
	private String time;
	private int msgType;
	private String path;
	
	public Msg(){
		
	}
	
	public Msg(int dir,String content,String time,int msgType,String path){
		
		this.dir = dir;
		this.content = content;
		this.time = time;
		this.msgType = msgType;
		this.path = path;
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
	
	public void setType(int type){
		this.msgType = type;
	}
	
	public int getType(){
		return msgType;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
}
