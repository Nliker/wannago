package com.ssafy.wannago.concept.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("concepts")
@RequiredArgsConstructor
public class ConceptController {
	private final ConceptService conceptService;
	
	@PostMapping("/")
	public ResponseEntity<Map<String,ConceptResponseDto>> concept(Authentication authentication,@RequestBody ConceptDto concept) throws Exception{
		Map<String,String> result=new HashMap<>();
		result.put("conceptInfo",conceptService.createConcept(authentication.getName(),concept));
		return ResponseEntity.ok().body(result);
	}
}
