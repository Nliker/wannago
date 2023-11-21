package com.ssafy.wannago.user.model.mapper;

import java.sql.SQLException;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.wannago.user.model.UserDto;

@Mapper
public interface UserMapper {
	UserDto selectByUserId(String userId) throws SQLException;
	UserDto selectByUserEmailId(String userEmailId) throws SQLException;
	void insertUser(UserDto user) throws SQLException;
	void updateUserPassword(String userId,String password) throws SQLException;
}
