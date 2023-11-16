package com.ssafy.wannago.media.model.service;

import com.ssafy.wannago.media.model.MediaDto;

public interface MediaService {
	MediaDto getMedia(int mediaNo,String userId) throws Exception;

	void deleteMedia(int mediaNo,String userId) throws Exception;
}
