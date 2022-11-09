package com.testing.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import au.com.dius.pact.core.model.annotations.PactDirectory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactDirectory("src/test/resources/pact")
public class PactConsumerSetDBMessageTest {
    @Pact(provider = "SpringBootDB", consumer = "SpringBootAuth")
    public RequestResponsePact setDbDataIdEmptyRule (PactDslWithProvider builder) {
        return builder
                .uponReceiving("a request to set data from DB with emtpy id")
                .method("POST")
                .headers("id", "")
                .body("{}")
                .path("/set-db-message")
                .willRespondWith()
                .body("Authorization failed")
                .status(HttpStatus.UNAUTHORIZED.value())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "setDbDataIdEmptyRule", port = "8080", pactVersion = PactSpecVersion.V3)
    public void testSetDbDataIdEmpty(MockServer mockServer) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "");

        RestAssured.baseURI = mockServer.getUrl();
        Response response = RestAssured
                .given()
                .headers(headers)
                .body("{}")
                .when()
                .post("/set-db-message");

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode(), "Wrong Status Code");
        assertEquals("Authorization failed", response.getBody().asString(), "Wrong Message");
    }


    @Pact(provider = "SpringBootDB", consumer = "SpringBootAuth")
    public RequestResponsePact setDbDataIdInvalidRule (PactDslWithProvider builder) {
        return builder
                .uponReceiving("a request to set data from DB with invalid id")
                .method("POST")
                .headers("id", "DUMMY")
                .body("{}")
                .path("/set-db-message")
                .willRespondWith()
                .body("Authorization failed")
                .status(HttpStatus.UNAUTHORIZED.value())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "setDbDataIdInvalidRule", port = "8080", pactVersion = PactSpecVersion.V3)
    public void testSetDbDataIdInvalid(MockServer mockServer) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "DUMMY");

        RestAssured.baseURI = mockServer.getUrl();
        Response response = RestAssured
                .given()
                .headers(headers)
                .body("{}")
                .when()
                .post("/set-db-message");

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode(), "Wrong Status Code");
        assertEquals("Authorization failed", response.getBody().asString(), "Wrong Message");
    }

    @Pact(provider = "SpringBootDB", consumer = "SpringBootAuth")
    public RequestResponsePact setDbDataRule (PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        PactDslJsonBody jsonBody = new PactDslJsonBody()
                .stringType("message");

        return builder
                .uponReceiving("a valid request to set data from DB")
                .method("POST")
                .headers(headers)
                .body(jsonBody)
                .path("/set-db-message")
                .willRespondWith()
                .body("Message updated successfully!")
                .status(HttpStatus.OK.value())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "setDbDataRule", port = "8080", pactVersion = PactSpecVersion.V3)
    public void testSetDbData(MockServer mockServer) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        RestAssured.baseURI = mockServer.getUrl();
        Response response = RestAssured
                .given()
                .headers(headers)
                .body("{\"message\":\"Baza de date aleasa este: \"}")
                .when()
                .post("/set-db-message");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode(), "Wrong Status Code");
        assertEquals("Message updated successfully!", response.getBody().asString(), "Wrong Message");
    }

    @Pact(provider = "SpringBootDB", consumer = "SpringBootAuth")
    public RequestResponsePact setDbDataRuleInvalidBody (PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        PactDslJsonBody jsonBody = new PactDslJsonBody().nullValue("message");

        return builder
                .uponReceiving("a request to set data from DB with invalid body")
                .method("POST")
                .headers(headers)
                .body(jsonBody)
                .path("/set-db-message")
                .willRespondWith()
                .body("Missing 'message' field!")
                .status(HttpStatus.OK.value())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "setDbDataRuleInvalidBody", port = "8080", pactVersion = PactSpecVersion.V3)
    public void setDbDataRuleInvalidBody(MockServer mockServer) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        RestAssured.baseURI = mockServer.getUrl();
        Response response = RestAssured
                .given()
                .headers(headers)
                .body("{\"message\": null}")
                .when()
                .post("/set-db-message");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode(), "Wrong Status Code");
        assertEquals("Missing 'message' field!", response.getBody().asString(), "Wrong Message");
    }
}