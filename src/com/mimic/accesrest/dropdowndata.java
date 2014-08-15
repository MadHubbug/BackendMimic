package com.mimic.accesrest;

public class dropdowndata {

	private String next, username;
	
	public  dropdowndata(String next, String username){
		this.next = next;
		this.username = username;
	}
	
	public String getusername(){
		return username;
	}
	
	public String getnext(){
		return next;
	}
}
