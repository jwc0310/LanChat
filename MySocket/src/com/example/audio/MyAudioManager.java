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
	
	private static MyAudioManager audioInstance; //����ģʽ
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
	
	//�¼������ӿ�
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
			//��������ļ�
			mRecorder.setOutputFile(currentFilePath);
			//������ƵԴ
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			//������Ƶ��ʽ
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			//������Ƶ����
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
		
	//��������ļ�����
	private String generateFileName(){
		return UUID.randomUUID().toString()+".amr";
	}

	public int getVoiceLevel(int maxLevel){
		if(isPrepared){
			try{
				//// �����ΧmediaRecorder.getMaxAmplitude():1-32767
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
