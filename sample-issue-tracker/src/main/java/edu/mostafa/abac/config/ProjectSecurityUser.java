package edu.mostafa.abac.config;

import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import edu.mostafa.abac.web.model.Project;
import edu.mostafa.abac.web.model.ProjectUser;
import edu.mostafa.abac.web.model.UserRole;

public class ProjectSecurityUser extends User implements ProjectUser {
	private static final long serialVersionUID = 8498233196842987555L;
	
	private Project project;
	private UserRole role;

	public ProjectSecurityUser(String username, String password, UserRole role) {
		super(username, password, new ArrayList<GrantedAuthority>(0));
		this.role = role;
	}
	
	public ProjectSecurityUser(String username, String password, Project project, UserRole role) {
		super(username, password, new ArrayList<GrantedAuthority>(0));
		this.role = role;
		this.project = project;
	}

	@Override
	public Project getProject() {
		return project;
	}

	@Override
	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public UserRole getRole() {
		return this.role;
	}

	@Override
	public void setRole(UserRole role) {
		this.role = role;
		
	}

	@Override
	public String getName() {
		return super.getUsername();
	}

	@Override
	public String toString() {
		return "{username:" + getUsername() + ", password: [PROTECTED], enabled:" + isEnabled()
				+ ", accountNonExpired:" + isAccountNonExpired() + ", accountNonLocked:" + isAccountNonLocked()
				+ ", credentialsNonExpired:" + isCredentialsNonExpired() + ", project:" + project + ", role:" + role
				+ "}";
	}
	
	

}
