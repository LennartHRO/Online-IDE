package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.SourceFile;
import edu.tum.ase.project.service.SourceFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/source-file")
@CrossOrigin(origins = "http://localhost:4200")
public class SourceFileController {
    @Autowired
    private SourceFileService sourceFileService;

    @GetMapping("/")
    public List<SourceFile> getSourceFiles() {
        return sourceFileService.getSourceFiles();
    }

    @GetMapping("/{id}")
    public SourceFile getSourceFile(@PathVariable("id") String sourceFileId) {
        return sourceFileService.getSourceFile(sourceFileId);
    }

    @DeleteMapping("/{id}")
    public void deleteSourceFile(@PathVariable("id") String sourceFileId) {
        sourceFileService.deleteSourceFile(sourceFileId);
    }

    @PostMapping("/")
    public SourceFile createSourceFile(@RequestBody SourceFile sourceFile) {
        return sourceFileService.createSourceFile(sourceFile);
    }

    @PutMapping("/{id}")
    public SourceFile updateSourceFile(@PathVariable("id") String sourceFileId, @RequestBody SourceFile updatedSourceFile) {
        return sourceFileService.updateSourceFile(sourceFileId, updatedSourceFile);
    }
}
