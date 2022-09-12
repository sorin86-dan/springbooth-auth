package com.testing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.utils.AuthObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.testing.services.AuthService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @Value("${db.ip.address:localhost}")
    private String dbIpAddress;

    @PostMapping(value = "/get-message")
    public ResponseEntity getMessage(@RequestBody String body) throws IOException, URISyntaxException {
        var jsonBody = new ObjectMapper();
        var authObject = jsonBody.readValue(body, AuthObject.class);
        var content = new HashMap<String, String>() {{
            put("db", authObject.getDb());
        }};
        var authBody = jsonBody.writeValueAsString(content);

        if (StringUtils.isNotBlank(authObject.getId()) && "OK".equals(authObject.getId())) {
            return authService.redirectRequest("http://" + dbIpAddress + ":8082/get-db-message", authObject.getId(), authBody);
        }

        return new ResponseEntity("Authorization failed", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/set-message")
    public ResponseEntity setMessage(@RequestBody String body) throws IOException, URISyntaxException {
        var jsonBody = new ObjectMapper();
        var authObject = jsonBody.readValue(body, AuthObject.class);
        var content = new HashMap<String, String>() {{
            put("db", authObject.getDb());
            put("message", authObject.getMessage());
        }};
        var authBody = jsonBody.writeValueAsString(content);

        if (StringUtils.isNotBlank(authObject.getId()) && "OK".equals(authObject.getId())) {
            return authService.redirectRequest("http://" + dbIpAddress + ":8082/set-db-message", authObject.getId(), authBody);
        }

        return new ResponseEntity("Authorization failed", HttpStatus.UNAUTHORIZED);
    }

}
