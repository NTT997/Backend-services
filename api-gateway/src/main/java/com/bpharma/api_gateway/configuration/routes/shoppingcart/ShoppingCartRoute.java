package com.bpharma.api_gateway.configuration.routes.shoppingcart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class ShoppingCartRoute {
    // @Autowired
	// private JwtAuthFilter jwtAuthFilter;
	
    @Bean
    public RouteLocator shoppingRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("shopping-cart", r -> r
        			    .path("/public/shoppingcart/**")
        			    .and()
        			    .method(HttpMethod.POST,HttpMethod.GET,HttpMethod.PUT, HttpMethod.DELETE)
        			    .filters(f -> f.rewritePath("/public/shoppingcart/(?<segment>.*)", "/api/v1/${segment}"))
        			    .uri("http://localhost:8080"))       		    		
                .build();
    }
}
