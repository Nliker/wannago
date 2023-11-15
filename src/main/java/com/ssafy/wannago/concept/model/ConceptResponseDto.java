package com.ssafy.wannago.concept.model;


import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ConceptResponseDto {
	private int conceptNo;
	private String conceptTitle;
	private String media;
	private int bucketTotalCnt;
	private int bucketDoneCnt;
	
	
	public ConceptResponseDto(ConceptDto concept,String media,int bucketTotalCnt,int bucketDoneCnt) {
		this.conceptNo=concept.getConceptNo();
		this.conceptTitle=concept.getConceptTitle();
		this.media=media;
		this.bucketTotalCnt=bucketTotalCnt;
		this.bucketDoneCnt=bucketDoneCnt;
	}
	
	public ConceptResponseDto(ConceptDto concept) { // set meta data later
		this.conceptNo=concept.getConceptNo();
		this.conceptTitle=concept.getConceptTitle();
	}
}
