import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})

export class ShareService {
    url = 'http://localhost:4200/api/project/';

    constructor(private http: HttpClient) { }

    async shareProject(id: any, name : string) : Promise<void> {
        const data = await fetch(this.url+'share/'+id, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ name })
        });
        return data.json();
    }

  }