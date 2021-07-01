package com.tripplanner.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tripplanner.dto.TokenDTO;
import com.tripplanner.dto.UserDTO;
import com.tripplanner.service.UserService;

@RestController
@RequestMapping("UserAPI")
@CrossOrigin(
        //origins = "*", aqui dejar este si la hace de pedo
        origins = {"*"},
        allowCredentials = "true",
        maxAge = 3600, 
        allowedHeaders = "*",
        methods= {RequestMethod.GET,RequestMethod.POST, 
                RequestMethod.DELETE, RequestMethod.PUT, 
                RequestMethod.PATCH, RequestMethod.OPTIONS, 
                RequestMethod.HEAD, RequestMethod.TRACE}
)
public class UserAPI {

	@Autowired
	private UserService userService;

	@Autowired
	private Environment environment;

	void setTokenCookie(HttpServletResponse response,TokenDTO token) {
		Cookie cookie = new Cookie("refreshToken", token.getRefreshToken());
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	public ResponseEntity<?> authenticateUser(@RequestBody UserDTO user, HttpServletResponse response) {

		try {
			TokenDTO token = userService.authenticateUser(user.getEmailId(), user.getPassword());
			setTokenCookie(response,token);
			return new ResponseEntity<>(token, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, environment.getProperty(e.getMessage()));
		}
	}

	@PostMapping(value = "/userRegister")
	public ResponseEntity<TokenDTO> registerUser(@RequestBody UserDTO user) {
		try {
			TokenDTO token = userService.registerUser(user);
			return new ResponseEntity<TokenDTO>(token, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, environment.getProperty(e.getMessage()));
		}
	}

	@GetMapping(value = "/validateRefresh/{userId}")
	public ResponseEntity<TokenDTO> validateRefreshToken(@PathVariable(value = "userId") String userId, HttpServletResponse response,
			@CookieValue(value = "refreshToken") String refreshToken) {
		try {
			TokenDTO token=userService.validateRefreshToken(refreshToken, userId);
			setTokenCookie(response,token);
			return new ResponseEntity<TokenDTO>(token,HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, environment.getProperty(e.getMessage()));
		}
	}
	
	@GetMapping(value = "/removeRefresh")
	public ResponseEntity<TokenDTO> removeRefreshToken( HttpServletResponse response) {
			TokenDTO token=new TokenDTO();
			token.setRefreshToken(null);
			setTokenCookie(response,token);
			return new ResponseEntity<TokenDTO>(token,HttpStatus.OK);
	}

}
