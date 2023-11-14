package com.ssafy.wannago.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.ssafy.wannago.errorcode.CredentialErrorCode;
import com.ssafy.wannago.errorcode.JwtErrorCode;
import com.ssafy.wannago.exception.CredentialException;
import com.ssafy.wannago.exception.JwtException;
import com.ssafy.wannago.user.model.UserDto;
import com.ssafy.wannago.user.model.mapper.UserMapper;

import io.jsonwebtoken.Claims;

import io.jsonwebtoken.SignatureAlgorithm;


@Component
public class JwtAuthTokenProvider implements AuthTokenProvider<JwtAuthToken>{
	@Value("${jwt.base64.secret}")
	private String base64Secret;
	private Key key;
	
	@Autowired
	private UserMapper userMapper;
	
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
		if(claims.get("id")==null || !(claims.get("id") instanceof String)) {
			throw new JwtException(JwtErrorCode.NotFoundRequiredJwtProperty.getCode(),JwtErrorCode.NotFoundRequiredJwtProperty.getDescription());
		}
		if (userMapper.selectByUserId((String)claims.get("id"))==null) {
			throw new CredentialException(CredentialErrorCode.NotFoundId.getCode(),CredentialErrorCode.NotFoundId.getDescription());
		}
		
		return new UsernamePasswordAuthenticationToken(claims.get("id"), authToken,authorities);
	}
}
