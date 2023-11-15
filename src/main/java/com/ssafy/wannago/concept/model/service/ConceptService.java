package com.ssafy.wannago.concept.model.service;

import java.util.List;

import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;

public interface ConceptService {
	ConceptResponseDto createConcept(String userId, ConceptDto concept) throws Exception;

	List<ConceptResponseDto> getConceptList(String userId) throws Exception;

	ConceptDetailResponseDto getConcept(String userId, int conceptNo) throws Exception;

}
