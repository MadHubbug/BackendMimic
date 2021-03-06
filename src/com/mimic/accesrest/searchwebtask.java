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

public class searchwebtask extends AsyncTask<String, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private search activity;
	private static final String debugtag = "profileBackgroundtask";
	private String user, password;
	private progdialogs prog;
	public searchwebtask (search activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("username", "madfresco");
		prog = new progdialogs(this.activity);
		password = prefs.getString("password", "genocide212");
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		prog.show();
	}
	
	@Override
	protected String doInBackground(String... q) {
		String query = q[0]; 
		try{
			Log.d(debugtag, "profileBackground");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/profilesearch/?search="+query+"&format=json", user, password);
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		ArrayList<searchdata> searchdata = new ArrayList<searchdata>(); 
		
		
		
		
		if(result.length() == 0){
		
		}
		
		try{
			JSONObject x  = new JSONObject(result);
			JSONArray respobj = x.getJSONArray("results");
			String next = x.getString("next");
			for (int i=0; i<respobj.length(); i++){
			JSONObject returnval = respobj.getJSONObject(i);
			int profid = returnval.getInt("profileid");
			String username = returnval.getString("username");
			boolean follows = returnval.getBoolean("follows");
			String dp = returnval.getString("profilepictureurl");
			String profileurl = returnval.getString("url");
			if (next == "null"){
				Log.d("ending", "end is here");

				searchdata.add(new searchdata(username, dp, follows, profid, profileurl, false));
			}else{

				searchdata.add(new searchdata(username, dp, follows, profid, profileurl, true));
			}
			
			
			}
			
			
			
				
		
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		this.activity.setUsers(searchdata);
		prog.dismiss();
		}
		
		
	}
	


