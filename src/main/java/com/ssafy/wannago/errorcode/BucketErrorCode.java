package com.ssafy.wannago.errorcode;

import lombok.Getter;

@Getter
public enum BucketErrorCode {
	UserIdNotMatchBucketUserId(401,"버킷의 소유주가 일치하지 않습니다."),
	BucketNotFound(404,"해당 버킷은 존재하지 않습니다.");
	private int code;
    private String description;
    
    private BucketErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
