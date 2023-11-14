package com.example.test.jwt;


public interface AuthToken<T> {
	String AUTHORITIES_KEY="role";
	boolean validate() throws Exception;
	T getData() throws Exception;
}
