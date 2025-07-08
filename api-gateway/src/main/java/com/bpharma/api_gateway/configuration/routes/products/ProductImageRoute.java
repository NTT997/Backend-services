package com.bpharma.api_gateway.configuration.routes.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class ProductImageRoute {
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public RouteLocator routingProductImage(RouteLocatorBuilder builder) {
		return builder
				.routes()
					.route("product-image-management", r -> r
						.path("/private/product-image/**")
						.and()
						.method(HttpMethod.POST,HttpMethod.PUT,HttpMethod.GET,HttpMethod.DELETE,HttpMethod.PATCH)
						.filters(f -> f
								.rewritePath("/private/product-image/(?<segment>.*)", "/api/v1/${segment}")
								.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
						)
						.uri("http://localhost:8080"))
				.build();
	}
	
}
