package com.mimic.accesrest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


public class signupwebtask extends AsyncTask<String, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private MainPage activity;
	private static final String debugtag = "Backgroundtask";
	private String bucket, profileid, username;
	
	
	public signupwebtask(MainPage activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
	
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		
	}
	
	@Override
	protected String doInBackground(String... s) {
		String fbid = s[0];
		try{
			Log.d(debugtag, "Background");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/profilesearch/?search="+fbid , "madfresco", "genesis09");
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		if(result.length() == 0){	
			Log.d("no one here", "new guy");
		}else{
		
		try{
			JSONObject x = new JSONObject(result);
			JSONArray resultj = x.getJSONArray("results");
			JSONObject w = resultj.getJSONObject(0);
			username = w.getString("username");
			profileid = w.getString("profileid");
			bucket = w.getString("bucket");
			Log.d("bucket", bucket + " ");
			Log.d("profileid", profileid + " ");
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		this.activity.setUser(bucket, profileid, username);
		}
	
		}
		
		
	}