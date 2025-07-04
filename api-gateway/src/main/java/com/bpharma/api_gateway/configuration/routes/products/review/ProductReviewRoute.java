package com.bpharma.api_gateway.configuration.routes.products.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class ProductReviewRoute {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public RouteLocator routingAuthProductReview(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("auth-product-preview-create", r -> r
						.path("/auth/product-review/{id}/reviews")
						.and()
						.method(HttpMethod.POST)
						.filters(f -> f
								.rewritePath("/auth/product-review/(?<id>[^/]+)/reviews", "/api/v1/auth/products/${id}/reviews")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))						)
						.uri("http://localhost:8080"))			
					.route("auth-product-preview-manage", r -> r
							.path("/auth/product-review/{id}/reviews/{reviewid}")
							.and()
							.method(HttpMethod.PUT, HttpMethod.DELETE)
							.filters(f -> f
									.rewritePath("/auth/product-review/(?<id>[^/]+)/reviews/(?<reviewid>[^/]+)", "/api/v1/auth/products/${id}/reviews/${reviewid}")
									.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
							.uri("http://localhost:8080"))	
				.build();
	}
	
	@Bean
	public RouteLocator routingPublicProductReview(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("product-review", r-> r
						.path("/public/product-review/{id}/reviews")
						.and()
						.method(HttpMethod.GET)
						.filters(f -> f.rewritePath("/public/product-review/(?<id>[^/]+)/reviews", "/api/v1/products/${id}/reviews"))
						.uri("http://localhost:8080"))
					.build();
					
	}
	@Bean
	public RouteLocator routingPrivateProductReview(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("private-product-review-create" ,r -> r
						.path("/private/product-review/{id}/reviews")
						.and()
						.method(HttpMethod.POST)
						.filters(f -> f
								.rewritePath("/private/product-review/(?<id>[^/]+)/reviews", "/api/v1/private/products/${id}/reviews")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080")
					)	
					.route("private-product-preview-manage", r -> r
							.path("/private/product-review/{id}/reviews/{reviewid}")
							.and()
							.method(HttpMethod.PUT, HttpMethod.DELETE)
							.filters(f -> f
									.rewritePath("/private/product-review/(?<id>[^/]+)/reviews/(?<reviewid>[^/]+)", "/api/v1/private/products/${id}/reviews/${reviewid}")
									.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
							.uri("http://localhost:8080"))	
				.build();
	}
}
