package com.mimic.accesrest;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class hash extends SherlockActivity {
	private ArrayList<MimicData> mimic;
	private LayoutInflater layoutinflater;
	private MimicAdapter mimicadapter;
	private ListView searchlist;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.hasht);
		 
		 searchlist= (ListView) findViewById(R.id.hashfield);
		 this.layoutinflater = LayoutInflater.from(this);
		 Uri uri = getIntent().getData();
		 String tag = uri.toString().split("/")[3];
		 Log.d("tag", tag);
		 SpannableString s = new SpannableString(tag.toUpperCase());
			s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			getSupportActionBar().setTitle(s);
			getSupportActionBar().setDisplayShowTitleEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setIcon(R.drawable.back);
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
			String x = tag.substring(1);
			Log.d("what is tag", x);
			searchmimics(x);
	}
	
	 public void searchmimics(String a){
			hashwebtask mimictask = new hashwebtask(hash.this);
			try{
				mimictask.execute(a);
				} catch (Exception e){
					mimictask.cancel(true);
					
				}
			
		
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
		
	 public void setMimics(ArrayList<MimicData> mimicdata){
			this.mimic= mimicdata;
			mimicadapter = new MimicAdapter(this, this.layoutinflater, this.mimic);
			this.searchlist.setAdapter(mimicadapter);
		}
	 
}
