package com.ss.eastcoderbank.router.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> unsecuredEndPoints = List.of(
          "users/login",
          "users"
    );

    public Predicate<ServerHttpRequest> isSecured =
            (request -> unsecuredEndPoints.stream()
            .noneMatch(uri -> request.getURI().getPath().contains(uri))
            );
}
