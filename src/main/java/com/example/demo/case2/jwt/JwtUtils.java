package com.example.demo.case2.jwt;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.case2.models.User;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${bankapp.jwtSecret}")
	private String jwtSecret;

	@Value("${bankapp.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	
	@Value("${bankapp.jwtResetPassExpirationMs}")
	private int jwtResetPassExpirationMs;

	
	/*
	 * Generate a JWT Token with authentication details
	 * Set the token with email,data,expire date and signature
	 */
	public String generateJwtToken(Authentication authentication) {

		User userPrincipal = (User) authentication.getPrincipal();
		
		return Jwts.builder()
				.setSubject((userPrincipal.getEmail()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
		
	}
	
	/*
	 * Generate a reset token for forget password
	 * 
	 */
	public String generateResetToken(String email) {

		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtResetPassExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
	
	}
	
	// Get email id by decoding the jwt token
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	//Validate Jwt token authencity
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
