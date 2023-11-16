package com.ssafy.wannago.concept.model.service;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.wannago.bucket.model.mapper.BucketMapper;
import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.concept.model.mapper.ConceptMapper;
import com.ssafy.wannago.errorcode.ConceptErrorCode;
import com.ssafy.wannago.exception.ConceptException;
import com.ssafy.wannago.media.model.MediaDto;
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
	
	private final ConceptMapper conceptMapper;
	private final UserMapper userMapper;
	private final MediaMapper mediaMapper;
	private final BucketMapper bucketMapper;

	@Override
	public ConceptResponseDto createConcept(String userId, ConceptDto concept,MultipartFile[] files) throws Exception {
		log.info("class:=================createConcept====================");
		saveFile(files);
		UserDto user=userMapper.selectByUserId(userId);
		concept.setUserId(user.getUserId());
		concept.setUserName(user.getUserName());
		conceptMapper.insertConcept(concept);	
		ConceptResponseDto conceptResponseDto=new ConceptResponseDto(concept);
		setConceptMetaData(conceptResponseDto);
		return conceptResponseDto;
	}
	
	private void saveFile(MultipartFile[] files) throws Exception {
        long beforeTime = System.currentTimeMillis(); 
        log.debug("start time:"+beforeTime);
        long fileSize=0;
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
            String saveFileName = UUID.randomUUID().toString()
                    + originalFileName.substring(originalFileName.lastIndexOf('.'));
            file.transferTo(new File(folder, saveFileName));
            fileSize+=file.getSize();
        }
        log.debug("total saved size"+fileSize);
        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        log.debug("end time:"+afterTime);
        long secDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
        log.debug("시간차이(m) : "+secDiffTime);
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
		MediaDto RandomMedia=mediaMapper.selectRandomOneByConceptNo(conceptNo);
		String mediaLink=(RandomMedia==null)?"default media link":RandomMedia.getLink();
		conceptResponseDto.setBucketDoneCnt(bucketDone);
		conceptResponseDto.setBucketTotalCnt(bucketCnt);
		conceptResponseDto.setMedia(mediaLink);	
		if (conceptResponseDto instanceof ConceptDetailResponseDto) {
			List<String> mediaList=new ArrayList<>();
			for(MediaDto media:mediaMapper.selectByConceptNo(conceptNo)) {
				mediaList.add(media.getLink());
			}
			((ConceptDetailResponseDto)conceptResponseDto).setMediaList(mediaList);
		}	
	}
}

