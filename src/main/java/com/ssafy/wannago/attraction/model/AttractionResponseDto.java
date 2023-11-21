package com.ssafy.wannago.attraction.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AttractionResponseDto {
	private int contentId;
	private String title;
	private String address;
	private String image;
	private int readCount;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private String description;
	
	public AttractionResponseDto(AttractionJoinDescriptionDto attraction) {
		this.contentId=attraction.getContentId();
		this.address=attraction.getAddr1();
		this.image=attraction.getFirstImage();
		this.readCount=attraction.getReadCount();
		this.title=attraction.getTitle();
		this.latitude=attraction.getLatitude();
		this.longitude=attraction.getLongitude();
		this.description=attraction.getDescription();
	}

	@Override
	public String toString() {
		return "AttractionResponseDto [contentId=" + contentId + ", title=" + title + ", address=" + address
				+ ", image=" + image + ", readCount=" + readCount + ", latitude=" + latitude + ", longitude="
				+ longitude + ", description=" + description + "]";
	}
	
}
