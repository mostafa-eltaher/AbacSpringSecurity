package com.github.gringostar.abac.config;

import com.github.gringostar.abac.web.model.Project;
import com.github.gringostar.abac.web.model.ProjectUser;
import com.github.gringostar.abac.web.model.UserRole;
import com.github.gringostar.abac.web.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class InMemoryUserDetailsService implements UserDetailsService, UserService {
    private static final String SECRET_STRING = "password";
    private Map<String, ProjectSecurityUser> users = new HashMap<>();

    @PostConstruct
    private void init() {
        this.users = new HashMap<>();
        User.UserBuilder userBuilder = User.withDefaultPasswordEncoder();
        this.createUser(
                new ProjectSecurityUser(userBuilder.username("admin").password(SECRET_STRING).roles(UserRole.ADMIN.name()).build(), UserRole.ADMIN));
        this.createUser(
                new ProjectSecurityUser(userBuilder.username("pm1").password(SECRET_STRING).roles(UserRole.PM.name()).build(),
                        UserRole.PM));
        this.createUser(
                new ProjectSecurityUser(userBuilder.username("pm2").password(SECRET_STRING).roles(UserRole.PM.name()).build(),
                        UserRole.PM));
        this.createUser(
                new ProjectSecurityUser(userBuilder.username("dev1").password(SECRET_STRING).roles(UserRole.DEVELOPER.name()).build(),
                        UserRole.DEVELOPER));
        this.createUser(
                new ProjectSecurityUser(userBuilder.username("dev2").password(SECRET_STRING).roles(UserRole.DEVELOPER.name()).build(),
                        UserRole.DEVELOPER));
        this.createUser(
                new ProjectSecurityUser(userBuilder.username("test1").password(SECRET_STRING).roles(UserRole.TESTER.name()).build(),
                        UserRole.TESTER));
        this.createUser(
                new ProjectSecurityUser(userBuilder.username("test2").password(SECRET_STRING).roles(UserRole.TESTER.name()).build(),
                        UserRole.TESTER));

    }


    public void createUser(ProjectSecurityUser user) {
        if (userExists(user.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        users.put(user.getUsername().toLowerCase(), user);
    }

    public boolean userExists(String username) {
        return users.containsKey(username.toLowerCase());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        ProjectSecurityUser user = users.get(username.toLowerCase());

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new ProjectSecurityUser(user.getUsername(), user.getPassword(), user.getProject(), user.getRole());
    }

    @Override
    public ProjectUser findUserByName(String name) {
        return users.get(name.toLowerCase());
    }

    @Override
    public List<ProjectUser> findUserByProject(Integer projectId) {
        if (projectId == null)
            return Collections.emptyList();
        List<ProjectUser> result = new LinkedList<>();
        for (ProjectSecurityUser user : users.values()) {
            Project project = user.getProject();
            if (project != null && projectId.equals(project.getId())) {
                result.add(user);
            }
        }
        return result;
    }

    @Override
    public String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
