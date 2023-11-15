package com.ssafy.wannago.errorcode;

import lombok.Getter;

@Getter
public enum ConceptErrorCode {
	UserIdNotMatchConceptUserId(401,"컨셉의 소유주가 일치하지 않습니다."),
	ConceptNotFound(404,"해당컨셉은 존재하지 않습니다.");
	private int code;
    private String description;
    
    private ConceptErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
