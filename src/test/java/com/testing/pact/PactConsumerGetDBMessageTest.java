package com.testing.pact;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
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
public class PactConsumerGetDBMessageTest {
    @Pact(provider = "SpringBootDB", consumer = "SpringBootAuth")
    public RequestResponsePact getDbDataIdEmptyRule (PactDslWithProvider builder) {
        return builder
                .uponReceiving("a request to get data from DB with emtpy id")
                .method("POST")
                .headers("id", "")
                .body("{}")
                .path("/get-db-message")
                .willRespondWith()
                .body("Authorization failed")
                .status(HttpStatus.UNAUTHORIZED.value())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getDbDataIdEmptyRule", pactVersion = PactSpecVersion.V3)
    public void testGetDbDataIdEmpty(MockServer mockServer) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "");

        RestAssured.baseURI = mockServer.getUrl();
        Response response = RestAssured
                .given()
                .headers(headers)
                .body("{}")
                .when()
                .post("/get-db-message");

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode(), "Wrong Status Code");
        assertEquals("Authorization failed", response.getBody().asString(), "Wrong Message");
    }

    @Pact(provider = "SpringBootDB", consumer = "SpringBootAuth")
    public RequestResponsePact getDbDataIdInvalidRule (PactDslWithProvider builder) {
        return builder
                .uponReceiving("a request to get data from DB with invalid id")
                .method("POST")
                .headers("id", "DUMMY")
                .body("{}")
                .path("/get-db-message")
                .willRespondWith()
                .body("Authorization failed")
                .status(HttpStatus.UNAUTHORIZED.value())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getDbDataIdInvalidRule", pactVersion = PactSpecVersion.V3)
    public void testGetDbDataIdInvalid(MockServer mockServer) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "DUMMY");

        RestAssured.baseURI = mockServer.getUrl();
        Response response = RestAssured
                .given()
                .headers(headers)
                .body("{}")
                .when()
                .post("/get-db-message");

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode(), "Wrong Status Code");
        assertEquals("Authorization failed", response.getBody().asString(), "Wrong Message");
    }

    @Pact(provider = "SpringBootDB", consumer = "SpringBootAuth")
    public RequestResponsePact getDbDataRule (PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        PactDslJsonBody jsonBody = new PactDslJsonBody().stringType("db", "Redis");

        return builder
                .uponReceiving("a valid request to get data from DB")
                .method("POST")
                .headers(headers)
                .body(jsonBody)
                .path("/get-db-message")
                .willRespondWith()
                .body("The chosen database is: Redis")
                .status(HttpStatus.OK.value())
                .toPact();
    }


    @Test
    @PactTestFor(pactMethod = "getDbDataRule", pactVersion = PactSpecVersion.V3)
    public void testGetDbData(MockServer mockServer) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        RestAssured.baseURI = mockServer.getUrl();
        Response response = RestAssured
                .given()
                .headers(headers)
                .body("{\"db\": \"Redis\"}")
                .when()
                .post("/get-db-message");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode(), "Wrong Status Code");
        assertEquals("The chosen database is: Redis", response.getBody().asString(), "Wrong Message");
    }

    @Pact(provider = "SpringBootDB", consumer = "SpringBootAuth")
    public RequestResponsePact getDbDataRuleInvalidBody (PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        PactDslJsonBody jsonBody = new PactDslJsonBody().nullValue("db");

        return builder
                .uponReceiving("a request to get data from DB with emtpy db")
                .method("POST")
                .headers(headers)
                .body(jsonBody)
                .path("/get-db-message")
                .willRespondWith()
                .body("Missing 'db' field!")
                .status(HttpStatus.OK.value())
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getDbDataRuleInvalidBody", pactVersion = PactSpecVersion.V3)
    public void testGetDbDataInvalidBody(MockServer mockServer) {
        Map<String, String> headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("id", "OK");

        RestAssured.baseURI = mockServer.getUrl();
        Response response = RestAssured
                .given()
                .headers(headers)
                .body("{\"db\": null}")
                .when()
                .post("/get-db-message");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode(), "Wrong Status Code");
        assertEquals("Missing 'db' field!", response.getBody().asString(), "Wrong Message");
    }
}