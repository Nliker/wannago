package com.ssafy.wannago.concept.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.wannago.concept.model.ConceptDto;

@Mapper
public interface ConceptMapper {

	void insertConcept(ConceptDto concept) throws Exception;

	List<ConceptDto> selectByUserId(Map<String, Object> param) throws Exception;

	ConceptDto selectByConceptNo(int conceptNo) throws Exception;

	void deleteByConceptNo(int conceptNo) throws Exception;

	void updateConcept(ConceptDto updateConcept) throws Exception;

	List<ConceptDto> selectByParam(Map<String, Object> param) throws Exception;

	void updateConceptShareCountUp(int conceptNo) throws Exception;
}
