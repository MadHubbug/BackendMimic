package com.mimic.accesrest;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.mimic.accesrest.notifications.Notifications;
//import com.fedorvlasov.lazylist.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pkmmte.circularimageview.CircularImageView;

public class profile extends SherlockActivity implements OnClickListener{

	private ArrayList<MimicData> mimic;
	private ArrayList<profiledata> profiledatas;
	private ListView MimicList;
	private LayoutInflater layoutinflater;
	private TextView fullnameprof;
	private TextView Usernameprof;
	private TextView followers;
	private TextView followings, description;
	private Typeface type;
	public ImageLoader imageloader;
	private CircularImageView display;
	private TextView mimicscount;
	private boolean follows, y;
	private ImageButton follow,btn, profbutton;
	private static final String logtaskact = "MainUI";
	private String postid, user, actualuser, username, x;
	private Bundle bundle;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		imageloader = ImageLoader.getInstance();
		this.layoutinflater = LayoutInflater.from(this);
		 Uri uri = getIntent().getData();
		 if (uri != null){
		 String tag = uri.toString().split("/")[3];
		 x = tag.substring(1);
		 } 
		 else{
			 bundle = getIntent().getExtras();
				 x = bundle.getString("profileurl");
				Log.d("profileurl", x);	
				y = bundle.getBoolean("prof");
			 }

			getSupportActionBar().setDisplayShowHomeEnabled(false);
		
		profbutton = (ImageButton) findViewById(R.id.profilebuttonprofpage);
		profbutton.setOnClickListener(this);
		description = (TextView) findViewById(R.id.postclickable);
		type = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Regular.ttf");
		btn = (ImageButton) findViewById(R.id.editprof);
		description.setTypeface(type);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		user = prefs.getString("profileid", "0");
		actualuser = prefs.getString("username", "madfresco");
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		follow = (ImageButton) findViewById(R.id.followprofs);
		fullnameprof = (TextView) findViewById(R.id.fullname);
		fullnameprof.setTypeface(type);

		follow.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				final follow tofollow = new follow();
				final unfollow tounfollow = new unfollow();	
				if (follows == true){
					
					follows=false;
					tounfollow.execute(postid, user);
					follow.setImageResource(R.drawable.followprof);			
				}else if (follows== false){
					
					tofollow.execute(postid, user,username, actualuser);
					follows = true;
					follow.setImageResource(R.drawable.followingprof);
				}
					
			
			}
			
		});

		this.followers = (TextView) findViewById(R.id.followertext);
		this.followings = (TextView) findViewById(R.id.followingtext);
		this.MimicList = (ListView) findViewById(R.id.proflist);
		ImageButton homebutton = (ImageButton) findViewById(R.id.homebuttonprofpage);
		homebutton.setOnClickListener(this);
		ImageButton postbutton = (ImageButton) findViewById(R.id.postbuttonprofpage);
		postbutton.setOnClickListener(this);
		ImageButton notification = (ImageButton) findViewById(R.id.notificationbuttonprofpage);
		notification.setOnClickListener(this);
		ImageButton explore = (ImageButton) findViewById(R.id.explorebuttonprofpage);
		explore.setOnClickListener((OnClickListener) this);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		display = (CircularImageView) findViewById(R.id.profilepic);
		mimicscount = (TextView) findViewById(R.id.postcounts);
		
		LinearLayout following = (LinearLayout) findViewById(R.id.followinglayout);
		following.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent x = new Intent(profile.this, followinglist.class);
				x.putExtra("label", "Follower");
				x.putExtra("postids", postid);
				startActivity(x);
				
				
			}
		});
		
		
		
		btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(profile.this, settings.class));
			}
			
			
			
		});
		
		LinearLayout follower = (LinearLayout) findViewById(R.id.followerslayout);
		
		follower.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent x = new Intent(profile.this, followinglist.class);
				x.putExtra("label", "Following");
				x.putExtra("postids", postid);
				startActivity(x);
				
				
			}
		});
		mimic(x);
		
	}
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		
//		 MenuInflater inflater = getSupportMenuInflater();
//		    inflater.inflate(R.menu.prof, menu);
//		    return super.onCreateOptionsMenu(menu);
//	}
	@Override
	public void onClick (View v){
	
	if (v.getId() == R.id.postbuttonprofpage)
	{
		startActivity(new Intent(profile.this, post.class));;
	
	}else if(v.getId() == R.id.homebuttonprofpage){
		startActivity(new Intent(profile.this, MainActivity.class));
		this.finish();
	}
	else if(v.getId() == R.id.notificationbuttonprofpage){
		Intent x = new Intent(profile.this, Notifications.class);
		startActivity(x);
		this.finish();
	} else if (v.getId()== R.id.profilebuttonprofpage){
		Intent x = new Intent(profile.this, profile.class);
		x.putExtra("profileurl", "http://mimictheapp.herokuapp.com/profiles/");
		startActivity(x);
		this.finish();
	}else if (v.getId()==R.id.explorebuttonprofpage){
		startActivity(new Intent(profile.this, Explore.class));
		this.finish();
	}
}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Log.d("did you", "click?");
        	this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public void mimic(String x){
		String query;
		profilewebtask proftask = new profilewebtask(profile.this);
		if (x.length()<20){
			query = "http://mimictheapp.herokuapp.com/profilesearch/?search="+x+"&format=json";
		}
		else{ query = x+"?format=json";
		}
		
		try{
			proftask.execute(query);
			} catch (Exception e){
				proftask.cancel(true);
				alert("No Mimics");
			}
		
	}
	
	public void alert(String msg) {
		Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
		
	}


	public void setUser(ArrayList<MimicData> mimic) {
		this.mimic = mimic;
		this.MimicList.setAdapter(new profileadapter(this,this.layoutinflater, this.mimic));
	}

	public void setUsers(ArrayList<profiledata> profiledata) {
		this.profiledatas = profiledata;
		int q = profiledatas.size();
		if (q>0){
		profiledata x = profiledatas.get(0);
//		imageloader=new ImageLoader(this);
		username = x.getUsername();
		imageloader.displayImage(x.getprofileurl(), display);
		SpannableString s = new SpannableString(x.getUsername().toUpperCase());
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowTitleEnabled(true);
		description.setText(x.getdescription());
		mimicscount.setText(x.getpostcount());
		followers.setText(x.getfollowers());
		followings.setText(x.getfollowing());
		follows = x.getfollows();
		fullnameprof.setText(x.getfullname());
		if (follows == true){
			follow.setImageResource(R.drawable.followingprof);
		}
		postid = x.getpostid();
		boolean own = x.getowner();
		if (own == true){
		btn.setVisibility(View.VISIBLE);
		follow.setVisibility(View.GONE);
	
		}else{
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setIcon(R.drawable.back);

			profbutton.setImageResource(R.drawable.profilebuttonunclicked);
			btn.setVisibility(View.GONE);
			LinearLayout ll = (LinearLayout) follow.getParent();
			int color = getResources().getColor(R.color.dividercolor);
			ll.setBackgroundColor(color);
			follow.setVisibility(View.VISIBLE);
		}
			
		}
		
	}
	  public static class MyViewHolder {
		     public TextView user, description, echonum, commentnum, likesnum, duration, timestamp;
		        public MimicData mimic;
		        public ImageButton like, play, share, reply, echo;
		        public String posturl, profileurl, username;
		        public SeekBar sb;
		        public ImageView dp;
		        public int postid, likecount;
		        public Boolean liked, own;

	    }
}
