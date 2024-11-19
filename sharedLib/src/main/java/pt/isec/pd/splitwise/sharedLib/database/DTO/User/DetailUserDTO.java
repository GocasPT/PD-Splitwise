package pt.isec.pd.splitwise.sharedLib.database.DTO.User;

import java.io.Serializable;

public record DetailUserDTO(int id, String username, String email, String phone_number) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "DetailUserDTO{" +
		       "id=" + id +
		       ", username='" + username + '\'' +
		       ", userEmail='" + email + '\'' +
		       ", phone_number='" + phone_number + '\'' +
		       '}';
	}
}
