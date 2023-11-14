package com.ssafy.wannago.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.wannago.user.model.UserDto;
import com.ssafy.wannago.user.model.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController("backapi/users")
@RequiredArgsConstructor
public class UserContoller {
	private final UserService userService;
	
	@PostMapping("login")
	public ResponseEntity<Map<String,String>> login(@RequestBody UserDto user) throws Exception{
		log.debug("/login");
		String token=userService.userLogin(user);		
		Map<String,String> result=new HashMap<>();
		log.debug(token);
		result.put("access_token",token);
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("signup")
	public ResponseEntity<Map<String,String>> signup(@RequestBody UserDto user) throws Exception{
		log.debug("/login");
		userService.createUser(user);		
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
}
