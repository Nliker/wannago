package com.ssafy.wannago.attraction.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.wannago.attraction.model.AttractionJoinDescriptionDto;

@Mapper
public interface AttractionMapper {
	List<AttractionJoinDescriptionDto> selectByParam(Map<String, Object> param) throws SQLException;
	List<AttractionJoinDescriptionDto> selectByConceptNo(int conceptNo) throws SQLException;
}
