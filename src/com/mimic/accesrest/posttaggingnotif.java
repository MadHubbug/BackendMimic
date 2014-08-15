package com.mimic.accesrest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class posttaggingnotif extends AsyncTask<String,String,Void> {
	private String usernamepush, types, usernames, actualuser, owner;
	@Override
	protected Void doInBackground(String... params) {
		String post = params[0];
		String user = params[1];
		String type = "tags";
		String follows = params[2];
		


		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			
			nameValuePairs.add(new BasicNameValuePair("\"user\"", user));
			nameValuePairs.add(new BasicNameValuePair("\"post\"", post));
			nameValuePairs.add(new BasicNameValuePair("\"typeofnotif\"", "\""+type+"\""));
			nameValuePairs.add(new BasicNameValuePair("\"follow\"", "\""+follows+"\""));

			DefaultHttpClient httpClient = new DefaultHttpClient();
			Log.d("appache", "httpclient");        
			HttpPost postRequest = new HttpPost("http://mimictheapp.herokuapp.com/notifications/");
			postRequest.addHeader("Authorization", "Basic " + Base64.encodeToString(("madfresco"+":"+"genesis09").getBytes(), Base64.NO_WRAP));
			StringEntity entity = new StringEntity(getQueryJSON(nameValuePairs));
			Log.d("apache",getQueryJSON(nameValuePairs));
			postRequest.setHeader("Content-type","application/json");
			postRequest.setEntity(entity);
			Log.d("apache", "noresponse");  
			HttpResponse response = httpClient.execute(postRequest);
			Log.d("apache", EntityUtils.toString(response.getEntity()));  

			System.out.println("Output from Server .... \n");

			httpClient.getConnectionManager().shutdown();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return null;

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