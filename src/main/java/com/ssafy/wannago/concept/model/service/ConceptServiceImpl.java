package com.ssafy.wannago.concept.model.service;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.wannago.bucket.model.mapper.BucketMapper;
import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.concept.model.mapper.ConceptMapper;
import com.ssafy.wannago.errorcode.ConceptErrorCode;
import com.ssafy.wannago.exception.ConceptException;
import com.ssafy.wannago.media.model.MediaDto;
import com.ssafy.wannago.media.model.MediaResponseDto;
import com.ssafy.wannago.media.model.mapper.MediaMapper;
import com.ssafy.wannago.user.model.UserDto;
import com.ssafy.wannago.user.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConceptServiceImpl implements ConceptService{
	
	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${file.video.extensions}")
	private String[] videoExtensionList;
	
	private final ConceptMapper conceptMapper;
	private final UserMapper userMapper;
	private final MediaMapper mediaMapper;
	private final BucketMapper bucketMapper;
	
	@Transactional
	@Override
	public ConceptResponseDto createConcept(String userId, ConceptDto concept,MultipartFile[] files) throws Exception {
		log.info("class:=================createConcept====================");

		UserDto user=userMapper.selectByUserId(userId);
		concept.setUserId(user.getUserId());
		concept.setUserName(user.getUserName());
		conceptMapper.insertConcept(concept);	
		
		List<MediaDto> mediaList=saveFile(files,concept.getConceptNo());
		if(mediaList.size()!=0) {
			mediaMapper.insertMediaList(mediaList);
		}
		
		ConceptResponseDto conceptResponseDto=new ConceptResponseDto(concept);
		log.info("conceptResponseDto"+conceptResponseDto.toString());
		setConceptMetaData(conceptResponseDto);
		
		return conceptResponseDto;
	}
	
	private List<MediaDto> saveFile(MultipartFile[] files,int conceptNo) throws Exception {
        long beforeTime = System.currentTimeMillis(); 
        log.debug("start time:"+beforeTime);
        long fileSize=0;
        List<MediaDto> mediaList=new ArrayList<>();
        for(MultipartFile file:files) {
            if(file.isEmpty()) {
                continue;
            }
            String originalFileName = file.getOriginalFilename();
            if(originalFileName.isEmpty()) {
                continue;
            }
            String today = new SimpleDateFormat("yyMMdd").format(new Date());
            String saveFolder = uploadPath + File.separator + today;
            File folder = new File(saveFolder);
            if (!folder.exists())
                folder.mkdirs();
            String extension=originalFileName.substring(originalFileName.lastIndexOf('.')+1);
            String saveFileName = UUID.randomUUID().toString()
                    +"."+extension;
            file.transferTo(new File(folder, saveFileName));
            String mediaType= isVideo(extension)? "video" : "raw";
            mediaList.add(new MediaDto(conceptNo,today,originalFileName,saveFileName,mediaType));
            fileSize+=file.getSize();
        }
        log.debug("total saved size"+fileSize);
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        log.debug("end time:"+afterTime);
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("시간차이(m) : "+secDiffTime);
        
        return mediaList;
    }
	private boolean isVideo(String extension) {
		for(String videoExtension:videoExtensionList) {
			if(videoExtension.equals(extension)) {
				return true;
			}
		}
		return false;
	}


	@Override
	public List<ConceptResponseDto> getConceptList(String userId) throws Exception {
		log.info("class:=================getConceptList====================");
		List<ConceptDto> conceptList=conceptMapper.selectByUserId(userId);
		List<ConceptResponseDto> conceptResponseList=new ArrayList<>();
		for(ConceptDto concept:conceptList) {
			ConceptResponseDto conceptResponseDto=new ConceptResponseDto(concept);
			setConceptMetaData(conceptResponseDto);
			conceptResponseList.add(conceptResponseDto);
		}
		return conceptResponseList;
	}


	@Override
	public ConceptDetailResponseDto getConcept(String userId, int conceptNo) throws Exception {
		log.info("class:=================getConcept====================");
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		ConceptDetailResponseDto conceptDetailRespons=new ConceptDetailResponseDto(concept);
		setConceptMetaData(conceptDetailRespons);
		
		
		
		return conceptDetailRespons;
	}
	
	private void setConceptMetaData(ConceptResponseDto conceptResponseDto) throws SQLException {
		int conceptNo=conceptResponseDto.getConceptNo();
		int bucketCnt=bucketMapper.selectCntByConceptNo(conceptNo);
		int bucketDone=bucketMapper.selectDoneCntByConceptNo(conceptNo);
		conceptResponseDto.setBucketDoneCnt(bucketDone);
		conceptResponseDto.setBucketTotalCnt(bucketCnt);
		
		if (conceptResponseDto instanceof ConceptDetailResponseDto) {
			List<MediaResponseDto> mediaList=new ArrayList<>();
			for(MediaDto media:mediaMapper.selectByConceptNo(conceptNo)) {
				MediaResponseDto mediaReponse=new MediaResponseDto(media.getMediaNo(),media.getMediaType());
				mediaList.add(mediaReponse);
			}
			((ConceptDetailResponseDto)conceptResponseDto).setMediaInfoList(mediaList);
		}else {
			MediaDto RandomMedia=mediaMapper.selectRandomOneByConceptNo(conceptNo);
			MediaResponseDto mediaReponse= (RandomMedia==null)?
					new MediaResponseDto(0,"raw"):new MediaResponseDto(RandomMedia.getMediaNo(),RandomMedia.getMediaType());
			conceptResponseDto.setMediaInfoList(Arrays.asList(mediaReponse));
		}
	}

	@Override
	public List<MediaResponseDto> getConceptMediaList(String userId, int conceptNo) throws Exception {
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		List<MediaResponseDto> mediaList=new ArrayList<>();
		for(MediaDto media:mediaMapper.selectByConceptNo(conceptNo)) {
			MediaResponseDto mediaReponse=new MediaResponseDto(media.getMediaNo(),media.getMediaType());
			mediaList.add(mediaReponse);
		}
		
		return mediaList;
	}

	@Override
	public void addConceptMediaList(String userId, int conceptNo, MultipartFile[] files) throws Exception {
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		
		List<MediaDto> mediaList=saveFile(files,concept.getConceptNo());
		if(mediaList.size()!=0) {
			mediaMapper.insertMediaList(mediaList);
		}
	}
}

