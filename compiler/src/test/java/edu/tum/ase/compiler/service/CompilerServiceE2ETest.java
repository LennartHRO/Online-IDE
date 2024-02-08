package edu.tum.ase.compiler.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
public class CompilerServiceE2ETest {

    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeAll
    public void setUp() {
        // Ensure that the application is running
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/actuator/health", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Application health check failed. Application may not be running properly.");
    }

    @Test
    public void should_CompileJavaSourceCode_Successfully() {
        // given
        String javaCode = "public class Test {}";

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/compile", javaCode, String.class);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add assertions to validate the response body as per your API contract
    }

    @Test
    public void should_HandleCompilationFailure() {
        // given
        String javaCodeWithError = "public class TestWithError { public void methodWithError() { error; } }";

        // when
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/compile", javaCodeWithError, String.class);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        // Add assertions to validate the response body as per your API contract
    }

    @AfterAll
    public void tearDown() {
        // Any cleanup logic, if needed
    }    
}
