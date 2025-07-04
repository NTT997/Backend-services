package com.bpharma.api_gateway.configuration.routes.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class ProductRoute {
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public RouteLocator routingAdminProduct(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("product-create", r -> r
						.path("/private/product")
						.and()
						.method(HttpMethod.POST)
						.filters(f -> f
								.rewritePath("/private/product", "/api/v1/private/product")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
						)
						.uri("http://localhost:8080"))
					
					.route("product-private", r -> r
						.path("/private/product/**")
						.and()
						.method(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH, HttpMethod.PUT)
						.filters(f -> f
								.rewritePath("/private/product/(?<segment>.*)", "/api/v1/private/product/${segment}")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
						)
						.uri("http://localhost:8080"))
					.route("products-private", r -> r
						.path("/private/products/**")
						.and()
						.method(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH)
						.filters(f -> f
								.rewritePath("/private/products/(?<segment>.*)", "/api/v1/private/products/${segment}")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080")
					)
				.build();
	}
	
	@Bean
	public RouteLocator routingPublicProduct(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("product-public", r-> r
						.path("/public/product/**")
						.and()
						.method(HttpMethod.GET)
						.filters(f -> f.rewritePath("/public/product/(?<segment>.*)", "/api/v1/product/${segment}"))
						.uri("http://localhost:8080")
					)
					.route("products-public", r-> r
							.path("/public/products/**")
							.and()
							.method(HttpMethod.GET, HttpMethod.DELETE )
							.filters(f -> f.rewritePath("/public/products/(?<segment>.*)", "/api/v1/products/${segment}"))
							.uri("http://localhost:8080")
						)
					.build();
					
	}
	@Bean
	public RouteLocator routingAuthProduct(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("product-auth-create" ,r -> r
						.path("/product/auth/**")
						.and()
						.method(HttpMethod.POST)
						.filters(f -> f
								.rewritePath("/product/auth", "/api/v1/auth/product")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
						.uri("http://localhost:8080")
					)	
					.route("product-auth" ,r -> r
							.path("/product/auth/**")
							.and()
							.method(HttpMethod.PUT, HttpMethod.DELETE)
							.filters(f -> f
									.rewritePath("/product/auth/(?<segment>.*)", "/api/v1/auth/product/${segment}")
									.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())))
							.uri("http://localhost:8080")
					)	
				.build();
	}
}
