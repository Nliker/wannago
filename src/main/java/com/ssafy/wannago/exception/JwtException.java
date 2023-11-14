package com.ssafy.wannago.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtException extends Exception implements CustomException{
	private final int code;
	private final String msg;
	
	@Override
	public int getCode() {
		return this.code;
		
	}
	@Override
	public String getMsg() {
		return this.msg;
	}
}
