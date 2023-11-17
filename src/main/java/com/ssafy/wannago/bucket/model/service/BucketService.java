package com.ssafy.wannago.bucket.model.service;

import com.ssafy.wannago.bucket.model.BucketDto;

public interface BucketService {

	void deleteBucket(String userId, int bucketNo) throws Exception;

	void updateBucket(String userId, int bucketNo, BucketDto bucket) throws Exception;
	
}
