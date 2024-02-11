import { Project } from './project'; // Import the Project class or adjust the import path

export class SourceFile {
  sourceFileId: string
  project: Project | undefined;
  fileName: string | undefined;
  sourceCode: string | undefined;

  constructor(project: Project, fileName: string, sourceCode: string) {
    this.project = project;
    this.fileName = fileName;
    this.sourceCode = sourceCode;
    this.sourceFileId = "";
  }
}
