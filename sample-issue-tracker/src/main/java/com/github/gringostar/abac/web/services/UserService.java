package com.github.gringostar.abac.web.services;

import java.util.List;

import com.github.gringostar.abac.web.model.ProjectUser;

public interface UserService {
	ProjectUser findUserByName(String name);
	List<ProjectUser> findUserByProject(Integer projectId);
	String getCurrentUsername();
}
