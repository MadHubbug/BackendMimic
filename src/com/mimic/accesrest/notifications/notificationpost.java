package com.mimic.accesrest.notifications;

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
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParsePush;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;


public class notificationpost extends AsyncTask<String,String,Void> {
	private String usernamepush, types, usernames, actualuser, owner;
	@Override
	protected Void doInBackground(String... params) {
		String post = params[0];
		String user = params[1];
		String type = params[2];
		String username = params[3];
		//actual user 
		actualuser = params[4];
		owner = params[5];
		usernames = username;
		types = type;
		usernamepush = "user"+username;
	

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			
			if (type.equals("likes") || type.equals("comments")){
			nameValuePairs.add(new BasicNameValuePair("\"user\"", user));
			nameValuePairs.add(new BasicNameValuePair("\"post\"", post));
			nameValuePairs.add(new BasicNameValuePair("\"typeofnotif\"", "\""+type+"\""));
			} else if (type.equals("follows")){
				nameValuePairs.add(new BasicNameValuePair("\"user\"", user));
				nameValuePairs.add(new BasicNameValuePair("\"follow\"", "\""+username+"\""));
				nameValuePairs.add(new BasicNameValuePair("\"typeofnotif\"", "\""+type+"\""));
			}

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
	
	
	@Override
	protected void onPostExecute(Void param) {
		if (owner.equals("true")){
			Log.d("I own this", "owner");
		}else {
		ParsePush push = new ParsePush();
		Log.d("channel", usernamepush);
		push.setChannel(usernamepush);
		//String message = null;
		if (types.equals("comments")){
			JSONObject data = null;
			try {
				data = new JSONObject("{\"action\": \"com.mimic.accesrest.UPDATE_STATUS\", \"name\": \"Notification\", \"msg\": \""+actualuser+" left a comment on your post\"}");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		push.setData(data);
		}else if (types.equals("likes")){
			JSONObject data= null;
			try {
				data = new JSONObject("{\"action\": \"com.mimic.accesrest.UPDATE_STATUS\", \"name\": \"Notification\", \"msg\": \""+actualuser+" liked your post\"}");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			push.setData(data);
		}else if (types.equals("follows")){
			JSONObject data = null;
			try {
				data = new JSONObject("{\"action\": \"com.mimic.accesrest.UPDATE_STATUS\", \"name\": \"Notification\", \"msg\": \""+actualuser+" started following you\"}");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			push.setData(data);
		}
		


		push.sendInBackground();
		
		Log.d("pushed", types);
	//	Log.d("pushed", message);
		Log.d("pushed", "pushed");
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

}