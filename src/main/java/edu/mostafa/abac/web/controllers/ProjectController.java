package edu.mostafa.abac.web.controllers;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.mostafa.abac.security.spring.ContextAwarePolicyEnforcement;
import edu.mostafa.abac.web.model.BasicProjectUser;
import edu.mostafa.abac.web.model.Project;
import edu.mostafa.abac.web.model.ProjectUser;
import edu.mostafa.abac.web.model.UserRole;
import edu.mostafa.abac.web.services.ProjectService;
import edu.mostafa.abac.web.services.UserService;

@RestController
@RequestMapping("/projects")
public class ProjectController {
	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	@Autowired
	private ContextAwarePolicyEnforcement policy;
	
	@Autowired
	private ProjectService projectsService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasPermission(null,'PROJECTS_LIST')")
	public List<Project> listProjects() {
		logger.info("[ListProjects] started ...");
		List<Project> result = projectsService.getProjects();
		logger.info("[ListProjects] done, result: {} projects", result == null? null : result.size());
		return result;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	@PostAuthorize("hasPermission(returnObject,'PROJECTS_VIEW')")
	public Project getProject(@PathVariable Integer id) {
		logger.info("[getProject({})] started ...", id);
		Project result = projectsService.getProject(id);
		logger.info("[getProject({})] done, result: {}", id, result);
		return result;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes={"application/json"}, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasPermission(null,'PROJECTS_CREATE')")
	public void createProject(@RequestBody Project project) {
		logger.info("[createProject({})] started ...", project);
		projectsService.createProject(project);
		logger.info("[createProject({})] done.", project);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes={"application/json"}, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void updateProject(@PathVariable Integer id, @RequestBody Project project) {
		logger.info("[updateProject({}, {})] started ...", id, project);
		if(project == null) {
			logger.info("[updateProject({}, {})] ignored, empty project", id, project);
			return;
		}
		
		Project existingProject = projectsService.getProject(id);
		if(existingProject == null) {
			logger.info("[updateProject({}, {})] ignored, non-exiting project", id, project);
			return;
		}
			
		
		policy.checkPermission(existingProject, "PROJECTS_UPDATE");
		
		projectsService.updateProject(project);
		logger.info("[updateProject({}, {})] done.", id, project);
	}
	

	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void deleteProject(@PathVariable Integer id) {
		logger.info("[deleteProject({})] started ...", id);
		Project existingProject = projectsService.getProject(id);
		if(existingProject == null) {
			logger.info("[deleteProject({})] ingnored, non existing project.", id);
			return;
		}
		
		policy.checkPermission(existingProject, "PROJECTS_DELETE");
		
		projectsService.deleteProject(existingProject);
		logger.info("[deleteProject({})] done.", id);
	}
	
	@RequestMapping(value = "/{id}/pm/", method = RequestMethod.PUT, consumes= {"text/plain"} , produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void updateProjectManager(@PathVariable Integer id, @RequestBody String newManagerName) {
		logger.info("[updateProjectManager({}, {})] started ...", id, newManagerName);
		Project existingProject = projectsService.getProject(id);
		if(existingProject == null) {
			logger.info("[updateProjectManager({}, {})] ingnored, non-existing project.", id, newManagerName);
			return;
		}
		
		policy.checkPermission(existingProject, "PROJECTS_PM_UPDATE");
		
		ProjectUser user = userService.findUserByName(newManagerName);
		if(user == null) {
			logger.info("[updateProjectManager({}, {})] ingnored, non-existing user.", id, newManagerName);
			return;
		}
		user.setProject(existingProject);
		user.setRole(UserRole.PM);
		logger.info("[updateProjectManager({}, {})] done.", id, newManagerName);
	}
	
	@RequestMapping(value = "/{id}/users/", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public List<BasicProjectUser> listProjectUsers(@PathVariable Integer id) {
		logger.info("[listProjectUsers({})] started ...", id);
		Project existingProject = projectsService.getProject(id);
		if(existingProject == null) {
			logger.info("[listProjectUsers({})] ignored, non-existing project.", id);
			return null;
		}
		
		policy.checkPermission(existingProject, "PROJECTS_USERS_LIST");
		
		
		List<BasicProjectUser> result = new LinkedList<>();
		List<ProjectUser> existingUsers = userService.findUserByProject(id);
		if(existingUsers != null) {
			for(ProjectUser user : existingUsers) {
				result.add(new BasicProjectUser(user.getName(), user.getRole()));
			}
		}
		logger.info("[listProjectUsers({})] done, result: {} users.", id, result.size());
		return result;
	}
	
	@RequestMapping(value = "/{id}/users/", method = RequestMethod.POST, consumes= {"application/json"}, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void addProjectUser(@PathVariable Integer id, @RequestBody BasicProjectUser user) {
		logger.info("[addProjectUser({}, {})] started ...", id, user);
		Project existingProject = projectsService.getProject(id);
		if(existingProject == null) {
			logger.info("[addProjectUser({}, {})] ignored, non-existing project.", id, user);
			return;
		}
		policy.checkPermission(existingProject, "PROJECTS_USERS_ADD");
		
		String userName = user.getName();
		if(userName == null || userName.isEmpty()) {
			logger.info("[addProjectUser({}, {})] ignored, empty user name.", id, user);
			return;
		}
		
		UserRole userRole = user.getRole();
		if(userRole == null) {
			logger.info("[addProjectUser({}, {})] ignored, empty user role.", id, user);
			return;
		}
		
		ProjectUser existingUser = userService.findUserByName(userName);
		if(existingUser == null) {
			logger.info("[addProjectUser({}, {})] ignored, non-existing user.", id, user);
			return;
		}
		
		existingUser.setProject(existingProject);
		existingUser.setRole(userRole);
		logger.info("[addProjectUser({}, {})] done.", id, user);
	}
	
	@RequestMapping(value = "/{id}/users/{userName}", method = RequestMethod.DELETE, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void removeProjectUser(@PathVariable Integer id, @PathVariable String userName) {
		logger.info("[removeProjectUser({}, {})] started ...", id, userName);
		Project existingProject = projectsService.getProject(id);
		if(existingProject == null) {
			logger.info("[removeProjectUser({}, {})] ignored, non-existing project.", id, userName);
			return;
		}
		
		policy.checkPermission(existingProject, "PROJECTS_USERS_REMOVE");
		
		if(userName == null || userName.isEmpty()) {
			logger.info("[removeProjectUser({}, {})] ignored, empty user name.", id, userName);
			return;
		}
		
		ProjectUser existingUser = userService.findUserByName(userName);
		if(existingUser == null) {
			logger.info("[removeProjectUser({}, {})] ignored, non-existing user.", id, userName);
			return;
		}
		
		existingUser.setProject(null);
		existingUser.setRole(null);
		logger.info("[removeProjectUser({}, {})] done.", id, userName);
	}
	
}
