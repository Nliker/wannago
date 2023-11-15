package com.ssafy.wannago.concept.model.service;

import org.springframework.stereotype.Service;

import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.concept.model.mapper.ConceptMapper;
import com.ssafy.wannago.user.model.UserDto;
import com.ssafy.wannago.user.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConceptServiceImpl implements ConceptService{
	private final ConceptMapper conceptMapper;
	private final UserMapper userMapper;
	

	@Override
	public ConceptResponseDto createConcept(String userId, ConceptDto concept) throws Exception {
		log.info("class:=================createConcept====================");
		UserDto user=userMapper.selectByUserId(userId);
		concept.setUserId(user.getUserId());
		concept.setUserName(user.getUserName());
		conceptMapper.insertConcept(concept);		
		return new ConceptResponseDto(concept);
	}
}
