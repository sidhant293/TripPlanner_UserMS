package com.tripplanner.dto;

import com.tripplanner.entity.UserEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
	private Integer userId;
	private String userName;
	private String emailId;
	private String password;
	
	public UserEntity convertIntoEntity() {
		UserEntity userEntity=new UserEntity();
		userEntity.setEmailId(emailId);
		userEntity.setPassword(password);
		userEntity.setUserName(userName);
		return userEntity;
	}
}
