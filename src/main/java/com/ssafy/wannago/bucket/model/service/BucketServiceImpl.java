package com.ssafy.wannago.bucket.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.wannago.bucket.model.BucketResponseDto;
import com.ssafy.wannago.bucket.model.mapper.BucketMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService{
	private final BucketMapper bucketMapper;	

}
