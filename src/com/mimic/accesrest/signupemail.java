package com.mimic.accesrest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.android.camera.CropImageIntentBuilder;


import com.mimic.accesrest.posting.apacheHttpClientPost;
import com.mimic.accesrest.signup.Createbucket;
import com.mimic.accesrest.signup.Register;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.stream.aws.Response;

public class signupemail extends SherlockActivity {

	private Uri currImageURI;
	private ImageLoader imageloader;
	private ImageView dp;
	private ProgressDialog progdialog;
	private String usernamestr, fullnamestr, passwordstr, emailstr, bucketname, id;
	private int responseid;
	private EditText username;
	private SharedPreferences prefs;
	public SharedPreferences.Editor editor;
	public static AmazonClientManager clientManager = null;
	private String LOG_TAG = "SIGNUPEMAIL";
	private static byte[] buff = new byte[1024];
	private DisplayImageOptions options;
	private File display;
	private URL displayurl;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signupwithemail);
		
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setTitle("SIGN UP");
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.back);
		final EditText fullname = (EditText) findViewById(R.id.fullnamesettings);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
		ImageLoader.getInstance().init(config);
		imageloader = ImageLoader.getInstance();
		 username = (EditText) findViewById(R.id.usernamesignup);
		 prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();
		InputFilter filter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO 
		                for (int i = start; i < end; i++) { 
		                        if (!Character.isLetterOrDigit(source.charAt(i))) { 
		                                return ""; 
		                        } 
		                } 
		                return null; 
		        }
			
			
		 
	}; 

	username.setFilters(new InputFilter[]{filter}); 

		 options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).build();
		final EditText password = (EditText) findViewById(R.id.passwordsignup);
		final EditText email = (EditText) findViewById(R.id.emailsignup);
		password.setFilters(new InputFilter[]{filter});
		
		dp = (ImageView) findViewById(R.id.dppic);
		dp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivityForResult(MediaStoreUtils.getPickImageIntent(signupemail.this), 1);
				
			}
			
		});
		
		clientManager = new AmazonClientManager(getSharedPreferences(
				"com.mimic.accessrest", Context.MODE_PRIVATE));
		if (signupemail.clientManager.hasCredentials()) {
			Log.d(LOG_TAG, "has credentials");
		}else {
			Log.d(LOG_TAG, "no credentials");
		}
		
		Button signup = (Button) findViewById(R.id.Signup);
		signup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
			if (fullname.length() == 0){
				fullname.setError("This entry can not be empty");
			}else if (username.getText().toString().contains(" ")) {
			     username.setError("No Spaces Allowed");
			}
			else if (email.length()<4){
				email.setError("Invalid email");
			}
			else if (username.length()<8){
				username.setError("Username should have at least more than 8 characters");
			}else if (password.length()<8){
				password.setError("Username should have at least more than 8 characters");
			} else{
				usernamestr = username.getText().toString();
				passwordstr = password.getText().toString();
				emailstr = email.getText().toString();
				fullnamestr = fullname.getText().toString();
				progdialog = ProgressDialog.show(signupemail.this, "Register", "Registration is in progress", true, false);
				apacheHttpClientPost post = new apacheHttpClientPost();
				Log.d(LOG_TAG, "Thisis");
				post.execute("http://mimictheapp.herokuapp.com/users/");
				Log.d(LOG_TAG, "Executing gg");

				
			}
				
			}
			
		});
		
		
		
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		   super.onActivityResult(requestCode, resultCode, data);
        File croppedImageFile = new File(getFilesDir(), "test.png");

        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            // When the user is done picking a picture, let's start the CropImage Activity,
            // setting the output image file and size to 200x200 pixels square.
            Uri croppedImage = Uri.fromFile(croppedImageFile);
            
            CropImageIntentBuilder cropImage = new CropImageIntentBuilder(200, 200, croppedImage);
            cropImage.setCircleCrop(true);
            cropImage.setSourceImage(data.getData());
            
             
            startActivityForResult(cropImage.getIntent(this), 2);
        } else if ((requestCode == 2) && (resultCode == RESULT_OK)) {
        	
        	display = croppedImageFile.getAbsoluteFile();
        	imageloader.displayImage("file://"+croppedImageFile.getAbsolutePath(), dp, options);
        	Log.d("What", "imageloading");
        	
        }
    }
	
	 
	public class apacheHttpClientPost extends AsyncTask<String,String,Void> {
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
		
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("\"username\"", "\""+usernamestr+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"email\"", "\""+emailstr+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"password\"", "\""+passwordstr+"\""));
				
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(params[0]);
				StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
				Log.d("signupwithemail",getQueryJSON(nameValuePairs));
				postRequest.setHeader("Content-type","application/json");
				postRequest.setEntity(entity);
				Log.d("signupwithemail", "noresponse");  
				postRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
				HttpResponse response = httpClient.execute(postRequest);
				httpClient.getConnectionManager().shutdown();

					Log.d("Log'd", response.getStatusLine().getStatusCode()+" ");
					responseid = response.getStatusLine().getStatusCode();

				
			
				

			} catch (MalformedURLException e) {
				progdialog.dismiss();
				   username.setError("No Spaces Allowed");
				     Toast.makeText(signupemail.this, "Username already used", 5000).show();
				e.printStackTrace();
				
			} catch (IOException e) {
				
				progdialog.dismiss();
				   username.setError("No Spaces Allowed");
				     Toast.makeText(signupemail.this, "Username already used", 5000).show();
				e.printStackTrace();

			}
			return null;
			
			
				

		}
		@Override
		protected void onPostExecute(Void param) {
		
			if(responseid == 400){
				progdialog.dismiss();
				username.setError("Username used already");
				
			}else{
				editor.putString("username", usernamestr);
				editor.putString("password", passwordstr);
				editor.apply();
				new ValidateCredentialsTask().execute();
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
	
	
	
	private class ValidateCredentialsTask extends
	AsyncTask<Void, Void, Response> {

		@Override
		protected Response doInBackground(Void... arg0) {


			return signupemail.clientManager.validateCredentials();
		}
		@Override
		protected void onPostExecute(Response response) {
			Createbucket bucket = new Createbucket();
			bucket.execute(response);
			Log.d("signupw/email", "onpostexecute");


		}



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
				Log.d("signupemail", "clientmanager");
				try{
					CreateBucketRequest request = new CreateBucketRequest(usernamestr.toLowerCase()+"mimicbucket");
			        Bucket bucket = s3Client.createBucket(request);
			        editor.putString("bucket", bucket.getName());
					editor.apply();
					Log.d("signupemail",bucket.getName());
					bucketname = bucket.getName();

				
				}
				catch (Exception exception){
					Log.d("fromsignupemail", exception.getMessage());
				}

				
			}
			return null;


		}
		@Override
		protected void onPostExecute(String result){
			try{
				if (display!=null){
				new putimage().execute();
				}
			new Register().execute();

			}catch (Exception exception){

			}

			
		


			

			}

		}
	
	public class putimage extends
	AsyncTask<Response, Void, String> {


		private String data = null;
		@Override
		protected String doInBackground(Response... responses) {


	
				Log.d(LOG_TAG, "Validated");
				TransferManager manager = new TransferManager(clientManager.s3());
				Log.d(LOG_TAG, "creating file");
				AmazonS3Client s3Client = clientManager.s3();
				
				try{

					Log.d("buket when uploading", bucketname);
					PutObjectRequest por = new PutObjectRequest(bucketname, "displaypic.png", display);
					por.withCannedAcl(CannedAccessControlList.PublicRead);
					Upload upload = manager.upload(por);

					if (upload.isDone()){
						Log.d(LOG_TAG, "done");

					} 

					Log.d(LOG_TAG, upload.getDescription());
					displayurl= s3Client.getUrl(bucketname, "displaypic.png");
				
					Log.d(LOG_TAG, "creating object");
				
//					Log.d(LOG_TAG, data);

					Log.d(LOG_TAG, "posting data");
				}
				catch (Exception exception){
					Log.d(LOG_TAG, "catching error");
					Log.d("Exception", exception+ " ");
				}


			
			return null;


		}
		



	}
	
	public class Register extends AsyncTask<Void, Void, Void>{
		String retval = null;
		@Override
		protected Void doInBackground(Void... params) {
			try{
			List<NameValuePair> ValuePairs = new ArrayList<NameValuePair>(3);
			ValuePairs.add(new BasicNameValuePair("\"username\"", "\""+usernamestr+"\""));
			ValuePairs.add(new BasicNameValuePair("\"profilepictureurl\"", "\""+displayurl+"\""));
			ValuePairs.add(new BasicNameValuePair("\"email\"", "\""+emailstr+"\""));
			ValuePairs.add(new BasicNameValuePair("\"fullname\"", "\""+fullnamestr+"\""));
			ValuePairs.add(new BasicNameValuePair("\"bucket\"", "\""+bucketname+"\""));
			ValuePairs.add(new BasicNameValuePair("\"fbid\"", "\""+null+"\""));
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
			GetRequest.addHeader("Authorization", "Basic " + Base64.encodeToString((usernamestr+":"+passwordstr).getBytes(), Base64.NO_WRAP));
			GetRequest.setHeader("Content-type","application/json");
			HttpResponse response = newclient.execute(GetRequest);
			
			Log.d("signupemail","try");
			
			
			HttpEntity ent = response.getEntity();
			
			InputStream ist = ent.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			Log.d("signupemail", "byte array output stream");

			int readCount = 0;
			Log.d("signupemail", "while loop");
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
			HttpPost followreq = new HttpPost("http://mimictheapp.herokuapp.com/postfollows/");
			StringEntity enti = new StringEntity(getQueryJSON(followpairs));
			Log.d("id", id+"");
			Log.d("followpairs",getQueryJSON(followpairs));
			followreq.setHeader("Content-type","application/json");
			followreq.setEntity(enti);
			Log.d("signupemail", "noresponse");  
			followreq.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
			httpClients.execute(followreq);
			httpClients.getConnectionManager().shutdown();
			progdialog.dismiss();
			Intent i = new Intent(signupemail.this, MainActivity.class);
			startActivity(i);
			finish();
			
		}catch(Exception e){
			
		}
		return null;
		}
		
	}



	}

