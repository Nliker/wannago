package com.ssafy.wannago.attraction.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AttractionJoinDescriptionDto {
	private int contentId;
	private String title;
	private String addr1;
	private String firstImage;
	private int readCount;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private String description;
	
	@Override
	public String toString() {
		return "AttractionJoinDescriptionDto [contentId=" + contentId + ", title=" + title + ", addr1=" + addr1
				+ ", firstImage=" + firstImage + ", readCount=" + readCount + ", latitude=" + latitude + ", longitude="
				+ longitude + ", description=" + description + "]";
	}
	

}
