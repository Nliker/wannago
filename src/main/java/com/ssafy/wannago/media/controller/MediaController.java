package com.ssafy.wannago.media.controller;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@GetMapping("/{mediaNo}")
	public ResponseEntity<Object> media(Authentication authentication,@PathVariable int mediaNo) throws Exception{
		log.debug("Get Medias/No");
		return mediaService.sendMedia(mediaNo,authentication.getName());
	}
	
	@DeleteMapping("/{mediaNo}")
	public ResponseEntity<Object> deleteMedia(Authentication authentication,@PathVariable int mediaNo) throws Exception{
		log.debug("Delete Medias/No");
		mediaService.deleteMedia(mediaNo,authentication.getName());
		Map<String,String> result=new HashMap<>();
		result.put("result","successful");
		return ResponseEntity.ok().body(result);
	}
	
	
	
	@GetMapping("/{mediaNo}/thumbnail")
	public ResponseEntity<Object> mediaThumbnail(@PathVariable int mediaNo) throws Exception{
		log.debug("Get Medias/No/thumbNail");
		if(mediaNo==0) {
			return mediaService.sendDefaultImage(mediaNo);
		}
		return mediaService.sendMediaThumbnail(mediaNo);
	}
	
//	@GetMapping(value="/{mediaNo}/resizeVideo")
//	public ResponseEntity<Object> mediaResizeVideo(Authentication authentication,@RequestHeader HttpHeaders headers,@PathVariable int mediaNo) throws Exception{
//		log.debug("Get Medias/No/resizeVideo");
//		MediaDto media=mediaService.getMedia(mediaNo,authentication.getName());
//		if(!"video".equals(media.getMediaType())) {
//			throw new Exception("요청하신 미디어 형식은 스트림에 적합하지 않은 파일입니다.");
//		}
//		log.debug(media.toString());
//		String file = videoThumbPath +File.separator+ media.getSavePath();
//		
//		FileUrlResource  resource = new FileUrlResource(file);
//        
//		ResourceRegion region;
//		long chunkSize = 1024 * 1024;
//		long contentLength = resource.contentLength();
//		
//		if (headers.getRange().isEmpty()) {
//			region=new ResourceRegion(resource, 0, Long.min(chunkSize, contentLength));
//	    }
//       
//        log.debug("Streaming Start===============================================================================");
//        try {
//            HttpRange httpRange = headers.getRange().stream().findFirst().get();
//            log.debug(httpRange.toString());
//            long start = httpRange.getRangeStart(contentLength);
//            long end = httpRange.getRangeEnd(contentLength);
//            long rangeLength = Long.min(chunkSize, end -start + 1);
//            
//            
//            log.debug("start: "+start);
//            log.debug("end: "+end);
//            log.debug("rangeLength: "+rangeLength);
//            log.info("start === {} , end == {}", start, end);
//            log.debug(MediaTypeFactory.getMediaType(resource).toString());
//            region = new ResourceRegion(resource, start, rangeLength);
//        } catch (Exception e) {
//            long rangeLength = Long.min(chunkSize, contentLength);
//            region = new ResourceRegion(resource, 0, rangeLength);
//        }
//        log.debug("Streaming end===============================================================================");
//        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(region);
//	}
	
	@GetMapping("/{mediaNo}/resizeVideo")
	public ResponseEntity<Object> mediaResizeVideo(@PathVariable int mediaNo) throws Exception{
		log.debug("Get Medias/No");
		return mediaService.sendResizeVideo(mediaNo);
	}
	
	@GetMapping("/{mediaNo}/stream")
	public ResponseEntity<ResourceRegion> mediaStream(@PathVariable String mediaNo,@RequestHeader HttpHeaders headers) throws Exception {
		log.debug("===================stream===============");
		
		return mediaService.streamMedia(mediaNo,headers);
	}
	
}
