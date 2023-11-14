package com.ssafy.wannago.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.wannago.exception.CredentialExeption;
import com.ssafy.wannago.exception.CustomException;
import com.ssafy.wannago.exception.JwtException;

import lombok.extern.slf4j.Slf4j;



@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Exception> handleException(Exception e, Model model) {
		e.printStackTrace();
		log.debug("Exception: 내부 에러");
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
	}
	
	@ExceptionHandler(CredentialExeption.class)
	public ResponseEntity<CustomException> handleCredentialExeption(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
	
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<CustomException> handleJwtException(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
//		((Exception)e).printStackTrace();
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
}
