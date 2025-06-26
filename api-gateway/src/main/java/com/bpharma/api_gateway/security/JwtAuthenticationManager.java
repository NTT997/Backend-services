package com.bpharma.api_gateway.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.bpharma.api_gateway.Util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String token = authentication.getCredentials().toString();
		try {
			Claims claims = jwtUtil.getAllClaimsFromToken(token);

			if (jwtUtil.isTokenExpired(claims)) {
				return Mono.empty();
			}
			
			String username = jwtUtil.getUsername(claims);
            if (username == null) {
                return Mono.empty();
            }

			@SuppressWarnings("unchecked")
			List<String> permissions = (List<String>) claims.get("permission");
			if (permissions == null) {
                return Mono.empty();
            }
			
			List<SimpleGrantedAuthority> authorities = permissions.stream().map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());

			AbstractAuthenticationToken auth = new UsernamePasswordAuthenticationToken(jwtUtil.getUsername(claims),
					null, authorities);

			return Mono.just(auth);

		} catch (ExpiredJwtException | SignatureException | IllegalArgumentException e) {
			return Mono.empty();
		}
	}

}
