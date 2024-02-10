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
        Path tempFile = null;

        /*
        String currentDir = Paths.get("").toAbsolutePath().toString();
        String relativePath = "src/main/temp";
        //System.out.println("Current Dir : " + currentDir);
        String filePath = Paths.get(currentDir, relativePath, fileName).toString();
        //String filePath = Paths.get(System.getProperty("user.dir"), relativePath, fileName).toString();*/

        try {
            //Files.writeString(Paths.get(filePath), code);

            // Create a temporary file
            tempFile = Files.createTempFile("compile-", fileName);

            // Write the source code to the temporary file
            Files.writeString(tempFile, code);

            System.out.println("tempFile: " + fileName.toString());
            System.out.println("tempFile: " + tempFile.toString());

            String absolutePath = tempFile.toAbsolutePath().toString();
            System.out.println("Absolute path of the temp file: " + absolutePath);

            Process process;

            if (fileName.endsWith(".java")) {
                // Execute the compilation command
                process = new ProcessBuilder("javac", tempFile.toString())
                        .redirectOutput(ProcessBuilder.Redirect.PIPE)
                        .redirectError(ProcessBuilder.Redirect.PIPE)
                        .start();
            } else if (fileName.endsWith(".c")) {
                process = new ProcessBuilder("gcc", tempFile.toString())
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
            //FileUtils.cleanDirectory(Paths.get(System.getProperty("user.dir"), relativePath).toFile());

            sourceCode.setCompilable(compilable);
            sourceCode.setStdout(stdout);
            sourceCode.setStderr(stderr);

        } catch (Exception e)
        {
            sourceCode.setStderr(String.valueOf(e));
            sourceCode.setCompilable(false);
        } finally {
            // Ensure the temporary file is deleted
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Optionally log this error or handle it further
                }
            }
        }

        return sourceCode;
    }
    private String readStream(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes());
    }
}
