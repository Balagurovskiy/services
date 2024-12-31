package com.microtask.msghandler.service;

import com.microtask.msghandler.dto.MessageRequest;
import com.microtask.msghandler.entity.MessageEntity;
import com.microtask.msghandler.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableRetry
public class MessageService {
    private final MessageRepository repository;


    @Retryable(retryFor = SQLException.class, maxAttemptsExpression = "${retry.maxAttempts}")
    public String save(MessageRequest request) throws ExecutionException, InterruptedException {
        return
            CompletableFuture.supplyAsync(() -> {
                        MessageEntity saveReq = new MessageEntity(
                                request.message(),
                                request.tag()
                        );
                        MessageEntity res = repository.save(saveReq);
                        log.info(String.format("Message saved at %s with id : %d",
                                LocalDateTime.now()
                                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")),
                                res.getId()));

                        return  res.getId().toString();
            }).get();
    }

    @Recover
    public String saveRecover(Exception ex, MessageRequest request){
        log.info("Message saving is currently not available! " + ex.getMessage());
        throw new IllegalStateException("Could not save message : " + request);
    }

    public List<MessageEntity> getAll()  {
        return repository.findAll();
    }

    public MessageEntity getOne(Long id)  {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("No message with such id"));
    }
}
