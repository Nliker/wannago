package com.ssafy.wannago.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("backapi")
@RequiredArgsConstructor
public class MainController {
	
	@GetMapping("/ping")
	public ResponseEntity<Map<String,String>> ping(){
		log.debug("/ping");
		Map<String,String> result=new HashMap<>();
		result.put("result","pong");
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/test")
	public ResponseEntity<Map<String,String>> test(){
		log.debug("/test");
		Map<String,String> result=new HashMap<>();
		result.put("result","test");
		return ResponseEntity.ok().body(result);
	}
}
