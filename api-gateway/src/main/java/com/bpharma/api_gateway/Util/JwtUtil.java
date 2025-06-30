package com.bpharma.api_gateway.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	public Claims getAllClaimsFromToken(String token) throws SignatureException, ExpiredJwtException {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	public boolean isTokenExpired(Claims claims) {
		Date expiration = claims.getExpiration();
		return expiration.before(new Date());
	}

	public boolean hasPermission(Claims claims, String requiredPermission) {
		@SuppressWarnings("unchecked")
		List<String> permissions = (List<String>) claims.get("permission");
		return permissions != null && permissions.contains(requiredPermission);
	}

	public String getUsername(Claims claims) {
		return claims.getSubject();
	}
}
