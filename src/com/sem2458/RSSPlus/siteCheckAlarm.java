package com.sem2458.RSSPlus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.sem2458.RSSPlus.siteCheckThread;;

public class siteCheckAlarm extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//check if database says its ok to start this alarm manager
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Alarm alarm = new Alarm();
            alarm.startAlarmManager(context);
        }
	}

	

}

