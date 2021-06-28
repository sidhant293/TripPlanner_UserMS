package com.tripplanner.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tripplanner.dto.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String userName;
	private String emailId;
	private String password;
	private String refreshToken;

	public UserDTO convertIntoModel() {
		UserDTO user=new UserDTO();
		user.setUserId(userId);
		user.setEmailId(emailId);
		user.setUserName(userName);
		user.setPassword(password);
		return user;
	}
}
