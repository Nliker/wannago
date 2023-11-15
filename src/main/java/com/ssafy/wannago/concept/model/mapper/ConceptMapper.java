package com.ssafy.wannago.concept.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;

@Mapper
public interface ConceptMapper {

	void insertConcept(ConceptDto concept);

	ConceptResponseDto selectByConceptNo(int conceptNo);

}
