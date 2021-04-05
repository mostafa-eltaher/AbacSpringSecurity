package com.github.gringostar.abac.web.model;

import java.io.Serializable;

public class Project implements Serializable {

    private static final long serialVersionUID = -1243540883857428940L;
    private Integer id;
    private String name;
    private String description;
    private String owner;

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
        return "{id:" + id + ", name:" + name + ", description:" + description + ", owner:" + owner + "}";
    }


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
