package com.mimic.accesrest;

import java.util.ArrayList;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;

public class search extends SherlockActivity{

	private TextView txtQuery;
	private ArrayList<searchdata> search;
	private searchadapter searchadapter;
	private ListView searchlist;
	private LayoutInflater layoutinflater;
	private ArrayList<MimicData> mimic;
	private MimicAdapter mimicadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setTitle("Search");
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.searchlist = (ListView) findViewById(R.id.searchlist);
		this.layoutinflater = LayoutInflater.from(this);
        handleIntent(getIntent());
	}
	
	
	 @Override
	    protected void onNewIntent(Intent intent) {
	        setIntent(intent);
	        handleIntent(intent);
	    }
	 
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case android.R.id.home:
	            this.finish();
	            return true;
	        }
	        return super.onOptionsItemSelected(item);

		}
		
	  private void handleIntent(Intent intent) {
		  if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	            final String query = intent.getStringExtra(SearchManager.QUERY);
	            searchusers(query);
	            final ImageButton usersearch = (ImageButton) findViewById(R.id.usersearch);
	            final ImageButton mimicsearch = (ImageButton) findViewById(R.id.mimicsearch);
	            usersearch.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						searchusers(query);
						mimicsearch.setImageResource(R.drawable.mimicsearch);
						usersearch.setImageResource(R.drawable.usersearchclickede);
					}
	            	
	            });
	            
	    
	            mimicsearch.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						mimicsearch.setImageResource(R.drawable.mimicclickedsearch);
						usersearch.setImageResource(R.drawable.usersearch);
						searchmimics(query);
					}
	            	
	            });
	            
	 
	        }
	 
	    }


	public void setUsers(ArrayList<searchdata> searchdata) {
		this.search= searchdata;
		searchadapter = new searchadapter(this,this.layoutinflater, this.search);
		this.searchlist.setAdapter(searchadapter);
		
	}
	
	public void setMimics(ArrayList<MimicData> mimicdata){
		this.mimic= mimicdata;
		mimicadapter = new MimicAdapter(this, this.layoutinflater, this.mimic);
		this.searchlist.setAdapter(mimicadapter);
	}
	
	 public static class MyHolder {
		 public ImageButton dp, follow;
		 public TextView username;
		 public searchdata searchdata;
		 public String profileurl;
		 public int profileid;

	 }
	
	 public void searchusers(String a){
			searchwebtask mimictask = new searchwebtask(search.this);
			try{
				mimictask.execute(a);
				} catch (Exception e){
					mimictask.cancel(true);
					
				}
			
		}
	 
	 public void searchmimics(String a){
			searchmimicwebtask mimictask = new searchmimicwebtask(search.this);
			try{
				mimictask.execute(a);
				} catch (Exception e){
					mimictask.cancel(true);
					
				}
			
		}

	
}
