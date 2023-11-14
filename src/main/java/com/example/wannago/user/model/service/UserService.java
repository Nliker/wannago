package com.example.wannago.user.model.service;

import java.sql.SQLException;

import com.example.wannago.user.model.UserDto;
import com.fasterxml.jackson.databind.JsonNode;


public interface UserService {
	String userLogin(UserDto user) throws Exception;
	String socialLogin(String code, String registrationId) throws Exception;
	String getAccessToken(String authorizationCode, String registrationId) throws Exception;
	JsonNode getSocialUserResource(String accessToken, String registrationId) throws Exception;
	String createToken(UserDto user) throws Exception;
	void createUser(UserDto user) throws Exception;
}
