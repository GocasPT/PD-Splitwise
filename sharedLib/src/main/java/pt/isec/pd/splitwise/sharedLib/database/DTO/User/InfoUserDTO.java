package pt.isec.pd.splitwise.sharedLib.database.DTO.User;

import java.io.Serializable;

public record InfoUserDTO(int id, String username, String email, String phoneNumber,
                          String password) implements Serializable {
	@Override
	public String toString() {
		return "InfoUserDTO{" +
		       "id: " + id +
		       ", username: '" + username + '\'' +
		       ", userEmail: '" + email + '\'' +
		       ", phoneNumber: '" + phoneNumber + '\'' +
		       ", password: '" + password + '\'' +
		       '}';
	}
}
