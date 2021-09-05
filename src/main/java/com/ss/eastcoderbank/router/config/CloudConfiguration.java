package com.ss.eastcoderbank.router.config;
import com.ss.eastcoderbank.router.security.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudConfiguration {

    @Autowired
    AuthenticationFilter filter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder rlb){
        return rlb.routes()
                .route(r -> r.path("/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://USERS-API"))
                .route(r -> r.path("/api/v1/accounts/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ACCOUNT-API"))
                .route(r -> r.path("/api/v1/transactions/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://TRANSACTION-API"))
                .route(r -> r.path("/credit-cards/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://CARD-API"))
                .build();
    }

}
