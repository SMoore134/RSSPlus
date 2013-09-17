package com.sem2458.RSSPlus;





import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SecondActivity  extends Activity implements onSelectedListener{
	Intent sender;
	int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sender = getIntent();
		String feed = (String) sender.getExtras().get("feed");
		setContentView(R.layout.activity_main);
		Fragment f = new ItemListFragment();
		Bundle args = new Bundle();
		args.putString("feedName", feed);
		f.setArguments(args);
		getFragmentManager().beginTransaction().add(R.id.main_activity, f).commit();
		setResult(1,sender);
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
	
	@Override
	public void onBackPressed(){
		Log.d("Stephen", "Back was pressed");
		Intent data = new Intent();
		if (getParent() == null) {
		    setResult(Activity.RESULT_OK, data);
		} else {
		    getParent().setResult(Activity.RESULT_OK, data);
		}
		finish();
	}

	
}
