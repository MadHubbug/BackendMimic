package com.mimic.accesrest;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class bucketwebtask extends AsyncTask<Void, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private posting activity;
	private static final String debugtag = "profileBackgroundtask";
	private String user, password, bucket;
	public bucketwebtask (posting activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("username", "madfresco");
		password = prefs.getString("password", "genocide212");
		Log.d("password", password);
		Log.d("user", user);
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
//		progdialog = ProgressDialog.show(this.activity, "Search", "Looking for your mimics", true, false);
	}
	
	@Override
	protected String doInBackground(Void... q) {
//		String query = q[0]; 
		try{
			Log.d(debugtag, "profileBackground");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/profiles", user, password);
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
	
		
//		progdialog.dismiss();
		
		
		if(result.length() == 0){
		
		}
		
		try{
			JSONArray x  = new JSONArray(result);
			JSONObject respobj = x.getJSONObject(0);
			bucket = respobj.getString("bucket");
			
			}
			
				
		
			
			 catch (JSONException e){
			
				e.printStackTrace();
			}
			
		this.activity.setUsers(bucket);
		}
		
		
	}
	


