package com.ssafy.wannago.file.utils;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.wannago.media.model.MediaDto;

public interface FileUtil {
	List<MediaDto> saveFiles(int conceptNo) throws Exception;
	void generateVideoThumbnail(MediaDto media) throws Exception;
	void generateImageThumbnail(MediaDto media) throws Exception;
}
