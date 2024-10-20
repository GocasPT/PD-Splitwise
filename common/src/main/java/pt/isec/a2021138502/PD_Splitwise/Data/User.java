package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;

public record User(String username, String phoneNumber, String email, String password) implements Serializable {
	@Override
	public String toString() {
		return "User [username: " + username + ", email: " + email + "]";
	}
}
