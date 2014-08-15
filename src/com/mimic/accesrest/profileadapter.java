package com.mimic.accesrest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;


import com.mimic.accesrest.profile.MyViewHolder;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class profileadapter extends BaseAdapter implements OnClickListener{

	private static final String debugtag = "MimicAdapter";
	private Activity activity;
	private MimicData mimic;
	private final boolean[] likedpositions = new boolean[50];
	private LayoutInflater layoutinflater;
	private ArrayList<MimicData> mimicdata;
	int initialposition = -1;  
	private int mPlayingPosition = not_playing;
	private static int not_playing = -1;
	public boolean mStartPlaying;
//	public Handler mHandler = new Handler();
//	private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
	private MediaPlayer player = new MediaPlayer();
//	public SeekBar seekbar;
	private final boolean[] mHighlightedPositions = new boolean[100];
	private boolean checker = true;
	public Typeface type;
	private String user, actualuser, owns;
	private final int[] likenumpos = new int[50];
	
	
	public profileadapter(Activity a, LayoutInflater l, ArrayList <MimicData> m){
		
		this.activity = a;
		this.layoutinflater = l;
		this.mimicdata = m;
		type = Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Regular.ttf");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("profileid", "madfresco");
		actualuser = prefs.getString("username", "madfresco");
	}
		
	@Override
	public int getCount() {
		return this.mimicdata.size();
	}
	
	@Override
	public boolean areAllItemsEnabled(){
		return true;
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
		
		final MyViewHolder holder;
		if (ConvertView == null){
			ConvertView = layoutinflater.inflate(R.layout.profrows, parent, false);
			holder = new MyViewHolder();
			holder.description= (TextView) ConvertView.findViewById(R.id.commentpagedescription);
			holder.likesnum= (TextView) ConvertView.findViewById(R.id.commentpagelikecount);
			holder.commentnum= (TextView) ConvertView.findViewById(R.id.commentpagereplycount);
			holder.timestamp = (TextView) ConvertView.findViewById(R.id.profilepagetimestamp); 
			
			ConvertView.setTag(holder);
//			holder.sb = (SeekBar) ConvertView.findViewById(R.id.commentpageseekBar);
			holder.like = (ImageButton) ConvertView.findViewById(R.id.commentpagelike);
			holder.like.setFocusable(false);
			holder.like.setOnClickListener(new OnClickListener(){

				@Override
				
					public void onClick(View v) {
						final liking likes = new liking();
						final dislike dislike = new dislike();	
						int y = holder.postid;
						int a = (Integer) v.getTag();
						final String x = Integer.toString(y);
						if (likedpositions[a] == true){
							Log.d("what is likecounter", likenumpos[a]+""); 
							mimic.setLikes(false);
							likedpositions[a] = false;
							dislike.execute(x);
							//notifyDataSetChanged();
							Log.d("what is ", "play: "+ mimic.getLikes());
							likenumpos[a] -= 1;
							mimic.setlikecounter(holder.likecount);
							holder.likesnum.setText(Integer.toString(likenumpos[a]));
							holder.like.setImageResource(R.drawable.like);			
						}else if (likedpositions[a] == false){
						
							likes.execute(x, user, holder.username, actualuser, owns);
							Log.d("username", holder.username+ " ");
							Log.d("what is x", x);
							mimic.setLikes(true);
							likedpositions[a] = true;
							//notifyDataSetChanged();
							Log.d("what is ", "play: "+ mimic.getLikes());
							Log.d("what is likecounter", likenumpos[a]+"");
							likenumpos[a] += 1;
							mimic.setlikecounter(holder.likecount);
							holder.likesnum.setText(Integer.toString(likenumpos[a]));
							holder.like.setImageResource(R.drawable.liked);
						}
							
					
					}
						
					
				
		
			});
			
			holder.reply = (ImageButton) ConvertView.findViewById(R.id.commentpagereply);
	        holder.reply.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
					
						Intent intent = new Intent(activity, comment.class);
						int y = holder.postid;
						intent.putExtra("postid", y);
						
						activity.startActivity(intent);
						
					}
		        	
		        	
		        });
			holder.play = (ImageButton) ConvertView.findViewById(R.id.commentpageplays);
	        holder.play.setFocusable(false);
	        holder.play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer)v.getTag();
				    Log.d("clicked", "Button row pos click: " + position);
				    String url = holder.posturl;
				    
				    RelativeLayout layout = (RelativeLayout)v.getParent();
				    ImageButton button = (ImageButton)layout.getChildAt(7);
				    
