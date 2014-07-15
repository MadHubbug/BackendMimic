package com.mimic.accesrest;



import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Util;






import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SherlockActivity implements OnClickListener, OnScrollListener{

	private ArrayList<MimicData> mimic;
	private ListView MimicList;
	private LayoutInflater layoutinflater;
	private MimicWebTask task;
	private int currentpage = 1;
	private static final String logtaskact = "MainUI";
	private MimicAdapter mimicadapter;	
	private UiLifecycleHelper uiHelper;
	private boolean d = true;
	private boolean stop = true;
	private PullToRefreshLayout mPullToRefreshLayout;
	private ImageView view;
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
	   
		@Override
	    public void call(Session session, SessionState state,
	            Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }

		
	
	};
	
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isClosed()) {
            Log.i(logtaskact, "Logged out...");
            Intent intent = new Intent(this, MainPage.class);
            this.startActivity(intent);
		}
		
}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);

		    // Now setup the PullToRefreshLayout
		   ActionBarPullToRefresh.from(this)
		            // Mark All Children as pullable
		            .allChildrenArePullable()
		            // Set a OnRefreshListener
		            .listener(new OnRefreshListener() {
					
						@Override
						public void onRefreshStarted(View view) {
						d=true;
						currentpage=1;
						mimic();
						mPullToRefreshLayout.setRefreshComplete();
						}
					})
		            // Finally commit the setup to our PullToRefreshLayout
		            .setup(mPullToRefreshLayout);
		this.MimicList = (ListView) findViewById(R.id.listView1);
		MimicList.setOnScrollListener(this);
		this.layoutinflater = LayoutInflater.from(this);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		SpannableString s = new SpannableString("mimic");
		s.setSpan(new Typefacespan(this, "Roboto-Medium.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getSupportActionBar().setTitle(s);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.logo);
		view = (ImageView) findViewById(android.R.id.home);
		
		ImageButton buttonpost = (ImageButton) findViewById(R.id.postbutton);
		buttonpost.setOnClickListener((OnClickListener) this);
		ImageButton notification = (ImageButton) findViewById(R.id.notificationbutton);
		notification.setOnClickListener((OnClickListener) this);
		ImageButton profile = (ImageButton) findViewById(R.id.profilebutton);
		profile.setOnClickListener((OnClickListener) this);
		ImageButton explore = (ImageButton) findViewById(R.id.explorebutton);
		explore.setOnClickListener((OnClickListener) this);
		ImageButton home = (ImageButton) findViewById(R.id.homebutton);
		home.setOnClickListener((OnClickListener) this);
		
		
		uiHelper = new UiLifecycleHelper(this, statusCallback);
	    uiHelper.onCreate(savedInstanceState); 
		Session session = Session.getActiveSession();
		    if (session == null) {
		        if (savedInstanceState != null) {
		            session = Session.restoreSession(this, null, statusCallback, savedInstanceState);
		        }
		        if (session == null) {
		            session = new Session(this);
		        }
		        Session.setActiveSession(session);
		    }
		
		
		


		mimic();
		registerlistcallback();
		
			}	
	
	 @Override
	    protected void onResume() {
	        super.onResume();
	        // For scenarios where the main activity is launched and user
	        // session is not null, the session state change notification
	        // may not be triggered. Trigger it if it's open/closed.
	        Session session = Session.getActiveSession();
	        if (session != null && (session.isOpened() || session.isClosed())) {
	            onSessionStateChange(session, session.getState(), null);
	        }
	        uiHelper.onResume();
	    }

	    @Override
	    protected void onPause() {
	        super.onPause();
	        uiHelper.onPause();
	    }

	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        uiHelper.onActivityResult(requestCode, resultCode, data);
	    }

	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        uiHelper.onDestroy();
	    }

	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        Session session = Session.getActiveSession();
	        Session.saveSession(session, outState);
	        uiHelper.onSaveInstanceState(outState);
	    }
	    
	@Override
		public void onClick (View v){
		if (v.getId() == R.id.homebutton){
			startActivity(new Intent(MainActivity.this, MainActivity.class));
		}
		else if (v.getId() == R.id.postbutton)
		{
			startActivity(new Intent(MainActivity.this, post.class));
			
		}else if(v.getId() == R.id.profilebutton){
			Intent x = new Intent(MainActivity.this, profile.class);
			x.putExtra("profileurl", "http://mimictheapp.herokuapp.com/profiles/");
			startActivity(x);
		}
		else if(v.getId() == R.id.notificationbutton){

		Intent x = new Intent(MainActivity.this, Notifications.class);
		startActivity(x);
		}
		else if (v.getId() == R.id.explorebutton){
			startActivity(new Intent(MainActivity.this, Explore.class));		}
}


	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		 MenuInflater inflater = getSupportMenuInflater();
		    inflater.inflate(R.menu.main, menu);
		  	 
		        // Associate searchable configuration with the SearchView
		        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		        SearchView searchView = (SearchView) menu.findItem(R.id.search)
		                .getActionView();
		        
		        searchView.setSearchableInfo(searchManager
		                .getSearchableInfo(getComponentName()));
		 
		        return super.onCreateOptionsMenu(menu);
		    }
	
	@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		   // Handle item selection
		   switch (item.getItemId()) {
		     
		      case R.id.search:
		    	  
					Log.d("SEARCH", "clicked");
					return true;
	
				        case android.R.id.home:
				            this.finish();
				            return true;
				        case R.id.logout:
				        	Session.getActiveSession().closeAndClearTokenInformation();
				        	return true;
					default:
		         return super.onOptionsItemSelected(item);
		         
		   }
		}
	
	public void mimic(){
		
		try{
			task = new MimicWebTask(MainActivity.this);
			task.execute(1);
			} catch (Exception e){
				task.cancel(true);
				alert("No Mimics");
			}
		
	}
	
	public void setUsers(ArrayList<MimicData> mimic) {
	int s = mimic.size();
	if (s>0){
		MimicData r = mimic.get(0);
		if (stop){
		if (r.getlast() == false){
			if (this.mimic == null){
				this.mimic = mimic;
				mimicadapter = new MimicAdapter(this,this.layoutinflater, this.mimic);
				this.MimicList.setAdapter(mimicadapter);
				d=false;
			}else{
			for (int i = 0; i<mimic.size(); i++){
				this.mimic.add(mimic.get(i));
				mimicadapter.notifyDataSetChanged();
				Log.d("counts", "mimic count:"+ mimic.size());
				
			}
			}
			stop = false;
		}else{
		if (d){
		
			this.mimic = mimic;
			mimicadapter = new MimicAdapter(this,this.layoutinflater, this.mimic);
			this.MimicList.setAdapter(mimicadapter);
			d=false;
			Log.d("counts", "mimic count:"+ mimic.size());
		
		}else{
			
			for (int i = 0; i<mimic.size(); i++){
				this.mimic.add(mimic.get(i));
				mimicadapter.notifyDataSetChanged();
				Log.d("counts", "mimic count:"+ mimic.size());
			}
			
		}
    }
		} else{
			Log.d("stopped", "stopped");
		}
	}else{
		Log.d("no follwers", "i know");
	}
}
	
	public void alert(String msg) {
		Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
		
	}
	
    public static class MyViewHolder {
        public TextView user, description, echonum, commentnum, likesnum, duration, timestamp;
        public MimicData mimic;
        public ImageButton like, play, share, reply, echo;
        public String posturl, profileurl;
        public SeekBar sb;
        public ImageView dp;
        public int postid, likecount;
        public Boolean liked;

    }
    

    
    
    public void registerlistcallback(){
    	this.MimicList.setAdapter(mimicadapter);
    	this.MimicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				MimicData mimics = (MimicData) mimicadapter.getItem(position);
				Log.d("You clicked", mimics.getUsername());
			}
		});
    }

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
	        if (MimicList.getLastVisiblePosition() >= MimicList.getCount() - 1 - 1) {
	            currentpage = currentpage + 1;
	            //load more list items:
	            new MimicWebTask(MainActivity.this).execute(currentpage);
	        }
	    }
	
		
	}

//    public String logout(Context context)
//            throws MalformedURLException, IOException {
//        Util.clearCookies(context);
//        Bundle b = new Bundle();
//        b.putString("method", "auth.expireSession");
//        String response = request(b);
//        setAccessToken(null);
//        setAccessExpires(0);
//        return response;
//    }

}