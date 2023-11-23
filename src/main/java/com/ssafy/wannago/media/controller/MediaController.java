package com.ssafy.wannago.media.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ssafy.wannago.media.model.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("medias")
@RequiredArgsConstructor
public class MediaController {
	private final MediaService mediaService;
	
	@GetMapping("/{mediaNo}/video")
	public ResponseEntity<ResourceRegion> mediaVideo(Authentication authentication,@PathVariable int mediaNo,@RequestHeader HttpHeaders headers) throws Exception{
		log.debug("Get Medias/No/video");
		return mediaService.sendMediaVideo(mediaNo,authentication.getName(),headers);
	}
	
	@GetMapping("/{mediaNo}/image")
	public ResponseEntity<Resource> mediaImage(Authentication authentication,@PathVariable int mediaNo) throws Exception{
		log.debug("Get Medias/No/image");
		return mediaService.sendMediaImage(mediaNo,authentication.getName());
	}
	
	@GetMapping("/{mediaNo}/thumbnail")
	public ResponseEntity<Resource> mediaThumbnail(@PathVariable int mediaNo) throws Exception{
		log.debug("Get Medias/No/thumbNail");
		if(mediaNo==0) {
			return mediaService.sendDefaultImage(mediaNo);
		}
		return mediaService.sendMediaThumbnail(mediaNo);
	}
	
	
	@GetMapping("/{mediaNo}/resizeVideo")
	public ResponseEntity<ResourceRegion> mediaResizeVideo(@PathVariable int mediaNo,@RequestHeader HttpHeaders headers) throws Exception{
		log.debug("Get Medias/No");
		return mediaService.sendResizeVideo(mediaNo,headers);
	}
	
	@DeleteMapping("/{mediaNo}")
	public ResponseEntity<Object> deleteMedia(Authentication authentication,@PathVariable int mediaNo) throws Exception{
		log.debug("Delete Medias/No");
		mediaService.deleteMedia(mediaNo,authentication.getName());
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping("/{mediaNo}/download")
	public ResponseEntity<Resource> mediaDownLoad(Authentication authentication,@PathVariable int mediaNo) throws Exception{
		log.debug("Get Medias/No/download");
		return mediaService.sendAttachMedia(mediaNo,authentication.getName());
	}
	
}
