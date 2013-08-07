package com.sem2458.RSSPlus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sem2458.RSSPlus.DatabaseHandlerHelper;;

public class DatabaseHandler extends SQLiteOpenHelper{



	
	public DatabaseHandler(Context context, String name) {
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DatabaseHandlerHelper.createTableString(true));
		db.execSQL(DatabaseHandlerHelper.createTableString(false));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void addRow(boolean table, ContentValues values ){
		Log.d("Stephen","addrow");
		try{SQLiteDatabase db = this.getWritableDatabase();
		if(table==true){
			db.insert(DatabaseHandlerHelper.itemTable, null, values);
			db.close();
		}else{
			db.insert(DatabaseHandlerHelper.feedsTable, null, values);
			db.close();
		}
		}catch(Exception e){

		}

	}

	public ArrayList<Object> getAllItems(String feed) {
		Log.d("Stephen","getallitems");
		ArrayList<Object> ItemList = new ArrayList<Object>();
		// Select All Query
		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("SELECT * FROM ").append(DatabaseHandlerHelper.itemTable);
		if(!feed.equals(null))	
			selectQuery.append(" WHERE "+ DatabaseHandlerHelper.keyFeedName+" = ").append("'"+feed+"'").append(" ORDER BY "+DatabaseHandlerHelper.keyPubDate+" DESC");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery.toString(), null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Item i = new Item();
				i.title = cursor.getString(0);
				i.author = cursor.getString(1);
				i.pubDate = cursor.getString(2);
				i.description = cursor.getString(3);
				i.feedName = cursor.getString(4);
				i.link = cursor.getString(5);
				i.read = cursor.getString(6);
				i.feed = cursor.getString(7);
				i.favorite = cursor.getString(8);
				i.id = cursor.getInt(9);
				ItemList.add(i);
			} while (cursor.moveToNext());
		}
		db.close();
		return ItemList;
	}

	public ArrayList<Object> getAllFeeds() {
		Log.d("Stephen","getallfeeds");
		ArrayList<Object> ChannelList = new ArrayList<Object>();
		// Select All Query

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("SELECT * FROM ").append(DatabaseHandlerHelper.feedsTable);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery.toString(), null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Channel channel = new Channel();
				channel.title = cursor.getString(0);
				channel.link = cursor.getString(1);
				channel.lastBuildDate = cursor.getString(2);
				channel.id = cursor.getInt(3);
				// Adding contact to list
				ChannelList.add(channel);
			} while (cursor.moveToNext());
		}
		db.close();
		// return contact list
		return ChannelList;
	}

	public void deleteFeedRow(boolean table, String title, int id){
		Log.d("Stephen","deletefeedrow");
		try{SQLiteDatabase db=this.getWritableDatabase();
		Log.d("Stephen", "title:"+title+"!!!!!!!!");
		if(!table){
			db.execSQL("DELETE FROM " + DatabaseHandlerHelper.feedsTable + " WHERE feedName" + " = '"+title+"'");
			db.execSQL("DELETE FROM " + DatabaseHandlerHelper.itemTable + " WHERE feedName" + " = '"+title+"'");
		}else if(table){
			db.execSQL("DELETE FROM " + DatabaseHandlerHelper.itemTable + " WHERE _id" + " = "+id);
		}
		db.close();
		//db.delete(DatabaseHandlerHelper.feedsTable, null, null);
		//db.delete(DatabaseHandlerHelper.itemTable, DatabaseHandlerHelper.keyFeedName+" = "+title, null);
		}catch(Exception e){

		}
	}

	public void updateFeedRow(boolean table, Channel channel){
		Log.d("Stephen","updatefeedrow");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("feed", channel.link);
		values.put("feedName", channel.title);
		values.put("pubdate", channel.lastBuildDate);
		values.put("_id", channel.id);
		db.update(DatabaseHandlerHelper.feedsTable, values, "_id="+channel.id, null);
		db.close();
	}
	
	public void updateItemRow(Item i) {
		Log.d("Stephen","updateitemrow");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("title", i.title);
		values.put("author", i.author);
		values.put("pubdate", i.pubDate);
		values.put("description", i.description);
		values.put("feedName", i.feedName);
		values.put("link", i.link);
		values.put("read", i.read);
		values.put("feed", i.feed);
		values.put("Favorite", i.favorite);
		values.put("_id",i.id);
		db.update(DatabaseHandlerHelper.itemTable, values, "_id="+i.id,null);
		db.close();
	}

	public ArrayList<String> getDownloadLinks() {
		Log.d("Stephen","getDownloadlinks");
		String query = "SELECT feed FROM " +DatabaseHandlerHelper.feedsTable;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query,null);
		ArrayList<String> links = new ArrayList<String>();
		if(cursor.moveToFirst()){
		do{
			links.add(cursor.getString(0));
		}while(cursor.moveToNext());
		}
		db.close();
		cursor.close();
		return links;
	}

	public String getLastBuildDate(String title) {
		
		Log.d("Stephen","getlastbuilddate");
		String query = "Select pubdate FROM " +DatabaseHandlerHelper.feedsTable +" WHERE feedName = '" +title+"'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query,null);
		cursor.moveToFirst();
		Log.d("Stephen",String.valueOf(cursor.getCount()));
		String result = cursor.getString(0);
		db.close();
		cursor.close();
		return result;
	}

	public Channel getFeedRow(String feed, String feedName) {
		Log.d("Stephen","getfeedRow");
		String query;
		if(feed!=null)
			query = "Select * FROM " +DatabaseHandlerHelper.feedsTable +" WHERE feed = '" + feed +"'";
		else
			query = "Select * FROM " +DatabaseHandlerHelper.feedsTable +" WHERE feedName = '" + feedName +"'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query,null);
		cursor.moveToFirst();
		Channel c = new Channel();
		c.title = cursor.getString(0);
		c.link = cursor.getString(1);
		c.lastBuildDate = cursor.getString(2);
		c.id = cursor.getInt(3);
		db.close();
		cursor.close();
		return c;
	}

	public Item getItemRow(int id) {
		Log.d("Stephen","getitemrow");
		String query = "Select * FROM " +DatabaseHandlerHelper.itemTable +" WHERE "+DatabaseHandlerHelper.keyId + " = " + id;
		SQLiteDatabase db = this.getReadableDatabase();
		
		Cursor cursor = db.rawQuery(query,null);
		cursor.moveToFirst();
		Item i = new Item();
		i.title = cursor.getString(0);
		i.author = cursor.getString(1);
		i.pubDate = cursor.getString(2);
		i.description = cursor.getString(3);
		i.feedName = cursor.getString(4);
		i.link = cursor.getString(5);
		i.read = cursor.getString(6);
		i.feed = cursor.getString(7);
		i.favorite = cursor.getString(8);
		i.id = cursor.getInt(9);
		db.close();
		cursor.close();
		return i;
	}

	public ArrayList<Object> getFavorites() {
		Log.d("Stephen","getfavorites");
		String query = "Select * FROM " +DatabaseHandlerHelper.itemTable +" WHERE "+DatabaseHandlerHelper.keyFavorite + " = '1'" + " ORDER BY "+DatabaseHandlerHelper.keyPubDate+" DESC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query,null);
		cursor.moveToFirst();
		ArrayList<Object> a = new ArrayList<Object>();
		if(cursor.getCount()>0){
			do{
				Item i = new Item();
				i.title = cursor.getString(0);
				i.author = cursor.getString(1);
				i.pubDate = cursor.getString(2);
				i.description = cursor.getString(3);
				i.feedName = cursor.getString(4);
				i.link = cursor.getString(5);
				i.read = cursor.getString(6);
				i.feed = cursor.getString(7);
				i.favorite = cursor.getString(8);
				i.id = cursor.getInt(9);
				a.add(i);
			}while(cursor.moveToNext());
		}
		db.close();
		cursor.close();
		return a;
	}

	public ArrayList<Object> getAll() {
		Log.d("Stephen","getall");
		String query = "Select * FROM " +DatabaseHandlerHelper.itemTable + " ORDER BY "+DatabaseHandlerHelper.keyId+" ASC";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query,null);
		cursor.moveToFirst();
		ArrayList<Object> a = new ArrayList<Object>();
		if(cursor.getCount()>0){
			do{
				Item i = new Item();
				i.title = cursor.getString(0);
				i.author = cursor.getString(1);
				i.pubDate = cursor.getString(2);
				i.description = cursor.getString(3);
				i.feedName = cursor.getString(4);
				i.link = cursor.getString(5);
				i.read = cursor.getString(6);
				i.feed = cursor.getString(7);
				i.favorite = cursor.getString(8);
				i.id = cursor.getInt(9);
				a.add(i);
			}while(cursor.moveToNext());
		}
		db.close();
		cursor.close();
		return a;

	}

	public String getUnread(String title) {
		String query = "Select * FROM " +DatabaseHandlerHelper.itemTable + " WHERE "+DatabaseHandlerHelper.keyFeedName+" = \""+title+"\" AND read = "+"0";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query,null);
		int count = cursor.getCount();
		db.close();
		cursor.close();
		return String.valueOf(count);
		
	}

	

}
