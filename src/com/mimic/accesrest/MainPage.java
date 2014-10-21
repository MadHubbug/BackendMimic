package com.mimic.accesrest;


import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.LoggingBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;

//import com.mimic.accesrest.signup.SessionStatusCallback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainPage extends SherlockActivity implements OnClickListener{
	
	private static final String LOG_TAG = "MainPage";
	private Intent x;
	private String  name, email, fbid;
	private int id;
	private SharedPreferences prf;
	private SharedPreferences.Editor edit;
	private signupwebtask task;
	
private Session.StatusCallback statusCallback = new Session.StatusCallback() {
    @Override
    public void call(Session session, SessionState state,
            Exception exception) {
        onSessionStateChange(session, state, exception);
    }
	
	
private UiLifecycleHelper uiHelper;
};

private void onSessionStateChange(Session session, SessionState state,
	Exception exception) {
if (state.isOpened()) {
//         
//     Intent intent = new Intent(this, signup.class);
//  this.startActivity(intent);
//	Intent x = new Intent(this, signup.class);
//	x.putExtra("fbid", id);
//startActivity(x);
} 
	
}


@Override
public void onRestoreInstanceState(Bundle savedInstanceState) {
    // Always call the superclass so it can restore the view hierarchy
    super.onRestoreInstanceState(savedInstanceState);
    Log.d("savedInstaneState", "is this null?!");
	startActivity(new Intent(MainPage.this, MainActivity.class));
	
   
}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			Log.d("savedInstaneState", "is this null?!");
		startActivity(new Intent(MainPage.this, MainActivity.class));
		}else{
			Log.d("savedInstanceState", "null");
		}
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		SpannableString s = new SpannableString("mimic");
		prf = PreferenceManager.getDefaultSharedPreferences(this);
		edit = prf.edit();
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.logo);	
		getSupportActionBar().hide();
		Log.d("HEY, WE'RE FROM MAINPAGE", "woot");
		setContentView(R.layout.mainpagelanding);
		Button signup = (Button) findViewById(R.id.Signuplanding);

		
		signup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainPage.this, signupemail.class));
				
			}
			
		});
		Button login = (Button) findViewById(R.id.LoginButton);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainPage.this, Login.class));
				
			}
		});
		
		
		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		    if (session == null) {
		        if (savedInstanceState != null) {
		            session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
		        }
		        if (session == null) {
		            session = new Session(this);
		        }
		        Session.setActiveSession(session);
		    }
	   updateView();
		    Update();
//			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
//			   SharedPreferences.Editor sa = prefs.edit();
//			   sa.clear();
//			   sa.commit();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
	}
	
	
	private void Update(){
		Boolean logged = prf.getBoolean("logged", false);
		if (logged){
			 Intent i = new Intent(MainPage.this,MainActivity.class);
        	 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
        	 startActivity(i);
        	 this.finish();
		} else{
			Log.d("not logged", "nope");
		}
		
	}
	private void updateView() {
		Session session = Session.getActiveSession();
        if (session.isOpened()) {
        	 Intent i = new Intent(MainPage.this,MainActivity.class);
        	 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
        	 startActivity(i);
        	 finish();
        }
	}

	 @Override
	    public void onStart() {
	        super.onStart();
	        Session.getActiveSession().addCallback(statusCallback);
	    }

	    @Override
	    public void onStop() {
	        super.onStop();
	        Session.getActiveSession().removeCallback(statusCallback);
	    }

	    
	@Override
	public void onClick(View v) {
		//if(v.getId() == R.id.Signuplanding){
			//startActivity(new Intent(MainPage.this, signup.class));
			//finish();
	//	}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	    if (Session.getActiveSession() != null || Session.getActiveSession().isOpened()){
	    	OpenRequest op = new Session.OpenRequest(MainPage.this);
	    	op.setCallback(null);
	    	 List<String> permissions = new ArrayList<String>();
//	         permissions.add("publish_stream");
//	         permissions.add("publish_actions");
	         permissions.add("email");
	         Session s = Session.getActiveSession();
	         s.requestNewReadPermissions(new Session.NewPermissionsRequest(MainPage.this, permissions));
	         Session.setActiveSession(s);
	         
	         
	    	
	     	new Request(
	     			s,
	     			"/me",
	     			null,
	     			HttpMethod.GET,
	     			new Request.Callback() {
	     			public void onCompleted(Response response) {
	     			try{
	     			Response x = response;
	     			JSONObject fbobj = response.getGraphObject().getInnerJSONObject();
	     			fbid= fbobj.getString("id");
	     			name = fbobj.getString("name");
	     			email = fbobj.getString("email");
	     			Log.d(LOG_TAG, "oncomplete request/add intent function");
	     			Log.d("fbobj", fbobj + " ");
	     			Log.d("response", response + "");
	     			Log.d("email", email);
	     			Log.d("fbid", fbid+ " ");
	     			Log.d("name", name);
	     			task = new signupwebtask(MainPage.this);
	     			task.execute(fbid);


	     			}catch (JSONException e){

	     			e.printStackTrace();
	     			}
	     			}

	     			}
	     			).executeAsync();
	    	
	    	
  
	    			
	    			
	                
	            }
	  }
	public void addintent(String i, String b, String c){
		x = new Intent(MainPage.this, signup.class);
		x.putExtra("fbid", i);
		Log.d(LOG_TAG, i+"");
		x.putExtra("name", b);
		x.putExtra("email", c);
		startActivity(x);
		Log.d(LOG_TAG, "Activity started with bundle");

		
		
	}
	
	public void setUser(String bucket, String profileid, String username) {
		Log.d("bucket", bucket+ " ");
		Log.d("bucket", profileid + " ");
		Log.d("bucket", username + " ");
			if (bucket==null){
				addintent(fbid, name, email);
				
			}else{

				edit.putString("bucket", bucket);
				edit.putString("profileid", profileid);
				edit.putString("username", username);
				edit.putString("password", "genocide212");
				edit.putString("fbid", fbid);
				edit.commit();
				Log.d("setuserareweevenhere", prf.getString("username", "nothing here") + ":" +prf.getString("profileid", "nothing here") + ":"+ prf.getString("password", "Nothinghere"));
				
				this.finish();
				startActivity(new Intent(MainPage.this, MainActivity.class));
				
				
			}
			
		}
	
	
	
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Session session = Session.getActiveSession();
//        Session.saveSession(session, outState);
    }




    
    
}
	