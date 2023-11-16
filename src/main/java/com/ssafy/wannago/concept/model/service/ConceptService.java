package com.ssafy.wannago.concept.model.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.media.model.MediaResponseDto;

public interface ConceptService {
	ConceptResponseDto createConcept(String userId, ConceptDto concept, MultipartFile[] medias) throws Exception;

	List<ConceptResponseDto> getConceptList(String userId) throws Exception;

	ConceptDetailResponseDto getConcept(String userId, int conceptNo) throws Exception;

	List<MediaResponseDto> getConceptMediaList(String userId, int conceptNo)throws Exception;

	void addConceptMediaList(String userId, int conceptNo, MultipartFile[] files) throws Exception;

	void deleteConcept(String name, int conceptNo) throws Exception;

}
