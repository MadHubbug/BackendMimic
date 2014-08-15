package com.mimic.accesrest;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

public class initfollow extends SherlockActivity{

	private ArrayList<searchdata> search;
	private searchadapter searchadapter;
	private ListView searchlist;
	private LayoutInflater layoutinflater;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.followinit);
		getWindow().setDimAmount((float) 0.70);
//		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.CENTER);
		this.layoutinflater = LayoutInflater.from(this);
		mimic();
		
		searchlist = (ListView) findViewById(R.id.initlistview);
		ImageButton next = (ImageButton) findViewById(R.id.initnext);
		next.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				initfollow.this.finish();
				
			}
			
		});
		
		
	}

	public void setUsers(ArrayList<searchdata> searchdata) {
		this.search= searchdata;
		searchadapter = new searchadapter(this,this.layoutinflater, this.search);
		this.searchlist.setAdapter(searchadapter);
		
	}
	 public void mimic(){
			initfollowwebtask mimictask = new initfollowwebtask(initfollow.this);
			try{
				mimictask.execute();
				} catch (Exception e){
					mimictask.cancel(true);
					
				}
			
		}
	 
	
	
}
