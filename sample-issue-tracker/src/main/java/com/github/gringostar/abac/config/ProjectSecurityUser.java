package com.github.gringostar.abac.config;

import com.github.gringostar.abac.web.model.Project;
import com.github.gringostar.abac.web.model.ProjectUser;
import com.github.gringostar.abac.web.model.UserRole;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Objects;

public class ProjectSecurityUser extends User implements ProjectUser {
    private static final long serialVersionUID = 8498233196842987555L;

    private Project project;
    private UserRole role;

    public ProjectSecurityUser(UserDetails userDetails, UserRole role) {
        this(userDetails.getUsername(), userDetails.getPassword(), role);
    }

    private ProjectSecurityUser(String username, String password, UserRole role) {
        super(username, password, new ArrayList<>(0));
        this.role = role;
    }

    public ProjectSecurityUser(String username, String password, Project project, UserRole role) {
        super(username, password, new ArrayList<>(0));
        this.role = role;
        this.project = project;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public UserRole getRole() {
        return this.role;
    }

    @Override
    public void setRole(UserRole role) {
        this.role = role;

    }

    @Override
    public String getName() {
        return super.getUsername();
    }

    @Override
    public String toString() {
        return "{username:" + getUsername() + ", password: [PROTECTED], enabled:" + isEnabled()
                + ", accountNonExpired:" + isAccountNonExpired() + ", accountNonLocked:" + isAccountNonLocked()
                + ", credentialsNonExpired:" + isCredentialsNonExpired() + ", project:" + project + ", role:" + role
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProjectSecurityUser that = (ProjectSecurityUser) o;
        return Objects.equals(project, that.project) &&
                role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), project, role);
    }
}
