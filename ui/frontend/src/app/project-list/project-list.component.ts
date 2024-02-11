import { Component, ViewChild, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Project } from '../project';
import { Router } from '@angular/router';
import { ProjectService } from '../services/projectService';
import { AuthService } from '../services/auth.service';


@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
})


export class ProjectListComponent {
  projectService: ProjectService = inject(ProjectService);
  
  newProjectName: string = '';
  editedProjectName: string | undefined = undefined; // Update the initialization to undefined
  editedProject: Project | null = null;

  // Reference to the modal template
  @ViewChild('editProjectModal') editProjectModal: any;

  projects: Project[] = [];

  constructor(private modalService: NgbModal, private router: Router, public authService: AuthService) {
    this.projectService.getProjects().then((projects :  Project[]) => {
      this.projects = projects;
    })
  }

  createProject() {
    this.projectService.createProject(this.newProjectName).then((project : Project) => {
      this.projects.push(project);
      this.newProjectName = '';
    })
  }

  editProjectName(project: Project) {
    this.editedProject = project;
    this.editedProjectName = project.name;
    this.modalService.open(this.editProjectModal);
  }

  saveEditedProject() {
    if (this.editedProject) {
      this.editedProject.name = this.editedProjectName!;
      this.projectService.updateProject(this.editedProject).then((project : Project) => {
        this.editedProject = project;
      })
      this.editedProject = null;
      this.editedProjectName = undefined;

      this.modalService.dismissAll(); // Close the modal
    }
  }

  openProject(project: Project) {
    this.router.navigate(['/editor', project.id]);
  }

  deleteProject(project: Project) {
    this.projectService.deleteProject(project.id).then(() => {
      this.projects = this.projects.filter((p) => p.id !== project.id);
    })
  }

  logout() {
    this.authService.logout();
    //reload the page
    window.location.reload();    
  }

}
