package com.sem2458.RSSPlus;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RSSListAdapter2 extends ArrayAdapter<Object>{
	
	private Activity mContext;
	private List<Object> objects;
	public Context context;

	public RSSListAdapter2(Context context, int i, List<Object> objects){
		super(context, i, objects);
		this.mContext = (Activity)context;
		this.objects = objects;
		this.context = context;
	}
	

	
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = mContext.getLayoutInflater();
		View row = inflater.inflate(R.layout.list_row, null);
		TextView top = (TextView)row.findViewById(R.id.listtext1);
		TextView bottom = (TextView)row.findViewById(R.id.listtext2);
		Channel c = (Channel)objects.get(position);
		top.setText(c.title);DatabaseHandler handler = new DatabaseHandler(context, DatabaseHandlerHelper.dataBaseName);
		bottom.setText("Updated: " + DateHandler.LongToDate(c.lastBuildDate) + " Unread: "+handler.getUnread(c.title));
		return row;
	}

}

