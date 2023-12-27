package edu.tum.ase.project.service;

import edu.tum.ase.project.model.SourceFile;
import edu.tum.ase.project.repository.ProjectRepository;
import edu.tum.ase.project.repository.SourceFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SourceFileService {

    @Autowired
    private SourceFileRepository sourceFileRepository;

    public List<SourceFile> getAllSourceFiles() {
        return sourceFileRepository.findAll();
    }


}
