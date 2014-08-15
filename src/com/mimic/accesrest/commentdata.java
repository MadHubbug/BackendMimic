package com.mimic.accesrest;

public class commentdata {

	private String commenturl, profilepictureurl, commentuser, times, profileurl;

	
	public commentdata(String commenturl, String commentuser, String profilepictureurl, String times, String profileurl){
		this.commenturl = commenturl;
		this.commentuser = commentuser;
		this.profilepictureurl = profilepictureurl;
		this.times = times;
		this.profileurl = profileurl;
		
	}
	
	public String getuser(){
		return profileurl;
	}
	public String gettimes(){
		return times;
	}
	
	public void settimes(String times){
		this.times = times;
	}

	public String getcommenturl(){
		return commenturl;
	}
	
	public void setposturl(String Commenturl){
		this.commenturl = Commenturl;
	}
	public String getusercommenturl(){
		return commentuser;
	}
	
	public void setusercommenturl(String user){
		this.commentuser = user;
	}
	public String getprofilepictureurl(){
		return profilepictureurl;
	}
	
	public void setprofilepictureurl(String dp){
		this.profilepictureurl= dp;
	}
	
}
