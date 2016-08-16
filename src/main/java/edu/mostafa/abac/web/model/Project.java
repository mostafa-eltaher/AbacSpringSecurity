package edu.mostafa.abac.web.model;

public class Project {
	private Integer id;
	private String name;
	private String description;
	
	public Project() {
		super();
	}
	
	public Project(Integer id) {
		super();
		this.id = id;
	}
	
	public Project(Integer id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "{id:" + id + ", name:" + name + ", description:" + description + "}";
	}
	
	
}
