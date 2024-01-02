package edu.tum.ase.compiler.controller;

import edu.tum.ase.compiler.model.SourceCode;
import edu.tum.ase.compiler.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compile")
@CrossOrigin(origins = "http://localhost:4200")
public class CompilerController {
    @Autowired
    private CompilerService compilerService;
    @PostMapping("/")
    public SourceCode compile(@RequestBody SourceCode sourceCode) {
        return compilerService.compile(sourceCode);
    }
}