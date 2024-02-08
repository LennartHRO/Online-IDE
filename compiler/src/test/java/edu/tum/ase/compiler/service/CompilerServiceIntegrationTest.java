package edu.tum.ase.compiler.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.tum.ase.compiler.model.SourceCode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CompilerServiceIntegrationTest {

    private CompilerService compilerService;

    @BeforeEach
    void setUp() {
        compilerService = new CompilerService();
    }

    @Test
    public void should_CompileJavaSourceCode_Successfully() throws IOException, InterruptedException {
        // given
        String javaCode = "public class Test {}";
        SourceCode sourceCode = new SourceCode("Test.java", javaCode);

        // mock Process and ProcessBuilder
        Process process = mock(Process.class);
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        when(processBuilder.start()).thenReturn(process);
        when(process.waitFor()).thenReturn(0);
        when(process.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        when(process.getErrorStream()).thenReturn(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));

        // when
        SourceCode compiledSourceCode = compilerService.compile(sourceCode);

        // then
        assertTrue(compiledSourceCode.isCompilable());
        assertEquals("", compiledSourceCode.getStdout());
        assertEquals("", compiledSourceCode.getStderr());
    }

    @Test
    public void should_HandleCompilationFailure() throws IOException, InterruptedException {
        // given
        String javaCodeWithError = "public class TestWithError { public void methodWithError() { error; } }";
        SourceCode sourceCode = new SourceCode("TestWithError.java", javaCodeWithError);

        // mock Process and ProcessBuilder
        Process process = mock(Process.class);
        ProcessBuilder processBuilder = mock(ProcessBuilder.class);
        when(processBuilder.start()).thenReturn(process);
        when(process.waitFor()).thenReturn(1); // Compilation error exit code
        when(process.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        when(process.getErrorStream()).thenReturn(new ByteArrayInputStream("error: cannot find symbol".getBytes(StandardCharsets.UTF_8)));

        // when
        SourceCode compiledSourceCode = compilerService.compile(sourceCode);

        // then
        assertFalse(compiledSourceCode.isCompilable());
        assertTrue(compiledSourceCode.getStderr().contains("error: not a statement"));
    }
}
