package com.mimic.accesrest;

public class profiledata{
	
	private String fullname, postid;
	private String username;
	private Integer followers;
	private Integer following, postcount;
	private String profileurl;
	private boolean follows;
	
	public profiledata(String full, String Username, Integer Followers, 
			Integer Following, String profileurl, Integer postcount, boolean follows, String postid){
		this.profileurl = profileurl;
		this.fullname = full;
		this.username = Username;
		this.followers = Followers;
		this.following = Following;
		this.postcount = postcount;
		this.follows = follows;
		this.postid = postid;
				
	}
	
	public String getpostid(){
		return postid;
	}
	public String getfullname(){
		return fullname;
	}
	
	public void setfullname(String full){
		this.fullname= full;
	}

	public String getprofileurl(){
		return profileurl;
	}
	
	public void setprofileurl(String full){
		this.profileurl= full;
	}
	

	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	
	
	public String getfollowing(){
		String x = following.toString();
		return x;
	}
	
	public void setfollowing(Integer following){
		this.following= following;
	}
	
	public String getfollowers(){
		String x = followers.toString();
		return x;
	}
	
	public void setfollowers(Integer follower){
		this.followers = follower;
	}


	public String getpostcount() {
		String x = postcount.toString();
		return x;
		
	}
	
	public void setpostcount(Integer S){
		this.postcount = S;
		
	}


	public boolean getfollows() {
		// TODO Auto-generated method stub
		return follows;
	}
	
	
	
}
