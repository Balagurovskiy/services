package com.microtask.msggenerator.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    ResponseEntity handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("A server internal error occurs :: " +  ex.getMessage());
    }
//    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    ResponseEntity handleAuthException(Exception ex) {
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body("Please provide valid username and password :: " + ex.getMessage());
//    }
}
