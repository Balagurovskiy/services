package com.microtask.msggenerator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartupAuthSetup implements CommandLineRunner {
    private final AuthService authService;

   @Override
    public void run(String...args) throws Exception {
       authService.refreshToken();
    }
}