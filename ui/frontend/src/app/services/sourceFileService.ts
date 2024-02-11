import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Project } from '../project';
import { SourceFile } from '../sourceFile';

@Injectable({
  providedIn: 'root'
})

export class SourceFileService {
    url = 'http://localhost:4200/api/source-file/';

    constructor(private http: HttpClient) { }

    async getSourceFiles() : Promise<SourceFile[]> {
        const data = await fetch(this.url);
        console.log(data);
        return data.json() ?? [];
    }

    async createSourceFile(sourceFile : SourceFile) : Promise<SourceFile> {
        const data = await fetch(this.url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sourceFile)
        });
        return data.json();
    }

    async updateSourceFile(sourceFile : SourceFile) : Promise<SourceFile> {
        const data = await fetch(this.url + sourceFile.sourceFileId, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(sourceFile)
        });
        return data.json();
    }

    async deleteSourceFile(id : any) : Promise<void> {
        await fetch(this.url + id, { method: 'DELETE' });
    }

    async getSourceFile(id : any) : Promise<SourceFile> {
        const data = await fetch(this.url + id);
        return data.json();
    }
}