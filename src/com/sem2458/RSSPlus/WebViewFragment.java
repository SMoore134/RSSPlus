package com.sem2458.RSSPlus;

import com.sem2458.RSSPlus.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebViewFragment extends Fragment {
	
	WebView webView;
	boolean isData;
	String data;
	String link;
	String title;
	private String androidUserAgent = "";
	private String desktopUserAgent = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Bundle b = this.getArguments();
		isData = b.getBoolean("isData");
		data = b.getString("Data");
		title = b.getString("Title");
		link = b.getString("Link");
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.webview_fragment, container, false);
		getActivity().getActionBar().hide();
		desktopUserAgent = getString(R.string.user_agent_string);
		webView = (WebView)v.findViewById(R.id.webView1);
		TextView textview = (TextView)v.findViewById(R.id.webviewtitle);
		textview.setText(title);
		initializeWebView();
		setWebView(false);
		return v;
		
	}

	public void initializeWebView(){
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		//webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setLoadWithOverviewMode(true);
	    webView.getSettings().setUseWideViewPort(true);
	    if(isData){
	    	webView.setBackgroundColor(Color.BLACK);
	    	compileData();
	    }
	}
	
	private void compileData() {
		
		data = (String) this.getText(R.string.html)
				+this.getText(R.string.head)
				+this.getText(R.string.meta)
				+this.getText(R.string.style)
				+this.getText(R.string.style_img)
				+this.getText(R.string.style_p)
				+this.getText(R.string.style_end)
				+this.getText(R.string.head_end)
				+data.replaceAll(" class=\".*?\"","")
				+"<br><br>"
				+"<a href="+link+">Read More</a>"
				+this.getText(R.string.html_end);
		Log.d("Stephen", data);
	}
	
	public void setWebView(boolean use){ 
		if(!use){
			
			if(androidUserAgent.isEmpty()){
				androidUserAgent = webView.getSettings().getUserAgentString();
			}else{
				webView.getSettings().setUserAgentString(androidUserAgent);
				//Toast.makeText(getActivity().getApplicationContext(), "Changing to Android View", Toast.LENGTH_SHORT).show();
			}
		}if(use){
			webView.getSettings().setUserAgentString(desktopUserAgent);
			//Toast.makeText(getActivity().getApplicationContext(), "Changing to Desktop View", Toast.LENGTH_SHORT).show();
		}
		if(!isData)
			webView.loadUrl(data);
		else{
			webView.loadData(data,"text/html",null);
		}
	}
	
}
