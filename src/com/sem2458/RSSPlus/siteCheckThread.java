package com.sem2458.RSSPlus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;



import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.text.format.Time;
import android.util.Log;

public class siteCheckThread implements Runnable{

	private static ArrayList<String> filenames;
	public Context context;
	public Activity activity;
	public Channel channel;
	public boolean checkMulti;
	public boolean updateUI;

	public siteCheckThread(Context context, Activity activity, Channel channel, boolean checkMulti, boolean updateUI){
		this.context = context;
		this.activity = activity;
		this.channel = channel;
		this.checkMulti = checkMulti;
	}
	@Override
	public void run() {
		
		Log.d("SiteCheck", "Check a site");
		try {
			downloadLinks();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(activity!=null){
			onSelectedListener callback = (onSelectedListener)activity;
			callback.onDone();
		}

	}
	
	public void downloadLinks() throws IOException{
		DatabaseHandler handler = new DatabaseHandler(context, DHH.dataBaseName);
		ArrayList<String> listString;
		if(!checkMulti){
			listString = new ArrayList<String>();
			listString.add(channel.link);
		}else{
			listString = handler.getDownloadLinks();
		}
		while(!listString.isEmpty()){
			String urlString = listString.get(0);
			if(!urlString.startsWith("http://"))
				urlString = "http://" + urlString;
			URL url = new URL(urlString);
			if(checkMulti){
				channel = handler.getFeedRow(listString.get(0), null);
			}
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = urlConnection.getInputStream();
			try {
				Parser(in, listString.get(0));
				
			} catch (XmlPullParserException e){
				e.printStackTrace();
			}
			urlConnection.disconnect();
			listString.remove(0);
		}
	}


	public void Parser(InputStream in, String link) throws XmlPullParserException, IOException{
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		Item item = null;
		String text = "";
		String tagname = "";
		boolean firstItem = true;
		DatabaseHandler handler = new DatabaseHandler(context, DHH.dataBaseName);
		try{
			factory = XmlPullParserFactory. newInstance();
			factory.setNamespaceAware(true);
			parser = factory.newPullParser();
			parser.setInput(in, null);

			int eventType = parser.getEventType();
			while(eventType != XmlPullParser.END_DOCUMENT){
				tagname = parser.getName();
				switch(eventType){
				case XmlPullParser.START_TAG:
					if(tagname.equalsIgnoreCase("item")){
						item = new Item();
						item.feed = channel.link;
						item.feedName = channel.title;
						Log.d("Stephen", "item");
						
					}
					if(tagname.equalsIgnoreCase("channel")){
						Log.d("Stephen", "channel");
					}
					break;
				case XmlPullParser.TEXT:
					text = parser.getText();
					break;
				case XmlPullParser.END_TAG:
					if(tagname.equalsIgnoreCase("item")){
						if(handler.checkIfInItemsTable(item)){
							return;
						}else{
							ContentValues values = new ContentValues();
							values.put(DHH.keyAuthor, item.author);
							values.put(DHH.keyDescription, item.description);
							values.put(DHH.keyFeed,item.feed);
							values.put(DHH.keyFeedName, item.feedName);
							values.put(DHH.keyLink, item.link);
							values.put(DHH.keyPubDate, item.pubDate);
							values.put(DHH.keyRead, "0");
							values.put(DHH.keyTitle, item.title);
							values.put(DHH.keyFavorite, "0");
							handler.addRow(true, values);
							if(firstItem){
							Time t = new Time(); 
							t.setToNow();
							channel.lastBuildDate = String.valueOf(t.toMillis(true));
							handler.updateFeedRow(false, channel);
							firstItem = false;
							}
						}
					}else if(tagname.equalsIgnoreCase("author")){
						if(item != null){
							item.author = text;
							Log.d("Stephen", "Author: "+text);
						}
					}else if(tagname.equalsIgnoreCase("category")){
						if(item != null){
							item.category = text;
							Log.d("Stephen", "Category: "+text);
						}
					}else if(tagname.equalsIgnoreCase("comments")){
						if(item != null){
							item.comments = text;
							Log.d("Stephen", "Comments: "+text);
						}
					}else if(tagname.equalsIgnoreCase("description")){
						if(item != null){
							item.description = text;
							Log.d("Stephen", "Description: "+text);
						}
					}else if(tagname.equalsIgnoreCase("enclosure")){
						if(item != null){
							item.enclosure = text;
							Log.d("Stephen", "Enclosure: "+text);
						}
					}else if(tagname.equalsIgnoreCase("guid")){
						if(item != null){
							item.guid = text;
							Log.d("Stephen", "Guid: "+text);
						}
					}else if(tagname.equalsIgnoreCase("link")){
						if(item != null){
							item.link = text;
							Log.d("Stephen", "Link: "+text);
						}
					}else if(tagname.equalsIgnoreCase("pubDate")){
						if(item != null){
							item.pubDate = DateHandler.dateToLong(text);
							Log.d("Stephen", "pubDate: "+text);
						}
					}else if(tagname.equalsIgnoreCase("source")){
						if(item != null){
							item.source = text;
							Log.d("Stephen", "Source: "+text);
						}
					}else if(tagname.equalsIgnoreCase("title")){
						if(item != null){
							item.title = text;
						}
						Log.d("Stephen", "Title: "+text);
					}
					else if(tagname.equalsIgnoreCase("lastBuildDate")){
						channel.lastBuildDate = DateHandler.dateToLong(text);
						Log.d("Stephen", "LastBuildDate: "+text + " "+ DateHandler.dateToLong(text));
					}
					break;

				default:
					break;
				}
				eventType = parser.next();

			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	
}