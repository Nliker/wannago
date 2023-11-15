package com.ssafy.wannago.bucket.model.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BucketMapper {

	int selectCntByConceptNo(int conceptNo) throws SQLException;

	int selectDoneCntByConceptNo(int conceptNo) throws SQLException;

}
