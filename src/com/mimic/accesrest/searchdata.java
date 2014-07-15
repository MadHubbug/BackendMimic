package com.mimic.accesrest;

public class searchdata{

	private String username, profilepictureurl, profileurl;
	private boolean follows;
	private int id;
	

	
	public searchdata(String username, String profilepictureurl, boolean following, int id, String profileurl){
		this.username = username;
		this.follows = following;
		this.profilepictureurl = profilepictureurl;
		this.id = id;
		this.profileurl = profileurl;

		
	}
	
	public String getprofileurl(){
		return profileurl;
	}

	public void setprofileurl(String x){
		this.profileurl = x;
	}
	public int getid(){
		return id;
	}
	
	public void setid(int id){
		this.id = id;
	}
	public String getusername(){
		return username;
	}
	
	public void setposturl(String username){
		this.username = username;
	}
	public boolean getfollows(){
		return follows;
	}
	
	public void setfollows(boolean following){
		this.follows = following;
	}
	public String getprofilepictureurl(){
		return profilepictureurl;
	}
	
	public void setprofilepictureurl(String dp){
		this.profilepictureurl= dp;
	}
	
}
