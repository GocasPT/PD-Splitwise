package Message.Request.User;

import Message.Request.EComands;
import Message.Request.Request;

public class Register extends Request {
	private String username;
	private String phone;
	private String email;
	private String password;

	public Register(String username, String phone, String email, String password) {
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return EComands.REGISTER + " " + username + " " + phone + " " + email + " " + password;
	}
}
