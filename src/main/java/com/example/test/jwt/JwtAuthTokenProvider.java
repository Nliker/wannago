package com.example.test.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtAuthTokenProvider implements AuthTokenProvider<JwtAuthToken>{
	@Value("${jwt.base64.secret}")
	private String base64Secret;
	private Key key;
	
	@PostConstruct
	public void init() {
		byte[] keyBytes=Base64.getDecoder().decode(base64Secret);
		this.key=new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
	}

	@Override
	public JwtAuthToken createAuthToken(String role, Map<String, Object> claims, Date expiredDate) {
		return new JwtAuthToken(key, role,claims,expiredDate);
	}

	@Override
	public JwtAuthToken convertAuthToken(String token) {
		return new JwtAuthToken(token,key);
	}

	@Override
	public Authentication getAuthentication(JwtAuthToken authToken) throws Exception {
		Claims claims=authToken.getData();
		Collection<? extends GrantedAuthority> authorities=Collections.singleton(new SimpleGrantedAuthority(claims.get(AuthToken.AUTHORITIES_KEY,String.class)));
		if(claims.get("id")==null) {
			throw new JwtException("token property");
		}
		return new UsernamePasswordAuthenticationToken(claims.get("id"), authToken,authorities);
	}
}
