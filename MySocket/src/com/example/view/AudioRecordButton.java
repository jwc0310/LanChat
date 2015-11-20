package com.example.view;

import com.example.audio.MyAudioManager;
import com.example.audio.MyAudioManager.AudioStateChangeListener;
import com.example.mysocket.R;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class AudioRecordButton extends Button {

	//录音状态
	private static final int STATE_NORMAL = 1;
	private static final int STATE_RECORDING = 2;
	private static final int STATE_WANT_CALCEL = 3;
	
	private static final int DISTANCE_CANCEL_Y = 50;
	
	private int currentState = STATE_NORMAL;
	private boolean isRecording = false;
	private AudioRecordDialog dialogManager;
	private MyAudioManager audioManager;
	
	private float mTime;
	//是否触发LongClick
	private boolean isReady = false;
	
	public AudioRecordButton(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}
	public AudioRecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Log.i("Andy","构造函数AudioRecordButton");
		dialogManager = new AudioRecordDialog(getContext());
		
		String dir = Environment.getExternalStorageDirectory()+"/andy_chat_audios";
		
		audioManager = MyAudioManager.getInstance(dir);
		audioManager.setOnAudioStateChangeListener(new MyOnAudioStateChangeListener());
		
		setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				isReady = true;
				Log.i("Andy", "start record ----1");
				audioManager.prepareAudio();
				Log.i("Andy", "start record ----2");
				return false;
			}
			
		});
		
	}
	
	/**
	 * 录完的回调函数
	 * @author c_jichen
	 *
	 */
	
	public interface AudioRecordFinishListener{
		void onFinish(float second, String filePath);
	}
	private AudioRecordFinishListener audioRecordFinishListener;
	
	public void setOnAudioRecordFinishListener(AudioRecordFinishListener listener){
		audioRecordFinishListener = listener;
	}
	
	
	class MyOnAudioStateChangeListener implements AudioStateChangeListener{
	
		@Override
		public void wellPrepared() {
			// TODO Auto-generated method stub
			Log.i("Andy", "MyOnAudioStateChangeListener wellPrepared()");
			mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
		}
	
	}
	
	private Runnable getVolumeRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(isRecording){
				try{
					Thread.sleep(100);
					mTime += 0.1f;
					mHandler.sendEmptyMessage(MSG_VOLUME_CHANGED);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
		
	};
	
	private static final int MSG_AUDIO_PREPARED = 0x110;
	private static final int MSG_VOLUME_CHANGED = 0x111;
	private static final int MSG_DIALOG_DISMISS = 0x112;
	private static final int STATE_WANT_CANCEL = 0;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_AUDIO_PREPARED:
				dialogManager.showDialog();
				isRecording = true;
				
				//volume
				new Thread(getVolumeRunnable).start();
				
				break;
				
			case MSG_VOLUME_CHANGED:
				dialogManager.updateVolumeLevel(audioManager.getVoiceLevel(7));
				break;
				
			case MSG_DIALOG_DISMISS:
				dialogManager.dismissDialog();
				break;
				
			default:
				break;
			}
		}
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		int action = event.getAction();
		int x = (int)event.getX();
		int y = (int)event.getY();
		//Log.i("Andy", "action = "+action+",x="+x+",y="+y);
		
		switch(action){
		case MotionEvent.ACTION_DOWN:
			changeState(STATE_RECORDING);
			break;
		case MotionEvent.ACTION_MOVE:
			if(isRecording){
				if(wantCancel(x,y)){
					changeState(STATE_WANT_CANCEL);
					dialogManager.stateWantCancel();
				}else {
					changeState(STATE_RECORDING);
					dialogManager.stateRecording();
				}
			}
			
			break;
		case MotionEvent.ACTION_UP:
			// 没有触发longClick
			if (!isReady) {
				resetState();
				return super.onTouchEvent(event);
			}
			// prepare未完成就up,录音时间过短
			if (!isRecording || mTime < 0.6f) {
				dialogManager.stateLengthShort();
				audioManager.cancel();
				mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DISMISS, 1300);
			} else if (currentState == STATE_RECORDING) { // 正常录制结束
				dialogManager.dismissDialog();
				audioManager.release();

				// callbackToActivity
				if (audioRecordFinishListener != null) {
					audioRecordFinishListener.onFinish(mTime,
							audioManager.getCurrentPath());
				}

			} else if (currentState == STATE_WANT_CANCEL) {
				dialogManager.dismissDialog();
				audioManager.cancel();

			}
			resetState();
			break;
		}
		
		return super.onTouchEvent(event);
		
	}
	private void resetState(){
		isRecording = false;
		isReady = false;
		changeState(STATE_NORMAL);
		mTime = 0;
	}
	
	private boolean wantCancel(int x, int y){
		if(x < 0 || x > getWidth())
			return true;
		if(y< -DISTANCE_CANCEL_Y || y > getHeight()+DISTANCE_CANCEL_Y) 
			return true;
		
		return false;
	}
	
	
	private void changeState(int state){
		if(currentState != state){
			currentState = state;
			switch(state){
			case STATE_NORMAL:
				Log.i("Andy","normal");
				setBackgroundResource(R.drawable.btn_recorder_normal);
				setText(R.string.btn_recorder_normal);
				
				break;
			case STATE_RECORDING:
				Log.i("Andy","recording");
				setBackgroundResource(R.drawable.btn_recorder_normal);
				setText(R.string.btn_recorder_recording);
				if(isRecording)
					dialogManager.stateRecording();
				break;
			case STATE_WANT_CALCEL:
				Log.i("Andy","cancel");
				setBackgroundResource(R.drawable.btn_recorder_normal);
				setText(R.string.btn_recorder_want_cancel);
				dialogManager.stateWantCancel();
				break;
			}
		}
	}
	
}
