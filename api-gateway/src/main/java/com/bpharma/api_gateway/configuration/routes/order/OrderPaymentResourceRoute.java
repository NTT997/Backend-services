package com.bpharma.api_gateway.configuration.routes.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class OrderPaymentResourceRoute {

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public RouteLocator routingPublicOrderPaymentResource(RouteLocatorBuilder builder) {
		return builder.routes().route("public-order-resource", r -> r
				.path("/public/order-resource/cart/{code}/payment/init").and().method(HttpMethod.POST).filters(f -> f
						.rewritePath("/public/cart/(?<code>[^/]+)/payment/init", "/api/v1/cart/${code}/payment/init"))
				.uri("http://localhost:8080")).build();

	}

	@Bean
	public RouteLocator routingAuthOrderPaymentResource(RouteLocatorBuilder builder) {
		return builder.routes().route("auth-order-resource",
				r -> r.path("/customer-auth/order-resource/cart/{code}/payment/init").and().method(HttpMethod.POST)
						.filters(f -> f
								.rewritePath("/customer-auth/order-resource/cart/(?<code>[^/]+)/payment/init",
										"/api/v1/auth/cart/${code}/payment/init")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))
				.build();
	}

	@Bean
	public RouteLocator routingPrivateOrderPaymentResource(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("private-order-resource-manage",
						r -> r.path("/private/order-resource/orders/**").and().method(HttpMethod.POST, HttpMethod.GET)
								.filters(f -> f
										.rewritePath("/private/order-resource/orders/(?<segment>.*)",
												"/api/v1/private/orders/${segment}")
										.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
								.uri("http://localhost:8080"))
				.build();
	}
}
