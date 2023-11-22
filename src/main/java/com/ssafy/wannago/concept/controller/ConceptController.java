package com.ssafy.wannago.concept.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.wannago.bucket.model.BucketDto;
import com.ssafy.wannago.bucket.model.BucketResponseDto;
import com.ssafy.wannago.bucket.model.service.BucketService;
import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.concept.model.ConceptSearchResponseDto;
import com.ssafy.wannago.concept.model.service.ConceptService;
import com.ssafy.wannago.media.model.MediaResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("concepts")
@RequiredArgsConstructor
public class ConceptController {
	private final ConceptService conceptService;
	
	@PostMapping("")
	public ResponseEntity<Map<String,ConceptResponseDto>> concept(Authentication authentication,ConceptDto concept,@RequestParam(value="upfile",required=false) MultipartFile[] files) throws Exception{
		Map<String,ConceptResponseDto> result=new HashMap<>();
		result.put("conceptInfo",conceptService.createConcept(authentication.getName(),concept,files));
	
		return ResponseEntity.ok().body(result);
	}
	
	@PutMapping("/{conceptNo}")
	public ResponseEntity<Map<String,String>> updateConcept(Authentication authentication,@RequestBody ConceptDto concept,@PathVariable int conceptNo) throws Exception{
		log.debug(concept.toString());
		conceptService.updateConcept(authentication.getName(),conceptNo,concept);
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("")
	public ResponseEntity<Map<String,List<ConceptResponseDto>>> concept(Authentication authentication,@RequestParam Map<String, String> map) throws Exception{
		Map<String,List<ConceptResponseDto>> result=new HashMap<>();
		result.put("conceptInfoList",conceptService.getConceptList(authentication.getName(),map));
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/{conceptNo}")
	public ResponseEntity<Map<String,ConceptDetailResponseDto>> concept(Authentication authentication,@PathVariable int conceptNo) throws Exception{
		Map<String,ConceptDetailResponseDto> result=new HashMap<>();
		result.put("conceptDetailInfo",conceptService.getConcept(authentication.getName(),conceptNo));
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/{conceptNo}/medias")
	public ResponseEntity<Map<String,List<MediaResponseDto>>> conceptMedias(Authentication authentication,@PathVariable int conceptNo,@RequestParam Map<String, String> map) throws Exception{
		Map<String,List<MediaResponseDto>> result=new HashMap<>();
		result.put("mediaInfoList",conceptService.getConceptMediaList(authentication.getName(),conceptNo,map));
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/{conceptNo}/medias")
	public ResponseEntity<Map<String,String>> conceptMedias(Authentication authentication,@RequestParam(value="upfile",required=false) MultipartFile[] files,@PathVariable int conceptNo) throws Exception{
		conceptService.addConceptMediaList(authentication.getName(),conceptNo,files);
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
	@DeleteMapping("/{conceptNo}")
	public ResponseEntity<Map<String,String>> deleteConcept(Authentication authentication,@PathVariable int conceptNo) throws Exception{
		conceptService.deleteConcept(authentication.getName(),conceptNo);
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/{conceptNo}/buckets")
	public ResponseEntity<Map<String,List<BucketResponseDto>>> conceptBuckets(Authentication authentication,@PathVariable int conceptNo) throws Exception{
		Map<String,List<BucketResponseDto>> result=new HashMap<>();
		result.put("bucketInfoList",conceptService.getBucketList(authentication.getName(),conceptNo));
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/{conceptNo}/buckets")
	public ResponseEntity<Map<String,String>> conceptBuckets(Authentication authentication,@PathVariable int conceptNo,@RequestBody BucketDto bucket) throws Exception{
		conceptService.createBucket(authentication.getName(),conceptNo,bucket);
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("search")
	public ResponseEntity<Map<String,List<ConceptSearchResponseDto>>> searchConceptList(Authentication authentication,@RequestParam Map<String, String> map) throws Exception{
		Map<String,List<ConceptSearchResponseDto>> result=new HashMap<>();
		result.put("searchConceptInfoList",conceptService.getSearchConceptList(authentication.getName(),map));
		return ResponseEntity.ok().body(result);
	}

}
