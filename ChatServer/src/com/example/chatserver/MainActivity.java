package com.example.chatserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private Button open,close,button;
	private TextView ip;
	
	private ServerSocket mServerSocket = null;
	private static final int PORT = 54321;
	private static ExecutorService mExecutorService;
	private static List<Socket> mClientList = new ArrayList<Socket>();
	WifiManager wifiManager = null;
	private BufferedReader mBufferedReader = null;
	private PrintWriter mPrintWriter = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainui);
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
            .detectDiskReads().detectDiskWrites().detectNetwork()  
            .penaltyLog().build());  
  
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
            .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()  
            .build());
		}
        
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        Log.i("Andy", "open server software");
        
        init();
    }
    
    private void init(){
    	open = (Button)findViewById(R.id.open);
    	close = (Button)findViewById(R.id.close);
    	ip = (TextView)findViewById(R.id.ip);
    	button = (Button)findViewById(R.id.button1);
    	
    	ip.setText(getServerIP());
    	
    	open.setOnClickListener(this);
    	close.setOnClickListener(this);
    	button.setOnClickListener(this);
    }

    
    
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.open:
			Log.i("Andy"," Click open button! ");
			if(mServerSocket == null){
				Log.i("Andy","create thread ! ");
				new Thread(openRun).start();
			}else{
				Log.i("Andy","has created! ");
			}
			
			break;
		case R.id.close:
			Log.i("Andy"," Click close button! ");
			try {
				mServerSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
			
		case R.id.button1:
			for(Socket client : mClientList){
				try {
					mPrintWriter = new PrintWriter(client.getOutputStream(),true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mPrintWriter.println("hello");
				mPrintWriter.flush();
				Log.i("Andy", "hello");
		}
			break;
			
			
		}
	}

	Runnable openRun = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				mServerSocket = new ServerSocket(54321);
				mExecutorService = Executors.newCachedThreadPool();
				Log.i("Andy", "Android Server Starting ...");
				Socket client = null;
				while(true){
					client = mServerSocket.accept();
					
					Log.i("Andy", "accept a client !");
					
					mClientList.add(client);
					mExecutorService.execute(new ThreadServer(client));
					
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	
	};
	
	class ThreadServer implements Runnable{
		
		private Socket mClient = null;
		
		private String mStrMSG = null;
		
		public ThreadServer(Socket client){
			this.mClient = client;
			try {
				//get
				mBufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
				mStrMSG = "user: "+this.mClient.getInetAddress()+ " client count "+mClientList.size();
				Log.i("Andy", mStrMSG);
				sendMessage();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//发给所有的客户端
		private void sendMessage() throws IOException{
			for(Socket client : mClientList){
					mPrintWriter = new PrintWriter(client.getOutputStream(),true);
					mPrintWriter.println(mStrMSG);
					mPrintWriter.flush();
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			/*
				while(true){
					Log.i("Andy", "true");

		    			try{
		    				if((mStrMSG = mBufferedReader.readLine())!= null){
		    					Log.i("Andy", mStrMSG+="\n");
		    					
		    					if(mStrMSG.trim().equals("exit")){
		    						mStrMSG="user: "+this.mClient.getInetAddress()+"exit total:"+mClientList.size();
									mClientList.remove(mClient);
									mBufferedReader.close();
									mPrintWriter.close();
									mClient.close();
		    					}else{
									mStrMSG = mClient.getInetAddress()+":"+mStrMSG; 
									
									sendMessage();
								}
		    					
		    				}
		    			}catch(Exception e){
		    				e.printStackTrace();
		    			}
					
				}
			*/
					try {
						while((mStrMSG = mBufferedReader.readLine())!=null){
							Log.i("Andy", mStrMSG);
							if(mStrMSG.trim().equals("exit")){
								mStrMSG="user: "+this.mClient.getInetAddress()+"exit total:"+mClientList.size();
								mClientList.remove(mClient);
								mBufferedReader.close();
								mPrintWriter.close();
								mClient.close();
							}else{
								mStrMSG = mClient.getInetAddress()+":"+mStrMSG; 
								
								sendMessage();
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
	
	}

    private String getServerIP(){
    	String ip = null;
    	
    	if(!wifiManager.isWifiEnabled()){
    		wifiManager.setWifiEnabled(true);
    	}
    	
    	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    	int ipAddress = wifiInfo.getIpAddress();
    	
    	ip = intToIp(ipAddress);
    	
    	return ip;
    }
    
    private String intToIp(int i){
    	 return (i & 0xFF ) + "." +       
    		        ((i >> 8 ) & 0xFF) + "." +       
    		        ((i >> 16 ) & 0xFF) + "." +       
    		        ( i >> 24 & 0xFF) ;
    }
	
	
}
