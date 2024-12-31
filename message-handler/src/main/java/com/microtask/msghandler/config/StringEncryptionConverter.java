package com.microtask.msghandler.config;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.codec.DecodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Converter
public class StringEncryptionConverter implements AttributeConverter<String, String> {
    @Value("${encryption.converter.algorithm}")
    private String ALGORITHM;
    @Value("${encryption.converter.key}")
    private String KEY;

    private Key AES_KEY;

    @PostConstruct
    public void init(){
        AES_KEY = new SecretKeySpec(KEY.getBytes(), "AES");
    }
    @Override
    public String convertToDatabaseColumn(String ccNumber) {
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, AES_KEY);
            return  Base64.getEncoder().encodeToString(c.doFinal(ccNumber.getBytes()));
        } catch (Exception e) {
            throw new DecodingException("String encoding failed", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {

        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, AES_KEY);
            return new String(c.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new DecodingException("String decoding failed", e);
        }
    }
}