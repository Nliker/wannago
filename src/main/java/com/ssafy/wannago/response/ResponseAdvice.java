package com.ssafy.wannago.response;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.ssafy.wannago.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {return true;}
	
    @Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
        MediaType selectedContentType, Class selectedConverterType,
        ServerHttpRequest request,ServerHttpResponse response) {
    	log.info("class:=================beforeBodyWrite====================");
        HttpServletResponse servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();
        
        if(body==null) {
        	return new ResponseDto("empty",status,"Request was successful");
        }
        
        log.debug("body class: "+body.getClass().toString());
        log.debug("body: "+body);
        if(body instanceof Resource) {
        	return body;
        }else if (body instanceof CustomException) {//실패한 반환
        	CustomException Ce=(CustomException)body;
        	
        	log.debug("사용자 실패 처리");
        	return new ResponseDto("empty",status,Ce.getMsg());
        }else if(body instanceof Exception){
        	Exception e=(Exception)body;
        	
        	log.debug("내부 실패 처리");
        	return new ResponseDto("empty",status,e.getMessage());
        }
        log.debug("성공된 반환 처리");
        return new ResponseDto(body,status,"Request was successful");
    }
}