package com.sem2458.RSSPlus;

import java.util.ArrayList;



import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ItemListFragment extends Fragment{

	
	public ListView list;
	public RSSListAdapter adapter;
	public String feedName;
	public ArrayList<Object> a2 = new ArrayList<Object>();

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		setHasOptionsMenu(true);
		View V = inflater.inflate(R.layout.item_list_fragment, container, false);
		list = new ListView(getActivity().getApplicationContext());
		DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), DHH.dataBaseName);
		ArrayList<Object> a = new ArrayList<Object>();
		Bundle b = this.getArguments();
		
		if(b.containsKey("feedName")){
			feedName = (String) b.get("feedName");
			a = handler.getAllItems(feedName);
			for(Object o:a){
				Item it = (Item)o;
				Log.d("Stephen",it.title);
				Log.d("Stephen",String.valueOf(it.pubDate));
			}
			getActivity().setTitle(feedName);
			getActivity().getActionBar().show();
		}else if(b.containsKey("favorites")){
			a = handler.getFavorites();
			for(Object o:a){
				Item it = (Item)o;
				Log.d("Stephen",it.title);
				Log.d("Stephen",String.valueOf(it.id));
			}
			getActivity().getActionBar().show();
		}else if(b.containsKey("all")){
			a = handler.getAll();
			for(Object o:a){
				Item it = (Item)o;
				Log.d("Stephen",it.title);
				Log.d("Stephen",String.valueOf(it.id));
			}
			getActivity().getActionBar().show();
		}
		a2 = a;
		list =(ListView)V.findViewById(R.id.item_list_view);
		registerForContextMenu(list);
		setListClick();
		adapter = new RSSListAdapter(getActivity(), 0, a);
		
		list.setAdapter(adapter);
		return V;

	}

	private void setListClick() {
		list.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> customerAdapter, View view, int selectedInt, long selectedLong){
				
				
				DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), DHH.dataBaseName);
				Item i = (Item) a2.get(selectedInt);
				i.read = "1";
				handler.updateItemRow(i);
				Bundle b = new Bundle();
				b.putBoolean("isData", true);
				b.putString("Data", i.description);
				b.putString("Title", i.title);
				b.putString("Link", i.link);
				Fragment f = new WebViewFragment();
				f.setArguments(b);
				getFragmentManager().beginTransaction().addToBackStack("Trans").replace(R.id.main_activity, f).commit();
				
//				Intent intent = new Intent(getActivity(), WebViewActivity.class);
//				intent.putExtra("Bundle", b);
//				int result  = 1;
//				startActivityForResult(intent, result);
			}	
		});
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(R.string.delete);
		menu.add(R.string.favorite);
	}
	
	@Override  
    public boolean onContextItemSelected(MenuItem item) {  
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        View v = info.targetView;
        TextView t = (TextView)v.findViewById(R.id.itemtext1);
        String title = t.getText().toString();
		DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), DHH.dataBaseName);
		onSelectedListener callback = (onSelectedListener)getActivity();
		Item i = (Item) a2.get(info.position);
		if(item.getTitle() == getString(R.string.delete)){
			handler.deleteFeedRow(true, null,i.id);
			Log.d("Stephen","delete");
			callback.onDone(this.getId());
        }else if(item.getTitle() == getString(R.string.favorite)){
        	Log.d("Stephen","favorite");
        	
        	ImageView iv;
        	if(i.favorite.equals("0")){
        		i.favorite="1";
        		
        	}else{
        		i.favorite = "0";
        		
        	}handler.updateItemRow(i);
        	Log.d("Stephen","callback");
        	callback.onDone(this.getId());
        }
    return true;  
    }  


}
