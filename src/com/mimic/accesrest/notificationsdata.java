package com.mimic.accesrest;

public class notificationsdata {
	
	private String profileurl, username, profilepictureurl, typeofnotif, playurl, time;
	int postid;
	public notificationsdata(String profileurl, String username, String profilepictureurl, String typeofnotif, int posturl2, String playurl, String time){
		this.profileurl = profileurl;
		this.username = username;
		this.typeofnotif= typeofnotif;
		this.profilepictureurl = profilepictureurl;
		this.postid = posturl2;
		this.playurl = playurl;
		this.time = time;
				
	}
	
	public String gettime(){
		return time;
	}
	public String getprofileurl(){
		return profileurl;
	}

	public void setprofileurl(String x){
		this.profileurl = x;
	}
	
	public String getusername(){
		return username;
	}

	public void setusername(String x){
		this.username = x;
	}
	
	public String gettypeofnotif(){
		return typeofnotif;
	}

	public void settypeofnotif(String x){
		this.typeofnotif = x;
	}
	
	public String getprofilpictureeurl(){
		return profilepictureurl;
	}

	public void setprofilepictureurl(String x){
		this.profilepictureurl = x;
	}
	
	public int getposturl(){
		return postid;
	}

	public void setposturl(int x){
		this.postid = x;
	}
	
	public String getplayurl(){
		return playurl;
	}

	public void setplayurl(String x){
		this.playurl = x;
	}
}
