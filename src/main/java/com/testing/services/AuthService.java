package com.testing.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class AuthService {

    private Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    public ResponseEntity redirectRequest (String url, String id, String body) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity request = RequestEntity.post(new URI(url)).header("id", id).body(body);

        return restTemplate.exchange(request, String.class);
    }

}
