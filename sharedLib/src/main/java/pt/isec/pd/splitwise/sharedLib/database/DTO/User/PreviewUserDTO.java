package pt.isec.pd.splitwise.sharedLib.database.DTO.User;

import java.io.Serializable;

public record PreviewUserDTO(int id, String username, String email) implements Serializable {
	@Override
	public String toString() {
		return "PreviewUserDTO{" +
		       "id: " + id +
		       ", username: '" + username + '\'' +
		       ", userEmail: '" + email + '\'' +
		       '}';
	}
}
