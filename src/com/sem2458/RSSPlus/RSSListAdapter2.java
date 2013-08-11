package com.sem2458.RSSPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RSSListAdapter2 extends ArrayAdapter<Object>{
	private Activity mContext;
	private List<Object> objects;
	public RSSListAdapter2(Context context, int textViewResourceId,
			List<Object> objects) {
		super(context, textViewResourceId, objects);
		this.mContext = (Activity)context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = mContext.getLayoutInflater();
		View row = inflater.inflate(R.layout.list_row, null);
		TextView text = (TextView)row.findViewById(R.id.listtext1);
		TextView text2 = (TextView)row.findViewById(R.id.listtext2);
		ImageView image = (ImageView)row.findViewById(R.id.imageView1);
		DatabaseHandler handler = new DatabaseHandler(mContext, DHH.dataBaseName);
		Channel c = (Channel)objects.get(position);
		int i = handler.getUnread(c.title);
		text.setText(c.title);
		text2.setText("Updated: " + DateHandler.LongToDate(c.lastBuildDate) + ", Unread: " + i);
		if(i>0)
			image.setImageResource(R.drawable.navigation_next_item);
		
		return row;
		
	}
}	