package com.microtask.msghandler;

import com.microtask.msghandler.config.RsaKeyProperties;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableEncryptableProperties
public class MessageHandlerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageHandlerApplication.class, args);
    }
}
