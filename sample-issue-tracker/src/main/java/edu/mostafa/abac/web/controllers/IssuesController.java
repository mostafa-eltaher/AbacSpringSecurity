package edu.mostafa.abac.web.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.mostafa.abac.security.spring.ContextAwarePolicyEnforcement;
import edu.mostafa.abac.web.model.Issue;
import edu.mostafa.abac.web.model.IssueStatus;
import edu.mostafa.abac.web.model.Project;
import edu.mostafa.abac.web.model.ProjectUser;
import edu.mostafa.abac.web.services.IssueService;
import edu.mostafa.abac.web.services.ProjectService;
import edu.mostafa.abac.web.services.UserService;

@RestController
@RequestMapping("/projects/{projectId}/issues")
public class IssuesController {
private static final Logger logger = LoggerFactory.getLogger(IssuesController.class);
	
	@Autowired
	private IssueService issueService;
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContextAwarePolicyEnforcement policy;
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public List<Issue> listIssues(@PathVariable Integer projectId) {
		logger.info("[listIssues({})] started ...", projectId);
		Project project = projectService.getProject(projectId);
		if(project == null) {
			logger.info("[listIssues({})] ignored, non-existing project.", projectId);
			return null;
		}
		policy.checkPermission(project, "ISSUES_LIST");
		List<Issue> result = issueService.getIssues(new Project(projectId));
		logger.info("[listIssues({})] done, result: {} issues.", projectId, result == null? null : result.size());
		return result;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = {"application/json"}, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void createIssue(@PathVariable Integer projectId, @RequestBody Issue issue) {
		logger.info("[createIssue({}, {})] started ...", projectId, issue);
		if(issue == null) {
			logger.info("[createIssue({}, {})] ignored, empty issue.", projectId, issue);
			return;
		}
		Project existingProject = projectService.getProject(projectId);
		if(existingProject == null) {
			logger.info("[createIssue({}, {})] ignored, non-existing project.", projectId, issue);
			return;
		}
		issue.setProject(existingProject);
		
		policy.checkPermission(issue, "ISSUES_CREATE");
		
		issueService.createIssue(issue);
		logger.info("[createIssue({}, {})] done.", projectId, issue);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"}, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void updateIssue(@PathVariable Integer projectId, @PathVariable Integer id, @RequestBody Issue issue) {
		logger.info("[updateIssue({}, {}, {})] started ...", projectId, id, issue);
		
		Project existingProject = projectService.getProject(projectId);
		if(existingProject == null) {
			logger.info("[updateIssue({}, {}, {})] ignored, non-existing project.", projectId, id, issue);
			return;
		}
		
		Issue currentIssue = issueService.getIssue(id);
		if(currentIssue == null) {
			logger.info("[updateIssue({}, {}, {})] ignored, non-existing issue.", projectId, id, issue);
			return;
		}
		
		policy.checkPermission(currentIssue, "ISSUES_UPDATE");
		
		issue.setId(id);
		issue.setProject(existingProject);
		issueService.updateIssue(issue);
		logger.info("[updateIssue({}, {}, {})] done.", projectId, id, issue);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void deleteIssue(@PathVariable Integer id) {
		logger.info("[deleteIssue({})] started ...", id);
		Issue currentIssue = issueService.getIssue(id);
		if(currentIssue == null) {
			logger.info("[deleteIssue({})] ignored, non-existing issue.", id);
			return;
		}
		
		policy.checkPermission(currentIssue, "ISSUES_DELTE");
		
		issueService.deleteIssue(new Issue(id));
		logger.info("[deleteIssue({})] done.", id);
	}
	
	@RequestMapping(value = "/{id}/assignee", method = RequestMethod.PUT, consumes = {"text/plain"}, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void updateIssueAssignee(@PathVariable Integer projectId, @PathVariable Integer id, @RequestBody String assigneeName) {
		logger.info("[updateIssueAssignee({}, {}, {})] started ...", projectId, id, assigneeName);
		Issue currentIssue = issueService.getIssue(id);
		if(currentIssue == null) {
			logger.info("[updateIssueAssignee({}, {}, {})] ignored, non-existing issue.", projectId, id, assigneeName);
			return;
		}
		
		policy.checkPermission(currentIssue, "ISSUES_ASSIGN");
		
		ProjectUser assignee = userService.findUserByName(assigneeName);
		if(assignee == null) {
			logger.info("[updateIssueAssignee({}, {}, {})] ignored, non-existing user.", projectId, id, assigneeName);
			return;
		}
		
		currentIssue.setAssignedTo(assigneeName);
		currentIssue.setStatus(IssueStatus.ASSIGNED);
		logger.info("[updateIssueAssignee({}, {}, {})] done.", projectId, id, assigneeName);
	}
	
	@RequestMapping(value = "/{id}/status", method = RequestMethod.PUT, consumes = {"text/plain"}, produces = {"application/json"})
	@ResponseStatus(HttpStatus.OK)
	public void updateIssueStatus(@PathVariable Integer projectId, @PathVariable Integer id, @RequestBody String newStatusStr) {
		logger.info("[updateIssueStatus({}, {}, {})] started ...", projectId, id, newStatusStr);
		
		IssueStatus newStatus = null;
		try {
			newStatus = IssueStatus.valueOf(newStatusStr);
		} catch(IllegalArgumentException ex) {
			logger.info("[updateIssueStatus({}, {}, {})] ignored, unrecognized status.", projectId, id, newStatusStr);
			return;
		}
		Issue currentIssue = issueService.getIssue(id);
		if(currentIssue == null) {
			logger.info("[updateIssueStatus({}, {}, {})] ignored, non-existing issue.", projectId, id, newStatusStr);
			return;
		}
		
		if(newStatus == IssueStatus.COMPLETED) {
			policy.checkPermission(currentIssue, "ISSUES_STATUS_CLOSE");
		} else {
			policy.checkPermission(currentIssue, "ISSUES_UPDATE");
		}
		currentIssue.setStatus(newStatus);
		logger.info("[updateIssueStatus({}, {}, {})] done.", projectId, id, newStatusStr);
	}

}
