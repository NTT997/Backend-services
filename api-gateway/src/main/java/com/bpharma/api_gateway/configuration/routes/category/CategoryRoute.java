package com.bpharma.api_gateway.configuration.routes.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class CategoryRoute {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
    @Bean
    public RouteLocator publicRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("category-public-create", r -> r
        			    .path("/public/category")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/public/category", "/api/v1/category"))
        			    .uri("http://localhost:8080"))
        		.route("category-public", r -> r
        			    .path("/public/category/**")
        			    .and()
        			    .method(HttpMethod.GET)
        			    .filters(f -> f.rewritePath("/public/category/(?<segment>.*)", "/api/v1/category/${segment}"))
        			    .uri("http://localhost:8080"))        		    		
                .build();
    }
    @Bean
    public RouteLocator privateRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("category-create", r -> r
        			    .path("/private/category")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f
        			    		.rewritePath("/private/category", "/api/v1/private/category")
				                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
        			    )
        			    .uri("http://localhost:8080")) 
        		.route("category-private", r -> r
        			    .path("/private/category/**")
        			    .and()
        			    .method(HttpMethod.GET, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT)
        			    .filters(f -> f.rewritePath("/private/category/(?<segment>.*)", "/api/v1/private/category/${segment}"))
        			    .uri("http://localhost:8080"))        		    		
                .build();
    }
}
