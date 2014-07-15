package com.mimic.accesrest;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class Mimicdatahelper {
	
	private static String MimicDataUserURL;
	private static final int HTTP_STATUS_OK = 200;
	private static byte[] buff = new byte[1024];
	private static final String logtag = "mimicdatahelper";
	
	
	public static class ApiException extends Exception {
		private static final long serialVersionUID = 1L;

		public ApiException (String msg)
		{
			super (msg);
		}

		public ApiException (String msg, Throwable thr)
		{
		 super (msg, thr);
		}
	}
	
	protected static synchronized String downloadFromServer(String url, String username)throws ApiException{
	
		String retval = null;
		
		Log.d(logtag, "Fetching ");
		String user = username;
		MimicDataUserURL = url;
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(MimicDataUserURL);               
		request.addHeader("Authorization", "Basic " + Base64.encodeToString((user+":"+"genocide212").getBytes(), Base64.NO_WRAP));
		
		try{
			
			HttpResponse response = client.execute(request);
			StatusLine status = response.getStatusLine();
			Log.d(logtag,"try");
			
			if(status.getStatusCode()!= HTTP_STATUS_OK){
			
			}
		
			HttpEntity entity = response.getEntity();
			
			InputStream ist = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			Log.d(logtag, "byte array output stream");

			int readCount = 0;
			Log.d(logtag, "while loop");
			while ((readCount = ist.read(buff)) != -1) {
				content.write(buff, 0, readCount);
			}
			
			retval = new String(content.toByteArray());
			Log.d(logtag, "return value" + retval);
			
	}
		catch (Exception e){
			Log.d(logtag, "exception catch");
			
			throw new ApiException("Problem Connecting to Server");
		}
		
		
		Log.d(logtag, "return value");
		return retval;
		
	}
	
	
	
}
