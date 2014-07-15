package com.mimic.accesrest;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.LoggingBehavior;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
//import com.mimic.accesrest.signup.SessionStatusCallback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainPage extends Activity implements OnClickListener{
	
	private static final String LOG_TAG = "MainPage";
	private Intent x;
	private String  name;
	private int id;
	
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


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		
		getActionBar().hide();
		setContentView(R.layout.mainpagelanding);
		Button signup = (Button) findViewById(R.id.Signuplanding);
		signup.setOnClickListener(this);
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
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); 
	//   SharedPreferences.Editor.clear();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
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
	    	
	    	
	    	new Request(
	    		    Session.getActiveSession(),
	    		    "/me",
	    		    null,
	    		    HttpMethod.GET,
	    		    new Request.Callback() {
	    		        public void onCompleted(Response response) {
	    		        	try{
	    		        		Response x = response;
	    		        		JSONObject fbobj = response.getGraphObject().getInnerJSONObject();
	    		        		Log.d(LOG_TAG, fbobj.getString("name"));
	    		    				id= fbobj.getInt("id");
	    		    				Log.d(LOG_TAG, id+"");
	    		    				name = fbobj.getString("name");
	    		    				

	    		    				
	    			                Log.d(LOG_TAG, "oncomplete request/add intent function");
	    		    				
	    			                addintent(id, name);
	    			    			
	    		    				
	    		    			}catch (JSONException e){
	    		    				
	    		    				e.printStackTrace();
	    		    			}
	    		        }
	    		        
	    		    }
	    		).executeAsync();
	    	
	    	
  
	    			
	    			
	                
	            }
	  }
	
	public void addintent(Integer i, String b){
		x = new Intent(MainPage.this, signup.class);
		x.putExtra("fbid", i);
		Log.d(LOG_TAG, i+"");
		x.putExtra("name", b);
		startActivity(x);
		Log.d(LOG_TAG, "Activity started with bundle");

		
		
	}
	
	
	
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }


}
	