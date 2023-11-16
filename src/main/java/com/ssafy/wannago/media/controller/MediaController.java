package com.ssafy.wannago.media.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.wannago.media.model.MediaDto;
import com.ssafy.wannago.media.model.service.MediaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("medias")
@RequiredArgsConstructor
public class MediaController {
	private final MediaService mediaService;
	
	@Value("${file.path}")
	private String uploadPath;
	
	
	@GetMapping("/{mediaNo}")
	public ResponseEntity<Object> media(Authentication authentication,@PathVariable int mediaNo) throws Exception{
		
		log.debug("Get Medias/No");
		MediaDto media=mediaService.getMedia(mediaNo,authentication.getName());
		String file = uploadPath +File.separator+ media.getSavePath();
		Path filePath = Paths.get(file);
		Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
		log.debug(filePath.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(media.getMediaOriginFile(), "UTF-8").replaceAll("\\+", "%20")).build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	@DeleteMapping("/{mediaNo}")
	public ResponseEntity<Object> deleteMedia(Authentication authentication,@PathVariable int mediaNo) throws Exception{
		log.debug("Delete Medias/No");
		mediaService.deleteMedia(mediaNo,authentication.getName());
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
}
