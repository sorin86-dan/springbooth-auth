package com.testing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.utils.AuthObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.testing.services.AuthService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping(value = "/auth-message")
    public ResponseEntity getMessage(@RequestBody String body) throws IOException, URISyntaxException {
        var jsonBody = new ObjectMapper();
        var authObject = jsonBody.readValue(body, AuthObject.class);
        var content = new HashMap<String, String>() {{
            put("db", authObject.getDb());
        }};
        var authBody = jsonBody.writeValueAsString(content);

        if (!StringUtils.isEmpty(authObject.getId()) && authObject.getId().equals("OK")) {
            return authService.redirectRequest("http://localhost:8080/db-message", authObject.getId(), authBody);
        }

        return new ResponseEntity("Authorization failed", HttpStatus.UNAUTHORIZED);
    }
}
