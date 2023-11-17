package com.ssafy.wannago.bucket.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BucketJoinAttractionDto extends BucketDto{
	private String title;
	private String addr;
	private String firstImage;
	
	@Override
	public String toString() {
		return "BucketJoinAttraction [title=" + title + ", addr=" + addr + ", firstImage=" + firstImage + "]";
	}
}
