package com.microtask.msggenerator;

import com.microtask.msggenerator.security.RsaKeyProperties;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableEncryptableProperties
public class MessageGeneratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MessageGeneratorApplication.class, args);
    }
}
