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
     @Autowired
	 private JwtAuthFilter jwtAuthFilter;
	
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
    
    @Bean
    public RouteLocator routingAuthCart(RouteLocatorBuilder builder) {
        return builder.routes()
        		.route("auth-shopping-cart", r -> r
        			    .path("/auth/customer/cart")
        			    .and()
        			    .method(HttpMethod.GET)
        			    .filters(f -> f
        			    		.rewritePath("/auth/customer/cart", "/api/v1/auth/customer/cart")
        			    		.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
        			    )
        			    .uri("http://localhost:8080"))    
        		
        		.route("auth-shopping-cart-create", r -> r
        			    .path("/auth/cart")
        			    .and()
        			    .method(HttpMethod.POST)
        			    .filters(f -> f
        			    		.rewritePath("/auth/cart", "/api/v1/auth/cart")
        			    		.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
        			    )
        			    .uri("http://localhost:8080")) 
        		
        		.route("auth-shopping-cart-put", r -> r
        			    .path("/auth/cart/{code}")
        			    .and()
        			    .method(HttpMethod.PUT)
        			    .filters(f -> f
        			    		.rewritePath("/auth/cart/(?<code>[^/]+)", "/api/v1/auth/cart/${code}")
        			    		.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
        			    )
        			    .uri("http://localhost:8080")) 
        		.build();
    }

}
