package com.mimic.accesrest;

import java.util.ArrayList;

//import com.fedorvlasov.lazylist.ImageLoader;
import com.mimic.accesrest.MainActivity.MyViewHolder;
import com.mimic.accesrest.search.MyHolder;
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

public class searchadapter extends BaseAdapter{
	private Activity activity;
	private LayoutInflater layoutinflater;
	private ArrayList<searchdata> mimicdata;
	private searchdata searchdata;
	private ImageLoader imageloader;
	private boolean checker = true;
	private boolean[] followedpositions = new boolean[50];
	private String profileurl, user, actualuser, username;
public searchadapter(Activity a, LayoutInflater l, ArrayList <searchdata> m){
		
		this.activity = a;
		this.layoutinflater = l;
		this.mimicdata = m;
		imageloader = ImageLoader.getInstance();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		user = prefs.getString("profileid", "0");
		actualuser = prefs.getString("username", "0");
		
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
		final MyHolder holder;
		if (ConvertView == null){
			ConvertView = layoutinflater.inflate(R.layout.searchusers, parent, false);
			holder = new MyHolder();
			holder.dp = (ImageView) ConvertView.findViewById(R.id.searchdp);
			holder.username =(TextView) ConvertView.findViewById(R.id.usernamesearch);
			holder.follow = (ImageButton) ConvertView.findViewById(R.id.followbutton);
			holder.follow.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					final follow follow = new follow();
					final unfollow unfollow = new unfollow();	
					int y = holder.profileid;
					int a = (Integer) v.getTag();
					final String x = Integer.toString(y);
					if (followedpositions[a] == true){
						Log.d("What is holder?", "Holder: "+ y);
						Log.d("What is holder?", "Holder: "+ a);
						followedpositions[a] = false;
						unfollow.execute(x);
						//notifyDataSetChanged();
						
						holder.follow.setImageResource(R.drawable.follow);			
					}else if (followedpositions[a] == false){
						Log.d("What is holder?", "Holder: "+ y);
						Log.d("What is holder?", "Holder: "+ a);
						follow.execute(x, user,holder.usern, actualuser);
						Log.d("what is x", x);
						followedpositions[a] = true;
						//notifyDataSetChanged();
						holder.follow.setImageResource(R.drawable.following);
					}
						
				
				}
				
			});
			
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
		holder = (MyHolder)ConvertView.getTag();
	}
		
		
		String x= mimicdata.get(pos).getusername();
		holder.searchdata = mimicdata.get(pos);
		holder.username.setText(mimicdata.get(pos).getusername());
		holder.usern = mimicdata.get(pos).getusername();
		holder.username.setTextColor(activity.getResources().getColor(R.color.descriptioncolor));
		holder.profileurl= mimicdata.get(pos).getprofileurl();
		holder.follow.setTag(pos);
		holder.profileid = mimicdata.get(pos).getid();
		profileurl = holder.profileurl;
		imageloader.displayImage(mimicdata.get(pos).getprofilepictureurl(), holder.dp);
		if (checker == true){
			for (int i=0; i<mimicdata.size(); i++)				
			{
				searchdata = mimicdata.get(i);
				boolean y = searchdata.getfollows();
				
			
			if (y == true){
				followedpositions[i] = true;				
			}else {
				followedpositions[i] = false;
			}
			checker = false;
			}
			}
		if (followedpositions[pos]){
			holder.follow.setImageResource(R.drawable.following);
		}else{
			holder.follow.setImageResource(R.drawable.follow);
		}
		
		return ConvertView;
	}
}
