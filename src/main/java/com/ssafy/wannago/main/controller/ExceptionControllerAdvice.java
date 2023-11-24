package com.ssafy.wannago.main.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.wannago.exception.AttractionException;
import com.ssafy.wannago.exception.BucketException;
import com.ssafy.wannago.exception.ConceptException;
import com.ssafy.wannago.exception.CredentialException;
import com.ssafy.wannago.exception.CustomException;
import com.ssafy.wannago.exception.FileException;
import com.ssafy.wannago.exception.JwtException;
import com.ssafy.wannago.exception.MediaException;

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
	
	@ExceptionHandler(CredentialException.class)
	public ResponseEntity<CustomException> handleCredentialExeption(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
	
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<CustomException> handleJwtException(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
	
	@ExceptionHandler(ConceptException.class)
	public ResponseEntity<CustomException> handleConceptException(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
	
	@ExceptionHandler(MediaException.class)
	public ResponseEntity<CustomException> handleMediaException(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
	
	@ExceptionHandler(BucketException.class)
	public ResponseEntity<CustomException> handleBucketException(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
	
	@ExceptionHandler(FileException.class)
	public ResponseEntity<CustomException> handleFileException(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
	@ExceptionHandler(AttractionException.class)
	public ResponseEntity<CustomException> handleAttractionException(CustomException e, Model model) {
		log.debug("Exception type:"+e.getClass());
		log.error("Exception 발생 : {}",e.getCode());
		log.error("Exception 발생 : {}",e.getMsg());
		
		return ResponseEntity.status(e.getCode()).body(e);
	}
}
