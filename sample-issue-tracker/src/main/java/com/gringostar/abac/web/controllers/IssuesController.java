package com.gringostar.abac.web.controllers;

import com.gringostar.abac.security.spring.ContextAwarePolicyEnforcer;
import com.gringostar.abac.web.model.Issue;
import com.gringostar.abac.web.model.IssueStatus;
import com.gringostar.abac.web.model.Project;
import com.gringostar.abac.web.model.ProjectUser;
import com.gringostar.abac.web.services.IssueService;
import com.gringostar.abac.web.services.ProjectService;
import com.gringostar.abac.web.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
    private ContextAwarePolicyEnforcer policy;

    @GetMapping(value = "/", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public List<Issue> listIssues(@PathVariable Integer projectId) {
        logger.info("[listIssues({})] started ...", projectId);
        Project project = projectService.getProject(projectId);
        if (project == null) {
            logger.info("[listIssues({})] ignored, non-existing project.", projectId);
            return Collections.emptyList();
        }
        policy.checkPermission(project, "ISSUES_LIST");
        List<Issue> result = issueService.getIssues(projectId);
        logger.info("[listIssues({})] done, result: {} issues.", projectId, result == null ? null : result.size());
        return result;
    }

    @PostMapping(value = "/", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.CREATED)
    public void createIssue(@PathVariable Integer projectId, @RequestBody Issue issue) {
        logger.info("[createIssue({}, {})] started ...", projectId, issue);
        if (issue == null) {
            logger.info("[createIssue({}, {})] ignored, empty issue.", projectId, issue);
            return;
        }
        Project existingProject = projectService.getProject(projectId);
        if (existingProject == null) {
            logger.info("[createIssue({}, {})] ignored, non-existing project.", projectId, issue);
            return;
        }
        issue.setProject(existingProject);

        policy.checkPermission(issue, "ISSUES_CREATE");

        issueService.createIssue(issue);
        logger.info("[createIssue({}, {})] done.", projectId, issue);
    }

    @PutMapping(value = "/{id}", consumes = {"application/json"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public void updateIssue(@PathVariable Integer projectId, @PathVariable Integer id, @RequestBody Issue issue) {
        logger.info("[updateIssue({}, {}, {})] started ...", projectId, id, issue);

        Project existingProject = projectService.getProject(projectId);
        if (existingProject == null) {
            logger.info("[updateIssue({}, {}, {})] ignored, non-existing project.", projectId, id, issue);
            return;
        }

        Issue currentIssue = issueService.getIssue(id);
        if (currentIssue == null) {
            logger.info("[updateIssue({}, {}, {})] ignored, non-existing issue.", projectId, id, issue);
            return;
        }

        policy.checkPermission(currentIssue, "ISSUES_UPDATE");

        issue.setId(id);
        issue.setProject(existingProject);
        issueService.updateIssue(issue);
        logger.info("[updateIssue({}, {}, {})] done.", projectId, id, issue);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public void deleteIssue(@PathVariable Integer id) {
        logger.info("[deleteIssue({})] started ...", id);
        Issue currentIssue = issueService.getIssue(id);
        if (currentIssue == null) {
            logger.info("[deleteIssue({})] ignored, non-existing issue.", id);
            return;
        }

        policy.checkPermission(currentIssue, "ISSUES_DELTE");

        issueService.deleteIssue(id);
        logger.info("[deleteIssue({})] done.", id);
    }

    @PutMapping(value = "/{id}/assignee", consumes = {"text/plain"}, produces = {"application" +
            "/json"})
    @ResponseStatus(HttpStatus.OK)
    public void updateIssueAssignee(@PathVariable Integer projectId, @PathVariable Integer id,
									@RequestBody String assigneeName) {
        logger.info("[updateIssueAssignee({}, {}, {})] started ...", projectId, id, assigneeName);
        Issue currentIssue = issueService.getIssue(id);
        if (currentIssue == null) {
            logger.info("[updateIssueAssignee({}, {}, {})] ignored, non-existing issue.", projectId, id, assigneeName);
            return;
        }

        policy.checkPermission(currentIssue, "ISSUES_ASSIGN");

        ProjectUser assignee = userService.findUserByName(assigneeName);
        if (assignee == null) {
            logger.info("[updateIssueAssignee({}, {}, {})] ignored, non-existing user.", projectId, id, assigneeName);
            return;
        }

        currentIssue.setAssignedTo(assigneeName);
        currentIssue.setStatus(IssueStatus.ASSIGNED);
        logger.info("[updateIssueAssignee({}, {}, {})] done.", projectId, id, assigneeName);
    }

    @PutMapping(value = "/{id}/status", consumes = {"text/plain"}, produces = {"application/json"})
    @ResponseStatus(HttpStatus.OK)
    public void updateIssueStatus(@PathVariable Integer projectId, @PathVariable Integer id,
								  @RequestBody String newStatusStr) {
        logger.info("[updateIssueStatus({}, {}, {})] started ...", projectId, id, newStatusStr);

        IssueStatus newStatus;
        try {
            newStatus = IssueStatus.valueOf(newStatusStr);
        } catch (IllegalArgumentException ex) {
            logger.info("[updateIssueStatus({}, {}, {})] ignored, unrecognized status.", projectId, id, newStatusStr);
            return;
        }
        Issue currentIssue = issueService.getIssue(id);
        if (currentIssue == null) {
            logger.info("[updateIssueStatus({}, {}, {})] ignored, non-existing issue.", projectId, id, newStatusStr);
            return;
        }

        if (newStatus == IssueStatus.COMPLETED) {
            policy.checkPermission(currentIssue, "ISSUES_STATUS_CLOSE");
        } else {
            policy.checkPermission(currentIssue, "ISSUES_UPDATE");
        }
        currentIssue.setStatus(newStatus);
        logger.info("[updateIssueStatus({}, {}, {})] done.", projectId, id, newStatusStr);
    }

}
