package com.bpharma.api_gateway.configuration.routes.system;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;
@Configuration
public class SystemRoute {
	public SystemRoute() {
	}

	@Bean
	public RouteLocator routingPublicSystem(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("systemroute", r -> r
	            .path("/public/schedules/**")
	            .and()
	            .method(HttpMethod.POST,HttpMethod.GET,HttpMethod.PUT)
	            .filters(f -> f.rewritePath("/public/schedules/(?<segment>.*)", "/api/v1/schedules/${segment}"))
	            .uri("http://localhost:8080") 
	        )
	        .build(); 

	}
}
