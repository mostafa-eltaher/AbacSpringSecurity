package com.gringostar.abac.web.services;

import com.gringostar.abac.web.model.Issue;

import java.util.List;

public interface IssueService {
    List<Issue> getIssues(Integer projectId);

    Issue getIssue(Integer id);

    void createIssue(Issue issue);

    void updateIssue(Issue issue);

    void deleteIssue(Integer issueId);
}
