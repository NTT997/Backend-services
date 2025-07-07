package com.bpharma.api_gateway.configuration.routes.user;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class UserRoute {
	private final JwtAuthFilter jwtAuthFilter;
	
    public UserRoute(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }
    @Bean
    public RouteLocator publicUserRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("user", r -> r
	            .path("/public/user/**")
	            .and()
	            .method(HttpMethod.GET)
	            .filters(f -> f.rewritePath("/public/store/(?<segment>.*)", "/api/v1/store/${segment}"))
	            .uri("http://localhost:8080") 
	        )
	        .build(); 
    }
    @Bean
	public RouteLocator privateUserRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("user-private", r -> r
	            .path("/private/user-service/**")
                .and()
	            .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE,HttpMethod.PATCH)
	            .filters(f -> f
	                .rewritePath("/private/user-service/(?<segment>.*)", "/api/v1/private/${segment}")       
	                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())) 
	            )
	            .uri("http://localhost:8080")
	        )
	        .build();
	}
    @Bean
	public RouteLocator privateAuthenticationLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("user-authen", r -> r
	            .path("/private/auth/**")
	            .and()
	            .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
	            .filters(f -> f
                    .rewritePath("/private/auth/token/(?<segment>.*)", "/api/v1/auth/${segment}")
	                .rewritePath("/private/auth/(?<segment>.*)", "/api/v1/private/${segment}")
                    
	            )
	            .uri("http://localhost:8080")
	        )
	        .build();
	}
}
