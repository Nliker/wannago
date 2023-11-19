package com.ssafy.wannago.concept.model.service;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.wannago.bucket.model.BucketDto;
import com.ssafy.wannago.bucket.model.BucketJoinAttractionDto;
import com.ssafy.wannago.bucket.model.BucketResponseDto;
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
	
	@Value("${concept.mediaListSize}")
	private int conceptMediaListSize;
	
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
            String extension=originalFileName.substring(originalFileName.lastIndexOf('.')+1).toLowerCase();
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
	public List<ConceptResponseDto> getConceptList(String userId,Map<String, String> map) throws Exception {
		log.info("class:=================getConceptList====================");
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start",map.get("start")==null?0:Integer.parseInt(map.get("start")));
		param.put("size",map.get("size")==null?conceptMediaListSize:Integer.parseInt(map.get("size")));
		param.put("orderby", map.get("orderby")==null?"latest":map.get("orderby"));
		param.put("userId", userId);
		log.debug(param.toString());

		List<ConceptDto> conceptList=conceptMapper.selectByUserId(param);
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
			for(MediaDto media:mediaMapper.selectAllByConceptNo(conceptNo)) {
				MediaResponseDto mediaReponse=new MediaResponseDto(media.getMediaNo(),media.getMediaType(),media.getMediaRegDate());
				mediaList.add(mediaReponse);
			}
			((ConceptDetailResponseDto)conceptResponseDto).setMediaInfoList(mediaList);
		}else {
			MediaDto RandomMedia=mediaMapper.selectRandomOneByConceptNo(conceptNo);
			MediaResponseDto mediaReponse= (RandomMedia==null)?
					new MediaResponseDto(0,"raw",(new Date()).toString()):new MediaResponseDto(RandomMedia.getMediaNo(),RandomMedia.getMediaType(),RandomMedia.getMediaRegDate());
			conceptResponseDto.setMediaInfoList(Arrays.asList(mediaReponse));
		}
	}

	@Override
	public List<MediaResponseDto> getConceptMediaList(String userId, int conceptNo,Map<String, String> map) throws Exception {
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start",map.get("start")==null?0:Integer.parseInt(map.get("start")));
		param.put("size",map.get("size")==null?conceptMediaListSize:Integer.parseInt(map.get("size")));
		param.put("type", map.get("type")==null?"all":map.get("type"));
		param.put("conceptNo",conceptNo);
		log.debug(param.toString());
		List<MediaResponseDto> mediaList=new ArrayList<>();
		for(MediaDto media:mediaMapper.selectByConceptNo(param)) {
			mediaList.add(new MediaResponseDto(media.getMediaNo(),media.getMediaType(),media.getMediaRegDate()));
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
	@Transactional
	@Override
	public void deleteConcept(String userId, int conceptNo) throws Exception {
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		mediaMapper.deleteByConceptNo(conceptNo);
		conceptMapper.deleteByConceptNo(conceptNo);
		
	}

	@Override
	public List<BucketResponseDto> getBucketList(String userId, int conceptNo) throws Exception {
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		List<BucketResponseDto> bucketResponseList=new ArrayList<>();
		log.debug(bucketMapper.selectByConceptNo(conceptNo).toString());
		for(BucketJoinAttractionDto bucket:bucketMapper.selectByConceptNo(conceptNo)) {
			log.debug(bucket.toString());
			bucketResponseList.add(new BucketResponseDto(bucket));
		}
		
		return bucketResponseList;
	}

	@Override
	public void createBucket(String userId, int conceptNo,BucketDto bucket) throws Exception {
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		bucket.setConceptNo(conceptNo);
		bucketMapper.insertBucket(bucket);
	}
}

