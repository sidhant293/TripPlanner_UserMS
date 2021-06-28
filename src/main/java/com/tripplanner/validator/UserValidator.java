package com.tripplanner.validator;

public class UserValidator {

	public static void validateUserForRegister( String password,String email,String userName) throws Exception {

		validateUserForLogin(email,password);
		if (!validateUserName(userName))
			throw new Exception("UserValidator.INVALID_USER_NAME");
		
	}
	
	public static void validateUserForLogin(String email, String password) throws Exception {

		if(!validateEmail(email))
			throw new Exception("UserValidator.INVALID_EMAIL");
		
		if (!validatePassword(password))
			throw new Exception("UserValidator.INVALID_PASSWORD_FORMAT");
	}

	public static Boolean validatePassword(String password) {
		if (password == null)
			return false;
		Boolean flag = false;
		if (password.length() >= 7 && password.length() <= 20)
			if (password.matches(".*[A-Z]+.*"))
				if (password.matches(".*[a-z]+.*"))
					if (password.matches(".*[0-9]+.*"))
						if (password.matches(".*[!@#$%^&*].*"))
							flag = true;
		return flag;
	}
	
	public static Boolean validateUserName(String userName) {
		if(userName==null)return false;
		Boolean flag=false;
		if(userName.matches("[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]"))
			if(userName.matches("[a-zA-Z].*"))
				flag=true;
		return flag;
	}
	
	public static Boolean validateEmail(String email) {
		if(email==null)return false;
		Boolean flag=false;
		if(email.matches("[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*"))
			flag=true;
		return flag;
	}
}
