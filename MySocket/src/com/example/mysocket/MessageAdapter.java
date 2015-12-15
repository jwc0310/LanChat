package com.example.mysocket;

import java.util.List;

import com.example.audio.MyAudioManager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MessageAdapter extends BaseAdapter {

	private static String TAG = "Andy";
	
	private Context context;
	private List<Msg> messageVo;
	MyAudioManager myAudio = new MyAudioManager();
	public MessageAdapter(Context context, List<Msg> messageVo){
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
		final Msg message = messageVo.get(position);
		int type = message.getType();
		switch(type){
		case Msg.MESSAGE_MSG:
			if( convertView == null || (holder = (ViewHolder)convertView.getTag()).flag != message.getDir()){
				holder = new ViewHolder();
				if(message.getDir() == Msg.MESSAGE_FROM){
					holder.flag = Msg.MESSAGE_FROM;
					convertView = LayoutInflater.from(context).inflate(R.layout.from_item, null);
				}else{
					holder.flag = Msg.MESSAGE_TO;
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
			holder.content.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(null == message.getPath()){
						Log.i("Andy", "I am Clicked now");
					}else{
						myAudio.startPlay(message.getPath());
					}
				}
				
			});
			break;
		case Msg.MESSAGE_AUD:
			
			break;
		case Msg.MESSAGE_PIC:
			break;
		}
		
		return convertView;
	}
	
	
	
	static class ViewHolder
	{
		int flag;
		TextView content;
		TextView time;
	}

}
