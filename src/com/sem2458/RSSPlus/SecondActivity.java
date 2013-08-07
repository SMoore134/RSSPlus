package com.sem2458.RSSPlus;



import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SecondActivity  extends Activity implements onSelectedListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent sender = getIntent();
		String feed = (String) sender.getExtras().get("feed");
		setContentView(R.layout.activity_main);
		Fragment f = new ItemListFragment();
		Bundle args = new Bundle();
		args.putString("feedName", feed);
		f.setArguments(args);
		getFragmentManager().beginTransaction().add(R.id.main_activity, f).commit();
	}

	public void onDone(int id) {
		Fragment f = getFragmentManager().findFragmentById(id);
		FragmentTransaction fm = getFragmentManager().beginTransaction();
		Log.d("Stephen", "Here");
		fm.detach(f);
		fm.attach(f);
		fm.commit();
		
	}

	@Override
	public void onDone() {
		// TODO Auto-generated method stub
		
	}

	
}
