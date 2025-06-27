package com.bpharma.api_gateway.configuration.routes.publics.customer;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

//Configures routes for public, non-authenticated services (e.g., public info, customer login)
@Configuration
public class LoginRegisterRoute {
    @Bean
    public RouteLocator loginPublicRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("customer-login", r -> r
        			    .path("/public/customer/login")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/public/customer/login", "/api/v1/customer/login"))
        			    .uri("http://localhost:8080"))
        		.route("customer-register", r -> r
        			    .path("/public/customer/register")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/public/customer/register", "/api/v1/customer/register"))
        			    .uri("http://localhost:8080"))        		
                .build();
    }
}
