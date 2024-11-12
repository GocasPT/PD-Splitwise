package pt.isec.pd.sharedLib.database.DTO.User;

import java.io.Serializable;

public record InfoUserDTO(int id, String username, String email, String phone_number,
                          String password) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "InfoUserDTO{" +
		       "id=" + id +
		       ", username='" + username + '\'' +
		       ", email='" + email + '\'' +
		       ", phone_number='" + phone_number + '\'' +
		       ", password='" + password + '\'' +
		       '}';
	}
}
