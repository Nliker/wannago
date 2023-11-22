package com.ssafy.wannago.concept.model;

import java.util.List;

import com.ssafy.wannago.media.model.MediaResponseDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConceptSearchResponseDto extends ConceptDto {
	private List<MediaResponseDto> mediaInfoList;

	public ConceptSearchResponseDto(List<MediaResponseDto> mediaInfoList) {
		super();
		this.mediaInfoList = mediaInfoList;
	}

	public ConceptSearchResponseDto(ConceptDto concept) {
		this.setConceptNo(concept.getConceptNo());
		this.setConceptTitle(concept.getConceptTitle());
		this.setConceptDescription(concept.getConceptDescription());
		this.setConceptSharedCnt(concept.getConceptSharedCnt());
		this.setUserId(concept.getUserId());
		this.setUserName(concept.getUserName());
		this.setConceptRegDate(concept.getConceptRegDate());
	}
}
