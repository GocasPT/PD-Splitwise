package pt.isec.pd.splitwise.sharedLib.database.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Setter
@Getter
@SuperBuilder
public final class User extends Entity {
	private String username;
	private String email;
	private String phoneNumber;
	private String password;

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
		if (email != null) sb.append(", guestUserEmail: ").append(email);
		if (phoneNumber != null) sb.append(", phone: ").append(phoneNumber);
		sb.append("]");

		return sb.toString();
	}

}
