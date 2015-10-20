package com.example.mysocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener{

	private Button connect,send;
	private EditText et;
	private TextView inputIP,edit;
	private ListView list;
	
	private static Socket client = null;
	PrintWriter out=null;
	BufferedReader br = null;
	
	
	
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
    private void connectServer(){
    	String ip = inputIP.getText().toString().trim();
    	int port = 54321;
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
    }
    
    private void initWidget(){
    	inputIP = (TextView)findViewById(R.id.inputip);
    	connect = (Button)findViewById(R.id.connect);
    	send = (Button)findViewById(R.id.send);
    	list = (ListView)findViewById(R.id.list);
    	edit = (TextView)findViewById(R.id.edit);
    	
    	connect.setOnClickListener(this);
    	send.setOnClickListener(this);
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
			
			sendMessage(edit.getText().toString());
			break;
		}
		
	}
}
