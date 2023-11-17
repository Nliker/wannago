package com.ssafy.wannago.bucket.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.wannago.bucket.model.BucketDto;
import com.ssafy.wannago.bucket.model.BucketJoinAttractionDto;

@Mapper
public interface BucketMapper {

	int selectCntByConceptNo(int conceptNo) throws SQLException;

	int selectDoneCntByConceptNo(int conceptNo) throws SQLException;

	List<BucketJoinAttractionDto> selectByConceptNo(int conceptNo) throws SQLException;

	void insertBucket(BucketDto bucket) throws SQLException;

}
