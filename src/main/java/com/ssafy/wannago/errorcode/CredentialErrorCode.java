package com.ssafy.wannago.errorcode;

import lombok.Getter;

@Getter
public enum CredentialErrorCode {
	NotFoundId(400,"아이디를 찾을 수 없습니다."),
	NotMatchIdPassword(400,"비밀번호가 일치하지 않습니다."),
	AlreadyExistId(400,"이미 존재하는 이이디 입니다."),
	AlreadyEmailId(400,"이미 존재하는 이메일 입니다.")
	;

	private int code;
    private String description;
    
    private CredentialErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
