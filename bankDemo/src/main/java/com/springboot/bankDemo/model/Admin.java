package com.springboot.bankDemo.model;

public class Admin {

	private String name;
	private User user;
		
	public Admin() {	}

	public Admin(String name, User user) {
		this.name = name;
		this.user = user;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
