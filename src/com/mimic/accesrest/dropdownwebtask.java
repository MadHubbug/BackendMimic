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

public class dropdownwebtask extends AsyncTask<String, Integer, String>{


	private Context context;
	private posting activity;
	private static final String debugtag = "profileBackgroundtask";
	private String user, userid, password;
	public dropdownwebtask (posting activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("username", "madfresco");
		userid = prefs.getString("profileid", "0");
		password = prefs.getString("password", "genocide212");
		
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();

	}
	
	@Override
	protected String doInBackground(String... query) {
		String q = query[0];
		try{
			Log.d(debugtag, "profileBackground");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/follows/?follower="+userid+"&format=json&page="+ q, user, password);
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		ArrayList<dropdowndata> dropdowndata= new ArrayList<dropdowndata>(); 
		if(result.length() == 0){
		
		}
		
		try{
			JSONObject s = new JSONObject(result);
			
			String next = s.getString("next");
		
				JSONArray respobj = s.getJSONArray("results");
				for (int i=0; i<respobj.length(); i++){
					JSONObject returnval = respobj.getJSONObject(i);
					JSONObject follow = returnval.getJSONObject("following");
					String username= follow.getString("username");
		
			dropdowndata.add(new dropdowndata(next, username));
			
			}
			
				
		
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		this.activity.setUsers(dropdowndata);
		}
		
		
	}
	


