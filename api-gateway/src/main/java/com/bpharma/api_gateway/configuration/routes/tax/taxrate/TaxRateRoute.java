package com.bpharma.api_gateway.configuration.routes.tax.taxrate;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class TaxRateRoute {

	private final JwtAuthFilter jwtAuthFilter;

	public TaxRateRoute(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}
	
	@Bean
	public RouteLocator privateTaxRateRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("tax-rate-root", r -> r.path("/private/tax/rate").and()
						.method(HttpMethod.GET, HttpMethod.POST)
						.filters(f -> f.rewritePath("/private/tax/rate", "/api/v1/private/tax/rate")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("tax-rates", r -> r.path("/private/tax/rates").and()
						.method(HttpMethod.GET)
						.filters(f -> f.rewritePath("/private/tax/rates", "/api/v1/private/tax/rates")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("tax-rate", r -> r.path("/private/tax/rate/**").and()
						.method(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
						.filters(f -> f.rewritePath("/private/tax/rate/(?<segment>.*)", "/api/v1/private/tax/rate/${segment}")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.build();
	}
}
