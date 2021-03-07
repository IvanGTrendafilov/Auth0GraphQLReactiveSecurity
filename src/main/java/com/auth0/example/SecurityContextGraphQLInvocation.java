package com.auth0.example;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Internal;
import graphql.spring.web.reactive.GraphQLInvocation;
import graphql.spring.web.reactive.GraphQLInvocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Primary
@Component
@Internal
public class SecurityContextGraphQLInvocation implements GraphQLInvocation {

    @Autowired
    private GraphQL graphQL;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<ExecutionResult> invoke(final GraphQLInvocationData invocationData, final ServerWebExchange serverWebExchange) {


        final ServerHttpRequest request = serverWebExchange.getRequest();
        final String token = request.getHeaders().getFirst("TOKEN");
        final Authentication authentication = new UsernamePasswordAuthenticationToken(token, token);
        final Mono<Authentication> authenticate = authenticationManager.authenticate(authentication);

        final Mono<SecurityContext> securityContextMono = authenticate.flatMap(auth -> {
            SecurityContext securityContext = new SecurityContextImpl(auth);
            return Mono.just(securityContext);
        });
        return securityContextMono.flatMap(securityContext -> {
            final ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                    .query(invocationData.getQuery())
                    .operationName(invocationData.getOperationName())
                    .variables(invocationData.getVariables())
                    .context(securityContext)
                    .build();

            return Mono.fromCompletionStage(graphQL.executeAsync(executionInput));
        });
    }
}
