package com.microtask.msggenerator.service;

import com.microtask.msggenerator.config.RoutingProperties;
import com.microtask.msggenerator.dto.MessageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSendingService{

    private final HttpHeaderBuilder httpHeaderBuilder;
    private final RestTemplate restTemplate;
    private final RoutingProperties routs;

    @Value("${spring.application.name}")
    private String appName;

    @Retryable(retryFor = { IllegalStateException.class, ResourceAccessException.class },  maxAttemptsExpression = "${retry.maxAttempts}")
    public String send(String token, String body){
        String res = "";
        String theUrl = String.format("%s://%s/%s" , routs.getProtocol(), routs.getHost(), routs.getUrl());
        log.info("Request :: {} :: {}", theUrl, body);
        try {
            HttpEntity<MessageRequest> entity = new HttpEntity<MessageRequest>(
                    new MessageRequest(body, appName),
                    httpHeaderBuilder.createBearerHeaders(token));
            ResponseEntity<String> response = restTemplate.exchange(
                    theUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            log.info("Response :: status ({}) :: body: {}", response.getStatusCode(), response.getBody());
            res = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() == false) {
                throw new IllegalStateException("Could not save message : " + body);
            }
        }catch(Exception e){
            log.error("Message sending failed : {}", e.getMessage());
            throw new IllegalStateException("Could not save message : " + body);
        }
        return res;
    }
}