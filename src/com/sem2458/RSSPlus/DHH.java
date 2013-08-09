package com.sem2458.RSSPlus;

public class DHH {
	
	public static String itemTable = "ItemTable";
	public static String feedsTable = "FeedsTable";
	public static String settingsTable ="SettingsTable";
	public static String keyAuthor = "author";
	public static String keyDescription = "description";
	public static String keyPubDate = "pubdate";
	public static String keyTitle = "title";
	public static String keyFeed = "feed";
	public static String keyFeedName = "feedName";
	public static String keyLink = "link";
	public static String keyRead = "read";//0 false 1 true
	public static String dataBaseName = "RSSDB";
	public static String keyFavorite = "Favorite";
	public static String keyId = "_id";
	private static Object keyInterval;
	private static Object keyBuiltInWeb; 
	
	
	//true is for tableId = ItemTable false is for feedsTable
	public static String createItemTableString(){
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
			sb.append(itemTable).append("(");
			sb.append(keyTitle).append(" TEXT, ");
			sb.append(keyAuthor).append(" TEXT, ");
			sb.append(keyPubDate).append(" TEXT, ");
			sb.append(keyDescription).append(" TEXT, ");
			sb.append(keyFeedName).append(" TEXT, ");
			sb.append(keyLink).append(" TEXT, ");
			sb.append(keyRead).append(" INTEGER, ");
			sb.append(keyFeed).append(" TEXT, ");
			sb.append(keyFavorite).append(" TEXT,");
			sb.append(keyId).append(" INTEGER PRIMARY KEY AUTOINCREMENT)");
		return sb.toString();
	}

	public static String createFeedsTableString(){
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(feedsTable).append("(");
		sb.append(keyFeedName).append(" TEXT, ");
		sb.append(keyFeed).append(" TEXT, ");
		sb.append(keyPubDate).append(" TEXT, ");
		sb.append(keyId).append(" INTEGER PRIMARY KEY AUTOINCREMENT)");
		return sb.toString();
	}
	public static String createSettingsTableString(){
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(settingsTable).append("(");
		sb.append(keyInterval).append(" INTEGER PRIMARY KEY, ");
		sb.append(keyBuiltInWeb).append(" INTEGER)");
		return sb.toString();
	}
}
