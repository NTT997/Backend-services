package com.bpharma.api_gateway.configuration.routes.payment;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class PaymentRoutes {
    private final JwtAuthFilter jwtAuthFilter;
	
    public PaymentRoutes(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public RouteLocator paymentRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("payment", r -> r
	            .path("/private/payment/**")
	            .and()
	            .method(HttpMethod.GET,HttpMethod.POST)
	            .filters(f -> f.rewritePath("/private/payment/(?<segment>.*)", "/api/v1/private/${segment}")
                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
                )
	            .uri("http://localhost:8080") 
	        )
	        .build(); 
    }
}
