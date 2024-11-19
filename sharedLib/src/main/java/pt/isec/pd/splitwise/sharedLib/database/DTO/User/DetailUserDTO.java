package pt.isec.pd.splitwise.sharedLib.database.DTO.User;

import java.io.Serializable;

public record DetailUserDTO(int id, String username, String email, String phoneNumber) implements Serializable {
	@Override
	public String toString() {
		return "DetailUserDTO{" +
		       "id: " + id +
		       ", username: '" + username + '\'' +
		       ", userEmail: '" + email + '\'' +
		       ", phoneNumber:'" + phoneNumber + '\'' +
		       '}';
	}
}
