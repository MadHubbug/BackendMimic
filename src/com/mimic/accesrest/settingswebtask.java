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

public class settingswebtask extends AsyncTask<String, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private settings activity;
	private static final String debugtag = "profileBackgroundtask";
	private JSONArray respobj;
	private JSONObject post;
	private String user;
	
	public settingswebtask (settings activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("username", "madfresco");
		
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		progdialog = ProgressDialog.show(this.activity, "Search", "Looking for your mimics", true, false);
	}
	
	@Override
	protected String doInBackground(String... x) {
		String query = x[0];
		try{
			Log.d(debugtag, "profileBackground");
			String result = Mimicdatahelper.downloadFromServer("http://mimictheapp.herokuapp.com/profiles/", user);
			return result;
		}
		catch (Exception e)
		{
			return new String();
		}
		
		
	}
	
	@Override
	protected void onPostExecute(String result){
		
		ArrayList<profiledata> profiledata= new ArrayList<profiledata>(); 
		
		progdialog.dismiss();
		
						
		try{
			post = JsonObjectCheck(result);
			JSONArray posts = post.getJSONArray("posts");
				Log.d("What is post","post is: "+post);
				String Username = post.getString("username");
				Log.d("What is post","post is: "+Username);
				String fullname = post.getString("fullname");
				String profileurl = post.getString("profilepictureurl");
				Boolean follows = post.getBoolean("follows");
				String postid = post.getString("profileid");
				String description = post.getString("description");
				if (description.equals("null")){
					description = " ";
				}
				boolean owner = post.getBoolean("own");

				
				
				
				
				profiledata.add(new profiledata(fullname, Username, null, null, profileurl, null, follows, postid, description, owner));
				
			
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		
		
		this.activity.setUsers(profiledata);
		}

		
	public JSONObject JsonObjectCheck(String result) throws JSONException{
		try{
			JSONArray x = new JSONArray(result);
			JSONObject w = x.getJSONObject(0);
			
			return w;
		}catch (Exception e){
			JSONObject r = new JSONObject(result);
			return r;
			
		}
		
	}
	
	
	}
	


