package com.example.audio;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

public class MyAudioManager{

	MediaRecorder mRecorder = null;
	private String dir;
	private String currentFilePath;
	
	private static MyAudioManager audioInstance; //单例模式
	private MyAudioManager(String dir){
		this.dir = dir;
	}
	
	public static MyAudioManager getInstance(String dir){
		if(audioInstance == null){
			synchronized(MyAudioManager.class){
				audioInstance = new MyAudioManager(dir);
			}
		}
		return audioInstance;
	}
	
	//事件监听接口
	public interface AudioStateChangeListener{
		void wellPrepared();
	}
	
	public AudioStateChangeListener audioStateChangeListener;
	
	public void setOnAudioStateChangeListener(AudioStateChangeListener listener){
		Log.i("Andy", "fuzhi");
		audioStateChangeListener = listener;
	}
	
	public boolean isPrepared = false;
	
	public void prepareAudio(){
	try{
			Log.i("Andy","prepareAudio() --1");
			isPrepared = false;
			File fileDir = new File(dir);
			if(!fileDir.exists()){
				fileDir.mkdirs();
			}
			String filename = generateFileName();
			File file = new File(fileDir,filename);
			currentFilePath = file.getAbsolutePath();
			mRecorder = new MediaRecorder();
			//设置输出文件
			mRecorder.setOutputFile(currentFilePath);
			//设置音频源
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//设置音频格式
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			//设置音频编码
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			
			mRecorder.prepare();
			mRecorder.start();
			
			isPrepared = true;
			Log.i("Andy","prepareAudio() --2");
			if(audioStateChangeListener != null){
				Log.i("Andy","audioStateChangeListener.wellPrepared();");
				audioStateChangeListener.wellPrepared();
			}else{
				Log.i("Andy","audioStateChangeListener == null");
			}
			Log.i("Andy","prepareAudio() --3");
		}catch(Exception e){
			Log.i("Andy","prepareAudio() --3 -1");
			e.printStackTrace();
		}
		
		Log.i("Andy","prepareAudio() --4");
	}
		
	//随机产生文件名称
	private String generateFileName(){
		return UUID.randomUUID().toString()+".amr";
	}

	public int getVoiceLevel(int maxLevel){
		if(isPrepared){
			try{
				//// 振幅范围mediaRecorder.getMaxAmplitude():1-32767
				return maxLevel * mRecorder.getMaxAmplitude() / 32768 +1;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return 1;
	}
	
	public void release(){
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}
	
	public void cancel(){
		release();
		if(currentFilePath != null){
			File file = new File(currentFilePath);
			file.delete();
			currentFilePath = null;
		}
	}
	
	public String getCurrentPath(){
		return currentFilePath;
	}
	
}
