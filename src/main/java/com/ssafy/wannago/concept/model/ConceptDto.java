package com.ssafy.wannago.concept.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConceptDto {
	private int conceptNo;
	private String conceptTitle;
	private String conceptDescription;
	private int conceptSharedCnt;
	private String userId;
	private String userName;
	
	
	@Override
	public String toString() {
		return "ConceptDto [conceptNo=" + conceptNo + ", conceptTitle=" + conceptTitle + ", conceptDescription="
				+ conceptDescription + ", conceptSharedCnt=" + conceptSharedCnt + ", userId=" + userId + ", userName="
				+ userName + "]";
	}
}
