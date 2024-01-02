package edu.tum.ase.project.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

@Entity
@Table(name = "project_source_files")
public class SourceFile {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "source_file_id")
    private String sourceFileId;

    @ManyToOne()
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "source_code", columnDefinition = "TEXT")
    private String sourceCode;

    protected SourceFile() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }
    public SourceFile(Project project, String fileName, String sourceCode) {
        this.project = project;
        this.fileName = fileName;
        this.sourceCode = sourceCode;
    }

    public String getSourceFileId() {
        return sourceFileId;
    }

    public void setSourceFileId(String sourceFileId) {
        this.sourceFileId = sourceFileId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
