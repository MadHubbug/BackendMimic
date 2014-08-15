package com.mimic.accesrest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.amazonaws.org.apache.http.annotation.NotThreadSafe;
import com.mimic.accesrest.posting.apacheHttpClientPost;
import com.nostra13.universalimageloader.core.ImageLoader;

public class settings extends SherlockActivity {
	private String user;
	private ArrayList<profiledata> profiledatas;
	private EditText fullname, description, email; 
	private ImageLoader imageloader;
	private ImageView dp;
	private String id;
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
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		user = prefs.getString("profileid", "0");
		fullname = (EditText) findViewById(R.id.fullnamesettings);
		dp = (ImageView) findViewById(R.id.dppicsettings);
		description = (EditText) findViewById(R.id.descriptionsettings);
		email = (EditText) findViewById(R.id.emailaddsettings);
		
		mimic("wow");

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
