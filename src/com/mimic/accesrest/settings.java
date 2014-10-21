package com.mimic.accesrest;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.amazonaws.org.apache.http.annotation.NotThreadSafe;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.android.camera.CropImageIntentBuilder;
import com.facebook.Session;
import com.mimic.accesrest.posting.apacheHttpClientPost;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.parse.PushService;
import com.stream.aws.Response;

public class settings extends SherlockActivity {
	private String user;
	private ArrayList<profiledata> profiledatas;
	private EditText fullname, description, email; 
	private ImageLoader imageloader;
	private ImageView dp;
	private String id;
	private File display;
	private URL displayurl;
	private DisplayImageOptions options;
	private SharedPreferences prefs;
	public static AmazonClientManager clientManager = null;
	private String LOG_TAG = "Settings";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageloader = ImageLoader.getInstance();
		setContentView(R.layout.settings);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		SpannableString s = new SpannableString("EDIT YOUR PROFILE");
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.back);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		user = prefs.getString("profileid", "0");
		fullname = (EditText) findViewById(R.id.fullnamesettings);

		dp = (ImageView) findViewById(R.id.dppicsettings);
		description = (EditText) findViewById(R.id.descriptionsettings);

		email = (EditText) findViewById(R.id.emailaddsettings);
		email.setOnKeyListener(new OnKeyListener() {

	        public boolean onKey(View v, int keyCode, KeyEvent event) {


	                if (event.getAction() == KeyEvent.ACTION_DOWN
	                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
	                    Log.i("event", "captured");

	                    return false;
	                } 
	                else if(event.getAction() == KeyEvent.ACTION_DOWN
	                        && event.getKeyCode() == KeyEvent.KEYCODE_BACK){
	                    Log.i("Back event Trigered","Back event");

	                }

	                return false;    

	            }
	        
	        
	    });
		clientManager = new AmazonClientManager(getSharedPreferences(
				"com.mimic.accessrest", Context.MODE_PRIVATE));
		if (settings.clientManager.hasCredentials()) {
			Log.d(LOG_TAG, "has credentials");
		}else {
			Log.d(LOG_TAG, "no credentials");
		}
		
		 options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).build();
		dp.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivityForResult(MediaStoreUtils.getPickImageIntent(settings.this), 1);
				
			}
			
		});
		
		
	
		LinearLayout Logout = (LinearLayout) findViewById(R.id.LogOutl);
		Logout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (Session.getActiveSession() != null){
				Session.getActiveSession().closeAndClearTokenInformation();
				}
				   SharedPreferences.Editor sa = prefs.edit();
				   sa.clear();
				   sa.commit();
//				   prog.dismiss();
				   Intent intent = new Intent(settings.this, MainPage.class);
				   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
				   startActivity(intent);
				   settings.this.finish();
				
				
				Set <String> set = PushService.getSubscriptions(settings.this);
				String[] x = new String[set.size()];
				set.toArray(x);
				for(int i = 0; i<x.length; i++){
					PushService.unsubscribe(settings.this, x[i]);
				}
			
			}
			
		});
		
		mimic("wow");

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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.settings, menu);
		  	 
		     
		 
		        return super.onCreateOptionsMenu(menu);
		    }
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Log.d("did you", "click?");
        	this.finish();
        case R.id.save:
        	try{
        		if (display!=null){
				new putimage().execute();
        		}
        		apacheHttpClientPost post = new apacheHttpClientPost();
				post.execute();
				
        	}       catch (Exception exception){

			} 
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public void mimic(String x){
		settingswebtask proftask = new settingswebtask(settings.this);
		String query = x+"?format=json";
		
		try{
			
			proftask.execute(query);
		} catch (Exception e){
			proftask.cancel(true);
			Log.d("w","No Mimics");
		}
	}
	public void setUsers(ArrayList<profiledata> profiledata) {
		this.profiledatas = profiledata;
		int q = profiledatas.size();
		if (q>0){
		profiledata x = profiledatas.get(0);
		fullname.setText(x.getfullname());
		description.setText(x.getdescription());
		imageloader.displayImage(x.getprofileurl(), dp);
		id = x.getpostid();
		
		}
	}
	
	public class putimage extends
	AsyncTask<Response, Void, String> {


		private String data = null;
		@Override
		protected String doInBackground(Response... responses) {


	
				
				TransferManager manager = new TransferManager(clientManager.s3());
				Log.d(LOG_TAG, "creating file");
				AmazonS3Client s3Client = clientManager.s3();
				
				try{
					String bucketname = prefs.getString("bucket", "nonehere");
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
	
	
	public class apacheHttpClientPost extends AsyncTask<String,String,Void> {
		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			String x = Integer.toString(0);
			String s = "2014-04-30T17:41:42.470Z";
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				String name = fullname.getText().toString();
				String descrip = description.getText().toString();
				
//				String email = email.getText().toString();
				nameValuePairs.add(new BasicNameValuePair("\"description\"", "\""+descrip+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"fullname\"", "\""+name+"\""));
				if (display!=null){
					nameValuePairs.add(new BasicNameValuePair("\"profilepictureurl\"", "\""+displayurl+"\""));
				}
//				nameValuePairs.add(new BasicNameValuePair("\"\"", "\""+Title+"\""));
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
			
				HttpPut patchRequest = new HttpPut("http://mimictheapp.herokuapp.com/profilesearch/"+id+"/");
				Log.d("id", id+ " ");
				StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
				patchRequest.setHeader("Content-type","application/json");
				patchRequest.setEntity(entity);	
				patchRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
				Log.d("postreq", EntityUtils.toString(patchRequest.getEntity())+ " ");
				HttpResponse Response = httpClient.execute(patchRequest);
				String result= EntityUtils.toString(Response.getEntity());
				Log.d("response", result+" ");
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
		protected void onPostExecute(Void param) {
			settings.this.finish();
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
	

/**
 * HTTP PATCH method.
 * <p>
 * The HTTP PATCH method is defined in <a
 * href="http://tools.ietf.org/html/rfc5789">RF5789</a>: <blockquote> The PATCH
 * method requests that a set of changes described in the request entity be
 * applied to the resource identified by the Request- URI. Differs from the PUT
 * method in the way the server processes the enclosed entity to modify the
 * resource identified by the Request-URI. In a PUT request, the enclosed entity
 * origin server, and the client is requesting that the stored version be
 * replaced. With PATCH, however, the enclosed entity contains a set of
 * instructions describing how a resource currently residing on the origin
 * server should be modified to produce a new version. </blockquote>
 * </p>
 *
 * @since 4.2
 */
	public class HttpPatch extends HttpPost {
	    public static final String METHOD_PATCH = "PATCH";

	    public HttpPatch(final String url) {
	        super(url);
	    }

	    @Override
	    public String getMethod() {
	        return METHOD_PATCH;
	    }
	}
	
	
	
}







