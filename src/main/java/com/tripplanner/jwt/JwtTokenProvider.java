package com.tripplanner.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component("jwtTokenProvider")
public class JwtTokenProvider  {
	
	public static final long JWT_TOKEN_VALIDITY = 60;
	
	@Value("${Auth.secret}")
	private String secret;

	public String generateToken(String userName,String userId) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userName,userId);
	}

	private String doGenerateToken(Map<String, Object> claims, String subject,String userId) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setId(userId)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(getExpDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
	
	public Date getExpDate() {
		return new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000);
	}

}
