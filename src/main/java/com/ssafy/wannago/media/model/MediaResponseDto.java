package com.ssafy.wannago.media.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaResponseDto {
	private int mediaNo;
	private String mediaType;

	public MediaResponseDto(int mediaNo, String mediaType) {
		super();
		this.mediaNo = mediaNo;
		this.mediaType = mediaType;
	}
}
