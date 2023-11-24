package com.ssafy.wannago.errorcode;

import lombok.Getter;

@Getter
public enum AttractionErrorCode {
	NotFoundRequiredParameter(401,"필수 매개변수가 존재하지 않습니다.");
	
	private int code;
    private String description;
    
    private AttractionErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
