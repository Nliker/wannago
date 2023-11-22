package com.ssafy.wannago.concept.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.wannago.attraction.model.AttractionResponseDto;
import com.ssafy.wannago.bucket.model.BucketDto;
import com.ssafy.wannago.bucket.model.BucketResponseDto;
import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.concept.model.ConceptSearchResponseDto;
import com.ssafy.wannago.media.model.MediaResponseDto;

public interface ConceptService {
	ConceptResponseDto createConcept(String userId, ConceptDto concept, MultipartFile[] medias) throws Exception;

	List<ConceptResponseDto> getConceptList(String userId,Map<String, String> map) throws Exception;

	ConceptDetailResponseDto getConcept(String userId, int conceptNo) throws Exception;

	List<MediaResponseDto> getConceptMediaList(String userId, int conceptNo,Map<String, String> map)throws Exception;

	void addConceptMediaList(String userId, int conceptNo, MultipartFile[] files) throws Exception;

	void deleteConcept(String userId, int conceptNo) throws Exception;

	List<BucketResponseDto> getBucketList(String userId, int conceptNo) throws Exception;

	void createBucket(String userId, int conceptNo,BucketDto bucket) throws Exception;

	void updateConcept(String userId,int conceptNo, ConceptDto concept) throws Exception;

	List<ConceptSearchResponseDto> getSearchConceptList(String name, Map<String, String> map) throws Exception;

	List<AttractionResponseDto> getConceptAttractionList(int conceptNo) throws Exception;
}
