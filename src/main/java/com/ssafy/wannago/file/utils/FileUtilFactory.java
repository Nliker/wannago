package com.ssafy.wannago.file.utils;

import org.springframework.web.multipart.MultipartFile;

public interface FileUtilFactory {
	FileUtil getInstance(MultipartFile[] files);
}
