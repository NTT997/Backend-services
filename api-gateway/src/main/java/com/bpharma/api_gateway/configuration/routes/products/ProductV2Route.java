package com.bpharma.api_gateway.configuration.routes.products;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class ProductV2Route {

	private final JwtAuthFilter jwtAuthFilter;

	public ProductV2Route(JwtAuthFilter jwtAuthFilter) {
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public RouteLocator publicProductV2RouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("products-v2-root",
						r -> r.path("/public/v2/products").and().method(HttpMethod.GET)
								.filters(f -> f.rewritePath("/public/v2/products", "/api/v2/products"))
								.uri("http://localhost:8080"))
				.route("products-v2",
						r -> r.path("/public/v2/products/**").and().method(HttpMethod.GET).filters(
								f -> f.rewritePath("/public/v2/products/(?<segment>.*)", "/api/v2/products/${segment}"))
								.uri("http://localhost:8080"))
				.route("product-v2",
						r -> r.path("/public/v2/product/**").and().method(HttpMethod.GET).filters(
								f -> f.rewritePath("/public/v2/product/(?<segment>.*)", "/api/v2/product/${segment}"))
								.uri("http://localhost:8080"))
				.build();
	}

	@Bean
	public RouteLocator privateProductV2RouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("product-v2-private-root", r -> r.path("/private/v2/product").and().method(HttpMethod.POST)
						.filters(f -> f.rewritePath("/private/v2/product", "/api/v2/private/product")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("product-v2-private", r -> r.path("/private/v2/product/**").and()
						.method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH)
						.filters(f -> f
								.rewritePath("/private/v2/product/(?<segment>.*)", "/api/v2/private/product/${segment}")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.build();
	}

}
