import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Project } from '../project';


@Injectable({
  providedIn: 'root'
})

export class ProjectService {
    url = 'http://localhost:8080/api/project/';

    constructor(private http: HttpClient) { }

    async getProjects() : Promise<Project[]> {
        const data = await fetch(this.url);
        return data.json() ?? [];
    }

    async createProject(name : string) : Promise<Project> {
        const data = await fetch(this.url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name })
        });
        return data.json();
    }

    async updateProject(project : Project) : Promise<Project> {
        const data = await fetch(this.url + project.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(project)
        });
        return data.json();
    }

    async deleteProject(id : any) : Promise<void> {
        await fetch(this.url + id, { method: 'DELETE' });
    }

    async getProject(id : any) : Promise<Project> {
        const data = await fetch(this.url + id);
        return data.json();
    }
}