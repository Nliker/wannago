package com.ssafy.wannago.concept.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConceptDetailResponseDto extends ConceptResponseDto{
	//설명과 공유된 수 추가
	private String conceptDescription;
	private int conceptSharedCnt;
	private String[] mediaList;
	
	
	public ConceptDetailResponseDto(ConceptDto concept, String media, int bucketTotalCnt, int bucketDoneCnt) {
		super(concept, media, bucketTotalCnt, bucketDoneCnt);
		this.conceptDescription=concept.getConceptDescription();
		this.conceptSharedCnt=concept.getConceptSharedCnt();
	}
	public ConceptDetailResponseDto(ConceptDto concept) {//set meta datas later
		super(concept);
		this.conceptDescription=concept.getConceptDescription();
		this.conceptSharedCnt=concept.getConceptSharedCnt();
	}
}
