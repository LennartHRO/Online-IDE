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
import java.util.stream.Collectors;

@Service
public class SourceFileService {

    @Autowired
    private SourceFileRepository sourceFileRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<SourceFile> getSourceFiles(String username) {
            return sourceFileRepository.findAll().stream()
                    .filter(file -> file.getProject().isAllowedToEdit(username))
                    .collect(Collectors.toList());
    }

    public SourceFile getSourceFile(String sourceFileId, String username) {
        SourceFile file= sourceFileRepository.findById(sourceFileId)
                .orElseThrow(() -> new RuntimeException("SourceFile not found with id: " + sourceFileId));

        if(file.getProject().isAllowedToEdit(username)){
            return file;
        }else{
            throw new RuntimeException("Not Allowed");
        }
    }

    public void deleteSourceFile(String sourceFileId, String username) {
        SourceFile file= sourceFileRepository.findById(sourceFileId)
                .orElseThrow(() -> new RuntimeException("SourceFile not found with id: " + sourceFileId));

        if(file.getProject().isAllowedToEdit(username)){
            sourceFileRepository.deleteById(sourceFileId);
        }else{
            throw new RuntimeException("Not Allowed");
        }
    }

    public SourceFile createSourceFile(SourceFile sourceFile, String username) {
        // Fetch the associated project from the database
        Project project = projectRepository.findById(sourceFile.getProject().getId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        if (!project.isAllowedToEdit(username)){
            throw new RuntimeException("Not allowed");
        }
        // Set the associated project in the source file
        sourceFile.setProject(project);

        // Additional validation or business logic can be added here before saving to the repository
        return sourceFileRepository.save(sourceFile);
    }

    public SourceFile updateSourceFile(String sourceFileId, SourceFile updatedSourceFile, String username) {
        SourceFile existingSourceFile = getSourceFile(sourceFileId, username);

        // Update the fields of the existing source file
        existingSourceFile.setFileName(updatedSourceFile.getFileName());
        existingSourceFile.setSourceCode(updatedSourceFile.getSourceCode());

        // Additional validation or business logic can be added here before saving to the repository

        return sourceFileRepository.save(existingSourceFile);
    }


}
