package com.mimic.accesrest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import com.mimic.accesrest.R.drawable;
import com.mimic.accesrest.notifications.notificationpost;
import com.mimic.accesrest.post.MyCounter;
import com.mimic.accesrest.posting.Createbucket;
import com.mimic.accesrest.posting.apacheHttpClientPost;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stream.aws.Response;


import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class comment extends SherlockActivity {

	private ArrayList<commentdata> comments;
	private ListView CommentList;
	private LayoutInflater layoutinflater;

	private commentadapter CommentAdapter;
	private ArrayList<MimicData> mimicdatas;
	private ImageButton playpostbutton;
	public MediaPlayer player;
	public ImageLoader imageloader;
	private int postid;
	public boolean playingpos = true;
	private static String mFileName = null;
	public static AmazonClientManager clientManager = null;
	private static URL newurl = null;
	private TextView tv;
	private ImageButton record, delete, play, next;
	private ExtAudioRecorder mRecorder = null;
	private boolean plays = true;
	private boolean owner;
	private String LOG_TAG = "comment";
	private String user, post, users, username, actualusername, owns;
	private SharedPreferences prefs;  
	
	private void onRecord (boolean start){
		if (start){
			startRecording();
		} else
			stopRecording();
	}
	private void onPlay(boolean start){
		if(start){
			startPlaying();
		} else {
			stopPlaying();
		}
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment);
		this.CommentList= (ListView) findViewById(R.id.commentpagelistviews);
		this.layoutinflater = LayoutInflater.from(this);
		this.playpostbutton = (ImageButton) findViewById(R.id.commentpageplays);
		Bundle bundle = getIntent().getExtras();
		postid = bundle.getInt("postid");
		player = new MediaPlayer();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		comment(postid);
		imageloader = ImageLoader.getInstance();
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		SpannableString s = new SpannableString("Comment");
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.back);
		ImageView view = (ImageView) findViewById(android.R.id.home);
		tv = (TextView) findViewById(R.id.countdowncomment);
		tv.setVisibility(View.GONE);
		play = (ImageButton) findViewById(R.id.playpostcomment);
		play.setVisibility(View.GONE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		users = prefs.getString("profileid", "0");
		user = prefs.getString("profileid", "0");
		actualusername= prefs.getString("username", "madfresco");
		clientManager = new AmazonClientManager(getSharedPreferences(
				"com.mimic.accessrest", Context.MODE_PRIVATE));
		if (comment.clientManager.hasCredentials()) {
			Log.d(LOG_TAG, "has credentials");
		}else {
			Log.d(LOG_TAG, "no credentials");
		}
		next = (ImageButton) findViewById(R.id.nextbuttoncomment);
		next.setVisibility(View.GONE);
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				new ValidateCredentialsTask().execute();
				record.setVisibility(View.VISIBLE);
				next.setVisibility(View.GONE);
				play.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
				
			}
			
		});
		play.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				playpostbutton.setImageResource(R.drawable.playbutton);
				ListView lv = (ListView) findViewById(R.id.commentpagelistviews);
				int k = lv.getChildCount();
				for (int s = 0; s<k; s++){
				RelativeLayout rl = (RelativeLayout) lv.getChildAt(s);
					ImageButton bt = (ImageButton) rl.getChildAt(2);
					bt.setImageResource(R.drawable.playbutton);
				}
				
				onPlay(plays);
				if (plays){
				play.setImageResource(R.drawable.biggerstop);
				}else {
					play.setImageResource(R.drawable.biggerplay);
				} plays = !plays;
			}
		});
		final MyCounter timer = new MyCounter(30000, 1000);
		record = (ImageButton) findViewById(R.id.recordcomment);
		record.setOnClickListener(new OnClickListener(){
			boolean mStartRecording = true;
			
			@Override
			public void onClick(View v) {
				onRecord(mStartRecording);
				if (mStartRecording){
				timer.start();
					record.setImageResource(R.drawable.recred);
					tv.setVisibility(View.VISIBLE);
					delete.setVisibility(View.GONE);
				} else{
					timer.cancel();
					record.setImageResource(R.drawable.recwhite);
					tv.setVisibility(View.GONE);
					delete.setVisibility(View.VISIBLE);
					record.setVisibility(View.GONE);
					next.setVisibility(View.VISIBLE);
					play.setVisibility(View.VISIBLE);
					
				}
				mStartRecording = !mStartRecording;
			}
				
			
			
		});
		delete = (ImageButton) findViewById(R.id.delete);
		delete.setVisibility(View.GONE);
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				play.setVisibility(View.GONE);
				record.setImageResource(R.drawable.recwhite);
				record.setVisibility(View.VISIBLE);
				next.setVisibility(View.GONE);
				delete.setVisibility(View.GONE);
			}
			
		});
		
	}
	
	private void startPlaying(){
		
		try{
			player.reset();
			player.setDataSource(mFileName);
			player.prepare();
			player.start();
			player.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer arg0) {
					play.setImageResource(R.drawable.biggerplay);
					player.reset();
					plays=true;
				}
				
			});
		} catch (IOException e){
			Log.e("player", "prepare() failed");
		}
	}
	
	private void stopPlaying(){
		player.stop();		
	}
	
	private void startRecording(){
		// Start recording
		//mRecorder = ExtAudioRecorder.getInstanse(true);	  // Compressed recording (AMR)
		mRecorder = ExtAudioRecorder.getInstanse(false); // Uncompressed recording (WAV)
		mRecorder.setOutputFile(mFileName);
		
		
		mRecorder.prepare();
		mRecorder.start();
	}
	
	private void stopRecording(){
		
		mRecorder.stop();
		mRecorder.release();

	}
	
	public comment(){
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.wav";
	}
	
	public class MyCounter extends CountDownTimer{

	    public MyCounter(long millisInFuture, long countDownInterval) {
	        super(millisInFuture, countDownInterval);
	    }

	    

	    @Override
	    public void onTick(long millisUntilFinished  ) {
	        tv.setText((millisUntilFinished/1000+"s"));
	        
	    }



		@Override
		public void onFinish() {
			
			record.setImageResource(R.drawable.recwhite);
			tv.setVisibility(View.GONE);
			delete.setVisibility(View.VISIBLE);
			record.setVisibility(View.GONE);
			next.setVisibility(View.VISIBLE);
			play.setVisibility(View.VISIBLE);	
			onRecord(false);
			
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
	
	public void setUsers(ArrayList<commentdata> comment) {
		this.comments = comment;
		
		CommentAdapter = new commentadapter(this,this.layoutinflater, this.comments);
		this.CommentList.setAdapter(CommentAdapter);
	
		
	}
	
	  public void comment(int a){
			commentwebtask mimictask = new commentwebtask(comment.this);
			try{
				mimictask.execute(a);
				} catch (Exception e){
					mimictask.cancel(true);
					alert("Nothing to play");
				}
			
		}
	  
	  public void alert(String msg) {
			Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
			
		}
	  
	  public static class MyCommentHolder {
		  public MimicData play;
		  public ImageButton commentplays;
		  public commentdata comments;
		  public String commenturl, posturl, profileurl;
		public ImageView dp;
		public TextView user, times;

		  
	  }

	public void setUser(ArrayList<MimicData> playdata) {
		this.mimicdatas = playdata;
		final MimicData x = mimicdatas.get(0);
		String na = x.geturl();
		ImageView dp = (ImageView) findViewById(R.id.commentpagedisplaypic);
		imageloader.displayImage(na, dp);
		TextView user = (TextView) findViewById(R.id.commentpageusername);
		user.setText(x.getUsername());
		username = x.getUsername();
		TextView description = (TextView) findViewById(R.id.commentpagedescription);
		description.setText(x.getsharecount());
		final TextView like = (TextView) findViewById(R.id.commentpagelikecount);
		like.setText(Integer.toString(x.getlikecounter()));
		TextView commentcount = (TextView) findViewById(R.id.commentpagereplycount);
		commentcount.setText(x.getcommentcounter());
		final ImageButton s = (ImageButton) findViewById(R.id.commentpagelike);
		TextView timestamp = (TextView) findViewById(R.id.commentpagetimestamp);
		timestamp.setText(x.gettime());
		owner = x.getowner();
		if (owner == true){
			owns = "true";
		}else if (owner == false){
			owns = "false";
		}
		final boolean w = x.getLikes();
		Log.d("liked?!", x.getLikes() + " ");
		if (w==true){
			s.setImageResource(R.drawable.liked);
		}else{
			s.setImageResource(R.drawable.like);
		}
		
		s.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				final liking likes = new liking();
				final dislike dislike = new dislike();
				if (w==true){
					s.setImageResource(R.drawable.like);
					String m = Integer.toString(postid);
					dislike.execute(m);
					int a = x.getlikecounter();
					a -= 1;
					like.setText(Integer.toString(a));
				}else{
					s.setImageResource(R.drawable.liked);
					String m = Integer.toString(postid);
					likes.execute(m, users, username, actualusername, owns);
					int a = x.getlikecounter();
					a += 1;
					like.setText(Integer.toString(a));
				}
				
			}
			
		});
		playpostbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			ListView lv = (ListView) findViewById(R.id.commentpagelistviews);
			int k = lv.getChildCount();
			for (int s = 0; s<k; s++){
			RelativeLayout rl = (RelativeLayout) lv.getChildAt(s);
				ImageButton bt = (ImageButton) rl.getChildAt(2);
				bt.setImageResource(R.drawable.playbutton);
			}
			String url = x.getposturl();	
			
			
			if (playingpos){
				if (player.isPlaying()){
				player.stop();
				startPlaying(url);
				playpostbutton.setImageResource(R.drawable.stopbutton);
				player.stop();
				playingpos = false;
			}else{
				playpostbutton.setImageResource(R.drawable.stopbutton);
				startPlaying(url);
				playingpos = false;
	
				}
			}
				else{
					
					playpostbutton.setImageResource(R.drawable.playbutton);
					player.stop();
					playingpos = true;
				}
				
			
			
			}
				
			
		});
		
	}
	
	public MediaPlayer getmediaplayer(){
		return player;
	};
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
					player.setOnCompletionListener(new OnCompletionListener(){

						@Override
						public void onCompletion(MediaPlayer arg0) {
							playpostbutton.setImageResource(R.drawable.playbutton);
							
						}
						
					});

		
			            	
			            }
			          });
 

        } catch (IOException e) {
            Log.e("preparefailed", "prepare() failed");
            player.stop();
            
        }

    }
	
	@Override
	protected void onDestroy()
	{
		player.release();
		super.onDestroy();
	}
	

	private class ValidateCredentialsTask extends
	AsyncTask<Void, Void, Response> {

		@Override
		protected Response doInBackground(Void... arg0) {


			return comment.clientManager.validateCredentials();
		}
		@Override
		protected void onPostExecute(Response response) {
			Createbucket bucket = new Createbucket();
			bucket.execute(response);
			Log.d("validation", "onpostexecute");
			

		}



	}
	
	public class Createbucket extends
	AsyncTask<Response, Void, String> {


		private String data = null;
		@Override
		protected String doInBackground(Response... responses) {

			Response response = responses[0];
			if (response != null && response.requestWasSuccessful()) {	
				Log.d(LOG_TAG, "Validated");
				TransferManager manager = new TransferManager(clientManager.s3());
				Log.d(LOG_TAG, "creating file");
				File filex = new File(mFileName);
				AmazonS3Client s3Client = clientManager.s3();
				Long timestamp =  (System.currentTimeMillis()/60000);
				String time = timestamp.toString();
				String bucket = prefs.getString("bucket", "no bucket");
				try{
					final ObjectMetadata metx = new ObjectMetadata();
					metx.addUserMetadata("Content-Type", "audio/x-wav");
					PutObjectRequest por = new PutObjectRequest(bucket, user+postid+time+".wav", filex);
					por.withMetadata(metx);
					por.withCannedAcl(CannedAccessControlList.PublicRead);
					Upload upload = manager.upload(por);

					if (upload.isDone()){
						Log.d(LOG_TAG, "done");

					} 

					Log.d(LOG_TAG, upload.getDescription());
					newurl = s3Client.getUrl(bucket, user+postid+time+".wav");
				
					Log.d(LOG_TAG, "creating object");
				
					Log.d(LOG_TAG, data);

					Log.d(LOG_TAG, "posting data");
				}
				catch (Exception exception){
					Log.d(LOG_TAG, "catching error");
				}


			}
			return null;


		}
		@Override
		protected void onPostExecute(String result){

			
			try{
				apacheHttpClientPost post = new apacheHttpClientPost();
				Log.d(LOG_TAG, "Thisis");
				post.execute("http://mimictheapp.herokuapp.com/comments/");
				Log.d(LOG_TAG, "Executing gg");
		    	comment(postid);


			}catch (Exception exception){

			}

		}



	}
	private String getQueryJSON(List<NameValuePair> params) throws UnsupportedEncodingException

	{

		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params)

		{

			if (first){

				first = false;

				result.append("{");

			}else

				result.append(",");


			result.append(pair.getName());

			result.append(":");

			result.append(pair.getValue());


		}

		result.append("}");

		return result.toString();

	}
	public class apacheHttpClientPost extends AsyncTask<String,String,Response> {
		@Override
		protected Response doInBackground(String... params) {
			// TODO Auto-generated method stub
			post = Integer.toString(postid);
			
		
		
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				
				nameValuePairs.add(new BasicNameValuePair("\"parentpost\"", post));
				nameValuePairs.add(new BasicNameValuePair("\"commenturl\"", "\""+newurl.toString()+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"user\"", user)); 
			
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				Log.d(LOG_TAG, "httpclient");        
				HttpPost postRequest = new HttpPost(params[0]);
				StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
				Log.d(LOG_TAG,getQueryJSON(nameValuePairs));
				postRequest.setHeader("Content-type","application/json");
				postRequest.setEntity(entity);	
				postRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
				Log.d(LOG_TAG, "noresponse");  
				httpClient.execute(postRequest);
				Log.d(LOG_TAG, "noresponse");  

				System.out.println("Output from Server .... \n");

				httpClient.getConnectionManager().shutdown();

			} catch (MalformedURLException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}
			return null;

		}
		
		@Override
		protected void onPostExecute(Response response) {
			notificationpost notif= new notificationpost();
			notif.execute(post, user, "comments", username, actualusername, owns);

			

		}

	}
	
	
	
	
	
	
	
	
	
	
}
