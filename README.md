# Abac Spring Security
Attribute-Based Access Control with Spring Security with Spring Boot example

For more details, check [The DZone article](https://dzone.com/articles/simple-attribute-based-access-control-with-spring)

All REST requests with sample data are in AbacSpringSecurity.postman_collection.json, you can import it into Postman chrome extension.

## How to Build
```shel
mvn clean install
```
# Sample Application
## How to deploy and run
- Run AbacApplication from IDE or from terminal:
```shel
    cd sample-issue-tracker/
    mvn spring-boot:run
```
## How to test
- Add new project using **POST /sample-issue-tracker/projects/**
- Assign PM to the project using **PUT /sample-issue-tracker/projects/{project_id}/pm/**
- Add users to the project using **POST /sample-issue-tracker/projects/{project_id}/users/**
- Add issues to the project using **POST /sample-issue-tracker/projects/{project_id}/issues/**
- Assign issues to users using **PUT /sample-issue-tracker/projects/{project_id}/issues/{issue_id}/assignee**
- Update issue's status **PUT /sample-issue-tracker/projects/{project_id}/issues/{issue_id}/status**

## User Authentication
The sample application uses HTTP Basic Authentication and blow are the users that can be used:
- admin/password	
- pm1/password
- pm2/password
- dev1/password
- dev2/password
- test1/password
- test2/password

All users can be found and modified in **com.gringostar.abac.config.InMemoryUserDetailsService** class.
