package com.sem2458.RSSPlus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class Alarm extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SiteCheckAlarm");
		wl.acquire();
		Log.d("Stephen", "Here");
		siteCheckThread r = new siteCheckThread(context, null, null, true, false);
		Thread t = new Thread(r);
		t.start();
		
		while(t.isAlive()){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		wl.release();
	}
	
	public void startAlarmManager(Context context){
		
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0,intent,0);
		int interval = 5;
		am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+100, interval *60000, pi);
		Log.d("Stephen","starting now");
	}


	public void stopAlarmManager(Context context) {
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, Alarm.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0,intent,0);
		am.cancel(pi);
		Log.d("Stephen","ending now");
	}
}
