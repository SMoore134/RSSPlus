package com.sem2458.RSSPlus;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class NewFeedDialogFragment extends DialogFragment{
	View view;
	public NewFeedDialogFragment(){
		
	}
	onSelectedListener callback;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		view = inflater.inflate(R.layout.new_feed_dialogfrag, container);
		EditText editText = (EditText) view.findViewById(R.id.feed_link_entry);
		getDialog().setTitle("Add a New Feed");
		editText.requestFocus();
		Button button = (Button) view.findViewById(R.id.done_add_feed);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DatabaseHandler handler = new DatabaseHandler(getActivity().getApplicationContext(), DHH.dataBaseName);
				ContentValues values = new ContentValues();
				EditText feedText = (EditText) view.findViewById(R.id.feed_link_entry);
				if(feedText==null)
					Log.d("Stephen","feedtext is null");
				EditText feedNameText = (EditText) view.findViewById(R.id.feed_name_entry);
				if(feedNameText==null)
					Log.d("Stephen","feednametext is null");
				if(!feedText.getText().toString().isEmpty() && !feedNameText.getText().toString().isEmpty()){
					values.put(DHH.keyFeedName, feedNameText.getText().toString());
					values.put(DHH.keyFeed, feedText.getText().toString());
					values.put(DHH.keyPubDate, "0");
					handler.addRow(false, values);
					exit();
				}
				
			}
		});
		return view;

	}
	
	@Override
    public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
		callback = (onSelectedListener)activity;
		}catch(Exception e){
			
		}
	}

	protected void exit() {
		callback.onDone();
		this.dismiss();
	}
}
