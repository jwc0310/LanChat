//package com.example.mysocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			
			
			ServerSocket serverSocket = new ServerSocket(54321);
			while(true){
				//���ܿͻ�������
				Socket client = serverSocket.accept();
				System.out.println("accept");
				
				try{
					//���տͻ�����Ϣ
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String str = in.readLine();
					System.out.println("read:"+str);
					
					//��ͻ��˷�����Ϣ
					PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())),true);
					out.println("server message");
					
					out.close();
					in.close();
					
				}catch(Exception e){
					System.out.println(e.getMessage());
					e.printStackTrace();
				}finally{
					//
					client.close();
					System.out.println("close");
				}
				
			}
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void main(String a[]){
		Thread desktopServerThread = new Thread(new Server());
		desktopServerThread.start();
	}
	
	

}
