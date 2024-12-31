package com.microtask.msggenerator.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
@SuppressWarnings("unused")
public class KeyConfiguration {
    @Value("${security.key.algorithm}")
    private String algorithm;

    @Value("${security.key.size}")
    private int keysize;

    @Bean
    public KeyPair kayPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keysize);
        return keyPairGenerator.generateKeyPair();
    }
}
