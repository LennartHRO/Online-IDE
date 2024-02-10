package edu.tum.ase.project.controller;

import edu.tum.ase.project.model.SourceFile;
import edu.tum.ase.project.service.SourceFileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/source-file")
@CrossOrigin(origins = "http://localhost:4200")
public class SourceFileController {
    @Autowired
    private SourceFileService sourceFileService;

    @GetMapping("/")
    public List<SourceFile> getSourceFiles(HttpServletRequest request) {
        String username = request.getHeader("Username");
        return sourceFileService.getSourceFiles(username);
    }

    @GetMapping("/{id}")
    public SourceFile getSourceFile(@PathVariable("id") String sourceFileId, HttpServletRequest request) {
        String username = request.getHeader("Username");
        return sourceFileService.getSourceFile(sourceFileId, username);
    }

    @DeleteMapping("/{id}")
    public void deleteSourceFile(@PathVariable("id") String sourceFileId, HttpServletRequest request) {
        String username = request.getHeader("Username");
        sourceFileService.deleteSourceFile(sourceFileId, username);
    }

    @PostMapping("/")
    public SourceFile createSourceFile(@RequestBody SourceFile sourceFile, HttpServletRequest request) {
        String username = request.getHeader("Username");
        return sourceFileService.createSourceFile(sourceFile, username);
    }

    @PutMapping("/{id}")
    public SourceFile updateSourceFile(@PathVariable("id") String sourceFileId, @RequestBody SourceFile updatedSourceFile, HttpServletRequest request) {
        String username = request.getHeader("Username");
        return sourceFileService.updateSourceFile(sourceFileId, updatedSourceFile, username);
    }
}