//				    seekbar = (SeekBar) layout.getChildAt(4);
				    
				    
				    ListView lv = (ListView) layout.getParent();
				    for (int i=0; i < lv.getChildCount(); i++){
				    	RelativeLayout row = (RelativeLayout) lv.getChildAt(i);
				    	ImageButton btns = (ImageButton) row.getChildAt(7);
				    	btns.setImageResource(R.drawable.playbutton);
				    	
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
//				        	mProgressUpdater.mBarToUpdate = seekbar;
				     
//				        	mHandler.postDelayed(mProgressUpdater, 500);
				        	
				        	startPlaying(url);
				  	      
				    	} else{
				    		mPlayingPosition = position;
//				    		mProgressUpdater.mBarToUpdate = seekbar;
				    	
//				            mHandler.postDelayed(mProgressUpdater, 500);
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
//				    		mProgressUpdater.mBarToUpdate = seekbar;
				    
//				            mHandler.postDelayed(mProgressUpdater, 500);
				    		startPlaying(url);
				    		
				    	}else
				    	{
				    		mPlayingPosition = position;
//				    		mProgressUpdater.mBarToUpdate = seekbar;
				    	
//				            mHandler.postDelayed(mProgressUpdater, 500);
				    		startPlaying(url);
				    	
				    	}
				    
				    }
				    initialposition = position;
			}
				});
			
		}
		else{
			holder = (MyViewHolder)ConvertView.getTag();
		}
		
		ConvertView.setOnClickListener(this);
		
		mimic = mimicdata.get(pos);
		holder.mimic = mimic;
		holder.username = mimic.getUsername();
		Log.d("whatis", holder.username+":"+mimic.getUsername());
		holder.description.setText(mimic.getsharecount());
		holder.description.setTypeface(type);
		holder.likesnum.setText(Integer.toString(mimic.getlikecounter()));
		holder.likesnum.setTypeface(type);
		holder.likecount = mimic.getlikecounter();
		Log.d("what", holder.likecount+"");
		holder.commentnum.setText(mimic.getcommentcounter());
		holder.commentnum.setTypeface(type);
		holder.timestamp.setText(mimic.gettime());
		holder.play.setTag(pos);
		holder.posturl = mimic.getposturl();
		holder.postid = mimic.getpostid();
		holder.like.setTag(pos);
		holder.own = mimic.getowner();
		if (holder.own == true){
			owns = "true";
		} else if (holder.own == false){
			owns = "false";
		}
		Log.d("before checker", holder.likecount+"");
		if (checker == true){
			for (int i=0; i<mimicdata.size(); i++)				
			{
				mimic = mimicdata.get(i);
				holder.liked = mimic.getLikes();
				holder.likecount=mimic.getlikecounter();
				likenumpos[i] = holder.likecount;
				
			if (holder.liked == true){
				likedpositions[i] = true;				
			}else {
				likedpositions[i] = false;
			}
			checker = false;
			}
			}
		if (likedpositions[pos]){
			holder.like.setImageResource(R.drawable.liked);
		}else{
			holder.like.setImageResource(R.drawable.like);
		}
		
		
		if(mHighlightedPositions[pos]) {
			holder.play.setImageResource(R.drawable.stopbutton);
//			mProgressUpdater.mBarToUpdate = holder.sb;
//			mHandler.postDelayed(mProgressUpdater, 100);
	    }else {
	    	holder.play.setImageResource(R.drawable.playbutton);
//	    	holder.sb.setProgress(0);
//	    	if (mProgressUpdater.mBarToUpdate == holder.sb){
//	    		mProgressUpdater.mBarToUpdate = null;
//	    	}
	    }
		Pattern tagMatcher = Pattern.compile("#([ء-يA-Za-z0-9_-]+)");
		String newActivityURL = "content://com.mimic.accesrest.hash/";
		Linkify.addLinks(holder.description, tagMatcher, newActivityURL);
		Pattern tagMatch = Pattern.compile("[@]+[A-Za-z0-9-_]+\\b");
		String newActivity = "mimic://com.mimic.accesrest.profile/";
		Linkify.addLinks(holder.description,tagMatch, newActivity);
		stripUnderlines(holder.description);
		
		
		
		return ConvertView;
		
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	  private void stripUnderlines(TextView textView) {
	        Spannable s = new SpannableString(textView.getText());
	        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
	        for (URLSpan span: spans) {
	            int start = s.getSpanStart(span);
	            int end = s.getSpanEnd(span);
	            s.removeSpan(span);
	            span = new URLSpanNoUnderline(span.getURL());
	            s.setSpan(span, start, end, 0);
	        }
	        textView.setText(s);
	    }
	   private class URLSpanNoUnderline extends URLSpan {
	        public URLSpanNoUnderline(String url) {
	            super(url);
	        }
	        @Override public void updateDrawState(TextPaint ds) {
	            super.updateDrawState(ds);
	            ds.setUnderlineText(false);
	        }
	    }
	   
	private void startPlaying(String url) {
        // TODO Auto-generated method stub
		
        try {
        	
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
//        mProgressUpdater.mBarToUpdate = null;
        player.stop();
     
    }
//	public class PlaybackUpdater implements Runnable {
//        public ProgressBar mBarToUpdate = null;
//
//        @Override
//        public void run() {
//            if ((mPlayingPosition != not_playing) && (null != mBarToUpdate)) {
//                mBarToUpdate.setProgress( (800*player.getCurrentPosition() / player.getDuration()) ); //Cast          
//                mHandler.postDelayed(this, 500);
//               
//                
//            } else {
//                //not playing so stop updating
//            }
//        }






//}
	
	
}
