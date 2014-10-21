package com.mimic.accesrest.notifications;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mimic.accesrest.R;
import com.mimic.accesrest.comment;
import com.mimic.accesrest.profile;
//import com.fedorvlasov.lazylist.ImageLoader;
import com.mimic.accesrest.R.drawable;
import com.mimic.accesrest.R.id;
import com.mimic.accesrest.R.layout;
import com.mimic.accesrest.notifications.Notifications.MyNotifHolder;
import com.mimic.accesrest.search.MyHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

public class notificationadapter extends BaseAdapter{
	private Activity activity;
	private LayoutInflater layoutinflater;
	private ArrayList<notificationsdata> notifsdata;
	private notificationsdata notifdata;
	private ImageLoader imageloader;
	int position=-1;
	private static int not_playing = -1;
	private final boolean[] mHighlightedPositions = new boolean[100];
	int initialposition = -1; 
	private int mPlayingPosition = not_playing;
	private String profileurl;
	public MediaPlayer player = new MediaPlayer();
	public Typeface type;
	
	public notificationadapter(Activity a, LayoutInflater l, ArrayList <notificationsdata> m){
		
		this.activity = a;
		this.layoutinflater = l;
		this.notifsdata = m;
		imageloader = ImageLoader.getInstance();
		type = Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Regular.ttf");
		
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.notifsdata.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}
	
	@Override
	public long getItemId(int pos) {
		return pos;
			}
	
	@Override
	public View getView(int pos, View ConvertView, ViewGroup parent) {
		final MyNotifHolder holder;
		if (ConvertView == null){
			ConvertView = layoutinflater.inflate(R.layout.notifrows, parent, false);
			holder = new MyNotifHolder();
			holder.description = (TextView) ConvertView.findViewById(R.id.descriptionnotif);
			holder.dp= (ImageView) ConvertView.findViewById(R.id.dpnotifs);
			holder.postplay = (ImageButton) ConvertView.findViewById(R.id.playnotif);
			holder.desc = (TextView) ConvertView.findViewById(R.id.descriptions);
			holder.desc.setFocusable(false);
			holder.time = (TextView) ConvertView.findViewById(R.id.time);
			holder.post = (TextView) ConvertView.findViewById(R.id.postclickable);
			holder.post.setFocusable(false);
			holder.dp.setFocusable(false);
			holder.dp.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Log.d("What url", "what:"+ holder.profileurl);
							Intent x = new Intent(activity, profile.class);
							x.putExtra("profileurl", holder.profileurl);
							x.putExtra("prof", true);
							activity.startActivity(x);
							

					
				}
				
			});
			
			holder.description.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {

							Intent x = new Intent(activity, profile.class);
							x.putExtra("profileurl", holder.profileurl);
							x.putExtra("prof", true);
							activity.startActivity(x);
							

					
				}
				
			});
			
			holder.post.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					
						
						Intent intent = new Intent(activity, comment.class);
						int y = holder.postid;
						intent.putExtra("postid", y);
						
						activity.startActivity(intent);
						
					
					
				}
				
			});
			holder.postplay.setFocusable(false);
			holder.postplay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer)v.getTag();
				    Log.d("clicked", "Button row pos click: " + position);
				    String url = holder.playurl;
				    Log.d("url","url: " + holder.playurl);
				    
				    
				    // Toggle background resource
				    RelativeLayout layout = (RelativeLayout)v.getParent();
				    ImageButton button = (ImageButton)layout.getChildAt(1);

				 
	
				    ListView lv = (ListView) layout.getParent();
				    for (int i=0; i < lv.getChildCount(); i++){
				    	RelativeLayout row = (RelativeLayout) lv.getChildAt(i);
				    	ImageButton btns = (ImageButton) row.getChildAt(1);
				    	btns.setImageResource(R.drawable.ic_action_play);
				    	
				    }
				    
				    if(initialposition!=-1)
				    {
				    	if(mHighlightedPositions[position]) {
				    	button.setImageResource(R.drawable.playbutton);
				        mHighlightedPositions[position] = false;
				        stopPlayback();
				        
				    }	else {
				    	button.setImageResource(R.drawable.stopbutton);
				        mHighlightedPositions[position] = true;
				        mHighlightedPositions[initialposition]=false;
				        if (player.isPlaying()){
				        	player.stop();
				        	mPlayingPosition = position;
				        						        	
				        	startPlaying(url);
				  	      
				    	} else{
				    		mPlayingPosition = position;
				    		
				    	  		startPlaying(url);
				    		
				    	}
				   }
				    }
				    else {
				    	button.setImageResource(R.drawable.stopbutton);
				    	mHighlightedPositions[position] = true;
				    	if (player.isPlaying()){
				    		player.stop();
				    		mPlayingPosition = position;
				    		
				    		startPlaying(url);
				    		
				    	}else
				    	{
				    		mPlayingPosition = position;
				    		
				    		startPlaying(url);
				    	
				    	}
				    
				    }
				    initialposition = position;
			}
			});
		}else{
			holder = (MyNotifHolder)ConvertView.getTag();
			
		}
		notifdata = notifsdata.get(pos);
		holder.notifdata = notifdata;
		holder.playurl = notifdata.getplayurl();
		String x = notifdata.gettypeofnotif();
		holder.profileurl = notifdata.getprofileurl();
		
		holder.postplay.setTag(pos);
		holder.time.setTypeface(type);
		holder.description.setTypeface(type);
		holder.desc.setTypeface(type);
		holder.post.setTypeface(type);
		holder.time.setText(notifdata.gettime());
		ConvertView.setTag(holder);
		if (x.equals("likes")){
			String user  =  notifdata.getusername();
			holder.description.setText(user);
			holder.desc.setText("liked your");
			holder.postid = notifdata.getposturl();
		    holder.post.setVisibility(View.VISIBLE);	
		
			
			
		} else if (x.equals("comments")){
			String user = notifdata.getusername();
			holder.description.setText(user);
			holder.desc.setText("commented on your");
			holder.postid = notifdata.getposturl();
			holder.post.setVisibility(View.VISIBLE);
		} else if (x.equals("follows")){
			String user = notifdata.getusername();
			holder.description.setText(user);
			holder.desc.setText("started following you");
			holder.post.setVisibility(View.GONE);
		} else if (x.equals("tags")){
			String user = notifdata.getusername();
			holder.description.setText(user);
			holder.desc.setText("tagged you in a ");
			holder.post.setVisibility(View.VISIBLE);
		}

		String na = notifdata.getprofilpictureeurl();
		imageloader.displayImage(na, holder.dp);

		return ConvertView;
	}
	
	private void startPlaying(String url) {
        // TODO Auto-generated method stub
		
        try {
        	player.reset();
        	player.setDataSource(url);
        	player.reset();
            player.setDataSource(url);
            // mPlayer.setDataSource(mFileName);
            player.prepareAsync();
            player.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					player.start();
					

		
			            	
			            }
			          });
 

        } catch (IOException e) {
            Log.e("preparefailed", "prepare() failed");
            stopPlayback();
        }

    }
	
	private void stopPlayback()
    {
        mPlayingPosition = not_playing;;
       
        player.stop();
    }


}

	