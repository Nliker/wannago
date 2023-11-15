package com.ssafy.wannago.media.model.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.wannago.media.model.MediaDto;

@Mapper
public interface MediaMapper {

	MediaDto selectRandomOneByConceptNo(int conceptNo) throws SQLException;

}
