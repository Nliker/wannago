package com.ssafy.wannago.attraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.wannago.attraction.model.AttractionResponseDto;
import com.ssafy.wannago.attraction.model.service.AttractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("attractions")
@RequiredArgsConstructor
public class AttractionController {
	private final AttractionService attractionService;
	
	@GetMapping("")
	public ResponseEntity<Map<String,List<AttractionResponseDto>>> attractions(@RequestParam Map<String, String> map) throws Exception{
		log.debug("/attractions");
		log.debug("maps:"+map.toString());
		Map<String,List<AttractionResponseDto>> result=new HashMap<>();
		result.put("AttractionInfoList",attractionService.getAttractionList(map));
		return ResponseEntity.ok().body(result);
	}
}
