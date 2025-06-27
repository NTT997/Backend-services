//package com.bpharma.api_gateway.security;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//
//import reactor.core.publisher.Mono;
//
//@Component
//public class JwtAuthenticationConverter implements ServerAuthenticationConverter {
//	
//	@Override
//	public Mono<Authentication> convert(ServerWebExchange exchange) {
//		String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
//		if (authHeader != null && authHeader.startsWith("Bearer ")) {
//			String token = authHeader.substring(7);
//			return Mono.just(new UsernamePasswordAuthenticationToken(token, token));
//		}
//		return Mono.empty();
//	}  
//
//
//}
