package com.bpharma.api_gateway.configuration.routes.customer;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class CustomerRoute {
    private final JwtAuthFilter jwtAuthFilter;

    CustomerRoute(JwtAuthFilter jwtAuthFilter){
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public RouteLocator publicCustomerRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("customer-auth-service", r -> r
	            .path("/public/customer-auth/**")
	            .and()
	            .method(HttpMethod.GET,HttpMethod.POST,HttpMethod.PATCH)
	            .filters(f -> f.rewritePath("/public/customer-auth/(?<segment>.*)", "/api/v1/customer/${segment}")
                 )
	            .uri("http://localhost:8080") 
                   
            )
	        .build(); 
    }

    @Bean
    public RouteLocator privateCustomerRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("customer-service", r -> r
	            .path("/private/customer-service/**")
	            .and()
	            .method(HttpMethod.GET,HttpMethod.POST,HttpMethod.PATCH)
	            .filters(f -> f.rewritePath("/private/customer-service/(?<segment>.*)", "/api/v1/auth/customer/${segment}")
                  .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())) )
	            .uri("http://localhost:8080") 
                   
            )
			.route("customer-admin-service", r -> r
	            .path("/private/admin-customer-service/**")
	            .and()
	            .method(HttpMethod.GET,HttpMethod.POST,HttpMethod.PATCH,HttpMethod.DELETE, HttpMethod.PUT)
	            .filters(f -> f.rewritePath("/private/admin-customer-service/(?<segment>.*)", "/api/v1/private/${segment}")
                  .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())) )
	            .uri("http://localhost:8080") 
                   
            )
	        .build(); 
    }
}
