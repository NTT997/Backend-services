package com.bpharma.api_gateway.configuration.routes.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class OrderingRoute {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public RouteLocator routingPublicOrdering(RouteLocatorBuilder builder) {
		return builder.routes().route("public-ordering-create",
				r -> r.path("/public/ordering/cart/{code}/checkout").and().method(HttpMethod.POST).filters(f -> f
						.rewritePath("/public/ordering/cart/(?<code>[^/]+)/checkout", "/api/v1/cart/${code}/checkout"))
						.uri("http://localhost:8080"))
				.build();

	}

	@Bean
	public RouteLocator routingAuthOrdering(RouteLocatorBuilder builder) {
		return builder.routes().route("auth-ordering-create",
				r -> r.path("/customer-auth/ordering/cart/{code}/checkout").and().method(HttpMethod.POST)
						.filters(f -> f
								.rewritePath("/customer-auth/ordering/cart/(?<code>[^/]+)/checkout",
										"/api/v1/auth/cart/${code}/checkout")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("auth-ordering-list", r -> r.path("/customer-auth/ordering/orders").and().method(HttpMethod.GET)
						.filters(f -> f.rewritePath("/customer-auth/ordering/orders", "/api/v1/auth/orders")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("auth-ordering-get", r -> r.path("/customer-auth/ordering/orders/{id}").and()
						.method(HttpMethod.GET)
						.filters(f -> f
								.rewritePath("/customer-auth/ordering/orders/(?<id>[^/]+)", "/api/v1/auth/orders/${id}")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.build();
	}

	@Bean
	public RouteLocator routingPrivateOrdering(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("private-ordering-list", r -> r.path("/private/ordering/orders").and().method(HttpMethod.GET)
						.filters(f -> f.rewritePath("/private/ordering/orders", "/api/v1/private/orders")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.route("private-ordering-manage",
						r -> r.path("/private/ordering/orders/**").and().method(HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.GET, HttpMethod.POST)
								.filters(f -> f
										.rewritePath("/private/ordering/orders/(?<segment>.*)",
												"/api/v1/private/orders/${segment}")
										.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
								.uri("http://localhost:8080"))
				.build();
	}
}
