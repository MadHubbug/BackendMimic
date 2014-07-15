package com.mimic.accesrest;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import com.facebook.UiLifecycleHelper;
import com.fedorvlasov.lazylist.ImageLoader;
import com.mimic.accesrest.posting.apacheHttpClientPost;
import com.stream.aws.Response;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.preference.PreferenceManager;

public class signup extends SherlockFragmentActivity	{

private final String logtag ="Signuptag";
//private Session.StatusCallback statusCallback = new SessionStatusCallback();

private UiLifecycleHelper uiHelper;		
private boolean isResumed = false;
public static AmazonClientManager clientManager = null;
public int fbid = 0;
private static final String LOG_TAG = "signup";
public ImageView dp;
public SharedPreferences prefs;
public EditText name, email, username, password;
public SharedPreferences.Editor editor;
public String firstname, lastname, namebun, usernamebun, passwordbun, emailbun;
private static byte[] buff = new byte[1024];
private ImageLoader imageloader;
private String imageURL, id;
private ProgressDialog progdialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setTitle("Sign Up");
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();
		Bundle bundle = getIntent().getExtras();
		imageloader=new ImageLoader(this);
		username = (EditText) findViewById(R.id.editText3);
		dp = (ImageView) findViewById(R.id.dppic);
//		namebun = bundle.getString("name");
		fbid = bundle.getInt("fbid");
	    imageURL = "http://graph.facebook.com/"+fbid+"/picture?type=large";
	    
	    Log.d("fbid", fbid+"");
	    imageloader.DisplayImage(imageURL, dp, 200);
		
		
		
		
		
		
		clientManager = new AmazonClientManager(getSharedPreferences(
				"com.mimic.accessrest", Context.MODE_PRIVATE));
		if (signup.clientManager.hasCredentials()) {
			Log.d(LOG_TAG, "has credentials");
		}else {
			Log.d(LOG_TAG, "no credentials");
		}
		
		Button signup = (Button) findViewById(R.id.Signup);
		signup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				usernamebun = username.getText().toString();
				progdialog = ProgressDialog.show(signup.this, "Register", "Registration is in progress", true, false);
				new ValidateCredentialsTask().execute();
				editor.putString("username", usernamebun);
				editor.apply();
				
				Log.d("SHAREDPREFERENCES", prefs.getString("fullname", "there's nothing here"));
				
			
				
			}
		});
	
		
//		uiHelper = new UiLifecycleHelper(this, statusCallback);
//	    uiHelper.onCreate(savedInstanceState); 
//		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//
//		 Session session = Session.getActiveSession();
//	        if (session == null) {
//	            if (savedInstanceState != null) {
//	                session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
//	            }
//	            if (session == null) {
//	                session = new Session(this);
//	            }
//	            Session.setActiveSession(session);
//	            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
//	                session.openForRead(new Session.OpenRequest(this).setCallback(statusCallback));
//	            }
//        updateView();
	}
	private class ValidateCredentialsTask extends
	AsyncTask<Void, Void, Response> {

		@Override
		protected Response doInBackground(Void... arg0) {


			return signup.clientManager.validateCredentials();
		}
		@Override
		protected void onPostExecute(Response response) {
			Createbucket bucket = new Createbucket();
			bucket.execute(response);
			Log.d(LOG_TAG, "onpostexecute");


		}



	}

	

