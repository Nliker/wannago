package com.ssafy.wannago.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.wannago.user.model.UserDto;
import com.ssafy.wannago.user.model.UserResponseDto;
import com.ssafy.wannago.user.model.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserContoller {
	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<Map<String,String>> login(@RequestBody UserDto user) throws Exception{
		log.debug("/login");
		String token=userService.userLogin(user);		
		Map<String,String> result=new HashMap<>();
		log.debug(token);
		result.put("access_token",token);
		result.put("userName",user.getUserName());
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Map<String,String>> signup(@RequestBody UserDto user) throws Exception{
		log.debug("/signup");
		userService.createUser(user);		
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/my")
	public ResponseEntity<Map<String,UserResponseDto>> my(Authentication authentication) throws Exception{
		log.debug("/my");
		log.debug(authentication.getName());
		UserResponseDto user=userService.getUserInfo(authentication.getName());		
		Map<String,UserResponseDto> result=new HashMap<>();
		result.put("userInfo",user);
		return ResponseEntity.ok().body(result);
	}
	
	@PutMapping("/mypassword")
	public ResponseEntity<Map<String,String>> updateMy(Authentication authentication,@RequestBody Map<String, String> passwordMap) throws Exception{
		log.debug("/UpdateMy");
		log.debug(authentication.getName());
		userService.updateUserInfo(authentication.getName(),passwordMap.get("password"));		
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
}
