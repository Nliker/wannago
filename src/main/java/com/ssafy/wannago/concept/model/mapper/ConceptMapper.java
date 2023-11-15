package com.ssafy.wannago.concept.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.wannago.concept.model.ConceptDto;

@Mapper
public interface ConceptMapper {

	void insertConcept(ConceptDto concept) throws Exception;

	List<ConceptDto> selectByUserId(String userId) throws Exception;

	ConceptDto selectByConceptNo(int conceptNo) throws Exception;
}