package com.bpharma.api_gateway.configuration.routes.manufacture;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class ManufacturerRoute {

	private final JwtAuthFilter jwtAuthFilter;

	public ManufacturerRoute(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public RouteLocator publicManufacturerRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes().route("manufacturer", r -> r.path("/public/manufacturer/**").and()
				.method(HttpMethod.GET)
				.filters(f -> f.rewritePath("/public/manufacturer/(?<segment>.*)", "/api/v1/manufacturer/${segment}"))
				.uri("http://localhost:8080"))
				.route("manufacturers",
						r -> r.path("/public/manufacturers").and().method(HttpMethod.GET)
								.filters(f -> f.rewritePath("/public/manufacturers", "/api/v1/manufacturers"))
								.uri("http://localhost:8080"))
				.route("category-manufacturer",
						r -> r.path("/public/category/*/manufacturer").and().method(HttpMethod.GET)
								.filters(f -> f.rewritePath("/public/category/(?<id>[^/]+)/manufacturer",
										"/api/v1/category/${id}/manufacturer"))
								.uri("http://localhost:8080"))
				.build();
	}

	@Bean
	public RouteLocator privateManufacturerRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("manufacturer-private-root", r -> r.path("/private/manufacturer").and().method(HttpMethod.POST)
						.filters(f -> f.rewritePath("/private/manufacturer", "/api/v1/private/manufacturer")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("manufacturer-private", r -> r.path("/private/manufacturer/**").and()
						.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
						.filters(f -> f
								.rewritePath("/private/manufacturer/(?<segment>.*)",
										"/api/v1/private/manufacturer/${segment}")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("manufacturers-private",
						r -> r.path("/private/manufacturers").and().method(HttpMethod.GET)
								.filters(f -> f.rewritePath("/private/manufacturers", "/api/v1/private/manufacturers"))
								.uri("http://localhost:8080"))
				.build();
	}
}