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
import android.util.Log;

public class siteCheckThread implements Runnable{

	private static ArrayList<String> filenames;
	public Context context;
	public Activity activity;
	public Channel channel;
	public boolean checkMulti;
	public boolean updateUI;
	List<Item> items;

	public siteCheckThread(Context context, Activity activity, Channel channel, boolean checkMulti, boolean updateUI){
		this.context = context;
		this.activity = activity;
		this.channel = channel;
		this.checkMulti = checkMulti;
		this.items = new ArrayList<Item>();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
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
		for(Item item: items){
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
			
		}
	}


	private static void download(String string, String name) throws IOException {
		// TODO Auto-generated method stub
		URL url = new URL(string);
		byte[] b = new byte[1024];
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		InputStream in;


		File f = new File("");

		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f.getPath());
		try {
			in = url.openStream();
			int count = 0;
			while((count = in.read(b, 0, 1024))!=-1){
				fos.write(b, 0, count);
			}
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			urlConnection.disconnect();
		}

	}

	private static ArrayList<String> search(String string) {
		// TODO Auto-generated method stub
		Pattern p = Pattern.compile("");
		Matcher m = p.matcher(string);
		ArrayList<String> x = new ArrayList<String>();
		filenames = new ArrayList<String>();
		while(m.find()){
			x.add(m.group(1));
			filenames.add(m.group(2).replaceAll("%20", " "));
		}
		return x;
	}

	public void Parser(InputStream in, String link) throws XmlPullParserException, IOException{
		
		Item item = null;
		Log.d("Stephen", channel.title);
		String text = "";
		String tagname = "";
		XmlPullParserFactory factory = null;
		XmlPullParser parser = null;
		String lastBuildDate = channel.lastBuildDate;
		Log.d("Stephen", "first lastbuilddate: "+channel.lastBuildDate);
		boolean isChannel = true;
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
						//update database to have new lastbuildDate or skip checking
						if(isChannel){
							String temp = channel.lastBuildDate;
							Log.d("Stephen", "second lastbuilddate: "+channel.lastBuildDate);
							if(Long.parseLong(temp)>Long.parseLong(lastBuildDate)){
								channel.lastBuildDate = temp;
								handler.updateFeedRow(false, channel);
							}else{
								Log.d("Stephen", "size of items: "+items.size());
								return;
							}
							isChannel = false;
						}
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
						items.add(item);
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
							if(Long.parseLong(lastBuildDate)>Long.parseLong(item.pubDate)){
								return;
							}
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

	public static String replace(String source, String regex, String replacement){
		return source.replaceAll(regex, replacement);

	}

}

