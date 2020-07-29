package com.testing;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@SpringBootTest
public class DefaultAuthTest extends AbstractTestNGSpringContextTests {

    @Test
    public void checkMessageEndpoint() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"db\":\"Redis\"}")
                .post("/auth-message");

        assertEquals(response.getStatusCode(), 500);
        assertTrue(response.getBody().asString().contains("\"error\":\"Internal Server Error\""));
    }

    @Test
    public void checkInvalidEndpoint() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"OK\", \"db\":\"Redis\"}")
                .post("/auth-message2");

        assertEquals(response.getStatusCode(), 404);
        assertTrue(response.getBody().asString().contains("\"error\":\"Not Found\""));
    }

    @Test
    public void checkMessageEndpointInvalidParameter() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("{\"id\":\"NOK\", \"db\":\"Redis\"}")
                .post("/auth-message");

        assertEquals(response.getStatusCode(), 401);
        assertEquals(response.getBody().asString(), "Authorization failed!");
    }

}