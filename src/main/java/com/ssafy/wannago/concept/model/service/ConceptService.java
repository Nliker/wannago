package com.ssafy.wannago.concept.model.service;

import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;

public interface ConceptService {
	ConceptResponseDto createConcept(String userId, ConceptDto concept) throws Exception;
}
