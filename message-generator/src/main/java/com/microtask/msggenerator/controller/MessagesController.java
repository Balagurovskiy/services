package com.microtask.msggenerator.controller;

import com.microtask.msggenerator.dto.MessageResponse;
import com.microtask.msggenerator.service.AuthService;
import com.microtask.msggenerator.service.MessageSendingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessagesController {
    
    private final MessageSendingService sendingService;
    private final AuthService authService;

    @PostMapping(value = "/", consumes = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String send(@RequestBody String request) {
        return sendingService.send(authService.getToken(), request);
    }

    @GetMapping("/{id}")
    public MessageResponse getItemById(@PathVariable("id") int id) {
        return sendingService.getItemById(authService.getToken(), String.valueOf(id));
    }

    @GetMapping("/all")
    public List<MessageResponse> getAll() {
        return sendingService.getAll(authService.getToken());
    }
}
