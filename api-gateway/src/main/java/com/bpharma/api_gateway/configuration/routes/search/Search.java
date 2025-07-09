package com.bpharma.api_gateway.configuration.routes.search;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class Search {
    @Bean
    public RouteLocator searchRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("searching", r -> r
        			    .path("/public/search-api/**")
        			    .and()
        			    .method(HttpMethod.GET,HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/public/search-api/(?<segment>.*)", "/api/v1/${segment}"))
        			    .uri("http://localhost:8080"))
                .route("searching-private", r -> r
        			    .path("/search-api/**")
        			    .and()
        			    .method(HttpMethod.GET,HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/search-api/(?<segment>.*)", "/api/v1/${segment}"))
        			    .uri("http://localhost:8080"))
                .build();
    }    
}
