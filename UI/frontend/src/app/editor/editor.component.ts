import { Component, ViewChild, inject, OnDestroy, OnInit  } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Project } from '../project';
import { ProjectService } from '../services/projectService';
import { SourceFile } from '../sourceFile';
import { SourceFileService } from '../services/sourceFileService';
import { CompileService } from '../services/compileService';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { DarkModeService } from '../services/darkmodeService';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrl: './editor.component.css',
})
export class EditorComponent implements OnInit, OnDestroy{
  editorOptions = { theme: 'vs', language: 'javascript' }; // vs-dark
  code: string = 'function x() {\nconsole.log("Hello world!");\n}';
  compileOutput: string = '';
  toggleEditor: any;

  showEditor: boolean = false;

  projectService: ProjectService = inject(ProjectService);
  sourceFileService: SourceFileService = inject(SourceFileService);
  compileService: CompileService = inject(CompileService);
  route: ActivatedRoute = inject(ActivatedRoute);

  projectId: string;
  project: Project = {
    id: '1',
    name: 'My Project',
  };

  sourceFiles: SourceFile[] = [];

  opendFileId: string | undefined = undefined;
  newFileName: string = '';
  editedFile: SourceFile | null = null;

  @ViewChild('newFileModal') newFileModal: any;
  @ViewChild('editFileModal') editFileModal: any;

  private darkModeSubscription: any;

  constructor(private modalService: NgbModal, public authService: AuthService, public darkModeService: DarkModeService) {
    this.projectId = this.route.snapshot.paramMap.get('id')!;

    this.projectService.getProject(this.projectId).then((project: Project) => {
      this.project = project;
    });

    this.sourceFileService
      .getSourceFiles()
      .then((sourceFiles: SourceFile[]) => {
        this.sourceFiles = sourceFiles;
        this.sourceFiles = this.sourceFiles.filter(
          (sf) => sf.project?.id === this.projectId
        );
        console.log(this.sourceFiles);
      });
  }

  ngOnInit(): void {
    this.darkModeSubscription = this.darkModeService.getDarkModeStatus().subscribe((isDarkMode) => {
      this.editorOptions = { ...this.editorOptions, theme: isDarkMode ? 'vs-dark' : 'vs' };
    });
  }

  ngOnDestroy(): void {
    if (this.darkModeSubscription) {
      this.darkModeSubscription.unsubscribe();
    }
  }

  deleteSourceFile(id: string) {
    if (id == this.opendFileId) {
      this.showEditor = false;
    }
    this.sourceFileService.deleteSourceFile(id).then(() => {
      this.sourceFiles = this.sourceFiles.filter(
        (file) => file.sourceFileId !== id
      );
    });
  }

  async changeSourceFileName(
    sourceFileId: string,
    newName: string
  ): Promise<void> {
    try {
      const sourceFile = this.sourceFiles.find(
        (file) => file.sourceFileId === sourceFileId
      );
      if (sourceFile) {
        sourceFile.fileName = newName; // Update the name locally
        const updatedFile = await this.sourceFileService.updateSourceFile(
          sourceFile
        );
        // Optionally, update the local array or handle the updated file
        console.log('Updated File:', updatedFile);
      }
    } catch (error) {
      console.error('Error updating source file:', error);
    }
    this.newFileName = ''; // Reset the input field
    this.modalService.dismissAll(); // Close the modal
    this.choseRightLanguage();

  }

  async openFile(sourceFileId: string): Promise<void> {
    const sourceFile = this.sourceFiles.find(
      (file) => file.sourceFileId === sourceFileId
    );
    if (sourceFile) {
      this.compileOutput = '';
      this.code = sourceFile.sourceCode || ''; // Set the editor content to the source file's code
      this.opendFileId = sourceFileId; // Keep track of the currently opened file
      this.editedFile = sourceFile;
      this.showEditor = true; // Show the editor if it's not already visible
      this.choseRightLanguage();
    }
  }

  openNewFileModal() {
    this.newFileName = '';
    this.modalService.open(this.newFileModal);
  }

  openEditFileModal(editedFile: SourceFile) {
    this.newFileName = editedFile.fileName || '';
    this.editedFile = editedFile;
    this.modalService.open(this.editFileModal);
  }

  choseRightLanguage() {
    if (this.editedFile?.fileName?.endsWith('.java')) {
      this.editorOptions = { ...this.editorOptions,  language: 'java'};
    } else if (this.editedFile?.fileName?.endsWith('.py')) {  
      this.editorOptions = { ...this.editorOptions,  language: 'python'};
    } else if (this.editedFile?.fileName?.endsWith('.c')) {
      this.editorOptions = { ...this.editorOptions,  language: 'c'};
    } else if (this.editedFile?.fileName?.endsWith('.cpp')) {
      this.editorOptions = { ...this.editorOptions,  language: 'cpp'};
    } else {
      this.editorOptions = { ...this.editorOptions,  language: 'javascript'};
    }

  }

  async createNewFile() {
    if (this.newFileName.trim() !== '') {
      let newSourceFile: SourceFile = {
        project: this.project,
        fileName: this.newFileName,
        sourceCode: 'print (Hello World))',
        sourceFileId: '',
      };
      this.sourceFileService
        .createSourceFile(newSourceFile)
        .then((sourceFile: SourceFile) => {
          this.sourceFiles.push(sourceFile);
        });
      this.newFileName = ''; // Reset the input field
      this.modalService.dismissAll(); // Close the modal
      this.choseRightLanguage();
    }
  }

  async safeCode() {
    try {
      const sourceFile = this.sourceFiles.find(
        (file) => file.sourceFileId === this.opendFileId
      );
      if (sourceFile) {
        sourceFile.sourceCode = this.code;
        const updatedFile = await this.sourceFileService.updateSourceFile(
          sourceFile
        );
      }
    } catch (error) {
      console.error('Error saving the source file', error);
    }
  }


  async compileCode() {
    try {
      const sourceFile = this.sourceFiles.find(
        (file) => file.sourceFileId === this.opendFileId
      );
      if (this.code == sourceFile?.sourceCode) {
        this.compileService
          .compile(this.code, sourceFile?.fileName || '')
          .then((response) => {
            if (response.compilable) {
              this.compileOutput = 'Compilation successful.';
              if (response.stdout) {
                this.compileOutput += ` Output: ${response.stdout}`;
              }
            } else {
              this.compileOutput = 'Compilation failed. ';
              if (response.stderr) {
                this.compileOutput += `\n Error: ${response.stderr}`;
              }
            }
            console.log(response);
          });
      } else {
        this.compileOutput = 'Please save your code before compiling';
      }
    } catch (error) {
      console.error('Error compiling the source file', error);
    }
  }
}
