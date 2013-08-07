package com.sem2458.RSSPlus;

import java.util.ArrayList;




import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FeedFragment extends Fragment {
		public ListView list;
		public RSSListAdapter2 adapter;
		ArrayList<Object> a;
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			MainActivity.id = this.getId();
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState){
			setHasOptionsMenu(true);
			View V = inflater.inflate(R.layout.feed_list_fragment, container, false);
			list = new ListView(getActivity().getApplicationContext());
			DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), DatabaseHandlerHelper.dataBaseName);
			a = new ArrayList<Object>();
			a = handler.getAllFeeds();
			list =(ListView)V.findViewById(R.id.feed_list_view);
			registerForContextMenu(list);
			setListClick();
			adapter = new RSSListAdapter2(getActivity(), a, false,getActivity().getApplicationContext());
			
			list.setAdapter(adapter);
			return V;
			
		}
		
		private void setListClick() {
			list.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> customerAdapter, View view, int selectedInt, long selectedLong){
					TextView t = (TextView)view.findViewById(R.id.listtext1);
					String s = t.getText().toString();
					Intent intent = new Intent(getActivity(), SecondActivity.class);
					intent.putExtra("feed", s);
					int result  = 1;
					startActivityForResult(intent, result);
				}	
			});
		}

		@Override
		public void onCreateOptionsMenu( Menu menu, MenuInflater inflater){
			inflater.inflate(R.menu.feed_list_menu, menu);
		}
		
		
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		   // handle item selection
		   switch (item.getItemId()) {
		      case R.id.new_feed:
		         FragmentManager fm = getFragmentManager();
		         NewFeedDialogFragment f = new NewFeedDialogFragment();
		         f.show(fm, "fragment_new_feed_dialogfrag");
		         return true;
		      case R.id.refresh_feeds:
		    	 siteCheckThread sct = new siteCheckThread(getActivity().getApplicationContext(), getActivity(),null, true, true);
		    	 Thread t = new Thread(sct);
		    	 t.start();
		    	 return true;
		      default:
		         return super.onOptionsItemSelected(item);
		   }
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
			super.onCreateContextMenu(menu, v, menuInfo);
			menu.add(R.string.delete_feed);
			menu.add(R.string.refresh_feed);
		}
		
		@Override  
	    public boolean onContextItemSelected(MenuItem item) {  
	        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	        View v = info.targetView;
	        TextView t = (TextView)v.findViewById(R.id.listtext1);
	        TextView t2 = (TextView)v.findViewById(R.id.listtext2);
	        String title = t.getText().toString();
			DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), DatabaseHandlerHelper.dataBaseName);
			Channel channel = handler.getFeedRow(null, title);
			if(item.getTitle() == getString(R.string.delete_feed)){
				handler.deleteFeedRow(false, title, 0);
				onSelectedListener callback = (onSelectedListener)getActivity();
				callback.onDone();
	        }else if(item.getTitle() == getString(R.string.refresh_feed)){
	        	siteCheckThread thr = new siteCheckThread(getActivity().getApplicationContext(), getActivity(),channel, false, true);
				Thread thread = new Thread(thr);
				thread.start();
				
	        }
	    return true;  
	    }  

}
