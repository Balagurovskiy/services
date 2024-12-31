package com.microtask.msghandler.dto;

import com.microtask.msghandler.entity.MessageEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements Converter<MessageEntity, MessageResponse> {
    @Override
    public MessageResponse convert(MessageEntity source) {
        return MessageResponse.builder()
                .message(source.getMessage())
                .createDt(source.createDt)
                .build();
    }
}
