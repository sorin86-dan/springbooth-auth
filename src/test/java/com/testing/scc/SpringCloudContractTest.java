package com.testing.scc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@AutoConfigureStubRunner(
        stubsMode = StubRunnerProperties.StubsMode.LOCAL,
        ids = "com.testing:springboot-db:+:stubs:8090")
public class SpringCloudContractTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSetDbMessage() throws Exception {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        mockMvc.perform(MockMvcRequestBuilders.post("/set-db-message")
                        .header("Content-Type", "application/json")
                        .header("id", "OK")
                        .content("{\"message\":\"Baza de date aleasa este: \"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Message updated successfully!"));
    }
    @Test
    public void testSetDbMessageIdEmpty() throws Exception {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "");

        mockMvc.perform(MockMvcRequestBuilders.post("/set-db-message")
                        .header("Content-Type", "application/json")
                        .header("id", "OK")
                        .content("{\"message\":\"Baza de date aleasa este: \"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Authorization failed"));
    }

    @Test
    public void testSetDbMessageIdInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/set-db-message")
                        .header("Content-Type", "application/json")
                        .header("id", "OK")
                        .content("{\"message\":\"Baza de date aleasa este: \"}"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string("Message updated successfully!"));
    }

}
