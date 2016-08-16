package edu.mostafa.abac.web.model;

public class Issue {
	private Integer id;

	private Project project;
	private IssueType type;
	private String name;
	private String description;
	
	private String createdBy;
	private String assignedTo;
	private IssueStatus status;
	
	public Issue() {
		super();
	}
	
	public Issue(Integer id) {
		super();
		this.id = id;
	}

	public Issue(Integer id, Project project, IssueType type, String name, String description, String createdBy,
			String assignedTo, IssueStatus status) {
		super();
		this.id = id;
		this.project = project;
		this.type = type;
		this.name = name;
		this.description = description;
		this.createdBy = createdBy;
		this.assignedTo = assignedTo;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public IssueType getType() {
		return type;
	}
	public void setType(IssueType type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public IssueStatus getStatus() {
		return status;
	}
	public void setStatus(IssueStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "{id:" + id + ", project:" + project + ", type:" + type + ", name:" + name + ", description:"
				+ description + ", createdBy:" + createdBy + ", assignedTo:" + assignedTo + ", status:" + status + "}";
	}	
}
