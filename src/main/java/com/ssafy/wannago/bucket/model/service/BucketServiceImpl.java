package com.ssafy.wannago.bucket.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.wannago.bucket.model.BucketDto;
import com.ssafy.wannago.bucket.model.BucketResponseDto;
import com.ssafy.wannago.bucket.model.mapper.BucketMapper;
import com.ssafy.wannago.errorcode.BucketErrorCode;
import com.ssafy.wannago.exception.BucketException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService{
	private final BucketMapper bucketMapper;

	@Override
	public void deleteBucket(String userId, int bucketNo) throws Exception {
		BucketDto bucket=bucketMapper.selectByBucketNo(bucketNo);
		if(bucket==null) {
			throw new BucketException(BucketErrorCode.BucketNotFound.getCode(),BucketErrorCode.BucketNotFound.getDescription());
		}
		log.debug(bucketMapper.selectBucketUserId(bucketNo));
		if(!userId.equals(bucketMapper.selectBucketUserId(bucketNo))) {
			throw new BucketException(BucketErrorCode.UserIdNotMatchBucketUserId.getCode(),BucketErrorCode.UserIdNotMatchBucketUserId.getDescription());
		}
		
		bucketMapper.deleteByBucketNo(bucketNo);
	}

	@Override
	public void updateBucket(String userId, int bucketNo, BucketDto updateBucket) throws Exception {
		BucketDto bucket=bucketMapper.selectByBucketNo(bucketNo);
		if(bucket==null) {
			throw new BucketException(BucketErrorCode.BucketNotFound.getCode(),BucketErrorCode.BucketNotFound.getDescription());
		}
		log.debug(bucketMapper.selectBucketUserId(bucketNo));
		if(!userId.equals(bucketMapper.selectBucketUserId(bucketNo))) {
			throw new BucketException(BucketErrorCode.UserIdNotMatchBucketUserId.getCode(),BucketErrorCode.UserIdNotMatchBucketUserId.getDescription());
		}
		if(isBucketChanged(bucket,updateBucket)) {
			log.debug("update==========");
			updateBucket.setBucketNo(bucketNo);
			bucketMapper.updateByBucketNo(updateBucket);
		}
	}	
	private boolean isBucketChanged(BucketDto bucket,BucketDto updateBucket) {
		String updateDeadline=updateBucket.getBucketDeadline();
		if(updateDeadline!=null && !updateDeadline.equals(bucket.getBucketDeadline())) {
			return true;
		};
		boolean updateIsDone=updateBucket.getBucketIsDone();
		if(updateIsDone!=bucket.getBucketIsDone()) {
			return true;
		};
		return false;
	}

}
