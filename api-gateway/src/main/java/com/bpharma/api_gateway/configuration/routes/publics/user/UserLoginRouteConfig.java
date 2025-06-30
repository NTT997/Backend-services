package com.bpharma.api_gateway.configuration.routes.publics.user;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

// Configures routes for authentication-related services (e.g., admin login, token endpoints)
@Configuration
public class UserLoginRouteConfig {
    @Bean
    public RouteLocator userPrivateRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("user-login", r -> r
        			    .path("/user/login")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/user/login", "/api/v1/private/login"))
        			    .uri("http://localhost:8080"))
                .build();
    }
}