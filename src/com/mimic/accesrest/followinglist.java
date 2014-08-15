package com.mimic.accesrest;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;


public class followinglist extends SherlockActivity{
	
	
	private LayoutInflater layoutinflater;
	private ListView followslist;
	private ArrayList<searchdata> searchdata;
	private String Label, postid;
	private followwebtask task;
	public Typeface type;
	private String q;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.followinglist);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		Bundle bundle = getIntent().getExtras();
		String label = bundle.getString("label");
		if (label.trim().equals("Following")){
			 
			q = "follower";
		}else if (label.trim().equals("Follower")){
			q = "following";
			
		}
		postid = bundle.getString("postids");
		SpannableString s = new SpannableString(q.toUpperCase());
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		Label = label.toLowerCase();
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.back);
		this.followslist = (ListView) findViewById(R.id.followlist);
		this.layoutinflater = LayoutInflater.from(this);
		mimic();
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Log.d("did you", "click?");
        	this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	public void mimic(){
		
		try{
			task = new followwebtask(followinglist.this);
			task.execute(postid, Label);
			} catch (Exception e){
				task.cancel(true);
				
			}
		
	}
	
	
	 public static class FollowHolder {
		 public ImageView dp;
		 public ImageButton follow;
		 public TextView username;
		 public searchdata searchdata;
		 public String profileurl;
		 public int profileid;

	 }
	 
	
	public void setUsers(ArrayList<searchdata> searchdata) {
		this.searchdata = searchdata;
		this.followslist.setAdapter(new followadapter(this,this.layoutinflater, this.searchdata));
	}

}
