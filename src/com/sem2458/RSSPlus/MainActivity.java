package com.sem2458.RSSPlus;

import com.sem2458.RSSPlus.R;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ArrayAdapter;


public class MainActivity extends Activity implements OnNavigationListener , onSelectedListener{

	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	public static int whichFrag;
	Tab tab;	
	public static int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar ab = getActionBar();
		 ab.setDisplayShowTitleEnabled(false);
		 final String[] dropdown = getResources().getStringArray(R.array.navigation_list);
		 ArrayAdapter<String> adapter = new ArrayAdapter<String>(ab.getThemedContext(),android.R.layout.simple_spinner_item,android.R.id.text1, dropdown);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		ab.setListNavigationCallbacks(adapter, this);
		Fragment f = new FeedFragment();
		whichFrag = 0;
		getFragmentManager().beginTransaction().add(R.id.main_activity, f).commit();

	}

	@Override
	  public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Restore the previously serialized current dropdown position.
	    if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
	      getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
	    }
	  }

	  @Override
	  public void onSaveInstanceState(Bundle outState) {
	    // Serialize the current dropdown position.
	    outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
	        .getSelectedNavigationIndex());
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.item_list_fragment, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		Fragment f;
		Bundle b;
		switch(itemPosition){
			case 0:
				f = new FeedFragment();

				getFragmentManager().beginTransaction().replace(R.id.main_activity, f).commit();
				break;
			case 1:
				f = new ItemListFragment();
				b = new Bundle();
				b.putBoolean("favorites", true);
				f.setArguments(b);
				getFragmentManager().beginTransaction().replace(R.id.main_activity, f).commit();
				break;
			case 2:
				f = new ItemListFragment();
				b = new Bundle();
				b.putBoolean("all", true);
				f.setArguments(b);
				getFragmentManager().beginTransaction().replace(R.id.main_activity, f).commit();
				break;

		}
		return true;
	}


	public void onDone() {
		Fragment f = getFragmentManager().findFragmentById(id);
		FragmentTransaction fm = getFragmentManager().beginTransaction();
		Log.d("Stephen", "Here");
		fm.detach(f);
		fm.attach(f);
		fm.commit();

	}

	@Override
	public void onDone(int id) {
		Fragment f = getFragmentManager().findFragmentById(id);
		FragmentTransaction fm = getFragmentManager().beginTransaction();
		Log.d("Stephen", "Here");
		fm.detach(f);
		fm.attach(f);
		fm.commit();

	}


//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//	   // handle item selection
//	   switch (item.getItemId()) {
//	      case R.id.new_feed:
//	         // do s.th.
//
//	         return true;
//	      default:
//	         return super.onOptionsItemSelected(item);
//
//	   }
//
//	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
		Log.d("Stephen", "back to this fragment");
		onDone(id);
	}
	
	public void goActivity(String s){
		Intent intent = new Intent(this, SecondActivity.class);
		intent.putExtra("feed", s);
		int result  = 1;
		startActivityForResult(intent, result);
	}

	public void deleteOld() {
		DatabaseHandler handler = new DatabaseHandler(this, DHH.dataBaseName);
		Long age = (long) 604800000;
		handler.deleteOldItems(age, false);
		onDone(id);
	}
	
	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) 
	    {
	        super.finish();
	        return true;
	    }
	    return super.onKeyLongPress(keyCode, event);
	}

}