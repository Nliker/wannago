package com.ssafy.wannago.file.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FileUtilFactoryImpl implements FileUtilFactory {
	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${file.video.thumbnail.path}")
	private String videoThumbPath;
	
	@Value("${file.image.thumbnail.path}")
	private String imageThumbPath;
	
	@Value("${file.image.thumbnail.ratio}")
	private int imageThumbRatio;
	
	
	@Value("${file.image.thumbnail.Format}")
	private String thumbnailImageFormat;
	
	
	@Value("${file.video.stream.thumnail.path}")
	private String streamingThumbnailPath;
	
	@Override
	public FileUtil getInstance(MultipartFile[] files) {
		return new FileUtilImpl(files,this.uploadPath,this.videoThumbPath,this.imageThumbPath,this.imageThumbRatio,this.thumbnailImageFormat,this.streamingThumbnailPath);
	}
}
