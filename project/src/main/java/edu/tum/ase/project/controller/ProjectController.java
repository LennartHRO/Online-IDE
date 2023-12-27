package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//In order to implement a full create, read, update, and delete (CRUD) interface for the Project and SourceFile entities,add the controller module with the according classes ProjectController.java and SourceFileController.java

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/")
    public List<Project> getProjects() {
        return projectService.getProjects();
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable("id") String projectId){
        Project project = projectService.findById(projectId);
        projectService.deleteProject(project);
    }

    @PostMapping("/")
    public Project createProject(@RequestBody Project project){
        return projectService.createProject(project);
    }
    /*
    TODO: UpdateProject
    @RequestMapping(path = "/api/projects", method = RequestMethod.PUT)
    public void updateProject(@RequestBody Project project) {
        projectService.updateProject(project);
    }
    */
}
