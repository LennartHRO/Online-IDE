package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//In order to implement a full create, read, update, and delete (CRUD) interface for the Project and SourceFile entities,add the controller module with the according classes ProjectController.java and SourceFileController.java


@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project findByName(String name) {
        return projectRepository.findByName(name);
    }

    public Project findById(String projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("Invalid projectId:" + projectId));
    }

    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public void updateProject(Project project) {
        
    }


}