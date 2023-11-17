package com.ssafy.wannago.bucket.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.wannago.bucket.model.BucketDto;
import com.ssafy.wannago.bucket.model.service.BucketService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("buckets")
@RequiredArgsConstructor
public class BucketController {
	private final BucketService bucketService;
	
	@DeleteMapping("/{bucketNo}")
	public ResponseEntity<Map<String,String>> conceptBuckets(Authentication authentication,@PathVariable int bucketNo) throws Exception{
		bucketService.deleteBucket(authentication.getName(),bucketNo);
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
	@PutMapping("/{bucketNo}")
	public ResponseEntity<Map<String,String>> conceptBuckets(Authentication authentication,@PathVariable int bucketNo,@RequestBody BucketDto bucket) throws Exception{
		bucketService.updateBucket(authentication.getName(),bucketNo,bucket);
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
}
