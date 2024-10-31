package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;
import java.util.Objects;

public final class User implements Serializable {
	private String username;
	private String email;
	private String phoneNumber;
	private String password;

	public User(String username) {
		this.username = username;
	}

	public User(String username, String email, String phoneNumber) {
		this.username = username;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}

	public User(String username, String email, String phoneNumber, String password) {
		this.username = username;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, email, phoneNumber, password);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (User) obj;
		return Objects.equals(this.username, that.username) &&
				Objects.equals(this.email, that.email) &&
				Objects.equals(this.phoneNumber, that.phoneNumber) &&
				Objects.equals(this.password, that.password);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("User [username: ").append(username);
		if (email != null) sb.append(", email: ").append(email);
		if (phoneNumber != null) sb.append(", phone: ").append(phoneNumber);
		sb.append("]");

		return sb.toString();
	}

}
