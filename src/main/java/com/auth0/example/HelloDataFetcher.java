package com.auth0.example;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Component
public class HelloDataFetcher implements DataFetcher<CompletableFuture<String>> {

    @Autowired
    private TestService testService;

    @Override
    public CompletableFuture<String> get(final DataFetchingEnvironment environment) throws Exception {
        final SecurityContext environmentContext = environment.getContext();
        System.out.println(environmentContext+ " ENVIRONMENT CONTEXT");
        final Mono<SecurityContext> context = ReactiveSecurityContextHolder.getContext();
        return context.flatMap(securityContext -> testService.hello()).toFuture();
    }
}
