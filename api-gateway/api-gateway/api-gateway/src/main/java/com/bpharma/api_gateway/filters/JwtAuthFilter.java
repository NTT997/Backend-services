package com.bpharma.api_gateway.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.bpharma.api_gateway.Util.JwtUtil;

import io.jsonwebtoken.Claims;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config>{
	@Autowired
	private JwtUtil jwtUtil;
	
	
	public JwtAuthFilter() {
		super(Config.class);
	}
	
	public static class Config {
		
	}

	@Override
	public GatewayFilter apply(Config config) {
		System.out.println("da vao apply");
		return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().toString();
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            System.out.println(">> [JwtAuthFilter] Path: " + path);
            System.out.println(">> [JwtAuthFilter] Authorization Header: " + authHeader);
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            	exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            	return exchange.getResponse().setComplete();
            }
            
            String token = authHeader.substring(7);
           
            if(!validateToken(token)) {
            	System.out.println("validate token failed!");
            	exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            	return exchange.getResponse().setComplete();     			
            }
            System.out.println("token hop le: " + token);
            return chain.filter(exchange);
		};
	}
	
    private boolean validateToken(String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        System.out.println("Claims: " + claims);
        return jwtUtil.isTokenExpired(claims) ? false: true;
    }
}
