package com.ssafy.wannago.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDto {
	private String userId;
	private String userName;
	private String userRole;
	private String userEmailId;
	private String userEmailDomain;
	
	public UserResponseDto(UserDto user) {
		this.userEmailId=user.getUserEmailId();
		this.userEmailDomain=user.getUserEmailDomain();
		this.userName=user.getUserName();
		this.userRole=user.getUserRole();
		this.userId=user.getUserId();
	}

	@Override
	public String toString() {
		return "UserResponseDto [userId=" + userId + ", userName=" + userName + ", userRole=" + userRole
				+ ", userEmailId=" + userEmailId + ", userEmailDomain=" + userEmailDomain + "]";
	}

}
