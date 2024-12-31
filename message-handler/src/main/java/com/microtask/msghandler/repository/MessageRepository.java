package com.microtask.msghandler.repository;


import com.microtask.msghandler.entity.MessageEntity;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.repository.JpaRepository;
@Observed
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
}
