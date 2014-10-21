package com.mimic.accesrest.notifications;

import java.io.IOException;
import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.mimic.accesrest.Explore;
import com.mimic.accesrest.MainActivity;
import com.mimic.accesrest.R;
import com.mimic.accesrest.Typefacespan;
import com.mimic.accesrest.commentdata;
import com.mimic.accesrest.post;
import com.mimic.accesrest.profile;
import com.mimic.accesrest.R.id;
import com.mimic.accesrest.R.layout;
import com.nostra13.universalimageloader.core.ImageLoader;
//import com.fedorvlasov.lazylist.ImageLoader;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Notifications extends SherlockActivity implements OnClickListener{
	private ArrayList<notificationsdata> notifs;
	private ListView notiflist;
	private LayoutInflater layoutinflater;
	
	private notificationadapter notifadapter;
	private ImageButton playpostbutton;
	public MediaPlayer player;
	public ImageLoader imageloader;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifications);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		SpannableString s = new SpannableString("NOTIFICATIONS");
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		this.notiflist = (ListView) findViewById(R.id.notifview);
//		this.notiflist.setDivider(null);
		this.layoutinflater = LayoutInflater.from(this);
		ImageButton a = (ImageButton) findViewById(R.id.postbuttonnotifpage);
		a.setOnClickListener(this);
		ImageButton b = (ImageButton) findViewById(R.id.homebuttonnotifpage);
		b.setOnClickListener(this);
		ImageButton c = (ImageButton) findViewById(R.id.notificationbuttonnotifpage);
		c.setOnClickListener(this);
		ImageButton d = (ImageButton) findViewById(R.id.profilebuttonnotifpage);
		d.setOnClickListener(this);
		ImageButton explore = (ImageButton) findViewById(R.id.explorebuttonnotifpage);
		explore.setOnClickListener((OnClickListener) this);
		notification();
	}
	
	@Override
	public void onClick (View v){
	
	if (v.getId() == R.id.postbuttonnotifpage)
	{
		startActivity(new Intent(Notifications.this, post.class));
		
		
	}else if(v.getId() == R.id.homebuttonnotifpage){
		startActivity(new Intent(Notifications.this, MainActivity.class));
		this.finish();
	}
	else if(v.getId() == R.id.notificationbuttonnotifpage){
		startActivity(new Intent(Notifications.this, Notifications.class));
		this.finish();
	} else if (v.getId()== R.id.profilebuttonnotifpage){
		Intent x = new Intent(Notifications.this, profile.class);
		x.putExtra("profileurl", "http://mimictheapp.herokuapp.com/profiles/");
		startActivity(x);
		this.finish();
	} else if (v.getId() == R.id.explorebuttonnotifpage){
		startActivity(new Intent(Notifications.this, Explore.class));
		this.finish();
	}
}

	
	public void setUser(ArrayList<notificationsdata> notifs) {
		this.notifs = notifs;
		
		notifadapter = new notificationadapter(this,this.layoutinflater, this.notifs);
		this.notiflist.setAdapter(notifadapter);
	
		
	}
	
	  public void notification(){
			NotificationWebTask task = new NotificationWebTask(Notifications.this);
			try{
				task.execute();
				} catch (Exception e){
					task.cancel(true);
					alert("Nothing to play");
				}
			
		}
	  
	  
	  public void alert(String msg) {
			Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
			
		}
	  
	  public static class MyNotifHolder{
		  public notificationsdata notifdata;
		  public ImageButton postplay;
		  public commentdata comments;
		  public String posturl, playurl, profileurl;
		  public ImageView dp;
		  public TextView time, description, desc, post;
		  public int postid;

		  
	  }

	
	


}
