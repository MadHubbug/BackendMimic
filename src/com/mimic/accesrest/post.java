package com.mimic.accesrest;

import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.actionbarsherlock.app.SherlockActivity;
import com.mimic.accesrest.ExtAudioRecorder;
import com.stream.aws.Response;



public class post extends SherlockActivity {
	private static final String LOG_TAG = "AudioRecordTest";
	private static String mFileName = null;
	
	
	private ExtAudioRecorder mRecorder = null;
	private boolean plays = true;
	
	private MediaPlayer mPlayer = new MediaPlayer();
	private TextView tv;
	private ImageButton record, delete, play;
	private static final String Logs = "postui";
	private RelativeLayout rel;
	
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
	
	private void startPlaying(){
		
		try{
			mPlayer.reset();
			mPlayer.setDataSource(mFileName);
			mPlayer.prepare();
			mPlayer.start();
			mPlayer.setOnCompletionListener(new OnCompletionListener(){

				@Override
				public void onCompletion(MediaPlayer arg0) {
					play.setImageResource(R.drawable.biggerplay);
					mPlayer.reset();
					plays=true;
				}
				
			});
		} catch (IOException e){
			Log.e(LOG_TAG, "prepare() failed");
		}
	}
	
	private void stopPlaying(){
		mPlayer.stop();		
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
	
	public post(){
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName += "/audiorecordtest.wav";
	}
	

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recording);
	   	tv = (TextView) findViewById(R.id.countdowncomment);
	   	tv.setVisibility(View.GONE);
	   	final ImageButton next = (ImageButton) findViewById(R.id.nextbuttoncomment);
		play = (ImageButton) findViewById(R.id.playpostcomment);
		play.setVisibility(View.GONE);
		play.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				onPlay(plays);
				if (plays){
				play.setImageResource(R.drawable.biggerstop);
				}else {
					play.setImageResource(R.drawable.biggerplay);
				} plays = !plays;
			}
		});
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 Intent i = new Intent(post.this,posting.class);
				 i.putExtra("audiofile", mFileName);
				 Log.e(Logs, "putextraintent");
				 startActivity(i);
				 finish();
				 Log.e(Logs, "startactivity");
	          	
			}
			
		});
		final MyCounter timer = new MyCounter(30000, 1000);
		getWindow().setDimAmount((float) 0.80);
		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.BOTTOM);
		delete = (ImageButton) findViewById(R.id.deletecomment);
		delete.setVisibility(View.GONE);
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				play.setVisibility(View.GONE);
				record.setImageResource(R.drawable.recwhite);
				record.setVisibility(View.VISIBLE);
				
			}
			
		});
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

					play.setVisibility(View.VISIBLE);
					
				}
				mStartRecording = !mStartRecording;
			}
				
			
			
		});
		
		ImageButton close =(ImageButton) findViewById(R.id.close);
		close.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
				}
				
			
			
		});
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
				onRecord(false);
				
			}
		
	}
		
		@Override
		protected void onDestroy()
		{
			if (mPlayer != null){
			mPlayer.release();
			}
			if (mRecorder != null){
			mRecorder.release();
			}
			super.onDestroy();
		}
	
}
	
