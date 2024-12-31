package com.microtask.msggenerator.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class RoutingProperties {
    @Value("${routing.message.url}")
    private String url;

    @Value("${routing.message.host}")
    private String host;

    @Value("${routing.message.protocol}")
    private String protocol;
}
