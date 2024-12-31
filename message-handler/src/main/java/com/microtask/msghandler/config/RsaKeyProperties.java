package com.microtask.msghandler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;
@ConfigurationProperties(prefix = "security.token.rsa")
public record RsaKeyProperties(RSAPublicKey publicKey) {
}