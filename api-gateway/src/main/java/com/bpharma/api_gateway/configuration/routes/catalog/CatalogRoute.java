package com.bpharma.api_gateway.configuration.routes.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class CatalogRoute {
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Bean
	public RouteLocator catalogPrivateRoute(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("catalog-private", r -> r
	            .path("/private/catalog/**")
	            .and()
	            .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
	            .filters(f -> f
	                .rewritePath("/private/catalog/(?<segment>.*)", "/api/v1/private/catalog/${segment}")
	                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
	            )
	            .uri("http://localhost:8080")
	        )
	        .route("catalogs-private", r -> r
	            .path("/private/catalogs")
	            .and()
	            .method(HttpMethod.GET)
	            .filters(f -> f
	                .rewritePath("/private/catalogs", "/api/v1/private/catalogs")
	                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
	            )
	            .uri("http://localhost:8080")
	        )
	        .build();
	}


}
