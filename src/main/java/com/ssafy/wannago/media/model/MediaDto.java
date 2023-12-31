package com.ssafy.wannago.media.model;

import java.io.File;
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
	private String mediaRegDate;

	public String getSavePath() {
		return this.mediaSaveFolder+File.separator+this.mediaSaveFile;
	}
	
	public String getFileNameWithoutExtension() {
		return this.mediaSaveFile.substring(0,this.mediaSaveFile.lastIndexOf('.'));
	}
	public String getOriginFileNameWithoutExtension() {
		return this.mediaOriginFile.substring(0,this.mediaOriginFile.lastIndexOf('.'));
	}
	public MediaDto(int conceptNo, String mediaSaveFolder, String mediaOriginFile, String mediaSaveFile,
			String mediaType) {
		super();
		this.conceptNo = conceptNo;
		this.mediaSaveFolder = mediaSaveFolder;
		this.mediaOriginFile = mediaOriginFile;
		this.mediaSaveFile = mediaSaveFile;
		this.mediaType = mediaType;
	}

	public MediaDto() {
		super();
	}
	@Override
	public String toString() {
		return "MediaDto [mediaNo=" + mediaNo + ", conceptNo=" + conceptNo + ", mediaSaveFolder=" + mediaSaveFolder
				+ ", mediaOriginFile=" + mediaOriginFile + ", mediaSaveFile=" + mediaSaveFile + ", mediaType="
				+ mediaType + ", mediaRegDate=" + mediaRegDate + "]";
	}
	
}	
