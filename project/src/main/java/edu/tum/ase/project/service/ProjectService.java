package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//In order to implement a full create, read, update, and delete (CRUD) interface for the Project and SourceFile entities,add the controller module with the according classes ProjectController.java and SourceFileController.java


@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project, String username) {
        project.addUserIds(username);
        return projectRepository.save(project);
    }


    public Project findById(String projectId, String user) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Invalid projectId:" + projectId));
        if( project.isAllowedToEdit(user)){
            return project;
        }
        return null;
    }

    public void deleteProject(String projectId, String username) {
        // Check if the project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectId: " + projectId));

        if (!project.isAllowedToEdit(username))
            throw new RuntimeException("No access to this Project");
        // Delete the project
        projectRepository.delete(project);
    }

    public List<Project> getProjects(String username) {
        List<Project> allProjects = projectRepository.findAll();

        // Filter projects based on allowedToView(username) method
        return allProjects.stream()
                .filter(project -> project.isAllowedToEdit(username))
                .collect(Collectors.toList());
    }

    public Project getProject(String projectId, String username){
        return findById(projectId,username);
    }

    public Project updateProject(String projectId, Project updatedProject, String username) {

        // Find the existing project by ID
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectId: " + projectId));

        if (!existingProject.isAllowedToEdit(username))
            throw new RuntimeException("No access to this Project");
        // Update the existing project with the new values
        existingProject.setName(updatedProject.getName());

        // Save the updated project
        return projectRepository.save(existingProject);
    }

    public void shareProject(String projectId, String name, String username) {
        // Find the existing project by ID
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectId: " + projectId));

        if (!existingProject.isAllowedToEdit(username))
            throw new RuntimeException("No access to this Project");

        // Update the existing project with the new values
        existingProject.addUserIds(name);

        // Save the updated project
        projectRepository.save(existingProject);
    }

}