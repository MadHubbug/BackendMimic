package com.mimic.accesrest;


import java.io.IOException;
import java.util.ArrayList;






import com.fedorvlasov.lazylist.ImageLoader;
import com.mimic.accesrest.MainActivity.MyViewHolder;







import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;



public class MimicAdapter extends BaseAdapter {

	private static final String debugtag = "MimicAdapter";
	private Activity activity;
	private LayoutInflater layoutinflater;
	private ArrayList<MimicData> mimicdata;
	private MimicData mimic;
	private final String LOG_TAG = "mimicadapter";
	private ImageView dp;
	public ImageLoader imageloader;
	//public SeekBar seekbar;
	private MediaPlayer player = new MediaPlayer();
	private final boolean[] mHighlightedPositions = new boolean[50];
	private final boolean[] likedpositions = new boolean[50];
	private final int[] likenumpos = new int[50];
	int initialposition = -1;  
	private int mPlayingPosition = not_playing;
	private static int not_playing = -1;
	public boolean mStartPlaying;
	public ViewGroup parents;
	public Handler mHandler = new Handler();
	//private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
	private boolean checker = true;
	public Typeface type;
	private RelativeLayout s;
	private String user;
	
	public MimicAdapter(Activity a, LayoutInflater l, ArrayList <MimicData> m){
		
		this.activity = a;
		this.layoutinflater = l;
		this.mimicdata = m;
		imageloader=new ImageLoader(activity.getApplicationContext());
		type = Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Regular.ttf");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("profileid", "0");

 
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
	public View getView(final int pos, View ConvertView, ViewGroup parent) {
		
		final MyViewHolder holder;
		if (ConvertView == null){
			ConvertView = layoutinflater.inflate(R.layout.rows, parent, false);
			holder = new MyViewHolder();
			holder.user = (TextView) ConvertView.findViewById(R.id.username);
			holder.description = (TextView) ConvertView.findViewById(R.id.description);
			holder.likesnum= (TextView) ConvertView.findViewById(R.id.likecount);
			holder.commentnum= (TextView) ConvertView.findViewById(R.id.replycount);
			holder.like = (ImageButton) ConvertView.findViewById(R.id.like);
			holder.timestamp = (TextView) ConvertView.findViewById(R.id.timestamp);
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
							mimic.setLikes(false);
							likedpositions[a] = false;
							dislike.execute(x);
							//notifyDataSetChanged();
							Log.d("what is ", "play: "+ mimic.getLikes());
							holder.like.setImageResource(R.drawable.like);
							likenumpos[a] -= 1;
							mimic.setlikecounter(holder.likecount);
							holder.likesnum.setText(Integer.toString(likenumpos[a]));
						}else if (likedpositions[a] == false){
						
							likes.execute(x, user);
							Log.d("what is x", x);
							mimic.setLikes(true);
							likedpositions[a] = true;
							//notifyDataSetChanged();
							Log.d("what is ", "play: "+ mimic.getLikes());
							holder.like.setImageResource(R.drawable.liked);
							likenumpos[a] += 1;
							mimic.setlikecounter(holder.likecount);
							holder.likesnum.setText(Integer.toString(likenumpos[a]));
					
						}
							
					
					}
						
					
				
		
			});
			
			
			
			
			
			
			holder.dp = (ImageView) ConvertView.findViewById(R.id.displaypic);
			holder.dp.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Log.d("whats in this", "what: "+ holder.profileurl);
					Intent x = new Intent(activity, profile.class);
					x.putExtra("profileurl", holder.profileurl);
					x.putExtra("prof", true);
					activity.startActivity(x);
					
				}
				
			});
			ImageView x = (ImageView) ConvertView.findViewById(R.id.displaypic);
	
			mimic = mimicdata.get(pos);
			
			//holder.sb = (SeekBar) ConvertView.findViewById(R.id.seekBar);
			
			

	
	        
	        
	        holder.play = (ImageButton) ConvertView.findViewById(R.id.plays);
	        holder.play.setFocusable(false);
	        holder.play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer)v.getTag();
				    Log.d("clicked", "Button row pos click: " + position);
				    String url = holder.posturl;
				    
				    RelativeLayout layout = (RelativeLayout)v.getParent();
				    s = layout;
				    ImageButton button = (ImageButton)layout.getChildAt(12);
				    
				//    seekbar = (SeekBar) layout.getChildAt(5);
				    
				  //  Log.d("seekbar", "button: "+ button.getTag());
				    //Log.d("seekbar", "seekbar: "+ seekbar.getTag());
				    ListView lv = (ListView) layout.getParent();
				    for (int i=0; i < lv.getChildCount(); i++){
				    	RelativeLayout row = (RelativeLayout) lv.getChildAt(i);
				    	ImageButton btns = (ImageButton) row.getChildAt(12);
				    	if (btns == null){
				    		Log.d("btns", "nulull");
;				    	}
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
				      //  	mProgressUpdater.mBarToUpdate = seekbar;
				     
				        	//mHandler.postDelayed(mProgressUpdater, 500);
				        	
				        	startPlaying(url, position);
				  	      
				    	} else{
				    		mPlayingPosition = position;
				    	//	mProgressUpdater.mBarToUpdate = seekbar;
				    	
				        //    mHandler.postDelayed(mProgressUpdater, 500);
				    	  		startPlaying(url, position);
				    		
				    	}
				   }
				    }
				    else {
				    	button.setImageResource(R.drawable.stopbutton);
				    	mHighlightedPositions[position] = true;
				    	if (player.isPlaying()){
				    		player.stop();
				    		mPlayingPosition = position;
				    		//mProgressUpdater.mBarToUpdate = seekbar;
				    
				          //  mHandler.postDelayed(mProgressUpdater, 500);
				    		startPlaying(url,position);
				    		
				    	}else
				    	{
				    		mPlayingPosition = position;
				    		//mProgressUpdater.mBarToUpdate = seekbar;
				    	
				            //mHandler.postDelayed(mProgressUpdater, 500);
				    		startPlaying(url, position);
				    	
				    	}
				    
				    }
				    initialposition = position;
			}
				});
	        
	        holder.share = (ImageButton) ConvertView.findViewById(R.id.share);
	        holder.share.setFocusable(false);
	        holder.share.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Log.d("clcked", "share");
					
				}
			});
	       

	        
	        holder.reply = (ImageButton) ConvertView.findViewById(R.id.reply);
	        holder.reply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
				
					Intent intent = new Intent(activity, comment.class);
					int y = holder.postid;
					intent.putExtra("postid", y);
					
					activity.startActivity(intent);
					
				}
	        	
	        	
	        });
	        
	        
	        
	        
	        
	        
	        
			ConvertView.setTag(holder);
			ConvertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v){
					Log.d("hi", "hi from "+ pos);
				}
			});
			parents = parent;
		}else{
			holder = (MyViewHolder)ConvertView.getTag();
		}
		
		
		
		mimic = mimicdata.get(pos);
		holder.mimic = mimic;
		String y = mimic.getUsername();
		holder.user.setText(y.substring(0,1).toUpperCase()+y.substring(1));
		holder.user.setTypeface(type);
		holder.description.setTypeface(type);
		holder.description.setText(mimic.getsharecount());
		holder.likecount = mimic.getlikecounter();
		
		holder.commentnum.setText(mimic.getcommentcounter());
		holder.commentnum.setTypeface(type);
		holder.timestamp.setText(mimic.gettime());
		holder.postid = mimic.getpostid();
		holder.profileurl= mimic.getprofileurl();
		//getuserPic x = new getuserPic();
		//x.execute("http://graph.facebook.com/snucks/picture?type=large");
		holder.posturl = mimic.getposturl();
		holder.play.setTag(pos);
		//holder.sb.setTag(pos);
		holder.like.setTag(pos);
		String na = mimic.geturl();
		imageloader.DisplayImage(na, holder.dp, 100);
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
		holder.likesnum.setText(Integer.toString(likenumpos[pos]));
		holder.likesnum.setTypeface(type);
		if (likedpositions[pos]){
			holder.like.setImageResource(R.drawable.liked);
		}else{
			holder.like.setImageResource(R.drawable.like);
		}
		
		
		if(mHighlightedPositions[pos]) {
			holder.play.setImageResource(R.drawable.stopbutton);
			//mProgressUpdater.mBarToUpdate = holder.sb;
			//mHandler.postDelayed(mProgressUpdater, 100);
	    }else {
	    	holder.play.setImageResource(R.drawable.playbutton);
	    //	holder.sb.setProgress(0);
	    	//if (mProgressUpdater.mBarToUpdate == holder.sb){
	    		//mProgressUpdater.mBarToUpdate = null;
	    	//}
	    }
		return ConvertView;
		
		
	}
	private void startPlaying(String url, int tag) {
        // TODO Auto-generated method stub
		final int k = tag;
        try {
        	
            player.reset();
            player.setDataSource(url);
            // mPlayer.setDataSource(mFileName);
            player.prepareAsync();
            player.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					
					player.start();
					player.setOnCompletionListener(new OnCompletionListener(){

						@Override
						public void onCompletion(MediaPlayer arg0) {
							ListView lr = (ListView) s.getParent();
							RelativeLayout m = (RelativeLayout) lr.getChildAt(k);
							ImageButton r = (ImageButton) m.getChildAt(12);
					    	r.setImageResource(R.drawable.playbutton);
					    	mHighlightedPositions[k] = false;
							
						}
						
					});
					
					
		
			            	
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
    //    mProgressUpdater.mBarToUpdate = null;
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

