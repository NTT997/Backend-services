package com.bpharma.api_gateway.configuration.routes.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class ShippingRoute {
     @Autowired
	 private JwtAuthFilter jwtAuthFilter;
	
    @Bean
    public RouteLocator routingPublicShipping(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("shipping-public", r -> r
        			    .path("/public/cart/**")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f.rewritePath("/public/cart/(?<segment>.*)", "/api/v1/cart/${segment}"))
        			    .uri("http://localhost:8080"))       		    		
                .build();
    }
    
    @Bean
    public RouteLocator routingAuthShipping(RouteLocatorBuilder builder) {
    	return builder
    			.routes()
    				.route("auth-shipping", r -> r
    						.path("/auth/cart/**")
    						.and()
    						.method(HttpMethod.GET)
    						.filters(f -> f
    								.rewritePath("/auth/cart/(?<segment>.*)", "/api/v1/auth/cart/${segment}")
    								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
    						)
    						.uri("http://localhost:8080")
    				)
    			.build();
    }
}
