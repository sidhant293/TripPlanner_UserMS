package com.tripplanner.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TokenDTO {
	private String token;
	private String refreshToken;
	private String username;
	private String userId;
	private Date exp;
	
	public TokenDTO(String token,String refreshToken,String username,String userId,Date exp) {
		this.token=token;
		this.refreshToken=refreshToken;
		this.userId=userId;
		this.username=username;
		this.exp=exp;
	}
}
