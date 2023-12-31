package com.ssafy.wannago.file.utils;

import java.util.List;
import com.ssafy.wannago.media.model.MediaDto;

public interface FileUtil {
	List<MediaDto> saveFiles(int conceptNo) throws Exception;
	void resizeVideo(MediaDto media) throws Exception;
	void generateImageThumbnail(MediaDto media) throws Exception;
}
