package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//In order to implement a full create, read, update, and delete (CRUD) interface for the Project and SourceFile entities,add the controller module with the according classes ProjectController.java and SourceFileController.java


@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project) {
        // TODO add own name to lrz list
        return projectRepository.save(project);
    }

    public Project findByName(String name) {
        return projectRepository.findByName(name);
    }

    public Project findById(String projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Invalid projectId:" + projectId));
    }

    public void deleteProject(String projectId) {
        // Check if the project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectId: " + projectId));

        // Delete the project
        projectRepository.delete(project);
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public Project getProject(String projectId){
        return findById(projectId);
    }

    public Project updateProject(String projectId, Project updatedProject) {
        // Find the existing project by ID
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectId: " + projectId));

        // Update the existing project with the new values
        existingProject.setName(updatedProject.getName());

        // Save the updated project
        return projectRepository.save(existingProject);
    }

    public void shareProject(String projectId, String name) {
        // Find the existing project by ID
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid projectId: " + projectId));

        // Update the existing project with the new values
        existingProject.addUserIds(name);

        // Save the updated project
        projectRepository.save(existingProject);
    }

}