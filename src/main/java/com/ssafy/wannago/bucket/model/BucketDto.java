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
	private boolean bucketDeleted;
	
	public boolean getBucketIsDone() {
		return this.bucketIsDone;
	}
	public boolean getBucketDeleted() {
		return this.bucketDeleted;
	}

	@Override
	public String toString() {
		return "BucketDto [bucketNo=" + bucketNo + ", contentId=" + contentId + ", bucketDeadline=" + bucketDeadline
				+ ", bucketIsDone=" + bucketIsDone + ", conceptNo=" + conceptNo + ", bucketDeleted=" + bucketDeleted
				+ "]";
	}

}
