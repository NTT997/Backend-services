package com.bpharma.api_gateway.configuration.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Configures routes for authentication-related services (e.g., login, token endpoints)
@Configuration
public class AuthRouteConfig {
	@Bean
	public RouteLocator authRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
					.route("auth-service", r -> r.path("/auth/**")
							.filters(f -> f.stripPrefix(1))
							.uri("http://localhost:8080"))
					.build();
	}
}
