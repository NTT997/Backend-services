package com.bpharma.api_gateway.configuration.routes.securities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class SecurityRoute {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public RouteLocator permissionPrivateRoute(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("permission-private", r-> r
							.path("/private/security/**")
							.and()
							.method(HttpMethod.GET)
							.filters(f->f
									.rewritePath("/private/security/(?<segment>.*)", "/api/v1/sec/private/${segment}")
					                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
					        )
							.uri("http://localhost:8080"))
				.build();
	}
}
