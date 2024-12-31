package com.microtask.msggenerator.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class HttpHeaderBuilder {
    public HttpHeaders createOathHeaders(String user, String password){
        String notEncoded = user + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(notEncoded.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE));
        headers.add("Authorization", "Basic " + encodedAuth);
        return headers;
    }

    public HttpHeaders createBearerHeaders(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}
