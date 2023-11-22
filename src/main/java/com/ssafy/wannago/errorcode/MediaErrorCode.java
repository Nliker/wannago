package com.ssafy.wannago.errorcode;

import lombok.Getter;

@Getter
public enum MediaErrorCode {
	NotFoundMedia(404,"해당 파일이 존재하지 않습니다."),
	MediaUserIdNotMatch(401,"해당 파일의 소유주가 다릅니다."),
	NotCorrectType(401,"요청하신 파일의 타입이 옳바르지 않습니다.");
	
	private int code;
    private String description;
    
    private MediaErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
