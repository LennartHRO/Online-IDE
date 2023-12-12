package edu.tum.ase.compiler.controller;

import edu.tum.ase.compiler.model.SourceCode;
import edu.tum.ase.compiler.service.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompilerController {
    @Autowired
    private CompilerService compilerService;
    @RequestMapping(path = "/compile", method = RequestMethod.POST)
    public SourceCode compile(@RequestBody SourceCode sourceCode) {
        return compilerService.compile(sourceCode);
    }
}