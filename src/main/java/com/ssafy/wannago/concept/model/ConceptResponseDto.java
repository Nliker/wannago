package com.ssafy.wannago.concept.model;


import java.util.List;

import com.ssafy.wannago.media.model.MediaResponseDto;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class ConceptResponseDto {
	private int conceptNo;
	private String conceptTitle;
	private List<MediaResponseDto> mediaInfoList;
	private int bucketTotalCnt;
	private int bucketDoneCnt;
	private String conceptRegDate;
	

	
	public ConceptResponseDto(ConceptDto concept) {
		this.conceptNo=concept.getConceptNo();
		this.conceptTitle=concept.getConceptTitle();
		this.conceptRegDate=concept.getConceptRegDate();
	}



	@Override
	public String toString() {
		return "ConceptResponseDto [conceptNo=" + conceptNo + ", conceptTitle=" + conceptTitle + ", mediaInfoList="
				+ mediaInfoList + ", bucketTotalCnt=" + bucketTotalCnt + ", bucketDoneCnt=" + bucketDoneCnt
				+ ", conceptRegDate=" + conceptRegDate + "]";
	}

}
