package com.mimic.accesrest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class loginwebtask extends AsyncTask<String, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private Login activity;
	private static final String debugtag = "Backgroundtask";
	private String bucket, profileid;
	
	
	public loginwebtask(Login activity){
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
		String user = s[0];
		String password = s[1];
		try{
			Log.d(debugtag, "Background");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/profiles/?format=json", user, password);
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
			this.activity.alert("Invalid username or password");
		}else{
		
		try{
			JSONArray s= new JSONArray(result);
			JSONObject x = s.getJSONObject(0);
			bucket = x.getString("bucket");
			profileid = x.getString("profileid");
			Log.d("bucket", bucket + " ");
			Log.d("profileid", profileid + " ");
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		this.activity.setUser(bucket, profileid);
		}
	
		}
		
		
	}
	