package com.ssafy.wannago.media.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MediaDto {
	private int mediaNo;
	private int conceptNo;
	private String mediaSaveFolder;
	private String mediaOriginFile;
	private String mediaSaveFile;
	private String mediaType;
	
	public String getLink() {
		return "download"+this.mediaSaveFolder+"/"+this.mediaOriginFile+"/"+this.mediaSaveFile;
	}

	@Override
	public String toString() {
		return "MediaDto [mediaNo=" + mediaNo + ", conceptNo=" + conceptNo + ", mediaSaveFolder=" + mediaSaveFolder
				+ ", mediaOriginFile=" + mediaOriginFile + ", mediaSaveFile=" + mediaSaveFile + ", mediaType="
				+ mediaType + "]";
	}
}	
