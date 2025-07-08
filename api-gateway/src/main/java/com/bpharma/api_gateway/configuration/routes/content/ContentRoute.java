package com.bpharma.api_gateway.configuration.routes.content;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class ContentRoute {
    @Autowired
	private JwtAuthFilter jwtAuthFilter;
	
    @Bean
    public RouteLocator contentRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("page-content", r -> r
        			    .path("/private/page-content/**")
        			    .and()
        			    .method(HttpMethod.POST,HttpMethod.GET,HttpMethod.PUT, HttpMethod.DELETE)
        			    .filters(f -> f.rewritePath("/private/page-content/(?<segment>.*)", "/api/v1/private/${segment}").filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
        			    .uri("http://localhost:8080"))       		
        		.route("page-content", r -> r
        			    .path("/public/page-content/**")
        			    .and()
        			    .method(HttpMethod.POST,HttpMethod.GET,HttpMethod.PUT, HttpMethod.DELETE)
        			    .filters(f -> f.rewritePath("/public/page-content/(?<segment>.*)", "/api/v1/${segment}"))
        			    .uri("http://localhost:8080"))     		
                .build();
    }
}
