package com.auth0.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class AuthenticationManager implements ReactiveAuthenticationManager {

    @Qualifier("jwtNormalDecoder")
    @Autowired
    private ReactiveJwtDecoder jwtNormalDecoder;
    @Override
    public Mono<Authentication> authenticate(final Authentication authentication) {
        final String permissionToken = authentication.getCredentials().toString();
        final Mono<Jwt> jwtMono = jwtNormalDecoder.decode(permissionToken)
                .log()
                .doOnNext(System.out::println)
                .doOnSuccess(System.out::println)
                .doOnError(System.out::println);
        return jwtMono
                .flatMap( it-> {
                    final Map<String,Object> claim = it.getClaim("http://localhost:3000/app_metadata");
                    final Authorization authorization = new ObjectMapper().convertValue(claim.get("authorization"), Authorization.class);
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    for (String permission : authorization.getPermissions()) {
                        authorities.add(new SimpleGrantedAuthority(permission));
                    }
                    return Mono.just(
                            new UsernamePasswordAuthenticationToken(
                                    it.getClaim("nickname"),
                                    null,
                                    authorities
                            )
                    );
                });
    }
}
