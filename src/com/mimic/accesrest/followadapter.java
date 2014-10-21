package com.mimic.accesrest;

import java.util.ArrayList;

//import com.fedorvlasov.lazylist.ImageLoader;
import com.mimic.accesrest.MainActivity.MyViewHolder;
import com.mimic.accesrest.followinglist.FollowHolder;
import com.mimic.accesrest.search.MyHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class followadapter extends BaseAdapter{
	private Activity activity;
	private LayoutInflater layoutinflater;
	private ArrayList<searchdata> mimicdata;
	private searchdata searchdata;
	private ImageLoader imageloader;
	private DisplayImageOptions options;

	private String profileurl, user;
public followadapter(Activity a, LayoutInflater l, ArrayList <searchdata> m){
		
		this.activity = a;
		this.layoutinflater = l;
		this.mimicdata = m;
		imageloader=ImageLoader.getInstance();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("profileid", "0");
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.stub)
		.showImageForEmptyUri(R.drawable.stub)
		.showImageOnFail(R.drawable.stub)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.build();
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mimicdata.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}
	
	@Override
	public long getItemId(int pos) {
		return pos;
			}
	
	@Override
	public View getView(int pos, View ConvertView, ViewGroup parent) {
		final FollowHolder holder;
		if (ConvertView == null){
			ConvertView = layoutinflater.inflate(R.layout.followrows, parent, false);
			holder = new FollowHolder();
			holder.dp = (ImageView) ConvertView.findViewById(R.id.dpfollows);
			holder.username =(TextView) ConvertView.findViewById(R.id.usernamefollows);

		
			
			holder.dp.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent x = new Intent(activity, profile.class);
					x.putExtra("profileurl", holder.profileurl);
					x.putExtra("prof", true);
					activity.startActivity(x);
					
				}
				
			});
			holder.username.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					Intent x = new Intent(activity, profile.class);
					x.putExtra("profileurl", holder.profileurl);
					x.putExtra("prof", true);
					activity.startActivity(x);
					
				}
				
			});
			ConvertView.setTag(holder);
	}else{
		holder = (FollowHolder)ConvertView.getTag();
	}
		
		String x= mimicdata.get(pos).getusername();
		holder.searchdata = mimicdata.get(pos);
		holder.username.setText(mimicdata.get(pos).getusername());
		holder.profileurl= mimicdata.get(pos).getprofileurl();
		
		holder.profileid = mimicdata.get(pos).getid();
		profileurl = holder.profileurl;
		imageloader.displayImage(mimicdata.get(pos).getprofilepictureurl(), holder.dp, options);
		
		
		return ConvertView;
	}
}
