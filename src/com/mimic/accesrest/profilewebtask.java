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

public class profilewebtask extends AsyncTask<String, Integer, String>{

	private ProgressDialog progdialog;
	private Context context;
	private profile activity;
	private static final String debugtag = "profileBackgroundtask";
	private JSONArray respobj;
	private JSONObject post;
	private String user;
	private progdialogs prog;
	
	public profilewebtask (profile activity){
		super();
		this.activity = activity; 
		this.context = this.activity.getApplicationContext();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("username", "madfresco");
		prog = new progdialogs(this.activity);
		
	}
	
	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		prog.show();
//		progdialog = ProgressDialog.show(this.activity, "Search", "Looking for your mimics", true, false);
	}
	
	@Override
	protected String doInBackground(String... x) {
		String query = x[0];
		try{
			Log.d(debugtag, "profileBackground");
			String result = Mimicdatahelper.downloadFromServer(query, user);
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
		ArrayList<MimicData> mimicdata = new ArrayList<MimicData>();
		prog.dismiss();
		
		
		if(result.length() == 0){
			this.activity.alert("Nada denada");
		}
		
		try{
			post = JsonObjectCheck(result);
			JSONArray posts = post.getJSONArray("posts");
				for (int x=0; x<posts.length(); x++){
				JSONObject l = posts.getJSONObject(x);
				int comments = l.getInt("commentscount");
				int likes = l.getInt("likescount");
				String username = post.getString("username");
				String dpurl = null;
				String url = l.getString("url");
				String posturl = l.getString("posturls");
				String description = l.getString("description");
				boolean owner = l.getBoolean("own");
				Boolean bool = l.getBoolean("favourites");
				int postid = l.getInt("id");
				String timestamp = l.getString("time");
				mimicdata.add(new MimicData(username, dpurl, url, postid, likes, comments, posturl, description, bool, timestamp, null, true, owner));
			}
				Log.d("What is post","post is: "+post);
				String Username = post.getString("username");
				Log.d("What is post","post is: "+Username);
				int followers = post.getInt("followingcount");
				int followings = post.getInt("followerscount");
				int postcount = post.getInt("postcount");
				String fullname = post.getString("fullname");
				String profileurl = post.getString("profilepictureurl");
				Boolean follows = post.getBoolean("follows");
				String postid = post.getString("profileid");
				boolean owner = post.getBoolean("own");
				String description = post.getString("description");
				if (description.equals("null")) {
					description = " ";
				}

				
				
				
				
				profiledata.add(new profiledata(fullname, Username, followings, followers, profileurl, postcount, follows, postid, description,owner));
				
			
			
			} catch (JSONException e){
			
				e.printStackTrace();
			}
		
		this.activity.setUser(mimicdata);
		this.activity.setUsers(profiledata);
		}

		
	public JSONObject JsonObjectCheck(String result) throws JSONException{
		try{
			JSONArray x = new JSONArray(result);
			JSONObject w = x.getJSONObject(0);
			
			return w;
		}catch (Exception e){
			try{JSONObject r = new JSONObject(result);
			JSONArray s = r.getJSONArray("results");
			JSONObject m = s.getJSONObject(0);
			
			return m;
			} catch(Exception s){
				JSONObject r = new JSONObject(result);
				return r;
			}
			
		}
		
	}
	
	
	}
	


