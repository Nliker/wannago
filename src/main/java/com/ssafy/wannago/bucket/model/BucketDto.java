package com.ssafy.wannago.bucket.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BucketDto {
	private int bucketNo;
	private int contentId;
	private String bucketDeadline;
	private boolean bucketIsDone;
	private int conceptNo;
	
	public boolean getBucketIsDone() {
		return this.bucketIsDone;
	}
	
	@Override
	public String toString() {
		return "BucketDto [bucketNo=" + bucketNo + ", contentId=" + contentId + ", bucketDeadline=" + bucketDeadline
				+ ", bucketIsDone=" + bucketIsDone + ", conceptNo=" + conceptNo + "]";
	}

}
