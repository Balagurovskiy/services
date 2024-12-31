package com.microtask.msggenerator.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;


import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class EncryptionConfiguration {
    @Bean
    public JwtEncoder jwtEncoder(RsaKeyProperties keyProperties) {
        JWK jwk = new RSAKey
                .Builder(keyProperties.publicKey())
                .privateKey(keyProperties.privateKey())
                .build();
        JWKSource<SecurityContext> jwkSet = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(RsaKeyProperties keyProperties) {
        return NimbusJwtDecoder.withPublicKey(keyProperties.publicKey()).build();
    }
}
