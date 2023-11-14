package com.example.test.jwt;

import java.util.Date;
import java.util.Map;

import org.springframework.security.core.Authentication;

public interface AuthTokenProvider<T> {
	T createAuthToken(String role,Map<String,Object > claims,Date expiredDate);
	T convertAuthToken(String token);
	Authentication getAuthentication(T authToken) throws Exception;
}
