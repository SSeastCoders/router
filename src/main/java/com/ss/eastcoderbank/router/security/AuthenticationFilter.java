package com.ss.eastcoderbank.router.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
public class AuthenticationFilter implements GatewayFilter {

    final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();

        if(routeValidator.isSecured.test(req)) {
            try {
                if (this.hasAuthHeader(req)) {
                    logger.info("Token missing");
                    return this.onError(exchange, "Missing authorization header", HttpStatus.UNAUTHORIZED);
                }
                final String token = this.getAuthHeader(req);
                //System.out.println(token);
                if (jwtHelper.isExpired(token)) {
                    logger.info("Expired token");
                    return this.onError(exchange, "Authorization expired", HttpStatus.UNAUTHORIZED);
                }
                //this.populateHeaders(exchange, token);
            }catch (JWTDecodeException e){
                logger.info(e.getMessage());
                return this.onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private boolean hasAuthHeader(ServerHttpRequest req) throws JWTDecodeException {
        //return !req.getHeader(JwtUtil.JWT_UTIL.getHeader()).startsWith(JwtUtil.JWT_UTIL.getTokenPrefix());
        return !req.getHeaders().containsKey(JwtUtil.JWT_UTIL.getHeader());
    }

    private String getAuthHeader(ServerHttpRequest req) throws JWTDecodeException {
        return req.getHeaders().getOrEmpty("Authorization").get(0);
       // return req.getHeader(JwtUtil.JWT_UTIL.getHeader());
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){
        ServerHttpResponse res = exchange.getResponse();
        res.setStatusCode(httpStatus);
        return res.setComplete();
    }
}
