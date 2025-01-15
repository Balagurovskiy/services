package com.microtask.msggenerator.service;

import com.microtask.msggenerator.MessageGeneratorTest;
import com.microtask.msggenerator.config.RoutingProperties;
import com.microtask.msggenerator.dto.MessageResponse;
import com.microtask.msggenerator.security.JwtTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(classes = MessageGeneratorTest.class)
@ActiveProfiles("test")
public class MessageSendingServiceTest {

    @Autowired
    private RoutingProperties properties;
    @Autowired
    private HttpHeaderBuilder httpHeaderBuilder;

    private RestTemplate restTemplate = new RestTemplate();

    private MessageSendingService sendingService;

    private MockRestServiceServer mockServer;

    @Value("${test.expected.body}")
    private String expectedResp;
    @Value("${test.expected.header.name}")
    private String expectedHeader;
    @Value("${test.expected.header.value}")
    private String expectedHeaderVal;
    @Value("${test.token}")
    private String testToken;
    @Value("${test.message}")
    private String testMessage;


    @BeforeEach
    public void before(){
        mockServer = MockRestServiceServer.createServer(restTemplate);
        sendingService = new MessageSendingService(httpHeaderBuilder, restTemplate, properties);
    }
    @Test
    public void sendMessage_ExpectedTextBody() throws URISyntaxException {
        String theUrl = String.format("%s://%s/%s" ,
                properties.getProtocol(), properties.getHost(), properties.getUrl());

        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI(theUrl)))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header(expectedHeader, expectedHeaderVal))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(expectedResp));

        String testResp = sendingService.send(testToken, testMessage);

        mockServer.verify();
        Assertions.assertEquals(expectedResp, testResp);

    }
}
