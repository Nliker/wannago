package com.ssafy.wannago.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDto {
	private String userEmail;
	private String userName;
	private String userRole;
	
	public UserResponseDto(UserDto user) {
		this.userEmail=user.getUserEmailId()+"@"+user.getUserEmailDomain();
		this.userName=user.getUserName();
		this.userRole=user.getUserRole();
	}

	@Override
	public String toString() {
		return "UserResponseDto [userEmail=" + userEmail + ", userName=" + userName + ", userRole=" + userRole + "]";
	}
}
