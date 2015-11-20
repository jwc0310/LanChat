package com.example.mysocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.view.AudioRecordButton;
import com.example.view.AudioRecordButton.AudioRecordFinishListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener,OnCheckedChangeListener{

	private Button connect,send;
	private AudioRecordButton input_voice;
	private ToggleButton toggle;
	private EditText et;
	private TextView inputIP,edit;
	private List<MessageVo> messageList = new ArrayList<MessageVo>();
	private ListView list;
	
	LinearLayout ll_input;
	FrameLayout ll_voice;
	
	private static Socket client = null;
	PrintWriter out=null;
	BufferedReader br = null;
	
	MessageAdapter myAdapter = new MessageAdapter(this,messageList);
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatui);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
            .detectDiskReads().detectDiskWrites().detectNetwork()  
            .penaltyLog().build());  
  
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
            .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()  
            .build());
		}
        
        initWidget();
        
        list.setAdapter(myAdapter);
        
    }
    
    private void initWidget(){
    	inputIP = (TextView)findViewById(R.id.inputip);
    	connect = (Button)findViewById(R.id.connect);
    	send = (Button)findViewById(R.id.send);
    	list = (ListView)findViewById(R.id.list);
    	edit = (TextView)findViewById(R.id.edit);
    	toggle = (ToggleButton)findViewById(R.id.toggle);
    	ll_input = (LinearLayout)findViewById(R.id.ll_input);
    	ll_voice = (FrameLayout)findViewById(R.id.ll_voice);
    	input_voice = (AudioRecordButton)findViewById(R.id.input_voice);
    	
    	connect.setOnClickListener(this);
    	send.setOnClickListener(this);
    	toggle.setOnCheckedChangeListener(this);
    	input_voice.setOnAudioRecordFinishListener(new MyAudioRecordFinishListener());
    }
    
    class MyAudioRecordFinishListener implements AudioRecordFinishListener {

		@Override
		public void onFinish(float second, String filePath) {
			// TODO Auto-generated method stub
			Log.i("Andy","time is "+second);
			Log.i("Andy","filePath "+filePath);
		}

	}
    private void sendMessage(String str){
    	Log.i("Andy", str);
    	try {
			out = new PrintWriter(client.getOutputStream(),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	out.println(str);
    	out.flush();
    }

    Runnable mRunnable = new Runnable(){
    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
    		String str = null;
    		while(true){
    			Log.i("Andy", "true");
    			try{
    				if((str = br.readLine())!= null){
    					Log.i("Andy", str+="\n");

    					SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
    					String time = sdf.format(new Date()).toString();
    					Log.i("time----:", time);
    					
    					if(str != null){
    						messageList.add(new MessageVo(MessageVo.MESSAGE_FROM,str,time));
    						myAdapter.notifyDataSetChanged();
    					}
    				}
    			}catch(Exception e){
    				e.printStackTrace();
    			}
    		}
    		
    	}
    };
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.connect:
			connectServer();
			break;
		case R.id.send:
			String sendMessage = edit.getText().toString();
			sendMessage(sendMessage);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
			String time = sdf.format(new Date()).toString();
			Log.i("time----:", time);
			
			if(sendMessage != null){
				messageList.add(new MessageVo(MessageVo.MESSAGE_TO,sendMessage,time));
				myAdapter.notifyDataSetChanged();
			}
			edit.setText("");
			break;
		}
	}
	
    private void connectServer(){
    	String ip = inputIP.getText().toString().trim();
    	int port = 54321;
    	
    	if(client == null){
    		try {
    			client = new Socket(ip,port);
    			Log.i("Andy", "connect");
    			
    			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
    			new Thread(mRunnable).start();
    			
    		} catch (UnknownHostException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}else{
    		Log.i("Andy", "has connected to server!");
    	}
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			Log.i("Andy", "font input");
			ll_input.setVisibility(View.VISIBLE);
			ll_voice.setVisibility(View.GONE);
		}
		else{
			Log.i("Andy", "voice input");
			ll_input.setVisibility(View.GONE);
			ll_voice.setVisibility(View.VISIBLE);
		}
	}
	
}
