package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//In order to implement a full create, read, update, and delete (CRUD) interface for the Project and SourceFile entities,add the controller module with the according classes ProjectController.java and SourceFileController.java

@RestController
@RequestMapping("/api/project")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public List<Project> getProjects(HttpServletRequest request) {
        String username = request.getHeader("Username");
        return projectService.getProjects(username);
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable("id") String projectId, HttpServletRequest request) {
        String username = request.getHeader("Username");
        return projectService.getProject(projectId, username);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable("id") String projectId, HttpServletRequest request) {
        String username = request.getHeader("Username");
        projectService.deleteProject(projectId, username);
    }

    @PostMapping("/")
    public Project createProject(@RequestBody Project project, HttpServletRequest request) {
        String username = request.getHeader("Username");
        return projectService.createProject(project, username);
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable("id") String projectId, @RequestBody Project updatedProject, HttpServletRequest request) {
        String username = request.getHeader("Username");
        return projectService.updateProject(projectId, updatedProject, username);
    }

    @PostMapping("/share/{id}")
    public void shareProject(@PathVariable("id") String projectId, @RequestBody  Project updatedProject, HttpServletRequest request) {
        String username = request.getHeader("Username");
        projectService.shareProject(projectId, updatedProject.getName(), username);
    }





}
