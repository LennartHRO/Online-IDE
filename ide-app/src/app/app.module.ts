import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProjectListComponent } from './project-list/project-list.component';
import { FormsModule } from '@angular/forms';
import { EditorComponent } from './editor/editor.component';
import { Route, RouterModule } from '@angular/router';
import { MonacoEditorModule } from 'ngx-monaco-editor-v2';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';


const routes : Route[] = [
  { path: 'projects', component: ProjectListComponent },
  { path: 'editor/:id', component: EditorComponent },
  { path: '**', redirectTo: 'projects' }
];
@NgModule({
  declarations: [
    AppComponent,
    ProjectListComponent,
    EditorComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    RouterModule.forRoot(routes),
    MonacoEditorModule.forRoot(),
    NgbModule,
    MatSlideToggleModule,
    BrowserAnimationsModule,
    HttpClientModule
  ],
  exports: [RouterModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
