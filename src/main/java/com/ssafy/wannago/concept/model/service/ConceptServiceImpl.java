package com.ssafy.wannago.concept.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.wannago.bucket.model.mapper.BucketMapper;
import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.concept.model.mapper.ConceptMapper;
import com.ssafy.wannago.errorcode.ConceptErrorCode;
import com.ssafy.wannago.exception.ConceptException;
import com.ssafy.wannago.media.model.MediaDto;
import com.ssafy.wannago.media.model.mapper.MediaMapper;
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
	private final MediaMapper mediaMapper;
	private final BucketMapper bucketMapper;

	@Override
	public ConceptResponseDto createConcept(String userId, ConceptDto concept) throws Exception {
		log.info("class:=================createConcept====================");
		UserDto user=userMapper.selectByUserId(userId);
		concept.setUserId(user.getUserId());
		concept.setUserName(user.getUserName());
		conceptMapper.insertConcept(concept);	
		return new ConceptResponseDto(concept,"default media link",0,0);
	}


	@Override
	public List<ConceptResponseDto> getConceptList(String userId) throws Exception {
		log.info("class:=================getConceptList====================");
		List<ConceptDto> conceptList=conceptMapper.selectByUserId(userId);
		List<ConceptResponseDto> conceptResponseList=new ArrayList<>();
		for(ConceptDto concept:conceptList) {
			ConceptResponseDto conceptResponseDto=new ConceptResponseDto(concept);
			setConceptMetaData(conceptResponseDto,concept.getConceptNo());
			conceptResponseList.add(conceptResponseDto);
		}
		return conceptResponseList;
	}


	@Override
	public ConceptDetailResponseDto getConcept(String userId, int conceptNo) throws Exception {
		log.info("class:=================getConcept====================");
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		ConceptDetailResponseDto conceptDetailRespons=new ConceptDetailResponseDto(concept);
		setConceptMetaData(conceptDetailRespons,conceptNo);
		return conceptDetailRespons;
	}
	
	private void setConceptMetaData(ConceptResponseDto conceptResponseDto,int conceptNo) throws SQLException {
		int bucketCnt=bucketMapper.selectCntByConceptNo(conceptNo);
		int bucketDone=bucketMapper.selectDoneCntByConceptNo(conceptNo);
		MediaDto media=mediaMapper.selectRandomOneByConceptNo(conceptNo);
		String mediaLink=(media==null)?"default media link":media.getLink();
		conceptResponseDto.setBucketDoneCnt(bucketDone);
		conceptResponseDto.setBucketTotalCnt(bucketCnt);
		conceptResponseDto.setMedia(mediaLink);		
	}
}

