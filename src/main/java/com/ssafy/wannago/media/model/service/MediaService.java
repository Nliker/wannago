package com.ssafy.wannago.media.model.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface MediaService {
	ResponseEntity<ResourceRegion> sendMediaVideo(int mediaNo,String userId,HttpHeaders headers) throws Exception;
	ResponseEntity<Resource> sendMediaImage(int mediaNo,String userId) throws Exception;
	ResponseEntity<Resource> sendMediaThumbnail(int mediaNo) throws Exception;
	ResponseEntity<ResourceRegion> sendResizeVideo(int mediaNo,HttpHeaders headers) throws Exception;
	void deleteMedia(int mediaNo,String userId) throws Exception;
	ResponseEntity<Resource> sendDefaultImage(int mediaNo) throws Exception;
	ResponseEntity<Resource> sendAttachMedia(int mediaNo, String userId) throws Exception;
}
