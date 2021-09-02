package com.ss.eastcoderbank.router.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@RefreshScope
public class AuthenticationFilter implements GatewayFilter {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();

        if(routeValidator.isSecured.test(req)) {
            if(this.hasAuthHeader(req)){
                return this.onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
            }

            final String token = this.getAuthHeader(req);

            if(jwtHelper.isExpired(token)){
                return this.onError(exchange, "Authorization expired", HttpStatus.UNAUTHORIZED);
            }

            this.populateHeaders(exchange, token);
        }
        return chain.filter(exchange);
    }



    private boolean hasAuthHeader(ServerHttpRequest request){
        return !request.getHeaders().containsKey("Authorization");
    }

    private String getAuthHeader(ServerHttpRequest req){
        return req.getHeaders().getOrEmpty("Authorization").get(0);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse res = exchange.getResponse();
        res.setStatusCode(httpStatus);
        return res.setComplete();
    }

    private void populateHeaders(ServerWebExchange exchange, String token){
        Claims claims = jwtHelper.getClaims(token);
        exchange.getRequest().mutate()
                .header("id", String.valueOf(claims.get("sub")))
                .header("role", String.valueOf(claims.get("role")))
                .header("exp", String.valueOf(claims.get("exp")))
                .header("username", String.valueOf(claims.get("username")))
        ;
    }

}
