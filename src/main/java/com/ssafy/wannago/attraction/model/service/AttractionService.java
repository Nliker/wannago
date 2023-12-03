package com.ssafy.wannago.attraction.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.wannago.attraction.model.AttractionResponseDto;

public interface AttractionService {
	List<AttractionResponseDto> getAttractionList(Map<String, String> map) throws Exception;
}
