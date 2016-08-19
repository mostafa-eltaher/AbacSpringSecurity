package edu.mostafa.abac.web.services;

import java.util.List;

import edu.mostafa.abac.web.model.Issue;
import edu.mostafa.abac.web.model.Project;

public interface IssueService {
	public List<Issue> getIssues(Project project);
	public Issue getIssue(Integer id);
	public void createIssue(Issue issue);
	public void updateIssue(Issue issue);
	public void deleteIssue(Issue issue);
}
