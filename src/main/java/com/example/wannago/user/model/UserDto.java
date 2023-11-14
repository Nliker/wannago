package com.example.wannago.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDto {
	private String userId;
	private String userPassword;
	private String userEmailId;
	private String userEmailDomain;
	private String userName;
	private String userRole="silver";
	
	
	@Override
	public String toString() {
		return "UserDto [userId=" + userId + ", userPassword=" + userPassword + ", userEmailId=" + userEmailId
				+ ", userEmailDomain=" + userEmailDomain + ", userName=" + userName + ", userRole=" + userRole + "]";
	}
	

}
