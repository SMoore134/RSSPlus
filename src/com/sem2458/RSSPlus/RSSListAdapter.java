package com.sem2458.RSSPlus;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RSSListAdapter extends ArrayAdapter<Object>{
	private Activity mContext;
	private List<Object> objects;
	public RSSListAdapter(Context context, int textViewResourceId,
			List<Object> objects) {
		super(context, textViewResourceId, objects);
		this.mContext = (Activity)context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = mContext.getLayoutInflater();
		View row = inflater.inflate(R.layout.item_row, null);
		TextView text = (TextView)row.findViewById(R.id.itemtext1);
		ImageView image = (ImageView)row.findViewById(R.id.imageView1);
		Item item = (Item) objects.get(position);
		text.setText(item.title);
		if(item.favorite.equals("1")){
			image.setImageResource(R.drawable.rating_important);
		}
		return row;
		
	}
}	