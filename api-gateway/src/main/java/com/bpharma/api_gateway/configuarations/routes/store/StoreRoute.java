package com.bpharma.api_gateway.configuarations.routes.store;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import com.bpharma.api_gateway.filters.JwtAuthFilter;

@Configuration
public class StoreRoute {
	
    private final JwtAuthFilter jwtAuthFilter;

    public StoreRoute(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

	@Bean
	public RouteLocator publicStoreRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("store", r -> r
	            .path("/public/store/**")
	            .and()
	            .method(HttpMethod.GET)
	            .filters(f -> f.rewritePath("/public/store/(?<segment>.*)", "/api/v1/store/${segment}"))
	            .uri("http://localhost:8080") 
	        )
	        .build(); 
	}
	
	@Bean
	public RouteLocator privateStoreRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("store-private", r -> r
	            .path("/private/store/**")
	            .and()
	            .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
	            .filters(f -> f
	                .rewritePath("/private/store/(?<segment>.*)", "/api/v1/private/store/${segment}")
	                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())) 
	            )
	            .uri("http://localhost:8080")
	        )
	        .build();
	}
	@Bean
	public RouteLocator privateMerchantRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route("merchant-private", r -> r
	            .path("/private/merchant/**")
	            .and()
	            .method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
	            .filters(f -> f
	                .rewritePath("/private/merchant/(?<segment>.*)", "/api/v1/private/merchant/${segment}")
	                .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())) 
	            )
	            .uri("http://localhost:8080")
	        )
	        .build();
	}
	@Bean
	public RouteLocator privateStoresRouteLocator(RouteLocatorBuilder builder) {
	    return builder.routes()
	    		.route("stores-private-root", r -> r
	    			    .path("/private/stores")
	    			    .and()
	    			    .method(HttpMethod.GET)
	    			    .filters(f -> f
	    			        .rewritePath("/private/stores", "/api/v1/private/stores")
	    			        .filter(jwtAuthFilter.apply(new JwtAuthFilter.Config()))
	    			    )
	    			    .uri("http://localhost:8080")
	    		)
	    		.route("stores-private", r -> r
	    				.path("/private/stores/**")
	    				.and()
	    				.method(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE)
	    				.filters(f -> f
	    						.rewritePath("/private/stores/(?<segment>.*)", "/api/v1/private/stores/${segment}")
	    						.filter(jwtAuthFilter.apply(new JwtAuthFilter.Config())) 
	            )
	    				.uri("http://localhost:8080")
	        ).build();
	}


}
