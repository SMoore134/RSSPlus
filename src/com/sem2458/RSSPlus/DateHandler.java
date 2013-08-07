package com.sem2458.RSSPlus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHandler {
	public static String dateToLong(String input){
		Date date = null;
		try {
			date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZ", Locale.ENGLISH).parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(date!=null){
			return Long.toString(date.getTime());
		}
		return "";
	}
	
	public static String LongToDate(String input){
		SimpleDateFormat f = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZ", Locale.ENGLISH);
		Date d = new Date();
		d.setTime(Long.parseLong(input));
		return f.format(d);
	}
}
