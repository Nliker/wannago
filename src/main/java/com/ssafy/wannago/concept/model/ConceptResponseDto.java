package com.ssafy.wannago.concept.model;


import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ConceptResponseDto {
	private int conceptNo;
	private String conceptTitle;
	private String conceptDescription;
	private int conceptSharedCnt;
	private String userId;
	private String userName;
	
	public ConceptResponseDto(ConceptDto concept) {
		this.conceptNo=concept.getConceptNo();
		this.conceptTitle=concept.getConceptTitle();
		this.conceptSharedCnt=concept.getConceptSharedCnt();
		this.conceptDescription=concept.getConceptDescription();
		this.userId=concept.getUserId();
		this.userName=concept.getUserName();
	}
	public ConceptResponseDto() {
		super();
	}
}
