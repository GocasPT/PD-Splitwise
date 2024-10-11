package Message.Request.User;

import Message.Request.EComands;
import Message.Request.Request;

public class Login extends Request {
	private String username;
	private String password;

	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return EComands.LOGIN + " " + username + " " + password;
	}
}