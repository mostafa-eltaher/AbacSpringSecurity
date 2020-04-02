package edu.mostafa.abac.web.services.impl;

import edu.mostafa.abac.web.model.Project;
import edu.mostafa.abac.web.services.ProjectService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryProjectService implements ProjectService {

    private final HashMap<Integer, Project> projectsById = new HashMap<>();
    private final InMemorySequence seq = new InMemorySequence();

    @Override
    public List<Project> getProjects() {
        ArrayList<Project> result = new ArrayList<>(projectsById.size());
        result.addAll(projectsById.values());
        return result;
    }

    @Override
    public Project getProject(Integer id) {
        if (id == null)
            return null;
        return projectsById.get(id);
    }

    @Override
    public void createProject(Project project) {
        if (project == null)
            return;
        Integer newId = seq.increment();
        project.setId(newId);
        projectsById.put(newId, project);
    }

    @Override
    public void updateProject(final Project project) {
        Project currentProject = getProject(project.getId());
        if (currentProject == null)
            return;
        currentProject.setName(project.getName());
        currentProject.setDescription(project.getDescription());
    }

    @Override
    public void deleteProject(final Project project) {
        projectsById.remove(project.getId());
    }
}
