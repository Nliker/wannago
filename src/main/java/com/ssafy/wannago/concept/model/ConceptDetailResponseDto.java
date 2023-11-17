package com.ssafy.wannago.concept.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConceptDetailResponseDto extends ConceptResponseDto{
	//설명과 공유된 수 추가
	private String conceptDescription;
	private int conceptSharedCnt;
	

	public ConceptDetailResponseDto(ConceptDto concept) {//set meta datas later
		super(concept);
		this.conceptDescription=concept.getConceptDescription();
		this.conceptSharedCnt=concept.getConceptSharedCnt();
	}
}
