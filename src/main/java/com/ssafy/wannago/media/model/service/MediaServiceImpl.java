package com.ssafy.wannago.media.model.service;

import java.io.File;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
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
import org.springframework.stereotype.Service;

import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.mapper.ConceptMapper;
import com.ssafy.wannago.errorcode.FileErrorCode;
import com.ssafy.wannago.errorcode.MediaErrorCode;
import com.ssafy.wannago.exception.FileException;
import com.ssafy.wannago.exception.MediaException;
import com.ssafy.wannago.media.model.MediaDto;
import com.ssafy.wannago.media.model.mapper.MediaMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {
	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${file.video.thumbnail.path}")
	private String videoThumbPath;
	
	@Value("${file.image.thumbnail.path}")
	private String imageThumbPath;
	
	@Value("${file.image.thumbnail.Format}")
	private String thumbnailImageFormat;
	
	@Value("${file.image.defualtImageFile}")
	private String defualtImageFile;
	
	private final MediaMapper mediaMapper;
	private final ConceptMapper conceptMapper;
	
	@Override
	public ResponseEntity<Object> sendMedia(int mediaNo,String userId) throws Exception{
		MediaDto media=mediaMapper.selectByMediaNo(mediaNo);
		if(media==null) {
			throw new MediaException(MediaErrorCode.NotFoundMedia.getCode(),MediaErrorCode.NotFoundMedia.getDescription());
		}
		ConceptDto concept=conceptMapper.selectByConceptNo(media.getConceptNo());
		if(!concept.getUserId().equals(userId)) {
			throw new MediaException(MediaErrorCode.MediaUserIdNotMatch.getCode(),MediaErrorCode.MediaUserIdNotMatch.getDescription());
		}

		Path filePath = Paths.get(uploadPath +File.separator+ media.getSavePath());
		if(!Files.exists(filePath)) {
			throw new FileException(FileErrorCode.NotFoundFile.getCode(),FileErrorCode.NotFoundFile.getDescription());
		}
		Resource resource = new InputStreamResource(Files.newInputStream(filePath));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(media.getMediaOriginFile(), "UTF-8").replaceAll("\\+", "%20")).build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		
		return ResponseEntity.ok().headers(headers).body(resource);
	}
	@Override
	public void deleteMedia(int mediaNo,String userId) throws Exception {	
		MediaDto media=mediaMapper.selectByMediaNo(mediaNo);
		if(media==null) {
			throw new MediaException(MediaErrorCode.NotFoundMedia.getCode(),MediaErrorCode.NotFoundMedia.getDescription());
		}
		ConceptDto concept=conceptMapper.selectByConceptNo(media.getConceptNo());
		if(!concept.getUserId().equals(userId)) {
			throw new MediaException(MediaErrorCode.MediaUserIdNotMatch.getCode(),MediaErrorCode.MediaUserIdNotMatch.getDescription());
		}
		
		mediaMapper.deleteByMediaNo(mediaNo);
	}
	@Override
	public ResponseEntity<Object> sendMediaThumbnail(int mediaNo) throws Exception {
		MediaDto media=mediaMapper.selectByMediaNo(mediaNo);
		if(media==null) {
			throw new MediaException(MediaErrorCode.NotFoundMedia.getCode(),MediaErrorCode.NotFoundMedia.getDescription());
		}
		
		String mediaSaveName=media.getFileNameWithoutExtension()+"."+this.thumbnailImageFormat;
		String mediaOriginName=media.getOriginFileNameWithoutExtension()+"."+this.thumbnailImageFormat;

		Path filePath = Paths.get(imageThumbPath +File.separator+media.getMediaSaveFolder()+ File.separator+mediaSaveName);
		if(!Files.exists(filePath)) {
			throw new FileException(FileErrorCode.NotFoundFile.getCode(),FileErrorCode.NotFoundFile.getDescription());
		}
		Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(mediaOriginName, "UTF-8").replaceAll("\\+", "%20")).build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	@Override
	public ResponseEntity<Object> sendDefaultImage(int mediaNo) throws Exception {
		log.debug("defaut send defaultImage");
		log.debug(defualtImageFile);
		Path filePath = Paths.get(imageThumbPath +File.separator+defualtImageFile);
		if(!Files.exists(filePath)) {
			throw new FileException(FileErrorCode.NotFoundFile.getCode(),FileErrorCode.NotFoundFile.getDescription());
		}
		Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(defualtImageFile, "UTF-8").replaceAll("\\+", "%20")).build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return ResponseEntity.ok().headers(headers).body(resource);
	}
	
	
	@Override
	public ResponseEntity<Object> sendResizeVideo(int mediaNo) throws Exception {
		MediaDto media=mediaMapper.selectByMediaNo(mediaNo);
		if(media==null) {
			throw new MediaException(MediaErrorCode.NotFoundMedia.getCode(),MediaErrorCode.NotFoundMedia.getDescription());
		}
		if(!"video".equals(media.getMediaType())) {
			throw new MediaException(MediaErrorCode.NotCorrectType.getCode(),MediaErrorCode.NotCorrectType.getDescription());
		}
		
		Path filePath = Paths.get(videoThumbPath +File.separator+ media.getSavePath());
		if(!Files.exists(filePath)) {
			throw new FileException(FileErrorCode.NotFoundFile.getCode(),FileErrorCode.NotFoundFile.getDescription());
		}
		
		Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(media.getMediaOriginFile(), "UTF-8").replaceAll("\\+", "%20")).build());
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return ResponseEntity.ok().headers(headers).body(resource);
	}
	@Override
	public ResponseEntity<ResourceRegion> streamMedia(String mediaNo, HttpHeaders headers) throws Exception {
		String videoUrl="/Users/codakcodak/Downloads/sample4.mp4";
        Resource resource = new FileSystemResource(videoUrl);

        long chunkSize = 10240 * 1024;
        long contentLength = resource.contentLength();

        ResourceRegion region;

        try {
            HttpRange httpRange = headers.getRange().stream().findFirst().get();
            long start = httpRange.getRangeStart(contentLength);
            long end = httpRange.getRangeEnd(contentLength);
            long rangeLength = Long.min(chunkSize, end -start + 1);

            region = new ResourceRegion(resource, start, rangeLength);
        } catch (Exception e) {
            long rangeLength = Long.min(chunkSize, contentLength);
            region = new ResourceRegion(resource, 0, rangeLength);
        }

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(resource).get())
                .header("Accept-Ranges", "bytes")
                .eTag(videoUrl)
                .body(region);
	}
}
