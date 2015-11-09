package com.example.mysocket;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private static String TAG = "Andy";
	
	private Context context;
	private List<MessageVo> messageVo;
	
	public MessageAdapter(Context context, List<MessageVo> messageVo){
		this.context = context;
		this.messageVo = messageVo;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return messageVo.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messageVo.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = null;
		MessageVo message = messageVo.get(position);
		
		if( convertView == null || (holder = (ViewHolder)convertView.getTag()).flag != message.getDir()){
			holder = new ViewHolder();
			if(message.getDir() == MessageVo.MESSAGE_FROM){
				holder.flag = MessageVo.MESSAGE_FROM;
				convertView = LayoutInflater.from(context).inflate(R.layout.from_item, null);
			}else{
				holder.flag = MessageVo.MESSAGE_TO;
				convertView = LayoutInflater.from(context).inflate(R.layout.to_item, null);
			}
			
			holder.content = (TextView)convertView.findViewById(R.id.content);
			holder.time = (TextView)convertView.findViewById(R.id.time);
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.content.setText(message.getContent());
		holder.time.setText(message.getTime());
		
		return convertView;
	}
	
	
	
	static class ViewHolder
	{
		int flag;
		TextView content;
		TextView time;
	}

}
