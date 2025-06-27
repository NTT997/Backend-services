package com.bpharma.api_gateway.configuration.routes.references;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

//Configures routes for public, non-authenticated services (e.g., public info, customer login)
@Configuration
public class ReferencePublicRoute {
    @Bean
    public RouteLocator referencePublicRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("country", r -> r
        			    .path("/public/country")
        			    .and()
        			    .method(HttpMethod.GET)
        			    .filters(f -> f.rewritePath("/public/country", "/api/v1/country"))
        			    .uri("http://localhost:8080"))
        		.route("languages", r -> r
        				.path("/public/languages")
        				.and()
        				.method(HttpMethod.GET)
        				.filters(f -> f.rewritePath("/public/languages", "/api/v1/languages"))
        				.uri("http://localhost:8080"))
        		.route("currency", r-> r
        				.path("/public/currency")
        				.and()
        				.method(HttpMethod.GET)
        				.filters(f->f.rewritePath("/public/currency", "/api/v1/currency"))
        				.uri("http://localhost:8080"))
        		.route("measures", r-> r
        				.path("/public/measures")
        				.and()
        				.method(HttpMethod.GET)
        				.filters(f->f.rewritePath("/public/measures", "/api/v1/measures"))
        				.uri("http://localhost:8080"))
        		.route("zones", r-> r
        				.path("/public/zones")
        				.and()
        				.method(HttpMethod.GET)
        				.filters(f->f.rewritePath("/public/zones", "/api/v1/zones"))
        				.uri("http://localhost:8080"))
                .build();
    }

}


