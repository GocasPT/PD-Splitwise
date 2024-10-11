package Message.Request.User;

import Message.Request.EComands;
import Message.Request.Request;

public class EditUser extends Request {
	private String username;
	private String phone;
	private String email;
	private String password;

	public EditUser(String username, String phone, String email, String password) {
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
		return EComands.EDIT_USER + " " + username + " " + phone + " " + email + " " + password;
	}
}
