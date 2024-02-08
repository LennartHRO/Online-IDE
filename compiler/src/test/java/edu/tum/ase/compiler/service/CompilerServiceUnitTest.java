package edu.tum.ase.compiler.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tum.ase.compiler.model.SourceCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CompilerServiceUnitTest {

    private CompilerService compilerService;

    @Mock
    private ProcessBuilder processBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        compilerService = new CompilerService();
    }

    @Test
    void compileWithJavaFile() throws Exception {
        SourceCode sourceCode = new SourceCode("Test.java", "public class Test {}");
        sourceCode.setCompilable(true);
        Process process = mock(Process.class);
        when(processBuilder.start()).thenReturn(process);
        when(process.waitFor()).thenReturn(0);

        SourceCode compiledSourceCode = compilerService.compile(sourceCode);

        assertTrue(compiledSourceCode.isCompilable());
        assertEquals("", compiledSourceCode.getStdout());
        assertEquals("", compiledSourceCode.getStderr());
    }

    @Test
    void compileWithUnsupportedFileType() {
        SourceCode sourceCode = new SourceCode("Test.txt", "Some text");
        sourceCode.setCompilable(true);
        SourceCode compiledSourceCode = compilerService.compile(sourceCode);

        assertFalse(compiledSourceCode.isCompilable());
        assertEquals("java.lang.UnsupportedOperationException: Unsupported file type: Test.txt", compiledSourceCode.getStderr());
    }
}
