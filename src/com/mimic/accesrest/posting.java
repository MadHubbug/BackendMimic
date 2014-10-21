package com.mimic.accesrest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
//import com.amazonaws.org.apache.http.util.EntityUtils;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.parse.ParsePush;




public class posting extends SherlockActivity implements OnClickListener{
	private static final String LOG_TAG = "secondpost";
	private MediaPlayer mPlayer = new MediaPlayer();
	private static String mFileName;
	public static AmazonClientManager clientManager = null;
	public static String bucketname = "mimic";
	private static URL newurl = null;
	public static String gg = null;
//	private static EditText et = null;
	private static String Title = null;
	private ImageButton play;
	private boolean mStartPlaying = true;
	private boolean fbclicked = false;
	private String user, username, id;
	private SharedPreferences prefs;
	private MultiAutoCompleteTextView autoComplete;
	private JSONObject data;
	private ArrayList<dropdowndata> dropdowndata;
	private ArrayList<String> usernames;
	private int page = 1;
	private dropdownwebtask task;
	private String fbid;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	private com.stream.aws.Response s = new com.stream.aws.Response(page, fbid);
	private ProgressDialog progress;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	   Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

	}
	
    private void onPlay(boolean start){
		if(start){
			startPlaying();
		} else {
			stopPlaying();
		}
	}
	
    private void mpPrepare(){
    	mPlayer = new MediaPlayer();
    	try{
    		mPlayer.setDataSource(mFileName);
    		mPlayer.prepare();
 
    		
    	}catch (IOException e){
    		Log.e(LOG_TAG, "prepare() failed");
    	}
    }
    
	private void startPlaying(){
			
			try{
				Log.d(LOG_TAG, mFileName);
				mPlayer.reset();
				mPlayer.setDataSource(mFileName);
				mPlayer.prepare();
				mPlayer.start();
		   		mPlayer.setOnCompletionListener(new OnCompletionListener(){

					@Override
					public void onCompletion(MediaPlayer arg0) {
						mStartPlaying = !mStartPlaying;
						play.setImageResource(R.drawable.playbutton);
						mPlayer.reset();
						
					}
	    		}); 
				  } catch(IOException e){
					  Log.e(LOG_TAG, "prepare() failed");
					  
				}
				
		
	}
	

	private void stopPlaying(){
		mPlayer.stop();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.e(LOG_TAG, "gettingintent");
		setContentView(R.layout.post);
		Bundle bundle = getIntent().getExtras();
		mFileName = bundle.getString("audiofile");
		Log.d(LOG_TAG, mFileName);
		((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		SpannableString s = new SpannableString("POST TO YOUR FEED");
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.back);
		username = prefs.getString("username", "madfresco");
		user = prefs.getString("profileid", "0");
		fbid = prefs.getString("fbid", "0");
		ImageView view = (ImageView) findViewById(android.R.id.home);
		final ImageButton tw = (ImageButton) findViewById(R.id.twittershare);
		usernames = new ArrayList<String>();
		mimic(page);
		new bucketwebtask(this).execute();
		
		tw.setOnClickListener(new OnClickListener(){
			boolean l = true;
			@Override
			public void onClick(View arg0) {
				if (l){ 
				tw.setImageResource(R.drawable.twitterclicked);
				
			}else {
				tw.setImageResource(R.drawable.twitternot);
			}
				l = !l;	
			}
			
		});
		final ImageButton fb = (ImageButton) findViewById(R.id.facebookshare);
		fb.setOnClickListener(new OnClickListener(){
			boolean v = true;
			@Override
			public void onClick(View arg0) {
				if(v){
					 Session session = Session.getActiveSession();
					    if (!session.isOpened() || session == null){
							Session.openActiveSession(posting.this, true, null);
							fbclicked=true;
					    Log.d("clicked", "no session");
					    }
					    
					    if (session != null){
					    	Log.d("clicked", "with session");
					    	fb.setImageResource(R.drawable.facebookclicked);
					fbclicked=true;
		}
				
			}else{
				fb.setImageResource(R.drawable.facebooknot);
				fbclicked=false;
				
			}
				v = !v;
			}
			
		});
	
		play =(ImageButton) findViewById(R.id.play);
		autoComplete = (MultiAutoCompleteTextView)findViewById(R.id.description);

		//Create a new Tokenizer which will get text after '@' and terminate on ' '
		        autoComplete.setTokenizer(new Tokenizer() {

		          @Override
		          public CharSequence terminateToken(CharSequence text) {
		            int i = text.length();

		            while (i > 0 && text.charAt(i - 1) == ' ') {
		              i--;
		            }

		            if (i > 0 && text.charAt(i - 1) == ' ') {
		              return text;
		            } else {
		              if (text instanceof Spanned) {
		                SpannableString sp = new SpannableString(text + " ");
		                TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
		                return sp;
		              } else {
		                return text + " ";
		              }
		            }
		          }

		          @Override
		          public int findTokenStart(CharSequence text, int cursor) {
		            int i = cursor;

		            while (i > 0 && text.charAt(i - 1) != '@') {
		              i--;
		            }

		            //Check if token really started with @, else we don't have a valid token
		            if (i < 1 || text.charAt(i - 1) != '@') {
		              return cursor;
		            }

		            return i;
		          }

		          @Override
		          public int findTokenEnd(CharSequence text, int cursor) {
		            int i = cursor;
		            int len = text.length();

		            while (i < len) {
		              if (text.charAt(i) == ' ') {
		                return i;
		              } else {
		                i++;
		              }
		            }

		            return len;
		          }
		        });
		     
		clientManager = new AmazonClientManager(getSharedPreferences(
				"com.mimic.accessrest", Context.MODE_PRIVATE));
		if (posting.clientManager.hasCredentials()) {
			Log.d(LOG_TAG, "has credentials");
		}else {
			Log.d(LOG_TAG, "no credentials");
		}
		play.setOnClickListener(new OnClickListener(){
			 boolean x = mStartPlaying;
			
			@Override
			public void onClick(View v) {
				onPlay(x);
				if (x){
					play.setImageResource(R.drawable.stopbutton);
				} else{
					play.setImageResource(R.drawable.playbutton);
				}
				mStartPlaying = !mStartPlaying;
				
			}
				
			
			
		});
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.cont, menu);
				return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   // Handle item selection

	   switch (item.getItemId()) {
	      case R.id.contin:
	    	  Title = autoComplete.getText().toString();
	    	  if (fbclicked){
					publishStory();
					Log.d(LOG_TAG, "here");
					}
					Log.d(LOG_TAG, fbclicked + " ");

	    	  
	    	  new ValidateCredentialsTask().execute();
	    	  progress = ProgressDialog.show(posting.this, "Posting", "Uploading your mimic now!");
	    	  ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(autoComplete.getWindowToken(), 0);
	      	
	        case android.R.id.home:
	            Log.d("did you", "click?");
	        	this.finish();
	            return true;
	    	default:
		         return super.onOptionsItemSelected(item);
	        
	    }}
	
	@Override
	public void onClick(View v) {
		
		
	}
	
	private class ValidateCredentialsTask extends
	AsyncTask<Void, Void, com.stream.aws.Response> {

		@Override
		protected com.stream.aws.Response doInBackground(Void... arg0) {


			return posting.clientManager.validateCredentials();
		}
		@Override
		protected void onPostExecute(com.stream.aws.Response response) {
			Createbucket bucket = new Createbucket();
			bucket.execute(response);
			Log.d(LOG_TAG, "onpostexecute");
			

		}



	}
	
	public class Createbucket extends
	AsyncTask<com.stream.aws.Response, Void, String> {


		private String data = null;
		@Override
		protected String doInBackground(com.stream.aws.Response... responses) {

			com.stream.aws.Response response = responses[0];
			if (response != null && response.requestWasSuccessful()) {
				Log.d(LOG_TAG, "Validated");
				TransferManager manager = new TransferManager(clientManager.s3());
				Log.d(LOG_TAG, "creating file");
				File filex = new File(mFileName);
				AmazonS3Client s3Client = clientManager.s3();
				try{
//					String firstWord= Title.substring(0, Title.indexOf(" ")); 
					Long timestamp =  (System.currentTimeMillis()/1000);
					String time = timestamp.toString();
					
					final ObjectMetadata metx = new ObjectMetadata();
					metx.addUserMetadata("Content-Type", "audio/x-wav");
					
					Log.d("bucketname", bucketname);
					PutObjectRequest por = new PutObjectRequest(bucketname, "mimic"+time+".wav", filex);
					por.withMetadata(metx);
					por.withCannedAcl(CannedAccessControlList.PublicRead);
					Upload upload = manager.upload(por);

					if (upload.isDone()){
						Log.d(LOG_TAG, "done");

					} 

					Log.d(LOG_TAG, upload.getDescription());
					newurl = s3Client.getUrl(bucketname, "mimic"+time+".wav");
				
					Log.d(LOG_TAG, "creating object");
				
//					Log.d(LOG_TAG, data);

					Log.d(LOG_TAG, "posting data");
				}
				catch (Exception exception){
					Log.d(LOG_TAG, "catching error");
					Log.d("Exception", exception+ " ");
				}


			}
			return null;


		}
		@Override
		protected void onPostExecute(String result){

			
			try{
				apacheHttpClientPost post = new apacheHttpClientPost();
				Log.d(LOG_TAG, "Thisis");
				post.execute("http://mimictheapp.herokuapp.com/posts/");
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
			String x = Integer.toString(0);
			String s = "2014-04-30T17:41:42.470Z";
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				
				nameValuePairs.add(new BasicNameValuePair("\"user\"", user));
				Log.d("newurl", newurl+ " ");
		
				nameValuePairs.add(new BasicNameValuePair("\"posturls\"", "\""+newurl.toString()+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"likes\"", x)); 
				nameValuePairs.add(new BasicNameValuePair("\"commentcounter\"", x));
				nameValuePairs.add(new BasicNameValuePair("\"created\"", "\""+s+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"description\"", "\""+Title+"\""));
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				Log.d(LOG_TAG, "httpclient");        
				HttpPost postRequest = new HttpPost(params[0]);
				StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
				Log.d(LOG_TAG,getQueryJSON(nameValuePairs));
				postRequest.setHeader("Content-type","application/json");
				postRequest.setEntity(entity);	
				postRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
				Log.d(LOG_TAG, "noresponse");  
				HttpResponse response =  (HttpResponse) httpClient.execute(postRequest);
				String t = EntityUtils.toString(response.getEntity());
				try {
					JSONObject obj = new JSONObject(t);
					id = obj.getString("id");
					Log.d("postid", id+" ");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d(LOG_TAG, t);  

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
		protected void onPostExecute(Void params){
			boolean check=false;
			
			try{
				 Pattern tagMatcher = Pattern.compile("@([ء-يA-Za-z0-9_-]+)");
		    	  Matcher m = tagMatcher.matcher(Title);
		    	  
		    	  LinkedList<String> channels = new LinkedList<String>();
		    	  Log.d("title", Title);
//		    	  Boolean x = m.find();
		    	  while (m.find()) { // Find each match in turn; String can't do this.
		    		  String name = m.group(0);
		    		  String s = "user"+name.substring(1);
		    		  Log.d("channels", name);
		    		  Log.d("channels", s);
		    		  channels.add(s);
		    		  posttaggingnotif tag = new posttaggingnotif();
		    		  tag.execute(id, user, name.substring(1));
		    		  check=true;
		    		  // Access a submatch group; String can't do this.
		    	  }
		    	  if (check){
			    		ParsePush push = new ParsePush();
			    		push.setChannels(channels);
			    		try {
							data = new JSONObject("{\"action\": \"com.mimic.accesrest.UPDATE_STATUS\", \"name\": \"Notification\", \"msg\": \""+username+" tagged you in a post\"}");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		push.setData(data);
			    		push.sendInBackground();
		    	  }else{Log.d("no one was tagged", "NO ONE WAS tAGGED");}


			}catch (Exception exception){

			}

		}

		

	}
	
	
		public void mimic(int page){
			String x = String.valueOf(page);
			try{
				task = new dropdownwebtask(posting.this);
				task.execute(x);
				} catch (Exception e){
					task.cancel(true);
					
				}
			
		}
	
	public void setUsers(ArrayList<dropdowndata> dropdowndata) {
		
		this.dropdowndata = dropdowndata;
		int q = dropdowndata.size();
		if (q > 0){
			for (int i = 0; i<q; i++){
			usernames.add(dropdowndata.get(i).getusername());
			}
			if (dropdowndata.get(0).getnext().equals("null")){
				Log.d("no more next page", "no ore");
				 ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.dropdown, R.id.item, usernames);       
				 autoComplete.setAdapter(adapt);
			}else {
				page +=1;
				
			mimic(page);
			}
		}else {
			Log.d("no followers", "no followers");
		}
		
		
	}

	public void setUsers(String bucket) {
		this.bucketname = bucket;
		
	}

	private void publishStory() {
	    Session session = Session.getActiveSession();
	    Log.d("session", session.getState()+ " ");
	 
	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	        Log.d("here?","permisions?");
    
	        return;
	            	        }
	        Log.d("Is it here?", "here");
	        Bundle postParams = new Bundle();
	        postParams.putString("name", Title);
	        postParams.putString("caption", "Download the app now to listen to this mimic!");
	        postParams.putString("link", "https://play.google.com/store/apps/details?id=com.mimic.accesrest");
	        postParams.putString("picture", "http://graph.facebook.com/mimictheapp/picture?type=large");
	        Log.d("Postparams", "params");
	        Request.Callback callback= new Request.Callback() {
	            @Override
	        	public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i("Posting",
	                        "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                Log.d("callback", "callback");
	                if (error != null) {
	                    Toast.makeText(posting.this
	                         .getApplicationContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(posting.this
	                             .getApplicationContext(), 
	                             postId,
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	        Log.d("Executed", "execute");
	        progress.dismiss();
	    	Intent i = new Intent(posting.this,MainActivity.class);
	      	startActivity(i);
	      	Log.d(LOG_TAG, "starting activity");
	      	finish();
	    }
		}
	
	
	private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	        	Log.d("here?","permisions? false");

	        	return false;
	        }
	    }
	    Log.d("here?","permisions? true");

	    return true;
	}	
	
	
	
	
	
}