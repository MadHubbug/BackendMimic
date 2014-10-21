package com.mimic.accesrest;


import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;







import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.hipmob.gifanimationdrawable.GifAnimationDrawable;
import com.mimic.accesrest.MainActivity.MyViewHolder;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.pkmmte.circularimageview.CircularImageView;







import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources.NotFoundException;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.CountDownTimer;
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
	public MediaPlayer player;
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
	public boolean checker = true;
	public Typeface type;
	private RelativeLayout s;
	private String user, share, actualusername, owns;
	private MainActivity main;
	private int Color;
	public UiLifecycleHelper uiHelper;
	private DisplayImageOptions options;
	private AnimationDrawable animation;
	public MimicAdapter(Activity a, LayoutInflater l, ArrayList <MimicData> m){
		
		this.activity = a;
		this.layoutinflater = l;
		this.mimicdata = m;
		Color = a.getResources().getColor(R.color.facebookblue);
		
		imageloader = ImageLoader.getInstance();
		type = Typeface.createFromAsset(a.getAssets(), "fonts/Roboto-Regular.ttf");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("profileid", "0");
		actualusername = prefs.getString("username", "madfresco");
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.stub)
		.showImageForEmptyUri(R.drawable.stub)
		.showImageOnFail(R.drawable.stub)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.build();

		
	}
	
	public MediaPlayer getPlayer(){
		return player;
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
			holder.user.setTypeface(type);
			
			holder.user.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Log.d("whats in this", "what: "+ holder.profileurl);
					Intent x = new Intent(activity, profile.class);
					x.putExtra("profileurl", holder.profileurl);
					x.putExtra("prof", true);
					activity.startActivity(x);
					
				}
				
			});
			holder.row = (RelativeLayout) ConvertView.findViewById(R.id.row);
			holder.row.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
				
					
					player.stop();
					Intent intent = new Intent(activity, comment.class);
					int y = holder.postid;
					intent.putExtra("postid", y);
					
					activity.startActivity(intent);
					
				}
	        	
	        	
	        });
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
							dislike.execute(x, user);
							//notifyDataSetChanged();
							Log.d("what is ", "play: "+ mimic.getLikes());
							holder.like.setImageResource(R.drawable.like);
							likenumpos[a] -= 1;
							mimic.setlikecounter(holder.likecount);
							holder.likesnum.setText(Integer.toString(likenumpos[a]));
						}else if (likedpositions[a] == false){
						
							likes.execute(x, user, holder.username, actualusername, owns);
							
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
			
			
			
			
			
			
			holder.dp = (CircularImageView) ConvertView.findViewById(R.id.displaypic);
			
			holder.dp.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					player.stop();
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
				    Log.d("position", position+" ");
				    Log.d("initialposition", initialposition+" ");
				    RelativeLayout layout = (RelativeLayout)v.getParent();
				    s = layout;
				    ImageButton button = (ImageButton)layout.getChildAt(11);
				    
				//    seekbar = (SeekBar) layout.getChildAt(5);
				    
				  //  Log.d("seekbar", "button: "+ button.getTag());
				    //Log.d("seekbar", "seekbar: "+ seekbar.getTag());
				    ListView lv = (ListView) layout.getParent();
				    for (int i=0; i < lv.getChildCount(); i++){
				    	RelativeLayout row = (RelativeLayout) lv.getChildAt(i);
				    	ImageButton btns = (ImageButton) row.getChildAt(11);
				    	if (btns == null){
				    		Log.d("btns", "nulull");
;				    	}
				    	btns.setImageResource(R.drawable.playbutton);
				    	
				    }
				    
		
				    if(position == initialposition){
				    	if (player.isPlaying()){
				    	Log.d("Are we going through here?", "stop ");
				    		player.stop();
				    		button.setImageResource(R.drawable.playbutton);
				    		mHighlightedPositions[position] = false;
				    		initialposition = position;
				    	      mHighlightedPositions[position] = false;
				    	
				    }
				    else{
				    	player.stop();
				    	button.setImageResource(R.drawable.stopbutton);
				    	startPlaying(url, position);
				    	initialposition = position;
				    	mHighlightedPositions[initialposition]=false;
				        mHighlightedPositions[position] = true;
				        
				        Log.d("Are we going through here?", "play same");
				    }
				    }
				    else if(initialposition!=-1)
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
				    
				    else if (position != initialposition){
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
					if (Session.getActiveSession().isClosed() || Session.getActiveSession() == null){
						Session.openActiveSession(activity, true, null);
					}
					if (FacebookDialog.canPresentShareDialog(activity.getApplicationContext(), 
                            FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
					// Publish the post using the Share Dialog
					FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
					.setLink("https://fb.com/mimictheapp")
					.setDescription("Shared this mimic called "+holder.sharetitle+" by " + holder.username +  ". Sign up for beta and download the app!"	)
					
					.build();
					if (uiHelper == null){
						Log.d("why", "idk");
					}
					uiHelper.trackPendingDialogCall(shareDialog.present());
					
					} else {
					// Fallback. For example, publish the post using the Feed Dialog
					}
					
					
					
				}
			});
	       

	        
	        holder.reply = (ImageButton) ConvertView.findViewById(R.id.reply);
	        holder.reply.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
				
					
					player.stop();
					Intent intent = new Intent(activity, comment.class);
					int y = holder.postid;
					intent.putExtra("postid", y);
					
					activity.startActivity(intent);
					
				}
	        	
	        	
	        });
	        
	        
	        
	        
	        
	        
	        
			ConvertView.setTag(holder);
			ConvertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				
					
					player.stop();
					Intent intent = new Intent(activity, comment.class);
					int y = holder.postid;
					intent.putExtra("postid", y);
					
					activity.startActivity(intent);
					
				}
			});
			parents = parent;
		}else{
			holder = (MyViewHolder)ConvertView.getTag();
		}
	   	if (player == null){
    		player = new MediaPlayer();
    	}

		mimic = mimicdata.get(pos);
		holder.mimic = mimic;
		String y = mimic.getUsername();
		holder.user.setText(y.substring(0,1).toUpperCase()+y.substring(1));
		holder.user.setTypeface(type);
		holder.username = mimic.getUsername();
		holder.description.setTypeface(type);
		holder.description.setText(mimic.getsharecount());
		holder.likecount = mimic.getlikecounter();
		holder.sharetitle = mimic.getsharecount();
		holder.commentnum.setText(mimic.getcommentcounter());
		holder.commentnum.setTypeface(type);
		holder.timestamp.setText(mimic.gettime());
		holder.postid = mimic.getpostid();
		holder.profileurl= mimic.getprofileurl();
		holder.own = mimic.getowner();
		if (holder.own == true){
			owns = "true";
		}else if (holder.own == false){
			owns = "false";
		}
		Log.d("What is parent", parent.getParent() + " ");
		//getuserPic x = new getuserPic();
		//x.execute("http://graph.facebook.com/snucks/picture?type=large");
		holder.posturl = mimic.getposturl();
		holder.play.setTag(pos);
		//holder.sb.setTag(pos);
		holder.like.setTag(pos);
		Pattern tagMatcher = Pattern.compile("#([ء-يA-Za-z0-9_-]+)");
		String newActivityURL = "content://com.mimic.accesrest.hash/";
		Linkify.addLinks(holder.description, tagMatcher, newActivityURL);
		Pattern tagMatch = Pattern.compile("[@]+[A-Za-z0-9-_]+\\b");
		String newActivity = "mimic://com.mimic.accesrest.profile/";
		Linkify.addLinks(holder.description,tagMatch, newActivity);
		stripUnderlines(holder.description);
		try {
			animation = new GifAnimationDrawable(activity.getResources().openRawResource(R.raw.wheel));
		} catch (NotFoundException e) {
			Log.d("woah", "not working");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String na = mimic.geturl();
		
		imageloader.displayImage(na, holder.dp, options);
//		if (checker == true){
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
//			checker = false;
			}
//			}
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
            ImageButton w = (ImageButton) s.getChildAt(11);
            
			w.setImageDrawable(animation);            
            
            player.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					
					player.start();
					ImageButton w = (ImageButton) s.getChildAt(11);
					w.setImageResource(R.drawable.stopbutton);
					player.setOnCompletionListener(new OnCompletionListener(){

						@Override
						public void onCompletion(MediaPlayer arg0) {
							ImageButton r = (ImageButton)s.getChildAt(11);
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

