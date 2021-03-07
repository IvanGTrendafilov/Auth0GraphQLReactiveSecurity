package com.auth0.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(final ServerWebExchange exchange, final SecurityContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Mono<SecurityContext> load(final ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final String token = request.getHeaders().getFirst("TOKEN");
        final Authentication authentication = new UsernamePasswordAuthenticationToken(token, token);
        final Mono<Authentication> authenticate = authenticationManager.authenticate(authentication);
        return authenticate.flatMap(auth-> {
            SecurityContext securityContext= new SecurityContextImpl(auth);
            return Mono.just(securityContext);
        });
    }
}
