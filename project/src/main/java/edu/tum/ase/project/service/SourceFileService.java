package edu.tum.ase.project.service;

import edu.tum.ase.project.model.Project;
import edu.tum.ase.project.model.SourceFile;
import edu.tum.ase.project.repository.ProjectRepository;
import edu.tum.ase.project.repository.SourceFileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SourceFileService {

    @Autowired
    private SourceFileRepository sourceFileRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<SourceFile> getSourceFiles() {
        return sourceFileRepository.findAll();
    }

    public SourceFile getSourceFile(String sourceFileId) {
        return sourceFileRepository.findById(sourceFileId)
                .orElseThrow(() -> new RuntimeException("SourceFile not found with id: " + sourceFileId));
    }

    public void deleteSourceFile(String sourceFileId) {
        sourceFileRepository.deleteById(sourceFileId);
    }

    public SourceFile createSourceFile(SourceFile sourceFile) {
        // Fetch the associated project from the database
        Project project = projectRepository.findById(sourceFile.getProject().getId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));

        // Set the associated project in the source file
        sourceFile.setProject(project);

        // Additional validation or business logic can be added here before saving to the repository
        return sourceFileRepository.save(sourceFile);
    }

    public SourceFile updateSourceFile(String sourceFileId, SourceFile updatedSourceFile) {
        SourceFile existingSourceFile = getSourceFile(sourceFileId);

        // Update the fields of the existing source file
        existingSourceFile.setFileName(updatedSourceFile.getFileName());
        existingSourceFile.setSourceCode(updatedSourceFile.getSourceCode());

        // Additional validation or business logic can be added here before saving to the repository

        return sourceFileRepository.save(existingSourceFile);
    }


}
