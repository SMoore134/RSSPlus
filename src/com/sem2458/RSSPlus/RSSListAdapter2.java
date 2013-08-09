package com.sem2458.RSSPlus;

import java.util.ArrayList;
import java.util.HashMap;

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
	private static LayoutInflater inflater=null;
	private Activity activity;
	private ArrayList<Object> data;
	private boolean item;
	private Context context;

	public RSSListAdapter2(Activity activity, ArrayList<Object> data, boolean item, Context context){
		super(context,R.layout.list_row,data);
		
		this.activity = activity;
		this.data = data;
		this.item =item;
		this.context = context;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return data.size();
	}

	
	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(item){
			View view = arg1;
			if(arg1==null){
				view = inflater.inflate(R.layout.item_row, null);
			TextView t = (TextView)view.findViewById(R.id.itemtext1);
			ImageView v = (ImageView)view.findViewById(R.id.imageView1);
			Item i = (Item) data.get(position);
			Log.d("Stephen","RSSLISTADAPTER:"+i.title);
			t.setText(i.title);
			if(i.favorite==null)
				Log.d("Stephen","favorite is null");
			if(i.favorite.equals("1")){
				v.setImageResource(R.drawable.rating_important);
			}}
			return view;
		}else{
			View view = arg1;
			if(arg1==null)
				view = inflater.inflate(R.layout.list_row, null);
			TextView top = (TextView)view.findViewById(R.id.listtext1);
			TextView bottom = (TextView)view.findViewById(R.id.listtext2);
			Channel c = (Channel) data.get(position);
			top.setText(c.title);
			bottom.setText(DateHandler.LongToDate(c.lastBuildDate)+", ");
			return view;
		}
	}

}

