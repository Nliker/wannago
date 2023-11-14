package com.example.wannago.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import com.ssafy.wannago.errorcode.JwtErrorCode;
import com.ssafy.wannago.exception.JwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthToken implements AuthToken<Claims>{
	private final String token;
	private final Key key;
	
	public JwtAuthToken(String token,Key key) {
		this.token=token;
		this.key=key;
	}
	
	public JwtAuthToken(Key key,String role,Map<String,Object> claims,Date expiredDate) {
		this.key=key;
		this.token=createJwtAuthToken(role,claims,expiredDate).get();
	}
	
	public String getToken() {
		return this.token;
	}
	
	public Optional<String> createJwtAuthToken(String role,Map<String, Object> claimsMap,Date expiredDate){
		Claims claims=new DefaultClaims(claimsMap);
		claims.put(JwtAuthToken.AUTHORITIES_KEY, role);
		return Optional.ofNullable(Jwts.builder()
				.addClaims(claims)
				.signWith(SignatureAlgorithm.HS256, key)
				.setExpiration(expiredDate)
				.compact());
	}
	@Override
	public boolean validate() throws Exception{
		return getData()!=null;
	}

	@Override
	public Claims getData() throws Exception{
		try {
			return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
		}catch(SecurityException | SignatureException | MalformedJwtException e) {
			throw new JwtException(JwtErrorCode.InvaldJwtSignature.getCode(),JwtErrorCode.InvaldJwtSignature.getDescription());
		}catch(ExpiredJwtException e) {
			throw new JwtException(JwtErrorCode.ExpiredJwt.getCode(),JwtErrorCode.ExpiredJwt.getDescription());
		}catch(UnsupportedJwtException e) {
			throw new JwtException(JwtErrorCode.UnsupportedJwt.getCode(),JwtErrorCode.UnsupportedJwt.getDescription());
		}catch(IllegalArgumentException e) {
			throw new JwtException(JwtErrorCode.IllegalArgumentException.getCode(),JwtErrorCode.IllegalArgumentException.getDescription());
		}
	}
	
}
