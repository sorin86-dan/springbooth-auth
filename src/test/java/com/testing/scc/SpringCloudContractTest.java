package com.testing.scc;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    @Test
    public void testSetDbMessageIdEmpty() throws Exception {
        URI uri = new URI("http://localhost:8090/set-db-message");
        HttpEntity<String> request = generateRequest("", "{\"message\": \"Baza de date aleasa: \"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authorization failed", result.getBody());
    }

    @Test
    public void testSetDbMessageIdInvalid() throws Exception {
        URI uri = new URI("http://localhost:8090/set-db-message");
        HttpEntity<String> request = generateRequest("DUMMY", "{\"message\": \"Baza de date aleasa: \"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authorization failed", result.getBody());
    }

    @Test
    public void testSetDbMessage() throws Exception {
        URI uri = new URI("http://localhost:8090/set-db-message");
        HttpEntity<String> request = generateRequest("OK", "{\"message\": \"Baza de date aleasa: \"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Message updated successfully!", result.getBody());
    }

    @Test
    public void testSetDbMessageInvalidBody() throws Exception {
        URI uri = new URI("http://localhost:8090/set-db-message");
        HttpEntity<String> request = generateRequest("OK", "{\"message\": null}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Missing 'message' field!", result.getBody());
    }

    @Test
    public void testGetDbMessageIdEmpty() throws Exception {
        URI uri = new URI("http://localhost:8090/get-db-message");
        HttpEntity<String> request = generateRequest("", "{\"db\": \"Redis\"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authorization failed", result.getBody());
    }

    @Test
    public void testGetDbMessageIdInvalid() throws Exception {
        URI uri = new URI("http://localhost:8090/get-db-message");
        HttpEntity<String> request = generateRequest("DUMMY", "{\"db\": \"Redis\"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Authorization failed", result.getBody());
    }

    @Test
    public void testGetDbMessage() throws Exception {
        URI uri = new URI("http://localhost:8090/get-db-message");
        HttpEntity<String> request = generateRequest("OK", "{\"db\": \"Redis\"}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Message updated successfully!", result.getBody());
    }

    @Test
    public void testGetDbMessageInvalidBody() throws Exception {
        URI uri = new URI("http://localhost:8090/get-db-message");
        HttpEntity<String> request = generateRequest("OK", "{\"db\": null}");
        ResponseEntity<String> result = new TestRestTemplate().postForEntity(uri, request, String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Missing 'db' field!", result.getBody());
    }


    @NotNull
    private HttpEntity<String> generateRequest(String ok, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/json");
        headers.set("id", ok);
        return new HttpEntity<>(jsonBody, headers);
    }
}
