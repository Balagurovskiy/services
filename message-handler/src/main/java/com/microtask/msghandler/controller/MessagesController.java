package com.microtask.msghandler.controller;

import com.microtask.msghandler.dto.MessageMapper;
import com.microtask.msghandler.dto.MessageRequest;
import com.microtask.msghandler.dto.MessageResponse;
import com.microtask.msghandler.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
public class MessagesController {
    private final MessageService service;
    private final MessageMapper mapper;

    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveMsg(@RequestBody MessageRequest request) throws ExecutionException, InterruptedException {
        return service.save(request);
    }

    @GetMapping(value = "/all")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageResponse> getMsg() {
        return service.getAll().stream()
                        .map(mapper::convert)
                        .collect(Collectors.toList());
    }

    @GetMapping(value = "/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse getMsg(@PathVariable Long id) {
        return mapper.convert(service.getOne(id));
    }
}
