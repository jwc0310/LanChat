package com.example.video;

import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Environment;

public class MyRecorder{

	MediaRecorder mRecorder = null;
	String fileName = Environment.getExternalStorageState()+"/audiorecordtest.3gp";
	public MyRecorder(){
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//不活生源方式
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setOutputFile(fileName);
	}
	public void start(){
		try{
			mRecorder.prepare();
		}catch(IOException e){
			e.printStackTrace();
		}
		mRecorder.start();
	}
	
	public void stop(){
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}
	
}
