package edu.mostafa.abac.web.services.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import edu.mostafa.abac.web.model.Issue;
import edu.mostafa.abac.web.model.IssueStatus;
import edu.mostafa.abac.web.model.Project;
import edu.mostafa.abac.web.services.IssueService;

@Component
public class InMemoryIssueService implements IssueService {
	private final HashMap<Integer, List<Issue>> issuesByProjectId = new HashMap<>();
	private final HashMap<Integer, Issue> issuesById = new HashMap<>();
	private final InMemorySequence seq = new InMemorySequence();

	@Override
	public List<Issue> getIssues(Project project) {
		if(project == null)
			return null;
		Integer projectId = project.getId();
		return issuesByProjectId.get(projectId);
	}

	@Override
	public Issue getIssue(Integer issueId) {
		if(issueId == null)
			return null;
		return issuesById.get(issueId);
	}

	@Override
	public void createIssue(Issue issue) {
		if(issue == null)
			return;
		Integer newId = seq.increment();
		Project project = issue.getProject();
		if(project == null)
			return;
		Integer projectId = project.getId();
		if(projectId == null)
			return;
		issue.setId(newId);
		issue.setStatus(IssueStatus.NEW);
		issuesById.put(newId, issue);
		addToProject(projectId, issue);
	}

	@Override
	public void updateIssue(Issue issue) {
		if(issue == null)
			return;
		Integer id = issue.getId();
		if(id == null)
			return;
		Project newProject = issue.getProject();
		if(newProject == null)
			return;
		Integer newProjectId = newProject.getId();
		if(newProjectId == null)
			return;
		Issue existingIssue = issuesById.get(id);
		Integer oldProjectId = existingIssue.getProject().getId();
		
		copyIssue(issue, existingIssue);
		
		if(!newProjectId.equals(oldProjectId)) {
			addToProject(newProjectId, existingIssue);
			removeFromProject(oldProjectId, existingIssue);
		}	
	}

	@Override
	public void deleteIssue(Issue issue) {
		Integer id = issue.getId();
		if(id == null)
			return;
		Issue existingIssue = issuesById.get(issue.getId());
		if(existingIssue == null)
			return;
		
		Integer projectId = existingIssue.getProject().getId();
		if(projectId == null)
			return;
		
		issuesById.remove(id);
		removeFromProject(projectId, existingIssue);
	}
	
	private void addToProject(Integer projectId, Issue issue) {
		List<Issue> projectIssues = issuesByProjectId.get(projectId);
		if(projectIssues == null) {
			projectIssues = new LinkedList<>();
			issuesByProjectId.put(projectId, projectIssues);
		} 
		projectIssues.add(issue);
	}
	
	private void removeFromProject(Integer projectId, Issue issue) {
		List<Issue> projectIssues = issuesByProjectId.get(projectId);
		if(projectIssues == null) 
			return;
		projectIssues.remove(issue);	
	}
	
	private Issue copyIssue(Issue source, Issue target) {
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		target.setProject(source.getProject());
		target.setType(source.getType());
		target.setStatus(source.getStatus());
		target.setCreatedBy(source.getCreatedBy());
		target.setAssignedTo(source.getAssignedTo());
		return target;
	}

}
