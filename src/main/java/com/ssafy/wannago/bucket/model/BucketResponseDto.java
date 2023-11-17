package com.ssafy.wannago.bucket.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BucketResponseDto {
	private int bucketNo;
	private String bucketDeadline;
	private boolean bucketIsDone;
	private String title;
	private String addr;
	private String firstImage;
	
	
	
	@Override
	public String toString() {
		return "BucketResponseDto [bucketNo=" + bucketNo + ", bucketDeadline=" + bucketDeadline + ", bucketIsDone="
				+ bucketIsDone + "]";
	}


	public BucketResponseDto(BucketJoinAttractionDto bucketJoinAttraction) {
		this.bucketNo=bucketJoinAttraction.getBucketNo();
		this.bucketDeadline=bucketJoinAttraction.getBucketDeadline();
		this.bucketIsDone=bucketJoinAttraction.getBucketIsDone();
		this.title=bucketJoinAttraction.getTitle();
		this.addr=bucketJoinAttraction.getAddr();
		this.firstImage=bucketJoinAttraction.getFirstImage();
	}
}
