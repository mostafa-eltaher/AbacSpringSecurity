package edu.mostafa.abac.web.services;

import java.util.List;

import edu.mostafa.abac.web.model.ProjectUser;

public interface UserService {
	ProjectUser findUserByName(String name);
	List<ProjectUser> findUserByProject(Integer projectId);
}
