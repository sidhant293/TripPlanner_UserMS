package com.tripplanner.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tripplanner.dto.TokenDTO;
import com.tripplanner.dto.UserDTO;
import com.tripplanner.entity.UserEntity;
import com.tripplanner.jwt.JwtTokenProvider;
import com.tripplanner.repository.UsersRepository;
import com.tripplanner.utility.HashingUtility;
import com.tripplanner.validator.UserValidator;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private final String PRE_USER = "US0";

	@Override
	public TokenDTO authenticateUser(String email, String password) throws Exception {
		// validate and get user from repository
		UserValidator.validateUserForLogin(email, password);
		UserEntity userEntity = usersRepository.findByEmailId(email);
		UserDTO userDTO = null;
		if (userEntity != null) {
			userDTO = userEntity.convertIntoModel();
		}
		if (userDTO == null)
			throw new Exception("UserService.INVALID_CREDENTIALS");
		String passwordFromDB = userDTO.getPassword();
		// match passwords and update refresh token in db
		if (passwordFromDB != null) {
			String hashedPassword = HashingUtility.getHashValue(password);

			if (hashedPassword.equals(passwordFromDB)) {
				userDTO.setPassword(null);

				String token = jwtTokenProvider.generateToken(userDTO.getUserName(), userDTO.getUserId().toString());
				String refreshToken = HashingUtility.getHashValue(token);
				// set refresh token in DB
				userEntity.setRefreshToken(refreshToken);
				TokenDTO tokenDto = new TokenDTO(token, refreshToken, userDTO.getUserName(),
						PRE_USER + userDTO.getUserId().toString(),jwtTokenProvider.getExpDate());
				return tokenDto;
			} else
				throw new Exception("UserService.INVALID_CREDENTIALS");
		} else
			throw new Exception("UserService.INVALID_CREDENTIALS");

	}

	@Override
	public TokenDTO registerUser(UserDTO userDTO) throws Exception {
		UserValidator.validateUserForRegister(userDTO.getPassword(), userDTO.getEmailId(), userDTO.getUserName());
		if (usersRepository.findByEmailId(userDTO.getEmailId()) != null)
			throw new Exception("UserService.CONTACT_PRESENT");
		userDTO.setPassword(HashingUtility.getHashValue(userDTO.getPassword()));
		UserEntity userEntity = userDTO.convertIntoEntity();
		userEntity.setRefreshToken("token");
		UserEntity userFromDB = usersRepository.save(userEntity);
		userDTO = userFromDB.convertIntoModel();
		userDTO.setPassword(null);

		String token = jwtTokenProvider.generateToken(userDTO.getUserName(), userDTO.getUserId().toString());
		String refreshToken = HashingUtility.getHashValue(token);
		// set refresh token in DB
		userEntity.setRefreshToken(refreshToken);
		TokenDTO tokenDto = new TokenDTO(token, refreshToken, userDTO.getUserName(),
				PRE_USER + userDTO.getUserId().toString(),jwtTokenProvider.getExpDate());
		return tokenDto;
	}

	@Override
	public TokenDTO validateRefreshToken(String tokenFromAPI, String userId) throws Exception {
		// convert userId of string to int
		System.out.println("hello " +userId+" "+ userId.indexOf(PRE_USER)+" "+userId.substring(userId.indexOf(PRE_USER) + PRE_USER.length()));
		int id = Integer.parseInt(userId.substring(userId.indexOf(PRE_USER) + PRE_USER.length()));
		// if token from api matches in database then true else false
		UserEntity userFromRepository = usersRepository.findById(id)
				.orElseThrow(() -> new Exception("UserService.INVALID_CREDENTIALS"));
		boolean validation = userFromRepository.getRefreshToken().equals(tokenFromAPI);
		if(!validation) throw new Exception("UserService.INVALID_REFRESH");
		
		String token = jwtTokenProvider.generateToken(userFromRepository.getUserName(), userFromRepository.getUserId().toString());
		String refreshToken = HashingUtility.getHashValue(token);
		// set refresh token in DB
		userFromRepository.setRefreshToken(refreshToken);
		TokenDTO tokenDto = new TokenDTO(token, refreshToken, userFromRepository.getUserName(),
				PRE_USER + userFromRepository.getUserId().toString(),jwtTokenProvider.getExpDate());
		return tokenDto;
	}
	
	
}
