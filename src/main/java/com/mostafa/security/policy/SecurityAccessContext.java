package com.mostafa.security.policy;

public class SecurityAccessContext {
	private Object subject;
	private Object resource;
	private Object action;
	private Object environment;
	
	
	
	public SecurityAccessContext(Object subject, Object resource, Object action, Object environment) {
		super();
		this.subject = subject;
		this.resource = resource;
		this.action = action;
		this.environment = environment;
	}
	public Object getSubject() {
		return subject;
	}
	public void setSubject(Object subject) {
		this.subject = subject;
	}
	public Object getResource() {
		return resource;
	}
	public void setResource(Object resource) {
		this.resource = resource;
	}
	public Object getAction() {
		return action;
	}
	public void setAction(Object action) {
		this.action = action;
	}
	public Object getEnvironment() {
		return environment;
	}
	public void setEnvironment(Object environment) {
		this.environment = environment;
	}
}
