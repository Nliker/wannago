package com.ssafy.wannago.user.model.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.wannago.user.model.UserDto;

@Mapper
public interface UserMapper {
	UserDto getLoginUser(String id,String password) throws SQLException ;
	UserDto selectByUserId(String userId) throws SQLException;
	UserDto selectByUserEmailId(String userEmailId);
	void insertUser(UserDto user);
}
