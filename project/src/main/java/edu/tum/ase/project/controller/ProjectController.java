package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//In order to implement a full create, read, update, and delete (CRUD) interface for the Project and SourceFile entities,add the controller module with the according classes ProjectController.java and SourceFileController.java

@RestController
@RequestMapping("/project")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable("id") String projectId){
        return projectService.getProject(projectId);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable("id") String projectId){
        projectService.deleteProject(projectId);
    }

    @PostMapping("/")
    public Project createProject(@RequestBody Project project){
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable("id") String projectId, @RequestBody Project updatedProject) {
        return projectService.updateProject(projectId, updatedProject);
    }



}
