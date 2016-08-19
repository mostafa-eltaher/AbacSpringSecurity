package edu.mostafa.abac.web.model;

import java.io.Serializable;

public class BasicProjectUser implements Serializable {
	private static final long serialVersionUID = -2977271355914647053L;
	
	private String name;
	private UserRole role;
	
	public BasicProjectUser(String name, UserRole role) {
		super();
		this.name = name;
		this.role = role;
	}
	public BasicProjectUser() {
		super();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
}
