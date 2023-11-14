package com.ssafy.wannago.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDto {
	private String userEmail;
	private String userName;
	private String userRole;
	
	public UserResponseDto(String userEmailId,String userEmailDomain,String userName,String userRole) {
		this.userEmail=userEmailId+"@"+userEmailDomain;
		this.userName=userName;
		this.userRole=userRole;
	}

	@Override
	public String toString() {
		return "UserResponseDto [userEmail=" + userEmail + ", userName=" + userName + ", userRole=" + userRole + "]";
	}
}
