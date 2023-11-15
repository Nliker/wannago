package com.ssafy.wannago.concept.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.concept.model.service.ConceptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("concepts")
@RequiredArgsConstructor
public class ConceptController {
	private final ConceptService conceptService;
	
	@PostMapping("")
	public ResponseEntity<Map<String,ConceptResponseDto>> concept(Authentication authentication,@RequestBody ConceptDto concept) throws Exception{
		Map<String,ConceptResponseDto> result=new HashMap<>();
		result.put("conceptInfo",conceptService.createConcept(authentication.getName(),concept));
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("")
	public ResponseEntity<Map<String,List<ConceptResponseDto>>> concept(Authentication authentication) throws Exception{
		Map<String,List<ConceptResponseDto>> result=new HashMap<>();
		result.put("conceptInfoList",conceptService.getConceptList(authentication.getName()));
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/{conceptNo}")
	public ResponseEntity<Map<String,ConceptDetailResponseDto>> concept(Authentication authentication,@PathVariable int conceptNo) throws Exception{
		Map<String,ConceptDetailResponseDto> result=new HashMap<>();
		result.put("conceptDetailInfo",conceptService.getConcept(authentication.getName(),conceptNo));
		return ResponseEntity.ok().body(result);
	}
	
}
