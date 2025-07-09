package com.bpharma.api_gateway.configuration.routes.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class CustomerReviewRoute {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	
	@Bean
	public RouteLocator routingPublicCustomerReview(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("customer-review", r-> r
						.path("/public/customer-review/{id}/reviews")
						.and()
						.method(HttpMethod.GET)
						.filters(f -> f.rewritePath("/public/customer-review/(?<id>[^/]+)/reviews", "/api/v1/customers/${id}/reviews"))
						.uri("http://localhost:8080"))
					.build();
					
	}
	@Bean
	public RouteLocator routingPrivateCustomerReview(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("private-customer-review-create" ,r -> r
						.path("/private/customer-review/{id}/reviews")
						.and()
						.method(HttpMethod.POST)
						.filters(f -> f
								.rewritePath("/private/customer-review/(?<id>[^/]+)/reviews", "/api/v1/private/customers/${id}/reviews")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080")
					)	
					.route("private-customer-preview-manage", r -> r
							.path("/private/customer-review/{id}/reviews/{reviewid}")
							.and()
							.method(HttpMethod.PUT, HttpMethod.DELETE)
							.filters(f -> f
									.rewritePath("/private/customer-review/(?<id>[^/]+)/reviews/(?<reviewid>[^/]+)", "/api/v1/private/customers/${id}/reviews/${reviewid}")
									.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
							.uri("http://localhost:8080"))	
				.build();
	}
	
	@Bean
	public RouteLocator routingAuthCustomerReview(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("auth-customer-review-create", r -> r
						.path("/customer-auth/customer-review/{id}/reviews")
						.and()
						.method(HttpMethod.POST)
						.filters(f -> f
								.rewritePath("/customer-auth/customer-review/(?<id>[^/]+)/reviews", "/api/v1/auth/customers/${id}/reviews")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080"))			
					.route("auth-customer-review-manage", r -> r
							.path("/customer-auth/customer-review/{id}/reviews/{reviewid}")
							.and()
							.method(HttpMethod.PUT, HttpMethod.DELETE)
							.filters(f -> f
									.rewritePath("/customer-auth/customer-review/(?<id>[^/]+)/reviews/(?<reviewid>[^/]+)", "/api/v1/auth/customers/${id}/reviews/${reviewid}")
									.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
							.uri("http://localhost:8080"))	
				.build();
	}
}
