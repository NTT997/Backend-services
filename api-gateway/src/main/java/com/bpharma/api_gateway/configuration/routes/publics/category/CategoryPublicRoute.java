package com.bpharma.api_gateway.configuration.routes.publics.category;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

//Configures routes for public, non-authenticated services (e.g., public info, customer login)
@Configuration
public class CategoryPublicRoute {
    @Bean
    public RouteLocator publicRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("category-getall", r -> r
        			    .path("/public/category/getall")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/public/category/getall", "/api/v1/category"))
        			    .uri("http://localhost:8080"))        		    		
                .build();
    }
}
