package com.mimic.accesrest;

public class MimicData {

	private String username, profilepictureurl, posturl, url, description, timestamp, profileurl;
	private Integer commentcounter, likeCounter, postid;
	private Boolean likes, last;
	
	
	
	public MimicData(String Username, String dpurl, String Url,  int postid, Integer likecounter, Integer commentcount, String posturl, String description, Boolean likes, String timestamp, String profileurl, boolean last){
		this.last = last;
		this.username = Username;
		this.profilepictureurl = dpurl;
		this.likeCounter = likecounter;
		this.url = Url;
		this.commentcounter = commentcount;
		this.description = description;
		this.posturl = posturl;
		this.likes = likes;
		this.postid = postid;
		this.timestamp= timestamp;
		this.profileurl=profileurl;	
		
	}
	
	public String getprofileurl(){
		return profileurl;
	}
	public boolean getlast(){
		return last;
	}
	public String gettime(){
		return timestamp;
	}
	
	public void settime(String timestamp){
		this.timestamp= timestamp;
	}
	
	public int getpostid(){
		return postid;
	}
	
	public void setpostid(int postid){
		this.postid = postid;
	}
	public Boolean getLikes(){
		return likes;
	}
	
	public void setLikes(Boolean Likes){
		this.likes = Likes;
	}
	
	public String geturl(){
		return profilepictureurl;
	}
	
	public void setUrl(String url){
		this.profilepictureurl = url;
	}
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public int getlikecounter(){
		return likeCounter;
	}
	
	public void setlikecounter(Integer likecount){
		this.likeCounter = likecount;
	}
	
	public String getcommentcounter(){
		String x = commentcounter.toString();
		return x;
	}
	
	public void setcommentcounter(Integer commentcount){
		this.commentcounter = commentcount;
	}
	
	public String getsharecount(){
		String x = description.toString();
		return x;
	}
	
	public void setsharecount(String description){
		this.description = description;
	}

	public String getposturl(){
		return posturl;
	}
	
	public void setposturl(String Title){
		this.posturl = Title;
	}
	
	
}
