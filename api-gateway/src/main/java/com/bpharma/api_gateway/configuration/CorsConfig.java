//package com.bpharma.api_gateway.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsWebFilter;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//
//@Configuration
//public class CorsConfig {
//
//	@Bean
//	public CorsWebFilter corWebFilter() {
//		CorsConfiguration corsConfig = new CorsConfiguration();
////		corsConfig.addAllowedOrigin("http://localhost:5174");
//		corsConfig.addAllowedOriginPattern("http://localhost:5174");
//		corsConfig.addAllowedMethod("*");
//		corsConfig.addAllowedHeader("*");
//		corsConfig.setAllowCredentials(true); //gui kem cookie
//		
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", corsConfig);
//		
//		return new CorsWebFilter(source);
//	}
//}
package com.bpharma.api_gateway.configuration;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

	private static final List<String> ALLOWED_ORIGINS = List.of("http://localhost:5173","http://localhost:5174" ,// React web app
			"http://localhost:19006", // React Native Expo web preview
			"http://localhost:8081"
	);

	@Bean
	public CorsWebFilter corWebFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();

		corsConfig.setAllowedOrigins(ALLOWED_ORIGINS);
		corsConfig.addAllowedMethod("*");
		corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		corsConfig.addAllowedHeader("*");
		corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
		corsConfig.setAllowCredentials(true); // gui kem cookie

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsWebFilter(source);
	}
}