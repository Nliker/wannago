package com.ssafy.wannago.media.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaResponseDto {
	private int mediaNo;
	private String mediaType;
	private String mediaRegDate;
	
	public MediaResponseDto(int mediaNo, String mediaType,String mediaRegDate) {
		super();
		this.mediaNo = mediaNo;
		this.mediaType = mediaType;
		this.mediaRegDate=mediaRegDate;
	}
}
