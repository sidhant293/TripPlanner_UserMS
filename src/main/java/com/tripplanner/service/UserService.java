package com.tripplanner.service;

import com.tripplanner.dto.TokenDTO;
import com.tripplanner.dto.UserDTO;

public interface UserService {

	public TokenDTO authenticateUser(String contactNumber, String password) throws Exception;
	public TokenDTO registerUser(UserDTO user) throws Exception;
	public TokenDTO validateRefreshToken(String tokenFromAPI,String userId) throws Exception;
	
}
