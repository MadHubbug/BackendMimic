package com.mimic.accesrest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class Login extends SherlockActivity {

	
	public String pass, user;
	public SharedPreferences prefs;
	public SharedPreferences.Editor editor;
	public loginwebtask task;
	private ProgressDialog progdialog;
	
	@Override
	protected void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F86960")));
		getSupportActionBar().setTitle("LOGIN");
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setIcon(R.drawable.back);
		setContentView(R.layout.login);
		Button login = (Button) findViewById(R.id.Follow);
		prefs =  PreferenceManager.getDefaultSharedPreferences(this);
		Log.d("HEY, WE'RE FROM SIGNUP", "woot");
		editor = prefs.edit();
		final EditText username = (EditText) findViewById(R.id.fullnamesettings);
		final EditText password = (EditText) findViewById(R.id.emailsignup);
		
		login.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 user = username.getText().toString();
				pass = password.getText().toString();
				task = new loginwebtask(Login.this);
				task.execute(user, pass);
				progdialog = ProgressDialog.show(Login.this, "Logging in", "Checking authentication", true, false);
				
				
				
					
			}
			
		});
		
		
		
		
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
	public void setUser(String bucket, String profileid) {
	Log.d("bucket", bucket+ " ");
	Log.d("bucket", profileid + " ");
		if (bucket==null){
			Log.d("woah", "bucket is null");
			alert("Invalid username or password");
			progdialog.dismiss();
			
		}else{

			editor.putString("bucket", bucket);
			editor.putString("profileid", profileid);
			editor.putString("username", user);
			editor.putString("password", pass);
			editor.commit();
			Log.d("setuserareweevenhere", prefs.getString("username", "nothing here") + ":" +prefs.getString("profileid", "nothing here"));
			progdialog.dismiss();
			this.finish();
			startActivity(new Intent(Login.this, MainActivity.class));
			
			
		}
		
	}
	public void alert(String msg) {
		Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
		
	}
}
