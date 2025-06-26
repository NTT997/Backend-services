package com.bpharma.api_gateway.configuration.routes.publics.references;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

//Configures routes for public, non-authenticated services (e.g., public info, customer login)
@Configuration
public class CountryPublicRoute {
    @Bean
    public RouteLocator countryPublicRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("country", r -> r
        			    .path("/public/country")
        			    .and()
        			    .method(HttpMethod.GET)
        			    .filters(f -> f.rewritePath("/public/country", "/api/v1/country"))
        			    .uri("http://localhost:8080"))        		    		
                .build();
    }
}
