package com.microtask.msggenerator.controller;

import com.microtask.msggenerator.service.AuthService;
import com.microtask.msggenerator.service.MessageSendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
public class MessagesController {
    
    private final MessageSendingService sendingService;
    private final AuthService authService;

    @PostMapping(value = "/", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String send(@RequestBody String request) {
        if(Objects.nonNull(authService.getToken())){
            log.info("Sending message :{}", request);
            return sendingService.send(
                    authService.getToken(),
                    request
            );
        } else {
            log.info("No token for message");
        }
        return "Message was not sent";
    }
}
