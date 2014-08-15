package com.mimic.accesrest;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import com.mimic.accesrest.notifications.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ParseReceiver extends BroadcastReceiver{
private final String LogTag = "Parse Notification";
private String msg = "";
private int count = 1;
private int i=1;
	
	
@Override
public void onReceive(Context context, Intent intent) {
	String msgs = null;
	Log.d("working?", "I think so");
			try {
		    String action = intent.getAction();
		    String channel = intent.getExtras().getString("com.parse.Channel");
		    JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
		
		    Log.d(LogTag, "got action " + action + " on channel " + channel + " with:");
		    Iterator itr = json.keys();
		    while (itr.hasNext()) {
		      String key = (String) itr.next();
		      Log.d(LogTag, "..." + key + " => " + json.getString(key));
		      if(key.equals("msg")){
		    	  msgs = json.getString(key);
		      }
		    }
		  } catch (JSONException e) {
		    Log.d(LogTag, "JSONException: " + e.getMessage());
		  }
		  
		  
		  	Bitmap icon = BitmapFactory.decodeResource(context.getResources(), 	R.drawable.notificon);
		    Intent launch = new Intent(context, Notifications.class);
		    PendingIntent pi = 	PendingIntent.getActivity(context, 0, launch, 	0);

		  	
		    
		  	Notification notif = new NotificationCompat.Builder(context)
		  	.setContentTitle("Mimic")
		  	.setContentText(msgs)
		  	.setSmallIcon(R.drawable.notificon)
		  	.setLargeIcon(icon)
		  	.setContentIntent(pi)
		  	.setAutoCancel(true)
		  	
		  	
		  	
		  	.build();
		  	

		  	notif.defaults |= Notification.DEFAULT_SOUND;
		  	notif.defaults |= Notification.DEFAULT_VIBRATE;
		  	
		  	NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		  	nm.notify(1, notif);
	}

}
