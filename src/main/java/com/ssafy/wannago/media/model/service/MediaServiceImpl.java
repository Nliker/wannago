package com.ssafy.wannago.media.model.service;

import org.springframework.stereotype.Service;

import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.mapper.ConceptMapper;
import com.ssafy.wannago.errorcode.MediaErrorCode;
import com.ssafy.wannago.exception.MediaException;
import com.ssafy.wannago.media.model.MediaDto;
import com.ssafy.wannago.media.model.mapper.MediaMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
	private final MediaMapper mediaMapper;
	private final ConceptMapper conceptMapper;
	@Override
	public MediaDto getMedia(int mediaNo,String userId) throws Exception{
		MediaDto media=mediaMapper.selectByMediaNo(mediaNo);
		if(media==null) {
			throw new MediaException(MediaErrorCode.NotFoundMedia.getCode(),MediaErrorCode.NotFoundMedia.getDescription());
		}
		ConceptDto concept=conceptMapper.selectByConceptNo(media.getConceptNo());
		if(!concept.getUserId().equals(userId)) {
			throw new MediaException(MediaErrorCode.MediaUserIdNotMatch.getCode(),MediaErrorCode.MediaUserIdNotMatch.getDescription());
		}
		return media;
	}
}