//	 @Override
//	    public void onStart() {
//	        super.onStart();
//	        Session.getActiveSession().addCallback(statusCallback);
//	    }
//
//	    @Override
//	    public void onStop() {
//	        super.onStop();
//	        Session.getActiveSession().removeCallback(statusCallback);
//	    }
//	    
//	    
//	    @Override
//	    protected void onSaveInstanceState(Bundle outState) {
//	        super.onSaveInstanceState(outState);
//	        Session session = Session.getActiveSession();
//	        Session.saveSession(session, outState);
//	    }
//	    public void updateView() {
//	        Session session = Session.getActiveSession();
//	        if (session.isOpened()) {
//	        	 Intent i = new Intent(signup.this,MainActivity.class);
//	        	 i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
//	        	 startActivity(i);
//	        	 finish();
//	            
//	        	        }
//	    }
//
//	   
//	    public class SessionStatusCallback implements Session.StatusCallback {
//	        @Override
//	        public void call(Session session, SessionState state, Exception exception) {
//	            updateView();
//	        }
//	    }
	



	
	

	


	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

	}
	
	public class Createbucket extends
	AsyncTask<Response, Void, String> {


		private String data = null;
		@Override
		protected String doInBackground(Response... responses) {

			Response response = responses[0];
			if (response != null && response.requestWasSuccessful()) {
				
				TransferManager manager = new TransferManager(clientManager.s3());
				AmazonS3Client s3Client = clientManager.s3();
				Log.d(LOG_TAG, "clientmanager");
				try{
					CreateBucketRequest request = new CreateBucketRequest(String.valueOf(fbid)+"mimicbucket");
			        Bucket bucket = s3Client.createBucket(request);
			        editor.putString("bucket", bucket.getName());
					editor.apply();
					Log.d(LOG_TAG,bucket.getName());
					

				
				}
				catch (Exception exception){
					Log.d(logtag, exception.getMessage());
				}


			}
			return null;


		}
		@Override
		protected void onPostExecute(String result){
			try{
				apacheHttpClientPost post = new apacheHttpClientPost();
				Log.d(LOG_TAG, "Thisis");
				post.execute("http://mimictheapp.herokuapp.com/users/");
				Log.d(LOG_TAG, "Executing gg");


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
	
	public class apacheHttpClientPost extends AsyncTask<String,String,Void> {
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String retval = null;
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("\"username\"", "\""+usernamebun+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"email\"", "\""+"mimic@g.com"+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"password\"", "\""+"genocide212"+"\""));
				
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(params[0]);
				StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
				Log.d(LOG_TAG,getQueryJSON(nameValuePairs));
				postRequest.setHeader("Content-type","application/json");
				postRequest.setEntity(entity);
				Log.d(LOG_TAG, "noresponse");  
				postRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
				httpClient.execute(postRequest);
				httpClient.getConnectionManager().shutdown();
				
				try{
					List<NameValuePair> ValuePairs = new ArrayList<NameValuePair>(3);
					ValuePairs.add(new BasicNameValuePair("\"username\"", "\""+usernamebun+"\""));
					ValuePairs.add(new BasicNameValuePair("\"profilepictureurl\"", "\""+imageURL+"\""));
					DefaultHttpClient postclient = new DefaultHttpClient();
					HttpPost request = new HttpPost("http://mimictheapp.herokuapp.com/profiles/");
					Log.d(LOG_TAG, getQueryJSON(ValuePairs));
					StringEntity ent = new StringEntity(getQueryJSON(ValuePairs));
					request.setHeader("Content-type","application/json");
					request.setEntity(ent);
					Log.d(LOG_TAG, "noresponse");  
					request.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
					postclient.execute(request);
					postclient.getConnectionManager().shutdown();
					
				}catch(Exception e){
					
				}
				Log.d("are we even here?", "what");
				HttpGet GetRequest = new HttpGet("http://mimictheapp.heroku.com/profiles/");
				
	             
				try{
					DefaultHttpClient newclient = new DefaultHttpClient();
					GetRequest.addHeader("Authorization", "Basic " + Base64.encodeToString((usernamebun+":"+"genocide212").getBytes(), Base64.NO_WRAP));
					GetRequest.setHeader("Content-type","application/json");
					HttpResponse response = newclient.execute(GetRequest);
					
					Log.d(logtag,"try");
					
					
					HttpEntity ent = response.getEntity();
					
					InputStream ist = ent.getContent();
					ByteArrayOutputStream content = new ByteArrayOutputStream();
					Log.d(logtag, "byte array output stream");

					int readCount = 0;
					Log.d(logtag, "while loop");
					while ((readCount = ist.read(buff)) != -1) {
						content.write(buff, 0, readCount);
					}
					
					retval = new String(content.toByteArray());
					newclient.getConnectionManager().shutdown();
				} catch (Exception e){
					Log.d("catch", "catch");
				} try{
					Log.d("show me", "the money");
					JSONArray array = new JSONArray(retval);
					JSONObject s= array.getJSONObject(0);
					String q = s.getString("profileid");
					id = q;
					Log.d("SHAREDPREFERENCEKEY1", id+"");
					
					editor.putString("profileid", q);
					editor.apply();
					Log.d("SHAREDPREFERENCEKEY2", prefs.getString("profileid", "Nothings here"));
					
					
				}catch (Exception e){
					
				}try {
					List<NameValuePair> followpairs = new ArrayList<NameValuePair>(3);
					followpairs.add(new BasicNameValuePair("\"following\"", id));
					followpairs.add(new BasicNameValuePair("\"follower\"", id));
					DefaultHttpClient httpClients = new DefaultHttpClient();
					HttpPost followreq = new HttpPost("http://mimictheapp.herokuapp.com/follows/");
					StringEntity enti = new StringEntity(getQueryJSON(followpairs));
					Log.d("id", id+"");
					Log.d("followpairs",getQueryJSON(followpairs));
					followreq.setHeader("Content-type","application/json");
					followreq.setEntity(enti);
					Log.d(LOG_TAG, "noresponse");  
					followreq.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
					httpClients.execute(followreq);
					httpClients.getConnectionManager().shutdown();
					progdialog.dismiss();
					Intent i = new Intent(signup.this, MainActivity.class);
					startActivity(i);
					finish();
					
				}catch(Exception e){
					
				}
				
			
				

			} catch (MalformedURLException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}
			return null;
			
			
				

		}

	}



	}


		
		

	