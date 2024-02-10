package edu.tum.ase.compiler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tum.ase.compiler.model.SourceCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CompilerServiceE2ETest {
    private final String URL = "/api/compile/";
    @Autowired
    private MockMvc systemUnderTest;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCompilable() throws Exception {
// given
        SourceCode sourceCode = new SourceCode("test.c","#include<stdio.h>\n" +
                "\n" +
                "int main() {\n" +
                "\tprintf(\"Hello World\\n\");\n" +
                "\treturn 0;\n" +
                "}");

// when
        ResultActions result = systemUnderTest.perform(post(URL)
                .content(objectMapper.writeValueAsString(sourceCode))
                .contentType(MediaType.APPLICATION_JSON));
// then
        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.compilable").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stdout").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stderr").value(""));
    }

    @Test
    public void testNotCompilable() throws Exception {
// given
        SourceCode sourceCode = new SourceCode("test.c","#include<stdio.h>\n" +
                "\n" +
                "int main() {\n" +
                "\tprintf(\"Hello World\\n\");\n" +
                "\treturn 0\n" +
                "}");

// when
        ResultActions result = systemUnderTest.perform(post(URL)
                .content(objectMapper.writeValueAsString(sourceCode))
                .contentType(MediaType.APPLICATION_JSON));
// then
        result
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.compilable").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stdout").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.stderr").isString());
    }



}
