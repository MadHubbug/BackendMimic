package com.mimic.accesrest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.mimic.accesrest.notifications.notificationpost;
import com.mimic.accesrest.posting.Createbucket;
import com.stream.aws.Response;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class liking extends AsyncTask<String,String,Response> {
	public String user, post, username, actualuser, owns;
	@Override
	protected Response doInBackground(String... params) {
		user = params[1];
		post = params[0];
		username = params[2];
		actualuser = params[3];
		owns = params[4];

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			
			
			nameValuePairs.add(new BasicNameValuePair("\"user\"", user));
			nameValuePairs.add(new BasicNameValuePair("\"post\"", post));
			

			DefaultHttpClient httpClient = new DefaultHttpClient();
			Log.d("appache", "httpclient");        
			HttpPost postRequest = new HttpPost("http://mimictheapp.herokuapp.com/likes/");
			postRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
			StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
			Log.d("apache",getQueryJSON(nameValuePairs));
			postRequest.setHeader("Content-type","application/json");
			postRequest.setEntity(entity);
			Log.d("apache", "noresponse");  
			httpClient.execute(postRequest);
			Log.d("apache", "noresponse");  

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
		notif.execute(post, user, "likes", username, actualuser, owns);

		

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

}