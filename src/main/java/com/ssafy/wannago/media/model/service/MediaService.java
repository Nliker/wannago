package com.ssafy.wannago.media.model.service;

import org.springframework.http.ResponseEntity;

public interface MediaService {
	ResponseEntity<Object> sendMedia(int mediaNo,String userId) throws Exception;
	ResponseEntity<Object> sendMediaThumbnail(int mediaNo,String userId) throws Exception;
	ResponseEntity<Object> sendResizeVideo(int mediaNo,String userId) throws Exception;
	void deleteMedia(int mediaNo,String userId) throws Exception;
}
