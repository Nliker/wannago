package com.ssafy.wannago.media.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.wannago.media.model.MediaDto;

@Mapper
public interface MediaMapper {

	MediaDto selectRandomOneByConceptNo(int conceptNo) throws SQLException;

	List<MediaDto> selectByConceptNo(Map<String,Object> param) throws SQLException;
	 
	void insertMediaList(List<MediaDto> mediaList) throws SQLException;

	MediaDto selectByMediaNo(int mediaNo) throws SQLException;

	void deleteByMediaNo(int mediaNo) throws SQLException;

	void deleteByConceptNo(int conceptNo) throws SQLException;

	List<MediaDto> selectAllByConceptNo(int conceptNo) throws SQLException;
}
