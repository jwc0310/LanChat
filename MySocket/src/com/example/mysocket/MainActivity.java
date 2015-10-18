package com.example.mysocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements Runnable,OnClickListener{

	private Button send;
	private EditText et;
	private TextView inputIP,tv;
	
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
        /*
        send = (Button)findViewById(R.id.button1);
        et = (EditText)findViewById(R.id.editText1);
        tv = (TextView)findViewById(R.id.textView1);
        
        send.setOnClickListener(this);
        */
        
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Socket socket;
		String message = et.getText().toString() + "\r\n";
		
		try{
			
			socket = new Socket("192.168.61.110",54321);
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
			out.println(message);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String msg = br.readLine();
			
			if(msg != null){
				tv.setText(msg);
			}else{
				tv.setText("data error.");
			}
			
			out.close();
			br.close();
			
			socket.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
