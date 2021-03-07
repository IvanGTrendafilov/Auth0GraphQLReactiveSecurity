package com.auth0.example;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TestService {

    @PreAuthorize("hasAuthority('read:all:projects')")
    public Mono<String> hello(){
        return Mono.just("TEST 123");
    }
}
