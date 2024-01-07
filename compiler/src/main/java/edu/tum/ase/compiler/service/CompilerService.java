package edu.tum.ase.compiler.service;

import edu.tum.ase.compiler.model.SourceCode;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CompilerService {
    public SourceCode compile(SourceCode sourceCode) {

        String fileName = sourceCode.getFileName();
        String code = sourceCode.getCode();

        String relativePath = "src/main/temp";
        String filePath = Paths.get(System.getProperty("user.dir"), relativePath, fileName).toString();

        try {
            Files.writeString(Paths.get(filePath), code);

            Process process;

            if (fileName.endsWith(".java")) {
                // Execute the compilation command
                process = new ProcessBuilder("javac", filePath)
                        .redirectOutput(ProcessBuilder.Redirect.PIPE)
                        .redirectError(ProcessBuilder.Redirect.PIPE)
                        .start();
            } else if (fileName.endsWith(".c")) {
                process = new ProcessBuilder("gcc", filePath)
                        .redirectOutput(ProcessBuilder.Redirect.PIPE)
                        .redirectError(ProcessBuilder.Redirect.PIPE)
                        .start();
            } else {
                throw new UnsupportedOperationException("Unsupported file type: " + fileName);
            }

            int exitCode = process.waitFor();

            // Read stdout and stderr

            String stdout = readStream(process.getInputStream());
            String stderr = readStream(process.getErrorStream());

            // Determine if the code is compilable based on the exit code
            boolean compilable = (exitCode == 0);

            // Delete the temporary file
            FileUtils.cleanDirectory(Paths.get(System.getProperty("user.dir"), relativePath).toFile());

            sourceCode.setCompilable(compilable);
            sourceCode.setStdout(stdout);
            sourceCode.setStderr(stderr);

        } catch (Exception e)
        {
            sourceCode.setStderr(String.valueOf(e));
            sourceCode.setCompilable(false);
        }

        return sourceCode;
    }
    private String readStream(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes());
    }
}
