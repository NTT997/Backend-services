package com.bpharma.api_gateway.configuration.routes.tax.taxclass;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class TaxClassRoute {

	private final JwtAuthFilter jwtAuthFilter;

	public TaxClassRoute(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}
	
	@Bean
	public RouteLocator privateTaxClassRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("tax-class-root", r -> r.path("/private/tax/class").and()
						.method(HttpMethod.GET, HttpMethod.POST)
						.filters(f -> f.rewritePath("/private/tax/class", "/api/v1/private/tax/class")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("tax-class", r -> r.path("/private/tax/class/**").and()
						.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
						.filters(f -> f.rewritePath("/private/tax/class/(?<segment>.*)", "/api/v1/private/tax/class/${segment}")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.build();
	}
}
