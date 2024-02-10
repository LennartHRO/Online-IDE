import { APP_INITIALIZER, NgModule } from '@angular/core';
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
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { AuthGuard } from './services/auth.guard';
import { Observable, catchError, of } from 'rxjs';


const routes : Route[] = [
  { path: 'projects', component: ProjectListComponent, canActivate: [AuthGuard]},
  { path: 'editor/:id', component: EditorComponent, canActivate: [AuthGuard]},
  { path: '**', redirectTo: 'projects', canActivate: [AuthGuard]}
];
// see https://stackoverflow.com/a/74813159
function getCsrfToken(httpClient: HttpClient): () => Observable<any> {
  return () => httpClient.get('/csrf').pipe(catchError((err) => of(null)));
}
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
  providers: [{
      provide: APP_INITIALIZER,
      useFactory: getCsrfToken,
      deps: [HttpClient],
      multi: true,
    },],
  bootstrap: [AppComponent]
})
export class AppModule { }
