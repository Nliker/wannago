package com.ssafy.wannago.user.model.service;

import java.sql.SQLException;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.wannago.user.model.UserDto;
import com.ssafy.wannago.user.model.UserResponseDto;


public interface UserService {
	String userLogin(UserDto user) throws Exception;
	String socialLogin(String code, String registrationId) throws Exception;
	String getAccessToken(String authorizationCode, String registrationId) throws Exception;
	JsonNode getSocialUserResource(String accessToken, String registrationId) throws Exception;
	String createToken(UserDto user) throws Exception;
	void createUser(UserDto user) throws Exception;
	UserResponseDto getUserInfo(String userId) throws Exception;
	void updateUserInfo(String userId,String password) throws Exception;
}
