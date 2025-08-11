package com.bpharma.api_gateway.configuration.routes.system;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class SystemConfigRoute {

	private final JwtAuthFilter jwtAuthFilter;

	public SystemConfigRoute(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public RouteLocator routingPrivateSystemConfig(RouteLocatorBuilder builder) {
	    return builder.routes()
	            .route("private-system-config", r -> r
	                    .path("/private/system/configuration/**")
	                    .and()
	                    .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
	                    .filters(f -> f
	                        .rewritePath(
	                            "/private/system/configuration(?<segment>/.*|$)",
	                            "/api/v1/system/configuration${segment}"
	                        )
	                        .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
	                    )
	                    .uri("http://localhost:8080"))
	            .build();
	}


}
