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
                //.route(r -> r.path("/users/**")
                //        .uri("lb://USERS-API"))
                        //.id("users-api"))
               // .route(r -> r.path("/api/v1/accounts/**")
                //        .uri("lb://ACCOUNT-API"))
                       // .id("account-api"))
               // .route(r -> r.path("/api/v1/transactions/**")
                //        .uri("lb://TRANSACTION-API"))
                       // .id("transactions-api"))
                .route(r -> r.path("/credit-cards/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://CARD-API"))
                       // .id("card-api"))
               // .route(r -> r.path("/debit-cards/**")
                       // .id("card-api"))
                //.route(r -> r.path("*")
                //        .uri("localhost:9999/"))
                .build();
    }

}
