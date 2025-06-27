package com.bpharma.api_gateway.configuration.routes.publics.customer;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

//Configures routes for public, non-authenticated services (e.g., public info, customer login)
@Configuration
public class ForgotPasswordRoute {
    @Bean
    public RouteLocator passwordPublicRoutes(RouteLocatorBuilder builder) {
        return builder.routes()        		
        		.route("customer-forgot-password", r -> r
        			    .path("/public/customer/forgot-password")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/public/customer/forgot-password", "/api/v1/customer/password/reset/request"))
        			    .uri("http://localhost:8080"))        		
        		.route("customer-new-password", r -> r
        			    .path("/public/customer/new-password")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/public/customer/new-password", "/api/v1/customer/password/new-password"))
        			    .uri("http://localhost:8080"))
                .build();
    }
}
