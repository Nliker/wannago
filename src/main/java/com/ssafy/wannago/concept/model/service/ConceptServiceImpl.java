package com.ssafy.wannago.concept.model.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.wannago.attraction.model.AttractionJoinDescriptionDto;
import com.ssafy.wannago.attraction.model.AttractionResponseDto;
import com.ssafy.wannago.attraction.model.mapper.AttractionMapper;
import com.ssafy.wannago.bucket.model.BucketDto;
import com.ssafy.wannago.bucket.model.BucketJoinAttractionDto;
import com.ssafy.wannago.bucket.model.BucketResponseDto;
import com.ssafy.wannago.bucket.model.mapper.BucketMapper;
import com.ssafy.wannago.concept.model.ConceptDetailResponseDto;
import com.ssafy.wannago.concept.model.ConceptDto;
import com.ssafy.wannago.concept.model.ConceptResponseDto;
import com.ssafy.wannago.concept.model.ConceptSearchResponseDto;
import com.ssafy.wannago.concept.model.mapper.ConceptMapper;
import com.ssafy.wannago.errorcode.ConceptErrorCode;
import com.ssafy.wannago.exception.ConceptException;
import com.ssafy.wannago.file.utils.FileUtil;
import com.ssafy.wannago.file.utils.FileUtilFactory;
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

	@Value("${concept.mediaListSize}")
	private int conceptMediaListSize;
	
	@Value("${concept.ListSize}")
	private int conceptListSize;

	private final ConceptMapper conceptMapper;
	private final UserMapper userMapper;
	private final MediaMapper mediaMapper;
	private final BucketMapper bucketMapper;
	private final AttractionMapper attractionMapper;
	
	private final FileUtilFactory fileUtilFactory;
	
	@Transactional
	@Override
	public ConceptResponseDto createConcept(String userId, ConceptDto concept,MultipartFile[] files) throws Exception {
		log.info("class:=================createConcept====================");

		UserDto user=userMapper.selectByUserId(userId);
		concept.setUserId(user.getUserId());
		concept.setUserName(user.getUserName());
		conceptMapper.insertConcept(concept);
		
		if(files!=null) {
			FileUtil fileUtil=fileUtilFactory.getInstance(files);
			List<MediaDto> mediaList=fileUtil.saveFiles(concept.getConceptNo());
			
			
			log.debug("media List insert start");
			
			if(mediaList.size()!=0) {
				mediaMapper.insertMediaList(mediaList);
			}
			for(MediaDto media:mediaList) {
				log.debug("media thumb start");
				if("image".equals(media.getMediaType())) {
					fileUtil.generateImageThumbnail(media);
				}else if("video".equals(media.getMediaType())){
					fileUtil.resizeVideo(media);
				}
			}
		}
		
		ConceptResponseDto conceptResponseDto=new ConceptResponseDto(concept);
		log.info("conceptResponseDto"+conceptResponseDto.toString());
		setConceptMetaData(conceptResponseDto);
		
		
		return conceptResponseDto;
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
					new MediaResponseDto(0,"image",(new Date()).toString()):new MediaResponseDto(RandomMedia.getMediaNo(),RandomMedia.getMediaType(),RandomMedia.getMediaRegDate());
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
	
	@Transactional
	@Override
	public void addConceptMediaList(String userId, int conceptNo, MultipartFile[] files) throws Exception {
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		
		FileUtil fileUtil=fileUtilFactory.getInstance(files);
		List<MediaDto> mediaList=fileUtil.saveFiles(concept.getConceptNo());
		if(mediaList.size()!=0) {
			mediaMapper.insertMediaList(mediaList);
		}
		for(MediaDto media:mediaList) {
			log.debug("media thumb start");
			if("image".equals(media.getMediaType())) {
				fileUtil.generateImageThumbnail(media);
			}else if("video".equals(media.getMediaType())){
				fileUtil.resizeVideo(media);
			}
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
		bucketMapper.deleteByConcepNo(conceptNo);
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



	@Override
	public void updateConcept(String userId, int conceptNo,ConceptDto updateConcept) throws Exception{
		log.info("class:=================updateConcept====================");
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		updateConcept.setConceptNo(conceptNo);
		log.debug(updateConcept.toString());
		conceptMapper.updateConcept(updateConcept);
	}



	@Override
	public List<ConceptSearchResponseDto> getSearchConceptList(Map<String, String> map) throws Exception {
		log.info("class:=================getSearchConceptList====================");
		
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("start",map.get("start")==null?0:Integer.parseInt(map.get("start")));
		param.put("size",map.get("size")==null?conceptListSize:Integer.parseInt(map.get("size")));
		param.put("keyword",map.get("keyword"));
		param.put("orderby",map.get("orderby"));
		log.debug(param.toString());

		List<ConceptDto> conceptList=conceptMapper.selectByParam(param);
		
		List<ConceptSearchResponseDto> conceptResponseList=new ArrayList<>();
		
		for(ConceptDto concept:conceptList) {
			ConceptSearchResponseDto conceptResponseDto=new ConceptSearchResponseDto(concept);
			MediaDto RandomMedia=mediaMapper.selectRandomOneByConceptNo(concept.getConceptNo());
			MediaResponseDto mediaReponse= (RandomMedia==null)?
					new MediaResponseDto(0,"image",(new Date()).toString()):new MediaResponseDto(RandomMedia.getMediaNo(),RandomMedia.getMediaType(),RandomMedia.getMediaRegDate());
			conceptResponseDto.setMediaInfoList(Arrays.asList(mediaReponse));
			conceptResponseList.add(conceptResponseDto);
		}
		return conceptResponseList;
	}


	@Override
	public List<AttractionResponseDto> getConceptAttractionList(int conceptNo) throws Exception {
		log.info("class:=================getConceptAttractionList====================");
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		List<AttractionResponseDto> attractionList=new ArrayList<>();
		for(AttractionJoinDescriptionDto attraction:attractionMapper.selectByConceptNo(conceptNo)) {
			attractionList.add(new AttractionResponseDto(attraction));
		}
		return attractionList;
	}



	@Override
	public List<BucketResponseDto> getBucketTrash(String userId, int conceptNo) throws Exception {
		log.info("class:=================getBucketTrash====================");
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		if(!concept.getUserId().equals(userId)) {
			throw new ConceptException(ConceptErrorCode.UserIdNotMatchConceptUserId.getCode(), ConceptErrorCode.UserIdNotMatchConceptUserId.getDescription());
		}
		List<BucketResponseDto> bucketResponseList=new ArrayList<>();
		for(BucketJoinAttractionDto bucket:bucketMapper.selectDeletedByConceptNo(conceptNo)) {
			log.debug(bucket.toString());
			bucketResponseList.add(new BucketResponseDto(bucket));
		}
		
		return bucketResponseList;
	}


	@Transactional
	@Override
	public void createConceptShare(String userId, int conceptNo) throws Exception {
		log.info("class:=================createConceptShare====================");
		log.info("shared conceptNo:" + conceptNo);
		ConceptDto concept=conceptMapper.selectByConceptNo(conceptNo);
		if(concept==null) {
			throw new ConceptException(ConceptErrorCode.ConceptNotFound.getCode(), ConceptErrorCode.ConceptNotFound.getDescription());
		}
		ConceptDto newConcept=new ConceptDto();
		newConcept.setConceptTitle(concept.getConceptTitle());
		newConcept.setUserId(userId);
		newConcept.setUserName((userMapper.selectByUserId(userId)).getUserName());
		conceptMapper.insertConcept(newConcept);
		log.info("new concept:" +newConcept.toString());
		for(AttractionJoinDescriptionDto attraction:attractionMapper.selectByConceptNo(conceptNo)) {
			BucketDto bucket=new BucketDto();
			bucket.setContentId(attraction.getContentId());
			bucket.setConceptNo(newConcept.getConceptNo());
			bucketMapper.insertBucket(bucket);
		}
		
		conceptMapper.updateConceptShareCountUp(conceptNo);
		
	}
}

