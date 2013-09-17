package com.sem2458.RSSPlus;





import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class SecondActivity  extends Activity implements onSelectedListener{
	Intent sender;
	public static int id;
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
		getFragmentManager().beginTransaction().add(R.id.main_activity, f, "itemList").commit();
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
		if(id==1){
		Intent data = new Intent();
		if (getParent() == null) {
		    setResult(Activity.RESULT_OK, data);
		} else {
		    getParent().setResult(Activity.RESULT_OK, data);
		}
		finish();
		}else if(id==2){
			WebViewFragment frag = (WebViewFragment) this.getFragmentManager().findFragmentByTag("web");
			if(frag.webView.canGoBack())
				frag.webView.goBack();
			else{
				SecondActivity.id = 1;
				super.onBackPressed();
			}
		}
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
