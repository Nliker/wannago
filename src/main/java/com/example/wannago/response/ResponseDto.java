package com.example.wannago.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseDto{
	private Object data;
	private int statusCode;
	private String message;
	public ResponseDto(Object data,int statusCode,String message){
		this.data=data;
		this.statusCode=statusCode;
		this.message=message;
	}
}
