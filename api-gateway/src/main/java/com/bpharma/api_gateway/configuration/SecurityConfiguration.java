package com.bpharma.api_gateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import com.bpharma.api_gateway.security.JwtAuthenticationConverter;
import com.bpharma.api_gateway.security.JwtAuthenticationManager;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

	@Autowired
    private JwtAuthenticationManager jwtAuthenticationManager;
	
	@Autowired
    private JwtAuthenticationConverter jwtAuthenticationConverter;
	 
	@Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/public/**", "/actuator/**").permitAll()
                .pathMatchers("/private/**").authenticated()
                .pathMatchers("/auth/**").authenticated()
                .anyExchange().authenticated()
            )
            .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .csrf(csrf -> csrf.disable())
            .build();
    }
    
	@Bean
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtAuthenticationManager);
        filter.setServerAuthenticationConverter(jwtAuthenticationConverter);
        return filter;
    }
}
