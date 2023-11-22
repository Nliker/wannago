package com.ssafy.wannago.errorcode;

import lombok.Getter;

@Getter
public enum FileErrorCode {
	NotFoundFile(404,"요청하신 파일을 찾을 수 없습니다.");
	
	
    private int code;
    private String description;
    
    private FileErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
