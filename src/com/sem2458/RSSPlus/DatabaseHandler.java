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

import com.sem2458.RSSPlus.DHH;;

public class DatabaseHandler extends SQLiteOpenHelper{



	//true is for tableId = ItemLists false is for feedLists
	public DatabaseHandler(Context context, String name) {
		super(context, name, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(DHH.createFeedsTableString());
		db.execSQL(DHH.createItemTableString());
		db.execSQL(DHH.createSettingsTableString());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void addRow(boolean table, ContentValues values ){
		Log.d("Stephen","addrow");
		try{SQLiteDatabase db = this.getWritableDatabase();
		if(table==true){
			db.insert(DHH.itemTable, null, values);
			db.close();
		}else{
			db.insert(DHH.feedsTable, null, values);
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
		selectQuery.append("SELECT * FROM ").append(DHH.itemTable);
		if(!feed.equals(null))	
			selectQuery.append(" WHERE "+ DHH.keyFeedName+" = ").append("'"+feed+"'").append(" ORDER BY "+DHH.keyPubDate+" DESC");
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
		selectQuery.append("SELECT * FROM ").append(DHH.feedsTable);
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
			db.execSQL("DELETE FROM " + DHH.feedsTable + " WHERE feedName" + " = '"+title+"'");
			db.execSQL("DELETE FROM " + DHH.itemTable + " WHERE feedName" + " = '"+title+"'");
		}else if(table){
			db.execSQL("DELETE FROM " + DHH.itemTable + " WHERE _id" + " = "+id);
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
		db.update(DHH.feedsTable, values, "_id="+channel.id, null);
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
		db.update(DHH.itemTable, values, "_id="+i.id,null);
		db.close();
	}

	public ArrayList<String> getDownloadLinks() {
		Log.d("Stephen","getDownloadlinks");
		String query = "SELECT feed FROM " +DHH.feedsTable;
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
		String query = "Select pubdate FROM " +DHH.feedsTable +" WHERE feedName = '" +title+"'";
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
			query = "Select * FROM " +DHH.feedsTable +" WHERE feed = '" + feed +"'";
		else
			query = "Select * FROM " +DHH.feedsTable +" WHERE feedName = '" + feedName +"'";
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
		String query = "Select * FROM " +DHH.itemTable +" WHERE "+DHH.keyId + " = " + id;
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
		String query = "Select * FROM " +DHH.itemTable +" WHERE "+DHH.keyFavorite + " = '1'" + " ORDER BY "+DHH.keyPubDate+" DESC";
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
		String query = "Select * FROM " +DHH.itemTable + " ORDER BY "+DHH.keyId+" ASC";
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

	public int getUnread(String title) {
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(DHH.itemTable, new String[]{DHH.keyTitle}, DHH.keyRead+"=? AND "+DHH.keyFeedName+"=?", new String[]{"0",title}, "", "", "");
		int count = cursor.getCount();
		db.close();
		cursor.close();
		return count;
	}

	public boolean checkIfInItemsTable(Item item) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(DHH.itemTable, new String[]{DHH.keyTitle},DHH.keyTitle+"=? AND " + DHH.keyFeedName+"=? AND " + DHH.keyPubDate+"=?", new String[]{item.title, item.feedName, item.pubDate}, "", "", "");
		int count = cursor.getCount();
		Log.d("Stephen", item.title+" "+count);
		db.close();
		cursor.close();
		return count>0;
	}

	public void deleteOldItems(Long age, boolean deleteFaves) {
		SQLiteDatabase db = this.getWritableDatabase();
		Log.d("Stephen","deleting old");
		if(!deleteFaves)
			db.delete(DHH.itemTable, DHH.keyPubDate+"<? AND " + DHH.keyFavorite+"=?", new String[]{String.valueOf(System.currentTimeMillis()-age),"0"});
		else
			db.delete(DHH.itemTable, DHH.keyPubDate+"<?", new String[]{String.valueOf(System.currentTimeMillis()-age)});
		db.close();
	}

	

}
