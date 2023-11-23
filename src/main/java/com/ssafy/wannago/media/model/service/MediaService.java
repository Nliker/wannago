package com.ssafy.wannago.media.model.service;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface MediaService {
	ResponseEntity<Object> sendMedia(int mediaNo,String userId) throws Exception;
	ResponseEntity<Object> sendMediaThumbnail(int mediaNo) throws Exception;
	ResponseEntity<Object> sendResizeVideo(int mediaNo) throws Exception;
	void deleteMedia(int mediaNo,String userId) throws Exception;
	ResponseEntity<Object> sendDefaultImage(int mediaNo) throws Exception;
	ResponseEntity<ResourceRegion> streamMedia(String mediaNo, HttpHeaders headers) throws Exception;;
}
