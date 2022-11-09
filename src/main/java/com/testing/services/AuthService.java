package com.testing.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class AuthService {

    public ResponseEntity redirectRequest (String url, String id, String body) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity request = RequestEntity.post(new URI(url)).header("id", id).body(body);
        ResponseEntity response = restTemplate.exchange(request, String.class);

        if(response.getBody().toString().contains("Authorization")) {
            return new ResponseEntity(response.getBody(), HttpStatus.UNAUTHORIZED);
        }
        if(response.getBody().toString().contains("Missing")) {
            return new ResponseEntity(response.getBody(), HttpStatus.BAD_REQUEST);
        }

        return response;
    }
}
