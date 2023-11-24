package com.ssafy.wannago.attraction.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.wannago.attraction.model.AttractionJoinDescriptionDto;
import com.ssafy.wannago.attraction.model.AttractionResponseDto;
import com.ssafy.wannago.attraction.model.mapper.AttractionMapper;
import com.ssafy.wannago.errorcode.AttractionErrorCode;
import com.ssafy.wannago.exception.AttractionException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService{
	private final AttractionMapper attractionMapper;
	
	@Override
	public List<AttractionResponseDto> getAttractionList(Map<String, String> map) throws Exception {

		Map<String, Object> param = new HashMap<String, Object>();
		log.debug(map.get("swLon"));
		if(map.get("neLat")==null || "".equals(map.get("neLat"))
				|| map.get("neLon")==null || "".equals(map.get("neLon"))
				|| map.get("swLat")==null || "".contentEquals(map.get("swLat"))
				|| map.get("swLon")==null || "".equals(map.get("swLon"))
				|| map.get("sidoCode")==null || "".equals(map.get("sidoCode"))){
			throw new AttractionException(AttractionErrorCode.NotFoundRequiredParameter.getCode(),AttractionErrorCode.NotFoundRequiredParameter.getDescription());
		}				
		param.put("neLat",new BigDecimal(map.get("neLat")));
		param.put("neLon",new BigDecimal(map.get("neLon")));
		param.put("swLat",new BigDecimal(map.get("swLat")));
		param.put("swLon",new BigDecimal(map.get("swLon")));
		param.put("sidoCode",Integer.parseInt(map.get("sidoCode")));
		param.put("title",map.get("title"));
		
		log.debug(param.toString());
		
		List<AttractionResponseDto> attractionList=new ArrayList<>();
		for(AttractionJoinDescriptionDto attraction:attractionMapper.selectByParam(param)) {
			attractionList.add(new AttractionResponseDto(attraction));
		}
		return attractionList;
	}
}
