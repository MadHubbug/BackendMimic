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
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;


public class followinglist extends SherlockActivity implements OnScrollListener{
	
	
	private LayoutInflater layoutinflater;
	private ListView followslist;
	private ArrayList<searchdata> searchdata;
	private String Label, postid;
	private followwebtask task;
	public Typeface type;
	private followadapter followadapter;
	private String q;
	private boolean stop = true;
	private boolean d = true;
	private int currentpage = 1;
	
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
		followslist.setOnScrollListener(this);
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
			task.execute(postid, Label, "1");
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

		int s = searchdata.size();
		if (s>0){
			searchdata r = searchdata.get(0);
			if (stop){
			if (r.getlast() == false){
				if (this.searchdata == null){
					this.searchdata= searchdata;
					followadapter = new followadapter(this,this.layoutinflater, this.searchdata);;
					this.followslist.setAdapter(followadapter);
//					mimicadapter.checker = true;
					
					d=false;
				}else{
				for (int i = 0; i<searchdata.size(); i++){
					this.searchdata.add(searchdata.get(i));
//					mimicadapter.checker = true;
					followadapter.notifyDataSetChanged();
					Log.d("counts", "mimic count:"+ searchdata.size());
					
				}
				}
				stop = false;
			}else{
			if (d){
			
				this.searchdata = searchdata;
				followadapter = new followadapter(this,this.layoutinflater, this.searchdata);
				this.followslist.setAdapter(followadapter);
				d=false;
//				mimicadapter.checker = true;
				Log.d("counts", "mimic count:"+ searchdata.size());
			
			}else{
				
				for (int i = 0; i<searchdata.size(); i++){
					this.searchdata.add(searchdata.get(i));
					followadapter.notifyDataSetChanged();
					Log.d("counts", "mimic count:"+ searchdata.size());
//					mimicadapter.checker = true;
				}
				
			}
	    }
			} else{
				Log.d("stopped", "stopped");
			}
	}

	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
	        if (followslist.getLastVisiblePosition() >= followslist.getCount() - 7 - 1) {
	            currentpage = currentpage + 1;
	            String page = String.valueOf(currentpage);
	            //load more list items:
	            new followwebtask(followinglist.this).execute(postid, Label, page);
	        }
	    }
	
		
	}
}
